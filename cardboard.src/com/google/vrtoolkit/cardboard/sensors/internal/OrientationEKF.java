/*     */ package com.google.vrtoolkit.cardboard.sensors.internal;
/*     */ 
/*     */ public class OrientationEKF
/*     */ {
/*     */   private static final float NS2S = 1.0E-09F;
/*     */   private static final double MIN_ACCEL_NOISE_SIGMA = 0.75D;
/*     */   private static final double MAX_ACCEL_NOISE_SIGMA = 7.0D;
/*  24 */   private double[] rotationMatrix = new double[16];
/*     */ 
/*  26 */   private Matrix3x3d so3SensorFromWorld = new Matrix3x3d();
/*  27 */   private Matrix3x3d so3LastMotion = new Matrix3x3d();
/*  28 */   private Matrix3x3d mP = new Matrix3x3d();
/*  29 */   private Matrix3x3d mQ = new Matrix3x3d();
/*  30 */   private Matrix3x3d mR = new Matrix3x3d();
/*  31 */   private Matrix3x3d mRaccel = new Matrix3x3d();
/*  32 */   private Matrix3x3d mS = new Matrix3x3d();
/*  33 */   private Matrix3x3d mH = new Matrix3x3d();
/*  34 */   private Matrix3x3d mK = new Matrix3x3d();
/*  35 */   private Vector3d mNu = new Vector3d();
/*  36 */   private Vector3d mz = new Vector3d();
/*  37 */   private Vector3d mh = new Vector3d();
/*  38 */   private Vector3d mu = new Vector3d();
/*  39 */   private Vector3d mx = new Vector3d();
/*  40 */   private Vector3d down = new Vector3d();
/*  41 */   private Vector3d north = new Vector3d();
/*     */   private long sensorTimeStampGyro;
/*  46 */   private final Vector3d lastGyro = new Vector3d();
/*     */ 
/*  49 */   private double previousAccelNorm = 0.0D;
/*     */ 
/*  52 */   private double movingAverageAccelNormChange = 0.0D;
/*     */   private float filteredGyroTimestep;
/*  56 */   private boolean timestepFilterInit = false;
/*     */   private int numGyroTimestepSamples;
/*  58 */   private boolean gyroFilterValid = true;
/*     */ 
/*  63 */   private Matrix3x3d getPredictedGLMatrixTempM1 = new Matrix3x3d();
/*  64 */   private Matrix3x3d getPredictedGLMatrixTempM2 = new Matrix3x3d();
/*  65 */   private Vector3d getPredictedGLMatrixTempV1 = new Vector3d();
/*     */ 
/*  68 */   private Matrix3x3d setHeadingDegreesTempM1 = new Matrix3x3d();
/*     */ 
/*  71 */   private Matrix3x3d processGyroTempM1 = new Matrix3x3d();
/*  72 */   private Matrix3x3d processGyroTempM2 = new Matrix3x3d();
/*     */ 
/*  75 */   private Matrix3x3d processAccTempM1 = new Matrix3x3d();
/*  76 */   private Matrix3x3d processAccTempM2 = new Matrix3x3d();
/*  77 */   private Matrix3x3d processAccTempM3 = new Matrix3x3d();
/*  78 */   private Matrix3x3d processAccTempM4 = new Matrix3x3d();
/*  79 */   private Matrix3x3d processAccTempM5 = new Matrix3x3d();
/*  80 */   private Vector3d processAccTempV1 = new Vector3d();
/*  81 */   private Vector3d processAccTempV2 = new Vector3d();
/*  82 */   private Vector3d processAccVDelta = new Vector3d();
/*     */ 
/*  85 */   private Vector3d processMagTempV1 = new Vector3d();
/*  86 */   private Vector3d processMagTempV2 = new Vector3d();
/*  87 */   private Vector3d processMagTempV3 = new Vector3d();
/*  88 */   private Vector3d processMagTempV4 = new Vector3d();
/*  89 */   private Vector3d processMagTempV5 = new Vector3d();
/*  90 */   private Matrix3x3d processMagTempM1 = new Matrix3x3d();
/*  91 */   private Matrix3x3d processMagTempM2 = new Matrix3x3d();
/*  92 */   private Matrix3x3d processMagTempM4 = new Matrix3x3d();
/*  93 */   private Matrix3x3d processMagTempM5 = new Matrix3x3d();
/*  94 */   private Matrix3x3d processMagTempM6 = new Matrix3x3d();
/*     */ 
/*  97 */   private Matrix3x3d updateCovariancesAfterMotionTempM1 = new Matrix3x3d();
/*  98 */   private Matrix3x3d updateCovariancesAfterMotionTempM2 = new Matrix3x3d();
/*     */ 
/* 101 */   private Matrix3x3d accObservationFunctionForNumericalJacobianTempM = new Matrix3x3d();
/*     */ 
/* 105 */   private Matrix3x3d magObservationFunctionForNumericalJacobianTempM = new Matrix3x3d();
/*     */   private boolean alignedToGravity;
/*     */   private boolean alignedToNorth;
/*     */ 
/*     */   public OrientationEKF()
/*     */   {
/* 113 */     reset();
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 120 */     this.sensorTimeStampGyro = 0L;
/*     */ 
/* 122 */     this.so3SensorFromWorld.setIdentity();
/* 123 */     this.so3LastMotion.setIdentity();
/*     */ 
/* 126 */     double initialSigmaP = 5.0D;
/*     */ 
/* 128 */     this.mP.setZero();
/* 129 */     this.mP.setSameDiagonal(25.0D);
/*     */ 
/* 132 */     double initialSigmaQ = 1.0D;
/* 133 */     this.mQ.setZero();
/* 134 */     this.mQ.setSameDiagonal(1.0D);
/*     */ 
/* 137 */     double initialSigmaR = 0.25D;
/* 138 */     this.mR.setZero();
/* 139 */     this.mR.setSameDiagonal(0.0625D);
/*     */ 
/* 142 */     this.mRaccel.setZero();
/* 143 */     this.mRaccel.setSameDiagonal(0.5625D);
/*     */ 
/* 145 */     this.mS.setZero();
/* 146 */     this.mH.setZero();
/* 147 */     this.mK.setZero();
/* 148 */     this.mNu.setZero();
/* 149 */     this.mz.setZero();
/* 150 */     this.mh.setZero();
/* 151 */     this.mu.setZero();
/* 152 */     this.mx.setZero();
/*     */ 
/* 154 */     this.down.set(0.0D, 0.0D, 9.810000000000001D);
/* 155 */     this.north.set(0.0D, 1.0D, 0.0D);
/*     */ 
/* 157 */     this.alignedToGravity = false;
/* 158 */     this.alignedToNorth = false;
/*     */   }
/*     */ 
/*     */   public boolean isReady()
/*     */   {
/* 166 */     return this.alignedToGravity;
/*     */   }
/*     */ 
/*     */   public double getHeadingDegrees()
/*     */   {
/* 178 */     double x = this.so3SensorFromWorld.get(2, 0);
/* 179 */     double y = this.so3SensorFromWorld.get(2, 1);
/* 180 */     double mag = Math.sqrt(x * x + y * y);
/*     */ 
/* 182 */     if (mag < 0.1D) {
/* 183 */       return 0.0D;
/*     */     }
/*     */ 
/* 186 */     double heading = -90.0D - Math.atan2(y, x) / 3.141592653589793D * 180.0D;
/* 187 */     if (heading < 0.0D) {
/* 188 */       heading += 360.0D;
/*     */     }
/* 190 */     if (heading >= 360.0D) {
/* 191 */       heading -= 360.0D;
/*     */     }
/* 193 */     return heading;
/*     */   }
/*     */ 
/*     */   public synchronized void setHeadingDegrees(double heading)
/*     */   {
/* 202 */     double currentHeading = getHeadingDegrees();
/* 203 */     double deltaHeading = heading - currentHeading;
/* 204 */     double s = Math.sin(deltaHeading / 180.0D * 3.141592653589793D);
/* 205 */     double c = Math.cos(deltaHeading / 180.0D * 3.141592653589793D);
/*     */ 
/* 207 */     double[][] deltaHeadingRotationVals = { { c, -s, 0.0D }, { s, c, 0.0D }, { 0.0D, 0.0D, 1.0D } };
/*     */ 
/* 209 */     arrayAssign(deltaHeadingRotationVals, this.setHeadingDegreesTempM1);
/* 210 */     Matrix3x3d.mult(this.so3SensorFromWorld, this.setHeadingDegreesTempM1, this.so3SensorFromWorld);
/*     */   }
/*     */ 
/*     */   public double[] getGLMatrix()
/*     */   {
/* 220 */     return glMatrixFromSo3(this.so3SensorFromWorld);
/*     */   }
/*     */ 
/*     */   public double[] getPredictedGLMatrix(double secondsAfterLastGyroEvent)
/*     */   {
/* 234 */     double dT = secondsAfterLastGyroEvent;
/* 235 */     Vector3d pmu = this.getPredictedGLMatrixTempV1;
/* 236 */     pmu.set(this.lastGyro);
/* 237 */     pmu.scale(-dT);
/* 238 */     Matrix3x3d so3PredictedMotion = this.getPredictedGLMatrixTempM1;
/* 239 */     So3Util.sO3FromMu(pmu, so3PredictedMotion);
/*     */ 
/* 241 */     Matrix3x3d so3PredictedState = this.getPredictedGLMatrixTempM2;
/* 242 */     Matrix3x3d.mult(so3PredictedMotion, this.so3SensorFromWorld, so3PredictedState);
/*     */ 
/* 244 */     return glMatrixFromSo3(so3PredictedState);
/*     */   }
/*     */ 
/*     */   public Matrix3x3d getRotationMatrix()
/*     */   {
/* 252 */     return this.so3SensorFromWorld;
/*     */   }
/*     */ 
/*     */   public static void arrayAssign(double[][] data, Matrix3x3d m)
/*     */   {
/* 262 */     assert (3 == data.length);
/* 263 */     assert (3 == data[0].length);
/* 264 */     assert (3 == data[1].length);
/* 265 */     assert (3 == data[2].length);
/* 266 */     m.set(data[0][0], data[0][1], data[0][2], data[1][0], data[1][1], data[1][2], data[2][0], data[2][1], data[2][2]);
/*     */   }
/*     */ 
/*     */   public boolean isAlignedToGravity()
/*     */   {
/* 276 */     return this.alignedToGravity;
/*     */   }
/*     */ 
/*     */   public boolean isAlignedToNorth()
/*     */   {
/* 284 */     return this.alignedToNorth;
/*     */   }
/*     */ 
/*     */   public synchronized void processGyro(Vector3d gyro, long sensorTimeStamp)
/*     */   {
/* 294 */     float kTimeThreshold = 0.04F;
/* 295 */     float kdTdefault = 0.01F;
/* 296 */     if (this.sensorTimeStampGyro != 0L) {
/* 297 */       float dT = (float)(sensorTimeStamp - this.sensorTimeStampGyro) * 1.0E-09F;
/* 298 */       if (dT > 0.04F)
/* 299 */         dT = this.gyroFilterValid ? this.filteredGyroTimestep : 0.01F;
/*     */       else {
/* 301 */         filterGyroTimestep(dT);
/*     */       }
/*     */ 
/* 304 */       this.mu.set(gyro);
/* 305 */       this.mu.scale(-dT);
/* 306 */       So3Util.sO3FromMu(this.mu, this.so3LastMotion);
/*     */ 
/* 308 */       this.processGyroTempM1.set(this.so3SensorFromWorld);
/* 309 */       Matrix3x3d.mult(this.so3LastMotion, this.so3SensorFromWorld, this.processGyroTempM1);
/* 310 */       this.so3SensorFromWorld.set(this.processGyroTempM1);
/*     */ 
/* 312 */       updateCovariancesAfterMotion();
/*     */ 
/* 314 */       this.processGyroTempM2.set(this.mQ);
/* 315 */       this.processGyroTempM2.scale(dT * dT);
/* 316 */       this.mP.plusEquals(this.processGyroTempM2);
/*     */     }
/* 318 */     this.sensorTimeStampGyro = sensorTimeStamp;
/* 319 */     this.lastGyro.set(gyro);
/*     */   }
/*     */ 
/*     */   private void updateAccelCovariance(double currentAccelNorm)
/*     */   {
/* 336 */     double currentAccelNormChange = Math.abs(currentAccelNorm - this.previousAccelNorm);
/* 337 */     this.previousAccelNorm = currentAccelNorm;
/*     */ 
/* 340 */     double kSmoothingFactor = 0.5D;
/* 341 */     this.movingAverageAccelNormChange = (0.5D * currentAccelNormChange + 0.5D * this.movingAverageAccelNormChange);
/*     */ 
/* 347 */     double kMaxAccelNormChange = 0.15D;
/*     */ 
/* 349 */     double normChangeRatio = this.movingAverageAccelNormChange / 0.15D;
/* 350 */     double accelNoiseSigma = Math.min(7.0D, 0.75D + normChangeRatio * 6.25D);
/*     */ 
/* 354 */     this.mRaccel.setSameDiagonal(accelNoiseSigma * accelNoiseSigma);
/*     */   }
/*     */ 
/*     */   public synchronized void processAcc(Vector3d acc, long sensorTimeStamp)
/*     */   {
/* 366 */     this.mz.set(acc);
/* 367 */     updateAccelCovariance(this.mz.length());
/*     */ 
/* 369 */     if (this.alignedToGravity) {
/* 370 */       accObservationFunctionForNumericalJacobian(this.so3SensorFromWorld, this.mNu);
/*     */ 
/* 373 */       double eps = 1.0E-07D;
/* 374 */       for (int dof = 0; dof < 3; dof++) {
/* 375 */         Vector3d delta = this.processAccVDelta;
/* 376 */         delta.setZero();
/* 377 */         delta.setComponent(dof, eps);
/*     */ 
/* 379 */         So3Util.sO3FromMu(delta, this.processAccTempM1);
/* 380 */         Matrix3x3d.mult(this.processAccTempM1, this.so3SensorFromWorld, this.processAccTempM2);
/*     */ 
/* 382 */         accObservationFunctionForNumericalJacobian(this.processAccTempM2, this.processAccTempV1);
/*     */ 
/* 384 */         Vector3d withDelta = this.processAccTempV1;
/*     */ 
/* 386 */         Vector3d.sub(this.mNu, withDelta, this.processAccTempV2);
/* 387 */         this.processAccTempV2.scale(1.0D / eps);
/* 388 */         this.mH.setColumn(dof, this.processAccTempV2);
/*     */       }
/*     */ 
/* 392 */       this.mH.transpose(this.processAccTempM3);
/* 393 */       Matrix3x3d.mult(this.mP, this.processAccTempM3, this.processAccTempM4);
/* 394 */       Matrix3x3d.mult(this.mH, this.processAccTempM4, this.processAccTempM5);
/* 395 */       Matrix3x3d.add(this.processAccTempM5, this.mRaccel, this.mS);
/*     */ 
/* 398 */       this.mS.invert(this.processAccTempM3);
/* 399 */       this.mH.transpose(this.processAccTempM4);
/* 400 */       Matrix3x3d.mult(this.processAccTempM4, this.processAccTempM3, this.processAccTempM5);
/* 401 */       Matrix3x3d.mult(this.mP, this.processAccTempM5, this.mK);
/*     */ 
/* 404 */       Matrix3x3d.mult(this.mK, this.mNu, this.mx);
/*     */ 
/* 407 */       Matrix3x3d.mult(this.mK, this.mH, this.processAccTempM3);
/* 408 */       this.processAccTempM4.setIdentity();
/* 409 */       this.processAccTempM4.minusEquals(this.processAccTempM3);
/* 410 */       Matrix3x3d.mult(this.processAccTempM4, this.mP, this.processAccTempM3);
/* 411 */       this.mP.set(this.processAccTempM3);
/*     */ 
/* 413 */       So3Util.sO3FromMu(this.mx, this.so3LastMotion);
/*     */ 
/* 415 */       Matrix3x3d.mult(this.so3LastMotion, this.so3SensorFromWorld, this.so3SensorFromWorld);
/*     */ 
/* 417 */       updateCovariancesAfterMotion();
/*     */     }
/*     */     else
/*     */     {
/* 422 */       So3Util.sO3FromTwoVec(this.down, this.mz, this.so3SensorFromWorld);
/* 423 */       this.alignedToGravity = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void processMag(float[] mag, long sensorTimeStamp)
/*     */   {
/* 435 */     if (!this.alignedToGravity) {
/* 436 */       return;
/*     */     }
/* 438 */     this.mz.set(mag[0], mag[1], mag[2]);
/* 439 */     this.mz.normalize();
/*     */ 
/* 441 */     Vector3d downInSensorFrame = new Vector3d();
/* 442 */     this.so3SensorFromWorld.getColumn(2, downInSensorFrame);
/*     */ 
/* 444 */     Vector3d.cross(this.mz, downInSensorFrame, this.processMagTempV1);
/* 445 */     Vector3d perpToDownAndMag = this.processMagTempV1;
/* 446 */     perpToDownAndMag.normalize();
/*     */ 
/* 448 */     Vector3d.cross(downInSensorFrame, perpToDownAndMag, this.processMagTempV2);
/* 449 */     Vector3d magHorizontal = this.processMagTempV2;
/*     */ 
/* 452 */     magHorizontal.normalize();
/* 453 */     this.mz.set(magHorizontal);
/*     */ 
/* 455 */     if (this.alignedToNorth) {
/* 456 */       magObservationFunctionForNumericalJacobian(this.so3SensorFromWorld, this.mNu);
/*     */ 
/* 459 */       double eps = 1.0E-07D;
/* 460 */       for (int dof = 0; dof < 3; dof++) {
/* 461 */         Vector3d delta = this.processMagTempV3;
/* 462 */         delta.setZero();
/* 463 */         delta.setComponent(dof, eps);
/*     */ 
/* 465 */         So3Util.sO3FromMu(delta, this.processMagTempM1);
/* 466 */         Matrix3x3d.mult(this.processMagTempM1, this.so3SensorFromWorld, this.processMagTempM2);
/*     */ 
/* 468 */         magObservationFunctionForNumericalJacobian(this.processMagTempM2, this.processMagTempV4);
/*     */ 
/* 470 */         Vector3d withDelta = this.processMagTempV4;
/*     */ 
/* 472 */         Vector3d.sub(this.mNu, withDelta, this.processMagTempV5);
/* 473 */         this.processMagTempV5.scale(1.0D / eps);
/*     */ 
/* 475 */         this.mH.setColumn(dof, this.processMagTempV5);
/*     */       }
/*     */ 
/* 479 */       this.mH.transpose(this.processMagTempM4);
/* 480 */       Matrix3x3d.mult(this.mP, this.processMagTempM4, this.processMagTempM5);
/* 481 */       Matrix3x3d.mult(this.mH, this.processMagTempM5, this.processMagTempM6);
/* 482 */       Matrix3x3d.add(this.processMagTempM6, this.mR, this.mS);
/*     */ 
/* 485 */       this.mS.invert(this.processMagTempM4);
/* 486 */       this.mH.transpose(this.processMagTempM5);
/* 487 */       Matrix3x3d.mult(this.processMagTempM5, this.processMagTempM4, this.processMagTempM6);
/* 488 */       Matrix3x3d.mult(this.mP, this.processMagTempM6, this.mK);
/*     */ 
/* 491 */       Matrix3x3d.mult(this.mK, this.mNu, this.mx);
/*     */ 
/* 494 */       Matrix3x3d.mult(this.mK, this.mH, this.processMagTempM4);
/* 495 */       this.processMagTempM5.setIdentity();
/* 496 */       this.processMagTempM5.minusEquals(this.processMagTempM4);
/* 497 */       Matrix3x3d.mult(this.processMagTempM5, this.mP, this.processMagTempM4);
/* 498 */       this.mP.set(this.processMagTempM4);
/*     */ 
/* 500 */       So3Util.sO3FromMu(this.mx, this.so3LastMotion);
/*     */ 
/* 502 */       Matrix3x3d.mult(this.so3LastMotion, this.so3SensorFromWorld, this.processMagTempM4);
/* 503 */       this.so3SensorFromWorld.set(this.processMagTempM4);
/*     */ 
/* 505 */       updateCovariancesAfterMotion();
/*     */     }
/*     */     else
/*     */     {
/* 509 */       magObservationFunctionForNumericalJacobian(this.so3SensorFromWorld, this.mNu);
/* 510 */       So3Util.sO3FromMu(this.mNu, this.so3LastMotion);
/*     */ 
/* 512 */       Matrix3x3d.mult(this.so3LastMotion, this.so3SensorFromWorld, this.processMagTempM4);
/* 513 */       this.so3SensorFromWorld.set(this.processMagTempM4);
/*     */ 
/* 515 */       updateCovariancesAfterMotion();
/* 516 */       this.alignedToNorth = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private double[] glMatrixFromSo3(Matrix3x3d so3)
/*     */   {
/* 522 */     for (int r = 0; r < 3; r++)
/* 523 */       for (int c = 0; c < 3; c++)
/*     */       {
/* 525 */         this.rotationMatrix[(4 * c + r)] = so3.get(r, c);
/*     */       }
/*     */     double tmp62_61 = (this.rotationMatrix[11] = 0.0D); this.rotationMatrix[7] = tmp62_61; this.rotationMatrix[3] = tmp62_61;
/*     */     double tmp86_85 = (this.rotationMatrix[14] = 0.0D); this.rotationMatrix[13] = tmp86_85; this.rotationMatrix[12] = tmp86_85;
/*     */ 
/* 534 */     this.rotationMatrix[15] = 1.0D;
/*     */ 
/* 536 */     return this.rotationMatrix;
/*     */   }
/*     */ 
/*     */   private void filterGyroTimestep(float timeStep)
/*     */   {
/* 546 */     float kFilterCoeff = 0.95F;
/* 547 */     float kMinSamples = 10.0F;
/* 548 */     if (!this.timestepFilterInit) {
/* 549 */       this.filteredGyroTimestep = timeStep;
/* 550 */       this.numGyroTimestepSamples = 1;
/* 551 */       this.timestepFilterInit = true;
/*     */     }
/*     */     else {
/* 554 */       this.filteredGyroTimestep = (0.95F * this.filteredGyroTimestep + 0.05000001F * timeStep);
/*     */ 
/* 556 */       if (++this.numGyroTimestepSamples > 10.0F)
/* 557 */         this.gyroFilterValid = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void updateCovariancesAfterMotion()
/*     */   {
/* 563 */     this.so3LastMotion.transpose(this.updateCovariancesAfterMotionTempM1);
/* 564 */     Matrix3x3d.mult(this.mP, this.updateCovariancesAfterMotionTempM1, this.updateCovariancesAfterMotionTempM2);
/*     */ 
/* 566 */     Matrix3x3d.mult(this.so3LastMotion, this.updateCovariancesAfterMotionTempM2, this.mP);
/* 567 */     this.so3LastMotion.setIdentity();
/*     */   }
/*     */ 
/*     */   private void accObservationFunctionForNumericalJacobian(Matrix3x3d so3SensorFromWorldPred, Vector3d result)
/*     */   {
/* 576 */     Matrix3x3d.mult(so3SensorFromWorldPred, this.down, this.mh);
/* 577 */     So3Util.sO3FromTwoVec(this.mh, this.mz, this.accObservationFunctionForNumericalJacobianTempM);
/*     */ 
/* 580 */     So3Util.muFromSO3(this.accObservationFunctionForNumericalJacobianTempM, result);
/*     */   }
/*     */ 
/*     */   private void magObservationFunctionForNumericalJacobian(Matrix3x3d so3SensorFromWorldPred, Vector3d result)
/*     */   {
/* 592 */     Matrix3x3d.mult(so3SensorFromWorldPred, this.north, this.mh);
/* 593 */     So3Util.sO3FromTwoVec(this.mh, this.mz, this.magObservationFunctionForNumericalJacobianTempM);
/*     */ 
/* 595 */     So3Util.muFromSO3(this.magObservationFunctionForNumericalJacobianTempM, result);
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.sensors.internal.OrientationEKF
 * JD-Core Version:    0.6.2
 */