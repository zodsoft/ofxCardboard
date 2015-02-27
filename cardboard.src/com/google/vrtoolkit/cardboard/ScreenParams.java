/*     */ package com.google.vrtoolkit.cardboard;
/*     */ 
/*     */ import android.util.DisplayMetrics;
/*     */ import android.view.Display;
/*     */ import com.google.vrtoolkit.cardboard.proto.Phone.PhoneParams;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public class ScreenParams
/*     */ {
/*     */   private static final float METERS_PER_INCH = 0.0254F;
/*     */   private static final float DEFAULT_BORDER_SIZE_METERS = 0.003F;
/*     */   private int mWidth;
/*     */   private int mHeight;
/*     */   private float mXMetersPerPixel;
/*     */   private float mYMetersPerPixel;
/*     */   private float mBorderSizeMeters;
/*     */ 
/*     */   public ScreenParams(Display display)
/*     */   {
/*  46 */     DisplayMetrics metrics = new DisplayMetrics();
/*     */     try
/*     */     {
/*  50 */       display.getRealMetrics(metrics);
/*     */     } catch (NoSuchMethodError e) {
/*  52 */       display.getMetrics(metrics);
/*     */     }
/*     */ 
/*  55 */     this.mXMetersPerPixel = (0.0254F / metrics.xdpi);
/*  56 */     this.mYMetersPerPixel = (0.0254F / metrics.ydpi);
/*  57 */     this.mWidth = metrics.widthPixels;
/*  58 */     this.mHeight = metrics.heightPixels;
/*  59 */     this.mBorderSizeMeters = 0.003F;
/*     */ 
/*  64 */     if (this.mHeight > this.mWidth) {
/*  65 */       int tempPx = this.mWidth;
/*  66 */       this.mWidth = this.mHeight;
/*  67 */       this.mHeight = tempPx;
/*     */ 
/*  69 */       float tempMetersPerPixel = this.mXMetersPerPixel;
/*  70 */       this.mXMetersPerPixel = this.mYMetersPerPixel;
/*  71 */       this.mYMetersPerPixel = tempMetersPerPixel;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static ScreenParams fromProto(Display display, Phone.PhoneParams params)
/*     */   {
/*  80 */     if (params == null) {
/*  81 */       return null;
/*     */     }
/*     */ 
/*  84 */     ScreenParams screenParams = new ScreenParams(display);
/*     */ 
/*  86 */     if (params.hasXPpi()) {
/*  87 */       screenParams.mXMetersPerPixel = (0.0254F / params.getXPpi());
/*     */     }
/*  89 */     if (params.hasYPpi()) {
/*  90 */       screenParams.mYMetersPerPixel = (0.0254F / params.getYPpi());
/*     */     }
/*  92 */     if (params.hasBottomBezelHeight()) {
/*  93 */       screenParams.mBorderSizeMeters = params.getBottomBezelHeight();
/*     */     }
/*     */ 
/*  96 */     return screenParams;
/*     */   }
/*     */ 
/*     */   public ScreenParams(ScreenParams params)
/*     */   {
/* 105 */     this.mWidth = params.mWidth;
/* 106 */     this.mHeight = params.mHeight;
/* 107 */     this.mXMetersPerPixel = params.mXMetersPerPixel;
/* 108 */     this.mYMetersPerPixel = params.mYMetersPerPixel;
/* 109 */     this.mBorderSizeMeters = params.mBorderSizeMeters;
/*     */   }
/*     */ 
/*     */   public void setWidth(int width)
/*     */   {
/* 118 */     this.mWidth = width;
/*     */   }
/*     */ 
/*     */   public int getWidth()
/*     */   {
/* 127 */     return this.mWidth;
/*     */   }
/*     */ 
/*     */   public void setHeight(int height)
/*     */   {
/* 136 */     this.mHeight = height;
/*     */   }
/*     */ 
/*     */   public int getHeight()
/*     */   {
/* 145 */     return this.mHeight;
/*     */   }
/*     */ 
/*     */   public float getWidthMeters()
/*     */   {
/* 154 */     return this.mWidth * this.mXMetersPerPixel;
/*     */   }
/*     */ 
/*     */   public float getHeightMeters()
/*     */   {
/* 163 */     return this.mHeight * this.mYMetersPerPixel;
/*     */   }
/*     */ 
/*     */   public void setBorderSizeMeters(float screenBorderSize)
/*     */   {
/* 175 */     this.mBorderSizeMeters = screenBorderSize;
/*     */   }
/*     */ 
/*     */   public float getBorderSizeMeters()
/*     */   {
/* 184 */     return this.mBorderSizeMeters;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 195 */     if (other == null) {
/* 196 */       return false;
/*     */     }
/*     */ 
/* 199 */     if (other == this) {
/* 200 */       return true;
/*     */     }
/*     */ 
/* 203 */     if (!(other instanceof ScreenParams)) {
/* 204 */       return false;
/*     */     }
/*     */ 
/* 207 */     ScreenParams o = (ScreenParams)other;
/*     */ 
/* 209 */     return (this.mWidth == o.mWidth) && (this.mHeight == o.mHeight) && (this.mXMetersPerPixel == o.mXMetersPerPixel) && (this.mYMetersPerPixel == o.mYMetersPerPixel) && (this.mBorderSizeMeters == o.mBorderSizeMeters);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 223 */     int i = this.mWidth; int j = this.mHeight; float f1 = this.mXMetersPerPixel; float f2 = this.mYMetersPerPixel; float f3 = this.mBorderSizeMeters; return "{\n" + new StringBuilder(22).append("  width: ").append(i).append(",\n").toString() + new StringBuilder(23).append("  height: ").append(j).append(",\n").toString() + new StringBuilder(39).append("  x_meters_per_pixel: ").append(f1).append(",\n").toString() + new StringBuilder(39).append("  y_meters_per_pixel: ").append(f2).append(",\n").toString() + new StringBuilder(39).append("  border_size_meters: ").append(f3).append(",\n").toString() + "}";
/*     */   }
/*     */ 
/*     */   public static ScreenParams createFromInputStream(Display display, InputStream inputStream)
/*     */   {
/* 243 */     Phone.PhoneParams phoneParams = PhoneParams.readFromInputStream(inputStream);
/* 244 */     if (phoneParams == null) {
/* 245 */       return null;
/*     */     }
/* 247 */     return fromProto(display, phoneParams);
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.ScreenParams
 * JD-Core Version:    0.6.2
 */