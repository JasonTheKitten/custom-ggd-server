package everyos.ggd.server.socket.encoder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import everyos.ggd.server.socket.SocketArray;
import everyos.ggd.server.socket.encoder.imp.SocketEncoderImp;
import everyos.ggd.server.socket.imp.SocketArrayImp;

public class SocketEncoderTest {

	private SocketEncoder encoder;
	
	@BeforeEach
	private void beforeEach() {
		this.encoder = new SocketEncoderImp();
	}
	
	@Test
	@DisplayName("Can encode simple number")
	public void canEncodeSimpleNumber() {
		byte[] encoded = encoder.encodeNumber(4);
		Assertions.assertArrayEquals(new byte[] { 4 }, encoded);
	}
	
	@Test
	@DisplayName("Can encode large number")
	public void canEncodeLargeNumber() {
		byte[] encoded = encoder.encodeNumber(355);
		Assertions.assertArrayEquals(new byte[] { -29, 2 }, encoded);
	}
	
	@Test
	@DisplayName("Can encode simple number")
	public void canEncodeNegativeNumber() {
		byte[] encoded = encoder.encodeNumber(-1);
		Assertions.assertArrayEquals(new byte[] {
			-1, -1, -1, -1, -1, -1, -1, -1, 1
		}, encoded);
	}
	
	@Test
	@DisplayName("Can encode string")
	public void canEncodeString() {
		byte[] encoded = encoder.encodeString("XKCD");
		Assertions.assertArrayEquals(new byte[] {
			'X', 'K', 'C', 'D'
		}, encoded);
	}
	
	@Test
	@DisplayName("Can encode boolean")
	public void canEncodeBoolean() {
		byte[] encoded = encoder.encodeBoolean(true);
		Assertions.assertArrayEquals(new byte[] { 1 }, encoded);
		encoded = encoder.encodeBoolean(false);
		Assertions.assertArrayEquals(new byte[] { 0 }, encoded);
	}
	
	@Test
	@DisplayName("Can encode float")
	public void canEncodeFloat() {
		byte[] encoded = encoder.encodeFloat(1.1f);
		Assertions.assertArrayEquals(new byte[] { -51, -52, -116, 63 }, encoded);
	}
	
	@Test
	@DisplayName("Can encode empty array")
	public void canEncodeEmptyArray() {
		SocketArray array = new SocketArrayImp(null, encoder);
		byte[] encoded = encoder.encodeArray(array);
		Assertions.assertArrayEquals(new byte[] {}, encoded);
	}
	
	@Test
	@DisplayName("Can encode array with number")
	public void canDecodeArrayWithSimpleNumber() {
		SocketArray array = new SocketArrayImp(null, encoder);
		array.set(0, 355);
		byte[] encoded = encoder.encodeArray(array);
		Assertions.assertArrayEquals(new byte[] {
			1 << 3, -29, 2
		}, encoded);
	}
	
	@Test
	@DisplayName("Can encode array with string")
	public void canEncodeArrayWithString() {
		SocketArray array = new SocketArrayImp(null, encoder);
		array.set(0, "XKCD");
		array.set(1, 42);
		byte[] encoded = encoder.encodeArray(array);
		Assertions.assertArrayEquals(new byte[] {
			(1 << 3) + 2, 4, 'X', 'K', 'C', 'D',
			2 << 3, 42
		}, encoded);
	}
	
	@Test
	@DisplayName("Can encode array with booleans")
	public void canDecodeArrayWithBooleans() {
		SocketArray array = new SocketArrayImp(null, encoder);
		array.set(0, true);
		array.set(1, false);
		byte[] encoded = encoder.encodeArray(array);
		Assertions.assertArrayEquals(new byte[] {
			1 << 3, 1,
			2 << 3, 0
		}, encoded);
	}
	
	@Test
	@DisplayName("Can encode array with float")
	public void canEncodeArrayWithFloat() {
		SocketArray array = new SocketArrayImp(null, encoder);
		array.set(0, 1.1f);
		array.set(1, 42);
		byte[] encoded = encoder.encodeArray(array);
		Assertions.assertArrayEquals(new byte[] {
			(1 << 3) + 5, -51, -52, -116, 63,
			2 << 3, 42
		}, encoded);
	}
	
	@Test
	@DisplayName("Can encode array with array")
	public void canEncodeArrayWithArray() {
		SocketArray subArray = new SocketArrayImp(null, encoder);
		subArray.set(0, 46);
		subArray.set(1, 55);
		
		SocketArray array = new SocketArrayImp(null, encoder);
		array.set(0, subArray);
		array.set(1, 10);
		
		byte[] encoded = encoder.encodeArray(array);
		Assertions.assertArrayEquals(new byte[] {
			(1 << 3) + 2, 4,
				1 << 3, 46,
				2 << 3, 55,
			2 << 3, 10
		}, encoded);
	}
	
	@Test
	@DisplayName("Can encode array with overloads")
	public void canEncodeArrayWithOverloads() {
		SocketArray array = new SocketArrayImp(null, encoder);
		array.set(0, 4);
		array.overload(1).set(0, 2);
		
		byte[] encoded = encoder.encodeArray(array);
		Assertions.assertArrayEquals(new byte[] {
			1 << 3, 4,
			1 << 3, 2
		}, encoded);
	}
	
}
