package everyos.ggd.server.server.message;

import everyos.ggd.server.message.Message;
import everyos.ggd.server.socket.SocketArray;

public interface MessageDecoder {

	Message decode(SocketArray encoded);

}
