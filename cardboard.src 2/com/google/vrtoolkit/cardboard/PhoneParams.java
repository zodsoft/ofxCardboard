/*     */ package com.google.vrtoolkit.cardboard;
/*     */ 
/*     */ import android.os.Build;
/*     */ import android.util.Log;
/*     */ import com.google.protobuf.nano.MessageNano;
/*     */ import com.google.vrtoolkit.cardboard.proto.Phone.PhoneParams;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ 
/*     */ public class PhoneParams
/*     */ {
/*  29 */   private static final String TAG = PhoneParams.class.getSimpleName();
/*     */   private static final int STREAM_SENTINEL = 779508118;
/*  65 */   private static final List<PpiOverride> PPI_OVERRIDES = Arrays.asList(new PpiOverride[] { new PpiOverride("Micromax", null, "4560MMX", null, 217, 217), new PpiOverride("HTC", "endeavoru", "HTC One X", null, 312, 312) });
/*     */ 
/*     */   static boolean getPpiOverride(List<PpiOverride> overrides, String manufacturer, String device, String model, String hardware, Phone.PhoneParams params)
/*     */   {
/*  78 */     Log.d(TAG, String.format("Override search for device: {MANUFACTURER=%s, DEVICE=%s, MODEL=%s, HARDWARE=%s}", new Object[] { manufacturer, device, model, hardware }));
/*     */ 
/*  81 */     for (PpiOverride override : overrides) {
/*  82 */       if (override.isMatching(manufacturer, device, model, hardware)) {
/*  83 */         Log.d(TAG, String.format("Found override: {MANUFACTURER=%s, DEVICE=%s, MODEL=%s, HARDWARE=%s} : x_ppi=%d, y_ppi=%d", new Object[] { override.manufacturer, override.device, override.model, override.hardware, Integer.valueOf(override.xPpi), Integer.valueOf(override.yPpi) }));
/*     */ 
/*  88 */         params.setXPpi(override.xPpi);
/*  89 */         params.setYPpi(override.yPpi);
/*  90 */         return true;
/*     */       }
/*     */     }
/*  93 */     return false;
/*     */   }
/*     */ 
/*     */   static void registerOverridesInternal(List<PpiOverride> overrides, String manufacturer, String device, String model, String hardware)
/*     */   {
/*  99 */     Phone.PhoneParams currentParams = readFromExternalStorage();
/* 100 */     Phone.PhoneParams newParams = currentParams == null ? new Phone.PhoneParams() : currentParams.clone();
/*     */ 
/* 102 */     if ((getPpiOverride(overrides, manufacturer, device, model, hardware, newParams)) && (!MessageNano.messageNanoEquals(currentParams, newParams)))
/*     */     {
/* 104 */       Log.i(TAG, "Applying phone param override.");
/* 105 */       writeToExternalStorage(newParams);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void registerOverrides()
/*     */   {
/* 114 */     registerOverridesInternal(PPI_OVERRIDES, Build.MANUFACTURER, Build.DEVICE, Build.MODEL, Build.HARDWARE); } 
/*     */   // ERROR //
/*     */   static Phone.PhoneParams readFromInputStream(InputStream inputStream) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: ifnonnull +5 -> 6
/*     */     //   4: aconst_null
/*     */     //   5: areturn
/*     */     //   6: bipush 8
/*     */     //   8: invokestatic 40	java/nio/ByteBuffer:allocate	(I)Ljava/nio/ByteBuffer;
/*     */     //   11: astore_1
/*     */     //   12: aload_0
/*     */     //   13: aload_1
/*     */     //   14: invokevirtual 41	java/nio/ByteBuffer:array	()[B
/*     */     //   17: iconst_0
/*     */     //   18: aload_1
/*     */     //   19: invokevirtual 41	java/nio/ByteBuffer:array	()[B
/*     */     //   22: arraylength
/*     */     //   23: invokevirtual 42	java/io/InputStream:read	([BII)I
/*     */     //   26: iconst_m1
/*     */     //   27: if_icmpne +14 -> 41
/*     */     //   30: getstatic 5	com/google/vrtoolkit/cardboard/PhoneParams:TAG	Ljava/lang/String;
/*     */     //   33: ldc 43
/*     */     //   35: invokestatic 44	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
/*     */     //   38: pop
/*     */     //   39: aconst_null
/*     */     //   40: areturn
/*     */     //   41: aload_1
/*     */     //   42: invokevirtual 45	java/nio/ByteBuffer:getInt	()I
/*     */     //   45: istore_2
/*     */     //   46: aload_1
/*     */     //   47: invokevirtual 45	java/nio/ByteBuffer:getInt	()I
/*     */     //   50: istore_3
/*     */     //   51: iload_2
/*     */     //   52: ldc 46
/*     */     //   54: if_icmpeq +14 -> 68
/*     */     //   57: getstatic 5	com/google/vrtoolkit/cardboard/PhoneParams:TAG	Ljava/lang/String;
/*     */     //   60: ldc 47
/*     */     //   62: invokestatic 44	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
/*     */     //   65: pop
/*     */     //   66: aconst_null
/*     */     //   67: areturn
/*     */     //   68: iload_3
/*     */     //   69: newarray byte
/*     */     //   71: astore 4
/*     */     //   73: aload_0
/*     */     //   74: aload 4
/*     */     //   76: iconst_0
/*     */     //   77: aload 4
/*     */     //   79: arraylength
/*     */     //   80: invokevirtual 42	java/io/InputStream:read	([BII)I
/*     */     //   83: iconst_m1
/*     */     //   84: if_icmpne +14 -> 98
/*     */     //   87: getstatic 5	com/google/vrtoolkit/cardboard/PhoneParams:TAG	Ljava/lang/String;
/*     */     //   90: ldc 43
/*     */     //   92: invokestatic 44	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
/*     */     //   95: pop
/*     */     //   96: aconst_null
/*     */     //   97: areturn
/*     */     //   98: new 26	com/google/vrtoolkit/cardboard/proto/Phone$PhoneParams
/*     */     //   101: dup
/*     */     //   102: invokespecial 27	com/google/vrtoolkit/cardboard/proto/Phone$PhoneParams:<init>	()V
/*     */     //   105: aload 4
/*     */     //   107: invokestatic 48	com/google/protobuf/nano/MessageNano:mergeFrom	(Lcom/google/protobuf/nano/MessageNano;[B)Lcom/google/protobuf/nano/MessageNano;
/*     */     //   110: checkcast 26	com/google/vrtoolkit/cardboard/proto/Phone$PhoneParams
/*     */     //   113: areturn
/*     */     //   114: astore_1
/*     */     //   115: getstatic 5	com/google/vrtoolkit/cardboard/PhoneParams:TAG	Ljava/lang/String;
/*     */     //   118: ldc 50
/*     */     //   120: aload_1
/*     */     //   121: invokevirtual 51	com/google/protobuf/nano/InvalidProtocolBufferNanoException:toString	()Ljava/lang/String;
/*     */     //   124: invokestatic 52	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   127: dup
/*     */     //   128: invokevirtual 53	java/lang/String:length	()I
/*     */     //   131: ifeq +9 -> 140
/*     */     //   134: invokevirtual 54	java/lang/String:concat	(Ljava/lang/String;)Ljava/lang/String;
/*     */     //   137: goto +12 -> 149
/*     */     //   140: pop
/*     */     //   141: new 55	java/lang/String
/*     */     //   144: dup_x1
/*     */     //   145: swap
/*     */     //   146: invokespecial 56	java/lang/String:<init>	(Ljava/lang/String;)V
/*     */     //   149: invokestatic 57	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
/*     */     //   152: pop
/*     */     //   153: goto +42 -> 195
/*     */     //   156: astore_1
/*     */     //   157: getstatic 5	com/google/vrtoolkit/cardboard/PhoneParams:TAG	Ljava/lang/String;
/*     */     //   160: ldc 59
/*     */     //   162: aload_1
/*     */     //   163: invokevirtual 60	java/io/IOException:toString	()Ljava/lang/String;
/*     */     //   166: invokestatic 52	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   169: dup
/*     */     //   170: invokevirtual 53	java/lang/String:length	()I
/*     */     //   173: ifeq +9 -> 182
/*     */     //   176: invokevirtual 54	java/lang/String:concat	(Ljava/lang/String;)Ljava/lang/String;
/*     */     //   179: goto +12 -> 191
/*     */     //   182: pop
/*     */     //   183: new 55	java/lang/String
/*     */     //   186: dup_x1
/*     */     //   187: swap
/*     */     //   188: invokespecial 56	java/lang/String:<init>	(Ljava/lang/String;)V
/*     */     //   191: invokestatic 57	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
/*     */     //   194: pop
/*     */     //   195: aconst_null
/*     */     //   196: areturn
/*     */     //
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   6	40	114	com/google/protobuf/nano/InvalidProtocolBufferNanoException
/*     */     //   41	67	114	com/google/protobuf/nano/InvalidProtocolBufferNanoException
/*     */     //   68	97	114	com/google/protobuf/nano/InvalidProtocolBufferNanoException
/*     */     //   98	113	114	com/google/protobuf/nano/InvalidProtocolBufferNanoException
/*     */     //   6	40	156	java/io/IOException
/*     */     //   41	67	156	java/io/IOException
/*     */     //   68	97	156	java/io/IOException
/*     */     //   98	113	156	java/io/IOException } 
/*     */   static Phone.PhoneParams readFromExternalStorage() { try { InputStream stream = null;
/*     */       try {
/* 164 */         stream = new BufferedInputStream(new FileInputStream(ConfigUtils.getConfigFile("phone_params")));
/*     */ 
/* 166 */         return readFromInputStream(stream);
/*     */       } finally {
/* 168 */         if (stream != null)
/*     */           try {
/* 170 */             stream.close();
/*     */           }
/*     */           catch (IOException e)
/*     */           {
/*     */           }
/*     */       }
/*     */     } catch (FileNotFoundException e) {
/* 177 */       localObject1 = String.valueOf(String.valueOf(e)); Log.d(TAG, 43 + ((String)localObject1).length() + "Cardboard phone parameters file not found: " + (String)localObject1);
/*     */     } catch (IllegalStateException e) {
/* 179 */       Object localObject1 = String.valueOf(String.valueOf(e)); Log.w(TAG, 32 + ((String)localObject1).length() + "Error reading phone parameters: " + (String)localObject1);
/*     */     }
/* 181 */     return null; } 
/*     */   static boolean writeToOutputStream(Phone.PhoneParams params, OutputStream outputStream) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 77	com/google/vrtoolkit/cardboard/proto/Phone$PhoneParams:dEPRECATEDGyroBias	[F
/*     */     //   4: ifnull +35 -> 39
/*     */     //   7: aload_0
/*     */     //   8: getfield 77	com/google/vrtoolkit/cardboard/proto/Phone$PhoneParams:dEPRECATEDGyroBias	[F
/*     */     //   11: arraylength
/*     */     //   12: ifne +27 -> 39
/*     */     //   15: aload_0
/*     */     //   16: invokevirtual 28	com/google/vrtoolkit/cardboard/proto/Phone$PhoneParams:clone	()Lcom/google/vrtoolkit/cardboard/proto/Phone$PhoneParams;
/*     */     //   19: astore_0
/*     */     //   20: aload_0
/*     */     //   21: iconst_3
/*     */     //   22: newarray float
/*     */     //   24: dup
/*     */     //   25: iconst_0
/*     */     //   26: fconst_0
/*     */     //   27: fastore
/*     */     //   28: dup
/*     */     //   29: iconst_1
/*     */     //   30: fconst_0
/*     */     //   31: fastore
/*     */     //   32: dup
/*     */     //   33: iconst_2
/*     */     //   34: fconst_0
/*     */     //   35: fastore
/*     */     //   36: putfield 77	com/google/vrtoolkit/cardboard/proto/Phone$PhoneParams:dEPRECATEDGyroBias	[F
/*     */     //   39: aload_0
/*     */     //   40: invokestatic 78	com/google/protobuf/nano/MessageNano:toByteArray	(Lcom/google/protobuf/nano/MessageNano;)[B
/*     */     //   43: astore_2
/*     */     //   44: bipush 8
/*     */     //   46: invokestatic 40	java/nio/ByteBuffer:allocate	(I)Ljava/nio/ByteBuffer;
/*     */     //   49: astore_3
/*     */     //   50: aload_3
/*     */     //   51: ldc 46
/*     */     //   53: invokevirtual 79	java/nio/ByteBuffer:putInt	(I)Ljava/nio/ByteBuffer;
/*     */     //   56: pop
/*     */     //   57: aload_3
/*     */     //   58: aload_2
/*     */     //   59: arraylength
/*     */     //   60: invokevirtual 79	java/nio/ByteBuffer:putInt	(I)Ljava/nio/ByteBuffer;
/*     */     //   63: pop
/*     */     //   64: aload_1
/*     */     //   65: aload_3
/*     */     //   66: invokevirtual 41	java/nio/ByteBuffer:array	()[B
/*     */     //   69: invokevirtual 80	java/io/OutputStream:write	([B)V
/*     */     //   72: aload_1
/*     */     //   73: aload_2
/*     */     //   74: invokevirtual 80	java/io/OutputStream:write	([B)V
/*     */     //   77: iconst_1
/*     */     //   78: ireturn
/*     */     //   79: astore_2
/*     */     //   80: getstatic 5	com/google/vrtoolkit/cardboard/PhoneParams:TAG	Ljava/lang/String;
/*     */     //   83: ldc 81
/*     */     //   85: aload_2
/*     */     //   86: invokevirtual 60	java/io/IOException:toString	()Ljava/lang/String;
/*     */     //   89: invokestatic 52	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   92: dup
/*     */     //   93: invokevirtual 53	java/lang/String:length	()I
/*     */     //   96: ifeq +9 -> 105
/*     */     //   99: invokevirtual 54	java/lang/String:concat	(Ljava/lang/String;)Ljava/lang/String;
/*     */     //   102: goto +12 -> 114
/*     */     //   105: pop
/*     */     //   106: new 55	java/lang/String
/*     */     //   109: dup_x1
/*     */     //   110: swap
/*     */     //   111: invokespecial 56	java/lang/String:<init>	(Ljava/lang/String;)V
/*     */     //   114: invokestatic 57	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
/*     */     //   117: pop
/*     */     //   118: iconst_0
/*     */     //   119: ireturn
/*     */     //
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	78	79	java/io/IOException } 
/* 223 */   static boolean writeToExternalStorage(Phone.PhoneParams params) { boolean success = false;
/* 224 */     OutputStream stream = null;
/*     */     try {
/* 226 */       stream = new BufferedOutputStream(new FileOutputStream(ConfigUtils.getConfigFile("phone_params")));
/*     */ 
/* 228 */       success = writeToOutputStream(params, stream);
/*     */     } catch (FileNotFoundException e) {
/* 230 */       str = String.valueOf(String.valueOf(e)); Log.e(TAG, 37 + str.length() + "Unexpected file not found exception: " + str);
/*     */     } catch (IllegalStateException e) {
/* 232 */       String str = String.valueOf(String.valueOf(e)); Log.w(TAG, 32 + str.length() + "Error writing phone parameters: " + str);
/*     */     } finally {
/* 234 */       if (stream != null)
/*     */         try {
/* 236 */           stream.close();
/*     */         }
/*     */         catch (IOException e)
/*     */         {
/*     */         }
/*     */     }
/* 242 */     return success;
/*     */   }
/*     */ 
/*     */   static class PpiOverride
/*     */   {
/*     */     String manufacturer;
/*     */     String device;
/*     */     String model;
/*     */     String hardware;
/*     */     int xPpi;
/*     */     int yPpi;
/*     */ 
/*     */     PpiOverride(String manufacturer, String device, String model, String hardware, int xPpi, int yPpi)
/*     */     {
/*  48 */       this.manufacturer = manufacturer;
/*  49 */       this.device = device;
/*  50 */       this.model = model;
/*  51 */       this.hardware = hardware;
/*  52 */       this.xPpi = xPpi;
/*  53 */       this.yPpi = yPpi;
/*     */     }
/*     */ 
/*     */     boolean isMatching(String manufacturer, String device, String model, String hardware)
/*     */     {
/*  58 */       return ((this.manufacturer == null) || (this.manufacturer.equals(manufacturer))) && ((this.device == null) || (this.device.equals(device))) && ((this.model == null) || (this.model.equals(model))) && ((this.hardware == null) || (this.hardware.equals(hardware)));
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.PhoneParams
 * JD-Core Version:    0.6.2
 */