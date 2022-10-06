package everyos.ggd.server.socket.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import everyos.ggd.server.socket.SocketArray;
import everyos.ggd.server.socket.decoder.SocketDecoder;
import everyos.ggd.server.socket.encoder.SocketEncoder;

public class SocketArrayImp implements SocketArray {
	
	private static final byte INT_LENGTH = 0;
	private static final byte FIXED_LENGTH = 2;
	private static final byte FLOAT_LENGTH = 5;

	private final List<Map<Integer, RawEntry>> overloads = new ArrayList<>();
	private final SocketDecoder decoder;
	private final SocketEncoder encoder;
	
	private Map<Integer, Object> cache;
	private Map<Integer, RawEntry> rawEntries;

	public SocketArrayImp(SocketDecoder decoder, SocketEncoder encoder) {
		this.decoder = decoder;
		this.encoder = encoder;
		overload(0);
	}
	
	@Override
	public SocketArray overload(int level) {
		if (level == overloads.size()) {
			overloads.add(new HashMap<>());
		}
		Map<Integer, RawEntry> newRawEntries = overloads.get(level);
		if (rawEntries != newRawEntries) {
			cache = new HashMap<>();
			rawEntries = newRawEntries;
		}
		
		return this;
	}
	
	@Override
	public boolean hasOverload(int level) {
		return overloads.size() > level;
	}

	@Override
	public int[] keys() {
		int[] keys = rawEntries
			.keySet()
			.stream()
			.mapToInt(Integer::intValue)
			.toArray();
		overload(0);
		return keys;
	}
	
	@Override
	public boolean hasKey(int index) {
		boolean hasKey = rawEntries.containsKey(index);
		overload(0);
		
		return hasKey;
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
	public void set(int index, float value) {
		cache.put(index, value);
		createRawEntry(index, encoder.encodeFloat(value), FLOAT_LENGTH);
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
		overload(0);
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
	public Float getFloat(int index) {
		return getElement(index,
			info -> decoder.decodeFloat(info.bytes(), info.offset()));
	}
	
	@Override
	public SocketArray getArray(int index) {
		return getElement(index,
			info -> decoder.decodeArray(info.bytes(), info.offset(), info.length()));
	}
	
	@Override
	public RawEntry getRaw(int index) {
		RawEntry entry = rawEntries.get(index);
		overload(0);
		return entry;
	}
	
	@SuppressWarnings("unchecked")
	private <T> T getElement(int index, Function<RawEntry, T> converter) {
		return (T) cache.computeIfAbsent(index, _1 -> {
			RawEntry info = rawEntries.get(index);
			if (info == null) {
				return null; 
			}
			overload(0);
			return converter.apply(info);
		});
	}
	
	private void createRawEntry(int index, byte[] bytes, byte type) {
		RawEntry entry = new RawEntry(bytes, 0, bytes.length, type);
		rawEntries.put(index, entry);
		overload(0);
	}

}
