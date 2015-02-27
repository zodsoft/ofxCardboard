/*     */ package com.google.vrtoolkit.cardboard;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.content.res.Resources;
/*     */ import android.graphics.Color;
/*     */ import android.graphics.Rect;
/*     */ import android.opengl.GLES20;
/*     */ import android.opengl.Matrix;
/*     */ import android.util.DisplayMetrics;
/*     */ import android.util.Log;
/*     */ import android.view.MotionEvent;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.nio.ShortBuffer;
/*     */ 
/*     */ class UiLayer
/*     */ {
/*  26 */   private static final String TAG = UiLayer.class.getSimpleName();
/*     */   private static final int NORMAL_COLOR = -3355444;
/*     */   private static final int PRESSED_COLOR = -12303292;
/*     */   private static final float CENTER_LINE_THICKNESS_DP = 4.0F;
/*     */   private static final int BUTTON_WIDTH_DP = 28;
/*     */   private static final float TOUCH_SLOP_FACTOR = 1.5F;
/*     */   private final int mTouchWidthPx;
/*  43 */   private volatile Rect mTouchRect = new Rect();
/*     */   private boolean mDownWithinBounds;
/*     */   private Context mContext;
/*     */   private final GLStateBackup mGlStateBackup;
/*     */   private final ShaderProgram mShader;
/*     */   private final SettingsButtonRenderer mSettingsButtonRenderer;
/*     */   private final AlignmentMarkerRenderer mAlignmentMarkerRenderer;
/*     */   private Viewport mViewport;
/*  57 */   private boolean mShouldUpdateViewport = true;
/*     */ 
/*  60 */   private boolean mSettingsButtonEnabled = true;
/*     */ 
/*  63 */   private boolean mAlignmentMarkerEnabled = true;
/*     */   private boolean initialized;
/*     */ 
/*     */   UiLayer(Context context)
/*     */   {
/*  75 */     this.mContext = context;
/*  76 */     float density = context.getResources().getDisplayMetrics().density;
/*  77 */     int buttonWidthPx = (int)(28.0F * density);
/*  78 */     this.mTouchWidthPx = ((int)(buttonWidthPx * 1.5F));
/*     */ 
/*  80 */     this.mGlStateBackup = new GLStateBackup();
/*  81 */     this.mShader = new ShaderProgram(null);
/*  82 */     this.mSettingsButtonRenderer = new SettingsButtonRenderer(this.mShader, buttonWidthPx);
/*  83 */     this.mAlignmentMarkerRenderer = new AlignmentMarkerRenderer(this.mShader, this.mTouchWidthPx, 4.0F * density);
/*     */ 
/*  85 */     this.mViewport = new Viewport();
/*     */   }
/*     */ 
/*     */   void updateViewport(Viewport viewport) {
/*  89 */     synchronized (this) {
/*  90 */       if (this.mViewport.equals(viewport)) {
/*  91 */         return;
/*     */       }
/*  93 */       int w = viewport.width;
/*  94 */       int h = viewport.height;
/*  95 */       this.mTouchRect = new Rect((w - this.mTouchWidthPx) / 2, h - this.mTouchWidthPx, (w + this.mTouchWidthPx) / 2, h);
/*     */ 
/*  97 */       this.mViewport.setViewport(viewport.x, viewport.y, viewport.width, viewport.height);
/*  98 */       this.mShouldUpdateViewport = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   void initializeGl() {
/* 103 */     this.mShader.initializeGl();
/* 104 */     this.mGlStateBackup.clearTrackedVertexAttributes();
/* 105 */     this.mGlStateBackup.addTrackedVertexAttribute(this.mShader.aPosition);
/*     */ 
/* 107 */     this.mGlStateBackup.readFromGL();
/* 108 */     this.mSettingsButtonRenderer.initializeGl();
/* 109 */     this.mAlignmentMarkerRenderer.initializeGl();
/* 110 */     this.mGlStateBackup.writeToGL();
/* 111 */     this.initialized = true;
/*     */   }
/*     */ 
/*     */   void draw()
/*     */   {
/* 119 */     if ((!getSettingsButtonEnabled()) && (!getAlignmentMarkerEnabled())) {
/* 120 */       return;
/*     */     }
/*     */ 
/* 123 */     if (!this.initialized) {
/* 124 */       initializeGl();
/*     */     }
/*     */ 
/* 127 */     this.mGlStateBackup.readFromGL();
/*     */ 
/* 129 */     synchronized (this)
/*     */     {
/* 131 */       if (this.mShouldUpdateViewport) {
/* 132 */         this.mShouldUpdateViewport = false;
/* 133 */         this.mSettingsButtonRenderer.updateViewport(this.mViewport);
/* 134 */         this.mAlignmentMarkerRenderer.updateViewport(this.mViewport);
/*     */       }
/* 136 */       this.mViewport.setGLViewport();
/*     */     }
/*     */ 
/* 139 */     if (getSettingsButtonEnabled()) {
/* 140 */       this.mSettingsButtonRenderer.draw();
/*     */     }
/* 142 */     if (getAlignmentMarkerEnabled()) {
/* 143 */       this.mAlignmentMarkerRenderer.draw();
/*     */     }
/*     */ 
/* 146 */     this.mGlStateBackup.writeToGL();
/*     */   }
/*     */ 
/*     */   synchronized void setAlignmentMarkerEnabled(boolean enabled) {
/* 150 */     if (this.mAlignmentMarkerEnabled != enabled) {
/* 151 */       this.mAlignmentMarkerEnabled = enabled;
/* 152 */       this.mShouldUpdateViewport = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized boolean getAlignmentMarkerEnabled() {
/* 157 */     return this.mAlignmentMarkerEnabled;
/*     */   }
/*     */ 
/*     */   synchronized void setSettingsButtonEnabled(boolean enabled) {
/* 161 */     if (this.mSettingsButtonEnabled != enabled) {
/* 162 */       this.mSettingsButtonEnabled = enabled;
/* 163 */       this.mShouldUpdateViewport = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized boolean getSettingsButtonEnabled() {
/* 168 */     return this.mSettingsButtonEnabled;
/*     */   }
/*     */ 
/*     */   boolean onTouchEvent(MotionEvent e) {
/* 172 */     boolean touchWithinBounds = false;
/* 173 */     synchronized (this) {
/* 174 */       if (!this.mSettingsButtonEnabled) {
/* 175 */         return false;
/*     */       }
/* 177 */       touchWithinBounds = this.mTouchRect.contains((int)e.getX(), (int)e.getY());
/*     */     }
/*     */ 
/* 180 */     if ((e.getActionMasked() == 0) && (touchWithinBounds)) {
/* 181 */       this.mDownWithinBounds = true;
/*     */     }
/* 183 */     if (!this.mDownWithinBounds) {
/* 184 */       return false;
/*     */     }
/* 186 */     if (e.getActionMasked() == 1) {
/* 187 */       if (touchWithinBounds) {
/* 188 */         UiUtils.launchOrInstallCardboard(this.mContext);
/*     */       }
/* 190 */       this.mDownWithinBounds = false;
/* 191 */     } else if (e.getActionMasked() == 3) {
/* 192 */       this.mDownWithinBounds = false;
/*     */     }
/* 194 */     setPressed((this.mDownWithinBounds) && (touchWithinBounds));
/* 195 */     return true;
/*     */   }
/*     */ 
/*     */   private void setPressed(boolean pressed) {
/* 199 */     if (this.mSettingsButtonRenderer != null)
/* 200 */       this.mSettingsButtonRenderer.setColor(pressed ? -12303292 : -3355444);
/*     */   }
/*     */ 
/*     */   private static void checkGlError(String op)
/*     */   {
/*     */     int error;
/* 545 */     if ((error = GLES20.glGetError()) != 0) {
/* 546 */       String str1 = String.valueOf(String.valueOf(op)); int i = error; Log.e(TAG, 21 + str1.length() + str1 + ": glError " + i);
/* 547 */       String str2 = String.valueOf(String.valueOf(op)); int j = error; throw new RuntimeException(21 + str2.length() + str2 + ": glError " + j);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static float lerp(float a, float b, float t) {
/* 552 */     return a * (1.0F - t) + b * t;
/*     */   }
/*     */ 
/*     */   private static class SettingsButtonRenderer extends UiLayer.MeshRenderer
/*     */   {
/*     */     private static final int DEGREES_PER_GEAR_SECTION = 60;
/*     */     private static final int OUTER_RIM_END_DEG = 12;
/*     */     private static final int INNER_RIM_BEGIN_DEG = 20;
/*     */     private static final float OUTER_RADIUS = 1.0F;
/*     */     private static final float MIDDLE_RADIUS = 0.75F;
/*     */     private static final float INNER_RADIUS = 0.3125F;
/*     */     private static final int NUM_VERTICES = 60;
/*     */     private int mButtonWidthPx;
/* 458 */     private int mColor = -3355444;
/*     */ 
/*     */     SettingsButtonRenderer(UiLayer.ShaderProgram shader, int buttonWidthPx) {
/* 461 */       super();
/* 462 */       this.mButtonWidthPx = buttonWidthPx;
/*     */     }
/*     */ 
/*     */     void initializeGl() {
/* 466 */       float[] vertexData = new float[120];
/* 467 */       int numVerticesPerRim = 30;
/*     */ 
/* 470 */       float lerpInterval = 8.0F;
/* 471 */       for (int i = 0; i < numVerticesPerRim; i++) {
/* 472 */         float theta = i / numVerticesPerRim * 360.0F;
/* 473 */         float mod = theta % 60.0F;
/*     */         float r;
/*     */         float r;
/* 475 */         if (mod <= 12.0F) {
/* 476 */           r = 1.0F;
/*     */         }
/*     */         else
/*     */         {
/*     */           float r;
/* 477 */           if (mod <= 20.0F) {
/* 478 */             r = UiLayer.lerp(1.0F, 0.75F, (mod - 12.0F) / lerpInterval);
/*     */           }
/*     */           else
/*     */           {
/*     */             float r;
/* 479 */             if (mod <= 40.0F) {
/* 480 */               r = 0.75F;
/*     */             }
/*     */             else
/*     */             {
/*     */               float r;
/* 481 */               if (mod <= 48.0F) {
/* 482 */                 r = UiLayer.lerp(0.75F, 1.0F, (mod - 60.0F + 20.0F) / lerpInterval);
/*     */               }
/*     */               else
/* 485 */                 r = 1.0F; 
/*     */             }
/*     */           }
/*     */         }
/* 488 */         vertexData[(2 * i)] = (r * (float)Math.cos(Math.toRadians(90.0F - theta)));
/*     */ 
/* 490 */         vertexData[(2 * i + 1)] = (r * (float)Math.sin(Math.toRadians(90.0F - theta)));
/*     */       }
/*     */ 
/* 496 */       int innerStartingIndex = 2 * numVerticesPerRim;
/* 497 */       for (int i = 0; i < numVerticesPerRim; i++) {
/* 498 */         float theta = i / numVerticesPerRim * 360.0F;
/* 499 */         vertexData[(innerStartingIndex + 2 * i)] = (0.3125F * (float)Math.cos(Math.toRadians(90.0F - theta)));
/*     */ 
/* 501 */         vertexData[(innerStartingIndex + 2 * i + 1)] = (0.3125F * (float)Math.sin(Math.toRadians(90.0F - theta)));
/*     */       }
/*     */ 
/* 507 */       short[] indexData = new short[62];
/* 508 */       for (int i = 0; i < numVerticesPerRim; i++) {
/* 509 */         indexData[(2 * i)] = ((short)i);
/* 510 */         indexData[(2 * i + 1)] = ((short)(numVerticesPerRim + i));
/*     */       }
/* 512 */       indexData[(indexData.length - 2)] = 0;
/* 513 */       indexData[(indexData.length - 1)] = ((short)numVerticesPerRim);
/*     */ 
/* 515 */       genAndBindBuffers(vertexData, indexData);
/*     */     }
/*     */ 
/*     */     synchronized void setColor(int color) {
/* 519 */       this.mColor = color;
/*     */     }
/*     */ 
/*     */     void updateViewport(Viewport viewport)
/*     */     {
/* 524 */       Matrix.setIdentityM(this.mMvp, 0);
/* 525 */       float yScale = this.mButtonWidthPx / viewport.height;
/* 526 */       float xScale = yScale * viewport.height / viewport.width;
/* 527 */       Matrix.translateM(this.mMvp, 0, 0.0F, yScale - 1.0F, 0.0F);
/* 528 */       Matrix.scaleM(this.mMvp, 0, xScale, yScale, 1.0F);
/*     */     }
/*     */ 
/*     */     void draw()
/*     */     {
/* 533 */       GLES20.glUseProgram(this.mShader.program);
/* 534 */       synchronized (this) {
/* 535 */         GLES20.glUniform4f(this.mShader.uColor, Color.red(this.mColor) / 255.0F, Color.green(this.mColor) / 255.0F, Color.blue(this.mColor) / 255.0F, Color.alpha(this.mColor) / 255.0F);
/*     */       }
/*     */ 
/* 539 */       super.draw();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class AlignmentMarkerRenderer extends UiLayer.MeshRenderer
/*     */   {
/* 375 */     private static final int COLOR = Color.argb(255, 50, 50, 50);
/*     */     private float mVerticalBorderPaddingPx;
/*     */     private float mLineThicknessPx;
/*     */ 
/*     */     AlignmentMarkerRenderer(UiLayer.ShaderProgram shader, float verticalBorderPaddingPx, float lineThicknessPx)
/*     */     {
/* 382 */       super();
/* 383 */       this.mVerticalBorderPaddingPx = verticalBorderPaddingPx;
/* 384 */       this.mLineThicknessPx = lineThicknessPx;
/*     */     }
/*     */ 
/*     */     void initializeGl() {
/* 388 */       float[] vertexData = { 1.0F, 1.0F, -1.0F, 1.0F, 1.0F, -1.0F, -1.0F, -1.0F };
/*     */ 
/* 394 */       short[] indexData = new short[vertexData.length / 2];
/* 395 */       for (int i = 0; i < indexData.length; i++) {
/* 396 */         indexData[i] = ((short)i);
/*     */       }
/* 398 */       genAndBindBuffers(vertexData, indexData);
/*     */     }
/*     */ 
/*     */     void updateViewport(Viewport viewport)
/*     */     {
/* 403 */       Matrix.setIdentityM(this.mMvp, 0);
/* 404 */       float xScale = this.mLineThicknessPx / viewport.width;
/* 405 */       float yScale = 1.0F - 2.0F * this.mVerticalBorderPaddingPx / viewport.height;
/* 406 */       Matrix.scaleM(this.mMvp, 0, xScale, yScale, 1.0F);
/*     */     }
/*     */ 
/*     */     void draw()
/*     */     {
/* 411 */       GLES20.glUseProgram(this.mShader.program);
/* 412 */       GLES20.glUniform4f(this.mShader.uColor, Color.red(COLOR) / 255.0F, Color.green(COLOR) / 255.0F, Color.blue(COLOR) / 255.0F, Color.alpha(COLOR) / 255.0F);
/*     */ 
/* 415 */       super.draw();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class MeshRenderer
/*     */   {
/*     */     private static final int BYTES_PER_FLOAT = 4;
/*     */     private static final int BYTES_PER_SHORT = 4;
/*     */     protected static final int COMPONENTS_PER_VERT = 2;
/*     */     private static final int DATA_STRIDE_BYTES = 8;
/*     */     private static final int DATA_POS_OFFSET = 0;
/* 305 */     protected int mArrayBufferId = -1;
/* 306 */     protected int mElementBufferId = -1;
/*     */     protected UiLayer.ShaderProgram mShader;
/* 308 */     protected float[] mMvp = new float[16];
/*     */     private int mNumIndices;
/*     */ 
/*     */     MeshRenderer(UiLayer.ShaderProgram shader)
/*     */     {
/* 313 */       this.mShader = shader;
/*     */     }
/*     */ 
/*     */     void genAndBindBuffers(float[] vertexData, short[] indexData) {
/* 317 */       FloatBuffer vertexBuffer = ByteBuffer.allocateDirect(vertexData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
/*     */ 
/* 320 */       vertexBuffer.put(vertexData).position(0);
/*     */ 
/* 322 */       this.mNumIndices = indexData.length;
/* 323 */       ShortBuffer indexBuffer = ByteBuffer.allocateDirect(this.mNumIndices * 4).order(ByteOrder.nativeOrder()).asShortBuffer();
/*     */ 
/* 325 */       indexBuffer.put(indexData).position(0);
/*     */ 
/* 327 */       int[] bufferIds = new int[2];
/* 328 */       GLES20.glGenBuffers(2, bufferIds, 0);
/* 329 */       this.mArrayBufferId = bufferIds[0];
/* 330 */       this.mElementBufferId = bufferIds[1];
/*     */ 
/* 332 */       GLES20.glBindBuffer(34962, this.mArrayBufferId);
/* 333 */       GLES20.glBufferData(34962, vertexData.length * 4, vertexBuffer, 35044);
/*     */ 
/* 335 */       GLES20.glBindBuffer(34963, this.mElementBufferId);
/* 336 */       GLES20.glBufferData(34963, indexData.length * 4, indexBuffer, 35044);
/*     */ 
/* 339 */       UiLayer.checkGlError("genAndBindBuffers");
/*     */     }
/*     */ 
/*     */     void updateViewport(Viewport viewport) {
/* 343 */       Matrix.setIdentityM(this.mMvp, 0);
/*     */     }
/*     */ 
/*     */     void draw() {
/* 347 */       GLES20.glDisable(2929);
/* 348 */       GLES20.glDisable(2884);
/*     */ 
/* 351 */       GLES20.glUseProgram(this.mShader.program);
/* 352 */       GLES20.glUniformMatrix4fv(this.mShader.uMvpMatrix, 1, false, this.mMvp, 0);
/*     */ 
/* 354 */       GLES20.glBindBuffer(34962, this.mArrayBufferId);
/* 355 */       GLES20.glVertexAttribPointer(this.mShader.aPosition, 2, 5126, false, 8, 0);
/*     */ 
/* 362 */       GLES20.glEnableVertexAttribArray(this.mShader.aPosition);
/* 363 */       GLES20.glBindBuffer(34963, this.mElementBufferId);
/*     */ 
/* 366 */       GLES20.glDrawElements(5, this.mNumIndices, 5123, 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class ShaderProgram
/*     */   {
/*     */     private static final String VERTEX_SHADER = "uniform mat4 uMVPMatrix;\nattribute vec2 aPosition;\nvoid main() {\n    gl_Position = uMVPMatrix * vec4(aPosition, 0.0, 1.0);\n}\n";
/*     */     private static final String FRAGMENT_SHADER = "precision mediump float;\nuniform vec4 uColor;\nvoid main() {\n    gl_FragColor = uColor;\n}\n";
/*     */     public int program;
/*     */     public int aPosition;
/*     */     public int uMvpMatrix;
/*     */     public int uColor;
/*     */ 
/*     */     void initializeGl()
/*     */     {
/* 225 */       this.program = createProgram("uniform mat4 uMVPMatrix;\nattribute vec2 aPosition;\nvoid main() {\n    gl_Position = uMVPMatrix * vec4(aPosition, 0.0, 1.0);\n}\n", "precision mediump float;\nuniform vec4 uColor;\nvoid main() {\n    gl_FragColor = uColor;\n}\n");
/* 226 */       if (this.program == 0) {
/* 227 */         throw new RuntimeException("Could not create program");
/*     */       }
/*     */ 
/* 230 */       this.aPosition = GLES20.glGetAttribLocation(this.program, "aPosition");
/* 231 */       UiLayer.checkGlError("glGetAttribLocation aPosition");
/* 232 */       if (this.aPosition == -1) {
/* 233 */         throw new RuntimeException("Could not get attrib location for aPosition");
/*     */       }
/*     */ 
/* 236 */       this.uMvpMatrix = GLES20.glGetUniformLocation(this.program, "uMVPMatrix");
/* 237 */       if (this.uMvpMatrix == -1) {
/* 238 */         throw new RuntimeException("Could not get uniform location for uMVPMatrix");
/*     */       }
/*     */ 
/* 241 */       this.uColor = GLES20.glGetUniformLocation(this.program, "uColor");
/* 242 */       if (this.uColor == -1)
/* 243 */         throw new RuntimeException("Could not get uniform location for uColor");
/*     */     }
/*     */ 
/*     */     private int loadShader(int shaderType, String source)
/*     */     {
/* 248 */       int shader = GLES20.glCreateShader(shaderType);
/* 249 */       if (shader != 0) {
/* 250 */         GLES20.glShaderSource(shader, source);
/* 251 */         GLES20.glCompileShader(shader);
/* 252 */         int[] compiled = new int[1];
/* 253 */         GLES20.glGetShaderiv(shader, 35713, compiled, 0);
/* 254 */         if (compiled[0] == 0) {
/* 255 */           int i = shaderType; Log.e(UiLayer.TAG, 37 + "Could not compile shader " + i + ":");
/* 256 */           Log.e(UiLayer.TAG, GLES20.glGetShaderInfoLog(shader));
/* 257 */           GLES20.glDeleteShader(shader);
/* 258 */           shader = 0;
/*     */         }
/*     */       }
/* 261 */       return shader;
/*     */     }
/*     */ 
/*     */     private int createProgram(String vertexSource, String fragmentSource) {
/* 265 */       int vertexShader = loadShader(35633, vertexSource);
/* 266 */       if (vertexShader == 0) {
/* 267 */         return 0;
/*     */       }
/* 269 */       int pixelShader = loadShader(35632, fragmentSource);
/* 270 */       if (pixelShader == 0) {
/* 271 */         return 0;
/*     */       }
/*     */ 
/* 274 */       int program = GLES20.glCreateProgram();
/* 275 */       if (program != 0) {
/* 276 */         GLES20.glAttachShader(program, vertexShader);
/* 277 */         UiLayer.checkGlError("glAttachShader");
/* 278 */         GLES20.glAttachShader(program, pixelShader);
/* 279 */         UiLayer.checkGlError("glAttachShader");
/* 280 */         GLES20.glLinkProgram(program);
/* 281 */         int[] linkStatus = new int[1];
/* 282 */         GLES20.glGetProgramiv(program, 35714, linkStatus, 0);
/* 283 */         if (linkStatus[0] != 1) {
/* 284 */           Log.e(UiLayer.TAG, "Could not link program: ");
/* 285 */           Log.e(UiLayer.TAG, GLES20.glGetProgramInfoLog(program));
/* 286 */           GLES20.glDeleteProgram(program);
/* 287 */           program = 0;
/*     */         }
/* 289 */         UiLayer.checkGlError("glLinkProgram");
/*     */       }
/* 291 */       return program;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.UiLayer
 * JD-Core Version:    0.6.2
 */