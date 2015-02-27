/*     */ package com.google.vrtoolkit.cardboard;
/*     */ 
/*     */ import android.net.Uri;
/*     */ import android.net.Uri.Builder;
/*     */ import android.nfc.NdefMessage;
/*     */ import android.nfc.NdefRecord;
/*     */ import android.util.Base64;
/*     */ import android.util.Log;
/*     */ import com.google.protobuf.nano.MessageNano;
/*     */ import com.google.vrtoolkit.cardboard.proto.CardboardDevice.DeviceParams;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ 
/*     */ public class CardboardDeviceParams
/*     */ {
/*     */   private static final String TAG = "CardboardDeviceParams";
/*     */   private static final String HTTP_SCHEME = "http";
/*     */   private static final String URI_HOST_GOOGLE_SHORT = "g.co";
/*     */   private static final String URI_HOST_GOOGLE = "google.com";
/*     */   private static final String URI_PATH_CARDBOARD_HOME = "cardboard";
/*     */   private static final String URI_PATH_CARDBOARD_CONFIG = "cardboard/cfg";
/*     */   private static final String URI_SCHEME_LEGACY_CARDBOARD = "cardboard";
/*     */   private static final String URI_HOST_LEGACY_CARDBOARD = "v1.0.0";
/*  73 */   private static final Uri URI_ORIGINAL_CARDBOARD_NFC = new Uri.Builder().scheme("cardboard").authority("v1.0.0").build();
/*     */ 
/*  79 */   private static final Uri URI_ORIGINAL_CARDBOARD_QR_CODE = new Uri.Builder().scheme("http").authority("g.co").appendEncodedPath("cardboard").build();
/*     */   private static final String URI_KEY_PARAMS = "p";
/*     */   private static final int STREAM_SENTINEL = 894990891;
/*     */   private static final String DEFAULT_VENDOR = "Google, Inc.";
/*     */   private static final String DEFAULT_MODEL = "Cardboard v1";
/*     */   private static final float DEFAULT_INTER_LENS_DISTANCE = 0.06F;
/*     */   private static final float DEFAULT_VERTICAL_DISTANCE_TO_LENS_CENTER = 0.035F;
/*     */   private static final float DEFAULT_SCREEN_TO_LENS_DISTANCE = 0.042F;
/*     */   private String mVendor;
/*     */   private String mModel;
/*     */   private float mInterLensDistance;
/*     */   private float mVerticalDistanceToLensCenter;
/*     */   private float mScreenToLensDistance;
/*     */   private FieldOfView mLeftEyeMaxFov;
/*     */   private boolean mHasMagnet;
/*     */   private Distortion mDistortion;
/*     */ 
/*     */   public CardboardDeviceParams()
/*     */   {
/* 126 */     setDefaultValues();
/*     */   }
/*     */ 
/*     */   public CardboardDeviceParams(CardboardDeviceParams params)
/*     */   {
/* 135 */     copyFrom(params);
/*     */   }
/*     */ 
/*     */   public CardboardDeviceParams(CardboardDevice.DeviceParams params)
/*     */   {
/* 145 */     setDefaultValues();
/*     */ 
/* 147 */     if (params == null) {
/* 148 */       return;
/*     */     }
/*     */ 
/* 151 */     this.mVendor = params.getVendor();
/* 152 */     this.mModel = params.getModel();
/*     */ 
/* 154 */     this.mInterLensDistance = params.getInterLensDistance();
/* 155 */     this.mVerticalDistanceToLensCenter = params.getTrayBottomToLensHeight();
/* 156 */     this.mScreenToLensDistance = params.getScreenToLensDistance();
/*     */ 
/* 158 */     this.mLeftEyeMaxFov = FieldOfView.parseFromProtobuf(params.leftEyeFieldOfViewAngles);
/* 159 */     if (this.mLeftEyeMaxFov == null) {
/* 160 */       this.mLeftEyeMaxFov = new FieldOfView();
/*     */     }
/*     */ 
/* 163 */     this.mDistortion = Distortion.parseFromProtobuf(params.distortionCoefficients);
/* 164 */     if (this.mDistortion == null) {
/* 165 */       this.mDistortion = new Distortion();
/*     */     }
/*     */ 
/* 168 */     this.mHasMagnet = params.getHasMagnet();
/*     */   }
/*     */ 
/*     */   public static boolean isOriginalCardboardDeviceUri(Uri uri)
/*     */   {
/* 178 */     return (URI_ORIGINAL_CARDBOARD_QR_CODE.equals(uri)) || ((URI_ORIGINAL_CARDBOARD_NFC.getScheme().equals(uri.getScheme())) && (URI_ORIGINAL_CARDBOARD_NFC.getAuthority().equals(uri.getAuthority())));
/*     */   }
/*     */ 
/*     */   private static boolean isCardboardDeviceUri(Uri uri)
/*     */   {
/* 185 */     return ("http".equals(uri.getScheme())) && ("google.com".equals(uri.getAuthority())) && ("/cardboard/cfg".equals(uri.getPath()));
/*     */   }
/*     */ 
/*     */   public static boolean isCardboardUri(Uri uri)
/*     */   {
/* 192 */     return (isOriginalCardboardDeviceUri(uri)) || (isCardboardDeviceUri(uri)); } 
/*     */   // ERROR //
/*     */   public static CardboardDeviceParams createFromUri(Uri uri) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: ifnonnull +5 -> 6
/*     */     //   4: aconst_null
/*     */     //   5: areturn
/*     */     //   6: aload_0
/*     */     //   7: invokestatic 41	com/google/vrtoolkit/cardboard/CardboardDeviceParams:isOriginalCardboardDeviceUri	(Landroid/net/Uri;)Z
/*     */     //   10: ifeq +25 -> 35
/*     */     //   13: ldc 43
/*     */     //   15: ldc 44
/*     */     //   17: invokestatic 45	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
/*     */     //   20: pop
/*     */     //   21: new 46	com/google/vrtoolkit/cardboard/CardboardDeviceParams
/*     */     //   24: dup
/*     */     //   25: invokespecial 47	com/google/vrtoolkit/cardboard/CardboardDeviceParams:<init>	()V
/*     */     //   28: astore_1
/*     */     //   29: aload_1
/*     */     //   30: invokespecial 7	com/google/vrtoolkit/cardboard/CardboardDeviceParams:setDefaultValues	()V
/*     */     //   33: aload_1
/*     */     //   34: areturn
/*     */     //   35: aload_0
/*     */     //   36: invokestatic 42	com/google/vrtoolkit/cardboard/CardboardDeviceParams:isCardboardDeviceUri	(Landroid/net/Uri;)Z
/*     */     //   39: ifne +24 -> 63
/*     */     //   42: ldc 43
/*     */     //   44: ldc 48
/*     */     //   46: iconst_1
/*     */     //   47: anewarray 49	java/lang/Object
/*     */     //   50: dup
/*     */     //   51: iconst_0
/*     */     //   52: aload_0
/*     */     //   53: aastore
/*     */     //   54: invokestatic 50	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   57: invokestatic 51	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
/*     */     //   60: pop
/*     */     //   61: aconst_null
/*     */     //   62: areturn
/*     */     //   63: aconst_null
/*     */     //   64: astore_1
/*     */     //   65: aload_0
/*     */     //   66: ldc 52
/*     */     //   68: invokevirtual 53	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
/*     */     //   71: astore_2
/*     */     //   72: aload_2
/*     */     //   73: ifnull +77 -> 150
/*     */     //   76: aload_2
/*     */     //   77: bipush 11
/*     */     //   79: invokestatic 54	android/util/Base64:decode	(Ljava/lang/String;I)[B
/*     */     //   82: astore_3
/*     */     //   83: new 55	com/google/vrtoolkit/cardboard/proto/CardboardDevice$DeviceParams
/*     */     //   86: dup
/*     */     //   87: invokespecial 56	com/google/vrtoolkit/cardboard/proto/CardboardDevice$DeviceParams:<init>	()V
/*     */     //   90: aload_3
/*     */     //   91: invokestatic 57	com/google/protobuf/nano/MessageNano:mergeFrom	(Lcom/google/protobuf/nano/MessageNano;[B)Lcom/google/protobuf/nano/MessageNano;
/*     */     //   94: checkcast 55	com/google/vrtoolkit/cardboard/proto/CardboardDevice$DeviceParams
/*     */     //   97: astore_1
/*     */     //   98: ldc 43
/*     */     //   100: ldc 58
/*     */     //   102: invokestatic 45	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
/*     */     //   105: pop
/*     */     //   106: goto +52 -> 158
/*     */     //   109: astore_3
/*     */     //   110: ldc 43
/*     */     //   112: ldc 60
/*     */     //   114: aload_3
/*     */     //   115: invokevirtual 61	java/lang/Exception:toString	()Ljava/lang/String;
/*     */     //   118: invokestatic 62	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   121: dup
/*     */     //   122: invokevirtual 63	java/lang/String:length	()I
/*     */     //   125: ifeq +9 -> 134
/*     */     //   128: invokevirtual 64	java/lang/String:concat	(Ljava/lang/String;)Ljava/lang/String;
/*     */     //   131: goto +12 -> 143
/*     */     //   134: pop
/*     */     //   135: new 65	java/lang/String
/*     */     //   138: dup_x1
/*     */     //   139: swap
/*     */     //   140: invokespecial 66	java/lang/String:<init>	(Ljava/lang/String;)V
/*     */     //   143: invokestatic 51	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
/*     */     //   146: pop
/*     */     //   147: goto +11 -> 158
/*     */     //   150: ldc 43
/*     */     //   152: ldc 67
/*     */     //   154: invokestatic 51	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
/*     */     //   157: pop
/*     */     //   158: new 46	com/google/vrtoolkit/cardboard/CardboardDeviceParams
/*     */     //   161: dup
/*     */     //   162: aload_1
/*     */     //   163: invokespecial 68	com/google/vrtoolkit/cardboard/CardboardDeviceParams:<init>	(Lcom/google/vrtoolkit/cardboard/proto/CardboardDevice$DeviceParams;)V
/*     */     //   166: areturn
/*     */     //
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   76	106	109	java/lang/Exception } 
/*     */   // ERROR //
/*     */   public static CardboardDeviceParams createFromInputStream(java.io.InputStream inputStream) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: ifnonnull +5 -> 6
/*     */     //   4: aconst_null
/*     */     //   5: areturn
/*     */     //   6: bipush 8
/*     */     //   8: invokestatic 69	java/nio/ByteBuffer:allocate	(I)Ljava/nio/ByteBuffer;
/*     */     //   11: astore_1
/*     */     //   12: aload_0
/*     */     //   13: aload_1
/*     */     //   14: invokevirtual 70	java/nio/ByteBuffer:array	()[B
/*     */     //   17: iconst_0
/*     */     //   18: aload_1
/*     */     //   19: invokevirtual 70	java/nio/ByteBuffer:array	()[B
/*     */     //   22: arraylength
/*     */     //   23: invokevirtual 71	java/io/InputStream:read	([BII)I
/*     */     //   26: iconst_m1
/*     */     //   27: if_icmpne +13 -> 40
/*     */     //   30: ldc 43
/*     */     //   32: ldc 72
/*     */     //   34: invokestatic 73	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
/*     */     //   37: pop
/*     */     //   38: aconst_null
/*     */     //   39: areturn
/*     */     //   40: aload_1
/*     */     //   41: invokevirtual 74	java/nio/ByteBuffer:getInt	()I
/*     */     //   44: istore_2
/*     */     //   45: aload_1
/*     */     //   46: invokevirtual 74	java/nio/ByteBuffer:getInt	()I
/*     */     //   49: istore_3
/*     */     //   50: iload_2
/*     */     //   51: ldc 75
/*     */     //   53: if_icmpeq +13 -> 66
/*     */     //   56: ldc 43
/*     */     //   58: ldc 76
/*     */     //   60: invokestatic 73	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
/*     */     //   63: pop
/*     */     //   64: aconst_null
/*     */     //   65: areturn
/*     */     //   66: iload_3
/*     */     //   67: newarray byte
/*     */     //   69: astore 4
/*     */     //   71: aload_0
/*     */     //   72: aload 4
/*     */     //   74: iconst_0
/*     */     //   75: aload 4
/*     */     //   77: arraylength
/*     */     //   78: invokevirtual 71	java/io/InputStream:read	([BII)I
/*     */     //   81: iconst_m1
/*     */     //   82: if_icmpne +13 -> 95
/*     */     //   85: ldc 43
/*     */     //   87: ldc 72
/*     */     //   89: invokestatic 73	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
/*     */     //   92: pop
/*     */     //   93: aconst_null
/*     */     //   94: areturn
/*     */     //   95: new 46	com/google/vrtoolkit/cardboard/CardboardDeviceParams
/*     */     //   98: dup
/*     */     //   99: new 55	com/google/vrtoolkit/cardboard/proto/CardboardDevice$DeviceParams
/*     */     //   102: dup
/*     */     //   103: invokespecial 56	com/google/vrtoolkit/cardboard/proto/CardboardDevice$DeviceParams:<init>	()V
/*     */     //   106: aload 4
/*     */     //   108: invokestatic 57	com/google/protobuf/nano/MessageNano:mergeFrom	(Lcom/google/protobuf/nano/MessageNano;[B)Lcom/google/protobuf/nano/MessageNano;
/*     */     //   111: checkcast 55	com/google/vrtoolkit/cardboard/proto/CardboardDevice$DeviceParams
/*     */     //   114: invokespecial 68	com/google/vrtoolkit/cardboard/CardboardDeviceParams:<init>	(Lcom/google/vrtoolkit/cardboard/proto/CardboardDevice$DeviceParams;)V
/*     */     //   117: areturn
/*     */     //   118: astore_1
/*     */     //   119: ldc 43
/*     */     //   121: ldc 78
/*     */     //   123: aload_1
/*     */     //   124: invokevirtual 79	com/google/protobuf/nano/InvalidProtocolBufferNanoException:toString	()Ljava/lang/String;
/*     */     //   127: invokestatic 62	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   130: dup
/*     */     //   131: invokevirtual 63	java/lang/String:length	()I
/*     */     //   134: ifeq +9 -> 143
/*     */     //   137: invokevirtual 64	java/lang/String:concat	(Ljava/lang/String;)Ljava/lang/String;
/*     */     //   140: goto +12 -> 152
/*     */     //   143: pop
/*     */     //   144: new 65	java/lang/String
/*     */     //   147: dup_x1
/*     */     //   148: swap
/*     */     //   149: invokespecial 66	java/lang/String:<init>	(Ljava/lang/String;)V
/*     */     //   152: invokestatic 51	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
/*     */     //   155: pop
/*     */     //   156: goto +41 -> 197
/*     */     //   159: astore_1
/*     */     //   160: ldc 43
/*     */     //   162: ldc 81
/*     */     //   164: aload_1
/*     */     //   165: invokevirtual 82	java/io/IOException:toString	()Ljava/lang/String;
/*     */     //   168: invokestatic 62	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   171: dup
/*     */     //   172: invokevirtual 63	java/lang/String:length	()I
/*     */     //   175: ifeq +9 -> 184
/*     */     //   178: invokevirtual 64	java/lang/String:concat	(Ljava/lang/String;)Ljava/lang/String;
/*     */     //   181: goto +12 -> 193
/*     */     //   184: pop
/*     */     //   185: new 65	java/lang/String
/*     */     //   188: dup_x1
/*     */     //   189: swap
/*     */     //   190: invokespecial 66	java/lang/String:<init>	(Ljava/lang/String;)V
/*     */     //   193: invokestatic 51	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
/*     */     //   196: pop
/*     */     //   197: aconst_null
/*     */     //   198: areturn
/*     */     //
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   6	39	118	com/google/protobuf/nano/InvalidProtocolBufferNanoException
/*     */     //   40	65	118	com/google/protobuf/nano/InvalidProtocolBufferNanoException
/*     */     //   66	94	118	com/google/protobuf/nano/InvalidProtocolBufferNanoException
/*     */     //   95	117	118	com/google/protobuf/nano/InvalidProtocolBufferNanoException
/*     */     //   6	39	159	java/io/IOException
/*     */     //   40	65	159	java/io/IOException
/*     */     //   66	94	159	java/io/IOException
/*     */     //   95	117	159	java/io/IOException } 
/*     */   public boolean writeToOutputStream(OutputStream outputStream) { try { byte[] paramBytes = toByteArray();
/* 292 */       ByteBuffer header = ByteBuffer.allocate(8);
/* 293 */       header.putInt(894990891);
/* 294 */       header.putInt(paramBytes.length);
/* 295 */       outputStream.write(header.array());
/* 296 */       outputStream.write(paramBytes);
/* 297 */       return true;
/*     */     } catch (IOException e) {
/* 299 */       "CardboardDeviceParams";
/*     */     }
/*     */   }
/*     */ 
/*     */   public static CardboardDeviceParams createFromNfcContents(NdefMessage tagContents)
/*     */   {
/* 311 */     if (tagContents == null) {
/* 312 */       Log.w("CardboardDeviceParams", "Could not get contents from NFC tag.");
/* 313 */       return null;
/*     */     }
/*     */ 
/* 316 */     for (NdefRecord record : tagContents.getRecords()) {
/* 317 */       CardboardDeviceParams params = createFromUri(record.toUri());
/*     */ 
/* 319 */       if (params != null) {
/* 320 */         return params;
/*     */       }
/*     */     }
/*     */ 
/* 324 */     return null;
/*     */   }
/*     */ 
/*     */   private byte[] toByteArray()
/*     */   {
/* 333 */     CardboardDevice.DeviceParams params = new CardboardDevice.DeviceParams();
/*     */ 
/* 335 */     params.setVendor(this.mVendor);
/* 336 */     params.setModel(this.mModel);
/* 337 */     params.setInterLensDistance(this.mInterLensDistance);
/* 338 */     params.setTrayBottomToLensHeight(this.mVerticalDistanceToLensCenter);
/* 339 */     params.setScreenToLensDistance(this.mScreenToLensDistance);
/* 340 */     params.leftEyeFieldOfViewAngles = this.mLeftEyeMaxFov.toProtobuf();
/* 341 */     params.distortionCoefficients = this.mDistortion.toProtobuf();
/*     */ 
/* 343 */     if (this.mHasMagnet) {
/* 344 */       params.setHasMagnet(this.mHasMagnet);
/*     */     }
/*     */ 
/* 347 */     return MessageNano.toByteArray(params);
/*     */   }
/*     */ 
/*     */   public Uri toUri()
/*     */   {
/* 362 */     byte[] paramsData = toByteArray();
/* 363 */     int paramsSize = paramsData.length;
/*     */ 
/* 365 */     return new Uri.Builder().scheme("http").authority("google.com").appendEncodedPath("cardboard/cfg").appendQueryParameter("p", Base64.encodeToString(paramsData, 0, paramsSize, 11)).build();
/*     */   }
/*     */ 
/*     */   public void setVendor(String vendor)
/*     */   {
/* 381 */     this.mVendor = (vendor != null ? vendor : "");
/*     */   }
/*     */ 
/*     */   public String getVendor()
/*     */   {
/* 390 */     return this.mVendor;
/*     */   }
/*     */ 
/*     */   public void setModel(String model)
/*     */   {
/* 399 */     this.mModel = (model != null ? model : "");
/*     */   }
/*     */ 
/*     */   public String getModel()
/*     */   {
/* 408 */     return this.mModel;
/*     */   }
/*     */ 
/*     */   public void setInterLensDistance(float interLensDistance)
/*     */   {
/* 417 */     this.mInterLensDistance = interLensDistance;
/*     */   }
/*     */ 
/*     */   public float getInterLensDistance()
/*     */   {
/* 426 */     return this.mInterLensDistance;
/*     */   }
/*     */ 
/*     */   public void setVerticalDistanceToLensCenter(float verticalDistanceToLensCenter)
/*     */   {
/* 436 */     this.mVerticalDistanceToLensCenter = verticalDistanceToLensCenter;
/*     */   }
/*     */ 
/*     */   public float getVerticalDistanceToLensCenter()
/*     */   {
/* 446 */     return this.mVerticalDistanceToLensCenter;
/*     */   }
/*     */ 
/*     */   public void setScreenToLensDistance(float screenToLensDistance)
/*     */   {
/* 457 */     this.mScreenToLensDistance = screenToLensDistance;
/*     */   }
/*     */ 
/*     */   public float getScreenToLensDistance()
/*     */   {
/* 466 */     return this.mScreenToLensDistance;
/*     */   }
/*     */ 
/*     */   public Distortion getDistortion()
/*     */   {
/* 475 */     return this.mDistortion;
/*     */   }
/*     */ 
/*     */   public FieldOfView getLeftEyeMaxFov()
/*     */   {
/* 487 */     return this.mLeftEyeMaxFov;
/*     */   }
/*     */ 
/*     */   public boolean getHasMagnet()
/*     */   {
/* 496 */     return this.mHasMagnet;
/*     */   }
/*     */ 
/*     */   public void setHasMagnet(boolean magnet)
/*     */   {
/* 505 */     this.mHasMagnet = magnet;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 516 */     if (other == null) {
/* 517 */       return false;
/*     */     }
/*     */ 
/* 520 */     if (other == this) {
/* 521 */       return true;
/*     */     }
/*     */ 
/* 524 */     if (!(other instanceof CardboardDeviceParams)) {
/* 525 */       return false;
/*     */     }
/*     */ 
/* 528 */     CardboardDeviceParams o = (CardboardDeviceParams)other;
/*     */ 
/* 531 */     return (this.mVendor.equals(o.mVendor)) && (this.mModel.equals(o.mModel)) && (this.mInterLensDistance == o.mInterLensDistance) && (this.mVerticalDistanceToLensCenter == o.mVerticalDistanceToLensCenter) && (this.mScreenToLensDistance == o.mScreenToLensDistance) && (this.mLeftEyeMaxFov.equals(o.mLeftEyeMaxFov)) && (this.mDistortion.equals(o.mDistortion)) && (this.mHasMagnet == o.mHasMagnet);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 548 */     String str1 = String.valueOf(String.valueOf(this.mVendor)); String str2 = String.valueOf(String.valueOf(this.mModel)); float f1 = this.mInterLensDistance; float f2 = this.mVerticalDistanceToLensCenter; float f3 = this.mScreenToLensDistance; String str3 = String.valueOf(String.valueOf(this.mLeftEyeMaxFov.toString().replace("\n", "\n  "))); String str4 = String.valueOf(String.valueOf(this.mDistortion.toString().replace("\n", "\n  "))); boolean bool = this.mHasMagnet; return "{\n" + new StringBuilder(12 + str1.length()).append("  vendor: ").append(str1).append(",\n").toString() + new StringBuilder(11 + str2.length()).append("  model: ").append(str2).append(",\n").toString() + new StringBuilder(40).append("  inter_lens_distance: ").append(f1).append(",\n").toString() + new StringBuilder(53).append("  vertical_distance_to_lens_center: ").append(f2).append(",\n").toString() + new StringBuilder(44).append("  screen_to_lens_distance: ").append(f3).append(",\n").toString() + new StringBuilder(22 + str3.length()).append("  left_eye_max_fov: ").append(str3).append(",\n").toString() + new StringBuilder(16 + str4.length()).append("  distortion: ").append(str4).append(",\n").toString() + new StringBuilder(17).append("  magnet: ").append(bool).append(",\n").toString() + "}\n";
/*     */   }
/*     */ 
/*     */   private void setDefaultValues()
/*     */   {
/* 566 */     this.mVendor = "Google, Inc.";
/* 567 */     this.mModel = "Cardboard v1";
/*     */ 
/* 569 */     this.mInterLensDistance = 0.06F;
/* 570 */     this.mVerticalDistanceToLensCenter = 0.035F;
/* 571 */     this.mScreenToLensDistance = 0.042F;
/*     */ 
/* 573 */     this.mLeftEyeMaxFov = new FieldOfView();
/*     */ 
/* 575 */     this.mHasMagnet = true;
/*     */ 
/* 577 */     this.mDistortion = new Distortion();
/*     */   }
/*     */ 
/*     */   private void copyFrom(CardboardDeviceParams params) {
/* 581 */     this.mVendor = params.mVendor;
/* 582 */     this.mModel = params.mModel;
/*     */ 
/* 584 */     this.mInterLensDistance = params.mInterLensDistance;
/* 585 */     this.mVerticalDistanceToLensCenter = params.mVerticalDistanceToLensCenter;
/* 586 */     this.mScreenToLensDistance = params.mScreenToLensDistance;
/*     */ 
/* 588 */     this.mLeftEyeMaxFov = new FieldOfView(params.mLeftEyeMaxFov);
/*     */ 
/* 590 */     this.mHasMagnet = params.mHasMagnet;
/*     */ 
/* 592 */     this.mDistortion = new Distortion(params.mDistortion);
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.CardboardDeviceParams
 * JD-Core Version:    0.6.2
 */