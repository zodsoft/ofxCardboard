/*    */ package com.google.vrtoolkit.cardboard;
/*    */ 
/*    */ import com.google.vrtoolkit.cardboard.sensors.MagnetSensor.OnCardboardTriggerListener;
/*    */ import com.google.vrtoolkit.cardboard.sensors.NfcSensor.OnCardboardNfcListener;
/*    */ 
/*    */ class CardboardActivity$SensorListener
/*    */   implements MagnetSensor.OnCardboardTriggerListener, NfcSensor.OnCardboardNfcListener
/*    */ {
/*    */   private CardboardActivity$SensorListener(CardboardActivity paramCardboardActivity)
/*    */   {
/*    */   }
/*    */ 
/*    */   public void onInsertedIntoCardboard(CardboardDeviceParams deviceParams)
/*    */   {
/* 74 */     this.this$0.onInsertedIntoCardboard(deviceParams);
/*    */   }
/*    */ 
/*    */   public void onRemovedFromCardboard()
/*    */   {
/* 79 */     this.this$0.onRemovedFromCardboard();
/*    */   }
/*    */ 
/*    */   public void onCardboardTrigger()
/*    */   {
/* 84 */     this.this$0.onCardboardTrigger();
/*    */   }
/*    */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.CardboardActivity.SensorListener
 * JD-Core Version:    0.6.2
 */