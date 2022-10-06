package everyos.ggd.server.socket.decoder;

import everyos.ggd.server.socket.SocketArray;

public interface SocketDecoder {

	int decodeNumber(byte[] bs, int offset);

	String decodeString(byte[] bs, int offset, int length);

	boolean decodeBoolean(byte[] bs, int offset);
	
	float decodeFloat(byte[] bs, int offset);

	SocketArray decodeArray(byte[] bs, int offset, int length);

}
