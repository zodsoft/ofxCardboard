/*     */ package com.google.vrtoolkit.cardboard.sensors;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import com.google.vrtoolkit.cardboard.CardboardDeviceParams;
/*     */ 
/*     */ public class SensorConnection
/*     */   implements MagnetSensor.OnCardboardTriggerListener, NfcSensor.OnCardboardNfcListener
/*     */ {
/*     */   private final SensorListener listener;
/*     */   private MagnetSensor magnetSensor;
/*     */   private NfcSensor nfcSensor;
/*     */ 
/*     */   public SensorConnection(SensorListener listener)
/*     */   {
/*  67 */     this.listener = listener;
/*     */   }
/*     */ 
/*     */   public NfcSensor getNfcSensor()
/*     */   {
/*  81 */     return this.nfcSensor;
/*     */   }
/*     */ 
/*     */   public void onCreate(Activity activity)
/*     */   {
/*  90 */     this.magnetSensor = new MagnetSensor(activity);
/*  91 */     this.magnetSensor.setOnCardboardTriggerListener(this);
/*     */ 
/*  94 */     this.nfcSensor = NfcSensor.getInstance(activity);
/*  95 */     this.nfcSensor.addOnCardboardNfcListener(this);
/*     */ 
/*  98 */     this.nfcSensor.onNfcIntent(activity.getIntent());
/*     */   }
/*     */ 
/*     */   public void onResume(Activity activity)
/*     */   {
/* 107 */     this.magnetSensor.start();
/* 108 */     this.nfcSensor.onResume(activity);
/*     */   }
/*     */ 
/*     */   public void onPause(Activity activity)
/*     */   {
/* 117 */     this.magnetSensor.stop();
/* 118 */     this.nfcSensor.onPause(activity);
/*     */   }
/*     */ 
/*     */   public void onDestroy(Activity activity)
/*     */   {
/* 126 */     this.nfcSensor.removeOnCardboardNfcListener(this);
/*     */   }
/*     */ 
/*     */   public void onInsertedIntoCardboard(CardboardDeviceParams deviceParams)
/*     */   {
/* 131 */     this.listener.onInsertedIntoCardboard(deviceParams);
/*     */   }
/*     */ 
/*     */   public void onRemovedFromCardboard()
/*     */   {
/* 136 */     this.listener.onRemovedFromCardboard();
/*     */   }
/*     */ 
/*     */   public void onCardboardTrigger()
/*     */   {
/* 141 */     this.listener.onCardboardTrigger();
/*     */   }
/*     */ 
/*     */   public static abstract interface SensorListener
/*     */   {
/*     */     public abstract void onInsertedIntoCardboard(CardboardDeviceParams paramCardboardDeviceParams);
/*     */ 
/*     */     public abstract void onRemovedFromCardboard();
/*     */ 
/*     */     public abstract void onCardboardTrigger();
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.sensors.SensorConnection
 * JD-Core Version:    0.6.2
 */