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
/* 102 */   private static final VerticalAlignmentType DEFAULT_VERTICAL_ALIGNMENT = VerticalAlignmentType.BOTTOM;
/*     */   private static final float DEFAULT_VERTICAL_DISTANCE_TO_LENS_CENTER = 0.035F;
/*     */   private static final float DEFAULT_SCREEN_TO_LENS_DISTANCE = 0.042F;
/*     */   private String vendor;
/*     */   private String model;
/*     */   private float interLensDistance;
/*     */   private VerticalAlignmentType verticalAlignment;
/*     */   private float verticalDistanceToLensCenter;
/*     */   private float screenToLensDistance;
/*     */   private FieldOfView leftEyeMaxFov;
/*     */   private boolean hasMagnet;
/*     */   private Distortion distortion;
/*     */ 
/*     */   public CardboardDeviceParams()
/*     */   {
/* 129 */     setDefaultValues();
/*     */   }
/*     */ 
/*     */   public CardboardDeviceParams(CardboardDeviceParams params)
/*     */   {
/* 138 */     copyFrom(params);
/*     */   }
/*     */ 
/*     */   public CardboardDeviceParams(CardboardDevice.DeviceParams params)
/*     */   {
/* 148 */     setDefaultValues();
/*     */ 
/* 150 */     if (params == null) {
/* 151 */       return;
/*     */     }
/*     */ 
/* 154 */     this.vendor = params.getVendor();
/* 155 */     this.model = params.getModel();
/*     */ 
/* 157 */     this.interLensDistance = params.getInterLensDistance();
/* 158 */     this.verticalAlignment = VerticalAlignmentType.fromProtoValue(params.getVerticalAlignment());
/* 159 */     this.verticalDistanceToLensCenter = params.getTrayToLensDistance();
/* 160 */     this.screenToLensDistance = params.getScreenToLensDistance();
/*     */ 
/* 162 */     this.leftEyeMaxFov = FieldOfView.parseFromProtobuf(params.leftEyeFieldOfViewAngles);
/* 163 */     if (this.leftEyeMaxFov == null) {
/* 164 */       this.leftEyeMaxFov = new FieldOfView();
/*     */     }
/*     */ 
/* 167 */     this.distortion = Distortion.parseFromProtobuf(params.distortionCoefficients);
/* 168 */     if (this.distortion == null) {
/* 169 */       this.distortion = new Distortion();
/*     */     }
/*     */ 
/* 172 */     this.hasMagnet = params.getHasMagnet();
/*     */   }
/*     */ 
/*     */   public static boolean isOriginalCardboardDeviceUri(Uri uri)
/*     */   {
/* 182 */     return (URI_ORIGINAL_CARDBOARD_QR_CODE.equals(uri)) || ((URI_ORIGINAL_CARDBOARD_NFC.getScheme().equals(uri.getScheme())) && (URI_ORIGINAL_CARDBOARD_NFC.getAuthority().equals(uri.getAuthority())));
/*     */   }
/*     */ 
/*     */   private static boolean isCardboardDeviceUri(Uri uri)
/*     */   {
/* 189 */     return ("http".equals(uri.getScheme())) && ("google.com".equals(uri.getAuthority())) && ("/cardboard/cfg".equals(uri.getPath()));
/*     */   }
/*     */ 
/*     */   public static boolean isCardboardUri(Uri uri)
/*     */   {
/* 196 */     return (isOriginalCardboardDeviceUri(uri)) || (isCardboardDeviceUri(uri)); } 
/*     */   // ERROR //
/*     */   public static CardboardDeviceParams createFromUri(Uri uri) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: ifnonnull +5 -> 6
/*     */     //   4: aconst_null
/*     */     //   5: areturn
/*     */     //   6: aload_0
/*     */     //   7: invokestatic 44	com/google/vrtoolkit/cardboard/CardboardDeviceParams:isOriginalCardboardDeviceUri	(Landroid/net/Uri;)Z
/*     */     //   10: ifeq +25 -> 35
/*     */     //   13: ldc 46
/*     */     //   15: ldc 47
/*     */     //   17: invokestatic 48	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
/*     */     //   20: pop
/*     */     //   21: new 49	com/google/vrtoolkit/cardboard/CardboardDeviceParams
/*     */     //   24: dup
/*     */     //   25: invokespecial 50	com/google/vrtoolkit/cardboard/CardboardDeviceParams:<init>	()V
/*     */     //   28: astore_1
/*     */     //   29: aload_1
/*     */     //   30: invokespecial 7	com/google/vrtoolkit/cardboard/CardboardDeviceParams:setDefaultValues	()V
/*     */     //   33: aload_1
/*     */     //   34: areturn
/*     */     //   35: aload_0
/*     */     //   36: invokestatic 45	com/google/vrtoolkit/cardboard/CardboardDeviceParams:isCardboardDeviceUri	(Landroid/net/Uri;)Z
/*     */     //   39: ifne +24 -> 63
/*     */     //   42: ldc 46
/*     */     //   44: ldc 51
/*     */     //   46: iconst_1
/*     */     //   47: anewarray 52	java/lang/Object
/*     */     //   50: dup
/*     */     //   51: iconst_0
/*     */     //   52: aload_0
/*     */     //   53: aastore
/*     */     //   54: invokestatic 53	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   57: invokestatic 54	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
/*     */     //   60: pop
/*     */     //   61: aconst_null
/*     */     //   62: areturn
/*     */     //   63: aconst_null
/*     */     //   64: astore_1
/*     */     //   65: aload_0
/*     */     //   66: ldc 55
/*     */     //   68: invokevirtual 56	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
/*     */     //   71: astore_2
/*     */     //   72: aload_2
/*     */     //   73: ifnull +77 -> 150
/*     */     //   76: aload_2
/*     */     //   77: bipush 11
/*     */     //   79: invokestatic 57	android/util/Base64:decode	(Ljava/lang/String;I)[B
/*     */     //   82: astore_3
/*     */     //   83: new 58	com/google/vrtoolkit/cardboard/proto/CardboardDevice$DeviceParams
/*     */     //   86: dup
/*     */     //   87: invokespecial 59	com/google/vrtoolkit/cardboard/proto/CardboardDevice$DeviceParams:<init>	()V
/*     */     //   90: aload_3
/*     */     //   91: invokestatic 60	com/google/protobuf/nano/MessageNano:mergeFrom	(Lcom/google/protobuf/nano/MessageNano;[B)Lcom/google/protobuf/nano/MessageNano;
/*     */     //   94: checkcast 58	com/google/vrtoolkit/cardboard/proto/CardboardDevice$DeviceParams
/*     */     //   97: astore_1
/*     */     //   98: ldc 46
/*     */     //   100: ldc 61
/*     */     //   102: invokestatic 48	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
/*     */     //   105: pop
/*     */     //   106: goto +52 -> 158
/*     */     //   109: astore_3
/*     */     //   110: ldc 46
/*     */     //   112: ldc 63
/*     */     //   114: aload_3
/*     */     //   115: invokevirtual 64	java/lang/Exception:toString	()Ljava/lang/String;
/*     */     //   118: invokestatic 65	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   121: dup
/*     */     //   122: invokevirtual 66	java/lang/String:length	()I
/*     */     //   125: ifeq +9 -> 134
/*     */     //   128: invokevirtual 67	java/lang/String:concat	(Ljava/lang/String;)Ljava/lang/String;
/*     */     //   131: goto +12 -> 143
/*     */     //   134: pop
/*     */     //   135: new 68	java/lang/String
/*     */     //   138: dup_x1
/*     */     //   139: swap
/*     */     //   140: invokespecial 69	java/lang/String:<init>	(Ljava/lang/String;)V
/*     */     //   143: invokestatic 54	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
/*     */     //   146: pop
/*     */     //   147: goto +11 -> 158
/*     */     //   150: ldc 46
/*     */     //   152: ldc 70
/*     */     //   154: invokestatic 54	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
/*     */     //   157: pop
/*     */     //   158: new 49	com/google/vrtoolkit/cardboard/CardboardDeviceParams
/*     */     //   161: dup
/*     */     //   162: aload_1
/*     */     //   163: invokespecial 71	com/google/vrtoolkit/cardboard/CardboardDeviceParams:<init>	(Lcom/google/vrtoolkit/cardboard/proto/CardboardDevice$DeviceParams;)V
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
/*     */     //   8: invokestatic 72	java/nio/ByteBuffer:allocate	(I)Ljava/nio/ByteBuffer;
/*     */     //   11: astore_1
/*     */     //   12: aload_0
/*     */     //   13: aload_1
/*     */     //   14: invokevirtual 73	java/nio/ByteBuffer:array	()[B
/*     */     //   17: iconst_0
/*     */     //   18: aload_1
/*     */     //   19: invokevirtual 73	java/nio/ByteBuffer:array	()[B
/*     */     //   22: arraylength
/*     */     //   23: invokevirtual 74	java/io/InputStream:read	([BII)I
/*     */     //   26: iconst_m1
/*     */     //   27: if_icmpne +13 -> 40
/*     */     //   30: ldc 46
/*     */     //   32: ldc 75
/*     */     //   34: invokestatic 76	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
/*     */     //   37: pop
/*     */     //   38: aconst_null
/*     */     //   39: areturn
/*     */     //   40: aload_1
/*     */     //   41: invokevirtual 77	java/nio/ByteBuffer:getInt	()I
/*     */     //   44: istore_2
/*     */     //   45: aload_1
/*     */     //   46: invokevirtual 77	java/nio/ByteBuffer:getInt	()I
/*     */     //   49: istore_3
/*     */     //   50: iload_2
/*     */     //   51: ldc 78
/*     */     //   53: if_icmpeq +13 -> 66
/*     */     //   56: ldc 46
/*     */     //   58: ldc 79
/*     */     //   60: invokestatic 76	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
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
/*     */     //   78: invokevirtual 74	java/io/InputStream:read	([BII)I
/*     */     //   81: iconst_m1
/*     */     //   82: if_icmpne +13 -> 95
/*     */     //   85: ldc 46
/*     */     //   87: ldc 75
/*     */     //   89: invokestatic 76	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
/*     */     //   92: pop
/*     */     //   93: aconst_null
/*     */     //   94: areturn
/*     */     //   95: new 49	com/google/vrtoolkit/cardboard/CardboardDeviceParams
/*     */     //   98: dup
/*     */     //   99: new 58	com/google/vrtoolkit/cardboard/proto/CardboardDevice$DeviceParams
/*     */     //   102: dup
/*     */     //   103: invokespecial 59	com/google/vrtoolkit/cardboard/proto/CardboardDevice$DeviceParams:<init>	()V
/*     */     //   106: aload 4
/*     */     //   108: invokestatic 60	com/google/protobuf/nano/MessageNano:mergeFrom	(Lcom/google/protobuf/nano/MessageNano;[B)Lcom/google/protobuf/nano/MessageNano;
/*     */     //   111: checkcast 58	com/google/vrtoolkit/cardboard/proto/CardboardDevice$DeviceParams
/*     */     //   114: invokespecial 71	com/google/vrtoolkit/cardboard/CardboardDeviceParams:<init>	(Lcom/google/vrtoolkit/cardboard/proto/CardboardDevice$DeviceParams;)V
/*     */     //   117: areturn
/*     */     //   118: astore_1
/*     */     //   119: ldc 46
/*     */     //   121: ldc 81
/*     */     //   123: aload_1
/*     */     //   124: invokevirtual 82	com/google/protobuf/nano/InvalidProtocolBufferNanoException:toString	()Ljava/lang/String;
/*     */     //   127: invokestatic 65	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   130: dup
/*     */     //   131: invokevirtual 66	java/lang/String:length	()I
/*     */     //   134: ifeq +9 -> 143
/*     */     //   137: invokevirtual 67	java/lang/String:concat	(Ljava/lang/String;)Ljava/lang/String;
/*     */     //   140: goto +12 -> 152
/*     */     //   143: pop
/*     */     //   144: new 68	java/lang/String
/*     */     //   147: dup_x1
/*     */     //   148: swap
/*     */     //   149: invokespecial 69	java/lang/String:<init>	(Ljava/lang/String;)V
/*     */     //   152: invokestatic 54	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
/*     */     //   155: pop
/*     */     //   156: goto +41 -> 197
/*     */     //   159: astore_1
/*     */     //   160: ldc 46
/*     */     //   162: ldc 84
/*     */     //   164: aload_1
/*     */     //   165: invokevirtual 85	java/io/IOException:toString	()Ljava/lang/String;
/*     */     //   168: invokestatic 65	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   171: dup
/*     */     //   172: invokevirtual 66	java/lang/String:length	()I
/*     */     //   175: ifeq +9 -> 184
/*     */     //   178: invokevirtual 67	java/lang/String:concat	(Ljava/lang/String;)Ljava/lang/String;
/*     */     //   181: goto +12 -> 193
/*     */     //   184: pop
/*     */     //   185: new 68	java/lang/String
/*     */     //   188: dup_x1
/*     */     //   189: swap
/*     */     //   190: invokespecial 69	java/lang/String:<init>	(Ljava/lang/String;)V
/*     */     //   193: invokestatic 54	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
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
/* 296 */       ByteBuffer header = ByteBuffer.allocate(8);
/* 297 */       header.putInt(894990891);
/* 298 */       header.putInt(paramBytes.length);
/* 299 */       outputStream.write(header.array());
/* 300 */       outputStream.write(paramBytes);
/* 301 */       return true;
/*     */     } catch (IOException e) {
/* 303 */       "CardboardDeviceParams";
/*     */     }
/*     */   }
/*     */ 
/*     */   public static CardboardDeviceParams createFromNfcContents(NdefMessage tagContents)
/*     */   {
/* 315 */     if (tagContents == null) {
/* 316 */       Log.w("CardboardDeviceParams", "Could not get contents from NFC tag.");
/* 317 */       return null;
/*     */     }
/*     */ 
/* 320 */     for (NdefRecord record : tagContents.getRecords()) {
/* 321 */       CardboardDeviceParams params = createFromUri(record.toUri());
/*     */ 
/* 323 */       if (params != null) {
/* 324 */         return params;
/*     */       }
/*     */     }
/*     */ 
/* 328 */     return null;
/*     */   }
/*     */ 
/*     */   private byte[] toByteArray()
/*     */   {
/* 337 */     CardboardDevice.DeviceParams params = new CardboardDevice.DeviceParams();
/*     */ 
/* 339 */     params.setVendor(this.vendor);
/* 340 */     params.setModel(this.model);
/* 341 */     params.setInterLensDistance(this.interLensDistance);
/* 342 */     params.setVerticalAlignment(this.verticalAlignment.toProtoValue());
/* 343 */     if (this.verticalAlignment == VerticalAlignmentType.CENTER)
/*     */     {
/* 348 */       params.setTrayToLensDistance(0.035F);
/*     */     }
/* 350 */     else params.setTrayToLensDistance(this.verticalDistanceToLensCenter);
/*     */ 
/* 352 */     params.setScreenToLensDistance(this.screenToLensDistance);
/* 353 */     params.leftEyeFieldOfViewAngles = this.leftEyeMaxFov.toProtobuf();
/* 354 */     params.distortionCoefficients = this.distortion.toProtobuf();
/*     */ 
/* 356 */     if (this.hasMagnet) {
/* 357 */       params.setHasMagnet(this.hasMagnet);
/*     */     }
/*     */ 
/* 360 */     return MessageNano.toByteArray(params);
/*     */   }
/*     */ 
/*     */   public Uri toUri()
/*     */   {
/* 375 */     byte[] paramsData = toByteArray();
/* 376 */     int paramsSize = paramsData.length;
/*     */ 
/* 378 */     return new Uri.Builder().scheme("http").authority("google.com").appendEncodedPath("cardboard/cfg").appendQueryParameter("p", Base64.encodeToString(paramsData, 0, paramsSize, 11)).build();
/*     */   }
/*     */ 
/*     */   public void setVendor(String vendor)
/*     */   {
/* 394 */     this.vendor = (vendor != null ? vendor : "");
/*     */   }
/*     */ 
/*     */   public String getVendor()
/*     */   {
/* 403 */     return this.vendor;
/*     */   }
/*     */ 
/*     */   public void setModel(String model)
/*     */   {
/* 412 */     this.model = (model != null ? model : "");
/*     */   }
/*     */ 
/*     */   public String getModel()
/*     */   {
/* 421 */     return this.model;
/*     */   }
/*     */ 
/*     */   public void setInterLensDistance(float interLensDistance)
/*     */   {
/* 430 */     this.interLensDistance = interLensDistance;
/*     */   }
/*     */ 
/*     */   public float getInterLensDistance()
/*     */   {
/* 439 */     return this.interLensDistance;
/*     */   }
/*     */ 
/*     */   public VerticalAlignmentType getVerticalAlignment()
/*     */   {
/* 478 */     return this.verticalAlignment;
/*     */   }
/*     */ 
/*     */   public void setVerticalAlignment(VerticalAlignmentType verticalAlignment)
/*     */   {
/* 485 */     this.verticalAlignment = verticalAlignment;
/*     */   }
/*     */ 
/*     */   public void setVerticalDistanceToLensCenter(float verticalDistanceToLensCenter)
/*     */   {
/* 495 */     this.verticalDistanceToLensCenter = verticalDistanceToLensCenter;
/*     */   }
/*     */ 
/*     */   public float getVerticalDistanceToLensCenter()
/*     */   {
/* 505 */     return this.verticalDistanceToLensCenter;
/*     */   }
/*     */ 
/*     */   float getYEyeOffsetMeters(ScreenParams screen)
/*     */   {
/* 510 */     switch (1.$SwitchMap$com$google$vrtoolkit$cardboard$CardboardDeviceParams$VerticalAlignmentType[getVerticalAlignment().ordinal()]) {
/*     */     case 1:
/*     */     default:
/* 513 */       return screen.getHeightMeters() / 2.0F;
/*     */     case 2:
/* 515 */       return getVerticalDistanceToLensCenter() - screen.getBorderSizeMeters();
/*     */     case 3:
/* 517 */     }return screen.getHeightMeters() - (getVerticalDistanceToLensCenter() - screen.getBorderSizeMeters());
/*     */   }
/*     */ 
/*     */   public void setScreenToLensDistance(float screenToLensDistance)
/*     */   {
/* 530 */     this.screenToLensDistance = screenToLensDistance;
/*     */   }
/*     */ 
/*     */   public float getScreenToLensDistance()
/*     */   {
/* 539 */     return this.screenToLensDistance;
/*     */   }
/*     */ 
/*     */   public Distortion getDistortion()
/*     */   {
/* 548 */     return this.distortion;
/*     */   }
/*     */ 
/*     */   public FieldOfView getLeftEyeMaxFov()
/*     */   {
/* 560 */     return this.leftEyeMaxFov;
/*     */   }
/*     */ 
/*     */   public boolean getHasMagnet()
/*     */   {
/* 569 */     return this.hasMagnet;
/*     */   }
/*     */ 
/*     */   public void setHasMagnet(boolean magnet)
/*     */   {
/* 578 */     this.hasMagnet = magnet;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 589 */     if (other == null) {
/* 590 */       return false;
/*     */     }
/*     */ 
/* 593 */     if (other == this) {
/* 594 */       return true;
/*     */     }
/*     */ 
/* 597 */     if (!(other instanceof CardboardDeviceParams)) {
/* 598 */       return false;
/*     */     }
/*     */ 
/* 601 */     CardboardDeviceParams o = (CardboardDeviceParams)other;
/*     */ 
/* 604 */     return (this.vendor.equals(o.vendor)) && (this.model.equals(o.model)) && (this.interLensDistance == o.interLensDistance) && (this.verticalAlignment == o.verticalAlignment) && ((this.verticalAlignment == VerticalAlignmentType.CENTER) || (this.verticalDistanceToLensCenter == o.verticalDistanceToLensCenter)) && (this.screenToLensDistance == o.screenToLensDistance) && (this.leftEyeMaxFov.equals(o.leftEyeMaxFov)) && (this.distortion.equals(o.distortion)) && (this.hasMagnet == o.hasMagnet);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 623 */     String str1 = String.valueOf(String.valueOf(this.vendor)); String str2 = String.valueOf(String.valueOf(this.model)); float f1 = this.interLensDistance; String str3 = String.valueOf(String.valueOf(this.verticalAlignment)); float f2 = this.verticalDistanceToLensCenter; float f3 = this.screenToLensDistance; String str4 = String.valueOf(String.valueOf(this.leftEyeMaxFov.toString().replace("\n", "\n  "))); String str5 = String.valueOf(String.valueOf(this.distortion.toString().replace("\n", "\n  "))); boolean bool = this.hasMagnet; return "{\n" + new StringBuilder(12 + str1.length()).append("  vendor: ").append(str1).append(",\n").toString() + new StringBuilder(11 + str2.length()).append("  model: ").append(str2).append(",\n").toString() + new StringBuilder(40).append("  inter_lens_distance: ").append(f1).append(",\n").toString() + new StringBuilder(24 + str3.length()).append("  vertical_alignment: ").append(str3).append(",\n").toString() + new StringBuilder(53).append("  vertical_distance_to_lens_center: ").append(f2).append(",\n").toString() + new StringBuilder(44).append("  screen_to_lens_distance: ").append(f3).append(",\n").toString() + new StringBuilder(22 + str4.length()).append("  left_eye_max_fov: ").append(str4).append(",\n").toString() + new StringBuilder(16 + str5.length()).append("  distortion: ").append(str5).append(",\n").toString() + new StringBuilder(17).append("  magnet: ").append(bool).append(",\n").toString() + "}\n";
/*     */   }
/*     */ 
/*     */   private void setDefaultValues()
/*     */   {
/* 640 */     this.vendor = "Google, Inc.";
/* 641 */     this.model = "Cardboard v1";
/*     */ 
/* 643 */     this.interLensDistance = 0.06F;
/* 644 */     this.verticalAlignment = DEFAULT_VERTICAL_ALIGNMENT;
/* 645 */     this.verticalDistanceToLensCenter = 0.035F;
/* 646 */     this.screenToLensDistance = 0.042F;
/*     */ 
/* 648 */     this.leftEyeMaxFov = new FieldOfView();
/*     */ 
/* 650 */     this.hasMagnet = true;
/*     */ 
/* 652 */     this.distortion = new Distortion();
/*     */   }
/*     */ 
/*     */   private void copyFrom(CardboardDeviceParams params) {
/* 656 */     this.vendor = params.vendor;
/* 657 */     this.model = params.model;
/*     */ 
/* 659 */     this.interLensDistance = params.interLensDistance;
/* 660 */     this.verticalAlignment = params.verticalAlignment;
/* 661 */     this.verticalDistanceToLensCenter = params.verticalDistanceToLensCenter;
/* 662 */     this.screenToLensDistance = params.screenToLensDistance;
/*     */ 
/* 664 */     this.leftEyeMaxFov = new FieldOfView(params.leftEyeMaxFov);
/*     */ 
/* 666 */     this.hasMagnet = params.hasMagnet;
/*     */ 
/* 668 */     this.distortion = new Distortion(params.distortion);
/*     */   }
/*     */ 
/*     */   public static enum VerticalAlignmentType
/*     */   {
/* 448 */     BOTTOM(0), 
/*     */ 
/* 450 */     CENTER(1), 
/*     */ 
/* 452 */     TOP(2);
/*     */ 
/*     */     private final int protoValue;
/*     */ 
/*     */     private VerticalAlignmentType(int protoValue) {
/* 457 */       this.protoValue = protoValue;
/*     */     }
/*     */     int toProtoValue() {
/* 460 */       return this.protoValue;
/*     */     }
/*     */     static VerticalAlignmentType fromProtoValue(int protoValue) {
/* 463 */       for (VerticalAlignmentType type : values()) {
/* 464 */         if (type.protoValue == protoValue) {
/* 465 */           return type;
/*     */         }
/*     */       }
/*     */ 
/* 469 */       Log.e("CardboardDeviceParams", String.format("Unknown alignment type from proto: %d", new Object[] { Integer.valueOf(protoValue) }));
/* 470 */       return BOTTOM;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.CardboardDeviceParams
 * JD-Core Version:    0.6.2
 */