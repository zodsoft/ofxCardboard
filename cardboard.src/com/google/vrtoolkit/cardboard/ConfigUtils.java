/*    */ package com.google.vrtoolkit.cardboard;
/*    */ 
/*    */ import android.content.res.AssetManager;
/*    */ import android.os.Environment;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ public abstract class ConfigUtils
/*    */ {
/*    */   public static final String CARDBOARD_CONFIG_FOLDER = "Cardboard";
/*    */   public static final String CARDBOARD_DEVICE_PARAMS_FILE = "current_device_params";
/*    */   public static final String CARDBOARD_PHONE_PARAMS_FILE = "phone_params";
/*    */ 
/*    */   public static File getConfigFile(String filename)
/*    */   {
/* 52 */     File configFolder = new File(Environment.getExternalStorageDirectory(), "Cardboard");
/*    */ 
/* 55 */     if (!configFolder.exists()) {
/* 56 */       configFolder.mkdirs();
/* 57 */     } else if (!configFolder.isDirectory()) {
/* 58 */       String str = String.valueOf(String.valueOf(configFolder)); throw new IllegalStateException(22 + str.length() + "Folder " + str + " already exists");
/*    */     }
/*    */ 
/* 61 */     return new File(configFolder, filename);
/*    */   }
/*    */ 
/*    */   public static InputStream openAssetConfigFile(AssetManager assetManager, String filename)
/*    */     throws IOException
/*    */   {
/* 74 */     String assetPath = new File("Cardboard", filename).getPath();
/* 75 */     return assetManager.open(assetPath);
/*    */   }
/*    */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.ConfigUtils
 * JD-Core Version:    0.6.2
 */