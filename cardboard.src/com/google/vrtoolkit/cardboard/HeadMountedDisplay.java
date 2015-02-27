/*     */ package com.google.vrtoolkit.cardboard;
/*     */ 
/*     */ public class HeadMountedDisplay
/*     */ {
/*     */   private ScreenParams mScreen;
/*     */   private CardboardDeviceParams mCardboardDevice;
/*     */ 
/*     */   public HeadMountedDisplay(ScreenParams screenParams, CardboardDeviceParams cardboardDevice)
/*     */   {
/*  38 */     this.mScreen = screenParams;
/*  39 */     this.mCardboardDevice = cardboardDevice;
/*     */   }
/*     */ 
/*     */   public HeadMountedDisplay(HeadMountedDisplay hmd)
/*     */   {
/*  48 */     this.mScreen = new ScreenParams(hmd.mScreen);
/*  49 */     this.mCardboardDevice = new CardboardDeviceParams(hmd.mCardboardDevice);
/*     */   }
/*     */ 
/*     */   public void setScreenParams(ScreenParams screen)
/*     */   {
/*  58 */     this.mScreen = new ScreenParams(screen);
/*     */   }
/*     */ 
/*     */   public ScreenParams getScreenParams()
/*     */   {
/*  67 */     return this.mScreen;
/*     */   }
/*     */ 
/*     */   public void setCardboardDeviceParams(CardboardDeviceParams cardboardDeviceParams)
/*     */   {
/*  76 */     this.mCardboardDevice = new CardboardDeviceParams(cardboardDeviceParams);
/*     */   }
/*     */ 
/*     */   public CardboardDeviceParams getCardboardDeviceParams()
/*     */   {
/*  85 */     return this.mCardboardDevice;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/*  96 */     if (other == null) return false;
/*  97 */     if (other == this) return true;
/*  98 */     if (!(other instanceof HeadMountedDisplay)) return false;
/*  99 */     HeadMountedDisplay o = (HeadMountedDisplay)other;
/*     */ 
/* 101 */     return (this.mScreen.equals(o.mScreen)) && (this.mCardboardDevice.equals(o.mCardboardDevice));
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.HeadMountedDisplay
 * JD-Core Version:    0.6.2
 */