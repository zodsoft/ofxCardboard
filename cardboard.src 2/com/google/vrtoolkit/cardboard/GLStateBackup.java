/*     */ package com.google.vrtoolkit.cardboard;
/*     */ 
/*     */ import android.opengl.GLES20;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ class GLStateBackup
/*     */ {
/*     */   private boolean cullFaceEnabled;
/*     */   private boolean scissorTestEnabled;
/*     */   private boolean depthTestEnabled;
/*     */   private IntBuffer viewport;
/*     */   private IntBuffer texture2dId;
/*     */   private IntBuffer textureUnit;
/*     */   private IntBuffer scissorBox;
/*     */   private IntBuffer shaderProgram;
/*     */   private IntBuffer arrayBufferBinding;
/*     */   private IntBuffer elementArrayBufferBinding;
/*     */   private FloatBuffer clearColor;
/*     */   private ArrayList<VertexAttributeState> vertexAttributes;
/*     */ 
/*     */   GLStateBackup()
/*     */   {
/*  19 */     this.viewport = IntBuffer.allocate(4);
/*  20 */     this.texture2dId = IntBuffer.allocate(1);
/*  21 */     this.textureUnit = IntBuffer.allocate(1);
/*  22 */     this.scissorBox = IntBuffer.allocate(4);
/*  23 */     this.shaderProgram = IntBuffer.allocate(1);
/*  24 */     this.arrayBufferBinding = IntBuffer.allocate(1);
/*  25 */     this.elementArrayBufferBinding = IntBuffer.allocate(1);
/*  26 */     this.clearColor = FloatBuffer.allocate(4);
/*     */ 
/*  28 */     this.vertexAttributes = new ArrayList();
/*     */   }
/*     */ 
/*     */   void addTrackedVertexAttribute(int attributeId)
/*     */   {
/*  60 */     this.vertexAttributes.add(new VertexAttributeState(attributeId));
/*     */   }
/*     */ 
/*     */   void clearTrackedVertexAttributes() {
/*  64 */     this.vertexAttributes.clear();
/*     */   }
/*     */ 
/*     */   void readFromGL()
/*     */   {
/*  72 */     GLES20.glGetIntegerv(2978, this.viewport);
/*     */ 
/*  75 */     this.cullFaceEnabled = GLES20.glIsEnabled(2884);
/*  76 */     this.scissorTestEnabled = GLES20.glIsEnabled(3089);
/*  77 */     this.depthTestEnabled = GLES20.glIsEnabled(2929);
/*     */ 
/*  80 */     GLES20.glGetFloatv(3106, this.clearColor);
/*     */ 
/*  83 */     GLES20.glGetIntegerv(35725, this.shaderProgram);
/*     */ 
/*  86 */     GLES20.glGetIntegerv(3088, this.scissorBox);
/*     */ 
/*  89 */     GLES20.glGetIntegerv(34016, this.textureUnit);
/*  90 */     GLES20.glGetIntegerv(32873, this.texture2dId);
/*     */ 
/*  93 */     GLES20.glGetIntegerv(34964, this.arrayBufferBinding);
/*  94 */     GLES20.glGetIntegerv(34965, this.elementArrayBufferBinding);
/*     */ 
/*  98 */     for (VertexAttributeState vas : this.vertexAttributes)
/*  99 */       vas.readFromGL();
/*     */   }
/*     */ 
/*     */   void writeToGL()
/*     */   {
/* 108 */     for (VertexAttributeState vas : this.vertexAttributes) {
/* 109 */       vas.writeToGL();
/*     */     }
/*     */ 
/* 113 */     GLES20.glBindBuffer(34962, this.arrayBufferBinding.array()[0]);
/* 114 */     GLES20.glBindBuffer(34963, this.elementArrayBufferBinding.array()[0]);
/*     */ 
/* 118 */     GLES20.glBindTexture(3553, this.texture2dId.array()[0]);
/* 119 */     GLES20.glActiveTexture(this.textureUnit.array()[0]);
/*     */ 
/* 122 */     GLES20.glScissor(this.scissorBox.array()[0], this.scissorBox.array()[1], this.scissorBox.array()[2], this.scissorBox.array()[3]);
/*     */ 
/* 126 */     GLES20.glUseProgram(this.shaderProgram.array()[0]);
/*     */ 
/* 129 */     GLES20.glClearColor(this.clearColor.array()[0], this.clearColor.array()[1], this.clearColor.array()[2], this.clearColor.array()[3]);
/*     */ 
/* 133 */     if (this.cullFaceEnabled)
/* 134 */       GLES20.glEnable(2884);
/*     */     else {
/* 136 */       GLES20.glDisable(2884);
/*     */     }
/* 138 */     if (this.scissorTestEnabled)
/* 139 */       GLES20.glEnable(3089);
/*     */     else {
/* 141 */       GLES20.glDisable(3089);
/*     */     }
/* 143 */     if (this.depthTestEnabled)
/* 144 */       GLES20.glEnable(2929);
/*     */     else {
/* 146 */       GLES20.glDisable(2929);
/*     */     }
/*     */ 
/* 150 */     GLES20.glViewport(this.viewport.array()[0], this.viewport.array()[1], this.viewport.array()[2], this.viewport.array()[3]);
/*     */   }
/*     */ 
/*     */   private class VertexAttributeState
/*     */   {
/*     */     private int attributeId;
/*  38 */     private IntBuffer enabled = IntBuffer.allocate(1);
/*     */ 
/*     */     VertexAttributeState(int attributeId) {
/*  41 */       this.attributeId = attributeId;
/*     */     }
/*     */ 
/*     */     void readFromGL() {
/*  45 */       GLES20.glGetVertexAttribiv(this.attributeId, 34338, this.enabled);
/*     */     }
/*     */ 
/*     */     void writeToGL()
/*     */     {
/*  51 */       if (this.enabled.array()[0] == 0)
/*  52 */         GLES20.glDisableVertexAttribArray(this.attributeId);
/*     */       else
/*  54 */         GLES20.glEnableVertexAttribArray(this.attributeId);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.GLStateBackup
 * JD-Core Version:    0.6.2
 */