/*     */ package com.google.vrtoolkit.cardboard.sensors.internal;
/*     */ 
/*     */ public class GyroBiasEstimator
/*     */ {
/*     */   private static final float GYRO_SMOOTHING_FACTOR = 0.01F;
/*     */   private static final float ACC_SMOOTHING_FACTOR = 0.1F;
/*  61 */   private static final Vector3d UP_VECTOR = new Vector3d(0.0D, 0.0D, 1.0D);
/*     */ 
/*  67 */   private static final float MIN_ACCEL_DOT_WITH_UP = (float)Math.cos(Math.toRadians(10.0D));
/*     */   private static final float MIN_ACCEL_LENGTH = 1.0E-04F;
/*     */   private static final float MAX_GYRO_DIFF = 0.01F;
/*     */   private static final long CALIBRATION_DURATION_NS = 5000000000L;
/*     */   private static final long MAX_DELAY_BETWEEN_EVENTS_NS = 100000000L;
/*  90 */   private final Vector3d mLastGyro = new Vector3d();
/*  91 */   private final Vector3d mCurrGyro = new Vector3d();
/*  92 */   private final Vector3d mGyroDiff = new Vector3d();
/*  93 */   private final Vector3d mCurrAcc = new Vector3d();
/*     */ 
/*  96 */   private final Vector3d mAccSmoothed = new Vector3d();
/*  97 */   private final Vector3d mAccNormalizedTmp = new Vector3d();
/*     */   private float mGyroMagnitudeDiffSmoothed;
/* 100 */   private final Estimate mBiasEstimate = new Estimate();
/*     */ 
/* 102 */   private long mCalibrationStartTimeNs = -1L;
/*     */ 
/* 104 */   private long mLastGyroTimeNs = -1L;
/*     */ 
/* 106 */   private long mLastAccTimeNs = -1L;
/*     */ 
/*     */   public void processGyroscope(Vector3d gyro, long sensorTimeStamp)
/*     */   {
/* 116 */     if (this.mBiasEstimate.mState == GyroBiasEstimator.Estimate.State.CALIBRATED) {
/* 117 */       return;
/*     */     }
/* 119 */     this.mCurrGyro.set(gyro);
/*     */ 
/* 121 */     Vector3d.sub(this.mCurrGyro, this.mLastGyro, this.mGyroDiff);
/* 122 */     float mCurrDiff = (float)this.mGyroDiff.length();
/* 123 */     this.mGyroMagnitudeDiffSmoothed = (0.01F * mCurrDiff + 0.99F * this.mGyroMagnitudeDiffSmoothed);
/*     */ 
/* 125 */     this.mLastGyro.set(this.mCurrGyro);
/* 126 */     boolean eventIsDelayed = sensorTimeStamp > this.mLastGyroTimeNs + 100000000L;
/* 127 */     this.mLastGyroTimeNs = sensorTimeStamp;
/*     */ 
/* 129 */     if (eventIsDelayed) {
/* 130 */       resetCalibration();
/* 131 */       return;
/*     */     }
/*     */ 
/* 135 */     if ((this.mBiasEstimate.mState == GyroBiasEstimator.Estimate.State.CALIBRATING) && (sensorTimeStamp > this.mCalibrationStartTimeNs + 5000000000L))
/*     */     {
/* 137 */       this.mBiasEstimate.mState = GyroBiasEstimator.Estimate.State.CALIBRATED;
/* 138 */       return;
/*     */     }
/*     */ 
/* 141 */     if (!canCalibrateGyro()) {
/* 142 */       resetCalibration();
/* 143 */       return;
/*     */     }
/*     */ 
/* 146 */     startCalibration(sensorTimeStamp);
/*     */   }
/*     */ 
/*     */   private void resetCalibration() {
/* 150 */     this.mBiasEstimate.mState = GyroBiasEstimator.Estimate.State.UNCALIBRATED;
/* 151 */     this.mBiasEstimate.mBias.set(0.0D, 0.0D, 0.0D);
/* 152 */     this.mCalibrationStartTimeNs = -1L;
/*     */   }
/*     */ 
/*     */   private void startCalibration(long gyroTimeStamp)
/*     */   {
/* 157 */     if (this.mBiasEstimate.mState != GyroBiasEstimator.Estimate.State.CALIBRATING) {
/* 158 */       this.mBiasEstimate.mBias.set(this.mCurrGyro);
/* 159 */       this.mBiasEstimate.mState = GyroBiasEstimator.Estimate.State.CALIBRATING;
/* 160 */       this.mCalibrationStartTimeNs = gyroTimeStamp;
/*     */     } else {
/* 162 */       smooth(this.mBiasEstimate.mBias, this.mCurrGyro, 0.01F);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void processAccelerometer(Vector3d acc, long sensorTimeStamp)
/*     */   {
/* 174 */     if (this.mBiasEstimate.mState == GyroBiasEstimator.Estimate.State.CALIBRATED) {
/* 175 */       return;
/*     */     }
/* 177 */     this.mCurrAcc.set(acc);
/* 178 */     boolean eventIsDelayed = sensorTimeStamp > this.mLastAccTimeNs + 100000000L;
/* 179 */     this.mLastAccTimeNs = sensorTimeStamp;
/* 180 */     if (eventIsDelayed) {
/* 181 */       resetCalibration();
/* 182 */       return;
/*     */     }
/* 184 */     smooth(this.mAccSmoothed, this.mCurrAcc, 0.1F);
/*     */   }
/*     */ 
/*     */   public void getEstimate(Estimate output)
/*     */   {
/* 191 */     output.set(this.mBiasEstimate);
/*     */   }
/*     */ 
/*     */   private boolean canCalibrateGyro()
/*     */   {
/* 199 */     if (this.mAccSmoothed.length() < 9.999999747378752E-05D) {
/* 200 */       return false;
/*     */     }
/* 202 */     this.mAccNormalizedTmp.set(this.mAccSmoothed);
/* 203 */     this.mAccNormalizedTmp.normalize();
/*     */ 
/* 205 */     if (Vector3d.dot(this.mAccNormalizedTmp, UP_VECTOR) < MIN_ACCEL_DOT_WITH_UP) {
/* 206 */       return false;
/*     */     }
/*     */ 
/* 209 */     if (this.mGyroMagnitudeDiffSmoothed > 0.01F) {
/* 210 */       return false;
/*     */     }
/* 212 */     return true;
/*     */   }
/*     */ 
/*     */   private static void smooth(Vector3d smoothed, Vector3d newValue, float smoothingFactor)
/*     */   {
/* 220 */     smoothed.x = (smoothingFactor * newValue.x + (1.0F - smoothingFactor) * smoothed.x);
/* 221 */     smoothed.y = (smoothingFactor * newValue.y + (1.0F - smoothingFactor) * smoothed.y);
/* 222 */     smoothed.z = (smoothingFactor * newValue.z + (1.0F - smoothingFactor) * smoothed.z);
/*     */   }
/*     */ 
/*     */   public static class Estimate
/*     */   {
/*  36 */     public State mState = State.UNCALIBRATED;
/*     */ 
/*  42 */     public final Vector3d mBias = new Vector3d();
/*     */ 
/*     */     public void set(Estimate from) {
/*  45 */       this.mState = from.mState;
/*  46 */       this.mBias.set(from.mBias);
/*     */     }
/*     */ 
/*     */     public static enum State
/*     */     {
/*  27 */       UNCALIBRATED, 
/*     */ 
/*  30 */       CALIBRATING, 
/*     */ 
/*  33 */       CALIBRATED;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.sensors.internal.GyroBiasEstimator
 * JD-Core Version:    0.6.2
 */