/*     */ package com.google.vrtoolkit.cardboard.sensors;
/*     */ 
/*     */ import android.hardware.Sensor;
/*     */ import android.hardware.SensorEvent;
/*     */ import android.hardware.SensorEventListener;
/*     */ import android.hardware.SensorManager;
/*     */ import android.os.Handler;
/*     */ import android.os.HandlerThread;
/*     */ import android.os.Looper;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class DeviceSensorLooper
/*     */   implements SensorEventProvider
/*     */ {
/*     */   private boolean mIsRunning;
/*     */   private SensorManager mSensorManager;
/*     */   private Looper mSensorLooper;
/*     */   private SensorEventListener mSensorEventListener;
/*  49 */   private final ArrayList<SensorEventListener> mRegisteredListeners = new ArrayList();
/*     */ 
/*  55 */   private static final int[] INPUT_SENSORS = { 1, 4 };
/*     */ 
/*     */   public DeviceSensorLooper(SensorManager sensorManager)
/*     */   {
/*  66 */     this.mSensorManager = sensorManager;
/*     */   }
/*     */ 
/*     */   public void start()
/*     */   {
/*  74 */     if (this.mIsRunning)
/*     */     {
/*  76 */       return;
/*     */     }
/*     */ 
/*  79 */     this.mSensorEventListener = new Object()
/*     */     {
/*     */       public void onSensorChanged(SensorEvent event)
/*     */       {
/*  83 */         for (SensorEventListener listener : DeviceSensorLooper.this.mRegisteredListeners)
/*  84 */           synchronized (listener) {
/*  85 */             listener.onSensorChanged(event);
/*     */           }
/*     */       }
/*     */ 
/*     */       public void onAccuracyChanged(Sensor sensor, int accuracy)
/*     */       {
/*  92 */         for (SensorEventListener listener : DeviceSensorLooper.this.mRegisteredListeners)
/*  93 */           synchronized (listener) {
/*  94 */             listener.onAccuracyChanged(sensor, accuracy);
/*     */           }
/*     */       }
/*     */     };
/* 100 */     HandlerThread sensorThread = new HandlerThread("sensor")
/*     */     {
/*     */       protected void onLooperPrepared() {
/* 103 */         Handler handler = new Handler(Looper.myLooper());
/* 104 */         for (int sensorType : DeviceSensorLooper.INPUT_SENSORS) {
/* 105 */           Sensor sensor = DeviceSensorLooper.this.mSensorManager.getDefaultSensor(sensorType);
/* 106 */           DeviceSensorLooper.this.mSensorManager.registerListener(DeviceSensorLooper.this.mSensorEventListener, sensor, 0, handler);
/*     */         }
/*     */       }
/*     */     };
/* 112 */     sensorThread.start();
/* 113 */     this.mSensorLooper = sensorThread.getLooper();
/* 114 */     this.mIsRunning = true;
/*     */   }
/*     */ 
/*     */   public void stop()
/*     */   {
/* 122 */     if (!this.mIsRunning)
/*     */     {
/* 124 */       return;
/*     */     }
/*     */ 
/* 127 */     this.mSensorManager.unregisterListener(this.mSensorEventListener);
/* 128 */     this.mSensorEventListener = null;
/*     */ 
/* 130 */     this.mSensorLooper.quit();
/* 131 */     this.mSensorLooper = null;
/* 132 */     this.mIsRunning = false;
/*     */   }
/*     */ 
/*     */   public void registerListener(SensorEventListener listener)
/*     */   {
/* 137 */     synchronized (this.mRegisteredListeners) {
/* 138 */       this.mRegisteredListeners.add(listener);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void unregisterListener(SensorEventListener listener)
/*     */   {
/* 144 */     synchronized (this.mRegisteredListeners) {
/* 145 */       this.mRegisteredListeners.remove(listener);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.sensors.DeviceSensorLooper
 * JD-Core Version:    0.6.2
 */