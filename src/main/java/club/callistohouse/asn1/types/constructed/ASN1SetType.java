package club.callistohouse.asn1.types.constructed;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import club.callistohouse.asn1.objects.OrderedSet;
import club.callistohouse.ston.STONWriter;

public class ASN1SetType extends ASN1UnmappedType {

	public ASN1SetType() { super(); }

	@Override
	public int asn1Tag() { return 49; }

	@SuppressWarnings("rawtypes")
	@Override
	protected Set<?> newContainer() {
		// TODO Auto-generated method stub
		return new OrderedSet();
	}

	@Override
	public void stonOn(Object obj, STONWriter stonWriter) throws IOException {
		stonWriter.writeObjectSet(obj, (Set)obj);
	}
}
