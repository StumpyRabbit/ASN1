package club.callistohouse.ston;

import java.io.IOException;
import java.sql.Date;

import org.junit.Test;

import junit.framework.TestCase;

public class STONWriterTests extends TestCase {

	private String serialize(Object obj) throws IOException {
		return STON.toString(obj);
	}

	@Test
	public void testBoolean() throws IOException {
		assertTrue(serialize(true).equals("true"));
		assertTrue(serialize(false).equals("false"));
	}
	@Test
	public void testByteArray() throws IOException {
		assertTrue(serialize(new Byte[] {1, 2, 3}).equals("ByteArray[''010203'']"));
	}
	@Test
	public void testDate() throws IOException {
		Date date = Date.valueOf("2012-01-01");
		assertTrue(serialize(date).equals("Date[\"2012-01-01\"]"));
	}
}
