/*     */ package com.google.vrtoolkit.cardboard.sensors.internal;
/*     */ 
/*     */ public class Vector3d
/*     */ {
/*     */   public double x;
/*     */   public double y;
/*     */   public double z;
/*     */ 
/*     */   public Vector3d()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Vector3d(double xx, double yy, double zz)
/*     */   {
/*  29 */     set(xx, yy, zz);
/*     */   }
/*     */ 
/*     */   public void set(double xx, double yy, double zz)
/*     */   {
/*  40 */     this.x = xx;
/*  41 */     this.y = yy;
/*  42 */     this.z = zz;
/*     */   }
/*     */ 
/*     */   public void setComponent(int i, double val)
/*     */   {
/*  53 */     if (i == 0)
/*  54 */       this.x = val;
/*  55 */     else if (i == 1)
/*  56 */       this.y = val;
/*     */     else
/*  58 */       this.z = val;
/*     */   }
/*     */ 
/*     */   public void setZero()
/*     */   {
/*  66 */     this.x = (this.y = this.z = 0.0D);
/*     */   }
/*     */ 
/*     */   public void set(Vector3d other)
/*     */   {
/*  75 */     this.x = other.x;
/*  76 */     this.y = other.y;
/*  77 */     this.z = other.z;
/*     */   }
/*     */ 
/*     */   public void scale(double s)
/*     */   {
/*  86 */     this.x *= s;
/*  87 */     this.y *= s;
/*  88 */     this.z *= s;
/*     */   }
/*     */ 
/*     */   public void normalize()
/*     */   {
/*  95 */     double d = length();
/*  96 */     if (d != 0.0D)
/*  97 */       scale(1.0D / d);
/*     */   }
/*     */ 
/*     */   public static double dot(Vector3d a, Vector3d b)
/*     */   {
/* 109 */     return a.x * b.x + a.y * b.y + a.z * b.z;
/*     */   }
/*     */ 
/*     */   public double length()
/*     */   {
/* 118 */     return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
/*     */   }
/*     */ 
/*     */   public boolean sameValues(Vector3d other)
/*     */   {
/* 127 */     return (this.x == other.x) && (this.y == other.y) && (this.z == other.z);
/*     */   }
/*     */ 
/*     */   public static void add(Vector3d a, Vector3d b, Vector3d result)
/*     */   {
/* 138 */     result.set(a.x + b.x, a.y + b.y, a.z + b.z);
/*     */   }
/*     */ 
/*     */   public static void sub(Vector3d a, Vector3d b, Vector3d result)
/*     */   {
/* 149 */     result.set(a.x - b.x, a.y - b.y, a.z - b.z);
/*     */   }
/*     */ 
/*     */   public static void cross(Vector3d a, Vector3d b, Vector3d result)
/*     */   {
/* 160 */     result.set(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x);
/*     */   }
/*     */ 
/*     */   public static void ortho(Vector3d v, Vector3d result)
/*     */   {
/* 170 */     int k = largestAbsComponent(v) - 1;
/* 171 */     if (k < 0) {
/* 172 */       k = 2;
/*     */     }
/* 174 */     result.setZero();
/* 175 */     result.setComponent(k, 1.0D);
/*     */ 
/* 177 */     cross(v, result, result);
/* 178 */     result.normalize();
/*     */   }
/*     */ 
/*     */   public static int largestAbsComponent(Vector3d v)
/*     */   {
/* 187 */     double xAbs = Math.abs(v.x);
/* 188 */     double yAbs = Math.abs(v.y);
/* 189 */     double zAbs = Math.abs(v.z);
/*     */ 
/* 191 */     if (xAbs > yAbs) {
/* 192 */       if (xAbs > zAbs) {
/* 193 */         return 0;
/*     */       }
/* 195 */       return 2;
/*     */     }
/*     */ 
/* 198 */     if (yAbs > zAbs) {
/* 199 */       return 1;
/*     */     }
/* 201 */     return 2;
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.sensors.internal.Vector3d
 * JD-Core Version:    0.6.2
 */