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
/*     */ public abstract interface Phone
/*     */ {
/*     */   public static final class PhoneParams extends MessageNano
/*     */   {
/*     */     private static volatile PhoneParams[] _emptyArray;
/*     */     private int bitField0_;
/*     */     private float xPpi_;
/*     */     private float yPpi_;
/*     */     private float bottomBezelHeight_;
/*     */     public float[] gyroBias;
/*     */ 
/*     */     public static PhoneParams[] emptyArray()
/*     */     {
/*  14 */       if (_emptyArray == null) {
/*  15 */         synchronized (InternalNano.LAZY_INIT_LOCK)
/*     */         {
/*  17 */           if (_emptyArray == null) {
/*  18 */             _emptyArray = new PhoneParams[0];
/*     */           }
/*     */         }
/*     */       }
/*  22 */       return _emptyArray;
/*     */     }
/*     */ 
/*     */     public float getXPpi()
/*     */     {
/*  30 */       return this.xPpi_;
/*     */     }
/*     */     public PhoneParams setXPpi(float value) {
/*  33 */       this.xPpi_ = value;
/*  34 */       this.bitField0_ |= 1;
/*  35 */       return this;
/*     */     }
/*     */     public boolean hasXPpi() {
/*  38 */       return (this.bitField0_ & 0x1) != 0;
/*     */     }
/*     */     public PhoneParams clearXPpi() {
/*  41 */       this.xPpi_ = 0.0F;
/*  42 */       this.bitField0_ &= -2;
/*  43 */       return this;
/*     */     }
/*     */ 
/*     */     public float getYPpi()
/*     */     {
/*  49 */       return this.yPpi_;
/*     */     }
/*     */     public PhoneParams setYPpi(float value) {
/*  52 */       this.yPpi_ = value;
/*  53 */       this.bitField0_ |= 2;
/*  54 */       return this;
/*     */     }
/*     */     public boolean hasYPpi() {
/*  57 */       return (this.bitField0_ & 0x2) != 0;
/*     */     }
/*     */     public PhoneParams clearYPpi() {
/*  60 */       this.yPpi_ = 0.0F;
/*  61 */       this.bitField0_ &= -3;
/*  62 */       return this;
/*     */     }
/*     */ 
/*     */     public float getBottomBezelHeight()
/*     */     {
/*  68 */       return this.bottomBezelHeight_;
/*     */     }
/*     */     public PhoneParams setBottomBezelHeight(float value) {
/*  71 */       this.bottomBezelHeight_ = value;
/*  72 */       this.bitField0_ |= 4;
/*  73 */       return this;
/*     */     }
/*     */     public boolean hasBottomBezelHeight() {
/*  76 */       return (this.bitField0_ & 0x4) != 0;
/*     */     }
/*     */     public PhoneParams clearBottomBezelHeight() {
/*  79 */       this.bottomBezelHeight_ = 0.0F;
/*  80 */       this.bitField0_ &= -5;
/*  81 */       return this;
/*     */     }
/*     */ 
/*     */     public PhoneParams()
/*     */     {
/*  88 */       clear();
/*     */     }
/*     */ 
/*     */     public PhoneParams clear() {
/*  92 */       this.bitField0_ = 0;
/*  93 */       this.xPpi_ = 0.0F;
/*  94 */       this.yPpi_ = 0.0F;
/*  95 */       this.bottomBezelHeight_ = 0.0F;
/*  96 */       this.gyroBias = WireFormatNano.EMPTY_FLOAT_ARRAY;
/*  97 */       this.cachedSize = -1;
/*  98 */       return this;
/*     */     }
/*     */ 
/*     */     public void writeTo(CodedOutputByteBufferNano output)
/*     */       throws IOException
/*     */     {
/* 104 */       if ((this.bitField0_ & 0x1) != 0) {
/* 105 */         output.writeFloat(1, this.xPpi_);
/*     */       }
/* 107 */       if ((this.bitField0_ & 0x2) != 0) {
/* 108 */         output.writeFloat(2, this.yPpi_);
/*     */       }
/* 110 */       if ((this.bitField0_ & 0x4) != 0) {
/* 111 */         output.writeFloat(3, this.bottomBezelHeight_);
/*     */       }
/* 113 */       if ((this.gyroBias != null) && (this.gyroBias.length > 0)) {
/* 114 */         int dataSize = 4 * this.gyroBias.length;
/* 115 */         output.writeRawVarint32(34);
/* 116 */         output.writeRawVarint32(dataSize);
/* 117 */         for (int i = 0; i < this.gyroBias.length; i++) {
/* 118 */           output.writeFloatNoTag(this.gyroBias[i]);
/*     */         }
/*     */       }
/* 121 */       super.writeTo(output);
/*     */     }
/*     */ 
/*     */     protected int computeSerializedSize()
/*     */     {
/* 126 */       int size = super.computeSerializedSize();
/* 127 */       if ((this.bitField0_ & 0x1) != 0) {
/* 128 */         size += CodedOutputByteBufferNano.computeFloatSize(1, this.xPpi_);
/*     */       }
/*     */ 
/* 131 */       if ((this.bitField0_ & 0x2) != 0) {
/* 132 */         size += CodedOutputByteBufferNano.computeFloatSize(2, this.yPpi_);
/*     */       }
/*     */ 
/* 135 */       if ((this.bitField0_ & 0x4) != 0) {
/* 136 */         size += CodedOutputByteBufferNano.computeFloatSize(3, this.bottomBezelHeight_);
/*     */       }
/*     */ 
/* 139 */       if ((this.gyroBias != null) && (this.gyroBias.length > 0)) {
/* 140 */         int dataSize = 4 * this.gyroBias.length;
/* 141 */         size += dataSize;
/* 142 */         size++;
/* 143 */         size += CodedOutputByteBufferNano.computeRawVarint32Size(dataSize);
/*     */       }
/*     */ 
/* 146 */       return size;
/*     */     }
/*     */ 
/*     */     public PhoneParams mergeFrom(CodedInputByteBufferNano input)
/*     */       throws IOException
/*     */     {
/*     */       while (true)
/*     */       {
/* 154 */         int tag = input.readTag();
/* 155 */         switch (tag) {
/*     */         case 0:
/* 157 */           return this;
/*     */         default:
/* 159 */           if (!WireFormatNano.parseUnknownField(input, tag)) {
/* 160 */             return this;
/*     */           }
/*     */ 
/*     */           break;
/*     */         case 13:
/* 165 */           this.xPpi_ = input.readFloat();
/* 166 */           this.bitField0_ |= 1;
/* 167 */           break;
/*     */         case 21:
/* 170 */           this.yPpi_ = input.readFloat();
/* 171 */           this.bitField0_ |= 2;
/* 172 */           break;
/*     */         case 29:
/* 175 */           this.bottomBezelHeight_ = input.readFloat();
/* 176 */           this.bitField0_ |= 4;
/* 177 */           break;
/*     */         case 37:
/* 180 */           int arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 37);
/*     */ 
/* 182 */           int i = this.gyroBias == null ? 0 : this.gyroBias.length;
/* 183 */           float[] newArray = new float[i + arrayLength];
/* 184 */           if (i != 0) {
/* 185 */             System.arraycopy(this.gyroBias, 0, newArray, 0, i);
/*     */           }
/* 187 */           for (; i < newArray.length - 1; i++) {
/* 188 */             newArray[i] = input.readFloat();
/* 189 */             input.readTag();
/*     */           }
/*     */ 
/* 192 */           newArray[i] = input.readFloat();
/* 193 */           this.gyroBias = newArray;
/* 194 */           break;
/*     */         case 34:
/* 197 */           int length = input.readRawVarint32();
/* 198 */           int limit = input.pushLimit(length);
/* 199 */           int arrayLength = length / 4;
/* 200 */           int i = this.gyroBias == null ? 0 : this.gyroBias.length;
/* 201 */           float[] newArray = new float[i + arrayLength];
/* 202 */           if (i != 0) {
/* 203 */             System.arraycopy(this.gyroBias, 0, newArray, 0, i);
/*     */           }
/* 205 */           for (; i < newArray.length; i++) {
/* 206 */             newArray[i] = input.readFloat();
/*     */           }
/* 208 */           this.gyroBias = newArray;
/* 209 */           input.popLimit(limit);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public static PhoneParams parseFrom(byte[] data)
/*     */       throws InvalidProtocolBufferNanoException
/*     */     {
/* 218 */       return (PhoneParams)MessageNano.mergeFrom(new PhoneParams(), data);
/*     */     }
/*     */ 
/*     */     public static PhoneParams parseFrom(CodedInputByteBufferNano input)
/*     */       throws IOException
/*     */     {
/* 224 */       return new PhoneParams().mergeFrom(input);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.proto.Phone
 * JD-Core Version:    0.6.2
 */