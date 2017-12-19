package club.callistohouse.ston;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import club.callistohouse.asn1.ASN1Module;
import club.callistohouse.asn1.types.ASN1Type;

public class STONWriter {
	private static List<String> STONCharacters = new ArrayList<String>(128);
	private static List<Integer> STONSimpleSymbolCharacters = Collections.nCopies(256, 1);
	static {
		for(int i = 33; i <= 127; i++) {
			STONCharacters.set(i, "pass");
		}
		STONCharacters.set(9, "\\b");
		STONCharacters.set(10, "\\t");
		STONCharacters.set(11, "\\n");
		STONCharacters.set(13, "\\f");
		STONCharacters.set(14, "\\r");
		STONCharacters.set(35, "\\\"");
		STONCharacters.set(40, "\\'");
		STONCharacters.set(93, "\\\\");
		for(int b = 0; b < 256; b++) {
			char c = (char) (b & 0xFF);
			if(isSimpleSymbolChar(c)) {
				STONSimpleSymbolCharacters.set(b, 0);
			}
		}
	}
	public static boolean isSimpleSymbolChar(char c) {
		return "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_./".contains(String.valueOf(c));
	}

	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	private OutputStream outStream;
	private Map<Object,Integer> objects = new HashMap<Object,Integer>();

	public STONWriter() {
	}
	public STONWriter(OutputStream out) {
		this.outStream = out;
	}
	public void reset() { objects.clear(); }
	public void nextPut(Object obj) throws IOException {
		ASN1Type type = ASN1Module.typeForObject(obj);
		type.stonOn(obj, this);
	}

	public boolean isSimpleSymbol(char c) {
		return STONSimpleSymbolCharacters.get((byte)c) == 0;
	}
	public void encodeCharacter(char c) throws IOException {
		String encoding = null;
		if(c < 127 && ((encoding = STONCharacters.get(c + 1)) != null)) {
			if(encoding.equals("pass")) {
				outStream.write(c);
			} else {
				outStream.write(encoding.getBytes());
			}
		} else {
			outStream.write("\\u".getBytes());
			outStream.write(c);
		}
	}

	public void writeBoolean(Boolean bool) throws IOException {
		if(bool) {
			outStream.write("true".getBytes());
		} else {
			outStream.write("false".getBytes());
		}
		
	}

	public void writeObjectSingleton(Object obj, String hex) throws IOException {
		Integer index = null;
		if((index = objects.get(obj)) != null) {
			outStream.write(("@" + index).getBytes());
		} else {
			index = objects.size() + 1;
			objects.put(obj, index);
			outStream.write((className(obj)).getBytes());
			outStream.write("[".getBytes());
			outStream.write(hex.getBytes());
			outStream.write("]".getBytes());
		}
	}
	public void writeObjectList(Object obj, List<?> list) throws IOException {
		Integer index = null;
		if((index = objects.get(obj)) != null) {
			outStream.write(("@" + index).getBytes());
		} else {
			index = objects.size() + 1;
			objects.put(obj, index);
			outStream.write((className(obj)).getBytes());
			outStream.write("[".getBytes());
			boolean first = true;
			for(int i = 0; i < list.size(); i++) {
				if(first) {
					first = false;
				} else {
					outStream.write(",".getBytes());
				}
				nextPut(list.get(i));
			}
			outStream.write("]".getBytes());
		}
	}
	public void writeObjectSet(Object obj, Set<?> set) throws IOException {
		Integer index = null;
		if((index = objects.get(obj)) != null) {
			outStream.write(("@" + index).getBytes());
		} else {
			index = objects.size() + 1;
			objects.put(obj, index);
			outStream.write((className(obj)).getBytes());
			outStream.write("[".getBytes());
			boolean first = true;
			for(Object each : set) {
				if(first) {
					first = false;
				} else {
					outStream.write(",".getBytes());
				}
				nextPut(each);
			}
			outStream.write("]".getBytes());
		}
	}
	public void writeObjectMap(Object obj, Map<String,?> map) throws IOException {
		Integer index = null;
		if((index = objects.get(obj)) != null) {
			outStream.write(("@" + index).getBytes());
		} else {
			index = objects.size() + 1;
			objects.put(obj, index);
			outStream.write((className(obj)).getBytes());
			outStream.write("[".getBytes());
			boolean first = true;
			for(String key : map.keySet()) {
				if(first) {
					first = false;
				} else {
					outStream.write(",".getBytes());
				}
				outStream.write(key.getBytes());
				outStream.write(" : ".getBytes());
				nextPut(map.get(key));
			}
			outStream.write("]".getBytes());
		}
	}
	private String className(Object obj) {
		Class<?> clazz = obj.getClass();
		if(clazz.equals(byte[].class)) {
			return "ByteArray";
		}
		return obj.getClass().getSimpleName();
	}
	public void writeBigInteger(BigInteger obj) throws IOException {
		outStream.write(obj.toByteArray());
	}
	public void writeFloat(Float obj) throws IOException {
		outStream.write(obj.toString().getBytes());
	}
	public void writeString(String obj) throws IOException {
		outStream.write("'".getBytes());
		char[] chars = obj.toCharArray();
		for(int i = 0; i < chars.length; i++) {
			encodeCharacter(chars[i]);;
		}
		outStream.write("'".getBytes());
	}
	public void writeNull() throws IOException {
		outStream.write("nil".getBytes());
	}
}
