package club.callistohouse.asn1.types;

import java.io.IOException;

import club.callistohouse.asn1.ASN1InputStream;
import club.callistohouse.asn1.ASN1OutputStream;
import club.callistohouse.ston.STONWriter;

public class ASN1TypeReference extends ASN1Type {

	public ASN1TypeReference(String name) { super(name); }

	@Override
	public int asn1Tag() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void encode(Object obj, ASN1OutputStream derStream) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void encodeValue(Object obj, ASN1OutputStream derStream) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object decodeValue(ASN1InputStream derStream, int length)
			throws IOException, InstantiationException, IllegalAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object decodeConstructedValue(ASN1InputStream derStream, int length)
			throws IOException, InstantiationException, IllegalAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	public void stonOn(Object obj, STONWriter stonWriter) throws IOException {
		stonWriter.writeObjectListSingleton(obj, obj.toString());
	}
}
