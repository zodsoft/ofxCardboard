/*     */ package com.google.vrtoolkit.cardboard.sensors;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.hardware.Sensor;
/*     */ import android.hardware.SensorEvent;
/*     */ import android.hardware.SensorEventListener;
/*     */ import android.hardware.SensorManager;
/*     */ import android.opengl.Matrix;
/*     */ import android.view.Display;
/*     */ import android.view.WindowManager;
/*     */ import com.google.vrtoolkit.cardboard.sensors.internal.OrientationEKF;
/*     */ import com.google.vrtoolkit.cardboard.sensors.internal.Vector3d;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ public class HeadTracker
/*     */   implements SensorEventListener
/*     */ {
/*     */   private static final float DEFAULT_NECK_HORIZONTAL_OFFSET = 0.08F;
/*     */   private static final float DEFAULT_NECK_VERTICAL_OFFSET = 0.075F;
/*     */   private static final boolean DEFAULT_NECK_MODEL_ENABLED = false;
/*     */   private final Display mDisplay;
/*  53 */   private final float[] mEkfToHeadTracker = new float[16];
/*     */ 
/*  56 */   private final float[] mSensorToDisplay = new float[16];
/*  57 */   private float mDisplayRotation = (0.0F / 0.0F);
/*     */ 
/*  59 */   private final float[] mNeckModelTranslation = new float[16];
/*     */ 
/*  61 */   private final float[] mTmpHeadView = new float[16];
/*  62 */   private final float[] mTmpHeadView2 = new float[16];
/*     */ 
/*  64 */   private boolean mNeckModelEnabled = false;
/*     */   private volatile boolean mTracking;
/*     */   private OrientationEKF mTracker;
/*     */   private SensorEventProvider mSensorEventProvider;
/*     */   private Clock mClock;
/*     */   private long mLatestGyroEventClockTimeNs;
/*  84 */   private final Vector3d mGyroBias = new Vector3d();
/*     */ 
/*  87 */   private final Vector3d mLatestGyro = new Vector3d();
/*     */ 
/*  90 */   private final Vector3d mLatestAcc = new Vector3d();
/*     */ 
/*     */   public static HeadTracker createFromContext(Context context)
/*     */   {
/* 100 */     SensorManager sensorManager = (SensorManager)context.getSystemService("sensor");
/*     */ 
/* 102 */     Display display = ((WindowManager)context.getSystemService("window")).getDefaultDisplay();
/*     */ 
/* 106 */     return new HeadTracker(new DeviceSensorLooper(sensorManager), new SystemClock(), display);
/*     */   }
/*     */ 
/*     */   public HeadTracker(SensorEventProvider sensorEventProvider, Clock clock, Display display)
/*     */   {
/* 119 */     this.mClock = clock;
/* 120 */     this.mSensorEventProvider = sensorEventProvider;
/*     */ 
/* 122 */     this.mTracker = new OrientationEKF();
/* 123 */     this.mDisplay = display;
/*     */ 
/* 126 */     Matrix.setIdentityM(this.mNeckModelTranslation, 0);
/* 127 */     Matrix.translateM(this.mNeckModelTranslation, 0, 0.0F, -0.075F, 0.08F);
/*     */   }
/*     */ 
/*     */   public void onSensorChanged(SensorEvent event)
/*     */   {
/* 138 */     if (event.sensor.getType() == 1) {
/* 139 */       this.mLatestAcc.set(event.values[0], event.values[1], event.values[2]);
/* 140 */       this.mTracker.processAcc(this.mLatestAcc, event.timestamp);
/* 141 */     } else if (event.sensor.getType() == 4)
/*     */     {
/* 148 */       this.mLatestGyroEventClockTimeNs = this.mClock.nanoTime();
/*     */ 
/* 150 */       this.mLatestGyro.set(event.values[0], event.values[1], event.values[2]);
/* 151 */       Vector3d.sub(this.mLatestGyro, this.mGyroBias, this.mLatestGyro);
/* 152 */       this.mTracker.processGyro(this.mLatestGyro, event.timestamp);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void onAccuracyChanged(Sensor sensor, int accuracy)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startTracking()
/*     */   {
/* 165 */     if (this.mTracking) {
/* 166 */       return;
/*     */     }
/* 168 */     this.mTracker.reset();
/* 169 */     this.mSensorEventProvider.registerListener(this);
/* 170 */     this.mSensorEventProvider.start();
/* 171 */     this.mTracking = true;
/*     */   }
/*     */ 
/*     */   public void stopTracking()
/*     */   {
/* 178 */     if (!this.mTracking) {
/* 179 */       return;
/*     */     }
/*     */ 
/* 182 */     this.mSensorEventProvider.unregisterListener(this);
/* 183 */     this.mSensorEventProvider.stop();
/* 184 */     this.mTracking = false;
/*     */   }
/*     */ 
/*     */   public void setGyroBias(float[] gyroBias)
/*     */   {
/* 197 */     if (gyroBias == null) {
/* 198 */       this.mGyroBias.setZero();
/* 199 */       return;
/*     */     }
/* 201 */     if (gyroBias.length != 3) {
/* 202 */       throw new IllegalArgumentException("Gyro bias should be an array of 3 values");
/*     */     }
/* 204 */     this.mGyroBias.set(gyroBias[0], gyroBias[1], gyroBias[2]);
/*     */   }
/*     */ 
/*     */   public void setNeckModelEnabled(boolean enabled)
/*     */   {
/* 219 */     this.mNeckModelEnabled = enabled;
/*     */   }
/*     */ 
/*     */   public void getLastHeadView(float[] headView, int offset)
/*     */   {
/* 231 */     if (offset + 16 > headView.length) {
/* 232 */       throw new IllegalArgumentException("Not enough space to write the result");
/*     */     }
/*     */ 
/* 236 */     float rotation = 0.0F;
/* 237 */     switch (this.mDisplay.getRotation()) {
/*     */     case 0:
/* 239 */       rotation = 0.0F;
/* 240 */       break;
/*     */     case 1:
/* 242 */       rotation = 90.0F;
/* 243 */       break;
/*     */     case 2:
/* 245 */       rotation = 180.0F;
/* 246 */       break;
/*     */     case 3:
/* 248 */       rotation = 270.0F;
/*     */     }
/*     */ 
/* 251 */     if (rotation != this.mDisplayRotation) {
/* 252 */       this.mDisplayRotation = rotation;
/* 253 */       Matrix.setRotateEulerM(this.mSensorToDisplay, 0, 0.0F, 0.0F, -rotation);
/* 254 */       Matrix.setRotateEulerM(this.mEkfToHeadTracker, 0, -90.0F, 0.0F, rotation);
/*     */     }
/*     */ 
/* 258 */     synchronized (this.mTracker) {
/* 259 */       double secondsSinceLastGyroEvent = TimeUnit.NANOSECONDS.toSeconds(this.mClock.nanoTime() - this.mLatestGyroEventClockTimeNs);
/*     */ 
/* 264 */       double secondsToPredictForward = secondsSinceLastGyroEvent + 0.03333333333333333D;
/* 265 */       double[] mat = this.mTracker.getPredictedGLMatrix(secondsToPredictForward);
/* 266 */       for (int i = 0; i < headView.length; i++) {
/* 267 */         this.mTmpHeadView[i] = ((float)mat[i]);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 272 */     Matrix.multiplyMM(this.mTmpHeadView2, 0, this.mSensorToDisplay, 0, this.mTmpHeadView, 0);
/*     */ 
/* 275 */     Matrix.multiplyMM(headView, offset, this.mTmpHeadView2, 0, this.mEkfToHeadTracker, 0);
/*     */ 
/* 279 */     if (this.mNeckModelEnabled)
/*     */     {
/* 284 */       Matrix.multiplyMM(this.mTmpHeadView, 0, this.mNeckModelTranslation, 0, headView, offset);
/* 285 */       Matrix.translateM(headView, offset, this.mTmpHeadView, 0, 0.0F, 0.075F, 0.0F);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.sensors.HeadTracker
 * JD-Core Version:    0.6.2
 */