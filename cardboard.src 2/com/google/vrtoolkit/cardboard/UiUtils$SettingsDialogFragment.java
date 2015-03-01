/*     */ package com.google.vrtoolkit.cardboard;
/*     */ 
/*     */ import android.app.AlertDialog.Builder;
/*     */ import android.app.Dialog;
/*     */ import android.app.DialogFragment;
/*     */ import android.content.DialogInterface.OnClickListener;
/*     */ import android.os.Bundle;
/*     */ 
/*     */ class UiUtils$SettingsDialogFragment extends DialogFragment
/*     */ {
/*     */   private UiUtils.DialogStrings mDialogStrings;
/*     */   private DialogInterface.OnClickListener mPositiveButtonListener;
/*     */ 
/*     */   private UiUtils$SettingsDialogFragment(UiUtils.DialogStrings dialogStrings, DialogInterface.OnClickListener positiveButtonListener)
/*     */   {
/* 133 */     this.mDialogStrings = dialogStrings;
/* 134 */     this.mPositiveButtonListener = positiveButtonListener;
/*     */   }
/*     */ 
/*     */   public Dialog onCreateDialog(Bundle savedInstanceState)
/*     */   {
/* 139 */     AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
/* 140 */     builder.setTitle(this.mDialogStrings.mTitle).setMessage(this.mDialogStrings.mMessage).setPositiveButton(this.mDialogStrings.mPositiveButtonText, this.mPositiveButtonListener).setNegativeButton(this.mDialogStrings.mNegativeButtonText, null);
/*     */ 
/* 144 */     return builder.create();
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.UiUtils.SettingsDialogFragment
 * JD-Core Version:    0.6.2
 */