/*     */ package com.google.vrtoolkit.cardboard;
/*     */ 
/*     */ import android.opengl.Matrix;
/*     */ 
/*     */ public class FieldOfView
/*     */ {
/*     */   private static final float DEFAULT_MAX_FOV_LEFT_RIGHT = 40.0F;
/*     */   private static final float DEFAULT_MAX_FOV_BOTTOM = 40.0F;
/*     */   private static final float DEFAULT_MAX_FOV_TOP = 40.0F;
/*     */   private float mLeft;
/*     */   private float mRight;
/*     */   private float mBottom;
/*     */   private float mTop;
/*     */ 
/*     */   public FieldOfView()
/*     */   {
/*  40 */     this.mLeft = 40.0F;
/*  41 */     this.mRight = 40.0F;
/*  42 */     this.mBottom = 40.0F;
/*  43 */     this.mTop = 40.0F;
/*     */   }
/*     */ 
/*     */   public FieldOfView(float left, float right, float bottom, float top)
/*     */   {
/*  55 */     this.mLeft = left;
/*  56 */     this.mRight = right;
/*  57 */     this.mBottom = bottom;
/*  58 */     this.mTop = top;
/*     */   }
/*     */ 
/*     */   public FieldOfView(FieldOfView other)
/*     */   {
/*  67 */     copy(other);
/*     */   }
/*     */ 
/*     */   public static FieldOfView parseFromProtobuf(float[] angles)
/*     */   {
/*  78 */     if (angles.length != 4) {
/*  79 */       return null;
/*     */     }
/*     */ 
/*  82 */     return new FieldOfView(angles[0], angles[1], angles[2], angles[3]);
/*     */   }
/*     */ 
/*     */   public float[] toProtobuf()
/*     */   {
/*  92 */     return new float[] { this.mLeft, this.mRight, this.mBottom, this.mTop };
/*     */   }
/*     */ 
/*     */   public void copy(FieldOfView other)
/*     */   {
/* 101 */     this.mLeft = other.mLeft;
/* 102 */     this.mRight = other.mRight;
/* 103 */     this.mBottom = other.mBottom;
/* 104 */     this.mTop = other.mTop;
/*     */   }
/*     */ 
/*     */   public void setLeft(float left)
/*     */   {
/* 113 */     this.mLeft = left;
/*     */   }
/*     */ 
/*     */   public float getLeft()
/*     */   {
/* 122 */     return this.mLeft;
/*     */   }
/*     */ 
/*     */   public void setRight(float right)
/*     */   {
/* 131 */     this.mRight = right;
/*     */   }
/*     */ 
/*     */   public float getRight()
/*     */   {
/* 140 */     return this.mRight;
/*     */   }
/*     */ 
/*     */   public void setBottom(float bottom)
/*     */   {
/* 149 */     this.mBottom = bottom;
/*     */   }
/*     */ 
/*     */   public float getBottom()
/*     */   {
/* 158 */     return this.mBottom;
/*     */   }
/*     */ 
/*     */   public void setTop(float top)
/*     */   {
/* 167 */     this.mTop = top;
/*     */   }
/*     */ 
/*     */   public float getTop()
/*     */   {
/* 176 */     return this.mTop;
/*     */   }
/*     */ 
/*     */   public void toPerspectiveMatrix(float near, float far, float[] perspective, int offset)
/*     */   {
/* 190 */     if (offset + 16 > perspective.length) {
/* 191 */       throw new IllegalArgumentException("Not enough space to write the result");
/*     */     }
/*     */ 
/* 194 */     float l = (float)-Math.tan(Math.toRadians(this.mLeft)) * near;
/* 195 */     float r = (float)Math.tan(Math.toRadians(this.mRight)) * near;
/* 196 */     float b = (float)-Math.tan(Math.toRadians(this.mBottom)) * near;
/* 197 */     float t = (float)Math.tan(Math.toRadians(this.mTop)) * near;
/* 198 */     Matrix.frustumM(perspective, offset, l, r, b, t, near, far);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 209 */     if (other == null) {
/* 210 */       return false;
/*     */     }
/*     */ 
/* 213 */     if (other == this) {
/* 214 */       return true;
/*     */     }
/*     */ 
/* 217 */     if (!(other instanceof FieldOfView)) {
/* 218 */       return false;
/*     */     }
/*     */ 
/* 221 */     FieldOfView o = (FieldOfView)other;
/* 222 */     return (this.mLeft == o.mLeft) && (this.mRight == o.mRight) && (this.mBottom == o.mBottom) && (this.mTop == o.mTop);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 232 */     float f1 = this.mLeft; float f2 = this.mRight; float f3 = this.mBottom; float f4 = this.mTop; return "{\n" + new StringBuilder(25).append("  left: ").append(f1).append(",\n").toString() + new StringBuilder(26).append("  right: ").append(f2).append(",\n").toString() + new StringBuilder(27).append("  bottom: ").append(f3).append(",\n").toString() + new StringBuilder(24).append("  top: ").append(f4).append(",\n").toString() + "}";
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.FieldOfView
 * JD-Core Version:    0.6.2
 */