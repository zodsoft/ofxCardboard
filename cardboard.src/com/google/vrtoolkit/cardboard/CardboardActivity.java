/*     */ package com.google.vrtoolkit.cardboard;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.nfc.NdefMessage;
/*     */ import android.os.Build.VERSION;
/*     */ import android.os.Bundle;
/*     */ import android.os.Handler;
/*     */ import android.view.KeyEvent;
/*     */ import android.view.View;
/*     */ import android.view.View.OnSystemUiVisibilityChangeListener;
/*     */ import android.view.ViewGroup.LayoutParams;
/*     */ import android.view.Window;
/*     */ import com.google.vrtoolkit.cardboard.sensors.MagnetSensor;
/*     */ import com.google.vrtoolkit.cardboard.sensors.MagnetSensor.OnCardboardTriggerListener;
/*     */ import com.google.vrtoolkit.cardboard.sensors.NfcSensor;
/*     */ import com.google.vrtoolkit.cardboard.sensors.NfcSensor.OnCardboardNfcListener;
/*     */ 
/*     */ public class CardboardActivity extends Activity
/*     */ {
/*     */   private static final int NAVIGATION_BAR_TIMEOUT_MS = 2000;
/*     */   private CardboardView mCardboardView;
/*     */   private MagnetSensor mMagnetSensor;
/*     */   private NfcSensor mNfcSensor;
/*  94 */   private SensorListener sensorListener = new SensorListener(null);
/*     */   private int mVolumeKeysMode;
/*     */ 
/*     */   public void setCardboardView(CardboardView cardboardView)
/*     */   {
/* 111 */     this.mCardboardView = cardboardView;
/*     */ 
/* 113 */     if (cardboardView == null) {
/* 114 */       return;
/*     */     }
/*     */ 
/* 118 */     NdefMessage tagContents = this.mNfcSensor.getTagContents();
/* 119 */     if (tagContents != null)
/* 120 */       updateCardboardDeviceParams(CardboardDeviceParams.createFromNfcContents(tagContents));
/*     */   }
/*     */ 
/*     */   public CardboardView getCardboardView()
/*     */   {
/* 131 */     return this.mCardboardView;
/*     */   }
/*     */ 
/*     */   public NfcSensor getNfcSensor()
/*     */   {
/* 145 */     return this.mNfcSensor;
/*     */   }
/*     */ 
/*     */   public void setVolumeKeysMode(int mode)
/*     */   {
/* 158 */     this.mVolumeKeysMode = mode;
/*     */   }
/*     */ 
/*     */   public int getVolumeKeysMode()
/*     */   {
/* 168 */     return this.mVolumeKeysMode;
/*     */   }
/*     */ 
/*     */   public boolean areVolumeKeysDisabled()
/*     */   {
/* 182 */     switch (this.mVolumeKeysMode) {
/*     */     case 0:
/* 184 */       return false;
/*     */     case 2:
/* 187 */       return this.mNfcSensor.isDeviceInCardboard();
/*     */     case 1:
/* 190 */       return true;
/*     */     }
/*     */ 
/* 193 */     int i = this.mVolumeKeysMode; throw new IllegalStateException(36 + "Invalid volume keys mode " + i);
/*     */   }
/*     */ 
/*     */   public void onInsertedIntoCardboard(CardboardDeviceParams cardboardDeviceParams)
/*     */   {
/* 205 */     updateCardboardDeviceParams(cardboardDeviceParams);
/*     */   }
/*     */ 
/*     */   public void onRemovedFromCardboard()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void onCardboardTrigger()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void updateCardboardDeviceParams(CardboardDeviceParams newParams)
/*     */   {
/* 234 */     if (this.mCardboardView != null)
/* 235 */       this.mCardboardView.updateCardboardDeviceParams(newParams);
/*     */   }
/*     */ 
/*     */   protected void onCreate(Bundle savedInstanceState)
/*     */   {
/* 242 */     super.onCreate(savedInstanceState);
/*     */ 
/* 245 */     requestWindowFeature(1);
/*     */ 
/* 248 */     getWindow().addFlags(128);
/*     */ 
/* 251 */     this.mMagnetSensor = new MagnetSensor(this);
/* 252 */     this.mMagnetSensor.setOnCardboardTriggerListener(this.sensorListener);
/*     */ 
/* 255 */     this.mNfcSensor = NfcSensor.getInstance(this);
/* 256 */     this.mNfcSensor.addOnCardboardNfcListener(this.sensorListener);
/*     */ 
/* 259 */     this.mNfcSensor.onNfcIntent(getIntent());
/*     */ 
/* 262 */     setVolumeKeysMode(2);
/*     */ 
/* 265 */     if (Build.VERSION.SDK_INT < 19) {
/* 266 */       final Handler handler = new Handler();
/* 267 */       getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
/*     */       {
/*     */         public void onSystemUiVisibilityChange(int visibility)
/*     */         {
/* 271 */           if ((visibility & 0x2) == 0)
/* 272 */             handler.postDelayed(new Runnable()
/*     */             {
/*     */               public void run() {
/* 275 */                 CardboardActivity.this.setFullscreenMode();
/*     */               }
/*     */             }
/*     */             , 2000L);
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void onResume()
/*     */   {
/* 287 */     super.onResume();
/*     */ 
/* 289 */     if (this.mCardboardView != null) {
/* 290 */       this.mCardboardView.onResume();
/*     */     }
/*     */ 
/* 294 */     this.mMagnetSensor.start();
/* 295 */     this.mNfcSensor.onResume(this);
/*     */   }
/*     */ 
/*     */   protected void onPause()
/*     */   {
/* 301 */     super.onPause();
/*     */ 
/* 303 */     if (this.mCardboardView != null) {
/* 304 */       this.mCardboardView.onPause();
/*     */     }
/*     */ 
/* 308 */     this.mMagnetSensor.stop();
/* 309 */     this.mNfcSensor.onPause(this);
/*     */   }
/*     */ 
/*     */   protected void onDestroy()
/*     */   {
/* 315 */     this.mNfcSensor.removeOnCardboardNfcListener(this.sensorListener);
/* 316 */     super.onDestroy();
/*     */   }
/*     */ 
/*     */   public void setContentView(View view)
/*     */   {
/* 322 */     if ((view instanceof CardboardView)) {
/* 323 */       setCardboardView((CardboardView)view);
/*     */     }
/*     */ 
/* 326 */     super.setContentView(view);
/*     */   }
/*     */ 
/*     */   public void setContentView(View view, ViewGroup.LayoutParams params)
/*     */   {
/* 332 */     if ((view instanceof CardboardView)) {
/* 333 */       setCardboardView((CardboardView)view);
/*     */     }
/*     */ 
/* 336 */     super.setContentView(view, params);
/*     */   }
/*     */ 
/*     */   public boolean onKeyDown(int keyCode, KeyEvent event)
/*     */   {
/* 343 */     if (((keyCode == 24) || (keyCode == 25)) && (areVolumeKeysDisabled()))
/*     */     {
/* 345 */       return true;
/*     */     }
/*     */ 
/* 348 */     return super.onKeyDown(keyCode, event);
/*     */   }
/*     */ 
/*     */   public boolean onKeyUp(int keyCode, KeyEvent event)
/*     */   {
/* 355 */     if (((keyCode == 24) || (keyCode == 25)) && (areVolumeKeysDisabled()))
/*     */     {
/* 357 */       return true;
/*     */     }
/*     */ 
/* 360 */     return super.onKeyUp(keyCode, event);
/*     */   }
/*     */ 
/*     */   public void onWindowFocusChanged(boolean hasFocus)
/*     */   {
/* 366 */     super.onWindowFocusChanged(hasFocus);
/*     */ 
/* 368 */     if (hasFocus)
/* 369 */       setFullscreenMode();
/*     */   }
/*     */ 
/*     */   private void setFullscreenMode()
/*     */   {
/* 375 */     getWindow().getDecorView().setSystemUiVisibility(5894);
/*     */   }
/*     */ 
/*     */   private class SensorListener
/*     */     implements MagnetSensor.OnCardboardTriggerListener, NfcSensor.OnCardboardNfcListener
/*     */   {
/*     */     private SensorListener()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void onInsertedIntoCardboard(CardboardDeviceParams deviceParams)
/*     */     {
/*  74 */       CardboardActivity.this.onInsertedIntoCardboard(deviceParams);
/*     */     }
/*     */ 
/*     */     public void onRemovedFromCardboard()
/*     */     {
/*  79 */       CardboardActivity.this.onRemovedFromCardboard();
/*     */     }
/*     */ 
/*     */     public void onCardboardTrigger()
/*     */     {
/*  84 */       CardboardActivity.this.onCardboardTrigger();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract class VolumeKeys
/*     */   {
/*     */     public static final int NOT_DISABLED = 0;
/*     */     public static final int DISABLED = 1;
/*     */     public static final int DISABLED_WHILE_IN_CARDBOARD = 2;
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.CardboardActivity
 * JD-Core Version:    0.6.2
 */