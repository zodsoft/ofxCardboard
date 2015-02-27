/*      */ package com.google.vrtoolkit.cardboard;
/*      */ 
/*      */ import android.opengl.GLES20;
/*      */ import android.util.Log;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ByteOrder;
/*      */ import java.nio.FloatBuffer;
/*      */ import java.nio.IntBuffer;
/*      */ import java.nio.ShortBuffer;
/*      */ 
/*      */ public class DistortionRenderer
/*      */ {
/*      */   private static final String TAG = "DistortionRenderer";
/*      */   private int mTextureId;
/*      */   private int mRenderbufferId;
/*      */   private int mFramebufferId;
/*      */   private IntBuffer mOriginalFramebufferId;
/*      */   private int mTextureFormat;
/*      */   private int mTextureType;
/*      */   private float mResolutionScale;
/*      */   private boolean mRestoreGLStateEnabled;
/*      */   private boolean mChromaticAberrationCorrectionEnabled;
/*      */   private boolean mVignetteEnabled;
/*      */   private DistortionMesh mLeftEyeDistortionMesh;
/*      */   private DistortionMesh mRightEyeDistortionMesh;
/*      */   private GLStateBackup mGLStateBackup;
/*      */   private GLStateBackup mGLStateBackupAberration;
/*      */   private HeadMountedDisplay mHmd;
/*      */   private EyeViewport mLeftEyeViewport;
/*      */   private EyeViewport mRightEyeViewport;
/*      */   private boolean mFovsChanged;
/*      */   private boolean mViewportsChanged;
/*      */   private boolean mTextureFormatChanged;
/*      */   private boolean mDrawingFrame;
/*      */   private float mXPxPerTanAngle;
/*      */   private float mYPxPerTanAngle;
/*      */   private float mMetersPerTanAngle;
/*      */   private ProgramHolder mProgramHolder;
/*      */   private ProgramHolderAberration mProgramHolderAberration;
/*      */   static final String VERTEX_SHADER = "attribute vec2 aPosition;\nattribute float aVignette;\nattribute vec2 aBlueTextureCoord;\nvarying vec2 vTextureCoord;\nvarying float vVignette;\nuniform float uTextureCoordScale;\nvoid main() {\n    gl_Position = vec4(aPosition, 0.0, 1.0);\n    vTextureCoord = aBlueTextureCoord.xy * uTextureCoordScale;\n    vVignette = aVignette;\n}\n";
/*      */   static final String FRAGMENT_SHADER = "precision mediump float;\nvarying vec2 vTextureCoord;\nvarying float vVignette;\nuniform sampler2D uTextureSampler;\nvoid main() {\n    gl_FragColor = vVignette * texture2D(uTextureSampler, vTextureCoord);\n}\n";
/*      */   static final String VERTEX_SHADER_ABERRATION = "attribute vec2 aPosition;\nattribute float aVignette;\nattribute vec2 aRedTextureCoord;\nattribute vec2 aGreenTextureCoord;\nattribute vec2 aBlueTextureCoord;\nvarying vec2 vRedTextureCoord;\nvarying vec2 vBlueTextureCoord;\nvarying vec2 vGreenTextureCoord;\nvarying float vVignette;\nuniform float uTextureCoordScale;\nvoid main() {\n    gl_Position = vec4(aPosition, 0.0, 1.0);\n    vRedTextureCoord = aRedTextureCoord.xy * uTextureCoordScale;\n    vGreenTextureCoord = aGreenTextureCoord.xy * uTextureCoordScale;\n    vBlueTextureCoord = aBlueTextureCoord.xy * uTextureCoordScale;\n    vVignette = aVignette;\n}\n";
/*      */   static final String FRAGMENT_SHADER_ABERRATION = "precision mediump float;\nvarying vec2 vRedTextureCoord;\nvarying vec2 vBlueTextureCoord;\nvarying vec2 vGreenTextureCoord;\nvarying float vVignette;\nuniform sampler2D uTextureSampler;\nvoid main() {\n    gl_FragColor = vVignette * vec4(texture2D(uTextureSampler, vRedTextureCoord).r,\n                    texture2D(uTextureSampler, vGreenTextureCoord).g,\n                    texture2D(uTextureSampler, vBlueTextureCoord).b, 1.0);\n}\n";
/*      */ 
/*      */   public DistortionRenderer()
/*      */   {
/*   37 */     this.mTextureId = -1;
/*   38 */     this.mRenderbufferId = -1;
/*   39 */     this.mFramebufferId = -1;
/*   40 */     this.mOriginalFramebufferId = IntBuffer.allocate(1);
/*      */ 
/*   42 */     this.mTextureFormat = 6407;
/*   43 */     this.mTextureType = 5121;
/*      */ 
/*   45 */     this.mResolutionScale = 1.0F;
/*      */ 
/*   52 */     this.mGLStateBackup = new GLStateBackup();
/*   53 */     this.mGLStateBackupAberration = new GLStateBackup();
/*      */   }
/*      */ 
/*      */   public void setTextureFormat(int textureFormat, int textureType)
/*      */   {
/*  179 */     if (this.mDrawingFrame) {
/*  180 */       throw new IllegalStateException("Cannot change texture format during rendering.");
/*      */     }
/*      */ 
/*  183 */     if ((textureFormat != this.mTextureFormat) || (textureType != this.mTextureType)) {
/*  184 */       this.mTextureFormat = textureFormat;
/*  185 */       this.mTextureType = textureType;
/*  186 */       this.mTextureFormatChanged = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void beforeDrawFrame()
/*      */   {
/*  196 */     this.mDrawingFrame = true;
/*      */ 
/*  198 */     if ((this.mFovsChanged) || (this.mTextureFormatChanged)) {
/*  199 */       updateTextureAndDistortionMesh();
/*      */     }
/*      */ 
/*  203 */     GLES20.glGetIntegerv(36006, this.mOriginalFramebufferId);
/*  204 */     GLES20.glBindFramebuffer(36160, this.mFramebufferId);
/*      */   }
/*      */ 
/*      */   public void afterDrawFrame()
/*      */   {
/*  215 */     GLES20.glBindFramebuffer(36160, this.mOriginalFramebufferId.array()[0]);
/*  216 */     undistortTexture(this.mTextureId);
/*  217 */     this.mDrawingFrame = false;
/*      */   }
/*      */ 
/*      */   public void undistortTexture(int textureId)
/*      */   {
/*  226 */     if (this.mRestoreGLStateEnabled) {
/*  227 */       if (this.mChromaticAberrationCorrectionEnabled)
/*  228 */         this.mGLStateBackupAberration.readFromGL();
/*      */       else {
/*  230 */         this.mGLStateBackup.readFromGL();
/*      */       }
/*      */     }
/*      */ 
/*  234 */     if ((this.mFovsChanged) || (this.mTextureFormatChanged)) {
/*  235 */       updateTextureAndDistortionMesh();
/*      */     }
/*      */ 
/*  238 */     GLES20.glViewport(0, 0, this.mHmd.getScreenParams().getWidth(), this.mHmd.getScreenParams().getHeight());
/*      */ 
/*  240 */     GLES20.glDisable(3089);
/*  241 */     GLES20.glDisable(2884);
/*      */ 
/*  243 */     GLES20.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
/*  244 */     GLES20.glClear(16640);
/*      */ 
/*  246 */     if (this.mChromaticAberrationCorrectionEnabled)
/*  247 */       GLES20.glUseProgram(this.mProgramHolderAberration.program);
/*      */     else {
/*  249 */       GLES20.glUseProgram(this.mProgramHolder.program);
/*      */     }
/*      */ 
/*  252 */     GLES20.glEnable(3089);
/*  253 */     GLES20.glScissor(0, 0, this.mHmd.getScreenParams().getWidth() / 2, this.mHmd.getScreenParams().getHeight());
/*      */ 
/*  257 */     renderDistortionMesh(this.mLeftEyeDistortionMesh, textureId);
/*      */ 
/*  259 */     GLES20.glScissor(this.mHmd.getScreenParams().getWidth() / 2, 0, this.mHmd.getScreenParams().getWidth() / 2, this.mHmd.getScreenParams().getHeight());
/*      */ 
/*  263 */     renderDistortionMesh(this.mRightEyeDistortionMesh, textureId);
/*      */ 
/*  265 */     if (this.mRestoreGLStateEnabled)
/*  266 */       if (this.mChromaticAberrationCorrectionEnabled)
/*  267 */         this.mGLStateBackupAberration.writeToGL();
/*      */       else
/*  269 */         this.mGLStateBackup.writeToGL();
/*      */   }
/*      */ 
/*      */   public void setResolutionScale(float scale)
/*      */   {
/*  285 */     this.mResolutionScale = scale;
/*  286 */     this.mViewportsChanged = true;
/*      */   }
/*      */ 
/*      */   public void setRestoreGLStateEnabled(boolean enabled)
/*      */   {
/*  300 */     this.mRestoreGLStateEnabled = enabled;
/*      */   }
/*      */ 
/*      */   public void setChromaticAberrationCorrectionEnabled(boolean enabled)
/*      */   {
/*  315 */     this.mChromaticAberrationCorrectionEnabled = enabled;
/*      */   }
/*      */ 
/*      */   public void setVignetteEnabled(boolean enabled)
/*      */   {
/*  330 */     this.mVignetteEnabled = enabled;
/*  331 */     this.mFovsChanged = true;
/*      */   }
/*      */ 
/*      */   public void onFovChanged(HeadMountedDisplay hmd, FieldOfView leftFov, FieldOfView rightFov, float virtualEyeToScreenDistance)
/*      */   {
/*  357 */     if (this.mDrawingFrame) {
/*  358 */       throw new IllegalStateException("Cannot change FOV while rendering a frame.");
/*      */     }
/*      */ 
/*  361 */     this.mHmd = new HeadMountedDisplay(hmd);
/*      */ 
/*  363 */     this.mLeftEyeViewport = initViewportForEye(leftFov, 0.0F);
/*  364 */     this.mRightEyeViewport = initViewportForEye(rightFov, this.mLeftEyeViewport.width);
/*      */ 
/*  366 */     this.mMetersPerTanAngle = virtualEyeToScreenDistance;
/*  367 */     ScreenParams screen = this.mHmd.getScreenParams();
/*  368 */     this.mXPxPerTanAngle = (screen.getWidth() / (screen.getWidthMeters() / this.mMetersPerTanAngle));
/*      */ 
/*  370 */     this.mYPxPerTanAngle = (screen.getHeight() / (screen.getHeightMeters() / this.mMetersPerTanAngle));
/*      */ 
/*  373 */     this.mFovsChanged = true;
/*  374 */     this.mViewportsChanged = true;
/*      */   }
/*      */ 
/*      */   public boolean haveViewportsChanged()
/*      */   {
/*  384 */     return this.mViewportsChanged;
/*      */   }
/*      */ 
/*      */   public void updateViewports(Viewport leftViewport, Viewport rightViewport)
/*      */   {
/*  394 */     leftViewport.setViewport(Math.round(this.mLeftEyeViewport.x * this.mXPxPerTanAngle * this.mResolutionScale), Math.round(this.mLeftEyeViewport.y * this.mYPxPerTanAngle * this.mResolutionScale), Math.round(this.mLeftEyeViewport.width * this.mXPxPerTanAngle * this.mResolutionScale), Math.round(this.mLeftEyeViewport.height * this.mYPxPerTanAngle * this.mResolutionScale));
/*      */ 
/*  400 */     rightViewport.setViewport(Math.round(this.mRightEyeViewport.x * this.mXPxPerTanAngle * this.mResolutionScale), Math.round(this.mRightEyeViewport.y * this.mYPxPerTanAngle * this.mResolutionScale), Math.round(this.mRightEyeViewport.width * this.mXPxPerTanAngle * this.mResolutionScale), Math.round(this.mRightEyeViewport.height * this.mYPxPerTanAngle * this.mResolutionScale));
/*      */ 
/*  406 */     this.mViewportsChanged = false;
/*      */   }
/*      */ 
/*      */   private void updateTextureAndDistortionMesh() {
/*  410 */     ScreenParams screen = this.mHmd.getScreenParams();
/*  411 */     CardboardDeviceParams cdp = this.mHmd.getCardboardDeviceParams();
/*      */ 
/*  413 */     if (this.mProgramHolder == null) {
/*  414 */       this.mProgramHolder = createProgramHolder();
/*      */     }
/*      */ 
/*  417 */     if (this.mProgramHolderAberration == null) {
/*  418 */       this.mProgramHolderAberration = ((ProgramHolderAberration)createProgramHolder(true));
/*      */     }
/*      */ 
/*  422 */     float textureWidthTanAngle = this.mLeftEyeViewport.width + this.mRightEyeViewport.width;
/*  423 */     float textureHeightTanAngle = Math.max(this.mLeftEyeViewport.height, this.mRightEyeViewport.height);
/*      */ 
/*  426 */     int[] maxTextureSize = new int[1];
/*  427 */     GLES20.glGetIntegerv(3379, maxTextureSize, 0);
/*      */ 
/*  429 */     int textureWidthPx = Math.min(Math.round(textureWidthTanAngle * this.mXPxPerTanAngle), maxTextureSize[0]);
/*      */ 
/*  431 */     int textureHeightPx = Math.min(Math.round(textureHeightTanAngle * this.mYPxPerTanAngle), maxTextureSize[0]);
/*      */ 
/*  434 */     float xEyeOffsetTanAngleScreen = (screen.getWidthMeters() / 2.0F - cdp.getInterLensDistance() / 2.0F) / this.mMetersPerTanAngle;
/*      */ 
/*  437 */     float yEyeOffsetTanAngleScreen = (cdp.getVerticalDistanceToLensCenter() - screen.getBorderSizeMeters()) / this.mMetersPerTanAngle;
/*      */ 
/*  441 */     this.mLeftEyeDistortionMesh = createDistortionMesh(this.mLeftEyeViewport, textureWidthTanAngle, textureHeightTanAngle, xEyeOffsetTanAngleScreen, yEyeOffsetTanAngleScreen);
/*      */ 
/*  444 */     xEyeOffsetTanAngleScreen = screen.getWidthMeters() / this.mMetersPerTanAngle - xEyeOffsetTanAngleScreen;
/*      */ 
/*  446 */     this.mRightEyeDistortionMesh = createDistortionMesh(this.mRightEyeViewport, textureWidthTanAngle, textureHeightTanAngle, xEyeOffsetTanAngleScreen, yEyeOffsetTanAngleScreen);
/*      */ 
/*  450 */     setupRenderTextureAndRenderbuffer(textureWidthPx, textureHeightPx);
/*      */ 
/*  452 */     this.mFovsChanged = false;
/*      */   }
/*      */ 
/*      */   private EyeViewport initViewportForEye(FieldOfView fov, float xOffset)
/*      */   {
/*  457 */     float left = (float)Math.tan(Math.toRadians(fov.getLeft()));
/*  458 */     float right = (float)Math.tan(Math.toRadians(fov.getRight()));
/*  459 */     float bottom = (float)Math.tan(Math.toRadians(fov.getBottom()));
/*  460 */     float top = (float)Math.tan(Math.toRadians(fov.getTop()));
/*      */ 
/*  462 */     EyeViewport vp = new EyeViewport(null);
/*  463 */     vp.x = xOffset;
/*  464 */     vp.y = 0.0F;
/*  465 */     vp.width = (left + right);
/*  466 */     vp.height = (bottom + top);
/*  467 */     vp.eyeX = (left + xOffset);
/*  468 */     vp.eyeY = bottom;
/*      */ 
/*  470 */     return vp;
/*      */   }
/*      */ 
/*      */   private DistortionMesh createDistortionMesh(EyeViewport eyeViewport, float textureWidthTanAngle, float textureHeightTanAngle, float xEyeOffsetTanAngleScreen, float yEyeOffsetTanAngleScreen)
/*      */   {
/*  479 */     return new DistortionMesh(this.mHmd.getCardboardDeviceParams().getDistortion(), this.mHmd.getCardboardDeviceParams().getDistortion(), this.mHmd.getCardboardDeviceParams().getDistortion(), this.mHmd.getScreenParams().getWidthMeters() / this.mMetersPerTanAngle, this.mHmd.getScreenParams().getHeightMeters() / this.mMetersPerTanAngle, xEyeOffsetTanAngleScreen, yEyeOffsetTanAngleScreen, textureWidthTanAngle, textureHeightTanAngle, eyeViewport.eyeX, eyeViewport.eyeY, eyeViewport.x, eyeViewport.y, eyeViewport.width, eyeViewport.height);
/*      */   }
/*      */ 
/*      */   private void renderDistortionMesh(DistortionMesh mesh, int textureId)
/*      */   {
/*      */     ProgramHolder holder;
/*      */     ProgramHolder holder;
/*  495 */     if (this.mChromaticAberrationCorrectionEnabled)
/*  496 */       holder = this.mProgramHolderAberration;
/*      */     else {
/*  498 */       holder = this.mProgramHolder;
/*      */     }
/*      */ 
/*  501 */     GLES20.glBindBuffer(34962, mesh.mArrayBufferId);
/*  502 */     GLES20.glVertexAttribPointer(holder.aPosition, 2, 5126, false, 36, 0 * 4);
/*      */ 
/*  509 */     GLES20.glEnableVertexAttribArray(holder.aPosition);
/*      */ 
/*  511 */     GLES20.glVertexAttribPointer(holder.aVignette, 1, 5126, false, 36, 2 * 4);
/*      */ 
/*  518 */     GLES20.glEnableVertexAttribArray(holder.aVignette);
/*      */ 
/*  520 */     GLES20.glVertexAttribPointer(holder.aBlueTextureCoord, 2, 5126, false, 36, 7 * 4);
/*      */ 
/*  527 */     GLES20.glEnableVertexAttribArray(holder.aBlueTextureCoord);
/*      */ 
/*  529 */     if (this.mChromaticAberrationCorrectionEnabled) {
/*  530 */       GLES20.glVertexAttribPointer(((ProgramHolderAberration)holder).aRedTextureCoord, 2, 5126, false, 36, 3 * 4);
/*      */ 
/*  537 */       GLES20.glEnableVertexAttribArray(((ProgramHolderAberration)holder).aRedTextureCoord);
/*      */ 
/*  539 */       GLES20.glVertexAttribPointer(((ProgramHolderAberration)holder).aGreenTextureCoord, 2, 5126, false, 36, 5 * 4);
/*      */ 
/*  546 */       GLES20.glEnableVertexAttribArray(((ProgramHolderAberration)holder).aGreenTextureCoord);
/*      */     }
/*      */ 
/*  549 */     GLES20.glActiveTexture(33984);
/*  550 */     GLES20.glBindTexture(3553, textureId);
/*  551 */     GLES20.glUniform1i(this.mProgramHolder.uTextureSampler, 0);
/*  552 */     GLES20.glUniform1f(this.mProgramHolder.uTextureCoordScale, this.mResolutionScale);
/*      */ 
/*  555 */     GLES20.glBindBuffer(34963, mesh.mElementBufferId);
/*  556 */     GLES20.glDrawElements(5, mesh.nIndices, 5123, 0);
/*      */   }
/*      */ 
/*      */   private float computeDistortionScale(Distortion distortion, float screenWidthM, float interpupillaryDistanceM)
/*      */   {
/*  566 */     return distortion.distortionFactor((screenWidthM / 2.0F - interpupillaryDistanceM / 2.0F) / (screenWidthM / 4.0F));
/*      */   }
/*      */ 
/*      */   private int createTexture(int width, int height, int textureFormat, int textureType)
/*      */   {
/*  573 */     int[] textureIds = new int[1];
/*  574 */     GLES20.glGenTextures(1, textureIds, 0);
/*      */ 
/*  576 */     GLES20.glBindTexture(3553, textureIds[0]);
/*  577 */     GLES20.glTexParameteri(3553, 10242, 33071);
/*      */ 
/*  579 */     GLES20.glTexParameteri(3553, 10243, 33071);
/*      */ 
/*  581 */     GLES20.glTexParameteri(3553, 10240, 9729);
/*      */ 
/*  583 */     GLES20.glTexParameteri(3553, 10241, 9729);
/*      */ 
/*  587 */     GLES20.glTexImage2D(3553, 0, textureFormat, width, height, 0, textureFormat, textureType, null);
/*      */ 
/*  598 */     return textureIds[0];
/*      */   }
/*      */ 
/*      */   private int setupRenderTextureAndRenderbuffer(int width, int height)
/*      */   {
/*  603 */     if (this.mTextureId != -1) {
/*  604 */       GLES20.glDeleteTextures(1, new int[] { this.mTextureId }, 0);
/*      */     }
/*  606 */     if (this.mRenderbufferId != -1) {
/*  607 */       GLES20.glDeleteRenderbuffers(1, new int[] { this.mRenderbufferId }, 0);
/*      */     }
/*  609 */     if (this.mFramebufferId != -1) {
/*  610 */       GLES20.glDeleteFramebuffers(1, new int[] { this.mFramebufferId }, 0);
/*      */     }
/*      */ 
/*  613 */     this.mTextureId = createTexture(width, height, this.mTextureFormat, this.mTextureType);
/*  614 */     this.mTextureFormatChanged = false;
/*      */ 
/*  616 */     checkGlError("setupRenderTextureAndRenderbuffer: create texture");
/*      */ 
/*  619 */     int[] renderbufferIds = new int[1];
/*  620 */     GLES20.glGenRenderbuffers(1, renderbufferIds, 0);
/*  621 */     GLES20.glBindRenderbuffer(36161, renderbufferIds[0]);
/*  622 */     GLES20.glRenderbufferStorage(36161, 33189, width, height);
/*      */ 
/*  624 */     this.mRenderbufferId = renderbufferIds[0];
/*  625 */     checkGlError("setupRenderTextureAndRenderbuffer: create renderbuffer");
/*      */ 
/*  627 */     int[] framebufferIds = new int[1];
/*  628 */     GLES20.glGenFramebuffers(1, framebufferIds, 0);
/*  629 */     GLES20.glBindFramebuffer(36160, framebufferIds[0]);
/*  630 */     this.mFramebufferId = framebufferIds[0];
/*      */ 
/*  632 */     GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.mTextureId, 0);
/*      */ 
/*  635 */     GLES20.glFramebufferRenderbuffer(36160, 36096, 36161, renderbufferIds[0]);
/*      */ 
/*  639 */     int status = GLES20.glCheckFramebufferStatus(36160);
/*      */ 
/*  641 */     if (status != 36053)
/*      */     {
/*      */       RuntimeException tmp211_208 = new java/lang/RuntimeException; tmp211_208; new String(tmp211_208);
/*      */     }
/*      */   }
/*      */ 
/*      */   private int loadShader(int shaderType, String source)
/*      */   {
/*  653 */     int shader = GLES20.glCreateShader(shaderType);
/*  654 */     if (shader != 0) {
/*  655 */       GLES20.glShaderSource(shader, source);
/*  656 */       GLES20.glCompileShader(shader);
/*  657 */       int[] compiled = new int[1];
/*  658 */       GLES20.glGetShaderiv(shader, 35713, compiled, 0);
/*  659 */       if (compiled[0] == 0) {
/*  660 */         int i = shaderType; Log.e("DistortionRenderer", 37 + "Could not compile shader " + i + ":");
/*  661 */         Log.e("DistortionRenderer", GLES20.glGetShaderInfoLog(shader));
/*  662 */         GLES20.glDeleteShader(shader);
/*  663 */         shader = 0;
/*      */       }
/*      */     }
/*  666 */     return shader;
/*      */   }
/*      */ 
/*      */   private int createProgram(String vertexSource, String fragmentSource) {
/*  670 */     int vertexShader = loadShader(35633, vertexSource);
/*  671 */     if (vertexShader == 0) {
/*  672 */       return 0;
/*      */     }
/*  674 */     int pixelShader = loadShader(35632, fragmentSource);
/*  675 */     if (pixelShader == 0) {
/*  676 */       return 0;
/*      */     }
/*      */ 
/*  679 */     int program = GLES20.glCreateProgram();
/*  680 */     if (program != 0) {
/*  681 */       GLES20.glAttachShader(program, vertexShader);
/*  682 */       checkGlError("glAttachShader");
/*  683 */       GLES20.glAttachShader(program, pixelShader);
/*  684 */       checkGlError("glAttachShader");
/*  685 */       GLES20.glLinkProgram(program);
/*  686 */       int[] linkStatus = new int[1];
/*  687 */       GLES20.glGetProgramiv(program, 35714, linkStatus, 0);
/*  688 */       if (linkStatus[0] != 1) {
/*  689 */         Log.e("DistortionRenderer", "Could not link program: ");
/*  690 */         Log.e("DistortionRenderer", GLES20.glGetProgramInfoLog(program));
/*  691 */         GLES20.glDeleteProgram(program);
/*  692 */         program = 0;
/*      */       }
/*      */     }
/*  695 */     return program;
/*      */   }
/*      */ 
/*      */   private ProgramHolder createProgramHolder() {
/*  699 */     return createProgramHolder(false);
/*      */   }
/*      */ 
/*      */   private ProgramHolder createProgramHolder(boolean aberrationCorrected)
/*      */   {
/*      */     GLStateBackup state;
/*      */     ProgramHolder holder;
/*      */     GLStateBackup state;
/*  705 */     if (aberrationCorrected) {
/*  706 */       ProgramHolder holder = new ProgramHolderAberration(null);
/*      */ 
/*  708 */       holder.program = createProgram("attribute vec2 aPosition;\nattribute float aVignette;\nattribute vec2 aRedTextureCoord;\nattribute vec2 aGreenTextureCoord;\nattribute vec2 aBlueTextureCoord;\nvarying vec2 vRedTextureCoord;\nvarying vec2 vBlueTextureCoord;\nvarying vec2 vGreenTextureCoord;\nvarying float vVignette;\nuniform float uTextureCoordScale;\nvoid main() {\n    gl_Position = vec4(aPosition, 0.0, 1.0);\n    vRedTextureCoord = aRedTextureCoord.xy * uTextureCoordScale;\n    vGreenTextureCoord = aGreenTextureCoord.xy * uTextureCoordScale;\n    vBlueTextureCoord = aBlueTextureCoord.xy * uTextureCoordScale;\n    vVignette = aVignette;\n}\n", "precision mediump float;\nvarying vec2 vRedTextureCoord;\nvarying vec2 vBlueTextureCoord;\nvarying vec2 vGreenTextureCoord;\nvarying float vVignette;\nuniform sampler2D uTextureSampler;\nvoid main() {\n    gl_FragColor = vVignette * vec4(texture2D(uTextureSampler, vRedTextureCoord).r,\n                    texture2D(uTextureSampler, vGreenTextureCoord).g,\n                    texture2D(uTextureSampler, vBlueTextureCoord).b, 1.0);\n}\n");
/*  709 */       if (holder.program == 0) {
/*  710 */         throw new RuntimeException("Could not create aberration-corrected program");
/*      */       }
/*      */ 
/*  713 */       state = this.mGLStateBackupAberration;
/*      */     } else {
/*  715 */       holder = new ProgramHolder(null);
/*      */ 
/*  717 */       holder.program = createProgram("attribute vec2 aPosition;\nattribute float aVignette;\nattribute vec2 aBlueTextureCoord;\nvarying vec2 vTextureCoord;\nvarying float vVignette;\nuniform float uTextureCoordScale;\nvoid main() {\n    gl_Position = vec4(aPosition, 0.0, 1.0);\n    vTextureCoord = aBlueTextureCoord.xy * uTextureCoordScale;\n    vVignette = aVignette;\n}\n", "precision mediump float;\nvarying vec2 vTextureCoord;\nvarying float vVignette;\nuniform sampler2D uTextureSampler;\nvoid main() {\n    gl_FragColor = vVignette * texture2D(uTextureSampler, vTextureCoord);\n}\n");
/*  718 */       if (holder.program == 0) {
/*  719 */         throw new RuntimeException("Could not create program");
/*      */       }
/*      */ 
/*  722 */       state = this.mGLStateBackup;
/*      */     }
/*      */ 
/*  725 */     holder.aPosition = GLES20.glGetAttribLocation(holder.program, "aPosition");
/*  726 */     checkGlError("glGetAttribLocation aPosition");
/*  727 */     if (holder.aPosition == -1) {
/*  728 */       throw new RuntimeException("Could not get attrib location for aPosition");
/*      */     }
/*  730 */     state.addTrackedVertexAttribute(holder.aPosition);
/*  731 */     holder.aVignette = GLES20.glGetAttribLocation(holder.program, "aVignette");
/*  732 */     checkGlError("glGetAttribLocation aVignette");
/*  733 */     if (holder.aVignette == -1) {
/*  734 */       throw new RuntimeException("Could not get attrib location for aVignette");
/*      */     }
/*  736 */     state.addTrackedVertexAttribute(holder.aVignette);
/*      */ 
/*  738 */     if (aberrationCorrected) {
/*  739 */       ((ProgramHolderAberration)holder).aRedTextureCoord = GLES20.glGetAttribLocation(holder.program, "aRedTextureCoord");
/*      */ 
/*  741 */       checkGlError("glGetAttribLocation aRedTextureCoord");
/*  742 */       if (((ProgramHolderAberration)holder).aRedTextureCoord == -1) {
/*  743 */         throw new RuntimeException("Could not get attrib location for aRedTextureCoord");
/*      */       }
/*  745 */       ((ProgramHolderAberration)holder).aGreenTextureCoord = GLES20.glGetAttribLocation(holder.program, "aGreenTextureCoord");
/*      */ 
/*  747 */       checkGlError("glGetAttribLocation aGreenTextureCoord");
/*  748 */       if (((ProgramHolderAberration)holder).aGreenTextureCoord == -1) {
/*  749 */         throw new RuntimeException("Could not get attrib location for aGreenTextureCoord");
/*      */       }
/*  751 */       state.addTrackedVertexAttribute(((ProgramHolderAberration)holder).aRedTextureCoord);
/*  752 */       state.addTrackedVertexAttribute(((ProgramHolderAberration)holder).aGreenTextureCoord);
/*      */     }
/*      */ 
/*  755 */     holder.aBlueTextureCoord = GLES20.glGetAttribLocation(holder.program, "aBlueTextureCoord");
/*      */ 
/*  757 */     checkGlError("glGetAttribLocation aBlueTextureCoord");
/*  758 */     if (holder.aBlueTextureCoord == -1) {
/*  759 */       throw new RuntimeException("Could not get attrib location for aBlueTextureCoord");
/*      */     }
/*  761 */     state.addTrackedVertexAttribute(holder.aBlueTextureCoord);
/*  762 */     holder.uTextureCoordScale = GLES20.glGetUniformLocation(holder.program, "uTextureCoordScale");
/*      */ 
/*  764 */     checkGlError("glGetUniformLocation uTextureCoordScale");
/*  765 */     if (holder.uTextureCoordScale == -1) {
/*  766 */       throw new RuntimeException("Could not get attrib location for uTextureCoordScale");
/*      */     }
/*  768 */     holder.uTextureSampler = GLES20.glGetUniformLocation(holder.program, "uTextureSampler");
/*      */ 
/*  770 */     checkGlError("glGetUniformLocation uTextureSampler");
/*  771 */     if (holder.uTextureSampler == -1) {
/*  772 */       throw new RuntimeException("Could not get attrib location for uTextureSampler");
/*      */     }
/*      */ 
/*  775 */     return holder;
/*      */   }
/*      */ 
/*      */   private void checkGlError(String op)
/*      */   {
/*      */     int error;
/*  780 */     if ((error = GLES20.glGetError()) != 0) {
/*  781 */       String str1 = String.valueOf(String.valueOf(op)); int i = error; Log.e("DistortionRenderer", 21 + str1.length() + str1 + ": glError " + i);
/*  782 */       String str2 = String.valueOf(String.valueOf(op)); int j = error; throw new RuntimeException(21 + str2.length() + str2 + ": glError " + j);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static float clamp(float val, float min, float max) {
/*  787 */     return Math.max(min, Math.min(max, val));
/*      */   }
/*      */ 
/*      */   private class DistortionMesh
/*      */   {
/*      */     private static final String TAG = "DistortionMesh";
/*      */     public static final int BYTES_PER_FLOAT = 4;
/*      */     public static final int BYTES_PER_SHORT = 2;
/*      */     public static final int COMPONENTS_PER_VERT = 9;
/*      */     public static final int DATA_STRIDE_BYTES = 36;
/*      */     public static final int DATA_POS_OFFSET = 0;
/*      */     public static final int DATA_POS_COMPONENTS = 2;
/*      */     public static final int DATA_VIGNETTE_OFFSET = 2;
/*      */     public static final int DATA_VIGNETTE_COMPONENTS = 1;
/*      */     public static final int DATA_RUV_OFFSET = 3;
/*      */     public static final int DATA_GUV_OFFSET = 5;
/*      */     public static final int DATA_BUV_OFFSET = 7;
/*      */     public static final int DATA_UV_COMPONENTS = 2;
/*      */     public static final int ROWS = 40;
/*      */     public static final int COLS = 40;
/*      */     public static final float VIGNETTE_SIZE_TAN_ANGLE = 0.05F;
/*      */     public int nIndices;
/*  819 */     public int mArrayBufferId = -1;
/*  820 */     public int mElementBufferId = -1;
/*      */ 
/*      */     public DistortionMesh(Distortion distortionRed, Distortion distortionGreen, Distortion distortionBlue, float screenWidth, float screenHeight, float xEyeOffsetScreen, float yEyeOffsetScreen, float textureWidth, float textureHeight, float xEyeOffsetTexture, float yEyeOffsetTexture, float viewportXTexture, float viewportYTexture, float viewportWidthTexture, float viewportHeightTexture)
/*      */     {
/*  832 */       float[] vertexData = new float[14400];
/*  833 */       short vertexOffset = 0;
/*      */ 
/*  845 */       for (int row = 0; row < 40; row++) {
/*  846 */         for (int col = 0; col < 40; col++)
/*      */         {
/*  853 */           float uTextureBlue = col / 39.0F * (viewportWidthTexture / textureWidth) + viewportXTexture / textureWidth;
/*      */ 
/*  856 */           float vTextureBlue = row / 39.0F * (viewportHeightTexture / textureHeight) + viewportYTexture / textureHeight;
/*      */ 
/*  861 */           float xTexture = uTextureBlue * textureWidth - xEyeOffsetTexture;
/*  862 */           float yTexture = vTextureBlue * textureHeight - yEyeOffsetTexture;
/*  863 */           float rTexture = (float)Math.sqrt(xTexture * xTexture + yTexture * yTexture);
/*      */ 
/*  869 */           float textureToScreenBlue = rTexture > 0.0F ? distortionBlue.distortInverse(rTexture) / rTexture : 1.0F;
/*      */ 
/*  874 */           float xScreen = xTexture * textureToScreenBlue;
/*  875 */           float yScreen = yTexture * textureToScreenBlue;
/*      */ 
/*  877 */           float uScreen = (xScreen + xEyeOffsetScreen) / screenWidth;
/*  878 */           float vScreen = (yScreen + yEyeOffsetScreen) / screenHeight;
/*      */ 
/*  881 */           float rScreen = rTexture * textureToScreenBlue;
/*      */ 
/*  885 */           float screenToTextureGreen = rScreen > 0.0F ? distortionGreen.distortionFactor(rScreen) : 1.0F;
/*      */ 
/*  889 */           float uTextureGreen = (xScreen * screenToTextureGreen + xEyeOffsetTexture) / textureWidth;
/*      */ 
/*  891 */           float vTextureGreen = (yScreen * screenToTextureGreen + yEyeOffsetTexture) / textureHeight;
/*      */ 
/*  894 */           float screenToTextureRed = rScreen > 0.0F ? distortionRed.distortionFactor(rScreen) : 1.0F;
/*      */ 
/*  898 */           float uTextureRed = (xScreen * screenToTextureRed + xEyeOffsetTexture) / textureWidth;
/*      */ 
/*  900 */           float vTextureRed = (yScreen * screenToTextureRed + yEyeOffsetTexture) / textureHeight;
/*      */ 
/*  904 */           float vignetteSizeTexture = 0.05F / textureToScreenBlue;
/*  905 */           float dxTexture = xTexture + xEyeOffsetTexture - DistortionRenderer.clamp(xTexture + xEyeOffsetTexture, viewportXTexture + vignetteSizeTexture, viewportXTexture + viewportWidthTexture - vignetteSizeTexture);
/*      */ 
/*  909 */           float dyTexture = yTexture + yEyeOffsetTexture - DistortionRenderer.clamp(yTexture + yEyeOffsetTexture, viewportYTexture + vignetteSizeTexture, viewportYTexture + viewportHeightTexture - vignetteSizeTexture);
/*      */ 
/*  913 */           float drTexture = (float)Math.sqrt(dxTexture * dxTexture + dyTexture * dyTexture);
/*      */           float vignette;
/*      */           float vignette;
/*  916 */           if (DistortionRenderer.this.mVignetteEnabled)
/*  917 */             vignette = 1.0F - DistortionRenderer.clamp(drTexture / vignetteSizeTexture, 0.0F, 1.0F);
/*      */           else {
/*  919 */             vignette = 1.0F;
/*      */           }
/*      */ 
/*  922 */           vertexData[(vertexOffset + 0)] = (2.0F * uScreen - 1.0F);
/*  923 */           vertexData[(vertexOffset + 1)] = (2.0F * vScreen - 1.0F);
/*  924 */           vertexData[(vertexOffset + 2)] = vignette;
/*  925 */           vertexData[(vertexOffset + 3)] = uTextureRed;
/*  926 */           vertexData[(vertexOffset + 4)] = vTextureRed;
/*  927 */           vertexData[(vertexOffset + 5)] = uTextureGreen;
/*  928 */           vertexData[(vertexOffset + 6)] = vTextureGreen;
/*  929 */           vertexData[(vertexOffset + 7)] = uTextureBlue;
/*  930 */           vertexData[(vertexOffset + 8)] = vTextureBlue;
/*      */ 
/*  932 */           vertexOffset = (short)(vertexOffset + 9);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  960 */       this.nIndices = 3158;
/*  961 */       short[] indexData = new short[this.nIndices];
/*  962 */       short indexOffset = 0;
/*  963 */       vertexOffset = 0;
/*  964 */       for (int row = 0; row < 39; row++) {
/*  965 */         if (row > 0) {
/*  966 */           indexData[indexOffset] = indexData[(indexOffset - 1)];
/*  967 */           indexOffset = (short)(indexOffset + 1);
/*      */         }
/*  969 */         for (int col = 0; col < 40; col++) {
/*  970 */           if (col > 0) {
/*  971 */             if (row % 2 == 0)
/*      */             {
/*  973 */               vertexOffset = (short)(vertexOffset + 1);
/*      */             }
/*      */             else {
/*  976 */               vertexOffset = (short)(vertexOffset - 1);
/*      */             }
/*      */           }
/*  979 */           indexOffset = (short)(indexOffset + 1); indexData[indexOffset] = vertexOffset;
/*  980 */           indexOffset = (short)(indexOffset + 1); indexData[indexOffset] = ((short)(vertexOffset + 40));
/*      */         }
/*  982 */         vertexOffset = (short)(vertexOffset + 40);
/*      */       }
/*      */ 
/*  985 */       FloatBuffer vertexBuffer = ByteBuffer.allocateDirect(vertexData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
/*      */ 
/*  988 */       vertexBuffer.put(vertexData).position(0);
/*      */ 
/*  990 */       ShortBuffer indexBuffer = ByteBuffer.allocateDirect(indexData.length * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
/*      */ 
/*  993 */       indexBuffer.put(indexData).position(0);
/*      */ 
/*  995 */       int[] bufferIds = new int[2];
/*  996 */       GLES20.glGenBuffers(2, bufferIds, 0);
/*  997 */       this.mArrayBufferId = bufferIds[0];
/*  998 */       this.mElementBufferId = bufferIds[1];
/*      */ 
/* 1000 */       GLES20.glBindBuffer(34962, this.mArrayBufferId);
/* 1001 */       GLES20.glBufferData(34962, vertexData.length * 4, vertexBuffer, 35044);
/*      */ 
/* 1004 */       GLES20.glBindBuffer(34963, this.mElementBufferId);
/* 1005 */       GLES20.glBufferData(34963, indexData.length * 2, indexBuffer, 35044);
/*      */ 
/* 1008 */       GLES20.glBindBuffer(34962, 0);
/* 1009 */       GLES20.glBindBuffer(34963, 0);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class EyeViewport
/*      */   {
/*      */     public float x;
/*      */     public float y;
/*      */     public float width;
/*      */     public float height;
/*      */     public float eyeX;
/*      */     public float eyeY;
/*      */ 
/*      */     private EyeViewport()
/*      */     {
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/*  156 */       float f1 = this.x; float f2 = this.y; float f3 = this.width; float f4 = this.height; float f5 = this.eyeX; float f6 = this.eyeY; return "{\n" + new StringBuilder(22).append("  x: ").append(f1).append(",\n").toString() + new StringBuilder(22).append("  y: ").append(f2).append(",\n").toString() + new StringBuilder(26).append("  width: ").append(f3).append(",\n").toString() + new StringBuilder(27).append("  height: ").append(f4).append(",\n").toString() + new StringBuilder(25).append("  eyeX: ").append(f5).append(",\n").toString() + new StringBuilder(25).append("  eyeY: ").append(f6).append(",\n").toString() + "}";
/*      */     }
/*      */   }
/*      */ 
/*      */   private class ProgramHolderAberration extends DistortionRenderer.ProgramHolder
/*      */   {
/*      */     public int aRedTextureCoord;
/*      */     public int aGreenTextureCoord;
/*      */ 
/*      */     private ProgramHolderAberration()
/*      */     {
/*  134 */       super(null);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class ProgramHolder
/*      */   {
/*      */     public int program;
/*      */     public int aPosition;
/*      */     public int aVignette;
/*      */     public int aBlueTextureCoord;
/*      */     public int uTextureCoordScale;
/*      */     public int uTextureSampler;
/*      */ 
/*      */     private ProgramHolder()
/*      */     {
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.DistortionRenderer
 * JD-Core Version:    0.6.2
 */