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
/*     */   private static final String CARDBOARD_WEBSITE = "http://google.com/cardboard/cfg?vrtoolkit_version=0.5.2";
/*     */   private static final String CARDBOARD_CONFIGURE_ACTION = "com.google.vrtoolkit.cardboard.CONFIGURE";
/*     */   private static final String INTENT_EXTRAS_VERSION_KEY = "VERSION";
/*     */   private static final String NO_BROWSER_TEXT = "No browser to open website.";
/*     */   private static final String INTENT_KEY = "intent";
/*     */ 
/*     */   static void launchOrInstallCardboard(Context context)
/*     */   {
/*  34 */     PackageManager pm = context.getPackageManager();
/*  35 */     Intent settingsIntent = new Intent();
/*  36 */     settingsIntent.setAction("com.google.vrtoolkit.cardboard.CONFIGURE");
/*  37 */     settingsIntent.putExtra("VERSION", "0.5.2");
/*     */ 
/*  40 */     List resolveInfos = pm.queryIntentActivities(settingsIntent, 0);
/*  41 */     List intentsToGoogleCardboard = new ArrayList();
/*  42 */     for (ResolveInfo info : resolveInfos) {
/*  43 */       String pkgName = info.activityInfo.packageName;
/*  44 */       if (pkgName.startsWith("com.google.")) {
/*  45 */         Intent intent = new Intent(settingsIntent);
/*  46 */         intent.setClassName(pkgName, info.activityInfo.name);
/*  47 */         intentsToGoogleCardboard.add(intent);
/*     */       }
/*     */     }
/*     */ 
/*  51 */     if (intentsToGoogleCardboard.isEmpty())
/*     */     {
/*  54 */       showInstallDialog(context);
/*  55 */     } else if (intentsToGoogleCardboard.size() == 1)
/*     */     {
/*  58 */       showConfigureDialog(context, (Intent)intentsToGoogleCardboard.get(0));
/*     */     }
/*     */     else
/*     */     {
/*  63 */       showConfigureDialog(context, settingsIntent);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void showInstallDialog(Context context) {
/*  68 */     FragmentManager fragmentManager = ((Activity)context).getFragmentManager();
/*  69 */     new InstallSettingsDialogFragment().show(fragmentManager, "InstallCardboardDialog");
/*     */   }
/*     */ 
/*     */   private static void showConfigureDialog(Context context, Intent intent) {
/*  73 */     FragmentManager fragmentManager = ((Activity)context).getFragmentManager();
/*  74 */     DialogFragment dialog = new ConfigureSettingsDialogFragment();
/*  75 */     Bundle bundle = new Bundle();
/*  76 */     bundle.putParcelable("intent", intent);
/*  77 */     dialog.setArguments(bundle);
/*  78 */     dialog.show(fragmentManager, "ConfigureCardboardDialog");
/*     */   }
/*     */ 
/*     */   public static class ConfigureSettingsDialogFragment extends DialogFragment
/*     */   {
/*     */     private static final String TITLE = "Configure";
/*     */     private static final String MESSAGE = "Set up your viewer for the best experience.";
/*     */     private static final String POSITIVE_BUTTON_TEXT = "Setup";
/*     */     private static final String NEGATIVE_BUTTON_TEXT = "Cancel";
/*     */     private Intent intent;
/* 140 */     private final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
/*     */     {
/*     */       public void onClick(DialogInterface dialog, int id) {
/*     */         try {
/* 144 */           UiUtils.ConfigureSettingsDialogFragment.this.getActivity().startActivity(UiUtils.ConfigureSettingsDialogFragment.this.intent);
/*     */         }
/*     */         catch (ActivityNotFoundException e)
/*     */         {
/* 148 */           UiUtils.showInstallDialog(UiUtils.ConfigureSettingsDialogFragment.this.getActivity());
/*     */         }
/*     */       }
/* 140 */     };
/*     */ 
/*     */     public void onCreate(Bundle savedInstanceState)
/*     */     {
/* 155 */       super.onCreate(savedInstanceState);
/* 156 */       this.intent = ((Intent)getArguments().getParcelable("intent"));
/*     */     }
/*     */ 
/*     */     public Dialog onCreateDialog(Bundle savedInstanceState)
/*     */     {
/* 161 */       AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
/* 162 */       builder.setTitle("Configure").setMessage("Set up your viewer for the best experience.").setPositiveButton("Setup", this.listener).setNegativeButton("Cancel", null);
/*     */ 
/* 165 */       return builder.create();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class InstallSettingsDialogFragment extends DialogFragment
/*     */   {
/*     */     private static final String TITLE = "Configure";
/*     */     private static final String MESSAGE = "Get the Cardboard app in order to configure your viewer.";
/*     */     private static final String POSITIVE_BUTTON_TEXT = "Go to Play Store";
/*     */     private static final String NEGATIVE_BUTTON_TEXT = "Cancel";
/*  98 */     private final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
/*     */     {
/*     */       public void onClick(DialogInterface dialog, int id) {
/*     */         try {
/* 102 */           UiUtils.InstallSettingsDialogFragment.this.getActivity().startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://google.com/cardboard/cfg?vrtoolkit_version=0.5.2")));
/*     */         }
/*     */         catch (ActivityNotFoundException e) {
/* 105 */           Toast.makeText(UiUtils.InstallSettingsDialogFragment.this.getActivity().getApplicationContext(), "No browser to open website.", 1).show();
/*     */         }
/*     */       }
/*  98 */     };
/*     */ 
/*     */     public Dialog onCreateDialog(Bundle savedInstanceState)
/*     */     {
/* 113 */       AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
/* 114 */       builder.setTitle("Configure").setMessage("Get the Cardboard app in order to configure your viewer.").setPositiveButton("Go to Play Store", this.listener).setNegativeButton("Cancel", null);
/*     */ 
/* 117 */       return builder.create();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.UiUtils
 * JD-Core Version:    0.6.2
 */