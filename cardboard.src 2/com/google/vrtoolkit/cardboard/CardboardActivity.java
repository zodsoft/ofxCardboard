/*     */ package com.google.vrtoolkit.cardboard;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.nfc.NdefMessage;
/*     */ import android.os.Bundle;
/*     */ import android.view.KeyEvent;
/*     */ import android.view.View;
/*     */ import android.view.ViewGroup.LayoutParams;
/*     */ import com.google.vrtoolkit.cardboard.sensors.NfcSensor;
/*     */ import com.google.vrtoolkit.cardboard.sensors.SensorConnection;
/*     */ import com.google.vrtoolkit.cardboard.sensors.SensorConnection.SensorListener;
/*     */ 
/*     */ public class CardboardActivity extends Activity
/*     */   implements SensorConnection.SensorListener, VolumeKeyState.Handler
/*     */ {
/*  39 */   private final SensorConnection sensorConnection = new SensorConnection(this);
/*  40 */   private final VolumeKeyState volumeKeyState = new VolumeKeyState(this);
/*  41 */   private final FullscreenMode fullscreenMode = new FullscreenMode(this);
/*     */   private CardboardView cardboardView;
/*     */ 
/*     */   public void setCardboardView(CardboardView cardboardView)
/*     */   {
/*  57 */     this.cardboardView = cardboardView;
/*     */ 
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
/*  77 */     return this.cardboardView;
/*     */   }
/*     */ 
/*     */   public NfcSensor getNfcSensor()
/*     */   {
/*  91 */     return this.sensorConnection.getNfcSensor();
/*     */   }
/*     */ 
/*     */   public void setVolumeKeysMode(int mode)
/*     */   {
/* 104 */     this.volumeKeyState.setVolumeKeysMode(mode);
/*     */   }
/*     */ 
/*     */   public int getVolumeKeysMode()
/*     */   {
/* 114 */     return this.volumeKeyState.getVolumeKeysMode();
/*     */   }
/*     */ 
/*     */   public boolean areVolumeKeysDisabled()
/*     */   {
/* 128 */     return this.volumeKeyState.areVolumeKeysDisabled(this.sensorConnection.getNfcSensor());
/*     */   }
/*     */ 
/*     */   public void onInsertedIntoCardboard(CardboardDeviceParams cardboardDeviceParams)
/*     */   {
/* 140 */     updateCardboardDeviceParams(cardboardDeviceParams);
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
/* 171 */     if (this.cardboardView != null)
/* 172 */       this.cardboardView.updateCardboardDeviceParams(newParams);
/*     */   }
/*     */ 
/*     */   protected void onCreate(Bundle savedInstanceState)
/*     */   {
/* 179 */     super.onCreate(savedInstanceState);
/*     */ 
/* 181 */     requestWindowFeature(1);
/* 182 */     this.fullscreenMode.startFullscreenMode();
/* 183 */     this.sensorConnection.onCreate(this);
/* 184 */     this.volumeKeyState.onCreate();
/*     */   }
/*     */ 
/*     */   protected void onResume()
/*     */   {
/* 190 */     super.onResume();
/*     */ 
/* 192 */     if (this.cardboardView != null) {
/* 193 */       this.cardboardView.onResume();
/*     */     }
/*     */ 
/* 196 */     this.sensorConnection.onResume(this);
/* 197 */     this.fullscreenMode.setFullscreenMode();
/*     */   }
/*     */ 
/*     */   protected void onPause()
/*     */   {
/* 203 */     super.onPause();
/* 204 */     if (this.cardboardView != null) {
/* 205 */       this.cardboardView.onPause();
/*     */     }
/*     */ 
/* 208 */     this.sensorConnection.onPause(this);
/*     */   }
/*     */ 
/*     */   protected void onDestroy()
/*     */   {
/* 214 */     this.sensorConnection.onDestroy(this);
/*     */ 
/* 216 */     super.onDestroy();
/*     */   }
/*     */ 
/*     */   public void setContentView(View view)
/*     */   {
/* 222 */     if ((view instanceof CardboardView)) {
/* 223 */       setCardboardView((CardboardView)view);
/*     */     }
/*     */ 
/* 226 */     super.setContentView(view);
/*     */   }
/*     */ 
/*     */   public void setContentView(View view, ViewGroup.LayoutParams params)
/*     */   {
/* 232 */     if ((view instanceof CardboardView)) {
/* 233 */       setCardboardView((CardboardView)view);
/*     */     }
/*     */ 
/* 236 */     super.setContentView(view, params);
/*     */   }
/*     */ 
/*     */   public boolean onKeyDown(int keyCode, KeyEvent event)
/*     */   {
/* 243 */     return (this.volumeKeyState.onKey(keyCode)) || (super.onKeyDown(keyCode, event));
/*     */   }
/*     */ 
/*     */   public boolean onKeyUp(int keyCode, KeyEvent event)
/*     */   {
/* 250 */     return (this.volumeKeyState.onKey(keyCode)) || (super.onKeyUp(keyCode, event));
/*     */   }
/*     */ 
/*     */   public void onWindowFocusChanged(boolean hasFocus)
/*     */   {
/* 256 */     super.onWindowFocusChanged(hasFocus);
/* 257 */     this.fullscreenMode.onWindowFocusChanged(hasFocus);
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.CardboardActivity
 * JD-Core Version:    0.6.2
 */