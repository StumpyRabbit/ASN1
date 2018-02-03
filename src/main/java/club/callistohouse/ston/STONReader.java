package club.callistohouse.ston;

import java.io.IOException;
import java.io.InputStream;

public class STONReader {

	private int unresolvedReferences;
	private InputStream inStream;

	public STONReader(InputStream instream) { this.inStream = instream; }

	public Object nextObject() throws IOException { return next(); }
	public Object next() throws IOException {
		consumeWhitespace();
		Object object = parseValue();
		if(unresolvedReferences > 0) {
			processSubObjectsOf(object);
		}
		return object;
	}
	private void processSubObjectsOf(Object object) {
		// TODO Auto-generated method stub
		
	}

	private byte peek() throws IOException {
		inStream.mark(0);
		byte b = (byte) inStream.read();
		inStream.reset();
		return b;
	}
	private void consumeWhitespace() {
		
	}

	public Object parseValue() throws IOException {
		byte b = peek();
		char c = (char) (b & 0xFF);
		Character ch = Character.valueOf(c);
		if(Character.isUpperCase(ch)) {
			return parseObject();
		} else if(ch == '{') {
			return parseMap();
		} else if(ch == '[') {
			return parseList();
		} else if((ch == '\'') || (ch == '\"')) {
			return parseString();
		} else if(ch == '#') {
			return parseSymbol();
		} else if(ch == '@') {
			return parseReference();
		} else if((ch == '-') | (Character.isDigit(ch))) {
			return parseNumber();
		}
		
		return null;
	}

	private Object parseNumber() {
		// TODO Auto-generated method stub
		return null;
	}

	private Object parseReference() {
		// TODO Auto-generated method stub
		return null;
	}

	private Object parseSymbol() {
		// TODO Auto-generated method stub
		return null;
	}

	private Object parseString() {
		// TODO Auto-generated method stub
		return null;
	}

	private Object parseList() {
		// TODO Auto-generated method stub
		return null;
	}

	private Object parseMap() {
		// TODO Auto-generated method stub
		return null;
	}

	private Object parseObject() {
		Class<?> targetClass = parseClass();
		STONReference ref = newReference();
		Object obj = fillClass(targetClass);
		setReference(obj, ref);
		return obj;
	}
	private void setReference(Object obj, STONReference ref) {
		// TODO Auto-generated method stub
		
	}

	private Object fillClass(Class<?> targetClass) {
		// TODO Auto-generated method stub
		return null;
	}

	private STONReference newReference() {
		// TODO Auto-generated method stub
		return null;
	}

	private Class<?> parseClass() {
		// TODO Auto-generated method stub
		return null;
	}
}