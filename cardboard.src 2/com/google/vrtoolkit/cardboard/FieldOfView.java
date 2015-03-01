/*     */ package com.google.vrtoolkit.cardboard;
/*     */ 
/*     */ import android.opengl.Matrix;
/*     */ 
/*     */ public class FieldOfView
/*     */ {
/*     */   private static final float DEFAULT_MAX_FOV_LEFT_RIGHT = 40.0F;
/*     */   private static final float DEFAULT_MAX_FOV_BOTTOM = 40.0F;
/*     */   private static final float DEFAULT_MAX_FOV_TOP = 40.0F;
/*     */   private float left;
/*     */   private float right;
/*     */   private float bottom;
/*     */   private float top;
/*     */ 
/*     */   public FieldOfView()
/*     */   {
/*  40 */     this.left = 40.0F;
/*  41 */     this.right = 40.0F;
/*  42 */     this.bottom = 40.0F;
/*  43 */     this.top = 40.0F;
/*     */   }
/*     */ 
/*     */   public FieldOfView(float left, float right, float bottom, float top)
/*     */   {
/*  55 */     this.left = left;
/*  56 */     this.right = right;
/*  57 */     this.bottom = bottom;
/*  58 */     this.top = top;
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
/*  92 */     return new float[] { this.left, this.right, this.bottom, this.top };
/*     */   }
/*     */ 
/*     */   public void copy(FieldOfView other)
/*     */   {
/* 101 */     this.left = other.left;
/* 102 */     this.right = other.right;
/* 103 */     this.bottom = other.bottom;
/* 104 */     this.top = other.top;
/*     */   }
/*     */ 
/*     */   public void setLeft(float left)
/*     */   {
/* 113 */     this.left = left;
/*     */   }
/*     */ 
/*     */   public float getLeft()
/*     */   {
/* 122 */     return this.left;
/*     */   }
/*     */ 
/*     */   public void setRight(float right)
/*     */   {
/* 131 */     this.right = right;
/*     */   }
/*     */ 
/*     */   public float getRight()
/*     */   {
/* 140 */     return this.right;
/*     */   }
/*     */ 
/*     */   public void setBottom(float bottom)
/*     */   {
/* 149 */     this.bottom = bottom;
/*     */   }
/*     */ 
/*     */   public float getBottom()
/*     */   {
/* 158 */     return this.bottom;
/*     */   }
/*     */ 
/*     */   public void setTop(float top)
/*     */   {
/* 167 */     this.top = top;
/*     */   }
/*     */ 
/*     */   public float getTop()
/*     */   {
/* 176 */     return this.top;
/*     */   }
/*     */ 
/*     */   public void toPerspectiveMatrix(float near, float far, float[] perspective, int offset)
/*     */   {
/* 190 */     if (offset + 16 > perspective.length) {
/* 191 */       throw new IllegalArgumentException("Not enough space to write the result");
/*     */     }
/*     */ 
/* 194 */     float l = (float)-Math.tan(Math.toRadians(this.left)) * near;
/* 195 */     float r = (float)Math.tan(Math.toRadians(this.right)) * near;
/* 196 */     float b = (float)-Math.tan(Math.toRadians(this.bottom)) * near;
/* 197 */     float t = (float)Math.tan(Math.toRadians(this.top)) * near;
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
/* 222 */     return (this.left == o.left) && (this.right == o.right) && (this.bottom == o.bottom) && (this.top == o.top);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 232 */     float f1 = this.left; float f2 = this.right; float f3 = this.bottom; float f4 = this.top; return "{\n" + new StringBuilder(25).append("  left: ").append(f1).append(",\n").toString() + new StringBuilder(26).append("  right: ").append(f2).append(",\n").toString() + new StringBuilder(27).append("  bottom: ").append(f3).append(",\n").toString() + new StringBuilder(24).append("  top: ").append(f4).append(",\n").toString() + "}";
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.FieldOfView
 * JD-Core Version:    0.6.2
 */