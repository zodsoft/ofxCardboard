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
/*     */ import com.google.vrtoolkit.cardboard.sensors.internal.GyroscopeBiasEstimator;
/*     */ import com.google.vrtoolkit.cardboard.sensors.internal.Matrix3x3d;
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
/*     */   private static final float PREDICTION_TIME_IN_SECONDS = 0.058F;
/*     */   private final Display display;
/*  62 */   private final float[] ekfToHeadTracker = new float[16];
/*     */ 
/*  65 */   private final float[] sensorToDisplay = new float[16];
/*     */ 
/*  67 */   private float displayRotation = (0.0F / 0.0F);
/*     */ 
/*  69 */   private final float[] neckModelTranslation = new float[16];
/*     */ 
/*  71 */   private final float[] tmpHeadView = new float[16];
/*  72 */   private final float[] tmpHeadView2 = new float[16];
/*     */ 
/*  74 */   private boolean neckModelEnabled = false;
/*     */   private volatile boolean tracking;
/*     */   private final OrientationEKF tracker;
/*  82 */   private final Object gyroBiasEstimatorMutex = new Object();
/*     */   private GyroscopeBiasEstimator gyroBiasEstimator;
/*     */   private SensorEventProvider sensorEventProvider;
/*     */   private Clock clock;
/*     */   private long latestGyroEventClockTimeNs;
/* 100 */   private final Vector3d gyroBias = new Vector3d();
/*     */ 
/* 103 */   private final Vector3d latestGyro = new Vector3d();
/*     */ 
/* 106 */   private final Vector3d latestAcc = new Vector3d();
/*     */ 
/*     */   public static HeadTracker createFromContext(Context context)
/*     */   {
/* 116 */     SensorManager sensorManager = (SensorManager)context.getSystemService("sensor");
/*     */ 
/* 118 */     Display display = ((WindowManager)context.getSystemService("window")).getDefaultDisplay();
/*     */ 
/* 122 */     return new HeadTracker(new DeviceSensorLooper(sensorManager), new SystemClock(), display);
/*     */   }
/*     */ 
/*     */   public HeadTracker(SensorEventProvider sensorEventProvider, Clock clock, Display display)
/*     */   {
/* 134 */     this.clock = clock;
/* 135 */     this.sensorEventProvider = sensorEventProvider;
/*     */ 
/* 137 */     this.tracker = new OrientationEKF();
/* 138 */     this.display = display;
/*     */ 
/* 141 */     Matrix.setIdentityM(this.neckModelTranslation, 0);
/* 142 */     Matrix.translateM(this.neckModelTranslation, 0, 0.0F, -0.075F, 0.08F);
/*     */   }
/*     */ 
/*     */   public void onSensorChanged(SensorEvent event)
/*     */   {
/* 153 */     if (event.sensor.getType() == 1) {
/* 154 */       this.latestAcc.set(event.values[0], event.values[1], event.values[2]);
/* 155 */       this.tracker.processAcc(this.latestAcc, event.timestamp);
/*     */ 
/* 157 */       synchronized (this.gyroBiasEstimatorMutex) {
/* 158 */         if (this.gyroBiasEstimator != null)
/* 159 */           this.gyroBiasEstimator.processAccelerometer(this.latestAcc, event.timestamp);
/*     */       }
/*     */     }
/* 162 */     else if (event.sensor.getType() == 4)
/*     */     {
/* 169 */       this.latestGyroEventClockTimeNs = this.clock.nanoTime();
/*     */ 
/* 171 */       this.latestGyro.set(event.values[0], event.values[1], event.values[2]);
/*     */ 
/* 173 */       synchronized (this.gyroBiasEstimatorMutex) {
/* 174 */         if (this.gyroBiasEstimator != null) {
/* 175 */           this.gyroBiasEstimator.processGyroscope(this.latestGyro, event.timestamp);
/*     */ 
/* 178 */           this.gyroBiasEstimator.getGyroBias(this.gyroBias);
/* 179 */           Vector3d.sub(this.latestGyro, this.gyroBias, this.latestGyro);
/*     */         }
/*     */       }
/* 182 */       this.tracker.processGyro(this.latestGyro, event.timestamp);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void onAccuracyChanged(Sensor sensor, int accuracy)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startTracking()
/*     */   {
/* 195 */     if (this.tracking) {
/* 196 */       return;
/*     */     }
/* 198 */     this.tracker.reset();
/*     */ 
/* 200 */     synchronized (this.gyroBiasEstimatorMutex) {
/* 201 */       if (this.gyroBiasEstimator != null) {
/* 202 */         this.gyroBiasEstimator.reset();
/*     */       }
/*     */     }
/*     */ 
/* 206 */     this.sensorEventProvider.registerListener(this);
/* 207 */     this.sensorEventProvider.start();
/* 208 */     this.tracking = true;
/*     */   }
/*     */ 
/*     */   public void resetTracker()
/*     */   {
/* 216 */     this.tracker.reset();
/*     */   }
/*     */ 
/*     */   public void stopTracking()
/*     */   {
/* 223 */     if (!this.tracking) {
/* 224 */       return;
/*     */     }
/*     */ 
/* 227 */     this.sensorEventProvider.unregisterListener(this);
/* 228 */     this.sensorEventProvider.stop();
/* 229 */     this.tracking = false;
/*     */   }
/*     */ 
/*     */   public void setNeckModelEnabled(boolean enabled)
/*     */   {
/* 244 */     this.neckModelEnabled = enabled;
/*     */   }
/*     */ 
/*     */   public void setGyroBiasEstimationEnabled(boolean enabled)
/*     */   {
/* 253 */     synchronized (this.gyroBiasEstimatorMutex) {
/* 254 */       if (!enabled)
/*     */       {
/* 257 */         this.gyroBiasEstimator = null;
/* 258 */       } else if (this.gyroBiasEstimator == null)
/* 259 */         this.gyroBiasEstimator = new GyroscopeBiasEstimator();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void getLastHeadView(float[] headView, int offset)
/*     */   {
/* 273 */     if (offset + 16 > headView.length) {
/* 274 */       throw new IllegalArgumentException("Not enough space to write the result");
/*     */     }
/*     */ 
/* 278 */     float rotation = 0.0F;
/* 279 */     switch (this.display.getRotation()) {
/*     */     case 0:
/* 281 */       rotation = 0.0F;
/* 282 */       break;
/*     */     case 1:
/* 284 */       rotation = 90.0F;
/* 285 */       break;
/*     */     case 2:
/* 287 */       rotation = 180.0F;
/* 288 */       break;
/*     */     case 3:
/* 290 */       rotation = 270.0F;
/*     */     }
/*     */ 
/* 293 */     if (rotation != this.displayRotation) {
/* 294 */       this.displayRotation = rotation;
/* 295 */       Matrix.setRotateEulerM(this.sensorToDisplay, 0, 0.0F, 0.0F, -rotation);
/* 296 */       Matrix.setRotateEulerM(this.ekfToHeadTracker, 0, -90.0F, 0.0F, rotation);
/*     */     }
/*     */ 
/* 300 */     synchronized (this.tracker) {
/* 301 */       if (!this.tracker.isReady()) {
/* 302 */         return;
/*     */       }
/* 304 */       double secondsSinceLastGyroEvent = TimeUnit.NANOSECONDS.toSeconds(this.clock.nanoTime() - this.latestGyroEventClockTimeNs);
/*     */ 
/* 306 */       double secondsToPredictForward = secondsSinceLastGyroEvent + 0.05799999833106995D;
/* 307 */       double[] mat = this.tracker.getPredictedGLMatrix(secondsToPredictForward);
/* 308 */       for (int i = 0; i < headView.length; i++) {
/* 309 */         this.tmpHeadView[i] = ((float)mat[i]);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 314 */     Matrix.multiplyMM(this.tmpHeadView2, 0, this.sensorToDisplay, 0, this.tmpHeadView, 0);
/*     */ 
/* 317 */     Matrix.multiplyMM(headView, offset, this.tmpHeadView2, 0, this.ekfToHeadTracker, 0);
/*     */ 
/* 321 */     if (this.neckModelEnabled)
/*     */     {
/* 326 */       Matrix.multiplyMM(this.tmpHeadView, 0, this.neckModelTranslation, 0, headView, offset);
/* 327 */       Matrix.translateM(headView, offset, this.tmpHeadView, 0, 0.0F, 0.075F, 0.0F);
/*     */     }
/*     */   }
/*     */ 
/*     */   Matrix3x3d getCurrentPoseForTest()
/*     */   {
/* 338 */     return new Matrix3x3d(this.tracker.getRotationMatrix());
/*     */   }
/*     */ 
/*     */   void setGyroBiasEstimator(GyroscopeBiasEstimator estimator)
/*     */   {
/* 345 */     synchronized (this.gyroBiasEstimatorMutex) {
/* 346 */       this.gyroBiasEstimator = estimator;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.sensors.HeadTracker
 * JD-Core Version:    0.6.2
 */