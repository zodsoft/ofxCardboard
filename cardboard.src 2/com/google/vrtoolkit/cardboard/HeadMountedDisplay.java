/*     */ package com.google.vrtoolkit.cardboard;
/*     */ 
/*     */ public class HeadMountedDisplay
/*     */ {
/*     */   private ScreenParams screen;
/*     */   private CardboardDeviceParams cardboardDevice;
/*     */ 
/*     */   public HeadMountedDisplay(ScreenParams screenParams, CardboardDeviceParams cardboardDevice)
/*     */   {
/*  38 */     this.screen = screenParams;
/*  39 */     this.cardboardDevice = cardboardDevice;
/*     */   }
/*     */ 
/*     */   public HeadMountedDisplay(HeadMountedDisplay hmd)
/*     */   {
/*  48 */     this.screen = new ScreenParams(hmd.screen);
/*  49 */     this.cardboardDevice = new CardboardDeviceParams(hmd.cardboardDevice);
/*     */   }
/*     */ 
/*     */   public void setScreenParams(ScreenParams screen)
/*     */   {
/*  58 */     this.screen = new ScreenParams(screen);
/*     */   }
/*     */ 
/*     */   public ScreenParams getScreenParams()
/*     */   {
/*  67 */     return this.screen;
/*     */   }
/*     */ 
/*     */   public void setCardboardDeviceParams(CardboardDeviceParams cardboardDeviceParams)
/*     */   {
/*  76 */     this.cardboardDevice = new CardboardDeviceParams(cardboardDeviceParams);
/*     */   }
/*     */ 
/*     */   public CardboardDeviceParams getCardboardDeviceParams()
/*     */   {
/*  85 */     return this.cardboardDevice;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/*  96 */     if (other == null) {
/*  97 */       return false;
/*     */     }
/*  99 */     if (other == this) {
/* 100 */       return true;
/*     */     }
/* 102 */     if (!(other instanceof HeadMountedDisplay)) {
/* 103 */       return false;
/*     */     }
/* 105 */     HeadMountedDisplay o = (HeadMountedDisplay)other;
/*     */ 
/* 107 */     return (this.screen.equals(o.screen)) && (this.cardboardDevice.equals(o.cardboardDevice));
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.HeadMountedDisplay
 * JD-Core Version:    0.6.2
 */