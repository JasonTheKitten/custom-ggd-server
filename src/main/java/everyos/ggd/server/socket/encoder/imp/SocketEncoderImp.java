package everyos.ggd.server.socket.encoder.imp;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import everyos.ggd.server.socket.SocketArray;
import everyos.ggd.server.socket.SocketArray.RawEntry;
import everyos.ggd.server.socket.encoder.SocketEncoder;

public class SocketEncoderImp implements SocketEncoder {
	
	@Override
	public byte[] encodeNumber(int i) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		pushInt(outputStream, i);
		
		return outputStream.toByteArray();
	}
	
	@Override
	public byte[] encodeString(String s) {
		return s.getBytes();
	}
	
	@Override
	public byte[] encodeBoolean(boolean b) {
		return encodeNumber(b ? 1: 0);
	}
	
	@Override
	public byte[] encodeArray(SocketArray array) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		for (int key: array.keys()) {
			RawEntry entry = array.getRaw(key);
			pushInt(outputStream, encodeIndex(key, entry.type()));
			pushRawEntry(outputStream, entry);
		}
		
		return outputStream.toByteArray();
	}

	private int encodeIndex(int i, int type) {
		return ((i + 1) << 3) + type;
	}

	private void pushInt(ByteArrayOutputStream outputStream, int i) {
		while (i > 127) {
			outputStream.write((i & ((1 << 7) - 1)) + (1 << 7));
			i >>>= 7;
		}
		outputStream.write(i);
	}
	
	private void pushRawEntry(ByteArrayOutputStream outputStream, RawEntry entry) {
		if (entry.type() == 2) {
			pushInt(outputStream, entry.length());
		}
		outputStream.writeBytes(Arrays.copyOfRange(
			entry.bytes(),
			entry.offset(),
			entry.offset() + entry.length()));
	}

}
