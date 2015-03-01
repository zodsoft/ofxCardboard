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
/*     */   private final HeadMountedDisplay hmd;
/*     */   private final Context context;
/*     */ 
/*     */   public HeadMountedDisplayManager(Context context)
/*     */   {
/*  53 */     this.context = context;
/*  54 */     this.hmd = new HeadMountedDisplay(createScreenParams(), createCardboardDeviceParams());
/*     */   }
/*     */ 
/*     */   public HeadMountedDisplay getHeadMountedDisplay()
/*     */   {
/*  69 */     return this.hmd;
/*     */   }
/*     */ 
/*     */   public void onResume()
/*     */   {
/*  86 */     CardboardDeviceParams deviceParams = createCardboardDeviceParamsFromExternalStorage();
/*  87 */     if ((deviceParams != null) && (!deviceParams.equals(this.hmd.getCardboardDeviceParams()))) {
/*  88 */       this.hmd.setCardboardDeviceParams(deviceParams);
/*  89 */       Log.i("HeadMountedDisplayManager", "Successfully read updated device params from external storage");
/*     */     }
/*  91 */     ScreenParams screenParams = createScreenParamsFromExternalStorage(getDisplay());
/*  92 */     if ((screenParams != null) && (!screenParams.equals(this.hmd.getScreenParams()))) {
/*  93 */       this.hmd.setScreenParams(screenParams);
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
/* 126 */     if ((cardboardDeviceParams == null) || (cardboardDeviceParams.equals(this.hmd.getCardboardDeviceParams())))
/*     */     {
/* 128 */       return false;
/*     */     }
/*     */ 
/* 132 */     this.hmd.setCardboardDeviceParams(cardboardDeviceParams);
/* 133 */     writeCardboardParamsToExternalStorage();
/* 134 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean updateScreenParams(ScreenParams screenParams)
/*     */   {
/* 144 */     if ((screenParams == null) || (screenParams.equals(this.hmd.getScreenParams()))) {
/* 145 */       return false;
/*     */     }
/* 147 */     this.hmd.setScreenParams(screenParams);
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
/* 158 */       success = this.hmd.getCardboardDeviceParams().writeToOutputStream(stream);
/*     */     } catch (FileNotFoundException e) {
/* 160 */       str = String.valueOf(String.valueOf(e)); Log.e("HeadMountedDisplayManager", 37 + str.length() + "Unexpected file not found exception: " + str);
/*     */     } catch (IllegalStateException e) {
/* 162 */       String str = String.valueOf(String.valueOf(e)); Log.w("HeadMountedDisplayManager", 32 + str.length() + "Error writing phone parameters: " + str);
/*     */     } finally {
/* 164 */       if (stream != null)
/*     */         try {
/* 166 */           stream.close();
/*     */         }
/*     */         catch (IOException e)
/*     */         {
/*     */         }
/*     */     }
/* 172 */     if (!success)
/* 173 */       Log.e("HeadMountedDisplayManager", "Could not write Cardboard parameters to external storage.");
/*     */     else
/* 175 */       Log.i("HeadMountedDisplayManager", "Successfully wrote Cardboard parameters to external storage.");
/*     */   }
/*     */ 
/*     */   private Display getDisplay()
/*     */   {
/* 180 */     WindowManager windowManager = (WindowManager)this.context.getSystemService("window");
/*     */ 
/* 182 */     return windowManager.getDefaultDisplay();
/*     */   }
/*     */ 
/*     */   private ScreenParams createScreenParams()
/*     */   {
/* 187 */     Display display = getDisplay();
/* 188 */     ScreenParams params = createScreenParamsFromExternalStorage(display);
/* 189 */     if (params != null) {
/* 190 */       Log.i("HeadMountedDisplayManager", "Successfully read screen params from external storage");
/* 191 */       return params;
/*     */     }
/* 193 */     return new ScreenParams(display);
/*     */   }
/*     */ 
/*     */   private CardboardDeviceParams createCardboardDeviceParams()
/*     */   {
/* 200 */     CardboardDeviceParams params = createCardboardDeviceParamsFromExternalStorage();
/* 201 */     if (params != null) {
/* 202 */       Log.i("HeadMountedDisplayManager", "Successfully read device params from external storage");
/* 203 */       return params;
/*     */     }
/*     */ 
/* 208 */     params = createCardboardDeviceParamsFromAssetFolder();
/* 209 */     if (params != null) {
/* 210 */       Log.i("HeadMountedDisplayManager", "Successfully read device params from asset folder");
/* 211 */       writeCardboardParamsToExternalStorage();
/* 212 */       return params;
/*     */     }
/*     */ 
/* 216 */     return new CardboardDeviceParams();
/*     */   }
/*     */ 
/*     */   private CardboardDeviceParams createCardboardDeviceParamsFromAssetFolder()
/*     */   {
/*     */     try {
/* 222 */       InputStream stream = null;
/*     */       try {
/* 224 */         stream = new BufferedInputStream(ConfigUtils.openAssetConfigFile(this.context.getAssets(), "current_device_params"));
/*     */ 
/* 226 */         return CardboardDeviceParams.createFromInputStream(stream);
/*     */       } finally {
/* 228 */         if (stream != null)
/* 229 */           stream.close();
/*     */       }
/*     */     }
/*     */     catch (FileNotFoundException e) {
/* 233 */       localObject1 = String.valueOf(String.valueOf(e)); Log.d("HeadMountedDisplayManager", 47 + ((String)localObject1).length() + "Bundled Cardboard device parameters not found: " + (String)localObject1);
/*     */     } catch (IOException e) {
/* 235 */       Object localObject1 = String.valueOf(String.valueOf(e)); Log.e("HeadMountedDisplayManager", 43 + ((String)localObject1).length() + "Error reading config file in asset folder: " + (String)localObject1);
/*     */     }
/* 237 */     return null;
/*     */   }
/*     */ 
/*     */   private CardboardDeviceParams createCardboardDeviceParamsFromExternalStorage()
/*     */   {
/*     */     try {
/* 243 */       InputStream stream = null;
/*     */       try {
/* 245 */         stream = new BufferedInputStream(new FileInputStream(ConfigUtils.getConfigFile("current_device_params")));
/*     */ 
/* 247 */         return CardboardDeviceParams.createFromInputStream(stream);
/*     */       } finally {
/* 249 */         if (stream != null)
/*     */           try {
/* 251 */             stream.close();
/*     */           }
/*     */           catch (IOException e) {
/*     */           }
/*     */       }
/*     */     }
/*     */     catch (FileNotFoundException e) {
/* 258 */       localObject1 = String.valueOf(String.valueOf(e)); Log.d("HeadMountedDisplayManager", 44 + ((String)localObject1).length() + "Cardboard device parameters file not found: " + (String)localObject1);
/*     */     } catch (IllegalStateException e) {
/* 260 */       Object localObject1 = String.valueOf(String.valueOf(e)); Log.w("HeadMountedDisplayManager", 43 + ((String)localObject1).length() + "Error reading Cardboard device parameters: " + (String)localObject1);
/*     */     }
/* 262 */     return null;
/*     */   }
/*     */ 
/*     */   private ScreenParams createScreenParamsFromExternalStorage(Display display)
/*     */   {
/*     */     try {
/* 268 */       InputStream stream = null;
/*     */       try {
/* 270 */         stream = new BufferedInputStream(new FileInputStream(ConfigUtils.getConfigFile("phone_params")));
/*     */ 
/* 272 */         return ScreenParams.createFromInputStream(display, stream);
/*     */       } finally {
/* 274 */         if (stream != null)
/*     */           try {
/* 276 */             stream.close();
/*     */           }
/*     */           catch (IOException e) {
/*     */           }
/*     */       }
/*     */     }
/*     */     catch (FileNotFoundException e) {
/* 283 */       localObject1 = String.valueOf(String.valueOf(e)); Log.d("HeadMountedDisplayManager", 44 + ((String)localObject1).length() + "Cardboard screen parameters file not found: " + (String)localObject1);
/*     */     } catch (IllegalStateException e) {
/* 285 */       Object localObject1 = String.valueOf(String.valueOf(e)); Log.w("HeadMountedDisplayManager", 43 + ((String)localObject1).length() + "Error reading Cardboard screen parameters: " + (String)localObject1);
/*     */     }
/* 287 */     return null;
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.HeadMountedDisplayManager
 * JD-Core Version:    0.6.2
 */