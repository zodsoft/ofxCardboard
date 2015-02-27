/*     */ package com.google.vrtoolkit.cardboard;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.app.AlertDialog.Builder;
/*     */ import android.app.Dialog;
/*     */ import android.app.DialogFragment;
/*     */ import android.app.FragmentManager;
/*     */ import android.content.ActivityNotFoundException;
/*     */ import android.content.Context;
/*     */ import android.content.DialogInterface;
/*     */ import android.content.DialogInterface.OnClickListener;
/*     */ import android.content.Intent;
/*     */ import android.content.pm.ActivityInfo;
/*     */ import android.content.pm.PackageManager;
/*     */ import android.content.pm.ResolveInfo;
/*     */ import android.net.Uri;
/*     */ import android.os.Bundle;
/*     */ import android.widget.Toast;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ class UiUtils
/*     */ {
/*     */   private static final String CARDBOARD_WEBSITE = "http://google.com/cardboard/cfg?vrtoolkit_version=0.5.1";
/*     */   private static final String CARDBOARD_CONFIGURE_ACTION = "com.google.vrtoolkit.cardboard.CONFIGURE";
/*     */   private static final String INTENT_EXTRAS_VERSION_KEY = "VERSION";
/*     */   private static final String NO_BROWSER_TEXT = "No browser to open website.";
/*     */ 
/*     */   static void launchOrInstallCardboard(Context context)
/*     */   {
/*  32 */     PackageManager pm = context.getPackageManager();
/*  33 */     Intent settingsIntent = new Intent();
/*  34 */     settingsIntent.setAction("com.google.vrtoolkit.cardboard.CONFIGURE");
/*  35 */     settingsIntent.putExtra("VERSION", "0.5.1");
/*     */ 
/*  38 */     List resolveInfos = pm.queryIntentActivities(settingsIntent, 0);
/*  39 */     List intentsToGoogleCardboard = new ArrayList();
/*  40 */     for (ResolveInfo info : resolveInfos) {
/*  41 */       String pkgName = info.activityInfo.packageName;
/*  42 */       if (pkgName.startsWith("com.google.")) {
/*  43 */         Intent intent = new Intent(settingsIntent);
/*  44 */         intent.setClassName(pkgName, info.activityInfo.name);
/*  45 */         intentsToGoogleCardboard.add(intent);
/*     */       }
/*     */     }
/*     */ 
/*  49 */     if (intentsToGoogleCardboard.isEmpty())
/*     */     {
/*  52 */       showInstallDialog(context);
/*  53 */     } else if (intentsToGoogleCardboard.size() == 1)
/*     */     {
/*  56 */       showConfigureDialog(context, (Intent)intentsToGoogleCardboard.get(0));
/*     */     }
/*     */     else
/*     */     {
/*  61 */       showConfigureDialog(context, settingsIntent);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void showInstallDialog(Context context) {
/*  66 */     DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
/*     */       public void onClick(DialogInterface dialog, int id) {
/*     */         try {
/*  69 */           this.val$context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://google.com/cardboard/cfg?vrtoolkit_version=0.5.1")));
/*     */         }
/*     */         catch (ActivityNotFoundException e) {
/*  72 */           Toast.makeText(this.val$context.getApplicationContext(), "No browser to open website.", 1).show();
/*     */         }
/*     */       }
/*     */     };
/*  77 */     FragmentManager fragmentManager = ((Activity)context).getFragmentManager();
/*  78 */     DialogFragment dialog = new SettingsDialogFragment(new InstallDialogStrings(), listener, null);
/*  79 */     dialog.show(fragmentManager, "InstallCardboardDialog");
/*     */   }
/*     */ 
/*     */   private static void showConfigureDialog(Context context, final Intent intent) {
/*  83 */     DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
/*     */       public void onClick(DialogInterface dialog, int id) {
/*     */         try {
/*  86 */           this.val$context.startActivity(intent);
/*     */         }
/*     */         catch (ActivityNotFoundException e)
/*     */         {
/*  90 */           UiUtils.showInstallDialog(this.val$context);
/*     */         }
/*     */       }
/*     */     };
/*  94 */     FragmentManager fragmentManager = ((Activity)context).getFragmentManager();
/*  95 */     DialogFragment dialog = new SettingsDialogFragment(new ConfigureDialogStrings(), listener, null);
/*  96 */     dialog.show(fragmentManager, "ConfigureCardboardDialog");
/*     */   }
/*     */ 
/*     */   private static class SettingsDialogFragment extends DialogFragment
/*     */   {
/*     */     private UiUtils.DialogStrings mDialogStrings;
/*     */     private DialogInterface.OnClickListener mPositiveButtonListener;
/*     */ 
/*     */     private SettingsDialogFragment(UiUtils.DialogStrings dialogStrings, DialogInterface.OnClickListener positiveButtonListener)
/*     */     {
/* 133 */       this.mDialogStrings = dialogStrings;
/* 134 */       this.mPositiveButtonListener = positiveButtonListener;
/*     */     }
/*     */ 
/*     */     public Dialog onCreateDialog(Bundle savedInstanceState)
/*     */     {
/* 139 */       AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
/* 140 */       builder.setTitle(this.mDialogStrings.mTitle).setMessage(this.mDialogStrings.mMessage).setPositiveButton(this.mDialogStrings.mPositiveButtonText, this.mPositiveButtonListener).setNegativeButton(this.mDialogStrings.mNegativeButtonText, null);
/*     */ 
/* 144 */       return builder.create();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class ConfigureDialogStrings extends UiUtils.DialogStrings
/*     */   {
/*     */     ConfigureDialogStrings()
/*     */     {
/* 116 */       super();
/* 117 */       this.mTitle = "Configure";
/* 118 */       this.mMessage = "Set up your viewer for the best experience.";
/* 119 */       this.mPositiveButtonText = "Setup";
/* 120 */       this.mNegativeButtonText = "Cancel";
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class InstallDialogStrings extends UiUtils.DialogStrings
/*     */   {
/*     */     InstallDialogStrings()
/*     */     {
/* 107 */       super();
/* 108 */       this.mTitle = "Configure";
/* 109 */       this.mMessage = "Get the Cardboard app in order to configure your viewer.";
/* 110 */       this.mPositiveButtonText = "Go to Play Store";
/* 111 */       this.mNegativeButtonText = "Cancel";
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class DialogStrings
/*     */   {
/*     */     String mTitle;
/*     */     String mMessage;
/*     */     String mPositiveButtonText;
/*     */     String mNegativeButtonText;
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.UiUtils
 * JD-Core Version:    0.6.2
 */