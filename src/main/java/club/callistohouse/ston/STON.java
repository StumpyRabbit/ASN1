package club.callistohouse.ston;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class STON {
	public static STONReader reader(InputStream in) { return new STONReader(in); } 
	public static STONWriter writer(OutputStream out) { return new STONWriter(out); } 

	@SuppressWarnings("rawtypes")
	public static Class<ArrayList> listClass() { return ArrayList.class; } 
	@SuppressWarnings("rawtypes")
	public static Class<HashMap> mapClass() { return HashMap.class; } 

	public static Object fromStream(InputStream instream) throws IOException {
		return reader(instream).nextObject();
	}
	public static Object fromString(String in) throws IOException {
		return fromStream(new ByteArrayInputStream(in.getBytes()));
	}
	public static String toString(Object obj) throws IOException {
		OutputStream out = new ByteArrayOutputStream();
		writer(out).nextPut(obj);
		return out.toString();
	}
}