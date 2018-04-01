package club.callistohouse.ston;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class STONReader {

	private int unresolvedReferences;
	private InputStream inStream;

	public STONReader(InputStream instream) { this.inStream = instream; }

	public Object next() throws IOException, InstantiationException, IllegalAccessException {
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
	private void consumeWhitespace() throws IOException {
		while((inStream.available() > 0) && isSeparator((char) peek())) {
			inStream.read();
		}
	}

	private boolean isSeparator(char peek) {
		String seps = "\t\n ";
		for(char c : seps.toCharArray()) {
			if(c == peek) {
				return true;
			}
		}
		return false;
	}

	public Object parseValue() throws IOException, InstantiationException, IllegalAccessException {
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

	private Object parseObject() {
		Class<?> targetClass = parseClass();
		STONReference ref = newReference();
		Object obj = fillClass(targetClass);
		setReference(obj, ref);
		return obj;
	}

	private Object parseMap() throws InstantiationException, IllegalAccessException {
		Map<String,Object> map = new HashMap<String,Object>();
		storeReference(map);
		expectChar('{');
		if(matchChar('}')) {
			return map;
		}
		try {
			while(inStream.available() > 0) {
				Object propName = parseValue();
				expectChar(':');
				Object propValue = parseValue();
				map.put((String) propName, propValue);
				if(matchChar('}')) {
					return map;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	private Object parseList() {
		// TODO Auto-generated method stub
		return null;
	}

	private Object parseString() {
		// TODO Auto-generated method stub
		return null;
	}
	private Object parseSymbol() {
		// TODO Auto-generated method stub
		return null;
	}
	private Object parseReference() {
		// TODO Auto-generated method stub
		return null;
	}
	private Object parseNumber() {
		// TODO Auto-generated method stub
		return null;
	}

	private STONReference newReference() {
		// TODO Auto-generated method stub
		return null;
	}
	private void setReference(Object obj, STONReference ref) {
		// TODO Auto-generated method stub
		
	}
	private STONReference resolveReference(Object obj) {
		// TODO Auto-generated method stub
		return null;		
	}
	private void storeReference(Object obj) {
		// TODO Auto-generated method stub
		
	}
	private boolean matchChar(char c) {
		try {
			if(((char)peek()) == c) {
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			return false;
		}
	}
	private void expectChar(char c) {
		try {
			if(matchChar(c)) {
				inStream.read();
			} else {
				new RuntimeException("expected " + c);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Object fillClass(Class<?> targetClass) {
		// TODO Auto-generated method stub
		return null;
	}


	private Class<?> parseClass() {
		// TODO Auto-generated method stub
		return null;
	}
}