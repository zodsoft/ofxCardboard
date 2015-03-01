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
/*     */   private int width;
/*     */   private int height;
/*     */   private float xMetersPerPixel;
/*     */   private float yMetersPerPixel;
/*     */   private float borderSizeMeters;
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
/*  55 */     this.xMetersPerPixel = (0.0254F / metrics.xdpi);
/*  56 */     this.yMetersPerPixel = (0.0254F / metrics.ydpi);
/*  57 */     this.width = metrics.widthPixels;
/*  58 */     this.height = metrics.heightPixels;
/*  59 */     this.borderSizeMeters = 0.003F;
/*     */ 
/*  64 */     if (this.height > this.width) {
/*  65 */       int tempPx = this.width;
/*  66 */       this.width = this.height;
/*  67 */       this.height = tempPx;
/*     */ 
/*  69 */       float tempMetersPerPixel = this.xMetersPerPixel;
/*  70 */       this.xMetersPerPixel = this.yMetersPerPixel;
/*  71 */       this.yMetersPerPixel = tempMetersPerPixel;
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
/*  87 */       screenParams.xMetersPerPixel = (0.0254F / params.getXPpi());
/*     */     }
/*  89 */     if (params.hasYPpi()) {
/*  90 */       screenParams.yMetersPerPixel = (0.0254F / params.getYPpi());
/*     */     }
/*  92 */     if (params.hasBottomBezelHeight()) {
/*  93 */       screenParams.borderSizeMeters = params.getBottomBezelHeight();
/*     */     }
/*     */ 
/*  96 */     return screenParams;
/*     */   }
/*     */ 
/*     */   public ScreenParams(ScreenParams params)
/*     */   {
/* 105 */     this.width = params.width;
/* 106 */     this.height = params.height;
/* 107 */     this.xMetersPerPixel = params.xMetersPerPixel;
/* 108 */     this.yMetersPerPixel = params.yMetersPerPixel;
/* 109 */     this.borderSizeMeters = params.borderSizeMeters;
/*     */   }
/*     */ 
/*     */   public void setWidth(int width)
/*     */   {
/* 118 */     this.width = width;
/*     */   }
/*     */ 
/*     */   public int getWidth()
/*     */   {
/* 127 */     return this.width;
/*     */   }
/*     */ 
/*     */   public void setHeight(int height)
/*     */   {
/* 136 */     this.height = height;
/*     */   }
/*     */ 
/*     */   public int getHeight()
/*     */   {
/* 145 */     return this.height;
/*     */   }
/*     */ 
/*     */   public float getWidthMeters()
/*     */   {
/* 154 */     return this.width * this.xMetersPerPixel;
/*     */   }
/*     */ 
/*     */   public float getHeightMeters()
/*     */   {
/* 163 */     return this.height * this.yMetersPerPixel;
/*     */   }
/*     */ 
/*     */   public void setBorderSizeMeters(float screenBorderSize)
/*     */   {
/* 175 */     this.borderSizeMeters = screenBorderSize;
/*     */   }
/*     */ 
/*     */   public float getBorderSizeMeters()
/*     */   {
/* 184 */     return this.borderSizeMeters;
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
/* 209 */     return (this.width == o.width) && (this.height == o.height) && (this.xMetersPerPixel == o.xMetersPerPixel) && (this.yMetersPerPixel == o.yMetersPerPixel) && (this.borderSizeMeters == o.borderSizeMeters);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 223 */     int i = this.width; int j = this.height; float f1 = this.xMetersPerPixel; float f2 = this.yMetersPerPixel; float f3 = this.borderSizeMeters; return "{\n" + new StringBuilder(22).append("  width: ").append(i).append(",\n").toString() + new StringBuilder(23).append("  height: ").append(j).append(",\n").toString() + new StringBuilder(39).append("  x_meters_per_pixel: ").append(f1).append(",\n").toString() + new StringBuilder(39).append("  y_meters_per_pixel: ").append(f2).append(",\n").toString() + new StringBuilder(39).append("  border_size_meters: ").append(f3).append(",\n").toString() + "}";
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