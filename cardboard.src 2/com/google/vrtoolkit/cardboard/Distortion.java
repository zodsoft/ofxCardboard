/*     */ package com.google.vrtoolkit.cardboard;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public class Distortion
/*     */ {
/*  27 */   private static final float[] DEFAULT_COEFFICIENTS = { 0.441F, 0.156F };
/*     */   private float[] coefficients;
/*     */ 
/*     */   public Distortion()
/*     */   {
/*  44 */     this.coefficients = ((float[])DEFAULT_COEFFICIENTS.clone());
/*     */   }
/*     */ 
/*     */   public Distortion(Distortion other)
/*     */   {
/*  53 */     setCoefficients(other.coefficients);
/*     */   }
/*     */ 
/*     */   public static Distortion parseFromProtobuf(float[] coefficients)
/*     */   {
/*  64 */     Distortion distortion = new Distortion();
/*  65 */     distortion.setCoefficients(coefficients);
/*  66 */     return distortion;
/*     */   }
/*     */ 
/*     */   public float[] toProtobuf()
/*     */   {
/*  76 */     return (float[])this.coefficients.clone();
/*     */   }
/*     */ 
/*     */   public void setCoefficients(float[] coefficients)
/*     */   {
/*  91 */     this.coefficients = (coefficients != null ? (float[])coefficients.clone() : new float[0]);
/*     */   }
/*     */ 
/*     */   public float[] getCoefficients()
/*     */   {
/* 100 */     return this.coefficients;
/*     */   }
/*     */ 
/*     */   public float distortionFactor(float radius)
/*     */   {
/* 110 */     float result = 1.0F;
/* 111 */     float rFactor = 1.0F;
/* 112 */     float rSquared = radius * radius;
/*     */ 
/* 114 */     for (float ki : this.coefficients) {
/* 115 */       rFactor *= rSquared;
/* 116 */       result += ki * rFactor;
/*     */     }
/*     */ 
/* 119 */     return result;
/*     */   }
/*     */ 
/*     */   public float distort(float radius)
/*     */   {
/* 129 */     return radius * distortionFactor(radius);
/*     */   }
/*     */ 
/*     */   public float distortInverse(float radius)
/*     */   {
/* 144 */     float r0 = radius / 0.9F;
/* 145 */     float r1 = radius * 0.9F;
/* 146 */     float dr0 = radius - distort(r0);
/* 147 */     while (Math.abs(r1 - r0) > 0.0001D) {
/* 148 */       float dr1 = radius - distort(r1);
/* 149 */       float r2 = r1 - dr1 * ((r1 - r0) / (dr1 - dr0));
/* 150 */       r0 = r1;
/* 151 */       r1 = r2;
/* 152 */       dr0 = dr1;
/*     */     }
/* 154 */     return r1;
/*     */   }
/*     */ 
/*     */   private static double[] solveLeastSquares(double[][] matA, double[] vecY)
/*     */   {
/* 172 */     int numSamples = matA.length;
/* 173 */     int numCoefficients = matA[0].length;
/*     */ 
/* 176 */     double[][] matATA = new double[numCoefficients][numCoefficients];
/*     */     int j;
/* 177 */     for (int k = 0; k < numCoefficients; k++) {
/* 178 */       for (j = 0; j < numCoefficients; j++) {
/* 179 */         double sum = 0.0D;
/* 180 */         for (int i = 0; i < numSamples; i++) {
/* 181 */           sum += matA[i][j] * matA[i][k];
/*     */         }
/* 183 */         matATA[j][k] = sum;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 190 */     double[][] matInvATA = new double[numCoefficients][numCoefficients];
/*     */ 
/* 194 */     if (numCoefficients != 2) {
/* 195 */       j = numCoefficients; throw new RuntimeException(new StringBuilder(78).append("solveLeastSquares: only 2 coefficients currently supported, ").append(j).append(" given.").toString());
/*     */     }
/*     */ 
/* 199 */     double det = matATA[0][0] * matATA[1][1] - matATA[0][1] * matATA[1][0];
/* 200 */     matInvATA[0][0] = (matATA[1][1] / det);
/* 201 */     matInvATA[1][1] = (matATA[0][0] / det);
/* 202 */     matInvATA[0][1] = (-matATA[1][0] / det);
/* 203 */     matInvATA[1][0] = (-matATA[0][1] / det);
/*     */ 
/* 206 */     double[] vecATY = new double[numCoefficients];
/* 207 */     for (int j = 0; j < numCoefficients; j++) {
/* 208 */       double sum = 0.0D;
/* 209 */       for (int i = 0; i < numSamples; i++) {
/* 210 */         sum += matA[i][j] * vecY[i];
/*     */       }
/* 212 */       vecATY[j] = sum;
/*     */     }
/*     */ 
/* 216 */     double[] vecX = new double[numCoefficients];
/* 217 */     for (int j = 0; j < numCoefficients; j++) {
/* 218 */       double sum = 0.0D;
/* 219 */       for (int i = 0; i < numCoefficients; i++) {
/* 220 */         sum += matInvATA[i][j] * vecATY[i];
/*     */       }
/* 222 */       vecX[j] = sum;
/*     */     }
/*     */ 
/* 225 */     return vecX;
/*     */   }
/*     */ 
/*     */   public Distortion getApproximateInverseDistortion(float maxRadius)
/*     */   {
/* 246 */     int numSamples = 10;
/* 247 */     int numCoefficients = 2;
/*     */ 
/* 249 */     double[][] matA = new double[10][2];
/* 250 */     double[] vecY = new double[10];
/*     */ 
/* 262 */     for (int i = 0; i < 10; i++) {
/* 263 */       float r = maxRadius * (i + 1) / 10.0F;
/* 264 */       double rp = distort(r);
/* 265 */       double v = rp;
/* 266 */       for (int j = 0; j < 2; j++) {
/* 267 */         v *= rp * rp;
/* 268 */         matA[i][j] = v;
/*     */       }
/* 270 */       vecY[i] = (r - rp);
/*     */     }
/*     */ 
/* 273 */     double[] vecK = solveLeastSquares(matA, vecY);
/*     */ 
/* 276 */     float[] coefficients = new float[vecK.length];
/* 277 */     for (int i = 0; i < vecK.length; i++) {
/* 278 */       coefficients[i] = ((float)vecK[i]);
/*     */     }
/* 280 */     Distortion inverse = new Distortion();
/* 281 */     inverse.setCoefficients(coefficients);
/* 282 */     return inverse;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 293 */     if (other == null) {
/* 294 */       return false;
/*     */     }
/*     */ 
/* 297 */     if (other == this) {
/* 298 */       return true;
/*     */     }
/*     */ 
/* 301 */     if (!(other instanceof Distortion)) {
/* 302 */       return false;
/*     */     }
/*     */ 
/* 305 */     Distortion o = (Distortion)other;
/* 306 */     return Arrays.equals(this.coefficients, o.coefficients);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 316 */     StringBuilder builder = new StringBuilder().append("{\n").append("  coefficients: [");
/*     */ 
/* 320 */     for (int i = 0; i < this.coefficients.length; i++) {
/* 321 */       builder.append(Float.toString(this.coefficients[i]));
/* 322 */       if (i < this.coefficients.length - 1) {
/* 323 */         builder.append(", ");
/*     */       }
/*     */     }
/*     */ 
/* 327 */     builder.append("],\n}");
/* 328 */     return builder.toString();
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.Distortion
 * JD-Core Version:    0.6.2
 */