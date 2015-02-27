/*     */ package com.google.vrtoolkit.cardboard.sensors;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.hardware.Sensor;
/*     */ import android.hardware.SensorEvent;
/*     */ import android.hardware.SensorEventListener;
/*     */ import android.hardware.SensorManager;
/*     */ import android.os.Build;
/*     */ import android.os.Handler;
/*     */ import android.os.Looper;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public class MagnetSensor
/*     */ {
/*     */   private static final String HTC_ONE_MODEL = "HTC One";
/*     */   private TriggerDetector mDetector;
/*     */   private Thread mDetectorThread;
/*     */ 
/*     */   public MagnetSensor(Context context)
/*     */   {
/*  59 */     if ("HTC One".equals(Build.MODEL))
/*  60 */       this.mDetector = new VectorTriggerDetector(context);
/*     */     else
/*  62 */       this.mDetector = new ThresholdTriggerDetector(context);
/*     */   }
/*     */ 
/*     */   public void start()
/*     */   {
/*  70 */     this.mDetectorThread = new Thread(this.mDetector);
/*  71 */     this.mDetectorThread.start();
/*     */   }
/*     */ 
/*     */   public void stop()
/*     */   {
/*  78 */     if (this.mDetectorThread != null) {
/*  79 */       this.mDetectorThread.interrupt();
/*  80 */       this.mDetector.stop();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setOnCardboardTriggerListener(OnCardboardTriggerListener listener)
/*     */   {
/*  92 */     this.mDetector.setOnCardboardTriggerListener(listener, new Handler());
/*     */   }
/*     */ 
/*     */   private static class VectorTriggerDetector extends MagnetSensor.TriggerDetector
/*     */   {
/*     */     private static final String TAG = "ThresholdTriggerDetector";
/*     */     private static final long NS_REFRESH_TIME = 350000000L;
/*     */     private static final long NS_THROWAWAY_SIZE = 500000000L;
/*     */     private static final long NS_WAIT_SIZE = 100000000L;
/* 293 */     private long mLastFiring = 0L;
/*     */     private static int mXThreshold;
/*     */     private static int mYThreshold;
/*     */     private static int mZThreshold;
/*     */     private ArrayList<float[]> mSensorData;
/*     */     private ArrayList<Long> mSensorTimes;
/*     */ 
/*     */     public VectorTriggerDetector(Context context)
/*     */     {
/* 304 */       super();
/* 305 */       this.mSensorData = new ArrayList();
/* 306 */       this.mSensorTimes = new ArrayList();
/*     */ 
/* 308 */       mXThreshold = -3;
/* 309 */       mYThreshold = 15;
/* 310 */       mZThreshold = 6;
/*     */     }
/*     */ 
/*     */     public VectorTriggerDetector(Context context, int xThreshold, int yThreshold, int zThreshold)
/*     */     {
/* 315 */       super();
/* 316 */       this.mSensorData = new ArrayList();
/* 317 */       this.mSensorTimes = new ArrayList();
/*     */ 
/* 319 */       mXThreshold = xThreshold;
/* 320 */       mYThreshold = yThreshold;
/* 321 */       mZThreshold = zThreshold;
/*     */     }
/*     */ 
/*     */     private void addData(float[] values, long time) {
/* 325 */       this.mSensorData.add(values);
/* 326 */       this.mSensorTimes.add(Long.valueOf(time));
/* 327 */       while (((Long)this.mSensorTimes.get(0)).longValue() < time - 500000000L) {
/* 328 */         this.mSensorData.remove(0);
/* 329 */         this.mSensorTimes.remove(0);
/*     */       }
/*     */ 
/* 333 */       evaluateModel(time);
/*     */     }
/*     */ 
/*     */     private void evaluateModel(long time)
/*     */     {
/* 338 */       if ((time - this.mLastFiring < 350000000L) || (this.mSensorData.size() < 2)) {
/* 339 */         return;
/*     */       }
/*     */ 
/* 343 */       int baseIndex = 0;
/* 344 */       for (int i = 1; i < this.mSensorTimes.size(); i++) {
/* 345 */         if (time - ((Long)this.mSensorTimes.get(i)).longValue() < 100000000L) {
/* 346 */           baseIndex = i;
/* 347 */           break;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 352 */       float[] oldValues = (float[])this.mSensorData.get(baseIndex);
/* 353 */       float[] currentValues = (float[])this.mSensorData.get(this.mSensorData.size() - 1);
/* 354 */       if ((currentValues[0] - oldValues[0] < mXThreshold) && (currentValues[1] - oldValues[1] > mYThreshold) && (currentValues[2] - oldValues[2] > mZThreshold))
/*     */       {
/* 357 */         this.mLastFiring = time;
/* 358 */         handleButtonPressed();
/*     */       }
/*     */     }
/*     */ 
/*     */     public void onSensorChanged(SensorEvent event)
/*     */     {
/* 364 */       if (event.sensor.equals(this.mMagnetometer)) {
/* 365 */         float[] values = event.values;
/*     */ 
/* 367 */         if ((values[0] == 0.0F) && (values[1] == 0.0F) && (values[2] == 0.0F)) {
/* 368 */           return;
/*     */         }
/* 370 */         addData((float[])event.values.clone(), event.timestamp);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void onAccuracyChanged(Sensor sensor, int accuracy)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class ThresholdTriggerDetector extends MagnetSensor.TriggerDetector
/*     */   {
/*     */     private static final String TAG = "ThresholdTriggerDetector";
/*     */     private static final long NS_SEGMENT_SIZE = 200000000L;
/*     */     private static final long NS_WINDOW_SIZE = 400000000L;
/*     */     private static final long NS_WAIT_TIME = 350000000L;
/* 176 */     private long mLastFiring = 0L;
/* 177 */     private static int mT1 = 30;
/* 178 */     private static int mT2 = 130;
/*     */     private ArrayList<float[]> mSensorData;
/*     */     private ArrayList<Long> mSensorTimes;
/*     */ 
/*     */     public ThresholdTriggerDetector(Context context)
/*     */     {
/* 185 */       super();
/* 186 */       this.mSensorData = new ArrayList();
/* 187 */       this.mSensorTimes = new ArrayList();
/*     */     }
/*     */ 
/*     */     public ThresholdTriggerDetector(Context context, int t1, int t2) {
/* 191 */       super();
/* 192 */       this.mSensorData = new ArrayList();
/* 193 */       this.mSensorTimes = new ArrayList();
/*     */ 
/* 195 */       mT1 = t1;
/* 196 */       mT2 = t2;
/*     */     }
/*     */ 
/*     */     private void addData(float[] values, long time) {
/* 200 */       this.mSensorData.add(values);
/* 201 */       this.mSensorTimes.add(Long.valueOf(time));
/* 202 */       while (((Long)this.mSensorTimes.get(0)).longValue() < time - 400000000L) {
/* 203 */         this.mSensorData.remove(0);
/* 204 */         this.mSensorTimes.remove(0);
/*     */       }
/*     */ 
/* 208 */       evaluateModel(time);
/*     */     }
/*     */ 
/*     */     private void evaluateModel(long time)
/*     */     {
/* 215 */       if ((time - this.mLastFiring < 350000000L) || (this.mSensorData.size() < 2)) {
/* 216 */         return;
/*     */       }
/*     */ 
/* 220 */       float[] baseline = (float[])this.mSensorData.get(this.mSensorData.size() - 1);
/*     */ 
/* 223 */       int startSecondSegment = 0;
/* 224 */       for (int i = 0; i < this.mSensorTimes.size(); i++) {
/* 225 */         if (time - ((Long)this.mSensorTimes.get(i)).longValue() < 200000000L) {
/* 226 */           startSecondSegment = i;
/* 227 */           break;
/*     */         }
/*     */       }
/*     */ 
/* 231 */       float[] offsets = new float[this.mSensorData.size()];
/* 232 */       computeOffsets(offsets, baseline);
/* 233 */       float min1 = computeMinimum(Arrays.copyOfRange(offsets, 0, startSecondSegment));
/* 234 */       float max2 = computeMaximum(Arrays.copyOfRange(offsets, startSecondSegment, this.mSensorData.size()));
/*     */ 
/* 237 */       if ((min1 < mT1) && (max2 > mT2)) {
/* 238 */         this.mLastFiring = time;
/* 239 */         handleButtonPressed();
/*     */       }
/*     */     }
/*     */ 
/*     */     private void computeOffsets(float[] offsets, float[] baseline) {
/* 244 */       for (int i = 0; i < this.mSensorData.size(); i++) {
/* 245 */         float[] point = (float[])this.mSensorData.get(i);
/* 246 */         float[] o = { point[0] - baseline[0], point[1] - baseline[1], point[2] - baseline[2] };
/*     */ 
/* 248 */         float magnitude = (float)Math.sqrt(o[0] * o[0] + o[1] * o[1] + o[2] * o[2]);
/* 249 */         offsets[i] = magnitude;
/*     */       }
/*     */     }
/*     */ 
/*     */     private float computeMaximum(float[] offsets) {
/* 254 */       float max = (1.0F / -1.0F);
/* 255 */       for (float o : offsets) {
/* 256 */         max = Math.max(o, max);
/*     */       }
/* 258 */       return max;
/*     */     }
/*     */ 
/*     */     private float computeMinimum(float[] offsets) {
/* 262 */       float min = (1.0F / 1.0F);
/* 263 */       for (float o : offsets) {
/* 264 */         min = Math.min(o, min);
/*     */       }
/* 266 */       return min;
/*     */     }
/*     */ 
/*     */     public void onSensorChanged(SensorEvent event)
/*     */     {
/* 271 */       if (event.sensor.equals(this.mMagnetometer)) {
/* 272 */         float[] values = event.values;
/*     */ 
/* 274 */         if ((values[0] == 0.0F) && (values[1] == 0.0F) && (values[2] == 0.0F)) {
/* 275 */           return;
/*     */         }
/* 277 */         addData((float[])event.values.clone(), event.timestamp);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void onAccuracyChanged(Sensor sensor, int accuracy)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   private static abstract class TriggerDetector
/*     */     implements Runnable, SensorEventListener
/*     */   {
/*     */     protected static final String TAG = "TriggerDetector";
/*     */     protected SensorManager mSensorManager;
/*     */     protected Sensor mMagnetometer;
/*     */     protected MagnetSensor.OnCardboardTriggerListener mListener;
/*     */     protected Handler mHandler;
/*     */ 
/*     */     public TriggerDetector(Context context)
/*     */     {
/* 122 */       this.mSensorManager = ((SensorManager)context.getSystemService("sensor"));
/* 123 */       this.mMagnetometer = this.mSensorManager.getDefaultSensor(2);
/*     */     }
/*     */ 
/*     */     public synchronized void setOnCardboardTriggerListener(MagnetSensor.OnCardboardTriggerListener listener, Handler handler)
/*     */     {
/* 128 */       this.mListener = listener;
/* 129 */       this.mHandler = handler;
/*     */     }
/*     */ 
/*     */     protected void handleButtonPressed()
/*     */     {
/* 135 */       synchronized (this) {
/* 136 */         if (this.mListener != null)
/* 137 */           this.mHandler.post(new Runnable()
/*     */           {
/*     */             public void run() {
/* 140 */               if (MagnetSensor.TriggerDetector.this.mListener != null)
/* 141 */                 MagnetSensor.TriggerDetector.this.mListener.onCardboardTrigger();
/*     */             }
/*     */           });
/*     */       }
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 151 */       Looper.prepare();
/* 152 */       this.mSensorManager.registerListener(this, this.mMagnetometer, 0);
/*     */ 
/* 154 */       Looper.loop();
/*     */     }
/*     */ 
/*     */     public void stop() {
/* 158 */       this.mSensorManager.unregisterListener(this);
/*     */     }
/*     */ 
/*     */     public void onSensorChanged(SensorEvent event)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void onAccuracyChanged(Sensor sensor, int accuracy)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract interface OnCardboardTriggerListener
/*     */   {
/*     */     public abstract void onCardboardTrigger();
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.sensors.MagnetSensor
 * JD-Core Version:    0.6.2
 */