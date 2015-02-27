/*       */ package com.google.vrtoolkit.cardboard.sensors;
/*       */ 
/*       */ import android.app.Activity;
/*       */ import android.app.PendingIntent;
/*       */ import android.content.BroadcastReceiver;
/*       */ import android.content.Context;
/*       */ import android.content.Intent;
/*       */ import android.content.IntentFilter;
/*       */ import android.net.Uri;
/*       */ import android.nfc.FormatException;
/*       */ import android.nfc.NdefMessage;
/*       */ import android.nfc.NdefRecord;
/*       */ import android.nfc.NfcAdapter;
/*       */ import android.nfc.Tag;
/*       */ import android.nfc.TagLostException;
/*       */ import android.nfc.tech.Ndef;
/*       */ import android.os.Handler;
/*       */ import android.util.Log;
/*       */ import com.google.vrtoolkit.cardboard.CardboardDeviceParams;
/*       */ import java.io.IOException;
/*       */ import java.util.ArrayList;
/*       */ import java.util.List;
/*       */ import java.util.Timer;
/*       */ import java.util.TimerTask;
/*       */ 
/*       */ public class NfcSensor
/*       */ {
/*       */   private static final String TAG = "NfcSensor";
/*       */   private static final int MAX_CONNECTION_FAILURES = 1;
/*       */   private static final long NFC_POLLING_INTERVAL_MS = 250L;
/*       */   private static NfcSensor sInstance;
/*       */   private final Context mContext;
/*       */   private final NfcAdapter mNfcAdapter;
/*       */   private final Object mTagLock;
/*       */   private final List<ListenerHelper> mListeners;
/*       */   private IntentFilter[] mNfcIntentFilters;
/*       */   private Ndef mCurrentNdef;
/*       */   private Tag mCurrentTag;
/*       */   private boolean mCurrentTagIsCardboard;
/*       */   private Timer mNfcDisconnectTimer;
/*       */   private int mTagConnectionFailures;
/*       */ 
/*       */   public static NfcSensor getInstance(Context context)
/*       */   {
/*    98 */     if (sInstance == null) {
/*    99 */       sInstance = new NfcSensor(context);
/*       */     }
/*       */ 
/*   102 */     return sInstance;
/*       */   }
/*       */ 
/*       */   private NfcSensor(Context context) {
/*   106 */     this.mContext = context.getApplicationContext();
/*   107 */     this.mNfcAdapter = NfcAdapter.getDefaultAdapter(this.mContext);
/*   108 */     this.mListeners = new ArrayList();
/*   109 */     this.mTagLock = new Object();
/*       */ 
/*   112 */     if (this.mNfcAdapter == null) {
/*   113 */       return;
/*       */     }
/*       */ 
/*   117 */     IntentFilter ndefIntentFilter = new IntentFilter("android.nfc.action.NDEF_DISCOVERED");
/*   118 */     ndefIntentFilter.addAction("android.nfc.action.TECH_DISCOVERED");
/*   119 */     ndefIntentFilter.addAction("android.nfc.action.TAG_DISCOVERED");
/*   120 */     this.mNfcIntentFilters = new IntentFilter[] { ndefIntentFilter };
/*       */ 
/*   123 */     this.mContext.registerReceiver(new BroadcastReceiver()
/*       */     {
/*       */       public void onReceive(Context context, Intent intent) {
/*   126 */         NfcSensor.this.onNfcIntent(intent);
/*       */       }
/*       */     }
/*       */     , ndefIntentFilter);
/*       */   }
/*       */ 
/*       */   public void addOnCardboardNfcListener(OnCardboardNfcListener listener)
/*       */   {
/*   139 */     if (listener == null) {
/*   140 */       return;
/*       */     }
/*       */ 
/*   143 */     synchronized (this.mListeners) {
/*   144 */       for (ListenerHelper helper : this.mListeners) {
/*   145 */         if (helper.getListener() == listener) {
/*   146 */           return;
/*       */         }
/*       */       }
/*       */ 
/*   150 */       this.mListeners.add(new ListenerHelper(listener, new Handler()));
/*       */     }
/*       */   }
/*       */ 
/*       */   public void removeOnCardboardNfcListener(OnCardboardNfcListener listener)
/*       */   {
/*   160 */     if (listener == null) {
/*   161 */       return;
/*       */     }
/*       */ 
/*   164 */     synchronized (this.mListeners) {
/*   165 */       for (ListenerHelper helper : this.mListeners)
/*   166 */         if (helper.getListener() == listener) {
/*   167 */           this.mListeners.remove(helper);
/*   168 */           return;
/*       */         }
/*       */     }
/*       */   }
/*       */ 
/*       */   public boolean isNfcSupported()
/*       */   {
/*   180 */     return this.mNfcAdapter != null;
/*       */   }
/*       */ 
/*       */   public boolean isNfcEnabled()
/*       */   {
/*   189 */     return (isNfcSupported()) && (this.mNfcAdapter.isEnabled());
/*       */   }
/*       */ 
/*       */   public boolean isDeviceInCardboard()
/*       */   {
/*   199 */     synchronized (this.mTagLock) {
/*   200 */       return this.mCurrentTagIsCardboard;
/*       */     }
/*       */   }
/*       */ 
/*       */   public NdefMessage getTagContents()
/*       */   {
/*   210 */     synchronized (this.mTagLock) {
/*   211 */       return this.mCurrentNdef != null ? this.mCurrentNdef.getCachedNdefMessage() : null;
/*       */     }
/*       */   }
/*       */ 
/*       */   public NdefMessage getCurrentTagContents()
/*       */     throws TagLostException, IOException, FormatException
/*       */   {
/*   225 */     synchronized (this.mTagLock) {
/*   226 */       return this.mCurrentNdef != null ? this.mCurrentNdef.getNdefMessage() : null;
/*       */     }
/*       */   }
/*       */ 
/*       */   public int getTagCapacity()
/*       */   {
/*   237 */     synchronized (this.mTagLock) {
/*   238 */       if (this.mCurrentNdef == null) {
/*   239 */         throw new IllegalStateException("No NFC tag");
/*       */       }
/*       */ 
/*   242 */       return this.mCurrentNdef.getMaxSize(); }  } 
/*       */   // ERROR //
/*       */   public void writeUri(Uri uri) throws TagLostException, IOException, java.lang.IllegalArgumentException { // Byte code:
/*       */     //   0: aload_0
/*       */     //   1: getfield 9	com/google/vrtoolkit/cardboard/sensors/NfcSensor:mTagLock	Ljava/lang/Object;
/*       */     //   4: dup
/*       */     //   5: astore_2
/*       */     //   6: monitorenter
/*       */     //   7: aload_0
/*       */     //   8: getfield 51	com/google/vrtoolkit/cardboard/sensors/NfcSensor:mCurrentTag	Landroid/nfc/Tag;
/*       */     //   11: ifnonnull +13 -> 24
/*       */     //   14: new 47	java/lang/IllegalStateException
/*       */     //   17: dup
/*       */     //   18: ldc 52
/*       */     //   20: invokespecial 49	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
/*       */     //   23: athrow
/*       */     //   24: aconst_null
/*       */     //   25: astore_3
/*       */     //   26: aconst_null
/*       */     //   27: astore 4
/*       */     //   29: aload_1
/*       */     //   30: invokestatic 53	android/nfc/NdefRecord:createUri	(Landroid/net/Uri;)Landroid/nfc/NdefRecord;
/*       */     //   33: astore 5
/*       */     //   35: aload_0
/*       */     //   36: invokevirtual 54	com/google/vrtoolkit/cardboard/sensors/NfcSensor:getCurrentTagContents	()Landroid/nfc/NdefMessage;
/*       */     //   39: astore_3
/*       */     //   40: goto +10 -> 50
/*       */     //   43: astore 6
/*       */     //   45: aload_0
/*       */     //   46: invokevirtual 56	com/google/vrtoolkit/cardboard/sensors/NfcSensor:getTagContents	()Landroid/nfc/NdefMessage;
/*       */     //   49: astore_3
/*       */     //   50: aload_3
/*       */     //   51: ifnull +110 -> 161
/*       */     //   54: new 18	java/util/ArrayList
/*       */     //   57: dup
/*       */     //   58: invokespecial 19	java/util/ArrayList:<init>	()V
/*       */     //   61: astore 6
/*       */     //   63: iconst_0
/*       */     //   64: istore 7
/*       */     //   66: aload_3
/*       */     //   67: invokevirtual 57	android/nfc/NdefMessage:getRecords	()[Landroid/nfc/NdefRecord;
/*       */     //   70: astore 8
/*       */     //   72: aload 8
/*       */     //   74: arraylength
/*       */     //   75: istore 9
/*       */     //   77: iconst_0
/*       */     //   78: istore 10
/*       */     //   80: iload 10
/*       */     //   82: iload 9
/*       */     //   84: if_icmpge +52 -> 136
/*       */     //   87: aload 8
/*       */     //   89: iload 10
/*       */     //   91: aaload
/*       */     //   92: astore 11
/*       */     //   94: aload_0
/*       */     //   95: aload 11
/*       */     //   97: invokespecial 58	com/google/vrtoolkit/cardboard/sensors/NfcSensor:isCardboardNdefRecord	(Landroid/nfc/NdefRecord;)Z
/*       */     //   100: ifeq +22 -> 122
/*       */     //   103: iload 7
/*       */     //   105: ifne +25 -> 130
/*       */     //   108: aload 6
/*       */     //   110: aload 5
/*       */     //   112: invokevirtual 59	java/util/ArrayList:add	(Ljava/lang/Object;)Z
/*       */     //   115: pop
/*       */     //   116: iconst_1
/*       */     //   117: istore 7
/*       */     //   119: goto +11 -> 130
/*       */     //   122: aload 6
/*       */     //   124: aload 11
/*       */     //   126: invokevirtual 59	java/util/ArrayList:add	(Ljava/lang/Object;)Z
/*       */     //   129: pop
/*       */     //   130: iinc 10 1
/*       */     //   133: goto -53 -> 80
/*       */     //   136: new 60	android/nfc/NdefMessage
/*       */     //   139: dup
/*       */     //   140: aload 6
/*       */     //   142: aload 6
/*       */     //   144: invokevirtual 61	java/util/ArrayList:size	()I
/*       */     //   147: anewarray 62	android/nfc/NdefRecord
/*       */     //   150: invokevirtual 63	java/util/ArrayList:toArray	([Ljava/lang/Object;)[Ljava/lang/Object;
/*       */     //   153: checkcast 64	[Landroid/nfc/NdefRecord;
/*       */     //   156: invokespecial 65	android/nfc/NdefMessage:<init>	([Landroid/nfc/NdefRecord;)V
/*       */     //   159: astore 4
/*       */     //   161: aload 4
/*       */     //   163: ifnonnull +21 -> 184
/*       */     //   166: new 60	android/nfc/NdefMessage
/*       */     //   169: dup
/*       */     //   170: iconst_1
/*       */     //   171: anewarray 62	android/nfc/NdefRecord
/*       */     //   174: dup
/*       */     //   175: iconst_0
/*       */     //   176: aload 5
/*       */     //   178: aastore
/*       */     //   179: invokespecial 65	android/nfc/NdefMessage:<init>	([Landroid/nfc/NdefRecord;)V
/*       */     //   182: astore 4
/*       */     //   184: aload_0
/*       */     //   185: getfield 8	com/google/vrtoolkit/cardboard/sensors/NfcSensor:mCurrentNdef	Landroid/nfc/tech/Ndef;
/*       */     //   188: ifnull +150 -> 338
/*       */     //   191: aload_0
/*       */     //   192: getfield 8	com/google/vrtoolkit/cardboard/sensors/NfcSensor:mCurrentNdef	Landroid/nfc/tech/Ndef;
/*       */     //   195: invokevirtual 66	android/nfc/tech/Ndef:isConnected	()Z
/*       */     //   198: ifne +10 -> 208
/*       */     //   201: aload_0
/*       */     //   202: getfield 8	com/google/vrtoolkit/cardboard/sensors/NfcSensor:mCurrentNdef	Landroid/nfc/tech/Ndef;
/*       */     //   205: invokevirtual 67	android/nfc/tech/Ndef:connect	()V
/*       */     //   208: aload_0
/*       */     //   209: getfield 8	com/google/vrtoolkit/cardboard/sensors/NfcSensor:mCurrentNdef	Landroid/nfc/tech/Ndef;
/*       */     //   212: invokevirtual 50	android/nfc/tech/Ndef:getMaxSize	()I
/*       */     //   215: aload 4
/*       */     //   217: invokevirtual 68	android/nfc/NdefMessage:getByteArrayLength	()I
/*       */     //   220: if_icmpge +64 -> 284
/*       */     //   223: new 69	java/lang/IllegalArgumentException
/*       */     //   226: dup
/*       */     //   227: aload_0
/*       */     //   228: getfield 8	com/google/vrtoolkit/cardboard/sensors/NfcSensor:mCurrentNdef	Landroid/nfc/tech/Ndef;
/*       */     //   231: invokevirtual 50	android/nfc/tech/Ndef:getMaxSize	()I
/*       */     //   234: istore 6
/*       */     //   236: aload 4
/*       */     //   238: invokevirtual 68	android/nfc/NdefMessage:getByteArrayLength	()I
/*       */     //   241: istore 7
/*       */     //   243: new 70	java/lang/StringBuilder
/*       */     //   246: dup
/*       */     //   247: bipush 82
/*       */     //   249: invokespecial 71	java/lang/StringBuilder:<init>	(I)V
/*       */     //   252: ldc 72
/*       */     //   254: invokevirtual 73	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*       */     //   257: iload 6
/*       */     //   259: invokevirtual 74	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*       */     //   262: ldc 75
/*       */     //   264: invokevirtual 73	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*       */     //   267: iload 7
/*       */     //   269: invokevirtual 74	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*       */     //   272: ldc 76
/*       */     //   274: invokevirtual 73	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*       */     //   277: invokevirtual 77	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*       */     //   280: invokespecial 78	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
/*       */     //   283: athrow
/*       */     //   284: aload_0
/*       */     //   285: getfield 8	com/google/vrtoolkit/cardboard/sensors/NfcSensor:mCurrentNdef	Landroid/nfc/tech/Ndef;
/*       */     //   288: aload 4
/*       */     //   290: invokevirtual 79	android/nfc/tech/Ndef:writeNdefMessage	(Landroid/nfc/NdefMessage;)V
/*       */     //   293: goto +139 -> 432
/*       */     //   296: astore 6
/*       */     //   298: new 81	java/lang/RuntimeException
/*       */     //   301: dup
/*       */     //   302: ldc 82
/*       */     //   304: aload 6
/*       */     //   306: invokevirtual 83	android/nfc/FormatException:toString	()Ljava/lang/String;
/*       */     //   309: invokestatic 84	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*       */     //   312: dup
/*       */     //   313: invokevirtual 85	java/lang/String:length	()I
/*       */     //   316: ifeq +9 -> 325
/*       */     //   319: invokevirtual 86	java/lang/String:concat	(Ljava/lang/String;)Ljava/lang/String;
/*       */     //   322: goto +12 -> 334
/*       */     //   325: pop
/*       */     //   326: new 87	java/lang/String
/*       */     //   329: dup_x1
/*       */     //   330: swap
/*       */     //   331: invokespecial 88	java/lang/String:<init>	(Ljava/lang/String;)V
/*       */     //   334: invokespecial 89	java/lang/RuntimeException:<init>	(Ljava/lang/String;)V
/*       */     //   337: athrow
/*       */     //   338: aload_0
/*       */     //   339: getfield 51	com/google/vrtoolkit/cardboard/sensors/NfcSensor:mCurrentTag	Landroid/nfc/Tag;
/*       */     //   342: invokestatic 90	android/nfc/tech/NdefFormatable:get	(Landroid/nfc/Tag;)Landroid/nfc/tech/NdefFormatable;
/*       */     //   345: astore 6
/*       */     //   347: aload 6
/*       */     //   349: ifnonnull +13 -> 362
/*       */     //   352: new 91	java/io/IOException
/*       */     //   355: dup
/*       */     //   356: ldc 92
/*       */     //   358: invokespecial 93	java/io/IOException:<init>	(Ljava/lang/String;)V
/*       */     //   361: athrow
/*       */     //   362: ldc 94
/*       */     //   364: ldc 95
/*       */     //   366: invokestatic 96	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
/*       */     //   369: pop
/*       */     //   370: aload 6
/*       */     //   372: invokevirtual 97	android/nfc/tech/NdefFormatable:connect	()V
/*       */     //   375: aload 6
/*       */     //   377: aload 4
/*       */     //   379: invokevirtual 98	android/nfc/tech/NdefFormatable:format	(Landroid/nfc/NdefMessage;)V
/*       */     //   382: aload 6
/*       */     //   384: invokevirtual 99	android/nfc/tech/NdefFormatable:close	()V
/*       */     //   387: goto +45 -> 432
/*       */     //   390: astore 7
/*       */     //   392: new 81	java/lang/RuntimeException
/*       */     //   395: dup
/*       */     //   396: ldc 82
/*       */     //   398: aload 7
/*       */     //   400: invokevirtual 83	android/nfc/FormatException:toString	()Ljava/lang/String;
/*       */     //   403: invokestatic 84	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*       */     //   406: dup
/*       */     //   407: invokevirtual 85	java/lang/String:length	()I
/*       */     //   410: ifeq +9 -> 419
/*       */     //   413: invokevirtual 86	java/lang/String:concat	(Ljava/lang/String;)Ljava/lang/String;
/*       */     //   416: goto +12 -> 428
/*       */     //   419: pop
/*       */     //   420: new 87	java/lang/String
/*       */     //   423: dup_x1
/*       */     //   424: swap
/*       */     //   425: invokespecial 88	java/lang/String:<init>	(Ljava/lang/String;)V
/*       */     //   428: invokespecial 89	java/lang/RuntimeException:<init>	(Ljava/lang/String;)V
/*       */     //   431: athrow
/*       */     //   432: aload_0
/*       */     //   433: aload_0
/*       */     //   434: getfield 51	com/google/vrtoolkit/cardboard/sensors/NfcSensor:mCurrentTag	Landroid/nfc/Tag;
/*       */     //   437: invokespecial 100	com/google/vrtoolkit/cardboard/sensors/NfcSensor:onNewNfcTag	(Landroid/nfc/Tag;)V
/*       */     //   440: aload_2
/*       */     //   441: monitorexit
/*       */     //   442: goto +10 -> 452
/*       */     //   445: astore 12
/*       */     //   447: aload_2
/*       */     //   448: monitorexit
/*       */     //   449: aload 12
/*       */     //   451: athrow
/*       */     //   452: return
/*       */     //
/*       */     // Exception table:
/*       */     //   from	to	target	type
/*       */     //   35	40	43	java/lang/Exception
/*       */     //   284	293	296	android/nfc/FormatException
/*       */     //   370	387	390	android/nfc/FormatException
/*       */     //   7	442	445	finally
/*       */     //   445	449	445	finally } 
/*   352 */   public void onResume(Activity activity) { if (!isNfcEnabled()) {
/*   353 */       return;
/*       */     }
/*       */ 
/*   356 */     Intent intent = new Intent("android.nfc.action.NDEF_DISCOVERED");
/*   357 */     intent.setPackage(activity.getPackageName());
/*       */ 
/*   359 */     PendingIntent pendingIntent = PendingIntent.getBroadcast(this.mContext, 0, intent, 0);
/*   360 */     this.mNfcAdapter.enableForegroundDispatch(activity, pendingIntent, this.mNfcIntentFilters, (String[][])null);
/*       */   }
/*       */ 
/*       */   public void onPause(Activity activity)
/*       */   {
/*   369 */     if (!isNfcEnabled()) {
/*   370 */       return;
/*       */     }
/*       */ 
/*   373 */     this.mNfcAdapter.disableForegroundDispatch(activity);
/*       */   }
/*       */ 
/*       */   public void onNfcIntent(Intent intent)
/*       */   {
/*   382 */     if ((!isNfcEnabled()) || (intent == null) || (!this.mNfcIntentFilters[0].matchAction(intent.getAction())))
/*       */     {
/*   384 */       return;
/*       */     }
/*       */ 
/*   387 */     onNewNfcTag((Tag)intent.getParcelableExtra("android.nfc.extra.TAG")); } 
/*       */   // ERROR //
/*       */   private void onNewNfcTag(Tag nfcTag) { // Byte code:
/*       */     //   0: aload_1
/*       */     //   1: ifnonnull +4 -> 5
/*       */     //   4: return
/*       */     //   5: aload_0
/*       */     //   6: getfield 9	com/google/vrtoolkit/cardboard/sensors/NfcSensor:mTagLock	Ljava/lang/Object;
/*       */     //   9: dup
/*       */     //   10: astore_2
/*       */     //   11: monitorenter
/*       */     //   12: aload_0
/*       */     //   13: getfield 51	com/google/vrtoolkit/cardboard/sensors/NfcSensor:mCurrentTag	Landroid/nfc/Tag;
/*       */     //   16: astore_3
/*       */     //   17: aload_0
/*       */     //   18: getfield 8	com/google/vrtoolkit/cardboard/sensors/NfcSensor:mCurrentNdef	Landroid/nfc/tech/Ndef;
/*       */     //   21: astore 4
/*       */     //   23: aload_0
/*       */     //   24: getfield 44	com/google/vrtoolkit/cardboard/sensors/NfcSensor:mCurrentTagIsCardboard	Z
/*       */     //   27: istore 5
/*       */     //   29: aload_0
/*       */     //   30: invokespecial 6	com/google/vrtoolkit/cardboard/sensors/NfcSensor:closeCurrentNfcTag	()V
/*       */     //   33: aload_0
/*       */     //   34: aload_1
/*       */     //   35: putfield 51	com/google/vrtoolkit/cardboard/sensors/NfcSensor:mCurrentTag	Landroid/nfc/Tag;
/*       */     //   38: aload_0
/*       */     //   39: aload_1
/*       */     //   40: invokestatic 115	android/nfc/tech/Ndef:get	(Landroid/nfc/Tag;)Landroid/nfc/tech/Ndef;
/*       */     //   43: putfield 8	com/google/vrtoolkit/cardboard/sensors/NfcSensor:mCurrentNdef	Landroid/nfc/tech/Ndef;
/*       */     //   46: aload_0
/*       */     //   47: getfield 8	com/google/vrtoolkit/cardboard/sensors/NfcSensor:mCurrentNdef	Landroid/nfc/tech/Ndef;
/*       */     //   50: ifnonnull +15 -> 65
/*       */     //   53: iload 5
/*       */     //   55: ifeq +7 -> 62
/*       */     //   58: aload_0
/*       */     //   59: invokespecial 5	com/google/vrtoolkit/cardboard/sensors/NfcSensor:sendDisconnectionEvent	()V
/*       */     //   62: aload_2
/*       */     //   63: monitorexit
/*       */     //   64: return
/*       */     //   65: iconst_0
/*       */     //   66: istore 6
/*       */     //   68: aload 4
/*       */     //   70: ifnull +59 -> 129
/*       */     //   73: aload_0
/*       */     //   74: getfield 51	com/google/vrtoolkit/cardboard/sensors/NfcSensor:mCurrentTag	Landroid/nfc/Tag;
/*       */     //   77: invokevirtual 116	android/nfc/Tag:getId	()[B
/*       */     //   80: astore 7
/*       */     //   82: aload_3
/*       */     //   83: invokevirtual 116	android/nfc/Tag:getId	()[B
/*       */     //   86: astore 8
/*       */     //   88: aload 7
/*       */     //   90: ifnull +22 -> 112
/*       */     //   93: aload 8
/*       */     //   95: ifnull +17 -> 112
/*       */     //   98: aload 7
/*       */     //   100: aload 8
/*       */     //   102: invokestatic 117	java/util/Arrays:equals	([B[B)Z
/*       */     //   105: ifeq +7 -> 112
/*       */     //   108: iconst_1
/*       */     //   109: goto +4 -> 113
/*       */     //   112: iconst_0
/*       */     //   113: istore 6
/*       */     //   115: iload 6
/*       */     //   117: ifne +12 -> 129
/*       */     //   120: iload 5
/*       */     //   122: ifeq +7 -> 129
/*       */     //   125: aload_0
/*       */     //   126: invokespecial 5	com/google/vrtoolkit/cardboard/sensors/NfcSensor:sendDisconnectionEvent	()V
/*       */     //   129: aload_0
/*       */     //   130: getfield 8	com/google/vrtoolkit/cardboard/sensors/NfcSensor:mCurrentNdef	Landroid/nfc/tech/Ndef;
/*       */     //   133: invokevirtual 67	android/nfc/tech/Ndef:connect	()V
/*       */     //   136: aload_0
/*       */     //   137: getfield 8	com/google/vrtoolkit/cardboard/sensors/NfcSensor:mCurrentNdef	Landroid/nfc/tech/Ndef;
/*       */     //   140: invokevirtual 45	android/nfc/tech/Ndef:getCachedNdefMessage	()Landroid/nfc/NdefMessage;
/*       */     //   143: astore 7
/*       */     //   145: goto +60 -> 205
/*       */     //   148: astore 8
/*       */     //   150: ldc 94
/*       */     //   152: ldc 118
/*       */     //   154: aload 8
/*       */     //   156: invokevirtual 119	java/lang/Exception:toString	()Ljava/lang/String;
/*       */     //   159: invokestatic 84	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*       */     //   162: dup
/*       */     //   163: invokevirtual 85	java/lang/String:length	()I
/*       */     //   166: ifeq +9 -> 175
/*       */     //   169: invokevirtual 86	java/lang/String:concat	(Ljava/lang/String;)Ljava/lang/String;
/*       */     //   172: goto +12 -> 184
/*       */     //   175: pop
/*       */     //   176: new 87	java/lang/String
/*       */     //   179: dup_x1
/*       */     //   180: swap
/*       */     //   181: invokespecial 88	java/lang/String:<init>	(Ljava/lang/String;)V
/*       */     //   184: invokestatic 120	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
/*       */     //   187: pop
/*       */     //   188: iload 6
/*       */     //   190: ifeq +12 -> 202
/*       */     //   193: iload 5
/*       */     //   195: ifeq +7 -> 202
/*       */     //   198: aload_0
/*       */     //   199: invokespecial 5	com/google/vrtoolkit/cardboard/sensors/NfcSensor:sendDisconnectionEvent	()V
/*       */     //   202: aload_2
/*       */     //   203: monitorexit
/*       */     //   204: return
/*       */     //   205: aload_0
/*       */     //   206: aload_0
/*       */     //   207: aload 7
/*       */     //   209: invokespecial 121	com/google/vrtoolkit/cardboard/sensors/NfcSensor:isCardboardNdefMessage	(Landroid/nfc/NdefMessage;)Z
/*       */     //   212: putfield 44	com/google/vrtoolkit/cardboard/sensors/NfcSensor:mCurrentTagIsCardboard	Z
/*       */     //   215: iload 6
/*       */     //   217: ifne +78 -> 295
/*       */     //   220: aload_0
/*       */     //   221: getfield 44	com/google/vrtoolkit/cardboard/sensors/NfcSensor:mCurrentTagIsCardboard	Z
/*       */     //   224: ifeq +71 -> 295
/*       */     //   227: aload_0
/*       */     //   228: getfield 20	com/google/vrtoolkit/cardboard/sensors/NfcSensor:mListeners	Ljava/util/List;
/*       */     //   231: dup
/*       */     //   232: astore 8
/*       */     //   234: monitorenter
/*       */     //   235: aload_0
/*       */     //   236: getfield 20	com/google/vrtoolkit/cardboard/sensors/NfcSensor:mListeners	Ljava/util/List;
/*       */     //   239: invokeinterface 32 1 0
/*       */     //   244: astore 9
/*       */     //   246: aload 9
/*       */     //   248: invokeinterface 33 1 0
/*       */     //   253: ifeq +28 -> 281
/*       */     //   256: aload 9
/*       */     //   258: invokeinterface 34 1 0
/*       */     //   263: checkcast 35	com/google/vrtoolkit/cardboard/sensors/NfcSensor$ListenerHelper
/*       */     //   266: astore 10
/*       */     //   268: aload 10
/*       */     //   270: aload 7
/*       */     //   272: invokestatic 122	com/google/vrtoolkit/cardboard/CardboardDeviceParams:createFromNfcContents	(Landroid/nfc/NdefMessage;)Lcom/google/vrtoolkit/cardboard/CardboardDeviceParams;
/*       */     //   275: invokevirtual 123	com/google/vrtoolkit/cardboard/sensors/NfcSensor$ListenerHelper:onInsertedIntoCardboard	(Lcom/google/vrtoolkit/cardboard/CardboardDeviceParams;)V
/*       */     //   278: goto -32 -> 246
/*       */     //   281: aload 8
/*       */     //   283: monitorexit
/*       */     //   284: goto +11 -> 295
/*       */     //   287: astore 11
/*       */     //   289: aload 8
/*       */     //   291: monitorexit
/*       */     //   292: aload 11
/*       */     //   294: athrow
/*       */     //   295: aload_0
/*       */     //   296: getfield 44	com/google/vrtoolkit/cardboard/sensors/NfcSensor:mCurrentTagIsCardboard	Z
/*       */     //   299: ifeq +42 -> 341
/*       */     //   302: aload_0
/*       */     //   303: iconst_0
/*       */     //   304: putfield 7	com/google/vrtoolkit/cardboard/sensors/NfcSensor:mTagConnectionFailures	I
/*       */     //   307: aload_0
/*       */     //   308: new 124	java/util/Timer
/*       */     //   311: dup
/*       */     //   312: ldc 125
/*       */     //   314: invokespecial 126	java/util/Timer:<init>	(Ljava/lang/String;)V
/*       */     //   317: putfield 127	com/google/vrtoolkit/cardboard/sensors/NfcSensor:mNfcDisconnectTimer	Ljava/util/Timer;
/*       */     //   320: aload_0
/*       */     //   321: getfield 127	com/google/vrtoolkit/cardboard/sensors/NfcSensor:mNfcDisconnectTimer	Ljava/util/Timer;
/*       */     //   324: new 128	com/google/vrtoolkit/cardboard/sensors/NfcSensor$2
/*       */     //   327: dup
/*       */     //   328: aload_0
/*       */     //   329: invokespecial 129	com/google/vrtoolkit/cardboard/sensors/NfcSensor$2:<init>	(Lcom/google/vrtoolkit/cardboard/sensors/NfcSensor;)V
/*       */     //   332: ldc2_w 130
/*       */     //   335: ldc2_w 130
/*       */     //   338: invokevirtual 132	java/util/Timer:schedule	(Ljava/util/TimerTask;JJ)V
/*       */     //   341: aload_2
/*       */     //   342: monitorexit
/*       */     //   343: goto +10 -> 353
/*       */     //   346: astore 12
/*       */     //   348: aload_2
/*       */     //   349: monitorexit
/*       */     //   350: aload 12
/*       */     //   352: athrow
/*       */     //   353: return
/*       */     //
/*       */     // Exception table:
/*       */     //   from	to	target	type
/*       */     //   129	145	148	java/lang/Exception
/*       */     //   235	284	287	finally
/*       */     //   287	292	287	finally
/*       */     //   12	64	346	finally
/*       */     //   65	204	346	finally
/*       */     //   205	343	346	finally
/*       */     //   346	350	346	finally } 
/*   485 */   private void closeCurrentNfcTag() { if (this.mNfcDisconnectTimer != null) {
/*   486 */       this.mNfcDisconnectTimer.cancel();
/*       */     }
/*       */ 
/*   489 */     if (this.mCurrentNdef == null) {
/*   490 */       return;
/*       */     }
/*       */ 
/*       */     try
/*       */     {
/*   495 */       this.mCurrentNdef.close();
/*       */     } catch (IOException e) {
/*   497 */       Log.w("NfcSensor", e.toString());
/*       */     }
/*       */ 
/*   500 */     this.mCurrentTag = null;
/*   501 */     this.mCurrentNdef = null;
/*   502 */     this.mCurrentTagIsCardboard = false; }
/*       */ 
/*       */   private void sendDisconnectionEvent()
/*       */   {
/*   506 */     synchronized (this.mListeners) {
/*   507 */       for (ListenerHelper listener : this.mListeners)
/*   508 */         listener.onRemovedFromCardboard();
/*       */     }
/*       */   }
/*       */ 
/*       */   private boolean isCardboardNdefMessage(NdefMessage message)
/*       */   {
/*   514 */     if (message == null) {
/*   515 */       return false;
/*       */     }
/*       */ 
/*   518 */     for (NdefRecord record : message.getRecords()) {
/*   519 */       if (isCardboardNdefRecord(record)) {
/*   520 */         return true;
/*       */       }
/*       */     }
/*       */ 
/*   524 */     return false;
/*       */   }
/*       */ 
/*       */   private boolean isCardboardNdefRecord(NdefRecord record) {
/*   528 */     if (record == null) {
/*   529 */       return false;
/*       */     }
/*       */ 
/*   532 */     Uri uri = record.toUri();
/*   533 */     return (uri != null) && (CardboardDeviceParams.isCardboardUri(uri));
/*       */   }
/*       */ 
/*       */   private static class ListenerHelper
/*       */     implements NfcSensor.OnCardboardNfcListener
/*       */   {
/*       */     private NfcSensor.OnCardboardNfcListener mListener;
/*       */     private Handler mHandler;
/*       */ 
/*       */     public ListenerHelper(NfcSensor.OnCardboardNfcListener listener, Handler handler)
/*       */     {
/*   545 */       this.mListener = listener;
/*   546 */       this.mHandler = handler;
/*       */     }
/*       */ 
/*       */     public NfcSensor.OnCardboardNfcListener getListener() {
/*   550 */       return this.mListener;
/*       */     }
/*       */ 
/*       */     public void onInsertedIntoCardboard(final CardboardDeviceParams deviceParams)
/*       */     {
/*   555 */       this.mHandler.post(new Runnable()
/*       */       {
/*       */         public void run() {
/*   558 */           NfcSensor.ListenerHelper.this.mListener.onInsertedIntoCardboard(deviceParams);
/*       */         }
/*       */       });
/*       */     }
/*       */ 
/*       */     public void onRemovedFromCardboard()
/*       */     {
/*   565 */       this.mHandler.post(new Runnable()
/*       */       {
/*       */         public void run() {
/*   568 */           NfcSensor.ListenerHelper.this.mListener.onRemovedFromCardboard();
/*       */         }
/*       */       });
/*       */     }
/*       */   }
/*       */ 
/*       */   public static abstract interface OnCardboardNfcListener
/*       */   {
/*       */     public abstract void onInsertedIntoCardboard(CardboardDeviceParams paramCardboardDeviceParams);
/*       */ 
/*       */     public abstract void onRemovedFromCardboard();
/*       */   }
/*       */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.sensors.NfcSensor
 * JD-Core Version:    0.6.2
 */