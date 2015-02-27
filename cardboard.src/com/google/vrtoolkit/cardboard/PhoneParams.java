/*    */ package com.google.vrtoolkit.cardboard;
/*    */ 
/*    */ import android.util.Log;
/*    */ import com.google.vrtoolkit.cardboard.proto.Phone.PhoneParams;
/*    */ import java.io.BufferedInputStream;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ public class PhoneParams
/*    */ {
/* 21 */   private static final String TAG = PhoneParams.class.getSimpleName();
/*    */   private static final int STREAM_SENTINEL = 779508118;
/*    */ 
/*    */   // ERROR //
/*    */   static Phone.PhoneParams readFromInputStream(InputStream inputStream)
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: ifnonnull +5 -> 6
/*    */     //   4: aconst_null
/*    */     //   5: areturn
/*    */     //   6: bipush 8
/*    */     //   8: invokestatic 5	java/nio/ByteBuffer:allocate	(I)Ljava/nio/ByteBuffer;
/*    */     //   11: astore_1
/*    */     //   12: aload_0
/*    */     //   13: aload_1
/*    */     //   14: invokevirtual 6	java/nio/ByteBuffer:array	()[B
/*    */     //   17: iconst_0
/*    */     //   18: aload_1
/*    */     //   19: invokevirtual 6	java/nio/ByteBuffer:array	()[B
/*    */     //   22: arraylength
/*    */     //   23: invokevirtual 7	java/io/InputStream:read	([BII)I
/*    */     //   26: iconst_m1
/*    */     //   27: if_icmpne +14 -> 41
/*    */     //   30: getstatic 8	com/google/vrtoolkit/cardboard/PhoneParams:TAG	Ljava/lang/String;
/*    */     //   33: ldc 9
/*    */     //   35: invokestatic 10	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
/*    */     //   38: pop
/*    */     //   39: aconst_null
/*    */     //   40: areturn
/*    */     //   41: aload_1
/*    */     //   42: invokevirtual 11	java/nio/ByteBuffer:getInt	()I
/*    */     //   45: istore_2
/*    */     //   46: aload_1
/*    */     //   47: invokevirtual 11	java/nio/ByteBuffer:getInt	()I
/*    */     //   50: istore_3
/*    */     //   51: iload_2
/*    */     //   52: ldc 12
/*    */     //   54: if_icmpeq +14 -> 68
/*    */     //   57: getstatic 8	com/google/vrtoolkit/cardboard/PhoneParams:TAG	Ljava/lang/String;
/*    */     //   60: ldc 13
/*    */     //   62: invokestatic 10	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
/*    */     //   65: pop
/*    */     //   66: aconst_null
/*    */     //   67: areturn
/*    */     //   68: iload_3
/*    */     //   69: newarray byte
/*    */     //   71: astore 4
/*    */     //   73: aload_0
/*    */     //   74: aload 4
/*    */     //   76: iconst_0
/*    */     //   77: aload 4
/*    */     //   79: arraylength
/*    */     //   80: invokevirtual 7	java/io/InputStream:read	([BII)I
/*    */     //   83: iconst_m1
/*    */     //   84: if_icmpne +14 -> 98
/*    */     //   87: getstatic 8	com/google/vrtoolkit/cardboard/PhoneParams:TAG	Ljava/lang/String;
/*    */     //   90: ldc 9
/*    */     //   92: invokestatic 10	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
/*    */     //   95: pop
/*    */     //   96: aconst_null
/*    */     //   97: areturn
/*    */     //   98: new 14	com/google/vrtoolkit/cardboard/proto/Phone$PhoneParams
/*    */     //   101: dup
/*    */     //   102: invokespecial 15	com/google/vrtoolkit/cardboard/proto/Phone$PhoneParams:<init>	()V
/*    */     //   105: aload 4
/*    */     //   107: invokestatic 16	com/google/protobuf/nano/MessageNano:mergeFrom	(Lcom/google/protobuf/nano/MessageNano;[B)Lcom/google/protobuf/nano/MessageNano;
/*    */     //   110: checkcast 14	com/google/vrtoolkit/cardboard/proto/Phone$PhoneParams
/*    */     //   113: areturn
/*    */     //   114: astore_1
/*    */     //   115: getstatic 8	com/google/vrtoolkit/cardboard/PhoneParams:TAG	Ljava/lang/String;
/*    */     //   118: ldc 18
/*    */     //   120: aload_1
/*    */     //   121: invokevirtual 19	com/google/protobuf/nano/InvalidProtocolBufferNanoException:toString	()Ljava/lang/String;
/*    */     //   124: invokestatic 20	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*    */     //   127: dup
/*    */     //   128: invokevirtual 21	java/lang/String:length	()I
/*    */     //   131: ifeq +9 -> 140
/*    */     //   134: invokevirtual 22	java/lang/String:concat	(Ljava/lang/String;)Ljava/lang/String;
/*    */     //   137: goto +12 -> 149
/*    */     //   140: pop
/*    */     //   141: new 23	java/lang/String
/*    */     //   144: dup_x1
/*    */     //   145: swap
/*    */     //   146: invokespecial 24	java/lang/String:<init>	(Ljava/lang/String;)V
/*    */     //   149: invokestatic 25	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
/*    */     //   152: pop
/*    */     //   153: goto +42 -> 195
/*    */     //   156: astore_1
/*    */     //   157: getstatic 8	com/google/vrtoolkit/cardboard/PhoneParams:TAG	Ljava/lang/String;
/*    */     //   160: ldc 27
/*    */     //   162: aload_1
/*    */     //   163: invokevirtual 28	java/io/IOException:toString	()Ljava/lang/String;
/*    */     //   166: invokestatic 20	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*    */     //   169: dup
/*    */     //   170: invokevirtual 21	java/lang/String:length	()I
/*    */     //   173: ifeq +9 -> 182
/*    */     //   176: invokevirtual 22	java/lang/String:concat	(Ljava/lang/String;)Ljava/lang/String;
/*    */     //   179: goto +12 -> 191
/*    */     //   182: pop
/*    */     //   183: new 23	java/lang/String
/*    */     //   186: dup_x1
/*    */     //   187: swap
/*    */     //   188: invokespecial 24	java/lang/String:<init>	(Ljava/lang/String;)V
/*    */     //   191: invokestatic 25	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
/*    */     //   194: pop
/*    */     //   195: aconst_null
/*    */     //   196: areturn
/*    */     //
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   6	40	114	com/google/protobuf/nano/InvalidProtocolBufferNanoException
/*    */     //   41	67	114	com/google/protobuf/nano/InvalidProtocolBufferNanoException
/*    */     //   68	97	114	com/google/protobuf/nano/InvalidProtocolBufferNanoException
/*    */     //   98	113	114	com/google/protobuf/nano/InvalidProtocolBufferNanoException
/*    */     //   6	40	156	java/io/IOException
/*    */     //   41	67	156	java/io/IOException
/*    */     //   68	97	156	java/io/IOException
/*    */     //   98	113	156	java/io/IOException
/*    */   }
/*    */ 
/*    */   static Phone.PhoneParams readFromExternalStorage()
/*    */   {
/*    */     try
/*    */     {
/* 72 */       InputStream stream = null;
/*    */       try {
/* 74 */         stream = new BufferedInputStream(new FileInputStream(ConfigUtils.getConfigFile("phone_params")));
/*    */ 
/* 76 */         return readFromInputStream(stream);
/*    */       } finally {
/* 78 */         if (stream != null)
/*    */           try {
/* 80 */             stream.close();
/*    */           }
/*    */           catch (IOException e) {
/*    */           }
/*    */       }
/*    */     }
/*    */     catch (FileNotFoundException e) {
/* 87 */       Object localObject1 = String.valueOf(String.valueOf(e)); Log.d(TAG, 43 + ((String)localObject1).length() + "Cardboard phone parameters file not found: " + (String)localObject1);
/* 88 */     }return null;
/*    */   }
/*    */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.PhoneParams
 * JD-Core Version:    0.6.2
 */