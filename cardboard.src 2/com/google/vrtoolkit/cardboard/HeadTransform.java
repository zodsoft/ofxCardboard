/*     */ package com.google.vrtoolkit.cardboard;
/*     */ 
/*     */ import android.opengl.Matrix;
/*     */ import android.util.FloatMath;
/*     */ 
/*     */ public class HeadTransform
/*     */ {
/*     */   private static final float GIMBAL_LOCK_EPSILON = 0.01F;
/*     */   private final float[] headView;
/*     */ 
/*     */   public HeadTransform()
/*     */   {
/*  33 */     this.headView = new float[16];
/*  34 */     Matrix.setIdentityM(this.headView, 0);
/*     */   }
/*     */ 
/*     */   float[] getHeadView()
/*     */   {
/*  46 */     return this.headView;
/*     */   }
/*     */ 
/*     */   public void getHeadView(float[] headView, int offset)
/*     */   {
/*  60 */     if (offset + 16 > headView.length) {
/*  61 */       throw new IllegalArgumentException("Not enough space to write the result");
/*     */     }
/*     */ 
/*  64 */     System.arraycopy(this.headView, 0, headView, offset, 16);
/*     */   }
/*     */ 
/*     */   public void getForwardVector(float[] forward, int offset)
/*     */   {
/*  79 */     if (offset + 3 > forward.length) {
/*  80 */       throw new IllegalArgumentException("Not enough space to write the result");
/*     */     }
/*     */ 
/*  84 */     for (int i = 0; i < 3; i++)
/*  85 */       forward[(i + offset)] = (-this.headView[(8 + i)]);
/*     */   }
/*     */ 
/*     */   public void getUpVector(float[] up, int offset)
/*     */   {
/*  98 */     if (offset + 3 > up.length) {
/*  99 */       throw new IllegalArgumentException("Not enough space to write the result");
/*     */     }
/*     */ 
/* 103 */     for (int i = 0; i < 3; i++)
/* 104 */       up[(i + offset)] = this.headView[(4 + i)];
/*     */   }
/*     */ 
/*     */   public void getRightVector(float[] right, int offset)
/*     */   {
/* 117 */     if (offset + 3 > right.length) {
/* 118 */       throw new IllegalArgumentException("Not enough space to write the result");
/*     */     }
/*     */ 
/* 122 */     for (int i = 0; i < 3; i++)
/* 123 */       right[(i + offset)] = this.headView[i];
/*     */   }
/*     */ 
/*     */   public void getQuaternion(float[] quaternion, int offset)
/*     */   {
/* 136 */     if (offset + 4 > quaternion.length) {
/* 137 */       throw new IllegalArgumentException("Not enough space to write the result");
/*     */     }
/*     */ 
/* 141 */     float[] m = this.headView;
/* 142 */     float t = m[0] + m[5] + m[10];
/*     */     float z;
/*     */     float z;
/*     */     float x;
/*     */     float y;
/*     */     float w;
/* 145 */     if (t >= 0.0F) {
/* 146 */       float s = FloatMath.sqrt(t + 1.0F);
/* 147 */       float w = 0.5F * s;
/* 148 */       s = 0.5F / s;
/* 149 */       float x = (m[9] - m[6]) * s;
/* 150 */       float y = (m[2] - m[8]) * s;
/* 151 */       z = (m[4] - m[1]) * s;
/*     */     }
/*     */     else
/*     */     {
/*     */       float w;
/* 153 */       if ((m[0] > m[5]) && (m[0] > m[10])) {
/* 154 */         float s = FloatMath.sqrt(1.0F + m[0] - m[5] - m[10]);
/* 155 */         float x = s * 0.5F;
/* 156 */         s = 0.5F / s;
/* 157 */         float y = (m[4] + m[1]) * s;
/* 158 */         float z = (m[2] + m[8]) * s;
/* 159 */         w = (m[9] - m[6]) * s;
/*     */       }
/*     */       else
/*     */       {
/*     */         float w;
/* 161 */         if (m[5] > m[10]) {
/* 162 */           float s = FloatMath.sqrt(1.0F + m[5] - m[0] - m[10]);
/* 163 */           float y = s * 0.5F;
/* 164 */           s = 0.5F / s;
/* 165 */           float x = (m[4] + m[1]) * s;
/* 166 */           float z = (m[9] + m[6]) * s;
/* 167 */           w = (m[2] - m[8]) * s;
/*     */         }
/*     */         else {
/* 170 */           float s = FloatMath.sqrt(1.0F + m[10] - m[0] - m[5]);
/* 171 */           z = s * 0.5F;
/* 172 */           s = 0.5F / s;
/* 173 */           x = (m[2] + m[8]) * s;
/* 174 */           y = (m[9] + m[6]) * s;
/* 175 */           w = (m[4] - m[1]) * s;
/*     */         }
/*     */       }
/*     */     }
/* 178 */     quaternion[(offset + 0)] = x;
/* 179 */     quaternion[(offset + 1)] = y;
/* 180 */     quaternion[(offset + 2)] = z;
/* 181 */     quaternion[(offset + 3)] = w;
/*     */   }
/*     */ 
/*     */   public void getEulerAngles(float[] eulerAngles, int offset)
/*     */   {
/* 210 */     if (offset + 3 > eulerAngles.length) {
/* 211 */       throw new IllegalArgumentException("Not enough space to write the result");
/*     */     }
/*     */ 
/* 215 */     float pitch = (float)Math.asin(this.headView[6]);
/*     */     float roll;
/*     */     float yaw;
/*     */     float roll;
/* 219 */     if (FloatMath.sqrt(1.0F - this.headView[6] * this.headView[6]) >= 0.01F)
/*     */     {
/* 222 */       float yaw = (float)Math.atan2(-this.headView[2], this.headView[10]);
/* 223 */       roll = (float)Math.atan2(-this.headView[4], this.headView[5]);
/*     */     }
/*     */     else
/*     */     {
/* 227 */       yaw = 0.0F;
/* 228 */       roll = (float)Math.atan2(this.headView[1], this.headView[0]);
/*     */     }
/*     */ 
/* 231 */     eulerAngles[(offset + 0)] = (-pitch);
/* 232 */     eulerAngles[(offset + 1)] = (-yaw);
/* 233 */     eulerAngles[(offset + 2)] = (-roll);
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.HeadTransform
 * JD-Core Version:    0.6.2
 */