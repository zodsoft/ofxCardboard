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
/*     */     implements Cloneable
/*     */   {
/*     */     private static volatile PhoneParams[] _emptyArray;
/*     */     private int bitField0_;
/*     */     private float xPpi_;
/*     */     private float yPpi_;
/*     */     private float bottomBezelHeight_;
/*     */     public float[] dEPRECATEDGyroBias;
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
/*  96 */       this.dEPRECATEDGyroBias = WireFormatNano.EMPTY_FLOAT_ARRAY;
/*  97 */       this.cachedSize = -1;
/*  98 */       return this;
/*     */     }
/*     */ 
/*     */     public PhoneParams clone() {
/*     */       PhoneParams cloned;
/*     */       try {
/* 104 */         cloned = (PhoneParams)super.clone();
/*     */       } catch (CloneNotSupportedException e) {
/* 106 */         throw new AssertionError(e);
/*     */       }
/* 108 */       if ((this.dEPRECATEDGyroBias != null) && (this.dEPRECATEDGyroBias.length > 0)) {
/* 109 */         cloned.dEPRECATEDGyroBias = ((float[])this.dEPRECATEDGyroBias.clone());
/*     */       }
/* 111 */       return cloned;
/*     */     }
/*     */ 
/*     */     public void writeTo(CodedOutputByteBufferNano output)
/*     */       throws IOException
/*     */     {
/* 118 */       if ((this.bitField0_ & 0x1) != 0) {
/* 119 */         output.writeFloat(1, this.xPpi_);
/*     */       }
/* 121 */       if ((this.bitField0_ & 0x2) != 0) {
/* 122 */         output.writeFloat(2, this.yPpi_);
/*     */       }
/* 124 */       if ((this.bitField0_ & 0x4) != 0) {
/* 125 */         output.writeFloat(3, this.bottomBezelHeight_);
/*     */       }
/* 127 */       if ((this.dEPRECATEDGyroBias != null) && (this.dEPRECATEDGyroBias.length > 0)) {
/* 128 */         int dataSize = 4 * this.dEPRECATEDGyroBias.length;
/* 129 */         output.writeRawVarint32(34);
/* 130 */         output.writeRawVarint32(dataSize);
/* 131 */         for (int i = 0; i < this.dEPRECATEDGyroBias.length; i++) {
/* 132 */           output.writeFloatNoTag(this.dEPRECATEDGyroBias[i]);
/*     */         }
/*     */       }
/* 135 */       super.writeTo(output);
/*     */     }
/*     */ 
/*     */     protected int computeSerializedSize()
/*     */     {
/* 140 */       int size = super.computeSerializedSize();
/* 141 */       if ((this.bitField0_ & 0x1) != 0) {
/* 142 */         size += CodedOutputByteBufferNano.computeFloatSize(1, this.xPpi_);
/*     */       }
/*     */ 
/* 145 */       if ((this.bitField0_ & 0x2) != 0) {
/* 146 */         size += CodedOutputByteBufferNano.computeFloatSize(2, this.yPpi_);
/*     */       }
/*     */ 
/* 149 */       if ((this.bitField0_ & 0x4) != 0) {
/* 150 */         size += CodedOutputByteBufferNano.computeFloatSize(3, this.bottomBezelHeight_);
/*     */       }
/*     */ 
/* 153 */       if ((this.dEPRECATEDGyroBias != null) && (this.dEPRECATEDGyroBias.length > 0)) {
/* 154 */         int dataSize = 4 * this.dEPRECATEDGyroBias.length;
/* 155 */         size += dataSize;
/* 156 */         size++;
/* 157 */         size += CodedOutputByteBufferNano.computeRawVarint32Size(dataSize);
/*     */       }
/*     */ 
/* 160 */       return size;
/*     */     }
/*     */ 
/*     */     public PhoneParams mergeFrom(CodedInputByteBufferNano input)
/*     */       throws IOException
/*     */     {
/*     */       while (true)
/*     */       {
/* 168 */         int tag = input.readTag();
/* 169 */         switch (tag) {
/*     */         case 0:
/* 171 */           return this;
/*     */         default:
/* 173 */           if (!WireFormatNano.parseUnknownField(input, tag)) {
/* 174 */             return this;
/*     */           }
/*     */ 
/*     */           break;
/*     */         case 13:
/* 179 */           this.xPpi_ = input.readFloat();
/* 180 */           this.bitField0_ |= 1;
/* 181 */           break;
/*     */         case 21:
/* 184 */           this.yPpi_ = input.readFloat();
/* 185 */           this.bitField0_ |= 2;
/* 186 */           break;
/*     */         case 29:
/* 189 */           this.bottomBezelHeight_ = input.readFloat();
/* 190 */           this.bitField0_ |= 4;
/* 191 */           break;
/*     */         case 37:
/* 194 */           int arrayLength = WireFormatNano.getRepeatedFieldArrayLength(input, 37);
/*     */ 
/* 196 */           int i = this.dEPRECATEDGyroBias == null ? 0 : this.dEPRECATEDGyroBias.length;
/* 197 */           float[] newArray = new float[i + arrayLength];
/* 198 */           if (i != 0) {
/* 199 */             System.arraycopy(this.dEPRECATEDGyroBias, 0, newArray, 0, i);
/*     */           }
/* 201 */           for (; i < newArray.length - 1; i++) {
/* 202 */             newArray[i] = input.readFloat();
/* 203 */             input.readTag();
/*     */           }
/*     */ 
/* 206 */           newArray[i] = input.readFloat();
/* 207 */           this.dEPRECATEDGyroBias = newArray;
/* 208 */           break;
/*     */         case 34:
/* 211 */           int length = input.readRawVarint32();
/* 212 */           int limit = input.pushLimit(length);
/* 213 */           int arrayLength = length / 4;
/* 214 */           int i = this.dEPRECATEDGyroBias == null ? 0 : this.dEPRECATEDGyroBias.length;
/* 215 */           float[] newArray = new float[i + arrayLength];
/* 216 */           if (i != 0) {
/* 217 */             System.arraycopy(this.dEPRECATEDGyroBias, 0, newArray, 0, i);
/*     */           }
/* 219 */           for (; i < newArray.length; i++) {
/* 220 */             newArray[i] = input.readFloat();
/*     */           }
/* 222 */           this.dEPRECATEDGyroBias = newArray;
/* 223 */           input.popLimit(limit);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public static PhoneParams parseFrom(byte[] data)
/*     */       throws InvalidProtocolBufferNanoException
/*     */     {
/* 232 */       return (PhoneParams)MessageNano.mergeFrom(new PhoneParams(), data);
/*     */     }
/*     */ 
/*     */     public static PhoneParams parseFrom(CodedInputByteBufferNano input)
/*     */       throws IOException
/*     */     {
/* 238 */       return new PhoneParams().mergeFrom(input);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/dantheman/src/android/cardboard-java/CardboardSample/libs/cardboard.jar
 * Qualified Name:     com.google.vrtoolkit.cardboard.proto.Phone
 * JD-Core Version:    0.6.2
 */