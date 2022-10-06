package everyos.ggd.server.socket.encoder;

import everyos.ggd.server.socket.SocketArray;

public interface SocketEncoder {
	
	byte[] encodeNumber(int i);
	
	byte[] encodeString(String s);
	
	byte[] encodeBoolean(boolean b);
	
	byte[] encodeFloat(float f);
	
	byte[] encodeArray(SocketArray array);

}
