/*     */ package com.google.vrtoolkit.cardboard;
/*     */ 
/*     */ import android.app.NativeActivity;
/*     */ import android.nfc.NdefMessage;
/*     */ import android.os.Bundle;
/*     */ import android.view.KeyEvent;
/*     */ import android.view.View;
/*     */ import android.view.ViewGroup.LayoutParams;
/*     */ import com.google.vrtoolkit.cardboard.sensors.NfcSensor;
/*     */ import com.google.vrtoolkit.cardboard.sensors.SensorConnection;
/*     */ import com.google.vrtoolkit.cardboard.sensors.SensorConnection.SensorListener;
/*     */ 
/*     */ public class CardboardNativeActivity extends NativeActivity
/*     */   implements SensorConnection.SensorListener, VolumeKeyState.Handler
/*     */ {
/*  40 */   private final SensorConnection sensorConnection = new SensorConnection(this);
/*  41 */   private final VolumeKeyState volumeKeyState = new VolumeKeyState(this);
/*  42 */   private final FullscreenMode fullscreenMode = new FullscreenMode(this);
/*     */   private CardboardView cardboardView;
/*     */ 
/*     */   public void setCardboardView(CardboardView cardboardView)
/*     */   {
/*  58 */     this.cardboardView = cardboardView;
/*  59 */     if (cardboardView == null) {
/*  60 */       return;
/*     */     }
/*     */ 
/*  64 */     NdefMessage tagContents = this.sensorConnection.getNfcSensor().getTagContents();
/*  65 */     if (tagContents != null)
/*  66 */       updateCardboardDeviceParams(CardboardDeviceParams.createFromNfcContents(tagContents));
/*     */   }
/*     */ 
/*     */   public CardboardView getCardboardView()
/*     */   {
/*  76 */     return this.cardboardView;
/*     */   }
/*     */ 
/*     */   public NfcSensor getNfcSensor()
/*     */   {
/*  89 */     return this.sensorConnection.getNfcSensor();
/*     */   }
/*     */ 
/*     */   public void setVolumeKeysMode(int mode)
/*     */   {
/* 102 */     this.volumeKeyState.setVolumeKeysMode(mode);
/*     */   }
/*     */ 
/*     */   public int getVolumeKeysMode()
/*     */   {
/* 112 */     return this.volumeKeyState.getVolumeKeysMode();
/*     */   }
/*     */ 
/*     */   public boolean areVolumeKeysDisabled()
/*     */   {
/* 127 */     return this.volumeKeyState.areVolumeKeysDisabled(this.sensorConnection.getNfcSensor());
/*     */   }
/*     */ 
/*     */   public void onInsertedIntoCardboard(CardboardDeviceParams cardboardDeviceParams)
/*     */   {
/* 139 */     updateCardboardDeviceParams(cardboardDeviceParams);
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
/* 170 */     if (this.cardboardView != null)
/* 171 */       this.cardboardView.updateCardboardDeviceParams(newParams);
/*     */   }
/*     */ 
/*     */   protected void onCreate(Bundle savedInstanceState)
/*     */   {
/* 183 */     requestWindowFeature(1);
/*     */ 
/* 185 */     super.onCreate(savedInstanceState);
/*     */ 
/* 187 */     this.fullscreenMode.startFullscreenMode();
/* 188 */     this.sensorConnection.onCreate(this);
/* 189 */     this.volumeKeyState.onCreate();
/*     */   }
/*     */ 
/*     */   protected void onResume()
/*     */   {
/* 197 */     super.onResume();
/*     */ 
/* 199 */     if (this.cardboardView != null) {
/* 200 */       this.cardboardView.onResume();
/*     */     }
/* 202 */     this.sensorConnection.onResume(this);
/* 203 */     this.fullscreenMode.setFullscreenMode();
/*     */   }
/*     */ 
/*     */   protected void onPause()
/*     */   {
/* 211 */     super.onPause();
/*     */ 
/* 213 */     if (this.cardboardView != null) {
/* 214 */       this.cardboardView.onPause();
/*     */     }
/* 216 */     this.sensorConnection.onPause(this);
/*     */   }
/*     */ 
/*     */   protected void onDestroy()
/*     */   {
/* 224 */     this.sensorConnection.onDestroy(this);
/*     */ 
/* 226 */     super.onDestroy();
/*     */   }
/*     */ 
/*     */   public void setContentView(View view)
/*     */   {
/* 234 */     if ((view instanceof CardboardView)) {
/* 235 */       setCardboardView((CardboardView)view);
/*     */     }
/*     */ 
/* 238 */     super.setContentView(view);
/*     */   }
/*     */ 
/*     */   public void setContentView(View view, ViewGroup.LayoutParams params)
/*     */   {
/* 246 */     if ((view instanceof CardboardView)) {
/* 247 */       setCardboardView((CardboardView)view);
/*     */     }
/*     */ 
/* 250 */     super.setContentView(view, params);
/*     */   }
/*     */ 
/*     */   public boolean onKeyDown(int keyCode, KeyEvent event)
/*     */   {
/* 259 */     return (this.volumeKeyState.onKey(keyCode)) || (super.onKeyDown(keyCode, event));
/*     */   }
/*     */ 
/*     */   public boolean onKeyUp(int keyCode, KeyEvent event)
/*     */   {
/* 268 */     return (this.volumeKeyState.onKey(keyCode)) || (super.onKeyUp(keyCode, event));
/*     */   }
/*     */ 
/*     */   public void onWindowFocusChanged(boolean hasFocus)
/*     */   {
/* 276 */     super.onWindowFocusChanged(hasFocus);
/* 277 */     this.fullscreenMode.onWindowFocusChanged(hasFocus);
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.CardboardNativeActivity
 * JD-Core Version:    0.6.2
 */