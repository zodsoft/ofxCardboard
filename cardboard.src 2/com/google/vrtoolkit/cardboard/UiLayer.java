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
/*     */   private final int touchWidthPx;
/*  43 */   private volatile Rect touchRect = new Rect();
/*     */   private boolean downWithinBounds;
/*     */   private Context context;
/*     */   private final GLStateBackup glStateBackup;
/*     */   private final ShaderProgram shader;
/*     */   private final SettingsButtonRenderer settingsButtonRenderer;
/*     */   private final AlignmentMarkerRenderer alignmentMarkerRenderer;
/*     */   private Viewport viewport;
/*  57 */   private boolean shouldUpdateViewport = true;
/*     */ 
/*  60 */   private boolean settingsButtonEnabled = true;
/*     */ 
/*  63 */   private boolean alignmentMarkerEnabled = true;
/*     */   private boolean initialized;
/*     */ 
/*     */   UiLayer(Context context)
/*     */   {
/*  75 */     this.context = context;
/*  76 */     float density = context.getResources().getDisplayMetrics().density;
/*  77 */     int buttonWidthPx = (int)(28.0F * density);
/*  78 */     this.touchWidthPx = ((int)(buttonWidthPx * 1.5F));
/*     */ 
/*  80 */     this.glStateBackup = new GLStateBackup();
/*  81 */     this.shader = new ShaderProgram(null);
/*  82 */     this.settingsButtonRenderer = new SettingsButtonRenderer(this.shader, buttonWidthPx);
/*  83 */     this.alignmentMarkerRenderer = new AlignmentMarkerRenderer(this.shader, this.touchWidthPx, 4.0F * density);
/*     */ 
/*  85 */     this.viewport = new Viewport();
/*     */   }
/*     */ 
/*     */   void updateViewport(Viewport viewport) {
/*  89 */     synchronized (this) {
/*  90 */       if (this.viewport.equals(viewport)) {
/*  91 */         return;
/*     */       }
/*  93 */       int w = viewport.width;
/*  94 */       int h = viewport.height;
/*  95 */       this.touchRect = new Rect((w - this.touchWidthPx) / 2, h - this.touchWidthPx, (w + this.touchWidthPx) / 2, h);
/*     */ 
/*  97 */       this.viewport.setViewport(viewport.x, viewport.y, viewport.width, viewport.height);
/*  98 */       this.shouldUpdateViewport = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   void initializeGl() {
/* 103 */     this.shader.initializeGl();
/* 104 */     this.glStateBackup.clearTrackedVertexAttributes();
/* 105 */     this.glStateBackup.addTrackedVertexAttribute(this.shader.aPosition);
/*     */ 
/* 107 */     this.glStateBackup.readFromGL();
/* 108 */     this.settingsButtonRenderer.initializeGl();
/* 109 */     this.alignmentMarkerRenderer.initializeGl();
/* 110 */     this.glStateBackup.writeToGL();
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
/* 127 */     this.glStateBackup.readFromGL();
/*     */ 
/* 129 */     synchronized (this)
/*     */     {
/* 131 */       if (this.shouldUpdateViewport) {
/* 132 */         this.shouldUpdateViewport = false;
/* 133 */         this.settingsButtonRenderer.updateViewport(this.viewport);
/* 134 */         this.alignmentMarkerRenderer.updateViewport(this.viewport);
/*     */       }
/* 136 */       this.viewport.setGLViewport();
/*     */     }
/*     */ 
/* 139 */     if (getSettingsButtonEnabled()) {
/* 140 */       this.settingsButtonRenderer.draw();
/*     */     }
/* 142 */     if (getAlignmentMarkerEnabled()) {
/* 143 */       this.alignmentMarkerRenderer.draw();
/*     */     }
/*     */ 
/* 146 */     this.glStateBackup.writeToGL();
/*     */   }
/*     */ 
/*     */   synchronized void setAlignmentMarkerEnabled(boolean enabled) {
/* 150 */     if (this.alignmentMarkerEnabled != enabled) {
/* 151 */       this.alignmentMarkerEnabled = enabled;
/* 152 */       this.shouldUpdateViewport = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized boolean getAlignmentMarkerEnabled() {
/* 157 */     return this.alignmentMarkerEnabled;
/*     */   }
/*     */ 
/*     */   synchronized void setSettingsButtonEnabled(boolean enabled) {
/* 161 */     if (this.settingsButtonEnabled != enabled) {
/* 162 */       this.settingsButtonEnabled = enabled;
/* 163 */       this.shouldUpdateViewport = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized boolean getSettingsButtonEnabled() {
/* 168 */     return this.settingsButtonEnabled;
/*     */   }
/*     */ 
/*     */   boolean onTouchEvent(MotionEvent e) {
/* 172 */     boolean touchWithinBounds = false;
/* 173 */     synchronized (this) {
/* 174 */       if (!this.settingsButtonEnabled) {
/* 175 */         return false;
/*     */       }
/* 177 */       touchWithinBounds = this.touchRect.contains((int)e.getX(), (int)e.getY());
/*     */     }
/*     */ 
/* 180 */     if ((e.getActionMasked() == 0) && (touchWithinBounds)) {
/* 181 */       this.downWithinBounds = true;
/*     */     }
/* 183 */     if (!this.downWithinBounds) {
/* 184 */       return false;
/*     */     }
/* 186 */     if (e.getActionMasked() == 1) {
/* 187 */       if (touchWithinBounds) {
/* 188 */         UiUtils.launchOrInstallCardboard(this.context);
/*     */       }
/* 190 */       this.downWithinBounds = false;
/* 191 */     } else if (e.getActionMasked() == 3) {
/* 192 */       this.downWithinBounds = false;
/*     */     }
/* 194 */     setPressed((this.downWithinBounds) && (touchWithinBounds));
/* 195 */     return true;
/*     */   }
/*     */ 
/*     */   private void setPressed(boolean pressed) {
/* 199 */     if (this.settingsButtonRenderer != null)
/* 200 */       this.settingsButtonRenderer.setColor(pressed ? -12303292 : -3355444);
/*     */   }
/*     */ 
/*     */   private static void clearGlError()
/*     */   {
/* 547 */     while (GLES20.glGetError() != 0);
/*     */   }
/*     */ 
/*     */   private static void checkGlError(String op)
/*     */   {
/*     */     int error;
/* 554 */     if ((error = GLES20.glGetError()) != 0) {
/* 555 */       String str1 = String.valueOf(String.valueOf(op)); int i = error; Log.e(TAG, 21 + str1.length() + str1 + ": glError " + i);
/* 556 */       String str2 = String.valueOf(String.valueOf(op)); int j = error; throw new RuntimeException(21 + str2.length() + str2 + ": glError " + j);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static float lerp(float a, float b, float t) {
/* 561 */     return a * (1.0F - t) + b * t;
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
/*     */     private int buttonWidthPx;
/* 461 */     private int color = -3355444;
/*     */ 
/*     */     SettingsButtonRenderer(UiLayer.ShaderProgram shader, int buttonWidthPx) {
/* 464 */       super();
/* 465 */       this.buttonWidthPx = buttonWidthPx;
/*     */     }
/*     */ 
/*     */     void initializeGl() {
/* 469 */       float[] vertexData = new float[120];
/* 470 */       int numVerticesPerRim = 30;
/*     */ 
/* 473 */       float lerpInterval = 8.0F;
/* 474 */       for (int i = 0; i < numVerticesPerRim; i++) {
/* 475 */         float theta = i / numVerticesPerRim * 360.0F;
/* 476 */         float mod = theta % 60.0F;
/*     */         float r;
/*     */         float r;
/* 478 */         if (mod <= 12.0F) {
/* 479 */           r = 1.0F;
/*     */         }
/*     */         else
/*     */         {
/*     */           float r;
/* 480 */           if (mod <= 20.0F) {
/* 481 */             r = UiLayer.lerp(1.0F, 0.75F, (mod - 12.0F) / lerpInterval);
/*     */           }
/*     */           else
/*     */           {
/*     */             float r;
/* 482 */             if (mod <= 40.0F) {
/* 483 */               r = 0.75F;
/*     */             }
/*     */             else
/*     */             {
/*     */               float r;
/* 484 */               if (mod <= 48.0F) {
/* 485 */                 r = UiLayer.lerp(0.75F, 1.0F, (mod - 60.0F + 20.0F) / lerpInterval);
/*     */               }
/*     */               else
/* 488 */                 r = 1.0F; 
/*     */             }
/*     */           }
/*     */         }
/* 491 */         vertexData[(2 * i)] = (r * (float)Math.cos(Math.toRadians(90.0F - theta)));
/*     */ 
/* 493 */         vertexData[(2 * i + 1)] = (r * (float)Math.sin(Math.toRadians(90.0F - theta)));
/*     */       }
/*     */ 
/* 499 */       int innerStartingIndex = 2 * numVerticesPerRim;
/* 500 */       for (int i = 0; i < numVerticesPerRim; i++) {
/* 501 */         float theta = i / numVerticesPerRim * 360.0F;
/* 502 */         vertexData[(innerStartingIndex + 2 * i)] = (0.3125F * (float)Math.cos(Math.toRadians(90.0F - theta)));
/*     */ 
/* 504 */         vertexData[(innerStartingIndex + 2 * i + 1)] = (0.3125F * (float)Math.sin(Math.toRadians(90.0F - theta)));
/*     */       }
/*     */ 
/* 510 */       short[] indexData = new short[62];
/* 511 */       for (int i = 0; i < numVerticesPerRim; i++) {
/* 512 */         indexData[(2 * i)] = ((short)i);
/* 513 */         indexData[(2 * i + 1)] = ((short)(numVerticesPerRim + i));
/*     */       }
/* 515 */       indexData[(indexData.length - 2)] = 0;
/* 516 */       indexData[(indexData.length - 1)] = ((short)numVerticesPerRim);
/*     */ 
/* 518 */       genAndBindBuffers(vertexData, indexData);
/*     */     }
/*     */ 
/*     */     synchronized void setColor(int color) {
/* 522 */       this.color = color;
/*     */     }
/*     */ 
/*     */     void updateViewport(Viewport viewport)
/*     */     {
/* 527 */       Matrix.setIdentityM(this.mvp, 0);
/* 528 */       float yScale = this.buttonWidthPx / viewport.height;
/* 529 */       float xScale = yScale * viewport.height / viewport.width;
/* 530 */       Matrix.translateM(this.mvp, 0, 0.0F, yScale - 1.0F, 0.0F);
/* 531 */       Matrix.scaleM(this.mvp, 0, xScale, yScale, 1.0F);
/*     */     }
/*     */ 
/*     */     void draw()
/*     */     {
/* 536 */       GLES20.glUseProgram(this.shader.program);
/* 537 */       synchronized (this) {
/* 538 */         GLES20.glUniform4f(this.shader.uColor, Color.red(this.color) / 255.0F, Color.green(this.color) / 255.0F, Color.blue(this.color) / 255.0F, Color.alpha(this.color) / 255.0F);
/*     */       }
/*     */ 
/* 542 */       super.draw();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class AlignmentMarkerRenderer extends UiLayer.MeshRenderer
/*     */   {
/* 378 */     private static final int COLOR = Color.argb(255, 50, 50, 50);
/*     */     private float verticalBorderPaddingPx;
/*     */     private float lineThicknessPx;
/*     */ 
/*     */     AlignmentMarkerRenderer(UiLayer.ShaderProgram shader, float verticalBorderPaddingPx, float lineThicknessPx)
/*     */     {
/* 385 */       super();
/* 386 */       this.verticalBorderPaddingPx = verticalBorderPaddingPx;
/* 387 */       this.lineThicknessPx = lineThicknessPx;
/*     */     }
/*     */ 
/*     */     void initializeGl() {
/* 391 */       float[] vertexData = { 1.0F, 1.0F, -1.0F, 1.0F, 1.0F, -1.0F, -1.0F, -1.0F };
/*     */ 
/* 397 */       short[] indexData = new short[vertexData.length / 2];
/* 398 */       for (int i = 0; i < indexData.length; i++) {
/* 399 */         indexData[i] = ((short)i);
/*     */       }
/* 401 */       genAndBindBuffers(vertexData, indexData);
/*     */     }
/*     */ 
/*     */     void updateViewport(Viewport viewport)
/*     */     {
/* 406 */       Matrix.setIdentityM(this.mvp, 0);
/* 407 */       float xScale = this.lineThicknessPx / viewport.width;
/* 408 */       float yScale = 1.0F - 2.0F * this.verticalBorderPaddingPx / viewport.height;
/* 409 */       Matrix.scaleM(this.mvp, 0, xScale, yScale, 1.0F);
/*     */     }
/*     */ 
/*     */     void draw()
/*     */     {
/* 414 */       GLES20.glUseProgram(this.shader.program);
/* 415 */       GLES20.glUniform4f(this.shader.uColor, Color.red(COLOR) / 255.0F, Color.green(COLOR) / 255.0F, Color.blue(COLOR) / 255.0F, Color.alpha(COLOR) / 255.0F);
/*     */ 
/* 418 */       super.draw();
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
/* 306 */     protected int arrayBufferId = -1;
/* 307 */     protected int elementBufferId = -1;
/*     */     protected UiLayer.ShaderProgram shader;
/* 309 */     protected float[] mvp = new float[16];
/*     */     private int numIndices;
/*     */ 
/*     */     MeshRenderer(UiLayer.ShaderProgram shader)
/*     */     {
/* 314 */       this.shader = shader;
/*     */     }
/*     */ 
/*     */     void genAndBindBuffers(float[] vertexData, short[] indexData) {
/* 318 */       FloatBuffer vertexBuffer = ByteBuffer.allocateDirect(vertexData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
/*     */ 
/* 321 */       vertexBuffer.put(vertexData).position(0);
/*     */ 
/* 323 */       this.numIndices = indexData.length;
/* 324 */       ShortBuffer indexBuffer = ByteBuffer.allocateDirect(this.numIndices * 4).order(ByteOrder.nativeOrder()).asShortBuffer();
/*     */ 
/* 326 */       indexBuffer.put(indexData).position(0);
/*     */ 
/* 328 */       int[] bufferIds = new int[2];
/* 329 */       GLES20.glGenBuffers(2, bufferIds, 0);
/* 330 */       this.arrayBufferId = bufferIds[0];
/* 331 */       this.elementBufferId = bufferIds[1];
/*     */ 
/* 333 */       UiLayer.access$100();
/*     */ 
/* 335 */       GLES20.glBindBuffer(34962, this.arrayBufferId);
/* 336 */       GLES20.glBufferData(34962, vertexData.length * 4, vertexBuffer, 35044);
/*     */ 
/* 338 */       GLES20.glBindBuffer(34963, this.elementBufferId);
/* 339 */       GLES20.glBufferData(34963, indexData.length * 4, indexBuffer, 35044);
/*     */ 
/* 342 */       UiLayer.checkGlError("genAndBindBuffers");
/*     */     }
/*     */ 
/*     */     void updateViewport(Viewport viewport) {
/* 346 */       Matrix.setIdentityM(this.mvp, 0);
/*     */     }
/*     */ 
/*     */     void draw() {
/* 350 */       GLES20.glDisable(2929);
/* 351 */       GLES20.glDisable(2884);
/*     */ 
/* 354 */       GLES20.glUseProgram(this.shader.program);
/* 355 */       GLES20.glUniformMatrix4fv(this.shader.uMvpMatrix, 1, false, this.mvp, 0);
/*     */ 
/* 357 */       GLES20.glBindBuffer(34962, this.arrayBufferId);
/* 358 */       GLES20.glVertexAttribPointer(this.shader.aPosition, 2, 5126, false, 8, 0);
/*     */ 
/* 365 */       GLES20.glEnableVertexAttribArray(this.shader.aPosition);
/* 366 */       GLES20.glBindBuffer(34963, this.elementBufferId);
/*     */ 
/* 369 */       GLES20.glDrawElements(5, this.numIndices, 5123, 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class ShaderProgram
/*     */   {
/*     */     private static final String VERTEX_SHADER = "uniform mat4 uMVPMatrix;\nattribute vec2 aPosition;\nvoid main() {\n  gl_Position = uMVPMatrix * vec4(aPosition, 0.0, 1.0);\n}\n";
/*     */     private static final String FRAGMENT_SHADER = "precision mediump float;\nuniform vec4 uColor;\nvoid main() {\n  gl_FragColor = uColor;\n}\n";
/*     */     public int program;
/*     */     public int aPosition;
/*     */     public int uMvpMatrix;
/*     */     public int uColor;
/*     */ 
/*     */     void initializeGl()
/*     */     {
/* 225 */       this.program = createProgram("uniform mat4 uMVPMatrix;\nattribute vec2 aPosition;\nvoid main() {\n  gl_Position = uMVPMatrix * vec4(aPosition, 0.0, 1.0);\n}\n", "precision mediump float;\nuniform vec4 uColor;\nvoid main() {\n  gl_FragColor = uColor;\n}\n");
/* 226 */       if (this.program == 0) {
/* 227 */         throw new RuntimeException("Could not create program");
/*     */       }
/* 229 */       UiLayer.access$100();
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
/* 276 */         UiLayer.access$100();
/* 277 */         GLES20.glAttachShader(program, vertexShader);
/* 278 */         UiLayer.checkGlError("glAttachShader");
/* 279 */         GLES20.glAttachShader(program, pixelShader);
/* 280 */         UiLayer.checkGlError("glAttachShader");
/* 281 */         GLES20.glLinkProgram(program);
/* 282 */         int[] linkStatus = new int[1];
/* 283 */         GLES20.glGetProgramiv(program, 35714, linkStatus, 0);
/* 284 */         if (linkStatus[0] != 1) {
/* 285 */           Log.e(UiLayer.TAG, "Could not link program: ");
/* 286 */           Log.e(UiLayer.TAG, GLES20.glGetProgramInfoLog(program));
/* 287 */           GLES20.glDeleteProgram(program);
/* 288 */           program = 0;
/*     */         }
/* 290 */         UiLayer.checkGlError("glLinkProgram");
/*     */       }
/* 292 */       return program;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.UiLayer
 * JD-Core Version:    0.6.2
 */