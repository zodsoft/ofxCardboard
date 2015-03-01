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
/*      */   private int textureId;
/*      */   private int renderbufferId;
/*      */   private int framebufferId;
/*      */   private IntBuffer originalFramebufferId;
/*      */   private int textureFormat;
/*      */   private int textureType;
/*      */   private float resolutionScale;
/*      */   private boolean restoreGLStateEnabled;
/*      */   private boolean chromaticAberrationCorrectionEnabled;
/*      */   private boolean vignetteEnabled;
/*      */   private DistortionMesh leftEyeDistortionMesh;
/*      */   private DistortionMesh rightEyeDistortionMesh;
/*      */   private GLStateBackup gLStateBackup;
/*      */   private GLStateBackup gLStateBackupAberration;
/*      */   private HeadMountedDisplay hmd;
/*      */   private EyeViewport leftEyeViewport;
/*      */   private EyeViewport rightEyeViewport;
/*      */   private boolean fovsChanged;
/*      */   private boolean viewportsChanged;
/*      */   private boolean textureFormatChanged;
/*      */   private boolean drawingFrame;
/*      */   private float xPxPerTanAngle;
/*      */   private float yPxPerTanAngle;
/*      */   private float metersPerTanAngle;
/*      */   private ProgramHolder programHolder;
/*      */   private ProgramHolderAberration programHolderAberration;
/*      */   static final String VERTEX_SHADER = "attribute vec2 aPosition;\nattribute float aVignette;\nattribute vec2 aBlueTextureCoord;\nvarying vec2 vTextureCoord;\nvarying float vVignette;\nuniform float uTextureCoordScale;\nvoid main() {\n  gl_Position = vec4(aPosition, 0.0, 1.0);\n  vTextureCoord = aBlueTextureCoord.xy * uTextureCoordScale;\n  vVignette = aVignette;\n}\n";
/*      */   static final String FRAGMENT_SHADER = "precision mediump float;\nvarying vec2 vTextureCoord;\nvarying float vVignette;\nuniform sampler2D uTextureSampler;\nvoid main() {\n  gl_FragColor = vVignette * texture2D(uTextureSampler, vTextureCoord);\n}\n";
/*      */   static final String VERTEX_SHADER_ABERRATION = "attribute vec2 aPosition;\nattribute float aVignette;\nattribute vec2 aRedTextureCoord;\nattribute vec2 aGreenTextureCoord;\nattribute vec2 aBlueTextureCoord;\nvarying vec2 vRedTextureCoord;\nvarying vec2 vBlueTextureCoord;\nvarying vec2 vGreenTextureCoord;\nvarying float vVignette;\nuniform float uTextureCoordScale;\nvoid main() {\n  gl_Position = vec4(aPosition, 0.0, 1.0);\n  vRedTextureCoord = aRedTextureCoord.xy * uTextureCoordScale;\n  vGreenTextureCoord = aGreenTextureCoord.xy * uTextureCoordScale;\n  vBlueTextureCoord = aBlueTextureCoord.xy * uTextureCoordScale;\n  vVignette = aVignette;\n}\n";
/*      */   static final String FRAGMENT_SHADER_ABERRATION = "precision mediump float;\nvarying vec2 vRedTextureCoord;\nvarying vec2 vBlueTextureCoord;\nvarying vec2 vGreenTextureCoord;\nvarying float vVignette;\nuniform sampler2D uTextureSampler;\nvoid main() {\n  gl_FragColor = vVignette * vec4(texture2D(uTextureSampler, vRedTextureCoord).r,\n          texture2D(uTextureSampler, vGreenTextureCoord).g,\n          texture2D(uTextureSampler, vBlueTextureCoord).b, 1.0);\n}\n";
/*      */ 
/*      */   public DistortionRenderer()
/*      */   {
/*   37 */     this.textureId = -1;
/*   38 */     this.renderbufferId = -1;
/*   39 */     this.framebufferId = -1;
/*   40 */     this.originalFramebufferId = IntBuffer.allocate(1);
/*      */ 
/*   42 */     this.textureFormat = 6407;
/*   43 */     this.textureType = 5121;
/*      */ 
/*   45 */     this.resolutionScale = 1.0F;
/*      */ 
/*   52 */     this.gLStateBackup = new GLStateBackup();
/*   53 */     this.gLStateBackupAberration = new GLStateBackup();
/*      */   }
/*      */ 
/*      */   public void setTextureFormat(int textureFormat, int textureType)
/*      */   {
/*  179 */     if (this.drawingFrame) {
/*  180 */       throw new IllegalStateException("Cannot change texture format during rendering.");
/*      */     }
/*      */ 
/*  183 */     if ((textureFormat != this.textureFormat) || (textureType != this.textureType)) {
/*  184 */       this.textureFormat = textureFormat;
/*  185 */       this.textureType = textureType;
/*  186 */       this.textureFormatChanged = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void beforeDrawFrame()
/*      */   {
/*  196 */     this.drawingFrame = true;
/*      */ 
/*  198 */     if ((this.fovsChanged) || (this.textureFormatChanged)) {
/*  199 */       updateTextureAndDistortionMesh();
/*      */     }
/*      */ 
/*  203 */     GLES20.glGetIntegerv(36006, this.originalFramebufferId);
/*  204 */     GLES20.glBindFramebuffer(36160, this.framebufferId);
/*      */   }
/*      */ 
/*      */   public void afterDrawFrame()
/*      */   {
/*  215 */     GLES20.glBindFramebuffer(36160, this.originalFramebufferId.array()[0]);
/*  216 */     undistortTexture(this.textureId);
/*  217 */     this.drawingFrame = false;
/*      */   }
/*      */ 
/*      */   public void undistortTexture(int textureId)
/*      */   {
/*  226 */     if (this.restoreGLStateEnabled) {
/*  227 */       if (this.chromaticAberrationCorrectionEnabled)
/*  228 */         this.gLStateBackupAberration.readFromGL();
/*      */       else {
/*  230 */         this.gLStateBackup.readFromGL();
/*      */       }
/*      */     }
/*      */ 
/*  234 */     if ((this.fovsChanged) || (this.textureFormatChanged)) {
/*  235 */       updateTextureAndDistortionMesh();
/*      */     }
/*      */ 
/*  238 */     GLES20.glViewport(0, 0, this.hmd.getScreenParams().getWidth(), this.hmd.getScreenParams().getHeight());
/*      */ 
/*  240 */     GLES20.glDisable(3089);
/*  241 */     GLES20.glDisable(2884);
/*      */ 
/*  243 */     GLES20.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
/*  244 */     GLES20.glClear(16640);
/*      */ 
/*  246 */     if (this.chromaticAberrationCorrectionEnabled)
/*  247 */       GLES20.glUseProgram(this.programHolderAberration.program);
/*      */     else {
/*  249 */       GLES20.glUseProgram(this.programHolder.program);
/*      */     }
/*      */ 
/*  252 */     GLES20.glEnable(3089);
/*  253 */     GLES20.glScissor(0, 0, this.hmd.getScreenParams().getWidth() / 2, this.hmd.getScreenParams().getHeight());
/*      */ 
/*  257 */     renderDistortionMesh(this.leftEyeDistortionMesh, textureId);
/*      */ 
/*  259 */     GLES20.glScissor(this.hmd.getScreenParams().getWidth() / 2, 0, this.hmd.getScreenParams().getWidth() / 2, this.hmd.getScreenParams().getHeight());
/*      */ 
/*  263 */     renderDistortionMesh(this.rightEyeDistortionMesh, textureId);
/*      */ 
/*  265 */     if (this.restoreGLStateEnabled)
/*  266 */       if (this.chromaticAberrationCorrectionEnabled)
/*  267 */         this.gLStateBackupAberration.writeToGL();
/*      */       else
/*  269 */         this.gLStateBackup.writeToGL();
/*      */   }
/*      */ 
/*      */   public void setResolutionScale(float scale)
/*      */   {
/*  285 */     this.resolutionScale = scale;
/*  286 */     this.viewportsChanged = true;
/*      */   }
/*      */ 
/*      */   public void setRestoreGLStateEnabled(boolean enabled)
/*      */   {
/*  300 */     this.restoreGLStateEnabled = enabled;
/*      */   }
/*      */ 
/*      */   public void setChromaticAberrationCorrectionEnabled(boolean enabled)
/*      */   {
/*  315 */     this.chromaticAberrationCorrectionEnabled = enabled;
/*      */   }
/*      */ 
/*      */   public void setVignetteEnabled(boolean enabled)
/*      */   {
/*  330 */     this.vignetteEnabled = enabled;
/*  331 */     this.fovsChanged = true;
/*      */   }
/*      */ 
/*      */   public void onFovChanged(HeadMountedDisplay hmd, FieldOfView leftFov, FieldOfView rightFov, float virtualEyeToScreenDistance)
/*      */   {
/*  357 */     if (this.drawingFrame) {
/*  358 */       throw new IllegalStateException("Cannot change FOV while rendering a frame.");
/*      */     }
/*      */ 
/*  361 */     this.hmd = new HeadMountedDisplay(hmd);
/*      */ 
/*  363 */     this.leftEyeViewport = initViewportForEye(leftFov, 0.0F);
/*  364 */     this.rightEyeViewport = initViewportForEye(rightFov, this.leftEyeViewport.width);
/*      */ 
/*  366 */     this.metersPerTanAngle = virtualEyeToScreenDistance;
/*  367 */     ScreenParams screen = this.hmd.getScreenParams();
/*  368 */     this.xPxPerTanAngle = (screen.getWidth() / (screen.getWidthMeters() / this.metersPerTanAngle));
/*      */ 
/*  370 */     this.yPxPerTanAngle = (screen.getHeight() / (screen.getHeightMeters() / this.metersPerTanAngle));
/*      */ 
/*  373 */     this.fovsChanged = true;
/*  374 */     this.viewportsChanged = true;
/*      */   }
/*      */ 
/*      */   public boolean haveViewportsChanged()
/*      */   {
/*  384 */     return this.viewportsChanged;
/*      */   }
/*      */ 
/*      */   public void updateViewports(Viewport leftViewport, Viewport rightViewport)
/*      */   {
/*  394 */     leftViewport.setViewport(Math.round(this.leftEyeViewport.x * this.xPxPerTanAngle * this.resolutionScale), Math.round(this.leftEyeViewport.y * this.yPxPerTanAngle * this.resolutionScale), Math.round(this.leftEyeViewport.width * this.xPxPerTanAngle * this.resolutionScale), Math.round(this.leftEyeViewport.height * this.yPxPerTanAngle * this.resolutionScale));
/*      */ 
/*  400 */     rightViewport.setViewport(Math.round(this.rightEyeViewport.x * this.xPxPerTanAngle * this.resolutionScale), Math.round(this.rightEyeViewport.y * this.yPxPerTanAngle * this.resolutionScale), Math.round(this.rightEyeViewport.width * this.xPxPerTanAngle * this.resolutionScale), Math.round(this.rightEyeViewport.height * this.yPxPerTanAngle * this.resolutionScale));
/*      */ 
/*  406 */     this.viewportsChanged = false;
/*      */   }
/*      */ 
/*      */   private void updateTextureAndDistortionMesh() {
/*  410 */     ScreenParams screen = this.hmd.getScreenParams();
/*  411 */     CardboardDeviceParams cdp = this.hmd.getCardboardDeviceParams();
/*      */ 
/*  413 */     if (this.programHolder == null) {
/*  414 */       this.programHolder = createProgramHolder();
/*      */     }
/*      */ 
/*  417 */     if (this.programHolderAberration == null) {
/*  418 */       this.programHolderAberration = ((ProgramHolderAberration)createProgramHolder(true));
/*      */     }
/*      */ 
/*  422 */     float textureWidthTanAngle = this.leftEyeViewport.width + this.rightEyeViewport.width;
/*  423 */     float textureHeightTanAngle = Math.max(this.leftEyeViewport.height, this.rightEyeViewport.height);
/*      */ 
/*  426 */     int[] maxTextureSize = new int[1];
/*  427 */     GLES20.glGetIntegerv(3379, maxTextureSize, 0);
/*      */ 
/*  429 */     int textureWidthPx = Math.min(Math.round(textureWidthTanAngle * this.xPxPerTanAngle), maxTextureSize[0]);
/*      */ 
/*  431 */     int textureHeightPx = Math.min(Math.round(textureHeightTanAngle * this.yPxPerTanAngle), maxTextureSize[0]);
/*      */ 
/*  434 */     float xEyeOffsetTanAngleScreen = (screen.getWidthMeters() / 2.0F - cdp.getInterLensDistance() / 2.0F) / this.metersPerTanAngle;
/*      */ 
/*  437 */     float yEyeOffsetTanAngleScreen = cdp.getYEyeOffsetMeters(screen) / this.metersPerTanAngle;
/*      */ 
/*  439 */     this.leftEyeDistortionMesh = createDistortionMesh(this.leftEyeViewport, textureWidthTanAngle, textureHeightTanAngle, xEyeOffsetTanAngleScreen, yEyeOffsetTanAngleScreen);
/*      */ 
/*  442 */     xEyeOffsetTanAngleScreen = screen.getWidthMeters() / this.metersPerTanAngle - xEyeOffsetTanAngleScreen;
/*      */ 
/*  444 */     this.rightEyeDistortionMesh = createDistortionMesh(this.rightEyeViewport, textureWidthTanAngle, textureHeightTanAngle, xEyeOffsetTanAngleScreen, yEyeOffsetTanAngleScreen);
/*      */ 
/*  448 */     setupRenderTextureAndRenderbuffer(textureWidthPx, textureHeightPx);
/*      */ 
/*  450 */     this.fovsChanged = false;
/*      */   }
/*      */ 
/*      */   private EyeViewport initViewportForEye(FieldOfView fov, float xOffset) {
/*  454 */     float left = (float)Math.tan(Math.toRadians(fov.getLeft()));
/*  455 */     float right = (float)Math.tan(Math.toRadians(fov.getRight()));
/*  456 */     float bottom = (float)Math.tan(Math.toRadians(fov.getBottom()));
/*  457 */     float top = (float)Math.tan(Math.toRadians(fov.getTop()));
/*      */ 
/*  459 */     EyeViewport vp = new EyeViewport(null);
/*  460 */     vp.x = xOffset;
/*  461 */     vp.y = 0.0F;
/*  462 */     vp.width = (left + right);
/*  463 */     vp.height = (bottom + top);
/*  464 */     vp.eyeX = (left + xOffset);
/*  465 */     vp.eyeY = bottom;
/*      */ 
/*  467 */     return vp;
/*      */   }
/*      */ 
/*      */   private DistortionMesh createDistortionMesh(EyeViewport eyeViewport, float textureWidthTanAngle, float textureHeightTanAngle, float xEyeOffsetTanAngleScreen, float yEyeOffsetTanAngleScreen)
/*      */   {
/*  476 */     return new DistortionMesh(this.hmd.getCardboardDeviceParams().getDistortion(), this.hmd.getCardboardDeviceParams().getDistortion(), this.hmd.getCardboardDeviceParams().getDistortion(), this.hmd.getScreenParams().getWidthMeters() / this.metersPerTanAngle, this.hmd.getScreenParams().getHeightMeters() / this.metersPerTanAngle, xEyeOffsetTanAngleScreen, yEyeOffsetTanAngleScreen, textureWidthTanAngle, textureHeightTanAngle, eyeViewport.eyeX, eyeViewport.eyeY, eyeViewport.x, eyeViewport.y, eyeViewport.width, eyeViewport.height);
/*      */   }
/*      */ 
/*      */   private void renderDistortionMesh(DistortionMesh mesh, int textureId)
/*      */   {
/*      */     ProgramHolder holder;
/*      */     ProgramHolder holder;
/*  492 */     if (this.chromaticAberrationCorrectionEnabled)
/*  493 */       holder = this.programHolderAberration;
/*      */     else {
/*  495 */       holder = this.programHolder;
/*      */     }
/*      */ 
/*  498 */     GLES20.glBindBuffer(34962, mesh.arrayBufferId);
/*  499 */     GLES20.glVertexAttribPointer(holder.aPosition, 2, 5126, false, 36, 0);
/*      */ 
/*  506 */     GLES20.glEnableVertexAttribArray(holder.aPosition);
/*      */ 
/*  508 */     GLES20.glVertexAttribPointer(holder.aVignette, 1, 5126, false, 36, 8);
/*      */ 
/*  515 */     GLES20.glEnableVertexAttribArray(holder.aVignette);
/*      */ 
/*  517 */     GLES20.glVertexAttribPointer(holder.aBlueTextureCoord, 2, 5126, false, 36, 28);
/*      */ 
/*  524 */     GLES20.glEnableVertexAttribArray(holder.aBlueTextureCoord);
/*      */ 
/*  526 */     if (this.chromaticAberrationCorrectionEnabled) {
/*  527 */       GLES20.glVertexAttribPointer(((ProgramHolderAberration)holder).aRedTextureCoord, 2, 5126, false, 36, 12);
/*      */ 
/*  534 */       GLES20.glEnableVertexAttribArray(((ProgramHolderAberration)holder).aRedTextureCoord);
/*      */ 
/*  536 */       GLES20.glVertexAttribPointer(((ProgramHolderAberration)holder).aGreenTextureCoord, 2, 5126, false, 36, 20);
/*      */ 
/*  543 */       GLES20.glEnableVertexAttribArray(((ProgramHolderAberration)holder).aGreenTextureCoord);
/*      */     }
/*      */ 
/*  546 */     GLES20.glActiveTexture(33984);
/*  547 */     GLES20.glBindTexture(3553, textureId);
/*  548 */     GLES20.glUniform1i(this.programHolder.uTextureSampler, 0);
/*  549 */     GLES20.glUniform1f(this.programHolder.uTextureCoordScale, this.resolutionScale);
/*      */ 
/*  552 */     GLES20.glBindBuffer(34963, mesh.elementBufferId);
/*  553 */     GLES20.glDrawElements(5, mesh.nIndices, 5123, 0);
/*      */   }
/*      */ 
/*      */   private int createTexture(int width, int height, int textureFormat, int textureType)
/*      */   {
/*  558 */     int[] textureIds = new int[1];
/*  559 */     GLES20.glGenTextures(1, textureIds, 0);
/*      */ 
/*  561 */     GLES20.glBindTexture(3553, textureIds[0]);
/*  562 */     GLES20.glTexParameteri(3553, 10242, 33071);
/*      */ 
/*  564 */     GLES20.glTexParameteri(3553, 10243, 33071);
/*      */ 
/*  566 */     GLES20.glTexParameteri(3553, 10240, 9729);
/*      */ 
/*  568 */     GLES20.glTexParameteri(3553, 10241, 9729);
/*      */ 
/*  572 */     GLES20.glTexImage2D(3553, 0, textureFormat, width, height, 0, textureFormat, textureType, null);
/*      */ 
/*  583 */     return textureIds[0];
/*      */   }
/*      */ 
/*      */   private int setupRenderTextureAndRenderbuffer(int width, int height) {
/*  587 */     if (this.textureId != -1) {
/*  588 */       GLES20.glDeleteTextures(1, new int[] { this.textureId }, 0);
/*      */     }
/*  590 */     if (this.renderbufferId != -1) {
/*  591 */       GLES20.glDeleteRenderbuffers(1, new int[] { this.renderbufferId }, 0);
/*      */     }
/*  593 */     if (this.framebufferId != -1) {
/*  594 */       GLES20.glDeleteFramebuffers(1, new int[] { this.framebufferId }, 0);
/*      */     }
/*  596 */     clearGlError();
/*      */ 
/*  598 */     this.textureId = createTexture(width, height, this.textureFormat, this.textureType);
/*  599 */     this.textureFormatChanged = false;
/*      */ 
/*  601 */     checkGlError("setupRenderTextureAndRenderbuffer: create texture");
/*      */ 
/*  604 */     int[] renderbufferIds = new int[1];
/*  605 */     GLES20.glGenRenderbuffers(1, renderbufferIds, 0);
/*  606 */     GLES20.glBindRenderbuffer(36161, renderbufferIds[0]);
/*  607 */     GLES20.glRenderbufferStorage(36161, 33189, width, height);
/*      */ 
/*  609 */     this.renderbufferId = renderbufferIds[0];
/*  610 */     checkGlError("setupRenderTextureAndRenderbuffer: create renderbuffer");
/*      */ 
/*  612 */     int[] framebufferIds = new int[1];
/*  613 */     GLES20.glGenFramebuffers(1, framebufferIds, 0);
/*  614 */     GLES20.glBindFramebuffer(36160, framebufferIds[0]);
/*  615 */     this.framebufferId = framebufferIds[0];
/*      */ 
/*  617 */     GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.textureId, 0);
/*      */ 
/*  620 */     GLES20.glFramebufferRenderbuffer(36160, 36096, 36161, renderbufferIds[0]);
/*      */ 
/*  624 */     int status = GLES20.glCheckFramebufferStatus(36160);
/*      */ 
/*  626 */     if (status != 36053)
/*      */     {
/*      */       RuntimeException tmp215_212 = new java/lang/RuntimeException; tmp215_212; new String(tmp215_212);
/*      */     }
/*      */   }
/*      */ 
/*      */   private int loadShader(int shaderType, String source)
/*      */   {
/*  638 */     int shader = GLES20.glCreateShader(shaderType);
/*  639 */     if (shader != 0) {
/*  640 */       GLES20.glShaderSource(shader, source);
/*  641 */       GLES20.glCompileShader(shader);
/*  642 */       int[] compiled = new int[1];
/*  643 */       GLES20.glGetShaderiv(shader, 35713, compiled, 0);
/*  644 */       if (compiled[0] == 0) {
/*  645 */         int i = shaderType; Log.e("DistortionRenderer", 37 + "Could not compile shader " + i + ":");
/*  646 */         Log.e("DistortionRenderer", GLES20.glGetShaderInfoLog(shader));
/*  647 */         GLES20.glDeleteShader(shader);
/*  648 */         shader = 0;
/*      */       }
/*      */     }
/*  651 */     return shader;
/*      */   }
/*      */ 
/*      */   private int createProgram(String vertexSource, String fragmentSource) {
/*  655 */     int vertexShader = loadShader(35633, vertexSource);
/*  656 */     if (vertexShader == 0) {
/*  657 */       return 0;
/*      */     }
/*  659 */     int pixelShader = loadShader(35632, fragmentSource);
/*  660 */     if (pixelShader == 0) {
/*  661 */       return 0;
/*      */     }
/*      */ 
/*  664 */     int program = GLES20.glCreateProgram();
/*  665 */     if (program != 0) {
/*  666 */       clearGlError();
/*  667 */       GLES20.glAttachShader(program, vertexShader);
/*  668 */       checkGlError("glAttachShader");
/*  669 */       GLES20.glAttachShader(program, pixelShader);
/*  670 */       checkGlError("glAttachShader");
/*  671 */       GLES20.glLinkProgram(program);
/*  672 */       int[] linkStatus = new int[1];
/*  673 */       GLES20.glGetProgramiv(program, 35714, linkStatus, 0);
/*  674 */       if (linkStatus[0] != 1) {
/*  675 */         Log.e("DistortionRenderer", "Could not link program: ");
/*  676 */         Log.e("DistortionRenderer", GLES20.glGetProgramInfoLog(program));
/*  677 */         GLES20.glDeleteProgram(program);
/*  678 */         program = 0;
/*      */       }
/*      */     }
/*  681 */     return program;
/*      */   }
/*      */ 
/*      */   private ProgramHolder createProgramHolder() {
/*  685 */     return createProgramHolder(false);
/*      */   }
/*      */ 
/*      */   private ProgramHolder createProgramHolder(boolean aberrationCorrected)
/*      */   {
/*      */     GLStateBackup state;
/*      */     ProgramHolder holder;
/*      */     GLStateBackup state;
/*  691 */     if (aberrationCorrected) {
/*  692 */       ProgramHolder holder = new ProgramHolderAberration(null);
/*      */ 
/*  694 */       holder.program = createProgram("attribute vec2 aPosition;\nattribute float aVignette;\nattribute vec2 aRedTextureCoord;\nattribute vec2 aGreenTextureCoord;\nattribute vec2 aBlueTextureCoord;\nvarying vec2 vRedTextureCoord;\nvarying vec2 vBlueTextureCoord;\nvarying vec2 vGreenTextureCoord;\nvarying float vVignette;\nuniform float uTextureCoordScale;\nvoid main() {\n  gl_Position = vec4(aPosition, 0.0, 1.0);\n  vRedTextureCoord = aRedTextureCoord.xy * uTextureCoordScale;\n  vGreenTextureCoord = aGreenTextureCoord.xy * uTextureCoordScale;\n  vBlueTextureCoord = aBlueTextureCoord.xy * uTextureCoordScale;\n  vVignette = aVignette;\n}\n", "precision mediump float;\nvarying vec2 vRedTextureCoord;\nvarying vec2 vBlueTextureCoord;\nvarying vec2 vGreenTextureCoord;\nvarying float vVignette;\nuniform sampler2D uTextureSampler;\nvoid main() {\n  gl_FragColor = vVignette * vec4(texture2D(uTextureSampler, vRedTextureCoord).r,\n          texture2D(uTextureSampler, vGreenTextureCoord).g,\n          texture2D(uTextureSampler, vBlueTextureCoord).b, 1.0);\n}\n");
/*  695 */       if (holder.program == 0) {
/*  696 */         throw new RuntimeException("Could not create aberration-corrected program");
/*      */       }
/*      */ 
/*  699 */       state = this.gLStateBackupAberration;
/*      */     } else {
/*  701 */       holder = new ProgramHolder(null);
/*      */ 
/*  703 */       holder.program = createProgram("attribute vec2 aPosition;\nattribute float aVignette;\nattribute vec2 aBlueTextureCoord;\nvarying vec2 vTextureCoord;\nvarying float vVignette;\nuniform float uTextureCoordScale;\nvoid main() {\n  gl_Position = vec4(aPosition, 0.0, 1.0);\n  vTextureCoord = aBlueTextureCoord.xy * uTextureCoordScale;\n  vVignette = aVignette;\n}\n", "precision mediump float;\nvarying vec2 vTextureCoord;\nvarying float vVignette;\nuniform sampler2D uTextureSampler;\nvoid main() {\n  gl_FragColor = vVignette * texture2D(uTextureSampler, vTextureCoord);\n}\n");
/*  704 */       if (holder.program == 0) {
/*  705 */         throw new RuntimeException("Could not create program");
/*      */       }
/*      */ 
/*  708 */       state = this.gLStateBackup;
/*      */     }
/*  710 */     clearGlError();
/*      */ 
/*  712 */     holder.aPosition = GLES20.glGetAttribLocation(holder.program, "aPosition");
/*  713 */     checkGlError("glGetAttribLocation aPosition");
/*  714 */     if (holder.aPosition == -1) {
/*  715 */       throw new RuntimeException("Could not get attrib location for aPosition");
/*      */     }
/*  717 */     state.addTrackedVertexAttribute(holder.aPosition);
/*  718 */     holder.aVignette = GLES20.glGetAttribLocation(holder.program, "aVignette");
/*  719 */     checkGlError("glGetAttribLocation aVignette");
/*  720 */     if (holder.aVignette == -1) {
/*  721 */       throw new RuntimeException("Could not get attrib location for aVignette");
/*      */     }
/*  723 */     state.addTrackedVertexAttribute(holder.aVignette);
/*      */ 
/*  725 */     if (aberrationCorrected) {
/*  726 */       ((ProgramHolderAberration)holder).aRedTextureCoord = GLES20.glGetAttribLocation(holder.program, "aRedTextureCoord");
/*      */ 
/*  728 */       checkGlError("glGetAttribLocation aRedTextureCoord");
/*  729 */       if (((ProgramHolderAberration)holder).aRedTextureCoord == -1) {
/*  730 */         throw new RuntimeException("Could not get attrib location for aRedTextureCoord");
/*      */       }
/*  732 */       ((ProgramHolderAberration)holder).aGreenTextureCoord = GLES20.glGetAttribLocation(holder.program, "aGreenTextureCoord");
/*      */ 
/*  734 */       checkGlError("glGetAttribLocation aGreenTextureCoord");
/*  735 */       if (((ProgramHolderAberration)holder).aGreenTextureCoord == -1) {
/*  736 */         throw new RuntimeException("Could not get attrib location for aGreenTextureCoord");
/*      */       }
/*  738 */       state.addTrackedVertexAttribute(((ProgramHolderAberration)holder).aRedTextureCoord);
/*  739 */       state.addTrackedVertexAttribute(((ProgramHolderAberration)holder).aGreenTextureCoord);
/*      */     }
/*      */ 
/*  742 */     holder.aBlueTextureCoord = GLES20.glGetAttribLocation(holder.program, "aBlueTextureCoord");
/*      */ 
/*  744 */     checkGlError("glGetAttribLocation aBlueTextureCoord");
/*  745 */     if (holder.aBlueTextureCoord == -1) {
/*  746 */       throw new RuntimeException("Could not get attrib location for aBlueTextureCoord");
/*      */     }
/*  748 */     state.addTrackedVertexAttribute(holder.aBlueTextureCoord);
/*  749 */     holder.uTextureCoordScale = GLES20.glGetUniformLocation(holder.program, "uTextureCoordScale");
/*      */ 
/*  751 */     checkGlError("glGetUniformLocation uTextureCoordScale");
/*  752 */     if (holder.uTextureCoordScale == -1) {
/*  753 */       throw new RuntimeException("Could not get attrib location for uTextureCoordScale");
/*      */     }
/*  755 */     holder.uTextureSampler = GLES20.glGetUniformLocation(holder.program, "uTextureSampler");
/*      */ 
/*  757 */     checkGlError("glGetUniformLocation uTextureSampler");
/*  758 */     if (holder.uTextureSampler == -1) {
/*  759 */       throw new RuntimeException("Could not get attrib location for uTextureSampler");
/*      */     }
/*      */ 
/*  762 */     return holder;
/*      */   }
/*      */ 
/*      */   private void clearGlError() {
/*  766 */     while (GLES20.glGetError() != 0);
/*      */   }
/*      */ 
/*      */   private void checkGlError(String op)
/*      */   {
/*      */     int error;
/*  773 */     if ((error = GLES20.glGetError()) != 0) {
/*  774 */       String str1 = String.valueOf(String.valueOf(op)); int i = error; Log.e("DistortionRenderer", 21 + str1.length() + str1 + ": glError " + i);
/*  775 */       String str2 = String.valueOf(String.valueOf(op)); int j = error; throw new RuntimeException(21 + str2.length() + str2 + ": glError " + j);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static float clamp(float val, float min, float max) {
/*  780 */     return Math.max(min, Math.min(max, val));
/*      */   }
/*      */ 
/*      */   private class DistortionMesh
/*      */   {
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
/*  810 */     public int arrayBufferId = -1;
/*  811 */     public int elementBufferId = -1;
/*      */ 
/*      */     public DistortionMesh(Distortion distortionRed, Distortion distortionGreen, Distortion distortionBlue, float screenWidth, float screenHeight, float xEyeOffsetScreen, float yEyeOffsetScreen, float textureWidth, float textureHeight, float xEyeOffsetTexture, float yEyeOffsetTexture, float viewportXTexture, float viewportYTexture, float viewportWidthTexture, float viewportHeightTexture)
/*      */     {
/*  823 */       float[] vertexData = new float[14400];
/*  824 */       short vertexOffset = 0;
/*      */ 
/*  836 */       for (int row = 0; row < 40; row++) {
/*  837 */         for (int col = 0; col < 40; col++)
/*      */         {
/*  844 */           float uTextureBlue = col / 39.0F * (viewportWidthTexture / textureWidth) + viewportXTexture / textureWidth;
/*      */ 
/*  847 */           float vTextureBlue = row / 39.0F * (viewportHeightTexture / textureHeight) + viewportYTexture / textureHeight;
/*      */ 
/*  852 */           float xTexture = uTextureBlue * textureWidth - xEyeOffsetTexture;
/*  853 */           float yTexture = vTextureBlue * textureHeight - yEyeOffsetTexture;
/*  854 */           float rTexture = (float)Math.sqrt(xTexture * xTexture + yTexture * yTexture);
/*      */ 
/*  860 */           float textureToScreenBlue = rTexture > 0.0F ? distortionBlue.distortInverse(rTexture) / rTexture : 1.0F;
/*      */ 
/*  865 */           float xScreen = xTexture * textureToScreenBlue;
/*  866 */           float yScreen = yTexture * textureToScreenBlue;
/*      */ 
/*  868 */           float uScreen = (xScreen + xEyeOffsetScreen) / screenWidth;
/*  869 */           float vScreen = (yScreen + yEyeOffsetScreen) / screenHeight;
/*      */ 
/*  872 */           float rScreen = rTexture * textureToScreenBlue;
/*      */ 
/*  876 */           float screenToTextureGreen = rScreen > 0.0F ? distortionGreen.distortionFactor(rScreen) : 1.0F;
/*      */ 
/*  880 */           float uTextureGreen = (xScreen * screenToTextureGreen + xEyeOffsetTexture) / textureWidth;
/*      */ 
/*  882 */           float vTextureGreen = (yScreen * screenToTextureGreen + yEyeOffsetTexture) / textureHeight;
/*      */ 
/*  885 */           float screenToTextureRed = rScreen > 0.0F ? distortionRed.distortionFactor(rScreen) : 1.0F;
/*      */ 
/*  889 */           float uTextureRed = (xScreen * screenToTextureRed + xEyeOffsetTexture) / textureWidth;
/*      */ 
/*  891 */           float vTextureRed = (yScreen * screenToTextureRed + yEyeOffsetTexture) / textureHeight;
/*      */ 
/*  895 */           float vignetteSizeTexture = 0.05F / textureToScreenBlue;
/*  896 */           float dxTexture = xTexture + xEyeOffsetTexture - DistortionRenderer.clamp(xTexture + xEyeOffsetTexture, viewportXTexture + vignetteSizeTexture, viewportXTexture + viewportWidthTexture - vignetteSizeTexture);
/*      */ 
/*  900 */           float dyTexture = yTexture + yEyeOffsetTexture - DistortionRenderer.clamp(yTexture + yEyeOffsetTexture, viewportYTexture + vignetteSizeTexture, viewportYTexture + viewportHeightTexture - vignetteSizeTexture);
/*      */ 
/*  904 */           float drTexture = (float)Math.sqrt(dxTexture * dxTexture + dyTexture * dyTexture);
/*      */           float vignette;
/*      */           float vignette;
/*  907 */           if (DistortionRenderer.this.vignetteEnabled)
/*  908 */             vignette = 1.0F - DistortionRenderer.clamp(drTexture / vignetteSizeTexture, 0.0F, 1.0F);
/*      */           else {
/*  910 */             vignette = 1.0F;
/*      */           }
/*      */ 
/*  913 */           vertexData[(vertexOffset + 0)] = (2.0F * uScreen - 1.0F);
/*  914 */           vertexData[(vertexOffset + 1)] = (2.0F * vScreen - 1.0F);
/*  915 */           vertexData[(vertexOffset + 2)] = vignette;
/*  916 */           vertexData[(vertexOffset + 3)] = uTextureRed;
/*  917 */           vertexData[(vertexOffset + 4)] = vTextureRed;
/*  918 */           vertexData[(vertexOffset + 5)] = uTextureGreen;
/*  919 */           vertexData[(vertexOffset + 6)] = vTextureGreen;
/*  920 */           vertexData[(vertexOffset + 7)] = uTextureBlue;
/*  921 */           vertexData[(vertexOffset + 8)] = vTextureBlue;
/*      */ 
/*  923 */           vertexOffset = (short)(vertexOffset + 9);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  951 */       this.nIndices = 3158;
/*  952 */       short[] indexData = new short[this.nIndices];
/*  953 */       short indexOffset = 0;
/*  954 */       vertexOffset = 0;
/*  955 */       for (int row = 0; row < 39; row++) {
/*  956 */         if (row > 0) {
/*  957 */           indexData[indexOffset] = indexData[(indexOffset - 1)];
/*  958 */           indexOffset = (short)(indexOffset + 1);
/*      */         }
/*  960 */         for (int col = 0; col < 40; col++) {
/*  961 */           if (col > 0) {
/*  962 */             if (row % 2 == 0)
/*      */             {
/*  964 */               vertexOffset = (short)(vertexOffset + 1);
/*      */             }
/*      */             else {
/*  967 */               vertexOffset = (short)(vertexOffset - 1);
/*      */             }
/*      */           }
/*  970 */           indexOffset = (short)(indexOffset + 1); indexData[indexOffset] = vertexOffset;
/*  971 */           indexOffset = (short)(indexOffset + 1); indexData[indexOffset] = ((short)(vertexOffset + 40));
/*      */         }
/*  973 */         vertexOffset = (short)(vertexOffset + 40);
/*      */       }
/*      */ 
/*  976 */       FloatBuffer vertexBuffer = ByteBuffer.allocateDirect(vertexData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
/*      */ 
/*  979 */       vertexBuffer.put(vertexData).position(0);
/*      */ 
/*  981 */       ShortBuffer indexBuffer = ByteBuffer.allocateDirect(indexData.length * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
/*      */ 
/*  984 */       indexBuffer.put(indexData).position(0);
/*      */ 
/*  986 */       int[] bufferIds = new int[2];
/*  987 */       GLES20.glGenBuffers(2, bufferIds, 0);
/*  988 */       this.arrayBufferId = bufferIds[0];
/*  989 */       this.elementBufferId = bufferIds[1];
/*      */ 
/*  991 */       GLES20.glBindBuffer(34962, this.arrayBufferId);
/*  992 */       GLES20.glBufferData(34962, vertexData.length * 4, vertexBuffer, 35044);
/*      */ 
/*  995 */       GLES20.glBindBuffer(34963, this.elementBufferId);
/*  996 */       GLES20.glBufferData(34963, indexData.length * 2, indexBuffer, 35044);
/*      */ 
/*  999 */       GLES20.glBindBuffer(34962, 0);
/* 1000 */       GLES20.glBindBuffer(34963, 0);
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