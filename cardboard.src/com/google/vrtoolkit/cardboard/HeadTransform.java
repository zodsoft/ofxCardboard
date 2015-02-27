/*     */ package com.google.vrtoolkit.cardboard;
/*     */ 
/*     */ import android.opengl.Matrix;
/*     */ import android.util.FloatMath;
/*     */ 
/*     */ public class HeadTransform
/*     */ {
/*     */   private static final float GIMBAL_LOCK_EPSILON = 0.01F;
/*     */   private static final float PI = 3.141593F;
/*     */   private final float[] mHeadView;
/*     */ 
/*     */   public HeadTransform()
/*     */   {
/*  35 */     this.mHeadView = new float[16];
/*  36 */     Matrix.setIdentityM(this.mHeadView, 0);
/*     */   }
/*     */ 
/*     */   float[] getHeadView()
/*     */   {
/*  48 */     return this.mHeadView;
/*     */   }
/*     */ 
/*     */   public void getHeadView(float[] headView, int offset)
/*     */   {
/*  62 */     if (offset + 16 > headView.length) {
/*  63 */       throw new IllegalArgumentException("Not enough space to write the result");
/*     */     }
/*     */ 
/*  66 */     System.arraycopy(this.mHeadView, 0, headView, offset, 16);
/*     */   }
/*     */ 
/*     */   public void getForwardVector(float[] forward, int offset)
/*     */   {
/*  81 */     if (offset + 3 > forward.length) {
/*  82 */       throw new IllegalArgumentException("Not enough space to write the result");
/*     */     }
/*     */ 
/*  86 */     for (int i = 0; i < 3; i++)
/*  87 */       forward[(i + offset)] = (-this.mHeadView[(8 + i)]);
/*     */   }
/*     */ 
/*     */   public void getUpVector(float[] up, int offset)
/*     */   {
/* 100 */     if (offset + 3 > up.length) {
/* 101 */       throw new IllegalArgumentException("Not enough space to write the result");
/*     */     }
/*     */ 
/* 105 */     for (int i = 0; i < 3; i++)
/* 106 */       up[(i + offset)] = this.mHeadView[(4 + i)];
/*     */   }
/*     */ 
/*     */   public void getRightVector(float[] right, int offset)
/*     */   {
/* 119 */     if (offset + 3 > right.length) {
/* 120 */       throw new IllegalArgumentException("Not enough space to write the result");
/*     */     }
/*     */ 
/* 124 */     for (int i = 0; i < 3; i++)
/* 125 */       right[(i + offset)] = this.mHeadView[i];
/*     */   }
/*     */ 
/*     */   public void getQuaternion(float[] quaternion, int offset)
/*     */   {
/* 138 */     if (offset + 4 > quaternion.length) {
/* 139 */       throw new IllegalArgumentException("Not enough space to write the result");
/*     */     }
/*     */ 
/* 143 */     float[] m = this.mHeadView;
/* 144 */     float t = m[0] + m[5] + m[10];
/*     */     float z;
/*     */     float z;
/*     */     float x;
/*     */     float y;
/*     */     float w;
/* 147 */     if (t >= 0.0F) {
/* 148 */       float s = FloatMath.sqrt(t + 1.0F);
/* 149 */       float w = 0.5F * s;
/* 150 */       s = 0.5F / s;
/* 151 */       float x = (m[9] - m[6]) * s;
/* 152 */       float y = (m[2] - m[8]) * s;
/* 153 */       z = (m[4] - m[1]) * s;
/*     */     }
/*     */     else
/*     */     {
/*     */       float w;
/* 155 */       if ((m[0] > m[5]) && (m[0] > m[10])) {
/* 156 */         float s = FloatMath.sqrt(1.0F + m[0] - m[5] - m[10]);
/* 157 */         float x = s * 0.5F;
/* 158 */         s = 0.5F / s;
/* 159 */         float y = (m[4] + m[1]) * s;
/* 160 */         float z = (m[2] + m[8]) * s;
/* 161 */         w = (m[9] - m[6]) * s;
/*     */       }
/*     */       else
/*     */       {
/*     */         float w;
/* 163 */         if (m[5] > m[10]) {
/* 164 */           float s = FloatMath.sqrt(1.0F + m[5] - m[0] - m[10]);
/* 165 */           float y = s * 0.5F;
/* 166 */           s = 0.5F / s;
/* 167 */           float x = (m[4] + m[1]) * s;
/* 168 */           float z = (m[9] + m[6]) * s;
/* 169 */           w = (m[2] - m[8]) * s;
/*     */         }
/*     */         else {
/* 172 */           float s = FloatMath.sqrt(1.0F + m[10] - m[0] - m[5]);
/* 173 */           z = s * 0.5F;
/* 174 */           s = 0.5F / s;
/* 175 */           x = (m[2] + m[8]) * s;
/* 176 */           y = (m[9] + m[6]) * s;
/* 177 */           w = (m[4] - m[1]) * s;
/*     */         }
/*     */       }
/*     */     }
/* 180 */     quaternion[(offset + 0)] = x;
/* 181 */     quaternion[(offset + 1)] = y;
/* 182 */     quaternion[(offset + 2)] = z;
/* 183 */     quaternion[(offset + 3)] = w;
/*     */   }
/*     */ 
/*     */   public void getEulerAngles(float[] eulerAngles, int offset)
/*     */   {
/* 211 */     if (offset + 3 > eulerAngles.length) {
/* 212 */       throw new IllegalArgumentException("Not enough space to write the result");
/*     */     }
/*     */ 
/* 216 */     float pitch = (float)Math.asin(this.mHeadView[6]);
/*     */     float roll;
/*     */     float yaw;
/*     */     float roll;
/* 220 */     if (FloatMath.sqrt(1.0F - this.mHeadView[6] * this.mHeadView[6]) >= 0.01F)
/*     */     {
/* 223 */       float yaw = (float)Math.atan2(-this.mHeadView[2], this.mHeadView[10]);
/* 224 */       roll = (float)Math.atan2(-this.mHeadView[4], this.mHeadView[5]);
/*     */     }
/*     */     else
/*     */     {
/* 228 */       yaw = 0.0F;
/* 229 */       roll = (float)Math.atan2(this.mHeadView[1], this.mHeadView[0]);
/*     */     }
/*     */ 
/* 232 */     eulerAngles[(offset + 0)] = (-pitch);
/* 233 */     eulerAngles[(offset + 1)] = (-yaw);
/* 234 */     eulerAngles[(offset + 2)] = (-roll);
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.HeadTransform
 * JD-Core Version:    0.6.2
 */