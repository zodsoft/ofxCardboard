/*      */ package com.google.vrtoolkit.cardboard;
/*      */ 
/*      */ import android.content.Context;
/*      */ import android.opengl.GLES20;
/*      */ import android.opengl.GLSurfaceView;
/*      */ import android.opengl.GLSurfaceView.Renderer;
/*      */ import android.opengl.Matrix;
/*      */ import android.util.AttributeSet;
/*      */ import android.util.Log;
/*      */ import android.view.MotionEvent;
/*      */ import com.google.vrtoolkit.cardboard.proto.Phone.PhoneParams;
/*      */ import com.google.vrtoolkit.cardboard.sensors.HeadTracker;
/*      */ import java.util.concurrent.CountDownLatch;
/*      */ import javax.microedition.khronos.egl.EGLConfig;
/*      */ import javax.microedition.khronos.opengles.GL10;
/*      */ 
/*      */ public class CardboardView extends GLSurfaceView
/*      */ {
/*      */   private static final String TAG = "CardboardView";
/*      */   private RendererHelper mRendererHelper;
/*      */   private HeadTracker mHeadTracker;
/*      */   private HeadMountedDisplayManager mHmdManager;
/*      */   private UiLayer mUiLayer;
/*      */   private CountDownLatch mShutdownLatch;
/*   63 */   private boolean mVRMode = true;
/*   64 */   private boolean mRendererSet = false;
/*   65 */   private volatile boolean mRestoreGLStateEnabled = true;
/*   66 */   private volatile boolean mDistortionCorrectionEnabled = true;
/*   67 */   private volatile boolean mChromaticAberrationCorrectionEnabled = false;
/*   68 */   private volatile boolean mVignetteEnabled = true;
/*      */ 
/*      */   public CardboardView(Context context)
/*      */   {
/*  262 */     super(context);
/*  263 */     init(context);
/*      */   }
/*      */ 
/*      */   public CardboardView(Context context, AttributeSet attrs) {
/*  267 */     super(context, attrs);
/*  268 */     init(context);
/*      */   }
/*      */ 
/*      */   public void setRenderer(Renderer renderer)
/*      */   {
/*  280 */     if (renderer == null) {
/*  281 */       return;
/*      */     }
/*      */ 
/*  284 */     this.mRendererHelper.setRenderer(renderer);
/*      */ 
/*  287 */     super.setRenderer(this.mRendererHelper);
/*  288 */     this.mRendererSet = true;
/*      */   }
/*      */ 
/*      */   public void setRenderer(StereoRenderer renderer)
/*      */   {
/*  300 */     setRenderer(renderer != null ? new StereoRendererHelper(renderer) : (Renderer)null);
/*      */   }
/*      */ 
/*      */   public void getCurrentEyeParams(HeadTransform head, Eye leftEye, Eye rightEye, Eye monocular)
/*      */   {
/*  319 */     this.mRendererHelper.getCurrentEyeParams(head, leftEye, rightEye, monocular);
/*      */   }
/*      */ 
/*      */   public void setVRModeEnabled(boolean enabled)
/*      */   {
/*  339 */     this.mVRMode = enabled;
/*  340 */     this.mRendererHelper.setVRModeEnabled(enabled);
/*      */   }
/*      */ 
/*      */   public boolean getVRMode()
/*      */   {
/*  350 */     return this.mVRMode;
/*      */   }
/*      */ 
/*      */   public void setAlignmentMarkerEnabled(boolean enabled)
/*      */   {
/*  362 */     this.mUiLayer.setAlignmentMarkerEnabled(enabled);
/*      */   }
/*      */ 
/*      */   public boolean getAlignmentMarkerEnabled()
/*      */   {
/*  372 */     return this.mUiLayer.getAlignmentMarkerEnabled();
/*      */   }
/*      */ 
/*      */   public void setSettingsButtonEnabled(boolean enabled)
/*      */   {
/*  386 */     this.mUiLayer.setSettingsButtonEnabled(enabled);
/*      */   }
/*      */ 
/*      */   public boolean getSettingsButtonEnabled()
/*      */   {
/*  396 */     return this.mUiLayer.getSettingsButtonEnabled();
/*      */   }
/*      */ 
/*      */   public HeadMountedDisplay getHeadMountedDisplay()
/*      */   {
/*  414 */     return this.mHmdManager.getHeadMountedDisplay();
/*      */   }
/*      */ 
/*      */   public void setRestoreGLStateEnabled(boolean enabled)
/*      */   {
/*  447 */     this.mRestoreGLStateEnabled = enabled;
/*  448 */     this.mRendererHelper.setRestoreGLStateEnabled(enabled);
/*      */   }
/*      */ 
/*      */   public boolean getRestoreGLStateEnabled()
/*      */   {
/*  458 */     return this.mRestoreGLStateEnabled;
/*      */   }
/*      */ 
/*      */   public void setChromaticAberrationCorrectionEnabled(boolean enabled)
/*      */   {
/*  473 */     this.mChromaticAberrationCorrectionEnabled = enabled;
/*  474 */     this.mRendererHelper.setChromaticAberrationCorrectionEnabled(enabled);
/*      */   }
/*      */ 
/*      */   public boolean getChromaticAberrationCorrectionEnabled()
/*      */   {
/*  485 */     return this.mChromaticAberrationCorrectionEnabled;
/*      */   }
/*      */ 
/*      */   public void setVignetteEnabled(boolean enabled)
/*      */   {
/*  500 */     this.mVignetteEnabled = enabled;
/*  501 */     this.mRendererHelper.setVignetteEnabled(enabled);
/*      */   }
/*      */ 
/*      */   public boolean getVignetteEnabled()
/*      */   {
/*  511 */     return this.mVignetteEnabled;
/*      */   }
/*      */ 
/*      */   public void setNeckModelEnabled(boolean enabled)
/*      */   {
/*  526 */     this.mHeadTracker.setNeckModelEnabled(enabled);
/*      */   }
/*      */ 
/*      */   public void updateCardboardDeviceParams(CardboardDeviceParams cardboardDeviceParams)
/*      */   {
/*  539 */     if (this.mHmdManager.updateCardboardDeviceParams(cardboardDeviceParams))
/*  540 */       this.mRendererHelper.setCardboardDeviceParams(getCardboardDeviceParams());
/*      */   }
/*      */ 
/*      */   public CardboardDeviceParams getCardboardDeviceParams()
/*      */   {
/*  558 */     return this.mHmdManager.getHeadMountedDisplay().getCardboardDeviceParams();
/*      */   }
/*      */ 
/*      */   public void updateScreenParams(ScreenParams screenParams)
/*      */   {
/*  570 */     if (this.mHmdManager.updateScreenParams(screenParams))
/*  571 */       this.mRendererHelper.setScreenParams(getScreenParams());
/*      */   }
/*      */ 
/*      */   public ScreenParams getScreenParams()
/*      */   {
/*  588 */     return this.mHmdManager.getHeadMountedDisplay().getScreenParams();
/*      */   }
/*      */ 
/*      */   public float getInterpupillaryDistance()
/*      */   {
/*  598 */     return getCardboardDeviceParams().getInterLensDistance();
/*      */   }
/*      */ 
/*      */   public void setDistortionCorrectionEnabled(boolean enabled)
/*      */   {
/*  609 */     this.mDistortionCorrectionEnabled = enabled;
/*  610 */     this.mRendererHelper.setDistortionCorrectionEnabled(enabled);
/*      */   }
/*      */ 
/*      */   public boolean getDistortionCorrectionEnabled()
/*      */   {
/*  619 */     return this.mDistortionCorrectionEnabled;
/*      */   }
/*      */ 
/*      */   public void setDistortionCorrectionTextureFormat(int textureFormat, int textureType)
/*      */   {
/*  629 */     this.mRendererHelper.setDistortionCorrectionTextureFormat(textureFormat, textureType);
/*      */   }
/*      */ 
/*      */   public void undistortTexture(int inputTexture)
/*      */   {
/*  646 */     this.mRendererHelper.undistortTexture(inputTexture);
/*      */   }
/*      */ 
/*      */   public void renderUiLayer()
/*      */   {
/*  662 */     this.mRendererHelper.renderUiLayer();
/*      */   }
/*      */ 
/*      */   public void setDistortionCorrectionScale(float scale)
/*      */   {
/*  684 */     this.mRendererHelper.setDistortionCorrectionScale(scale);
/*      */   }
/*      */ 
/*      */   public void onResume()
/*      */   {
/*  693 */     this.mHmdManager.onResume();
/*  694 */     this.mRendererHelper.setCardboardDeviceParams(getCardboardDeviceParams());
/*      */ 
/*  697 */     if (this.mRendererSet) {
/*  698 */       super.onResume();
/*      */     }
/*      */ 
/*  703 */     Phone.PhoneParams phoneParams = PhoneParams.readFromExternalStorage();
/*  704 */     if (phoneParams != null) {
/*  705 */       this.mHeadTracker.setGyroBias(phoneParams.gyroBias);
/*      */     }
/*  707 */     this.mHeadTracker.startTracking();
/*      */   }
/*      */ 
/*      */   public void onPause()
/*      */   {
/*  715 */     this.mHmdManager.onPause();
/*      */ 
/*  718 */     if (this.mRendererSet) {
/*  719 */       super.onPause();
/*      */     }
/*      */ 
/*  722 */     this.mHeadTracker.stopTracking();
/*      */   }
/*      */ 
/*      */   public void queueEvent(Runnable r)
/*      */   {
/*  729 */     if (!this.mRendererSet) {
/*  730 */       r.run();
/*  731 */       return;
/*      */     }
/*      */ 
/*  734 */     super.queueEvent(r);
/*      */   }
/*      */ 
/*      */   public void setRenderer(GLSurfaceView.Renderer renderer)
/*      */   {
/*  740 */     throw new RuntimeException("Please use the CardboardView renderer interfaces"); } 
/*      */   // ERROR //
/*      */   public void onDetachedFromWindow() { // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 10	com/google/vrtoolkit/cardboard/CardboardView:mRendererSet	Z
/*      */     //   4: ifeq +82 -> 86
/*      */     //   7: aload_0
/*      */     //   8: getfield 3	com/google/vrtoolkit/cardboard/CardboardView:mShutdownLatch	Ljava/util/concurrent/CountDownLatch;
/*      */     //   11: ifnonnull +75 -> 86
/*      */     //   14: aload_0
/*      */     //   15: new 60	java/util/concurrent/CountDownLatch
/*      */     //   18: dup
/*      */     //   19: iconst_1
/*      */     //   20: invokespecial 61	java/util/concurrent/CountDownLatch:<init>	(I)V
/*      */     //   23: putfield 3	com/google/vrtoolkit/cardboard/CardboardView:mShutdownLatch	Ljava/util/concurrent/CountDownLatch;
/*      */     //   26: aload_0
/*      */     //   27: getfield 13	com/google/vrtoolkit/cardboard/CardboardView:mRendererHelper	Lcom/google/vrtoolkit/cardboard/CardboardView$RendererHelper;
/*      */     //   30: invokevirtual 62	com/google/vrtoolkit/cardboard/CardboardView$RendererHelper:shutdown	()V
/*      */     //   33: aload_0
/*      */     //   34: getfield 3	com/google/vrtoolkit/cardboard/CardboardView:mShutdownLatch	Ljava/util/concurrent/CountDownLatch;
/*      */     //   37: invokevirtual 63	java/util/concurrent/CountDownLatch:await	()V
/*      */     //   40: goto +41 -> 81
/*      */     //   43: astore_1
/*      */     //   44: ldc 65
/*      */     //   46: ldc 66
/*      */     //   48: aload_1
/*      */     //   49: invokevirtual 67	java/lang/InterruptedException:toString	()Ljava/lang/String;
/*      */     //   52: invokestatic 68	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*      */     //   55: dup
/*      */     //   56: invokevirtual 69	java/lang/String:length	()I
/*      */     //   59: ifeq +9 -> 68
/*      */     //   62: invokevirtual 70	java/lang/String:concat	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   65: goto +12 -> 77
/*      */     //   68: pop
/*      */     //   69: new 71	java/lang/String
/*      */     //   72: dup_x1
/*      */     //   73: swap
/*      */     //   74: invokespecial 72	java/lang/String:<init>	(Ljava/lang/String;)V
/*      */     //   77: invokestatic 73	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
/*      */     //   80: pop
/*      */     //   81: aload_0
/*      */     //   82: aconst_null
/*      */     //   83: putfield 3	com/google/vrtoolkit/cardboard/CardboardView:mShutdownLatch	Ljava/util/concurrent/CountDownLatch;
/*      */     //   86: aload_0
/*      */     //   87: invokespecial 74	android/opengl/GLSurfaceView:onDetachedFromWindow	()V
/*      */     //   90: return
/*      */     //
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   33	40	43	java/lang/InterruptedException } 
/*  764 */   private void init(Context context) { setEGLContextClientVersion(2);
/*  765 */     setPreserveEGLContextOnPause(true);
/*      */ 
/*  767 */     this.mHeadTracker = HeadTracker.createFromContext(context);
/*  768 */     this.mHmdManager = new HeadMountedDisplayManager(context);
/*  769 */     this.mRendererHelper = new RendererHelper();
/*  770 */     this.mUiLayer = new UiLayer(context);
/*      */   }
/*      */ 
/*      */   public boolean onTouchEvent(MotionEvent e)
/*      */   {
/*  776 */     if (this.mUiLayer.onTouchEvent(e)) {
/*  777 */       return true;
/*      */     }
/*  779 */     return super.onTouchEvent(e);
/*      */   }
/*      */ 
/*      */   private class StereoRendererHelper
/*      */     implements CardboardView.Renderer
/*      */   {
/*      */     private final CardboardView.StereoRenderer mStereoRenderer;
/*      */     private boolean mVRMode;
/*      */ 
/*      */     public StereoRendererHelper(CardboardView.StereoRenderer stereoRenderer)
/*      */     {
/* 1260 */       this.mStereoRenderer = stereoRenderer;
/* 1261 */       this.mVRMode = CardboardView.this.mVRMode;
/*      */     }
/*      */ 
/*      */     public void setVRModeEnabled(final boolean enabled) {
/* 1265 */       CardboardView.this.queueEvent(new Runnable()
/*      */       {
/*      */         public void run() {
/* 1268 */           CardboardView.StereoRendererHelper.this.mVRMode = enabled;
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     public void onDrawFrame(HeadTransform head, Eye leftEye, Eye rightEye)
/*      */     {
/* 1275 */       this.mStereoRenderer.onNewFrame(head);
/* 1276 */       GLES20.glEnable(3089);
/*      */ 
/* 1278 */       leftEye.getViewport().setGLViewport();
/* 1279 */       leftEye.getViewport().setGLScissor();
/* 1280 */       this.mStereoRenderer.onDrawEye(leftEye);
/*      */ 
/* 1283 */       if (rightEye == null) {
/* 1284 */         return;
/*      */       }
/*      */ 
/* 1287 */       rightEye.getViewport().setGLViewport();
/* 1288 */       rightEye.getViewport().setGLScissor();
/* 1289 */       this.mStereoRenderer.onDrawEye(rightEye);
/*      */     }
/*      */ 
/*      */     public void onFinishFrame(Viewport viewport)
/*      */     {
/* 1294 */       viewport.setGLViewport();
/* 1295 */       viewport.setGLScissor();
/* 1296 */       this.mStereoRenderer.onFinishFrame(viewport);
/*      */     }
/*      */ 
/*      */     public void onSurfaceChanged(int width, int height)
/*      */     {
/* 1301 */       if (this.mVRMode)
/*      */       {
/* 1304 */         this.mStereoRenderer.onSurfaceChanged(width / 2, height);
/*      */       }
/* 1306 */       else this.mStereoRenderer.onSurfaceChanged(width, height);
/*      */     }
/*      */ 
/*      */     public void onSurfaceCreated(EGLConfig config)
/*      */     {
/* 1312 */       this.mStereoRenderer.onSurfaceCreated(config);
/*      */     }
/*      */ 
/*      */     public void onRendererShutdown()
/*      */     {
/* 1317 */       this.mStereoRenderer.onRendererShutdown();
/*      */     }
/*      */   }
/*      */ 
/*      */   private class RendererHelper
/*      */     implements GLSurfaceView.Renderer
/*      */   {
/*      */     private final HeadTransform mHeadTransform;
/*      */     private final Eye mMonocular;
/*      */     private final Eye mLeftEye;
/*      */     private final Eye mRightEye;
/*      */     private final float[] mLeftEyeTranslate;
/*      */     private final float[] mRightEyeTranslate;
/*      */     private CardboardView.Renderer mRenderer;
/*      */     private boolean mSurfaceCreated;
/*      */     private HeadMountedDisplay mHmd;
/*      */     private DistortionRenderer mDistortionRenderer;
/*      */     private boolean mVRMode;
/*      */     private boolean mDistortionCorrectionEnabled;
/*      */     private boolean mProjectionChanged;
/*      */     private boolean mInvalidSurfaceSize;
/*      */ 
/*      */     public RendererHelper()
/*      */     {
/*  809 */       this.mHmd = new HeadMountedDisplay(CardboardView.this.getHeadMountedDisplay());
/*  810 */       this.mHeadTransform = new HeadTransform();
/*  811 */       this.mMonocular = new Eye(0);
/*  812 */       this.mLeftEye = new Eye(1);
/*  813 */       this.mRightEye = new Eye(2);
/*  814 */       updateFieldOfView(this.mLeftEye.getFov(), this.mRightEye.getFov());
/*  815 */       this.mDistortionRenderer = new DistortionRenderer();
/*  816 */       this.mDistortionRenderer.setRestoreGLStateEnabled(CardboardView.this.mRestoreGLStateEnabled);
/*  817 */       this.mDistortionRenderer.setChromaticAberrationCorrectionEnabled(CardboardView.this.mChromaticAberrationCorrectionEnabled);
/*      */ 
/*  819 */       this.mDistortionRenderer.setVignetteEnabled(CardboardView.this.mVignetteEnabled);
/*      */ 
/*  821 */       this.mLeftEyeTranslate = new float[16];
/*  822 */       this.mRightEyeTranslate = new float[16];
/*      */ 
/*  825 */       this.mVRMode = CardboardView.this.mVRMode;
/*  826 */       this.mDistortionCorrectionEnabled = CardboardView.this.mDistortionCorrectionEnabled;
/*      */ 
/*  829 */       this.mProjectionChanged = true;
/*      */     }
/*      */ 
/*      */     public void setRenderer(CardboardView.Renderer renderer) {
/*  833 */       this.mRenderer = renderer;
/*      */     }
/*      */ 
/*      */     public void shutdown() {
/*  837 */       CardboardView.this.queueEvent(new Runnable()
/*      */       {
/*      */         public void run() {
/*  840 */           if ((CardboardView.RendererHelper.this.mRenderer != null) && (CardboardView.RendererHelper.this.mSurfaceCreated)) {
/*  841 */             CardboardView.RendererHelper.this.mSurfaceCreated = false;
/*  842 */             CardboardView.RendererHelper.this.mRenderer.onRendererShutdown();
/*      */           }
/*      */ 
/*  845 */           CardboardView.this.mShutdownLatch.countDown();
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     public void setCardboardDeviceParams(CardboardDeviceParams newParams) {
/*  851 */       final CardboardDeviceParams deviceParams = new CardboardDeviceParams(newParams);
/*  852 */       CardboardView.this.queueEvent(new Runnable()
/*      */       {
/*      */         public void run() {
/*  855 */           CardboardView.RendererHelper.this.mHmd.setCardboardDeviceParams(deviceParams);
/*  856 */           CardboardView.RendererHelper.this.mProjectionChanged = true;
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     public void setScreenParams(ScreenParams newParams) {
/*  862 */       final ScreenParams screenParams = new ScreenParams(newParams);
/*  863 */       CardboardView.this.queueEvent(new Runnable()
/*      */       {
/*      */         public void run() {
/*  866 */           CardboardView.RendererHelper.this.mHmd.setScreenParams(screenParams);
/*  867 */           CardboardView.RendererHelper.this.mProjectionChanged = true;
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     public void setDistortionCorrectionEnabled(final boolean enabled) {
/*  873 */       CardboardView.this.queueEvent(new Runnable()
/*      */       {
/*      */         public void run() {
/*  876 */           CardboardView.RendererHelper.this.mDistortionCorrectionEnabled = enabled;
/*  877 */           CardboardView.RendererHelper.this.mProjectionChanged = true;
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     public void setDistortionCorrectionScale(final float scale) {
/*  883 */       CardboardView.this.queueEvent(new Runnable()
/*      */       {
/*      */         public void run() {
/*  886 */           CardboardView.RendererHelper.this.mDistortionRenderer.setResolutionScale(scale);
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     public void setVRModeEnabled(final boolean enabled) {
/*  892 */       CardboardView.this.queueEvent(new Runnable()
/*      */       {
/*      */         public void run() {
/*  895 */           if (CardboardView.RendererHelper.this.mVRMode == enabled) {
/*  896 */             return;
/*      */           }
/*      */ 
/*  899 */           CardboardView.RendererHelper.this.mVRMode = enabled;
/*      */ 
/*  902 */           if ((CardboardView.RendererHelper.this.mRenderer instanceof CardboardView.StereoRendererHelper)) {
/*  903 */             CardboardView.StereoRendererHelper stereoHelper = (CardboardView.StereoRendererHelper)CardboardView.RendererHelper.this.mRenderer;
/*  904 */             stereoHelper.setVRModeEnabled(enabled);
/*      */           }
/*      */ 
/*  909 */           CardboardView.RendererHelper.this.mProjectionChanged = true;
/*  910 */           CardboardView.RendererHelper.this.onSurfaceChanged((GL10)null, CardboardView.RendererHelper.this.mHmd.getScreenParams().getWidth(), CardboardView.RendererHelper.this.mHmd.getScreenParams().getHeight());
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     public void setRestoreGLStateEnabled(final boolean enabled)
/*      */     {
/*  918 */       CardboardView.this.queueEvent(new Runnable()
/*      */       {
/*      */         public void run() {
/*  921 */           CardboardView.RendererHelper.this.mDistortionRenderer.setRestoreGLStateEnabled(enabled);
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     public void setChromaticAberrationCorrectionEnabled(final boolean enabled) {
/*  927 */       CardboardView.this.queueEvent(new Runnable()
/*      */       {
/*      */         public void run() {
/*  930 */           CardboardView.RendererHelper.this.mDistortionRenderer.setChromaticAberrationCorrectionEnabled(enabled);
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     public void setVignetteEnabled(final boolean enabled) {
/*  936 */       CardboardView.this.queueEvent(new Runnable()
/*      */       {
/*      */         public void run() {
/*  939 */           CardboardView.RendererHelper.this.mDistortionRenderer.setVignetteEnabled(enabled);
/*  940 */           CardboardView.RendererHelper.this.mProjectionChanged = true;
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     public void setDistortionCorrectionTextureFormat(final int textureFormat, final int textureType)
/*      */     {
/*  947 */       CardboardView.this.queueEvent(new Runnable()
/*      */       {
/*      */         public void run() {
/*  950 */           CardboardView.RendererHelper.this.mDistortionRenderer.setTextureFormat(textureFormat, textureType);
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     public void undistortTexture(final int inputTexture) {
/*  956 */       CardboardView.this.queueEvent(new Runnable()
/*      */       {
/*      */         public void run() {
/*  959 */           CardboardView.RendererHelper.this.mDistortionRenderer.undistortTexture(inputTexture);
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     public void renderUiLayer() {
/*  965 */       CardboardView.this.queueEvent(new Runnable()
/*      */       {
/*      */         public void run() {
/*  968 */           CardboardView.this.mUiLayer.draw(); }  } ); } 
/*      */     // ERROR //
/*      */     public void getCurrentEyeParams(final HeadTransform head, final Eye leftEye, final Eye rightEye, final Eye monocular) { // Byte code:
/*      */       //   0: new 69	java/util/concurrent/CountDownLatch
/*      */       //   3: dup
/*      */       //   4: iconst_1
/*      */       //   5: invokespecial 70	java/util/concurrent/CountDownLatch:<init>	(I)V
/*      */       //   8: astore 5
/*      */       //   10: aload_0
/*      */       //   11: getfield 13	com/google/vrtoolkit/cardboard/CardboardView$RendererHelper:this$0	Lcom/google/vrtoolkit/cardboard/CardboardView;
/*      */       //   14: new 71	com/google/vrtoolkit/cardboard/CardboardView$RendererHelper$13
/*      */       //   17: dup
/*      */       //   18: aload_0
/*      */       //   19: aload_1
/*      */       //   20: aload_2
/*      */       //   21: aload_3
/*      */       //   22: aload 4
/*      */       //   24: aload 5
/*      */       //   26: invokespecial 72	com/google/vrtoolkit/cardboard/CardboardView$RendererHelper$13:<init>	(Lcom/google/vrtoolkit/cardboard/CardboardView$RendererHelper;Lcom/google/vrtoolkit/cardboard/HeadTransform;Lcom/google/vrtoolkit/cardboard/Eye;Lcom/google/vrtoolkit/cardboard/Eye;Lcom/google/vrtoolkit/cardboard/Eye;Ljava/util/concurrent/CountDownLatch;)V
/*      */       //   29: invokevirtual 42	com/google/vrtoolkit/cardboard/CardboardView:queueEvent	(Ljava/lang/Runnable;)V
/*      */       //   32: aload 5
/*      */       //   34: invokevirtual 73	java/util/concurrent/CountDownLatch:await	()V
/*      */       //   37: goto +43 -> 80
/*      */       //   40: astore 6
/*      */       //   42: ldc 75
/*      */       //   44: ldc 76
/*      */       //   46: aload 6
/*      */       //   48: invokevirtual 77	java/lang/InterruptedException:toString	()Ljava/lang/String;
/*      */       //   51: invokestatic 78	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*      */       //   54: dup
/*      */       //   55: invokevirtual 79	java/lang/String:length	()I
/*      */       //   58: ifeq +9 -> 67
/*      */       //   61: invokevirtual 80	java/lang/String:concat	(Ljava/lang/String;)Ljava/lang/String;
/*      */       //   64: goto +12 -> 76
/*      */       //   67: pop
/*      */       //   68: new 81	java/lang/String
/*      */       //   71: dup_x1
/*      */       //   72: swap
/*      */       //   73: invokespecial 82	java/lang/String:<init>	(Ljava/lang/String;)V
/*      */       //   76: invokestatic 83	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
/*      */       //   79: pop
/*      */       //   80: return
/*      */       //
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   32	37	40	java/lang/InterruptedException } 
/*  993 */     private void getFrameParams(HeadTransform head, Eye leftEye, Eye rightEye, Eye monocular) { CardboardDeviceParams cdp = this.mHmd.getCardboardDeviceParams();
/*  994 */       ScreenParams screen = this.mHmd.getScreenParams();
/*      */ 
/*  997 */       CardboardView.this.mHeadTracker.getLastHeadView(head.getHeadView(), 0);
/*      */ 
/* 1004 */       float halfInterpupillaryDistance = cdp.getInterLensDistance() * 0.5F;
/*      */ 
/* 1006 */       if (this.mVRMode)
/*      */       {
/* 1008 */         Matrix.setIdentityM(this.mLeftEyeTranslate, 0);
/* 1009 */         Matrix.setIdentityM(this.mRightEyeTranslate, 0);
/*      */ 
/* 1011 */         Matrix.translateM(this.mLeftEyeTranslate, 0, halfInterpupillaryDistance, 0.0F, 0.0F);
/*      */ 
/* 1013 */         Matrix.translateM(this.mRightEyeTranslate, 0, -halfInterpupillaryDistance, 0.0F, 0.0F);
/*      */ 
/* 1017 */         Matrix.multiplyMM(leftEye.getEyeView(), 0, this.mLeftEyeTranslate, 0, head.getHeadView(), 0);
/*      */ 
/* 1020 */         Matrix.multiplyMM(rightEye.getEyeView(), 0, this.mRightEyeTranslate, 0, head.getHeadView(), 0);
/*      */       }
/*      */       else
/*      */       {
/* 1026 */         System.arraycopy(head.getHeadView(), 0, monocular.getEyeView(), 0, head.getHeadView().length);
/*      */       }
/*      */ 
/* 1032 */       if (this.mProjectionChanged)
/*      */       {
/* 1035 */         monocular.getViewport().setViewport(0, 0, screen.getWidth(), screen.getHeight());
/*      */ 
/* 1037 */         CardboardView.this.mUiLayer.updateViewport(monocular.getViewport());
/*      */ 
/* 1039 */         if (!this.mVRMode) {
/* 1040 */           updateMonocularFieldOfView(monocular.getFov());
/* 1041 */         } else if (this.mDistortionCorrectionEnabled) {
/* 1042 */           updateFieldOfView(leftEye.getFov(), rightEye.getFov());
/* 1043 */           this.mDistortionRenderer.onFovChanged(this.mHmd, leftEye.getFov(), rightEye.getFov(), getVirtualEyeToScreenDistance());
/*      */         }
/*      */         else
/*      */         {
/* 1047 */           updateUndistortedFovAndViewport();
/*      */         }
/*      */ 
/* 1050 */         leftEye.setProjectionChanged();
/* 1051 */         rightEye.setProjectionChanged();
/* 1052 */         monocular.setProjectionChanged();
/* 1053 */         this.mProjectionChanged = false;
/*      */       }
/*      */ 
/* 1057 */       if ((this.mDistortionCorrectionEnabled) && (this.mDistortionRenderer.haveViewportsChanged()))
/* 1058 */         this.mDistortionRenderer.updateViewports(leftEye.getViewport(), rightEye.getViewport());
/*      */     }
/*      */ 
/*      */     public void onDrawFrame(GL10 gl)
/*      */     {
/* 1064 */       if ((this.mRenderer == null) || (!this.mSurfaceCreated) || (this.mInvalidSurfaceSize)) {
/* 1065 */         return;
/*      */       }
/*      */ 
/* 1068 */       getFrameParams(this.mHeadTransform, this.mLeftEye, this.mRightEye, this.mMonocular);
/*      */ 
/* 1070 */       if (this.mVRMode) {
/* 1071 */         if (this.mDistortionCorrectionEnabled) {
/* 1072 */           this.mDistortionRenderer.beforeDrawFrame();
/* 1073 */           this.mRenderer.onDrawFrame(this.mHeadTransform, this.mLeftEye, this.mRightEye);
/* 1074 */           this.mDistortionRenderer.afterDrawFrame();
/*      */         } else {
/* 1076 */           this.mRenderer.onDrawFrame(this.mHeadTransform, this.mLeftEye, this.mRightEye);
/*      */         }
/*      */       }
/* 1079 */       else this.mRenderer.onDrawFrame(this.mHeadTransform, this.mMonocular, null);
/*      */ 
/* 1082 */       this.mRenderer.onFinishFrame(this.mMonocular.getViewport());
/* 1083 */       if (this.mVRMode)
/* 1084 */         CardboardView.this.mUiLayer.draw();
/*      */     }
/*      */ 
/*      */     public void onSurfaceChanged(GL10 gl, int width, int height)
/*      */     {
/* 1090 */       if ((this.mRenderer == null) || (!this.mSurfaceCreated)) {
/* 1091 */         return;
/*      */       }
/*      */ 
/* 1095 */       ScreenParams screen = this.mHmd.getScreenParams();
/* 1096 */       if ((width != screen.getWidth()) || (height != screen.getHeight())) {
/* 1097 */         if (!this.mInvalidSurfaceSize) {
/* 1098 */           GLES20.glClear(16384);
/* 1099 */           int i = width; int j = height; int k = screen.getWidth(); int m = screen.getHeight(); Log.w("CardboardView", 124 + "Surface size " + i + "x" + j + " does not match the expected screen size " + k + "x" + m + ". Rendering is disabled.");
/*      */         }
/*      */ 
/* 1105 */         this.mInvalidSurfaceSize = true;
/*      */       } else {
/* 1107 */         this.mInvalidSurfaceSize = false;
/*      */       }
/*      */ 
/* 1112 */       this.mRenderer.onSurfaceChanged(width, height);
/*      */     }
/*      */ 
/*      */     public void onSurfaceCreated(GL10 gl, EGLConfig config)
/*      */     {
/* 1117 */       if (this.mRenderer == null) {
/* 1118 */         return;
/*      */       }
/*      */ 
/* 1121 */       this.mSurfaceCreated = true;
/* 1122 */       this.mRenderer.onSurfaceCreated(config);
/* 1123 */       CardboardView.this.mUiLayer.initializeGl();
/*      */     }
/*      */ 
/*      */     private void updateFieldOfView(FieldOfView leftEyeFov, FieldOfView rightEyeFov) {
/* 1127 */       CardboardDeviceParams cdp = this.mHmd.getCardboardDeviceParams();
/* 1128 */       ScreenParams screen = this.mHmd.getScreenParams();
/* 1129 */       Distortion distortion = cdp.getDistortion();
/*      */ 
/* 1143 */       float eyeToScreenDist = getVirtualEyeToScreenDistance();
/*      */ 
/* 1145 */       float outerDist = (screen.getWidthMeters() - cdp.getInterLensDistance()) / 2.0F;
/*      */ 
/* 1147 */       float innerDist = cdp.getInterLensDistance() / 2.0F;
/* 1148 */       float bottomDist = cdp.getVerticalDistanceToLensCenter() - screen.getBorderSizeMeters();
/*      */ 
/* 1150 */       float topDist = screen.getHeightMeters() + screen.getBorderSizeMeters() - cdp.getVerticalDistanceToLensCenter();
/*      */ 
/* 1155 */       float outerAngle = (float)Math.toDegrees(Math.atan(distortion.distort(outerDist / eyeToScreenDist)));
/*      */ 
/* 1157 */       float innerAngle = (float)Math.toDegrees(Math.atan(distortion.distort(innerDist / eyeToScreenDist)));
/*      */ 
/* 1159 */       float bottomAngle = (float)Math.toDegrees(Math.atan(distortion.distort(bottomDist / eyeToScreenDist)));
/*      */ 
/* 1161 */       float topAngle = (float)Math.toDegrees(Math.atan(distortion.distort(topDist / eyeToScreenDist)));
/*      */ 
/* 1164 */       leftEyeFov.setLeft(Math.min(outerAngle, cdp.getLeftEyeMaxFov().getLeft()));
/* 1165 */       leftEyeFov.setRight(Math.min(innerAngle, cdp.getLeftEyeMaxFov().getRight()));
/* 1166 */       leftEyeFov.setBottom(Math.min(bottomAngle, cdp.getLeftEyeMaxFov().getBottom()));
/* 1167 */       leftEyeFov.setTop(Math.min(topAngle, cdp.getLeftEyeMaxFov().getTop()));
/*      */ 
/* 1170 */       rightEyeFov.setLeft(leftEyeFov.getRight());
/* 1171 */       rightEyeFov.setRight(leftEyeFov.getLeft());
/* 1172 */       rightEyeFov.setBottom(leftEyeFov.getBottom());
/* 1173 */       rightEyeFov.setTop(leftEyeFov.getTop());
/*      */     }
/*      */ 
/*      */     private void updateMonocularFieldOfView(FieldOfView monocularFov) {
/* 1177 */       ScreenParams screen = this.mHmd.getScreenParams();
/*      */ 
/* 1182 */       float monocularBottomFov = 22.5F;
/* 1183 */       float monocularLeftFov = (float)Math.toDegrees(Math.atan(Math.tan(Math.toRadians(monocularBottomFov)) * screen.getWidthMeters() / screen.getHeightMeters()));
/*      */ 
/* 1187 */       monocularFov.setLeft(monocularLeftFov);
/* 1188 */       monocularFov.setRight(monocularLeftFov);
/* 1189 */       monocularFov.setBottom(monocularBottomFov);
/* 1190 */       monocularFov.setTop(monocularBottomFov);
/*      */     }
/*      */ 
/*      */     private void updateUndistortedFovAndViewport() {
/* 1194 */       ScreenParams screen = this.mHmd.getScreenParams();
/* 1195 */       CardboardDeviceParams cdp = this.mHmd.getCardboardDeviceParams();
/*      */ 
/* 1197 */       float halfLensDistance = cdp.getInterLensDistance() / 2.0F;
/* 1198 */       float eyeToScreen = getVirtualEyeToScreenDistance();
/*      */ 
/* 1211 */       float left = screen.getWidthMeters() / 2.0F - halfLensDistance;
/* 1212 */       float right = halfLensDistance;
/* 1213 */       float bottom = cdp.getVerticalDistanceToLensCenter() - screen.getBorderSizeMeters();
/* 1214 */       float top = screen.getBorderSizeMeters() + screen.getHeightMeters() - cdp.getVerticalDistanceToLensCenter();
/*      */ 
/* 1217 */       FieldOfView leftEyeFov = this.mLeftEye.getFov();
/* 1218 */       leftEyeFov.setLeft((float)Math.toDegrees(Math.atan2(left, eyeToScreen)));
/*      */ 
/* 1220 */       leftEyeFov.setRight((float)Math.toDegrees(Math.atan2(right, eyeToScreen)));
/*      */ 
/* 1222 */       leftEyeFov.setBottom((float)Math.toDegrees(Math.atan2(bottom, eyeToScreen)));
/*      */ 
/* 1224 */       leftEyeFov.setTop((float)Math.toDegrees(Math.atan2(top, eyeToScreen)));
/*      */ 
/* 1228 */       FieldOfView rightEyeFov = this.mRightEye.getFov();
/* 1229 */       rightEyeFov.setLeft(leftEyeFov.getRight());
/* 1230 */       rightEyeFov.setRight(leftEyeFov.getLeft());
/* 1231 */       rightEyeFov.setBottom(leftEyeFov.getBottom());
/* 1232 */       rightEyeFov.setTop(leftEyeFov.getTop());
/*      */ 
/* 1234 */       this.mLeftEye.getViewport().setViewport(0, 0, screen.getWidth() / 2, screen.getHeight());
/*      */ 
/* 1236 */       this.mRightEye.getViewport().setViewport(screen.getWidth() / 2, 0, screen.getWidth() / 2, screen.getHeight());
/*      */     }
/*      */ 
/*      */     private float getVirtualEyeToScreenDistance()
/*      */     {
/* 1246 */       return this.mHmd.getCardboardDeviceParams().getScreenToLensDistance();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract interface StereoRenderer
/*      */   {
/*      */     public abstract void onNewFrame(HeadTransform paramHeadTransform);
/*      */ 
/*      */     public abstract void onDrawEye(Eye paramEye);
/*      */ 
/*      */     public abstract void onFinishFrame(Viewport paramViewport);
/*      */ 
/*      */     public abstract void onSurfaceChanged(int paramInt1, int paramInt2);
/*      */ 
/*      */     public abstract void onSurfaceCreated(EGLConfig paramEGLConfig);
/*      */ 
/*      */     public abstract void onRendererShutdown();
/*      */   }
/*      */ 
/*      */   public static abstract interface Renderer
/*      */   {
/*      */     public abstract void onDrawFrame(HeadTransform paramHeadTransform, Eye paramEye1, Eye paramEye2);
/*      */ 
/*      */     public abstract void onFinishFrame(Viewport paramViewport);
/*      */ 
/*      */     public abstract void onSurfaceChanged(int paramInt1, int paramInt2);
/*      */ 
/*      */     public abstract void onSurfaceCreated(EGLConfig paramEGLConfig);
/*      */ 
/*      */     public abstract void onRendererShutdown();
/*      */   }
/*      */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.CardboardView
 * JD-Core Version:    0.6.2
 */