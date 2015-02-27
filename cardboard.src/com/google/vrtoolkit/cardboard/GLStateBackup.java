/*     */ package com.google.vrtoolkit.cardboard;
/*     */ 
/*     */ import android.opengl.GLES20;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ class GLStateBackup
/*     */ {
/*     */   private boolean mCullFaceEnabled;
/*     */   private boolean mScissorTestEnabled;
/*     */   private boolean mDepthTestEnabled;
/*     */   private IntBuffer mViewport;
/*     */   private IntBuffer mTexture2dId;
/*     */   private IntBuffer mTextureUnit;
/*     */   private IntBuffer mScissorBox;
/*     */   private IntBuffer mShaderProgram;
/*     */   private IntBuffer mArrayBufferBinding;
/*     */   private IntBuffer mElementArrayBufferBinding;
/*     */   private FloatBuffer mClearColor;
/*     */   private ArrayList<VertexAttributeState> mVertexAttributes;
/*     */ 
/*     */   GLStateBackup()
/*     */   {
/*  19 */     this.mViewport = IntBuffer.allocate(4);
/*  20 */     this.mTexture2dId = IntBuffer.allocate(1);
/*  21 */     this.mTextureUnit = IntBuffer.allocate(1);
/*  22 */     this.mScissorBox = IntBuffer.allocate(4);
/*  23 */     this.mShaderProgram = IntBuffer.allocate(1);
/*  24 */     this.mArrayBufferBinding = IntBuffer.allocate(1);
/*  25 */     this.mElementArrayBufferBinding = IntBuffer.allocate(1);
/*  26 */     this.mClearColor = FloatBuffer.allocate(4);
/*     */ 
/*  28 */     this.mVertexAttributes = new ArrayList();
/*     */   }
/*     */ 
/*     */   void addTrackedVertexAttribute(int attributeId)
/*     */   {
/*  60 */     this.mVertexAttributes.add(new VertexAttributeState(attributeId));
/*     */   }
/*     */ 
/*     */   void clearTrackedVertexAttributes() {
/*  64 */     this.mVertexAttributes.clear();
/*     */   }
/*     */ 
/*     */   void readFromGL()
/*     */   {
/*  72 */     GLES20.glGetIntegerv(2978, this.mViewport);
/*     */ 
/*  75 */     this.mCullFaceEnabled = GLES20.glIsEnabled(2884);
/*  76 */     this.mScissorTestEnabled = GLES20.glIsEnabled(3089);
/*  77 */     this.mDepthTestEnabled = GLES20.glIsEnabled(2929);
/*     */ 
/*  80 */     GLES20.glGetFloatv(3106, this.mClearColor);
/*     */ 
/*  83 */     GLES20.glGetIntegerv(35725, this.mShaderProgram);
/*     */ 
/*  86 */     GLES20.glGetIntegerv(3088, this.mScissorBox);
/*     */ 
/*  89 */     GLES20.glGetIntegerv(34016, this.mTextureUnit);
/*  90 */     GLES20.glGetIntegerv(32873, this.mTexture2dId);
/*     */ 
/*  93 */     GLES20.glGetIntegerv(34964, this.mArrayBufferBinding);
/*  94 */     GLES20.glGetIntegerv(34965, this.mElementArrayBufferBinding);
/*     */ 
/*  98 */     for (VertexAttributeState vas : this.mVertexAttributes)
/*  99 */       vas.readFromGL();
/*     */   }
/*     */ 
/*     */   void writeToGL()
/*     */   {
/* 108 */     for (VertexAttributeState vas : this.mVertexAttributes) {
/* 109 */       vas.writeToGL();
/*     */     }
/*     */ 
/* 113 */     GLES20.glBindBuffer(34962, this.mArrayBufferBinding.array()[0]);
/* 114 */     GLES20.glBindBuffer(34963, this.mElementArrayBufferBinding.array()[0]);
/*     */ 
/* 118 */     GLES20.glBindTexture(3553, this.mTexture2dId.array()[0]);
/* 119 */     GLES20.glActiveTexture(this.mTextureUnit.array()[0]);
/*     */ 
/* 122 */     GLES20.glScissor(this.mScissorBox.array()[0], this.mScissorBox.array()[1], this.mScissorBox.array()[2], this.mScissorBox.array()[3]);
/*     */ 
/* 126 */     GLES20.glUseProgram(this.mShaderProgram.array()[0]);
/*     */ 
/* 129 */     GLES20.glClearColor(this.mClearColor.array()[0], this.mClearColor.array()[1], this.mClearColor.array()[2], this.mClearColor.array()[3]);
/*     */ 
/* 133 */     if (this.mCullFaceEnabled)
/* 134 */       GLES20.glEnable(2884);
/*     */     else {
/* 136 */       GLES20.glDisable(2884);
/*     */     }
/* 138 */     if (this.mScissorTestEnabled)
/* 139 */       GLES20.glEnable(3089);
/*     */     else {
/* 141 */       GLES20.glDisable(3089);
/*     */     }
/* 143 */     if (this.mDepthTestEnabled)
/* 144 */       GLES20.glEnable(2929);
/*     */     else {
/* 146 */       GLES20.glDisable(2929);
/*     */     }
/*     */ 
/* 150 */     GLES20.glViewport(this.mViewport.array()[0], this.mViewport.array()[1], this.mViewport.array()[2], this.mViewport.array()[3]);
/*     */   }
/*     */ 
/*     */   private class VertexAttributeState
/*     */   {
/*     */     private int mAttributeId;
/*  38 */     private IntBuffer mEnabled = IntBuffer.allocate(1);
/*     */ 
/*     */     VertexAttributeState(int attributeId) {
/*  41 */       this.mAttributeId = attributeId;
/*     */     }
/*     */ 
/*     */     void readFromGL() {
/*  45 */       GLES20.glGetVertexAttribiv(this.mAttributeId, 34338, this.mEnabled);
/*     */     }
/*     */ 
/*     */     void writeToGL()
/*     */     {
/*  51 */       if (this.mEnabled.array()[0] == 0)
/*  52 */         GLES20.glDisableVertexAttribArray(this.mAttributeId);
/*     */       else
/*  54 */         GLES20.glEnableVertexAttribArray(this.mAttributeId);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.GLStateBackup
 * JD-Core Version:    0.6.2
 */