package club.callistohouse.ston;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class STON {
	public static STONReader reader(InputStream in) { return new STONReader(in); } 
	public static STONWriter writer(OutputStream out) { return new STONWriter(out); } 

	public static Class<ArrayList> listClass() { return ArrayList.class; } 
	public static Class<HashMap> mapClass() { return HashMap.class; } 

	public static Object fromStream(InputStream instream) {
		return reader(instream).nextObject();
	}
	public static Object fromString(String in) {
		return fromStream(new ByteArrayInputStream(in.getBytes()));
	}
	public static String toString(Object obj) {
		OutputStream out = new ByteArrayOutputStream();
		return writer(out).nextPutObject(obj);
	}
}
