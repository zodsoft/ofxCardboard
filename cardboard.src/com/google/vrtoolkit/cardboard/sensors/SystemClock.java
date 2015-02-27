/*    */ package com.google.vrtoolkit.cardboard.sensors;
/*    */ 
/*    */ public class SystemClock
/*    */   implements Clock
/*    */ {
/*    */   public long nanoTime()
/*    */   {
/* 27 */     return System.nanoTime();
/*    */   }
/*    */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.sensors.SystemClock
 * JD-Core Version:    0.6.2
 */