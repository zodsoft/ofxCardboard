/*     */ package com.google.vrtoolkit.cardboard;
/*     */ 
/*     */ import android.opengl.GLES20;
/*     */ 
/*     */ public class Viewport
/*     */ {
/*     */   public int x;
/*     */   public int y;
/*     */   public int width;
/*     */   public int height;
/*     */ 
/*     */   public void setViewport(int x, int y, int width, int height)
/*     */   {
/*  39 */     this.x = x;
/*  40 */     this.y = y;
/*  41 */     this.width = width;
/*  42 */     this.height = height;
/*     */   }
/*     */ 
/*     */   public void setGLViewport()
/*     */   {
/*  47 */     GLES20.glViewport(this.x, this.y, this.width, this.height);
/*     */   }
/*     */ 
/*     */   public void setGLScissor()
/*     */   {
/*  52 */     GLES20.glScissor(this.x, this.y, this.width, this.height);
/*     */   }
/*     */ 
/*     */   public void getAsArray(int[] array, int offset)
/*     */   {
/*  64 */     if (offset + 4 > array.length) {
/*  65 */       throw new IllegalArgumentException("Not enough space to write the result");
/*     */     }
/*     */ 
/*  68 */     array[offset] = this.x;
/*  69 */     array[(offset + 1)] = this.y;
/*  70 */     array[(offset + 2)] = this.width;
/*  71 */     array[(offset + 3)] = this.height;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  81 */     int i = this.x; int j = this.y; int k = this.width; int m = this.height; return "{\n" + new StringBuilder(18).append("  x: ").append(i).append(",\n").toString() + new StringBuilder(18).append("  y: ").append(j).append(",\n").toString() + new StringBuilder(22).append("  width: ").append(k).append(",\n").toString() + new StringBuilder(23).append("  height: ").append(m).append(",\n").toString() + "}";
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/*  93 */     if (obj == this) {
/*  94 */       return true;
/*     */     }
/*  96 */     if (!(obj instanceof Viewport)) {
/*  97 */       return false;
/*     */     }
/*  99 */     Viewport other = (Viewport)obj;
/* 100 */     return (this.x == other.x) && (this.y == other.y) && (this.width == other.width) && (this.height == other.height);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 105 */     return Integer.valueOf(this.x).hashCode() ^ Integer.valueOf(this.y).hashCode() ^ Integer.valueOf(this.width).hashCode() ^ Integer.valueOf(this.height).hashCode();
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.Viewport
 * JD-Core Version:    0.6.2
 */