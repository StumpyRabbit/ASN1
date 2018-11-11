package club.callistohouse.asn1.types.basic;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;

import club.callistohouse.asn1.ASN1InputStream;
import club.callistohouse.asn1.ASN1OutputStream;

public class ASN1RealType extends ASN1BasicType {

	public ASN1RealType() { super("ASN1RealType"); }

	public boolean isTypeFor(Object obj) {
		return obj instanceof Float | obj instanceof Double;
	}
	public Integer sizeOfObject(Object obj) {
		ASN1OutputStream out = new ASN1OutputStream();
		try {
			encodeValue(obj, out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.stream.size();
	}
	public int asn1Tag() { return 9; }

	@Override
	public void encodeValue(Object obj, ASN1OutputStream derStream) throws IOException {
		encodeDoubleValue((Double) obj, derStream);
	}

	public void encodeDoubleValue(Double doubleObj, ASN1OutputStream derStream) throws IOException {
		if(doubleObj == 0) {return; }
		if(doubleObj == Double.POSITIVE_INFINITY) { derStream.write(0x40); return; }
		if(doubleObj == Double.NEGATIVE_INFINITY) { derStream.write(0x41); return; }


		long bits = Double.doubleToRawLongBits(doubleObj);
		int signByte = (int) (bits >> (63 - 6)) & (1 << 6) | (1 << 7);
		// shift to the correct place to start with, and pre-add bit 8
		int exponent = ((int) (bits >> 52) & 0x7FF) - (1023 + 52);
		// don't need to box/unbox to do arithmetic
		long mantissa = (bits & 0xFFFFFFFFFFFFFL) | (1L << 52);
		// add the hidden bit
		while ((mantissa & 1) == 0) {
			mantissa >>= 1;
			exponent++;
		}

		// normalize
		byte[] exptbytes = BigInteger.valueOf(exponent).toByteArray();
		if (exptbytes.length <= 3)
			derStream.write(signByte | (exptbytes.length - 1));
		else {
			derStream.write(signByte | 3);
			derStream.write(exptbytes.length);
		}
		// only the if branch is actually needed
		derStream.write(exptbytes);
		derStream.write(BigInteger.valueOf(mantissa/* >>scale */).toByteArray());
	}

	private Double decodeBinaryEncodedValue(ASN1InputStream derStream, int length, byte specialByte) throws IOException {
		int sign = ((specialByte & 0x40) == 0) ? 1 : -1;
		int base = 0;
		byte baseFlag = (byte) (specialByte & 0x30);
		switch (baseFlag) {
		case 0:
			base = 2;
			break;
		case 1:
			base = 8;
			break;
		case 2:
			base = 16;
			break;
		default:
			throw new IOException("bad base encoding of float");
		}
		int scalingFactor = specialByte & 0x0C;
		int exponentSizeCode = specialByte & 0x03;
		Integer exponent;
		if (exponentSizeCode == 3) {
			exponentSizeCode = derStream.nextLength();
			exponent = (Integer) new ASN1IntegerType().decodeValue(derStream, exponentSizeCode); 
		} else {
			exponent = (Integer) new ASN1IntegerType().decodeValue(derStream, exponentSizeCode + 1); 
		}
		int mantissaLength = length - (exponentSizeCode + 2);
		byte[] bytes = new byte[mantissaLength];
		derStream.read(bytes);
		long mantissa = new BigInteger(ByteBuffer.wrap(bytes).array()).longValue();
		return sign * mantissa * Math.pow(2, scalingFactor) * Math.pow(base, exponent);
	}

	@Override
	public Double decodeValue(ASN1InputStream derStream, int length) throws IOException {
		if(length == 0) { return 0.0; }
		byte specialByte = (byte) derStream.read();
		if((specialByte & 0x80) == 128) { return decodeBinaryEncodedValue(derStream, length, specialByte); }
		if((specialByte & 0xC0) == 0) {
			if((specialByte ^ 0x01) == 0) { return decodeDecimalEncodedNR1Value(derStream, length, specialByte); }
			if((specialByte ^ 0x02) == 0) { return decodeDecimalEncodedNR2Value(derStream, length, specialByte); }
			if((specialByte ^ 0x03) == 0) { return decodeDecimalEncodedNR3Value(derStream, length, specialByte); }
		}
		if((specialByte & 0x40) == 64) { return decodeSpecialEncodedValue(derStream, length, specialByte); }
		throw new IOException("bad construction of float");
	}

	private Double decodeDecimalEncodedNR1Value(ASN1InputStream derStream, int length, byte specialByte) throws IOException {
		throw new IOException("unsupported NR1 construction of float");
	}
	private Double decodeDecimalEncodedNR2Value(ASN1InputStream derStream, int length, byte specialByte) throws IOException {
		throw new IOException("unsupported NR2 construction of float");
	}
	private Double decodeDecimalEncodedNR3Value(ASN1InputStream derStream, int length, byte specialByte) throws IOException {
		throw new IOException("unsupported NR3 construction of float");
	}

	private Double decodeSpecialEncodedValue(ASN1InputStream derStream, int length, byte specialByte) throws IOException {
		if((length == 1) && ((specialByte ^ 0x40) == 0)) { return Double.POSITIVE_INFINITY; }
		if((length == 1) && ((specialByte ^ 0x41) == 0)) { return Double.NEGATIVE_INFINITY; }
		throw new IOException("bad construction of specially encoded float");
	}

	public static double getMantissa(double x) {
	    int exponent = Math.getExponent(x);
	    return x / Math.pow(2, exponent);
	}
}
