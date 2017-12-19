package club.callistohouse.asn1.types.constructed;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import club.callistohouse.ston.STONWriter;

public class ASN1SequenceType extends ASN1UnmappedType {

	public ASN1SequenceType() { super(); }

	@Override
	public int asn1Tag() { return 48; }

	@SuppressWarnings("rawtypes")
	@Override
	protected List<?> newContainer() {
		// TODO Auto-generated method stub
		return new ArrayList();
	}

	@Override
	public void stonOn(Object obj, STONWriter stonWriter) throws IOException {
		stonWriter.writeObjectList(obj, (List)obj);
	}

}
