/*     */ package com.google.vrtoolkit.cardboard;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public class Distortion
/*     */ {
/*  27 */   private static final float[] DEFAULT_COEFFICIENTS = { 0.441F, 0.156F };
/*     */   private float[] mCoefficients;
/*     */ 
/*     */   public Distortion()
/*     */   {
/*  44 */     this.mCoefficients = ((float[])DEFAULT_COEFFICIENTS.clone());
/*     */   }
/*     */ 
/*     */   public Distortion(Distortion other)
/*     */   {
/*  53 */     setCoefficients(other.mCoefficients);
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
/*  76 */     return (float[])this.mCoefficients.clone();
/*     */   }
/*     */ 
/*     */   public void setCoefficients(float[] coefficients)
/*     */   {
/*  91 */     this.mCoefficients = (coefficients != null ? (float[])coefficients.clone() : new float[0]);
/*     */   }
/*     */ 
/*     */   public float[] getCoefficients()
/*     */   {
/* 100 */     return this.mCoefficients;
/*     */   }
/*     */ 
/*     */   public float distortionFactor(float radius)
/*     */   {
/* 110 */     float result = 1.0F;
/* 111 */     float rFactor = 1.0F;
/* 112 */     float rSquared = radius * radius;
/*     */ 
/* 114 */     for (float ki : this.mCoefficients) {
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
/*     */ 
/* 147 */     float dr0 = radius - distort(r0);
/*     */ 
/* 149 */     while (Math.abs(r1 - r0) > 0.0001D) {
/* 150 */       float dr1 = radius - distort(r1);
/* 151 */       float r2 = r1 - dr1 * ((r1 - r0) / (dr1 - dr0));
/* 152 */       r0 = r1;
/* 153 */       r1 = r2;
/* 154 */       dr0 = dr1;
/*     */     }
/* 156 */     return r1;
/*     */   }
/*     */ 
/*     */   private static double[] solveLeastSquares(double[][] matA, double[] vecY)
/*     */   {
/* 174 */     int numSamples = matA.length;
/* 175 */     int numCoefficients = matA[0].length;
/*     */ 
/* 178 */     double[][] matATA = new double[numCoefficients][numCoefficients];
/*     */     int j;
/* 179 */     for (int k = 0; k < numCoefficients; k++) {
/* 180 */       for (j = 0; j < numCoefficients; j++) {
/* 181 */         double sum = 0.0D;
/* 182 */         for (int i = 0; i < numSamples; i++) {
/* 183 */           sum += matA[i][j] * matA[i][k];
/*     */         }
/* 185 */         matATA[j][k] = sum;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 192 */     double[][] matInvATA = new double[numCoefficients][numCoefficients];
/*     */ 
/* 196 */     if (numCoefficients != 2) {
/* 197 */       j = numCoefficients; throw new RuntimeException(new StringBuilder(78).append("solveLeastSquares: only 2 coefficients currently supported, ").append(j).append(" given.").toString());
/*     */     }
/*     */ 
/* 201 */     double det = matATA[0][0] * matATA[1][1] - matATA[0][1] * matATA[1][0];
/* 202 */     matInvATA[0][0] = (matATA[1][1] / det);
/* 203 */     matInvATA[1][1] = (matATA[0][0] / det);
/* 204 */     matInvATA[0][1] = (-matATA[1][0] / det);
/* 205 */     matInvATA[1][0] = (-matATA[0][1] / det);
/*     */ 
/* 208 */     double[] vecATY = new double[numCoefficients];
/* 209 */     for (int j = 0; j < numCoefficients; j++) {
/* 210 */       double sum = 0.0D;
/* 211 */       for (int i = 0; i < numSamples; i++) {
/* 212 */         sum += matA[i][j] * vecY[i];
/*     */       }
/* 214 */       vecATY[j] = sum;
/*     */     }
/*     */ 
/* 218 */     double[] vecX = new double[numCoefficients];
/* 219 */     for (int j = 0; j < numCoefficients; j++) {
/* 220 */       double sum = 0.0D;
/* 221 */       for (int i = 0; i < numCoefficients; i++) {
/* 222 */         sum += matInvATA[i][j] * vecATY[i];
/*     */       }
/* 224 */       vecX[j] = sum;
/*     */     }
/*     */ 
/* 227 */     return vecX;
/*     */   }
/*     */ 
/*     */   public Distortion getApproximateInverseDistortion(float maxRadius)
/*     */   {
/* 248 */     int numSamples = 10;
/* 249 */     int numCoefficients = 2;
/*     */ 
/* 251 */     double[][] matA = new double[10][2];
/* 252 */     double[] vecY = new double[10];
/*     */ 
/* 264 */     for (int i = 0; i < 10; i++) {
/* 265 */       float r = maxRadius * (i + 1) / 10.0F;
/* 266 */       double rp = distort(r);
/* 267 */       double v = rp;
/* 268 */       for (int j = 0; j < 2; j++) {
/* 269 */         v *= rp * rp;
/* 270 */         matA[i][j] = v;
/*     */       }
/* 272 */       vecY[i] = (r - rp);
/*     */     }
/*     */ 
/* 275 */     double[] vecK = solveLeastSquares(matA, vecY);
/*     */ 
/* 278 */     float[] coefficients = new float[vecK.length];
/* 279 */     for (int i = 0; i < vecK.length; i++) {
/* 280 */       coefficients[i] = ((float)vecK[i]);
/*     */     }
/* 282 */     Distortion inverse = new Distortion();
/* 283 */     inverse.setCoefficients(coefficients);
/* 284 */     return inverse;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 295 */     if (other == null) {
/* 296 */       return false;
/*     */     }
/*     */ 
/* 299 */     if (other == this) {
/* 300 */       return true;
/*     */     }
/*     */ 
/* 303 */     if (!(other instanceof Distortion)) {
/* 304 */       return false;
/*     */     }
/*     */ 
/* 307 */     Distortion o = (Distortion)other;
/* 308 */     return Arrays.equals(this.mCoefficients, o.mCoefficients);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 318 */     StringBuilder builder = new StringBuilder().append("{\n").append("  coefficients: [");
/*     */ 
/* 322 */     for (int i = 0; i < this.mCoefficients.length; i++) {
/* 323 */       builder.append(Float.toString(this.mCoefficients[i]));
/* 324 */       if (i < this.mCoefficients.length - 1) {
/* 325 */         builder.append(", ");
/*     */       }
/*     */     }
/*     */ 
/* 329 */     builder.append("],\n}");
/* 330 */     return builder.toString();
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.Distortion
 * JD-Core Version:    0.6.2
 */