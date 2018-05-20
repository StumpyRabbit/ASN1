package club.callistohouse.asn1.types.basic;

import java.io.IOException;
import java.util.Map;

import club.callistohouse.asn1.ASN1InputStream;
import club.callistohouse.asn1.ASN1OutputStream;
import club.callistohouse.ston.STONWriter;

public class STONDictionaryType extends ASN1OctetsType {

	public STONDictionaryType() { super("ASN1ByteArrayType"); }

	public boolean isTypeFor(Object obj) { return obj instanceof Map<?,?>; }
	public Integer sizeOfObject(Object obj) { return ((byte[]) obj).length; }
	public int asn1Tag() { return 4; }

	@Override
	public void stonOn(Object obj, STONWriter stonWriter) throws IOException {
		String hex = STONWriter.bytesToHex((byte[])obj);
		stonWriter.writeObjectSingleton(obj, ("''" + hex + "''"));
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

}
