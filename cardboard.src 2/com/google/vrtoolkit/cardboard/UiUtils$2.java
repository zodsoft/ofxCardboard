/*    */ package com.google.vrtoolkit.cardboard;
/*    */ 
/*    */ import android.content.ActivityNotFoundException;
/*    */ import android.content.Context;
/*    */ import android.content.DialogInterface;
/*    */ import android.content.DialogInterface.OnClickListener;
/*    */ import android.content.Intent;
/*    */ 
/*    */ final class UiUtils$2
/*    */   implements DialogInterface.OnClickListener
/*    */ {
/*    */   UiUtils$2(Context paramContext, Intent paramIntent)
/*    */   {
/*    */   }
/*    */ 
/*    */   public void onClick(DialogInterface dialog, int id)
/*    */   {
/*    */     try
/*    */     {
/* 86 */       this.val$context.startActivity(this.val$intent);
/*    */     }
/*    */     catch (ActivityNotFoundException e)
/*    */     {
/* 90 */       UiUtils.access$100(this.val$context);
/*    */     }
/*    */   }
/*    */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.UiUtils.2
 * JD-Core Version:    0.6.2
 */