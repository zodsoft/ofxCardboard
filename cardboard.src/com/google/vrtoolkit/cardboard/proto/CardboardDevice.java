/*     */ package com.google.vrtoolkit.cardboard.proto;
/*     */ 
/*     */ import com.google.protobuf.nano.CodedInputByteBufferNano;
/*     */ import com.google.protobuf.nano.CodedOutputByteBufferNano;
/*     */ import com.google.protobuf.nano.InternalNano;
/*     */ import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
/*     */ import com.google.protobuf.nano.MessageNano;
/*     */ import com.google.protobuf.nano.WireFormatNano;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public abstract interface CardboardDevice
/*     */ {
/*     */   public static final class DeviceParams extends MessageNano
/*     */   {
/*     */     private static volatile DeviceParams[] _emptyArray;
/*     */     private int bitField0_;
/*     */     private String vendor_;
/*     */     private String model_;
/*     */     private float screenToLensDistance_;
/*     */     private float interLensDistance_;
/*     */     public float[] leftEyeFieldOfViewAngles;
/*     */     private float trayBottomToLensHeight_;
/*     */     public float[] distortionCoefficients;
/*     */     private boolean hasMagnet_;
/*     */ 
/*     */     public static DeviceParams[] emptyArray()
/*     */     {
/*  14 */       if (_emptyArray == null) {
/*  15 */         synchronized (InternalNano.LAZY_INIT_LOCK)
/*     */         {
/*  17 */           if (_emptyArray == null) {
/*  18 */             _emptyArray = new DeviceParams[0];
/*     */           }
/*     */         }
/*     */       }
/*  22 */       return _emptyArray;
/*     */     }
/*     */ 
/*     */     public String getVendor()
/*     */     {
/*  30 */       return this.vendor_;
/*     */     }
/*     */     public DeviceParams setVendor(String value) {
/*  33 */       if (value == null) {
/*  34 */         throw new NullPointerException();
/*     */       }
/*  36 */       this.vendor_ = value;
/*  37 */       this.bitField0_ |= 1;
/*  38 */       return this;
/*     */     }
/*     */     public boolean hasVendor() {
/*  41 */       return (this.bitField0_ & 0x1) != 0;
/*     */     }
/*     */     public DeviceParams clearVendor() {
/*  44 */       this.vendor_ = "";
/*  45 */       this.bitField0_ &= -2;
/*  46 */       return this;
/*     */     }
/*     */ 
/*     */     public String getModel()
/*     */     {
/*  52 */       return this.model_;
/*     */     }
/*     */     public DeviceParams setModel(String value) {
/*  55 */       if (value == null) {
/*  56 */         throw new NullPointerException();
/*     */       }
/*  58 */       this.model_ = value;
/*  59 */       this.bitField0_ |= 2;
/*  60 */       return this;
/*     */     }
/*     */     public boolean hasModel() {
/*  63 */       return (this.bitField0_ & 0x2) != 0;
/*     */     }
/*     */     public DeviceParams clearModel() {
/*  66 */       this.model_ = "";
/*  67 */       this.bitField0_ &= -3;
/*  68 */       return this;
/*     */     }
/*     */ 
/*     */     public float getScreenToLensDistance()
/*     */     {
/*  74 */       return this.screenToLensDistance_;
/*     */     }
/*     */     public DeviceParams setScreenToLensDistance(float value) {
/*  77 */       this.screenToLensDistance_ = value;
/*  78 */       this.bitField0_ |= 4;
/*  79 */       return this;
/*     */     }
/*     */     public boolean hasScreenToLensDistance() {
/*  82 */       return (this.bitField0_ & 0x4) != 0;
/*     */     }
/*     */     public DeviceParams clearScreenToLensDistance() {
/*  85 */       this.screenToLensDistance_ = 0.0F;
/*  86 */       this.bitField0_ &= -5;
/*  87 */       return this;
/*     */     }
/*     */ 
/*     */     public float getInterLensDistance()
/*     */     {
/*  93 */       return this.interLensDistance_;
/*     */     }
/*     */     public DeviceParams setInterLensDistance(float value) {
/*  96 */       this.interLensDistance_ = value;
/*  97 */       this.bitField0_ |= 8;
/*  98 */       return this;
/*     */     }
/*     */     public boolean hasInterLensDistance() {
/* 101 */       return (this.bitField0_ & 0x8) != 0;
/*     */     }
/*     */     public DeviceParams clearInterLensDistance() {
/* 104 */       this.interLensDistance_ = 0.0F;
/* 105 */       this.bitField0_ &= -9;
/* 106 */       return this;
/*     */     }
/*     */ 
/*     */     public float getTrayBottomToLensHeight()
/*     */     {
/* 115 */       return this.trayBottomToLensHeight_;
/*     */     }
/*     */     public DeviceParams setTrayBottomToLensHeight(float value) {
/* 118 */       this.trayBottomToLensHeight_ = value;
/* 119 */       this.bitField0_ |= 16;
/* 120 */       return this;
/*     */     }
/*     */     public boolean hasTrayBottomToLensHeight() {
/* 123 */       return (this.bitField0_ & 0x10) != 0;
/*     */     }
/*     */     public DeviceParams clearTrayBottomToLensHeight() {
/* 126 */       this.trayBottomToLensHeight_ = 0.0F;
/* 127 */       this.bitField0_ &= -17;
/* 128 */       return this;
/*     */     }
/*     */ 
/*     */     public boolean getHasMagnet()
/*     */     {
/* 137 */       return this.hasMagnet_;
/*     */     }
/*     */     public DeviceParams setHasMagnet(boolean value) {
/* 140 */       this.hasMagnet_ = value;
/* 141 */       this.bitField0_ |= 32;
/* 142 */       return this;
/*     */     }
/*     */     public boolean hasHasMagnet() {
/* 145 */       return (this.bitField0_ & 0x20) != 0;
/*     */     }
/*     */     public DeviceParams clearHasMagnet() {
/* 148 */       this.hasMagnet_ = false;
/* 149 */       this.bitField0_ &= -33;
/* 150 */       return this;
/*     */     }
/*     */ 
/*     */     public DeviceParams() {
/* 154 */       clear();
/*     */     }
/*     */ 
/*     */     public DeviceParams clear() {
/* 158 */       this.bitField0_ = 0;
/* 159 */       this.vendor_ = "";
/* 160 */       this.model_ = "";
/* 161 */       this.screenToLensDistance_ = 0.0F;
/* 162 */       this.interLensDistance_ = 0.0F;
/* 163 */       this.leftEyeFieldOfViewAngles = WireFormatNano.EMPTY_FLOAT_ARRAY;
/* 164 */       this.trayBottomToLensHeight_ = 0.0F;
/* 165 */       this.distortionCoefficients = WireFormatNano.EMPTY_FLOAT_ARRAY;
/* 166 */       this.hasMagnet_ = false;
/* 167 */       this.cachedSize = -1;
/* 168 */       return this;
/*     */     }
/*     */ 
/*     */     public void writeTo(CodedOutputByteBufferNano output)
/*     */       throws IOException
/*     */     {
/* 174 */       if ((this.bitField0_ & 0x1) != 0) {
/* 175 */         output.writeString(1, this.vendor_);
/*     */       }
/* 177 */       if ((this.bitField0_ & 0x2) != 0) {
/* 178 */         output.writeString(2, this.model_);
/*     */       }
/* 180 */       if ((this.bitField0_ & 0x4) != 0) {
/* 181 */         output.writeFloat(3, this.screenToLensDistance_);
/*     */       }
/* 183 */       if ((this.bitField0_ & 0x8) != 0) {
/* 184 */         output.writeFloat(4, this.interLensDistance_);
/*     */       }
/* 186 */       if ((this.leftEyeFieldOfViewAngles != null) && (this.leftEyeFieldOfViewAngles.length > 0)) {
/* 187 */         int dataSize = 4 * this.leftEyeFieldOfViewAngles.length;
/* 188 */         output.writeRawVarint32(42);
/* 189 */         output.writeRawVarint32(dataSize);
/* 190 */         for (int i = 0; i < this.leftEyeFieldOfViewAngles.length; i++) {
/* 191 */           output.writeFloatNoTag(this.leftEyeFieldOfViewAngles[i]);
/*     */         }
/*     */       }
/* 194 */       if ((this.bitField0_ & 0x10) != 0) {
/* 195 */         output.writeFloat(6, this.trayBottomToLensHeight_);
/*     */       }
/* 197 */       if ((this.distortionCoefficients != null) && (this.distortionCoefficients.length > 0)) {
/* 198 */         int dataSize = 4 * this.distortionCoefficients.length;
/* 199 */         output.writeRawVarint32(58);
/* 200 */         output.writeRawVarint32(dataSize);
/* 201 */         for (int i = 0; i < this.distortionCoefficients.length; i++) {
/* 202 */           output.writeFloatNoTag(this.distortionCoefficients[i]);
/*     */         }
/*     */       }
/* 205 */       if ((this.bitField0_ & 0x20) != 0) {
/* 206 */         output.writeBool(10, this.hasMagnet_);
/*     */       }
/* 208 */       super.writeTo(output);
/*     */     }
/*     */ 
/*     */     protected int computeSerializedSize()
/*     */     {
/* 213 */       int size = super.computeSerializedSize();
/* 214 */       if ((this.bitField0_ & 0x1) != 0) {
/* 215 */         size += CodedOutputByteBufferNano.computeStringSize(1, this.vendor_);
/*     */       }
/*     */ 
/* 218 */       if ((this.bitField0_ & 0x2) != 0) {
/* 219 */         size += CodedOutputByteBufferNano.computeStringSize(2, this.model_);
/*     */       }
/*     */ 
/* 222 */       if ((this.bitField0_ & 0x4) != 0) {
/* 223 */         size += CodedOutputByteBufferNano.computeFloatSize(3, this.screenToLensDistance_);
/*     */       }
/*     */ 
/* 226 */       if ((this.bitField0_ & 0x8) != 0) {
/* 227 */         size += CodedOutputByteBufferNano.computeFloatSize(4, this.interLensDistance_);
/*     */       }
/*     */ 
/* 230 */       if ((this.leftEyeFieldOfViewAngles != null) && (this.leftEyeFieldOfViewAngles.length > 0)) {
/* 231 */         int dataSize = 4 * this.leftEyeFieldOfViewAngles.length;
/* 232 */         size += dataSize;
/* 233 */         size++;
/* 234 */         size += CodedOutputByteBufferNano.computeRawVarint32Size(dataSize);
/*     */       }
/*     */ 
/* 237 */       if ((this.bitField0_ & 0x10) != 0) {
/* 238 */         size += CodedOutputByteBufferNano.computeFloatSize(6, this.trayBottomToLensHeight_);
/*     */       }
/*     */ 
/* 241 */       if ((this.distortionCoefficients != null) && (this.distortionCoefficients.length > 0)) {
/* 242 */         int dataSize = 4 * this.distortionCoefficients.length;
/* 243 */         size += dataSize;
/* 244 */         size++;
/* 245 */         size += CodedOutputByteBufferNano.computeRawVarint32Size(dataSize);
/*     */       }
/*     */ 
/* 248 */       if ((this.bitField0_ & 0x20) != 0) {
/* 249 */         size += CodedOutputByteBufferNano.computeBoolSize(10, this.hasMagnet_);
/*     */       }
/*     */ 
/* 252 */       return size;
/*     */     }
/*     */ 
/*     */     public DeviceParams mergeFrom(CodedInputByteBufferNano input)
/*     */       throws IOException
/*     */     {
/*     */       while (true)
/*     */       {
/* 260 */         int tag = input.readTag();
/* 261 */         switch (tag) {
/*     */         case 0:
/* 263 */           return this;
/*     */         default:
/* 265 */           if (!WireFormatNano.parseUnknownField(input, tag)) {
/* 266 */             return this;
/*     */           }
/*     */ 
/*     */           break;
/*     */         case 10:
/* 271 */           this.vendor_ = input.readString();
/* 272 */           this.bitField0_ |= 1;
/* 273 */           break;
/*     */         case 18:
/* 276 */           this.model_ = input.readString();
/* 277 */           this.bitField0_ |= 2;
/* 278 */           break;
/*     */         case 29:
/* 281 */           this.screenToLensDistance_ = input.readFloat();
/* 282 */           this.bitField0_ |= 4;
/* 283 */           break;
/*     */         case 37:
/* 286 */           this.interLensDistance_ = input.readFloat();
/* 287 */           this.bitField0_ |= 8;
/* 288 */           break;
/*     */         case 45:
/* 291 */           int arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 45);
/*     */ 
/* 293 */           int i = this.leftEyeFieldOfViewAngles == null ? 0 : this.leftEyeFieldOfViewAngles.length;
/* 294 */           float[] newArray = new float[i + arrayLength];
/* 295 */           if (i != 0) {
/* 296 */             System.arraycopy(this.leftEyeFieldOfViewAngles, 0, newArray, 0, i);
/*     */           }
/* 298 */           for (; i < newArray.length - 1; i++) {
/* 299 */             newArray[i] = input.readFloat();
/* 300 */             input.readTag();
/*     */           }
/*     */ 
/* 303 */           newArray[i] = input.readFloat();
/* 304 */           this.leftEyeFieldOfViewAngles = newArray;
/* 305 */           break;
/*     */         case 42:
/* 308 */           int length = input.readRawVarint32();
/* 309 */           int limit = input.pushLimit(length);
/* 310 */           int arrayLength = length / 4;
/* 311 */           int i = this.leftEyeFieldOfViewAngles == null ? 0 : this.leftEyeFieldOfViewAngles.length;
/* 312 */           float[] newArray = new float[i + arrayLength];
/* 313 */           if (i != 0) {
/* 314 */             System.arraycopy(this.leftEyeFieldOfViewAngles, 0, newArray, 0, i);
/*     */           }
/* 316 */           for (; i < newArray.length; i++) {
/* 317 */             newArray[i] = input.readFloat();
/*     */           }
/* 319 */           this.leftEyeFieldOfViewAngles = newArray;
/* 320 */           input.popLimit(limit);
/* 321 */           break;
/*     */         case 53:
/* 324 */           this.trayBottomToLensHeight_ = input.readFloat();
/* 325 */           this.bitField0_ |= 16;
/* 326 */           break;
/*     */         case 61:
/* 329 */           int arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 61);
/*     */ 
/* 331 */           int i = this.distortionCoefficients == null ? 0 : this.distortionCoefficients.length;
/* 332 */           float[] newArray = new float[i + arrayLength];
/* 333 */           if (i != 0) {
/* 334 */             System.arraycopy(this.distortionCoefficients, 0, newArray, 0, i);
/*     */           }
/* 336 */           for (; i < newArray.length - 1; i++) {
/* 337 */             newArray[i] = input.readFloat();
/* 338 */             input.readTag();
/*     */           }
/*     */ 
/* 341 */           newArray[i] = input.readFloat();
/* 342 */           this.distortionCoefficients = newArray;
/* 343 */           break;
/*     */         case 58:
/* 346 */           int length = input.readRawVarint32();
/* 347 */           int limit = input.pushLimit(length);
/* 348 */           int arrayLength = length / 4;
/* 349 */           int i = this.distortionCoefficients == null ? 0 : this.distortionCoefficients.length;
/* 350 */           float[] newArray = new float[i + arrayLength];
/* 351 */           if (i != 0) {
/* 352 */             System.arraycopy(this.distortionCoefficients, 0, newArray, 0, i);
/*     */           }
/* 354 */           for (; i < newArray.length; i++) {
/* 355 */             newArray[i] = input.readFloat();
/*     */           }
/* 357 */           this.distortionCoefficients = newArray;
/* 358 */           input.popLimit(limit);
/* 359 */           break;
/*     */         case 80:
/* 362 */           this.hasMagnet_ = input.readBool();
/* 363 */           this.bitField0_ |= 32;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public static DeviceParams parseFrom(byte[] data)
/*     */       throws InvalidProtocolBufferNanoException
/*     */     {
/* 372 */       return (DeviceParams)MessageNano.mergeFrom(new DeviceParams(), data);
/*     */     }
/*     */ 
/*     */     public static DeviceParams parseFrom(CodedInputByteBufferNano input)
/*     */       throws IOException
/*     */     {
/* 378 */       return new DeviceParams().mergeFrom(input);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.proto.CardboardDevice
 * JD-Core Version:    0.6.2
 */