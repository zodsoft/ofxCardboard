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
/*     */   private boolean isRunning;
/*     */   private SensorManager sensorManager;
/*     */   private Looper sensorLooper;
/*     */   private SensorEventListener sensorEventListener;
/*  49 */   private final ArrayList<SensorEventListener> registeredListeners = new ArrayList();
/*     */ 
/*  55 */   private static final int[] INPUT_SENSORS = { 1, 4 };
/*     */ 
/*     */   public DeviceSensorLooper(SensorManager sensorManager)
/*     */   {
/*  66 */     this.sensorManager = sensorManager;
/*     */   }
/*     */ 
/*     */   public void start()
/*     */   {
/*  74 */     if (this.isRunning)
/*     */     {
/*  76 */       return;
/*     */     }
/*     */ 
/*  79 */     this.sensorEventListener = new Object()
/*     */     {
/*     */       public void onSensorChanged(SensorEvent event)
/*     */       {
/*  83 */         for (SensorEventListener listener : DeviceSensorLooper.this.registeredListeners)
/*  84 */           synchronized (listener) {
/*  85 */             listener.onSensorChanged(event);
/*     */           }
/*     */       }
/*     */ 
/*     */       public void onAccuracyChanged(Sensor sensor, int accuracy)
/*     */       {
/*  92 */         for (SensorEventListener listener : DeviceSensorLooper.this.registeredListeners)
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
/* 105 */           Sensor sensor = DeviceSensorLooper.this.sensorManager.getDefaultSensor(sensorType);
/* 106 */           DeviceSensorLooper.this.sensorManager.registerListener(DeviceSensorLooper.this.sensorEventListener, sensor, 0, handler);
/*     */         }
/*     */       }
/*     */     };
/* 112 */     sensorThread.start();
/* 113 */     this.sensorLooper = sensorThread.getLooper();
/* 114 */     this.isRunning = true;
/*     */   }
/*     */ 
/*     */   public void stop()
/*     */   {
/* 122 */     if (!this.isRunning)
/*     */     {
/* 124 */       return;
/*     */     }
/*     */ 
/* 127 */     this.sensorManager.unregisterListener(this.sensorEventListener);
/* 128 */     this.sensorEventListener = null;
/*     */ 
/* 130 */     this.sensorLooper.quit();
/* 131 */     this.sensorLooper = null;
/* 132 */     this.isRunning = false;
/*     */   }
/*     */ 
/*     */   public void registerListener(SensorEventListener listener)
/*     */   {
/* 137 */     synchronized (this.registeredListeners) {
/* 138 */       this.registeredListeners.add(listener);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void unregisterListener(SensorEventListener listener)
/*     */   {
/* 144 */     synchronized (this.registeredListeners) {
/* 145 */       this.registeredListeners.remove(listener);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.sensors.DeviceSensorLooper
 * JD-Core Version:    0.6.2
 */