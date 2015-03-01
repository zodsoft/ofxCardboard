/*    */ package com.google.vrtoolkit.cardboard;
/*    */ 
/*    */ import android.content.ActivityNotFoundException;
/*    */ import android.content.Context;
/*    */ import android.content.DialogInterface;
/*    */ import android.content.DialogInterface.OnClickListener;
/*    */ import android.content.Intent;
/*    */ import android.net.Uri;
/*    */ import android.widget.Toast;
/*    */ 
/*    */ final class UiUtils$1
/*    */   implements DialogInterface.OnClickListener
/*    */ {
/*    */   UiUtils$1(Context paramContext)
/*    */   {
/*    */   }
/*    */ 
/*    */   public void onClick(DialogInterface dialog, int id)
/*    */   {
/*    */     try
/*    */     {
/* 69 */       this.val$context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://google.com/cardboard/cfg?vrtoolkit_version=0.5.1")));
/*    */     }
/*    */     catch (ActivityNotFoundException e) {
/* 72 */       Toast.makeText(this.val$context.getApplicationContext(), "No browser to open website.", 1).show();
/*    */     }
/*    */   }
/*    */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.UiUtils.1
 * JD-Core Version:    0.6.2
 */