/*     */ package com.google.vrtoolkit.cardboard;
/*     */ 
/*     */ public class Eye
/*     */ {
/*     */   private final int type;
/*     */   private final float[] eyeView;
/*     */   private final Viewport viewport;
/*     */   private final FieldOfView fov;
/*     */   private volatile boolean projectionChanged;
/*     */   private float[] perspective;
/*     */   private float lastZNear;
/*     */   private float lastZFar;
/*     */ 
/*     */   public Eye(int type)
/*     */   {
/*  54 */     this.type = type;
/*  55 */     this.eyeView = new float[16];
/*  56 */     this.viewport = new Viewport();
/*  57 */     this.fov = new FieldOfView();
/*  58 */     this.projectionChanged = true;
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/*  67 */     return this.type;
/*     */   }
/*     */ 
/*     */   public float[] getEyeView()
/*     */   {
/*  85 */     return this.eyeView;
/*     */   }
/*     */ 
/*     */   public float[] getPerspective(float zNear, float zFar)
/*     */   {
/*  99 */     if ((!this.projectionChanged) && (this.lastZNear == zNear) && (this.lastZFar == zFar)) {
/* 100 */       return this.perspective;
/*     */     }
/*     */ 
/* 103 */     if (this.perspective == null) {
/* 104 */       this.perspective = new float[16];
/*     */     }
/*     */ 
/* 107 */     getFov().toPerspectiveMatrix(zNear, zFar, this.perspective, 0);
/*     */ 
/* 109 */     this.lastZNear = zNear;
/* 110 */     this.lastZFar = zFar;
/* 111 */     this.projectionChanged = false;
/*     */ 
/* 113 */     return this.perspective;
/*     */   }
/*     */ 
/*     */   public Viewport getViewport()
/*     */   {
/* 122 */     return this.viewport;
/*     */   }
/*     */ 
/*     */   public FieldOfView getFov()
/*     */   {
/* 134 */     return this.fov;
/*     */   }
/*     */ 
/*     */   public void setProjectionChanged()
/*     */   {
/* 144 */     this.projectionChanged = true;
/*     */   }
/*     */ 
/*     */   public boolean getProjectionChanged()
/*     */   {
/* 153 */     return this.projectionChanged;
/*     */   }
/*     */ 
/*     */   public static abstract class Type
/*     */   {
/*     */     public static final int MONOCULAR = 0;
/*     */     public static final int LEFT = 1;
/*     */     public static final int RIGHT = 2;
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.Eye
 * JD-Core Version:    0.6.2
 */