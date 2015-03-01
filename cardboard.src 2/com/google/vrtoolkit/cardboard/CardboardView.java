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
/*      */ import com.google.vrtoolkit.cardboard.sensors.HeadTracker;
/*      */ import java.util.concurrent.CountDownLatch;
/*      */ import javax.microedition.khronos.egl.EGLConfig;
/*      */ import javax.microedition.khronos.opengles.GL10;
/*      */ 
/*      */ public class CardboardView extends GLSurfaceView
/*      */ {
/*      */   private static final String TAG = "CardboardView";
/*      */   private RendererHelper rendererHelper;
/*      */   private HeadTracker headTracker;
/*      */   private HeadMountedDisplayManager hmdManager;
/*      */   private UiLayer uiLayer;
/*      */   private CountDownLatch shutdownLatch;
/*   62 */   private boolean vrMode = true;
/*   63 */   private boolean rendererSet = false;
/*   64 */   private volatile boolean restoreGLStateEnabled = true;
/*   65 */   private volatile boolean distortionCorrectionEnabled = true;
/*   66 */   private volatile boolean chromaticAberrationCorrectionEnabled = false;
/*   67 */   private volatile boolean vignetteEnabled = true;
/*      */ 
/*      */   public CardboardView(Context context)
/*      */   {
/*  261 */     super(context);
/*  262 */     init(context);
/*      */   }
/*      */ 
/*      */   public CardboardView(Context context, AttributeSet attrs) {
/*  266 */     super(context, attrs);
/*  267 */     init(context);
/*      */   }
/*      */ 
/*      */   public void setRenderer(Renderer renderer)
/*      */   {
/*  279 */     if (renderer == null) {
/*  280 */       return;
/*      */     }
/*      */ 
/*  283 */     this.rendererHelper.setRenderer(renderer);
/*      */ 
/*  286 */     super.setRenderer(this.rendererHelper);
/*  287 */     this.rendererSet = true;
/*      */   }
/*      */ 
/*      */   public void setRenderer(StereoRenderer renderer)
/*      */   {
/*  299 */     setRenderer(renderer != null ? new StereoRendererHelper(renderer) : (Renderer)null);
/*      */   }
/*      */ 
/*      */   public void getCurrentEyeParams(HeadTransform head, Eye leftEye, Eye rightEye, Eye monocular, Eye leftEyeNoDistortionCorrection, Eye rightEyeNoDistortionCorrection)
/*      */   {
/*  329 */     this.rendererHelper.getCurrentEyeParams(head, leftEye, rightEye, monocular, leftEyeNoDistortionCorrection, rightEyeNoDistortionCorrection);
/*      */   }
/*      */ 
/*      */   public void setVRModeEnabled(boolean enabled)
/*      */   {
/*  351 */     this.vrMode = enabled;
/*  352 */     this.rendererHelper.setVRModeEnabled(enabled);
/*      */   }
/*      */ 
/*      */   public boolean getVRMode()
/*      */   {
/*  362 */     return this.vrMode;
/*      */   }
/*      */ 
/*      */   public void setAlignmentMarkerEnabled(boolean enabled)
/*      */   {
/*  374 */     this.uiLayer.setAlignmentMarkerEnabled(enabled);
/*      */   }
/*      */ 
/*      */   public boolean getAlignmentMarkerEnabled()
/*      */   {
/*  384 */     return this.uiLayer.getAlignmentMarkerEnabled();
/*      */   }
/*      */ 
/*      */   public void setSettingsButtonEnabled(boolean enabled)
/*      */   {
/*  398 */     this.uiLayer.setSettingsButtonEnabled(enabled);
/*      */   }
/*      */ 
/*      */   public boolean getSettingsButtonEnabled()
/*      */   {
/*  408 */     return this.uiLayer.getSettingsButtonEnabled();
/*      */   }
/*      */ 
/*      */   public HeadMountedDisplay getHeadMountedDisplay()
/*      */   {
/*  426 */     return this.hmdManager.getHeadMountedDisplay();
/*      */   }
/*      */ 
/*      */   public void setRestoreGLStateEnabled(boolean enabled)
/*      */   {
/*  459 */     this.restoreGLStateEnabled = enabled;
/*  460 */     this.rendererHelper.setRestoreGLStateEnabled(enabled);
/*      */   }
/*      */ 
/*      */   public boolean getRestoreGLStateEnabled()
/*      */   {
/*  470 */     return this.restoreGLStateEnabled;
/*      */   }
/*      */ 
/*      */   public void setChromaticAberrationCorrectionEnabled(boolean enabled)
/*      */   {
/*  485 */     this.chromaticAberrationCorrectionEnabled = enabled;
/*  486 */     this.rendererHelper.setChromaticAberrationCorrectionEnabled(enabled);
/*      */   }
/*      */ 
/*      */   public boolean getChromaticAberrationCorrectionEnabled()
/*      */   {
/*  497 */     return this.chromaticAberrationCorrectionEnabled;
/*      */   }
/*      */ 
/*      */   public void setVignetteEnabled(boolean enabled)
/*      */   {
/*  512 */     this.vignetteEnabled = enabled;
/*  513 */     this.rendererHelper.setVignetteEnabled(enabled);
/*      */   }
/*      */ 
/*      */   public boolean getVignetteEnabled()
/*      */   {
/*  523 */     return this.vignetteEnabled;
/*      */   }
/*      */ 
/*      */   public void setNeckModelEnabled(boolean enabled)
/*      */   {
/*  538 */     this.headTracker.setNeckModelEnabled(enabled);
/*      */   }
/*      */ 
/*      */   public void setGyroBiasEstimationEnabled(boolean enabled)
/*      */   {
/*  547 */     this.headTracker.setGyroBiasEstimationEnabled(enabled);
/*      */   }
/*      */ 
/*      */   public void resetHeadTracker()
/*      */   {
/*  555 */     this.headTracker.resetTracker();
/*      */   }
/*      */ 
/*      */   public void updateCardboardDeviceParams(CardboardDeviceParams cardboardDeviceParams)
/*      */   {
/*  568 */     if (this.hmdManager.updateCardboardDeviceParams(cardboardDeviceParams))
/*  569 */       this.rendererHelper.setCardboardDeviceParams(getCardboardDeviceParams());
/*      */   }
/*      */ 
/*      */   public CardboardDeviceParams getCardboardDeviceParams()
/*      */   {
/*  588 */     return this.hmdManager.getHeadMountedDisplay().getCardboardDeviceParams();
/*      */   }
/*      */ 
/*      */   public void updateScreenParams(ScreenParams screenParams)
/*      */   {
/*  600 */     if (this.hmdManager.updateScreenParams(screenParams))
/*  601 */       this.rendererHelper.setScreenParams(getScreenParams());
/*      */   }
/*      */ 
/*      */   public ScreenParams getScreenParams()
/*      */   {
/*  619 */     return this.hmdManager.getHeadMountedDisplay().getScreenParams();
/*      */   }
/*      */ 
/*      */   public float getInterpupillaryDistance()
/*      */   {
/*  629 */     return getCardboardDeviceParams().getInterLensDistance();
/*      */   }
/*      */ 
/*      */   public void setDistortionCorrectionEnabled(boolean enabled)
/*      */   {
/*  640 */     this.distortionCorrectionEnabled = enabled;
/*  641 */     this.rendererHelper.setDistortionCorrectionEnabled(enabled);
/*      */   }
/*      */ 
/*      */   public boolean getDistortionCorrectionEnabled()
/*      */   {
/*  650 */     return this.distortionCorrectionEnabled;
/*      */   }
/*      */ 
/*      */   public void setDistortionCorrectionTextureFormat(int textureFormat, int textureType)
/*      */   {
/*  660 */     this.rendererHelper.setDistortionCorrectionTextureFormat(textureFormat, textureType);
/*      */   }
/*      */ 
/*      */   public void undistortTexture(int inputTexture)
/*      */   {
/*  677 */     this.rendererHelper.undistortTexture(inputTexture);
/*      */   }
/*      */ 
/*      */   public void renderUiLayer()
/*      */   {
/*  693 */     this.rendererHelper.renderUiLayer();
/*      */   }
/*      */ 
/*      */   public void setDistortionCorrectionScale(float scale)
/*      */   {
/*  715 */     this.rendererHelper.setDistortionCorrectionScale(scale);
/*      */   }
/*      */ 
/*      */   public void onResume()
/*      */   {
/*  724 */     this.hmdManager.onResume();
/*  725 */     this.rendererHelper.setCardboardDeviceParams(getCardboardDeviceParams());
/*      */ 
/*  728 */     if (this.rendererSet) {
/*  729 */       super.onResume();
/*      */     }
/*      */ 
/*  732 */     this.headTracker.startTracking();
/*      */   }
/*      */ 
/*      */   public void onPause()
/*      */   {
/*  740 */     this.hmdManager.onPause();
/*      */ 
/*  743 */     if (this.rendererSet) {
/*  744 */       super.onPause();
/*      */     }
/*      */ 
/*  747 */     this.headTracker.stopTracking();
/*      */   }
/*      */ 
/*      */   public void queueEvent(Runnable r)
/*      */   {
/*  754 */     if (!this.rendererSet) {
/*  755 */       r.run();
/*  756 */       return;
/*      */     }
/*      */ 
/*  759 */     super.queueEvent(r);
/*      */   }
/*      */ 
/*      */   public void setRenderer(GLSurfaceView.Renderer renderer)
/*      */   {
/*  765 */     throw new RuntimeException("Please use the CardboardView renderer interfaces"); } 
/*      */   // ERROR //
/*      */   public void onDetachedFromWindow() { // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 10	com/google/vrtoolkit/cardboard/CardboardView:rendererSet	Z
/*      */     //   4: ifeq +82 -> 86
/*      */     //   7: aload_0
/*      */     //   8: getfield 3	com/google/vrtoolkit/cardboard/CardboardView:shutdownLatch	Ljava/util/concurrent/CountDownLatch;
/*      */     //   11: ifnonnull +75 -> 86
/*      */     //   14: aload_0
/*      */     //   15: new 59	java/util/concurrent/CountDownLatch
/*      */     //   18: dup
/*      */     //   19: iconst_1
/*      */     //   20: invokespecial 60	java/util/concurrent/CountDownLatch:<init>	(I)V
/*      */     //   23: putfield 3	com/google/vrtoolkit/cardboard/CardboardView:shutdownLatch	Ljava/util/concurrent/CountDownLatch;
/*      */     //   26: aload_0
/*      */     //   27: getfield 13	com/google/vrtoolkit/cardboard/CardboardView:rendererHelper	Lcom/google/vrtoolkit/cardboard/CardboardView$RendererHelper;
/*      */     //   30: invokevirtual 61	com/google/vrtoolkit/cardboard/CardboardView$RendererHelper:shutdown	()V
/*      */     //   33: aload_0
/*      */     //   34: getfield 3	com/google/vrtoolkit/cardboard/CardboardView:shutdownLatch	Ljava/util/concurrent/CountDownLatch;
/*      */     //   37: invokevirtual 62	java/util/concurrent/CountDownLatch:await	()V
/*      */     //   40: goto +41 -> 81
/*      */     //   43: astore_1
/*      */     //   44: ldc 64
/*      */     //   46: ldc 65
/*      */     //   48: aload_1
/*      */     //   49: invokevirtual 66	java/lang/InterruptedException:toString	()Ljava/lang/String;
/*      */     //   52: invokestatic 67	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*      */     //   55: dup
/*      */     //   56: invokevirtual 68	java/lang/String:length	()I
/*      */     //   59: ifeq +9 -> 68
/*      */     //   62: invokevirtual 69	java/lang/String:concat	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   65: goto +12 -> 77
/*      */     //   68: pop
/*      */     //   69: new 70	java/lang/String
/*      */     //   72: dup_x1
/*      */     //   73: swap
/*      */     //   74: invokespecial 71	java/lang/String:<init>	(Ljava/lang/String;)V
/*      */     //   77: invokestatic 72	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
/*      */     //   80: pop
/*      */     //   81: aload_0
/*      */     //   82: aconst_null
/*      */     //   83: putfield 3	com/google/vrtoolkit/cardboard/CardboardView:shutdownLatch	Ljava/util/concurrent/CountDownLatch;
/*      */     //   86: aload_0
/*      */     //   87: invokespecial 73	android/opengl/GLSurfaceView:onDetachedFromWindow	()V
/*      */     //   90: return
/*      */     //
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   33	40	43	java/lang/InterruptedException } 
/*  789 */   private void init(Context context) { setEGLContextClientVersion(2);
/*  790 */     setPreserveEGLContextOnPause(true);
/*      */ 
/*  792 */     this.headTracker = HeadTracker.createFromContext(context);
/*  793 */     this.hmdManager = new HeadMountedDisplayManager(context);
/*  794 */     this.rendererHelper = new RendererHelper();
/*  795 */     this.uiLayer = new UiLayer(context);
/*      */   }
/*      */ 
/*      */   public boolean onTouchEvent(MotionEvent e)
/*      */   {
/*  801 */     if (this.uiLayer.onTouchEvent(e)) {
/*  802 */       return true;
/*      */     }
/*  804 */     return super.onTouchEvent(e);
/*      */   }
/*      */ 
/*      */   private class StereoRendererHelper
/*      */     implements CardboardView.Renderer
/*      */   {
/*      */     private final CardboardView.StereoRenderer stereoRenderer;
/*      */     private boolean vrMode;
/*      */ 
/*      */     public StereoRendererHelper(CardboardView.StereoRenderer stereoRenderer)
/*      */     {
/* 1318 */       this.stereoRenderer = stereoRenderer;
/* 1319 */       this.vrMode = CardboardView.this.vrMode;
/*      */     }
/*      */ 
/*      */     public void setVRModeEnabled(final boolean enabled) {
/* 1323 */       CardboardView.this.queueEvent(new Runnable()
/*      */       {
/*      */         public void run() {
/* 1326 */           CardboardView.StereoRendererHelper.this.vrMode = enabled;
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     public void onDrawFrame(HeadTransform head, Eye leftEye, Eye rightEye)
/*      */     {
/* 1333 */       this.stereoRenderer.onNewFrame(head);
/* 1334 */       GLES20.glEnable(3089);
/*      */ 
/* 1336 */       leftEye.getViewport().setGLViewport();
/* 1337 */       leftEye.getViewport().setGLScissor();
/* 1338 */       this.stereoRenderer.onDrawEye(leftEye);
/*      */ 
/* 1341 */       if (rightEye == null) {
/* 1342 */         return;
/*      */       }
/*      */ 
/* 1345 */       rightEye.getViewport().setGLViewport();
/* 1346 */       rightEye.getViewport().setGLScissor();
/* 1347 */       this.stereoRenderer.onDrawEye(rightEye);
/*      */     }
/*      */ 
/*      */     public void onFinishFrame(Viewport viewport)
/*      */     {
/* 1352 */       viewport.setGLViewport();
/* 1353 */       viewport.setGLScissor();
/* 1354 */       this.stereoRenderer.onFinishFrame(viewport);
/*      */     }
/*      */ 
/*      */     public void onSurfaceChanged(int width, int height)
/*      */     {
/* 1359 */       if (this.vrMode)
/*      */       {
/* 1362 */         this.stereoRenderer.onSurfaceChanged(width / 2, height);
/*      */       }
/* 1364 */       else this.stereoRenderer.onSurfaceChanged(width, height);
/*      */     }
/*      */ 
/*      */     public void onSurfaceCreated(EGLConfig config)
/*      */     {
/* 1370 */       this.stereoRenderer.onSurfaceCreated(config);
/*      */     }
/*      */ 
/*      */     public void onRendererShutdown()
/*      */     {
/* 1375 */       this.stereoRenderer.onRendererShutdown();
/*      */     }
/*      */   }
/*      */ 
/*      */   private class RendererHelper
/*      */     implements GLSurfaceView.Renderer
/*      */   {
/*      */     private final HeadTransform headTransform;
/*      */     private final Eye monocular;
/*      */     private final Eye leftEye;
/*      */     private final Eye rightEye;
/*      */     private final float[] leftEyeTranslate;
/*      */     private final float[] rightEyeTranslate;
/*      */     private CardboardView.Renderer renderer;
/*      */     private boolean surfaceCreated;
/*      */     private HeadMountedDisplay hmd;
/*      */     private DistortionRenderer distortionRenderer;
/*      */     private boolean vrMode;
/*      */     private boolean distortionCorrectionEnabled;
/*      */     private boolean projectionChanged;
/*      */     private boolean invalidSurfaceSize;
/*      */ 
/*      */     public RendererHelper()
/*      */     {
/*  834 */       this.hmd = new HeadMountedDisplay(CardboardView.this.getHeadMountedDisplay());
/*  835 */       this.headTransform = new HeadTransform();
/*  836 */       this.monocular = new Eye(0);
/*  837 */       this.leftEye = new Eye(1);
/*  838 */       this.rightEye = new Eye(2);
/*  839 */       updateFieldOfView(this.leftEye.getFov(), this.rightEye.getFov());
/*  840 */       this.distortionRenderer = new DistortionRenderer();
/*  841 */       this.distortionRenderer.setRestoreGLStateEnabled(CardboardView.this.restoreGLStateEnabled);
/*  842 */       this.distortionRenderer.setChromaticAberrationCorrectionEnabled(CardboardView.this.chromaticAberrationCorrectionEnabled);
/*      */ 
/*  844 */       this.distortionRenderer.setVignetteEnabled(CardboardView.this.vignetteEnabled);
/*      */ 
/*  846 */       this.leftEyeTranslate = new float[16];
/*  847 */       this.rightEyeTranslate = new float[16];
/*      */ 
/*  850 */       this.vrMode = CardboardView.this.vrMode;
/*  851 */       this.distortionCorrectionEnabled = CardboardView.this.distortionCorrectionEnabled;
/*      */ 
/*  854 */       this.projectionChanged = true;
/*      */     }
/*      */ 
/*      */     public void setRenderer(CardboardView.Renderer renderer) {
/*  858 */       this.renderer = renderer;
/*      */     }
/*      */ 
/*      */     public void shutdown() {
/*  862 */       CardboardView.this.queueEvent(new Runnable()
/*      */       {
/*      */         public void run() {
/*  865 */           if ((CardboardView.RendererHelper.this.renderer != null) && (CardboardView.RendererHelper.this.surfaceCreated)) {
/*  866 */             CardboardView.RendererHelper.this.surfaceCreated = false;
/*  867 */             CardboardView.RendererHelper.this.renderer.onRendererShutdown();
/*      */           }
/*      */ 
/*  870 */           CardboardView.this.shutdownLatch.countDown();
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     public void setCardboardDeviceParams(CardboardDeviceParams newParams) {
/*  876 */       final CardboardDeviceParams deviceParams = new CardboardDeviceParams(newParams);
/*  877 */       CardboardView.this.queueEvent(new Runnable()
/*      */       {
/*      */         public void run() {
/*  880 */           CardboardView.RendererHelper.this.hmd.setCardboardDeviceParams(deviceParams);
/*  881 */           CardboardView.RendererHelper.this.projectionChanged = true;
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     public void setScreenParams(ScreenParams newParams) {
/*  887 */       final ScreenParams screenParams = new ScreenParams(newParams);
/*  888 */       CardboardView.this.queueEvent(new Runnable()
/*      */       {
/*      */         public void run() {
/*  891 */           CardboardView.RendererHelper.this.hmd.setScreenParams(screenParams);
/*  892 */           CardboardView.RendererHelper.this.projectionChanged = true;
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     public void setDistortionCorrectionEnabled(final boolean enabled) {
/*  898 */       CardboardView.this.queueEvent(new Runnable()
/*      */       {
/*      */         public void run() {
/*  901 */           CardboardView.RendererHelper.this.distortionCorrectionEnabled = enabled;
/*  902 */           CardboardView.RendererHelper.this.projectionChanged = true;
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     public void setDistortionCorrectionScale(final float scale) {
/*  908 */       CardboardView.this.queueEvent(new Runnable()
/*      */       {
/*      */         public void run() {
/*  911 */           CardboardView.RendererHelper.this.distortionRenderer.setResolutionScale(scale);
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     public void setVRModeEnabled(final boolean enabled) {
/*  917 */       CardboardView.this.queueEvent(new Runnable()
/*      */       {
/*      */         public void run() {
/*  920 */           if (CardboardView.RendererHelper.this.vrMode == enabled) {
/*  921 */             return;
/*      */           }
/*      */ 
/*  924 */           CardboardView.RendererHelper.this.vrMode = enabled;
/*      */ 
/*  927 */           if ((CardboardView.RendererHelper.this.renderer instanceof CardboardView.StereoRendererHelper)) {
/*  928 */             CardboardView.StereoRendererHelper stereoHelper = (CardboardView.StereoRendererHelper)CardboardView.RendererHelper.this.renderer;
/*  929 */             stereoHelper.setVRModeEnabled(enabled);
/*      */           }
/*      */ 
/*  934 */           CardboardView.RendererHelper.this.projectionChanged = true;
/*  935 */           CardboardView.RendererHelper.this.onSurfaceChanged((GL10)null, CardboardView.RendererHelper.this.hmd.getScreenParams().getWidth(), CardboardView.RendererHelper.this.hmd.getScreenParams().getHeight());
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     public void setRestoreGLStateEnabled(final boolean enabled)
/*      */     {
/*  943 */       CardboardView.this.queueEvent(new Runnable()
/*      */       {
/*      */         public void run() {
/*  946 */           CardboardView.RendererHelper.this.distortionRenderer.setRestoreGLStateEnabled(enabled);
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     public void setChromaticAberrationCorrectionEnabled(final boolean enabled) {
/*  952 */       CardboardView.this.queueEvent(new Runnable()
/*      */       {
/*      */         public void run() {
/*  955 */           CardboardView.RendererHelper.this.distortionRenderer.setChromaticAberrationCorrectionEnabled(enabled);
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     public void setVignetteEnabled(final boolean enabled) {
/*  961 */       CardboardView.this.queueEvent(new Runnable()
/*      */       {
/*      */         public void run() {
/*  964 */           CardboardView.RendererHelper.this.distortionRenderer.setVignetteEnabled(enabled);
/*  965 */           CardboardView.RendererHelper.this.projectionChanged = true;
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     public void setDistortionCorrectionTextureFormat(final int textureFormat, final int textureType)
/*      */     {
/*  972 */       CardboardView.this.queueEvent(new Runnable()
/*      */       {
/*      */         public void run() {
/*  975 */           CardboardView.RendererHelper.this.distortionRenderer.setTextureFormat(textureFormat, textureType);
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     public void undistortTexture(final int inputTexture) {
/*  981 */       CardboardView.this.queueEvent(new Runnable()
/*      */       {
/*      */         public void run() {
/*  984 */           CardboardView.RendererHelper.this.distortionRenderer.undistortTexture(inputTexture);
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     public void renderUiLayer() {
/*  990 */       CardboardView.this.queueEvent(new Runnable()
/*      */       {
/*      */         public void run() {
/*  993 */           CardboardView.this.uiLayer.draw(); }  } ); } 
/*      */     // ERROR //
/*      */     public void getCurrentEyeParams(final HeadTransform head, final Eye leftEye, final Eye rightEye, final Eye monocular, final Eye leftEyeNoDistortionCorrection, final Eye rightEyeNoDistortionCorrection) { // Byte code:
/*      */       //   0: new 70	java/util/concurrent/CountDownLatch
/*      */       //   3: dup
/*      */       //   4: iconst_1
/*      */       //   5: invokespecial 71	java/util/concurrent/CountDownLatch:<init>	(I)V
/*      */       //   8: astore 7
/*      */       //   10: aload_0
/*      */       //   11: getfield 14	com/google/vrtoolkit/cardboard/CardboardView$RendererHelper:this$0	Lcom/google/vrtoolkit/cardboard/CardboardView;
/*      */       //   14: new 72	com/google/vrtoolkit/cardboard/CardboardView$RendererHelper$13
/*      */       //   17: dup
/*      */       //   18: aload_0
/*      */       //   19: aload_1
/*      */       //   20: aload_2
/*      */       //   21: aload_3
/*      */       //   22: aload 4
/*      */       //   24: aload 5
/*      */       //   26: aload 6
/*      */       //   28: aload 7
/*      */       //   30: invokespecial 73	com/google/vrtoolkit/cardboard/CardboardView$RendererHelper$13:<init>	(Lcom/google/vrtoolkit/cardboard/CardboardView$RendererHelper;Lcom/google/vrtoolkit/cardboard/HeadTransform;Lcom/google/vrtoolkit/cardboard/Eye;Lcom/google/vrtoolkit/cardboard/Eye;Lcom/google/vrtoolkit/cardboard/Eye;Lcom/google/vrtoolkit/cardboard/Eye;Lcom/google/vrtoolkit/cardboard/Eye;Ljava/util/concurrent/CountDownLatch;)V
/*      */       //   33: invokevirtual 43	com/google/vrtoolkit/cardboard/CardboardView:queueEvent	(Ljava/lang/Runnable;)V
/*      */       //   36: aload 7
/*      */       //   38: invokevirtual 74	java/util/concurrent/CountDownLatch:await	()V
/*      */       //   41: goto +43 -> 84
/*      */       //   44: astore 8
/*      */       //   46: ldc 76
/*      */       //   48: ldc 77
/*      */       //   50: aload 8
/*      */       //   52: invokevirtual 78	java/lang/InterruptedException:toString	()Ljava/lang/String;
/*      */       //   55: invokestatic 79	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*      */       //   58: dup
/*      */       //   59: invokevirtual 80	java/lang/String:length	()I
/*      */       //   62: ifeq +9 -> 71
/*      */       //   65: invokevirtual 81	java/lang/String:concat	(Ljava/lang/String;)Ljava/lang/String;
/*      */       //   68: goto +12 -> 80
/*      */       //   71: pop
/*      */       //   72: new 82	java/lang/String
/*      */       //   75: dup_x1
/*      */       //   76: swap
/*      */       //   77: invokespecial 83	java/lang/String:<init>	(Ljava/lang/String;)V
/*      */       //   80: invokestatic 84	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
/*      */       //   83: pop
/*      */       //   84: return
/*      */       //
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   36	41	44	java/lang/InterruptedException } 
/* 1024 */     private void getFrameParams(HeadTransform head, Eye leftEye, Eye rightEye, Eye monocular) { CardboardDeviceParams cdp = this.hmd.getCardboardDeviceParams();
/* 1025 */       ScreenParams screen = this.hmd.getScreenParams();
/*      */ 
/* 1028 */       CardboardView.this.headTracker.getLastHeadView(head.getHeadView(), 0);
/*      */ 
/* 1035 */       float halfInterpupillaryDistance = cdp.getInterLensDistance() * 0.5F;
/*      */ 
/* 1037 */       if (this.vrMode)
/*      */       {
/* 1039 */         Matrix.setIdentityM(this.leftEyeTranslate, 0);
/* 1040 */         Matrix.setIdentityM(this.rightEyeTranslate, 0);
/*      */ 
/* 1042 */         Matrix.translateM(this.leftEyeTranslate, 0, halfInterpupillaryDistance, 0.0F, 0.0F);
/*      */ 
/* 1044 */         Matrix.translateM(this.rightEyeTranslate, 0, -halfInterpupillaryDistance, 0.0F, 0.0F);
/*      */ 
/* 1048 */         Matrix.multiplyMM(leftEye.getEyeView(), 0, this.leftEyeTranslate, 0, head.getHeadView(), 0);
/*      */ 
/* 1051 */         Matrix.multiplyMM(rightEye.getEyeView(), 0, this.rightEyeTranslate, 0, head.getHeadView(), 0);
/*      */       }
/*      */       else
/*      */       {
/* 1057 */         System.arraycopy(head.getHeadView(), 0, monocular.getEyeView(), 0, head.getHeadView().length);
/*      */       }
/*      */ 
/* 1063 */       if (this.projectionChanged)
/*      */       {
/* 1066 */         monocular.getViewport().setViewport(0, 0, screen.getWidth(), screen.getHeight());
/*      */ 
/* 1068 */         CardboardView.this.uiLayer.updateViewport(monocular.getViewport());
/*      */ 
/* 1070 */         if (!this.vrMode) {
/* 1071 */           updateMonocularFieldOfView(monocular.getFov());
/*      */         } else {
/* 1073 */           updateFieldOfView(leftEye.getFov(), rightEye.getFov());
/* 1074 */           if (this.distortionCorrectionEnabled) {
/* 1075 */             this.distortionRenderer.onFovChanged(this.hmd, leftEye.getFov(), rightEye.getFov(), getVirtualEyeToScreenDistance());
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1081 */         leftEye.setProjectionChanged();
/* 1082 */         rightEye.setProjectionChanged();
/* 1083 */         monocular.setProjectionChanged();
/* 1084 */         this.projectionChanged = false;
/*      */       }
/*      */ 
/* 1088 */       if ((this.distortionCorrectionEnabled) && (this.distortionRenderer.haveViewportsChanged()))
/* 1089 */         this.distortionRenderer.updateViewports(leftEye.getViewport(), rightEye.getViewport());
/*      */     }
/*      */ 
/*      */     public void onDrawFrame(GL10 gl)
/*      */     {
/* 1095 */       if ((this.renderer == null) || (!this.surfaceCreated) || (this.invalidSurfaceSize)) {
/* 1096 */         return;
/*      */       }
/*      */ 
/* 1099 */       getFrameParams(this.headTransform, this.leftEye, this.rightEye, this.monocular);
/*      */ 
/* 1101 */       if (this.vrMode) {
/* 1102 */         if (this.distortionCorrectionEnabled) {
/* 1103 */           this.distortionRenderer.beforeDrawFrame();
/* 1104 */           this.renderer.onDrawFrame(this.headTransform, this.leftEye, this.rightEye);
/* 1105 */           this.distortionRenderer.afterDrawFrame();
/*      */         } else {
/* 1107 */           this.renderer.onDrawFrame(this.headTransform, this.leftEye, this.rightEye);
/*      */         }
/*      */       }
/* 1110 */       else this.renderer.onDrawFrame(this.headTransform, this.monocular, null);
/*      */ 
/* 1113 */       this.renderer.onFinishFrame(this.monocular.getViewport());
/* 1114 */       if (this.vrMode)
/* 1115 */         CardboardView.this.uiLayer.draw();
/*      */     }
/*      */ 
/*      */     public void onSurfaceChanged(GL10 gl, int width, int height)
/*      */     {
/* 1121 */       if ((this.renderer == null) || (!this.surfaceCreated)) {
/* 1122 */         return;
/*      */       }
/*      */ 
/* 1126 */       ScreenParams screen = this.hmd.getScreenParams();
/* 1127 */       if ((width != screen.getWidth()) || (height != screen.getHeight())) {
/* 1128 */         if (!this.invalidSurfaceSize) {
/* 1129 */           GLES20.glClear(16384);
/* 1130 */           int i = width; int j = height; int k = screen.getWidth(); int m = screen.getHeight(); Log.w("CardboardView", 124 + "Surface size " + i + "x" + j + " does not match the expected screen size " + k + "x" + m + ". Rendering is disabled.");
/*      */         }
/*      */ 
/* 1136 */         this.invalidSurfaceSize = true;
/*      */       } else {
/* 1138 */         this.invalidSurfaceSize = false;
/*      */       }
/*      */ 
/* 1143 */       this.renderer.onSurfaceChanged(width, height);
/*      */     }
/*      */ 
/*      */     public void onSurfaceCreated(GL10 gl, EGLConfig config)
/*      */     {
/* 1148 */       if (this.renderer == null) {
/* 1149 */         return;
/*      */       }
/*      */ 
/* 1152 */       this.surfaceCreated = true;
/* 1153 */       this.renderer.onSurfaceCreated(config);
/* 1154 */       CardboardView.this.uiLayer.initializeGl();
/*      */     }
/*      */ 
/*      */     private void updateFieldOfView(FieldOfView leftEyeFov, FieldOfView rightEyeFov) {
/* 1158 */       CardboardDeviceParams cdp = this.hmd.getCardboardDeviceParams();
/* 1159 */       ScreenParams screen = this.hmd.getScreenParams();
/* 1160 */       Distortion distortion = cdp.getDistortion();
/*      */ 
/* 1174 */       float eyeToScreenDist = getVirtualEyeToScreenDistance();
/*      */ 
/* 1176 */       float outerDist = (screen.getWidthMeters() - cdp.getInterLensDistance()) / 2.0F;
/*      */ 
/* 1178 */       float innerDist = cdp.getInterLensDistance() / 2.0F;
/* 1179 */       float bottomDist = cdp.getYEyeOffsetMeters(screen);
/* 1180 */       float topDist = screen.getHeightMeters() - bottomDist;
/*      */ 
/* 1183 */       float outerAngle = (float)Math.toDegrees(Math.atan(distortion.distort(outerDist / eyeToScreenDist)));
/*      */ 
/* 1185 */       float innerAngle = (float)Math.toDegrees(Math.atan(distortion.distort(innerDist / eyeToScreenDist)));
/*      */ 
/* 1187 */       float bottomAngle = (float)Math.toDegrees(Math.atan(distortion.distort(bottomDist / eyeToScreenDist)));
/*      */ 
/* 1189 */       float topAngle = (float)Math.toDegrees(Math.atan(distortion.distort(topDist / eyeToScreenDist)));
/*      */ 
/* 1192 */       leftEyeFov.setLeft(Math.min(outerAngle, cdp.getLeftEyeMaxFov().getLeft()));
/* 1193 */       leftEyeFov.setRight(Math.min(innerAngle, cdp.getLeftEyeMaxFov().getRight()));
/* 1194 */       leftEyeFov.setBottom(Math.min(bottomAngle, cdp.getLeftEyeMaxFov().getBottom()));
/* 1195 */       leftEyeFov.setTop(Math.min(topAngle, cdp.getLeftEyeMaxFov().getTop()));
/*      */ 
/* 1198 */       rightEyeFov.setLeft(leftEyeFov.getRight());
/* 1199 */       rightEyeFov.setRight(leftEyeFov.getLeft());
/* 1200 */       rightEyeFov.setBottom(leftEyeFov.getBottom());
/* 1201 */       rightEyeFov.setTop(leftEyeFov.getTop());
/*      */     }
/*      */ 
/*      */     private void updateMonocularFieldOfView(FieldOfView monocularFov) {
/* 1205 */       ScreenParams screen = this.hmd.getScreenParams();
/*      */ 
/* 1210 */       float monocularBottomFov = 22.5F;
/* 1211 */       float monocularLeftFov = (float)Math.toDegrees(Math.atan(Math.tan(Math.toRadians(monocularBottomFov)) * screen.getWidthMeters() / screen.getHeightMeters()));
/*      */ 
/* 1215 */       monocularFov.setLeft(monocularLeftFov);
/* 1216 */       monocularFov.setRight(monocularLeftFov);
/* 1217 */       monocularFov.setBottom(monocularBottomFov);
/* 1218 */       monocularFov.setTop(monocularBottomFov);
/*      */     }
/*      */ 
/*      */     private void getFovAndViewportNoDistortionCorrection(Eye leftEye, Eye rightEye)
/*      */     {
/* 1231 */       ScreenParams screen = this.hmd.getScreenParams();
/* 1232 */       CardboardDeviceParams cdp = this.hmd.getCardboardDeviceParams();
/* 1233 */       Distortion distortion = cdp.getDistortion();
/*      */ 
/* 1245 */       float eyeToScreenDistMeters = getVirtualEyeToScreenDistance();
/* 1246 */       float halfLensDistance = cdp.getInterLensDistance() / 2.0F / eyeToScreenDistMeters;
/* 1247 */       float screenWidth = screen.getWidthMeters() / eyeToScreenDistMeters;
/* 1248 */       float screenHeight = screen.getHeightMeters() / eyeToScreenDistMeters;
/* 1249 */       float xPxPerTanAngle = screen.getWidth() / screenWidth;
/* 1250 */       float yPxPerTanAngle = screen.getHeight() / screenHeight;
/*      */ 
/* 1252 */       float eyePosX = screenWidth / 2.0F - halfLensDistance;
/* 1253 */       float eyePosY = cdp.getYEyeOffsetMeters(screen) / eyeToScreenDistMeters;
/*      */ 
/* 1255 */       FieldOfView maxFov = cdp.getLeftEyeMaxFov();
/* 1256 */       float outerDist = Math.min(eyePosX, distortion.distortInverse((float)Math.tan(Math.toRadians(maxFov.getLeft()))));
/*      */ 
/* 1258 */       float innerDist = Math.min(halfLensDistance, distortion.distortInverse((float)Math.tan(Math.toRadians(maxFov.getRight()))));
/*      */ 
/* 1260 */       float bottomDist = Math.min(eyePosY, distortion.distortInverse((float)Math.tan(Math.toRadians(maxFov.getBottom()))));
/*      */ 
/* 1262 */       float topDist = Math.min(screenHeight - eyePosY, distortion.distortInverse((float)Math.tan(Math.toRadians(maxFov.getTop()))));
/*      */ 
/* 1265 */       FieldOfView leftEyeFov = leftEye.getFov();
/* 1266 */       leftEyeFov.setLeft((float)Math.toDegrees(Math.atan(outerDist)));
/* 1267 */       leftEyeFov.setRight((float)Math.toDegrees(Math.atan(innerDist)));
/* 1268 */       leftEyeFov.setBottom((float)Math.toDegrees(Math.atan(bottomDist)));
/* 1269 */       leftEyeFov.setTop((float)Math.toDegrees(Math.atan(topDist)));
/*      */ 
/* 1271 */       Viewport leftViewport = leftEye.getViewport();
/* 1272 */       leftViewport.x = ((int)((eyePosX - outerDist) * xPxPerTanAngle + 0.5F));
/* 1273 */       leftViewport.width = ((int)((eyePosX + innerDist) * xPxPerTanAngle + 0.5F) - leftViewport.x);
/*      */ 
/* 1275 */       leftViewport.y = ((int)((eyePosY - bottomDist) * yPxPerTanAngle + 0.5F));
/* 1276 */       leftViewport.height = ((int)((eyePosY + topDist) * yPxPerTanAngle + 0.5F) - leftViewport.y);
/*      */ 
/* 1279 */       leftEye.setProjectionChanged();
/*      */ 
/* 1284 */       FieldOfView rightEyeFov = rightEye.getFov();
/* 1285 */       rightEyeFov.setLeft(leftEyeFov.getRight());
/* 1286 */       rightEyeFov.setRight(leftEyeFov.getLeft());
/* 1287 */       rightEyeFov.setBottom(leftEyeFov.getBottom());
/* 1288 */       rightEyeFov.setTop(leftEyeFov.getTop());
/*      */ 
/* 1290 */       Viewport rightViewport = rightEye.getViewport();
/* 1291 */       rightViewport.width = leftViewport.width;
/* 1292 */       rightViewport.height = leftViewport.height;
/* 1293 */       rightViewport.x = (screen.getWidth() - leftViewport.x - rightViewport.width);
/* 1294 */       rightViewport.y = leftViewport.y;
/*      */ 
/* 1296 */       rightEye.setProjectionChanged();
/*      */     }
/*      */ 
/*      */     private float getVirtualEyeToScreenDistance()
/*      */     {
/* 1304 */       return this.hmd.getCardboardDeviceParams().getScreenToLensDistance();
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