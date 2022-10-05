package everyos.ggd.server.socket.decoder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import everyos.ggd.server.socket.SocketArray;
import everyos.ggd.server.socket.decoder.imp.SocketDecoderImp;

public class SocketDecoderTest {

	private SocketDecoder decoder;

	@BeforeEach
	private void beforeEach() {
		this.decoder = new SocketDecoderImp();
	}
	
	@Test
	@DisplayName("Can decode simple number")
	public void canDecodeSimpleNumber() {
		int decoded = decoder.decodeNumber(new byte[] { 4 }, 0);
		Assertions.assertEquals(4, decoded);
	}
	
	@Test
	@DisplayName("Can decode large number")
	public void canDecodeLargeNumber() {
		int decoded = decoder.decodeNumber(new byte[] { -29, 2 }, 0);
		Assertions.assertEquals(355, decoded);
	}
	
	@Test
	@DisplayName("Can decode string")
	public void canDecodeString() {
		String decoded = decoder.decodeString(new byte[] { 'X', 'K', 'C', 'D' }, 0, 4);
		Assertions.assertEquals("XKCD", decoded);
	}
	
	@Test
	@DisplayName("Can decode boolean")
	public void canDecodeBoolean() {
		boolean decodedFalse = decoder.decodeBoolean(new byte[] { 0 }, 0);
		boolean decodedTrue = decoder.decodeBoolean(new byte[] { 1 }, 0);
		
		Assertions.assertEquals(false, decodedFalse);
		Assertions.assertEquals(true, decodedTrue);
	}
	
	@Test
	@DisplayName("Can decode empty array")
	public void canDecodeEmptyArray() {
		SocketArray array = decoder.decodeArray(new byte[] {}, 0, 0);
		Assertions.assertArrayEquals(new int[] {}, array.keys());
	}
	
	@Test
	@DisplayName("Can decode array with one element")
	public void canDecodeSingleElementArray() {
		SocketArray array = decoder.decodeArray(new byte[] {
			1 << 3, 10
		}, 0, 2);
		Assertions.assertArrayEquals(new int[] { 0 }, array.keys());
		Assertions.assertEquals(10, array.getInt(0));
	}
	
	@Test
	@DisplayName("Can decode array with two elements")
	public void canDecodeDoubleElementArray() {
		SocketArray array = decoder.decodeArray(new byte[] {
			1 << 3, 10,
			2 << 3, 42
		}, 0, 4);
		Assertions.assertArrayEquals(new int[] { 0, 1 }, array.keys());
		Assertions.assertEquals(10, array.getInt(0));
		Assertions.assertEquals(42, array.getInt(1));
	}
	
	@Test
	@DisplayName("Can decode array with string")
	public void canDecodeArrayWithString() {
		SocketArray array = decoder.decodeArray(new byte[] {
			(1 << 3) + 2, 4, 'X', 'K', 'C', 'D',
			2 << 3, 42
		}, 0, 8);
		Assertions.assertArrayEquals(new int[] { 0, 1 }, array.keys());
		Assertions.assertEquals("XKCD", array.getString(0));
		Assertions.assertEquals(42, array.getInt(1));
	}
	
	@Test
	@DisplayName("Can decode array with boolean")
	public void canDecodeArrayWithBoolean() {
		SocketArray array = decoder.decodeArray(new byte[] {
			1 << 3, 1,
			2 << 3, 0
		}, 0, 4);
		Assertions.assertArrayEquals(new int[] { 0, 1 }, array.keys());
		Assertions.assertEquals(true, array.getBoolean(0));
		Assertions.assertEquals(false, array.getBoolean(1));
	}
	
	@Test
	@DisplayName("Can decode array with array")
	public void canDecodeArrayWithArray() {
		SocketArray array = decoder.decodeArray(new byte[] {
			(1 << 3) + 2, 4,
				1 << 3, 46,
				2 << 3, 55,
			2 << 3, 10
		}, 0, 8);
		Assertions.assertArrayEquals(new int[] { 0, 1 }, array.keys());
		Assertions.assertEquals(10, array.getInt(1));
		SocketArray subArray = array.getArray(0);
		Assertions.assertArrayEquals(new int[] { 0, 1 }, subArray.keys());
		Assertions.assertEquals(46, subArray.getInt(0));
		Assertions.assertEquals(55, subArray.getInt(1));
	}
	
}
