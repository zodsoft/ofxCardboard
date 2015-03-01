/*     */ package com.google.vrtoolkit.cardboard;
/*     */ 
/*     */ import android.os.Handler;
/*     */ import android.view.View.OnSystemUiVisibilityChangeListener;
/*     */ 
/*     */ class CardboardActivity$1
/*     */   implements View.OnSystemUiVisibilityChangeListener
/*     */ {
/*     */   CardboardActivity$1(CardboardActivity paramCardboardActivity, Handler paramHandler)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void onSystemUiVisibilityChange(int visibility)
/*     */   {
/* 271 */     if ((visibility & 0x2) == 0)
/* 272 */       this.val$handler.postDelayed(new Runnable()
/*     */       {
/*     */         public void run() {
/* 275 */           CardboardActivity.access$100(CardboardActivity.1.this.this$0);
/*     */         }
/*     */       }
/*     */       , 2000L);
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.CardboardActivity.1
 * JD-Core Version:    0.6.2
 */