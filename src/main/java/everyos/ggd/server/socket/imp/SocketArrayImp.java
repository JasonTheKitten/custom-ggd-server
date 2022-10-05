package everyos.ggd.server.socket.imp;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import everyos.ggd.server.socket.SocketArray;
import everyos.ggd.server.socket.decoder.SocketDecoder;
import everyos.ggd.server.socket.encoder.SocketEncoder;

public class SocketArrayImp implements SocketArray {
	
	private static final byte INT_LENGTH = 0;
	private static final byte FIXED_LENGTH = 2;

	private final Map<Integer, Object> cache = new HashMap<>();
	private final Map<Integer, RawEntry> rawEntries = new HashMap<>();
	private final SocketDecoder decoder;
	private final SocketEncoder encoder;

	public SocketArrayImp(SocketDecoder decoder, SocketEncoder encoder) {
		this.decoder = decoder;
		this.encoder = encoder;
	}

	@Override
	public int[] keys() {
		return rawEntries
			.keySet()
			.stream()
			.mapToInt(Integer::intValue)
			.toArray();
	}
	
	@Override
	public void set(int index, int value) {
		cache.put(index, value);
		createRawEntry(index, encoder.encodeNumber(value), INT_LENGTH);
	}
	
	@Override
	public void set(int index, String value) {
		cache.put(index, value);
		createRawEntry(index, encoder.encodeString(value), FIXED_LENGTH);
	}
	
	@Override
	public void set(int index, boolean value) {
		cache.put(index, value);
		createRawEntry(index, encoder.encodeBoolean(value), INT_LENGTH);
	}
	
	@Override
	public void set(int index, SocketArray value) {
		cache.put(index, value);
		createRawEntry(index, encoder.encodeArray(value), FIXED_LENGTH);
	}

	@Override
	public void setRaw(int index, RawEntry entry) {
		rawEntries.put(index, entry);
		cache.remove(index);
	}
	
	@Override
	public int getInt(int index) {
		return getElement(index,
			info -> decoder.decodeNumber(info.bytes(), info.offset()));
	};

	@Override
	public String getString(int index) {
		return getElement(index,
			info -> decoder.decodeString(info.bytes(), info.offset(), info.length()));
	}
	
	@Override
	public boolean getBoolean(int index) {
		return getElement(index,
			info -> decoder.decodeBoolean(info.bytes(), info.offset()));
	};
	
	@Override
	public SocketArray getArray(int index) {
		return getElement(index,
			info -> decoder.decodeArray(info.bytes(), info.offset(), info.length()));
	}
	
	@Override
	public RawEntry getRaw(int index) {
		return rawEntries.get(index);
	}
	
	@SuppressWarnings("unchecked")
	private <T> T getElement(int index, Function<RawEntry, T> converter) {
		return (T) cache.computeIfAbsent(index, _1 -> {
			RawEntry info = rawEntries.get(index);
			if (info == null) {
				return null; 
			}
			return converter.apply(info);
		});
	}
	
	private void createRawEntry(int index, byte[] bytes, byte type) {
		RawEntry entry = new RawEntry(bytes, 0, bytes.length, type);
		rawEntries.put(index, entry);
	}

}
