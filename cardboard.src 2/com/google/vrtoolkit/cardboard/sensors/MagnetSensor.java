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
/*     */   private TriggerDetector detector;
/*     */   private Thread detectorThread;
/*     */ 
/*     */   public MagnetSensor(Context context)
/*     */   {
/*  58 */     if ("HTC One".equals(Build.MODEL))
/*  59 */       this.detector = new VectorTriggerDetector(context);
/*     */     else
/*  61 */       this.detector = new ThresholdTriggerDetector(context);
/*     */   }
/*     */ 
/*     */   public void start()
/*     */   {
/*  69 */     this.detectorThread = new Thread(this.detector);
/*  70 */     this.detectorThread.start();
/*     */   }
/*     */ 
/*     */   public void stop()
/*     */   {
/*  77 */     if (this.detectorThread != null) {
/*  78 */       this.detectorThread.interrupt();
/*  79 */       this.detector.stop();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setOnCardboardTriggerListener(OnCardboardTriggerListener listener)
/*     */   {
/*  91 */     this.detector.setOnCardboardTriggerListener(listener, new Handler());
/*     */   }
/*     */ 
/*     */   private static class VectorTriggerDetector extends MagnetSensor.TriggerDetector
/*     */   {
/*     */     private static final long NS_REFRESH_TIME = 350000000L;
/*     */     private static final long NS_THROWAWAY_SIZE = 500000000L;
/*     */     private static final long NS_WAIT_SIZE = 100000000L;
/* 288 */     private long lastFiring = 0L;
/*     */     private static int xThreshold;
/*     */     private static int yThreshold;
/*     */     private static int zThreshold;
/*     */     private ArrayList<float[]> sensorData;
/*     */     private ArrayList<Long> sensorTimes;
/*     */ 
/*     */     public VectorTriggerDetector(Context context)
/*     */     {
/* 299 */       super();
/* 300 */       this.sensorData = new ArrayList();
/* 301 */       this.sensorTimes = new ArrayList();
/*     */ 
/* 303 */       xThreshold = -3;
/* 304 */       yThreshold = 15;
/* 305 */       zThreshold = 6;
/*     */     }
/*     */ 
/*     */     public VectorTriggerDetector(Context context, int xThreshold, int yThreshold, int zThreshold)
/*     */     {
/* 310 */       super();
/* 311 */       this.sensorData = new ArrayList();
/* 312 */       this.sensorTimes = new ArrayList();
/*     */ 
/* 314 */       xThreshold = xThreshold;
/* 315 */       yThreshold = yThreshold;
/* 316 */       zThreshold = zThreshold;
/*     */     }
/*     */ 
/*     */     private void addData(float[] values, long time) {
/* 320 */       this.sensorData.add(values);
/* 321 */       this.sensorTimes.add(Long.valueOf(time));
/* 322 */       while (((Long)this.sensorTimes.get(0)).longValue() < time - 500000000L) {
/* 323 */         this.sensorData.remove(0);
/* 324 */         this.sensorTimes.remove(0);
/*     */       }
/*     */ 
/* 328 */       evaluateModel(time);
/*     */     }
/*     */ 
/*     */     private void evaluateModel(long time)
/*     */     {
/* 333 */       if ((time - this.lastFiring < 350000000L) || (this.sensorData.size() < 2)) {
/* 334 */         return;
/*     */       }
/*     */ 
/* 338 */       int baseIndex = 0;
/* 339 */       for (int i = 1; i < this.sensorTimes.size(); i++) {
/* 340 */         if (time - ((Long)this.sensorTimes.get(i)).longValue() < 100000000L) {
/* 341 */           baseIndex = i;
/* 342 */           break;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 347 */       float[] oldValues = (float[])this.sensorData.get(baseIndex);
/* 348 */       float[] currentValues = (float[])this.sensorData.get(this.sensorData.size() - 1);
/* 349 */       if ((currentValues[0] - oldValues[0] < xThreshold) && (currentValues[1] - oldValues[1] > yThreshold) && (currentValues[2] - oldValues[2] > zThreshold))
/*     */       {
/* 352 */         this.lastFiring = time;
/* 353 */         handleButtonPressed();
/*     */       }
/*     */     }
/*     */ 
/*     */     public void onSensorChanged(SensorEvent event)
/*     */     {
/* 359 */       if (event.sensor.equals(this.magnetometer)) {
/* 360 */         float[] values = event.values;
/*     */ 
/* 362 */         if ((values[0] == 0.0F) && (values[1] == 0.0F) && (values[2] == 0.0F)) {
/* 363 */           return;
/*     */         }
/* 365 */         addData((float[])event.values.clone(), event.timestamp);
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
/*     */     private static final long NS_SEGMENT_SIZE = 200000000L;
/*     */     private static final long NS_WINDOW_SIZE = 400000000L;
/*     */     private static final long NS_WAIT_TIME = 350000000L;
/* 173 */     private long lastFiring = 0L;
/* 174 */     private static int t1 = 30;
/* 175 */     private static int t2 = 130;
/*     */     private ArrayList<float[]> sensorData;
/*     */     private ArrayList<Long> sensorTimes;
/*     */ 
/*     */     public ThresholdTriggerDetector(Context context)
/*     */     {
/* 182 */       super();
/* 183 */       this.sensorData = new ArrayList();
/* 184 */       this.sensorTimes = new ArrayList();
/*     */     }
/*     */ 
/*     */     public ThresholdTriggerDetector(Context context, int t1, int t2) {
/* 188 */       super();
/* 189 */       this.sensorData = new ArrayList();
/* 190 */       this.sensorTimes = new ArrayList();
/*     */ 
/* 192 */       t1 = t1;
/* 193 */       t2 = t2;
/*     */     }
/*     */ 
/*     */     private void addData(float[] values, long time) {
/* 197 */       this.sensorData.add(values);
/* 198 */       this.sensorTimes.add(Long.valueOf(time));
/* 199 */       while (((Long)this.sensorTimes.get(0)).longValue() < time - 400000000L) {
/* 200 */         this.sensorData.remove(0);
/* 201 */         this.sensorTimes.remove(0);
/*     */       }
/*     */ 
/* 205 */       evaluateModel(time);
/*     */     }
/*     */ 
/*     */     private void evaluateModel(long time)
/*     */     {
/* 212 */       if ((time - this.lastFiring < 350000000L) || (this.sensorData.size() < 2)) {
/* 213 */         return;
/*     */       }
/*     */ 
/* 217 */       float[] baseline = (float[])this.sensorData.get(this.sensorData.size() - 1);
/*     */ 
/* 220 */       int startSecondSegment = 0;
/* 221 */       for (int i = 0; i < this.sensorTimes.size(); i++) {
/* 222 */         if (time - ((Long)this.sensorTimes.get(i)).longValue() < 200000000L) {
/* 223 */           startSecondSegment = i;
/* 224 */           break;
/*     */         }
/*     */       }
/*     */ 
/* 228 */       float[] offsets = new float[this.sensorData.size()];
/* 229 */       computeOffsets(offsets, baseline);
/* 230 */       float min1 = computeMinimum(Arrays.copyOfRange(offsets, 0, startSecondSegment));
/* 231 */       float max2 = computeMaximum(Arrays.copyOfRange(offsets, startSecondSegment, this.sensorData.size()));
/*     */ 
/* 234 */       if ((min1 < t1) && (max2 > t2)) {
/* 235 */         this.lastFiring = time;
/* 236 */         handleButtonPressed();
/*     */       }
/*     */     }
/*     */ 
/*     */     private void computeOffsets(float[] offsets, float[] baseline) {
/* 241 */       for (int i = 0; i < this.sensorData.size(); i++) {
/* 242 */         float[] point = (float[])this.sensorData.get(i);
/* 243 */         float[] o = { point[0] - baseline[0], point[1] - baseline[1], point[2] - baseline[2] };
/*     */ 
/* 245 */         float magnitude = (float)Math.sqrt(o[0] * o[0] + o[1] * o[1] + o[2] * o[2]);
/* 246 */         offsets[i] = magnitude;
/*     */       }
/*     */     }
/*     */ 
/*     */     private float computeMaximum(float[] offsets) {
/* 251 */       float max = (1.0F / -1.0F);
/* 252 */       for (float o : offsets) {
/* 253 */         max = Math.max(o, max);
/*     */       }
/* 255 */       return max;
/*     */     }
/*     */ 
/*     */     private float computeMinimum(float[] offsets) {
/* 259 */       float min = (1.0F / 1.0F);
/* 260 */       for (float o : offsets) {
/* 261 */         min = Math.min(o, min);
/*     */       }
/* 263 */       return min;
/*     */     }
/*     */ 
/*     */     public void onSensorChanged(SensorEvent event)
/*     */     {
/* 268 */       if (event.sensor.equals(this.magnetometer)) {
/* 269 */         float[] values = event.values;
/*     */ 
/* 271 */         if ((values[0] == 0.0F) && (values[1] == 0.0F) && (values[2] == 0.0F)) {
/* 272 */           return;
/*     */         }
/* 274 */         addData((float[])event.values.clone(), event.timestamp);
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
/*     */     protected SensorManager sensorManager;
/*     */     protected Sensor magnetometer;
/*     */     protected MagnetSensor.OnCardboardTriggerListener listener;
/*     */     protected Handler handler;
/*     */ 
/*     */     public TriggerDetector(Context context)
/*     */     {
/* 121 */       this.sensorManager = ((SensorManager)context.getSystemService("sensor"));
/* 122 */       this.magnetometer = this.sensorManager.getDefaultSensor(2);
/*     */     }
/*     */ 
/*     */     public synchronized void setOnCardboardTriggerListener(MagnetSensor.OnCardboardTriggerListener listener, Handler handler)
/*     */     {
/* 127 */       this.listener = listener;
/* 128 */       this.handler = handler;
/*     */     }
/*     */ 
/*     */     protected void handleButtonPressed()
/*     */     {
/* 134 */       synchronized (this) {
/* 135 */         if (this.listener != null)
/* 136 */           this.handler.post(new Runnable()
/*     */           {
/*     */             public void run() {
/* 139 */               if (MagnetSensor.TriggerDetector.this.listener != null)
/* 140 */                 MagnetSensor.TriggerDetector.this.listener.onCardboardTrigger();
/*     */             }
/*     */           });
/*     */       }
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 150 */       Looper.prepare();
/* 151 */       this.sensorManager.registerListener(this, this.magnetometer, 0);
/*     */ 
/* 153 */       Looper.loop();
/*     */     }
/*     */ 
/*     */     public void stop() {
/* 157 */       this.sensorManager.unregisterListener(this);
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