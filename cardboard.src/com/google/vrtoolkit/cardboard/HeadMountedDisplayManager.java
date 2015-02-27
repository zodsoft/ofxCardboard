/*     */ package com.google.vrtoolkit.cardboard;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.util.Log;
/*     */ import android.view.Display;
/*     */ import android.view.WindowManager;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public class HeadMountedDisplayManager
/*     */ {
/*     */   private static final String TAG = "HeadMountedDisplayManager";
/*     */   private final HeadMountedDisplay mHmd;
/*     */   private final Context mContext;
/*     */ 
/*     */   public HeadMountedDisplayManager(Context context)
/*     */   {
/*  53 */     this.mContext = context;
/*  54 */     this.mHmd = new HeadMountedDisplay(createScreenParams(), createCardboardDeviceParams());
/*     */   }
/*     */ 
/*     */   public HeadMountedDisplay getHeadMountedDisplay()
/*     */   {
/*  69 */     return this.mHmd;
/*     */   }
/*     */ 
/*     */   public void onResume()
/*     */   {
/*  86 */     CardboardDeviceParams deviceParams = createCardboardDeviceParamsFromExternalStorage();
/*  87 */     if ((deviceParams != null) && (!deviceParams.equals(this.mHmd.getCardboardDeviceParams()))) {
/*  88 */       this.mHmd.setCardboardDeviceParams(deviceParams);
/*  89 */       Log.i("HeadMountedDisplayManager", "Successfully read updated device params from external storage");
/*     */     }
/*  91 */     ScreenParams screenParams = createScreenParamsFromExternalStorage(getDisplay());
/*  92 */     if ((screenParams != null) && (!screenParams.equals(this.mHmd.getScreenParams()))) {
/*  93 */       this.mHmd.setScreenParams(screenParams);
/*  94 */       Log.i("HeadMountedDisplayManager", "Successfully read updated screen params from external storage");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void onPause()
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean updateCardboardDeviceParams(CardboardDeviceParams cardboardDeviceParams)
/*     */   {
/* 126 */     if ((cardboardDeviceParams == null) || (cardboardDeviceParams.equals(this.mHmd.getCardboardDeviceParams())))
/*     */     {
/* 128 */       return false;
/*     */     }
/*     */ 
/* 132 */     this.mHmd.setCardboardDeviceParams(cardboardDeviceParams);
/* 133 */     writeCardboardParamsToExternalStorage();
/* 134 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean updateScreenParams(ScreenParams screenParams)
/*     */   {
/* 144 */     if ((screenParams == null) || (screenParams.equals(this.mHmd.getScreenParams()))) {
/* 145 */       return false;
/*     */     }
/* 147 */     this.mHmd.setScreenParams(screenParams);
/* 148 */     return true;
/*     */   }
/*     */ 
/*     */   private void writeCardboardParamsToExternalStorage()
/*     */   {
/* 153 */     boolean success = false;
/* 154 */     OutputStream stream = null;
/*     */     try {
/* 156 */       stream = new BufferedOutputStream(new FileOutputStream(ConfigUtils.getConfigFile("current_device_params")));
/*     */ 
/* 158 */       success = this.mHmd.getCardboardDeviceParams().writeToOutputStream(stream);
/*     */     } catch (FileNotFoundException e) {
/*     */     }
/*     */     finally {
/* 162 */       if (stream != null)
/*     */         try {
/* 164 */           stream.close();
/*     */         }
/*     */         catch (IOException e)
/*     */         {
/*     */         }
/*     */     }
/* 170 */     if (!success)
/* 171 */       Log.e("HeadMountedDisplayManager", "Could not write Cardboard parameters to external storage.");
/*     */     else
/* 173 */       Log.i("HeadMountedDisplayManager", "Successfully wrote Cardboard parameters to external storage.");
/*     */   }
/*     */ 
/*     */   private Display getDisplay()
/*     */   {
/* 178 */     WindowManager windowManager = (WindowManager)this.mContext.getSystemService("window");
/*     */ 
/* 180 */     return windowManager.getDefaultDisplay();
/*     */   }
/*     */ 
/*     */   private ScreenParams createScreenParams()
/*     */   {
/* 185 */     Display display = getDisplay();
/* 186 */     ScreenParams params = createScreenParamsFromExternalStorage(display);
/* 187 */     if (params != null) {
/* 188 */       Log.i("HeadMountedDisplayManager", "Successfully read screen params from external storage");
/* 189 */       return params;
/*     */     }
/* 191 */     return new ScreenParams(display);
/*     */   }
/*     */ 
/*     */   private CardboardDeviceParams createCardboardDeviceParams()
/*     */   {
/* 198 */     CardboardDeviceParams params = createCardboardDeviceParamsFromExternalStorage();
/* 199 */     if (params != null) {
/* 200 */       Log.i("HeadMountedDisplayManager", "Successfully read device params from external storage");
/* 201 */       return params;
/*     */     }
/*     */ 
/* 206 */     params = createCardboardDeviceParamsFromAssetFolder();
/* 207 */     if (params != null) {
/* 208 */       Log.i("HeadMountedDisplayManager", "Successfully read device params from asset folder");
/* 209 */       writeCardboardParamsToExternalStorage();
/* 210 */       return params;
/*     */     }
/*     */ 
/* 214 */     return new CardboardDeviceParams();
/*     */   }
/*     */ 
/*     */   private CardboardDeviceParams createCardboardDeviceParamsFromAssetFolder()
/*     */   {
/*     */     try {
/* 220 */       InputStream stream = null;
/*     */       try {
/* 222 */         stream = new BufferedInputStream(ConfigUtils.openAssetConfigFile(this.mContext.getAssets(), "current_device_params"));
/*     */ 
/* 224 */         return CardboardDeviceParams.createFromInputStream(stream);
/*     */       } finally {
/* 226 */         if (stream != null)
/* 227 */           stream.close();
/*     */       }
/*     */     }
/*     */     catch (FileNotFoundException e) {
/* 231 */       localObject1 = String.valueOf(String.valueOf(e)); Log.d("HeadMountedDisplayManager", 47 + ((String)localObject1).length() + "Bundled Cardboard device parameters not found: " + (String)localObject1);
/*     */     } catch (IOException e) {
/* 233 */       Object localObject1 = String.valueOf(String.valueOf(e)); Log.e("HeadMountedDisplayManager", 43 + ((String)localObject1).length() + "Error reading config file in asset folder: " + (String)localObject1);
/*     */     }
/* 235 */     return null;
/*     */   }
/*     */ 
/*     */   private CardboardDeviceParams createCardboardDeviceParamsFromExternalStorage()
/*     */   {
/*     */     try {
/* 241 */       InputStream stream = null;
/*     */       try {
/* 243 */         stream = new BufferedInputStream(new FileInputStream(ConfigUtils.getConfigFile("current_device_params")));
/*     */ 
/* 245 */         return CardboardDeviceParams.createFromInputStream(stream);
/*     */       } finally {
/* 247 */         if (stream != null)
/*     */           try {
/* 249 */             stream.close();
/*     */           }
/*     */           catch (IOException e) {
/*     */           }
/*     */       }
/*     */     }
/*     */     catch (FileNotFoundException e) {
/* 256 */       Object localObject1 = String.valueOf(String.valueOf(e)); Log.d("HeadMountedDisplayManager", 44 + ((String)localObject1).length() + "Cardboard device parameters file not found: " + (String)localObject1);
/* 257 */     }return null;
/*     */   }
/*     */ 
/*     */   private ScreenParams createScreenParamsFromExternalStorage(Display display)
/*     */   {
/*     */     try
/*     */     {
/* 264 */       InputStream stream = null;
/*     */       try {
/* 266 */         stream = new BufferedInputStream(new FileInputStream(ConfigUtils.getConfigFile("phone_params")));
/*     */ 
/* 268 */         return ScreenParams.createFromInputStream(display, stream);
/*     */       } finally {
/* 270 */         if (stream != null)
/*     */           try {
/* 272 */             stream.close();
/*     */           }
/*     */           catch (IOException e) {
/*     */           }
/*     */       }
/*     */     }
/*     */     catch (FileNotFoundException e) {
/* 279 */       Object localObject1 = String.valueOf(String.valueOf(e)); Log.d("HeadMountedDisplayManager", 44 + ((String)localObject1).length() + "Cardboard screen parameters file not found: " + (String)localObject1);
/* 280 */     }return null;
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.HeadMountedDisplayManager
 * JD-Core Version:    0.6.2
 */