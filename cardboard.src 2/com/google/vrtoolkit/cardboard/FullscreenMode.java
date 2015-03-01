/*    */ package com.google.vrtoolkit.cardboard;
/*    */ 
/*    */ import android.app.Activity;
/*    */ import android.os.Build.VERSION;
/*    */ import android.os.Handler;
/*    */ import android.view.View;
/*    */ import android.view.View.OnSystemUiVisibilityChangeListener;
/*    */ import android.view.Window;
/*    */ 
/*    */ class FullscreenMode
/*    */ {
/*    */   static final int NAVIGATION_BAR_TIMEOUT_MS = 2000;
/*    */   Activity activity;
/*    */ 
/*    */   FullscreenMode(Activity activity)
/*    */   {
/* 21 */     this.activity = activity;
/*    */   }
/*    */ 
/*    */   void startFullscreenMode()
/*    */   {
/* 29 */     this.activity.getWindow().addFlags(128);
/*    */ 
/* 32 */     if (Build.VERSION.SDK_INT < 19) {
/* 33 */       final Handler handler = new Handler();
/* 34 */       this.activity.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
/*    */       {
/*    */         public void onSystemUiVisibilityChange(int visibility)
/*    */         {
/* 38 */           if ((visibility & 0x2) == 0)
/* 39 */             handler.postDelayed(new Runnable()
/*    */             {
/*    */               public void run() {
/* 42 */                 FullscreenMode.this.setFullscreenMode();
/*    */               }
/*    */             }
/*    */             , 2000L);
/*    */         }
/*    */       });
/*    */     }
/*    */   }
/*    */ 
/*    */   void onWindowFocusChanged(boolean hasFocus)
/*    */   {
/* 56 */     if (hasFocus)
/* 57 */       setFullscreenMode();
/*    */   }
/*    */ 
/*    */   void setFullscreenMode()
/*    */   {
/* 63 */     this.activity.getWindow().getDecorView().setSystemUiVisibility(5894);
/*    */   }
/*    */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.FullscreenMode
 * JD-Core Version:    0.6.2
 */