/*     */ package com.google.vrtoolkit.cardboard;
/*     */ 
/*     */ import com.google.vrtoolkit.cardboard.sensors.NfcSensor;
/*     */ 
/*     */ final class VolumeKeyState
/*     */ {
/*     */   private final Handler handler;
/*     */   private int volumeKeysMode;
/*     */ 
/*     */   public VolumeKeyState(Handler handler)
/*     */   {
/*  61 */     this.handler = handler;
/*  62 */     this.volumeKeysMode = 0;
/*     */   }
/*     */ 
/*     */   public void onCreate()
/*     */   {
/*  70 */     this.volumeKeysMode = 2;
/*     */   }
/*     */ 
/*     */   public void setVolumeKeysMode(int mode)
/*     */   {
/*  83 */     this.volumeKeysMode = mode;
/*     */   }
/*     */ 
/*     */   public int getVolumeKeysMode()
/*     */   {
/*  93 */     return this.volumeKeysMode;
/*     */   }
/*     */ 
/*     */   public boolean areVolumeKeysDisabled(NfcSensor nfcSensor)
/*     */   {
/* 111 */     switch (this.volumeKeysMode) {
/*     */     case 0:
/* 113 */       return false;
/*     */     case 2:
/* 116 */       return nfcSensor.isDeviceInCardboard();
/*     */     case 1:
/* 119 */       return true;
/*     */     }
/*     */ 
/* 122 */     int i = this.volumeKeysMode; throw new IllegalStateException(36 + "Invalid volume keys mode " + i);
/*     */   }
/*     */ 
/*     */   public boolean onKey(int keyCode)
/*     */   {
/* 133 */     return ((keyCode == 24) || (keyCode == 25)) && (this.handler.areVolumeKeysDisabled());
/*     */   }
/*     */ 
/*     */   public static abstract interface Handler
/*     */   {
/*     */     public abstract boolean areVolumeKeysDisabled();
/*     */ 
/*     */     public static abstract class VolumeKeys
/*     */     {
/*     */       public static final int NOT_DISABLED = 0;
/*     */       public static final int DISABLED = 1;
/*     */       public static final int DISABLED_WHILE_IN_CARDBOARD = 2;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.VolumeKeyState
 * JD-Core Version:    0.6.2
 */