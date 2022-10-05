package everyos.ggd.server.socket;

public interface SocketArray {

	int[] keys();
	
	void set(int index, int value);
	
	void set(int index, String value);
	
	void set(int index, boolean value);
	
	void set(int index, SocketArray value);

	void setRaw(int index, RawEntry entry);

	int getInt(int i);

	String getString(int i);

	boolean getBoolean(int i);

	SocketArray getArray(int i);
	
	RawEntry getRaw(int index);
	
	public static record RawEntry(byte[] bytes, int offset, int length, int type) {}

}
