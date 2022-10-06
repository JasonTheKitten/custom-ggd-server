package everyos.ggd.server.server.message;

import everyos.ggd.server.message.Message;
import everyos.ggd.server.socket.SocketArray;

public interface MessageEncoder {

	SocketArray encode(Message message);
	
}
