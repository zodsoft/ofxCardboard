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
/*     */     implements Cloneable
/*     */   {
/*     */     private static volatile DeviceParams[] _emptyArray;
/*     */     private int bitField0_;
/*     */     private String vendor_;
/*     */     private String model_;
/*     */     private float screenToLensDistance_;
/*     */     private float interLensDistance_;
/*     */     public float[] leftEyeFieldOfViewAngles;
/*     */     private int verticalAlignment_;
/*     */     private float trayToLensDistance_;
/*     */     public float[] distortionCoefficients;
/*     */     private boolean hasMagnet_;
/*     */ 
/*     */     public static DeviceParams[] emptyArray()
/*     */     {
/*  21 */       if (_emptyArray == null) {
/*  22 */         synchronized (InternalNano.LAZY_INIT_LOCK)
/*     */         {
/*  24 */           if (_emptyArray == null) {
/*  25 */             _emptyArray = new DeviceParams[0];
/*     */           }
/*     */         }
/*     */       }
/*  29 */       return _emptyArray;
/*     */     }
/*     */ 
/*     */     public String getVendor()
/*     */     {
/*  37 */       return this.vendor_;
/*     */     }
/*     */     public DeviceParams setVendor(String value) {
/*  40 */       if (value == null) {
/*  41 */         throw new NullPointerException();
/*     */       }
/*  43 */       this.vendor_ = value;
/*  44 */       this.bitField0_ |= 1;
/*  45 */       return this;
/*     */     }
/*     */     public boolean hasVendor() {
/*  48 */       return (this.bitField0_ & 0x1) != 0;
/*     */     }
/*     */     public DeviceParams clearVendor() {
/*  51 */       this.vendor_ = "";
/*  52 */       this.bitField0_ &= -2;
/*  53 */       return this;
/*     */     }
/*     */ 
/*     */     public String getModel()
/*     */     {
/*  59 */       return this.model_;
/*     */     }
/*     */     public DeviceParams setModel(String value) {
/*  62 */       if (value == null) {
/*  63 */         throw new NullPointerException();
/*     */       }
/*  65 */       this.model_ = value;
/*  66 */       this.bitField0_ |= 2;
/*  67 */       return this;
/*     */     }
/*     */     public boolean hasModel() {
/*  70 */       return (this.bitField0_ & 0x2) != 0;
/*     */     }
/*     */     public DeviceParams clearModel() {
/*  73 */       this.model_ = "";
/*  74 */       this.bitField0_ &= -3;
/*  75 */       return this;
/*     */     }
/*     */ 
/*     */     public float getScreenToLensDistance()
/*     */     {
/*  81 */       return this.screenToLensDistance_;
/*     */     }
/*     */     public DeviceParams setScreenToLensDistance(float value) {
/*  84 */       this.screenToLensDistance_ = value;
/*  85 */       this.bitField0_ |= 4;
/*  86 */       return this;
/*     */     }
/*     */     public boolean hasScreenToLensDistance() {
/*  89 */       return (this.bitField0_ & 0x4) != 0;
/*     */     }
/*     */     public DeviceParams clearScreenToLensDistance() {
/*  92 */       this.screenToLensDistance_ = 0.0F;
/*  93 */       this.bitField0_ &= -5;
/*  94 */       return this;
/*     */     }
/*     */ 
/*     */     public float getInterLensDistance()
/*     */     {
/* 100 */       return this.interLensDistance_;
/*     */     }
/*     */     public DeviceParams setInterLensDistance(float value) {
/* 103 */       this.interLensDistance_ = value;
/* 104 */       this.bitField0_ |= 8;
/* 105 */       return this;
/*     */     }
/*     */     public boolean hasInterLensDistance() {
/* 108 */       return (this.bitField0_ & 0x8) != 0;
/*     */     }
/*     */     public DeviceParams clearInterLensDistance() {
/* 111 */       this.interLensDistance_ = 0.0F;
/* 112 */       this.bitField0_ &= -9;
/* 113 */       return this;
/*     */     }
/*     */ 
/*     */     public int getVerticalAlignment()
/*     */     {
/* 122 */       return this.verticalAlignment_;
/*     */     }
/*     */     public DeviceParams setVerticalAlignment(int value) {
/* 125 */       this.verticalAlignment_ = value;
/* 126 */       this.bitField0_ |= 16;
/* 127 */       return this;
/*     */     }
/*     */     public boolean hasVerticalAlignment() {
/* 130 */       return (this.bitField0_ & 0x10) != 0;
/*     */     }
/*     */     public DeviceParams clearVerticalAlignment() {
/* 133 */       this.verticalAlignment_ = 0;
/* 134 */       this.bitField0_ &= -17;
/* 135 */       return this;
/*     */     }
/*     */ 
/*     */     public float getTrayToLensDistance()
/*     */     {
/* 141 */       return this.trayToLensDistance_;
/*     */     }
/*     */     public DeviceParams setTrayToLensDistance(float value) {
/* 144 */       this.trayToLensDistance_ = value;
/* 145 */       this.bitField0_ |= 32;
/* 146 */       return this;
/*     */     }
/*     */     public boolean hasTrayToLensDistance() {
/* 149 */       return (this.bitField0_ & 0x20) != 0;
/*     */     }
/*     */     public DeviceParams clearTrayToLensDistance() {
/* 152 */       this.trayToLensDistance_ = 0.0F;
/* 153 */       this.bitField0_ &= -33;
/* 154 */       return this;
/*     */     }
/*     */ 
/*     */     public boolean getHasMagnet()
/*     */     {
/* 163 */       return this.hasMagnet_;
/*     */     }
/*     */     public DeviceParams setHasMagnet(boolean value) {
/* 166 */       this.hasMagnet_ = value;
/* 167 */       this.bitField0_ |= 64;
/* 168 */       return this;
/*     */     }
/*     */     public boolean hasHasMagnet() {
/* 171 */       return (this.bitField0_ & 0x40) != 0;
/*     */     }
/*     */     public DeviceParams clearHasMagnet() {
/* 174 */       this.hasMagnet_ = false;
/* 175 */       this.bitField0_ &= -65;
/* 176 */       return this;
/*     */     }
/*     */ 
/*     */     public DeviceParams() {
/* 180 */       clear();
/*     */     }
/*     */ 
/*     */     public DeviceParams clear() {
/* 184 */       this.bitField0_ = 0;
/* 185 */       this.vendor_ = "";
/* 186 */       this.model_ = "";
/* 187 */       this.screenToLensDistance_ = 0.0F;
/* 188 */       this.interLensDistance_ = 0.0F;
/* 189 */       this.leftEyeFieldOfViewAngles = WireFormatNano.EMPTY_FLOAT_ARRAY;
/* 190 */       this.verticalAlignment_ = 0;
/* 191 */       this.trayToLensDistance_ = 0.0F;
/* 192 */       this.distortionCoefficients = WireFormatNano.EMPTY_FLOAT_ARRAY;
/* 193 */       this.hasMagnet_ = false;
/* 194 */       this.cachedSize = -1;
/* 195 */       return this;
/*     */     }
/*     */ 
/*     */     public DeviceParams clone() {
/*     */       DeviceParams cloned;
/*     */       try {
/* 201 */         cloned = (DeviceParams)super.clone();
/*     */       } catch (CloneNotSupportedException e) {
/* 203 */         throw new AssertionError(e);
/*     */       }
/* 205 */       if ((this.leftEyeFieldOfViewAngles != null) && (this.leftEyeFieldOfViewAngles.length > 0)) {
/* 206 */         cloned.leftEyeFieldOfViewAngles = ((float[])this.leftEyeFieldOfViewAngles.clone());
/*     */       }
/* 208 */       if ((this.distortionCoefficients != null) && (this.distortionCoefficients.length > 0)) {
/* 209 */         cloned.distortionCoefficients = ((float[])this.distortionCoefficients.clone());
/*     */       }
/* 211 */       return cloned;
/*     */     }
/*     */ 
/*     */     public void writeTo(CodedOutputByteBufferNano output)
/*     */       throws IOException
/*     */     {
/* 218 */       if ((this.bitField0_ & 0x1) != 0) {
/* 219 */         output.writeString(1, this.vendor_);
/*     */       }
/* 221 */       if ((this.bitField0_ & 0x2) != 0) {
/* 222 */         output.writeString(2, this.model_);
/*     */       }
/* 224 */       if ((this.bitField0_ & 0x4) != 0) {
/* 225 */         output.writeFloat(3, this.screenToLensDistance_);
/*     */       }
/* 227 */       if ((this.bitField0_ & 0x8) != 0) {
/* 228 */         output.writeFloat(4, this.interLensDistance_);
/*     */       }
/* 230 */       if ((this.leftEyeFieldOfViewAngles != null) && (this.leftEyeFieldOfViewAngles.length > 0)) {
/* 231 */         int dataSize = 4 * this.leftEyeFieldOfViewAngles.length;
/* 232 */         output.writeRawVarint32(42);
/* 233 */         output.writeRawVarint32(dataSize);
/* 234 */         for (int i = 0; i < this.leftEyeFieldOfViewAngles.length; i++) {
/* 235 */           output.writeFloatNoTag(this.leftEyeFieldOfViewAngles[i]);
/*     */         }
/*     */       }
/* 238 */       if ((this.bitField0_ & 0x20) != 0) {
/* 239 */         output.writeFloat(6, this.trayToLensDistance_);
/*     */       }
/* 241 */       if ((this.distortionCoefficients != null) && (this.distortionCoefficients.length > 0)) {
/* 242 */         int dataSize = 4 * this.distortionCoefficients.length;
/* 243 */         output.writeRawVarint32(58);
/* 244 */         output.writeRawVarint32(dataSize);
/* 245 */         for (int i = 0; i < this.distortionCoefficients.length; i++) {
/* 246 */           output.writeFloatNoTag(this.distortionCoefficients[i]);
/*     */         }
/*     */       }
/* 249 */       if ((this.bitField0_ & 0x40) != 0) {
/* 250 */         output.writeBool(10, this.hasMagnet_);
/*     */       }
/* 252 */       if ((this.bitField0_ & 0x10) != 0) {
/* 253 */         output.writeInt32(11, this.verticalAlignment_);
/*     */       }
/* 255 */       super.writeTo(output);
/*     */     }
/*     */ 
/*     */     protected int computeSerializedSize()
/*     */     {
/* 260 */       int size = super.computeSerializedSize();
/* 261 */       if ((this.bitField0_ & 0x1) != 0) {
/* 262 */         size += CodedOutputByteBufferNano.computeStringSize(1, this.vendor_);
/*     */       }
/*     */ 
/* 265 */       if ((this.bitField0_ & 0x2) != 0) {
/* 266 */         size += CodedOutputByteBufferNano.computeStringSize(2, this.model_);
/*     */       }
/*     */ 
/* 269 */       if ((this.bitField0_ & 0x4) != 0) {
/* 270 */         size += CodedOutputByteBufferNano.computeFloatSize(3, this.screenToLensDistance_);
/*     */       }
/*     */ 
/* 273 */       if ((this.bitField0_ & 0x8) != 0) {
/* 274 */         size += CodedOutputByteBufferNano.computeFloatSize(4, this.interLensDistance_);
/*     */       }
/*     */ 
/* 277 */       if ((this.leftEyeFieldOfViewAngles != null) && (this.leftEyeFieldOfViewAngles.length > 0)) {
/* 278 */         int dataSize = 4 * this.leftEyeFieldOfViewAngles.length;
/* 279 */         size += dataSize;
/* 280 */         size++;
/* 281 */         size += CodedOutputByteBufferNano.computeRawVarint32Size(dataSize);
/*     */       }
/*     */ 
/* 284 */       if ((this.bitField0_ & 0x20) != 0) {
/* 285 */         size += CodedOutputByteBufferNano.computeFloatSize(6, this.trayToLensDistance_);
/*     */       }
/*     */ 
/* 288 */       if ((this.distortionCoefficients != null) && (this.distortionCoefficients.length > 0)) {
/* 289 */         int dataSize = 4 * this.distortionCoefficients.length;
/* 290 */         size += dataSize;
/* 291 */         size++;
/* 292 */         size += CodedOutputByteBufferNano.computeRawVarint32Size(dataSize);
/*     */       }
/*     */ 
/* 295 */       if ((this.bitField0_ & 0x40) != 0) {
/* 296 */         size += CodedOutputByteBufferNano.computeBoolSize(10, this.hasMagnet_);
/*     */       }
/*     */ 
/* 299 */       if ((this.bitField0_ & 0x10) != 0) {
/* 300 */         size += CodedOutputByteBufferNano.computeInt32Size(11, this.verticalAlignment_);
/*     */       }
/*     */ 
/* 303 */       return size;
/*     */     }
/*     */ 
/*     */     public DeviceParams mergeFrom(CodedInputByteBufferNano input)
/*     */       throws IOException
/*     */     {
/*     */       while (true)
/*     */       {
/* 311 */         int tag = input.readTag();
/* 312 */         switch (tag) {
/*     */         case 0:
/* 314 */           return this;
/*     */         default:
/* 316 */           if (!WireFormatNano.parseUnknownField(input, tag)) {
/* 317 */             return this;
/*     */           }
/*     */ 
/*     */           break;
/*     */         case 10:
/* 322 */           this.vendor_ = input.readString();
/* 323 */           this.bitField0_ |= 1;
/* 324 */           break;
/*     */         case 18:
/* 327 */           this.model_ = input.readString();
/* 328 */           this.bitField0_ |= 2;
/* 329 */           break;
/*     */         case 29:
/* 332 */           this.screenToLensDistance_ = input.readFloat();
/* 333 */           this.bitField0_ |= 4;
/* 334 */           break;
/*     */         case 37:
/* 337 */           this.interLensDistance_ = input.readFloat();
/* 338 */           this.bitField0_ |= 8;
/* 339 */           break;
/*     */         case 45:
/* 342 */           int arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 45);
/*     */ 
/* 344 */           int i = this.leftEyeFieldOfViewAngles == null ? 0 : this.leftEyeFieldOfViewAngles.length;
/* 345 */           float[] newArray = new float[i + arrayLength];
/* 346 */           if (i != 0) {
/* 347 */             System.arraycopy(this.leftEyeFieldOfViewAngles, 0, newArray, 0, i);
/*     */           }
/* 349 */           for (; i < newArray.length - 1; i++) {
/* 350 */             newArray[i] = input.readFloat();
/* 351 */             input.readTag();
/*     */           }
/*     */ 
/* 354 */           newArray[i] = input.readFloat();
/* 355 */           this.leftEyeFieldOfViewAngles = newArray;
/* 356 */           break;
/*     */         case 42:
/* 359 */           int length = input.readRawVarint32();
/* 360 */           int limit = input.pushLimit(length);
/* 361 */           int arrayLength = length / 4;
/* 362 */           int i = this.leftEyeFieldOfViewAngles == null ? 0 : this.leftEyeFieldOfViewAngles.length;
/* 363 */           float[] newArray = new float[i + arrayLength];
/* 364 */           if (i != 0) {
/* 365 */             System.arraycopy(this.leftEyeFieldOfViewAngles, 0, newArray, 0, i);
/*     */           }
/* 367 */           for (; i < newArray.length; i++) {
/* 368 */             newArray[i] = input.readFloat();
/*     */           }
/* 370 */           this.leftEyeFieldOfViewAngles = newArray;
/* 371 */           input.popLimit(limit);
/* 372 */           break;
/*     */         case 53:
/* 375 */           this.trayToLensDistance_ = input.readFloat();
/* 376 */           this.bitField0_ |= 32;
/* 377 */           break;
/*     */         case 61:
/* 380 */           int arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 61);
/*     */ 
/* 382 */           int i = this.distortionCoefficients == null ? 0 : this.distortionCoefficients.length;
/* 383 */           float[] newArray = new float[i + arrayLength];
/* 384 */           if (i != 0) {
/* 385 */             System.arraycopy(this.distortionCoefficients, 0, newArray, 0, i);
/*     */           }
/* 387 */           for (; i < newArray.length - 1; i++) {
/* 388 */             newArray[i] = input.readFloat();
/* 389 */             input.readTag();
/*     */           }
/*     */ 
/* 392 */           newArray[i] = input.readFloat();
/* 393 */           this.distortionCoefficients = newArray;
/* 394 */           break;
/*     */         case 58:
/* 397 */           int length = input.readRawVarint32();
/* 398 */           int limit = input.pushLimit(length);
/* 399 */           int arrayLength = length / 4;
/* 400 */           int i = this.distortionCoefficients == null ? 0 : this.distortionCoefficients.length;
/* 401 */           float[] newArray = new float[i + arrayLength];
/* 402 */           if (i != 0) {
/* 403 */             System.arraycopy(this.distortionCoefficients, 0, newArray, 0, i);
/*     */           }
/* 405 */           for (; i < newArray.length; i++) {
/* 406 */             newArray[i] = input.readFloat();
/*     */           }
/* 408 */           this.distortionCoefficients = newArray;
/* 409 */           input.popLimit(limit);
/* 410 */           break;
/*     */         case 80:
/* 413 */           this.hasMagnet_ = input.readBool();
/* 414 */           this.bitField0_ |= 64;
/* 415 */           break;
/*     */         case 88:
/* 418 */           int value = input.readInt32();
/* 419 */           switch (value) {
/*     */           case 0:
/*     */           case 1:
/*     */           case 2:
/* 423 */             this.verticalAlignment_ = value;
/* 424 */             this.bitField0_ |= 16;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public static DeviceParams parseFrom(byte[] data)
/*     */       throws InvalidProtocolBufferNanoException
/*     */     {
/* 435 */       return (DeviceParams)MessageNano.mergeFrom(new DeviceParams(), data);
/*     */     }
/*     */ 
/*     */     public static DeviceParams parseFrom(CodedInputByteBufferNano input)
/*     */       throws IOException
/*     */     {
/* 441 */       return new DeviceParams().mergeFrom(input);
/*     */     }
/*     */ 
/*     */     public static abstract interface VerticalAlignmentType
/*     */     {
/*     */       public static final int BOTTOM = 0;
/*     */       public static final int CENTER = 1;
/*     */       public static final int TOP = 2;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.proto.CardboardDevice
 * JD-Core Version:    0.6.2
 */