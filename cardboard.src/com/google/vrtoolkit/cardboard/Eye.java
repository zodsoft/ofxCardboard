/*     */ package com.google.vrtoolkit.cardboard;
/*     */ 
/*     */ public class Eye
/*     */ {
/*     */   private final int mType;
/*     */   private final float[] mEyeView;
/*     */   private final Viewport mViewport;
/*     */   private final FieldOfView mFov;
/*     */   private volatile boolean mProjectionChanged;
/*     */   private float[] mPerspective;
/*     */   private float mLastZNear;
/*     */   private float mLastZFar;
/*     */ 
/*     */   public Eye(int type)
/*     */   {
/*  54 */     this.mType = type;
/*  55 */     this.mEyeView = new float[16];
/*  56 */     this.mViewport = new Viewport();
/*  57 */     this.mFov = new FieldOfView();
/*  58 */     this.mProjectionChanged = true;
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/*  67 */     return this.mType;
/*     */   }
/*     */ 
/*     */   public float[] getEyeView()
/*     */   {
/*  85 */     return this.mEyeView;
/*     */   }
/*     */ 
/*     */   public float[] getPerspective(float zNear, float zFar)
/*     */   {
/*  99 */     if ((!this.mProjectionChanged) && (this.mLastZNear == zNear) && (this.mLastZFar == zFar)) {
/* 100 */       return this.mPerspective;
/*     */     }
/*     */ 
/* 103 */     if (this.mPerspective == null) {
/* 104 */       this.mPerspective = new float[16];
/*     */     }
/*     */ 
/* 107 */     getFov().toPerspectiveMatrix(zNear, zFar, this.mPerspective, 0);
/*     */ 
/* 109 */     this.mLastZNear = zNear;
/* 110 */     this.mLastZFar = zFar;
/* 111 */     this.mProjectionChanged = false;
/*     */ 
/* 113 */     return this.mPerspective;
/*     */   }
/*     */ 
/*     */   public Viewport getViewport()
/*     */   {
/* 122 */     return this.mViewport;
/*     */   }
/*     */ 
/*     */   public FieldOfView getFov()
/*     */   {
/* 134 */     return this.mFov;
/*     */   }
/*     */ 
/*     */   public void setProjectionChanged()
/*     */   {
/* 144 */     this.mProjectionChanged = true;
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