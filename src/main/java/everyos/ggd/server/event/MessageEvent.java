package everyos.ggd.server.event;

import everyos.ggd.server.message.Message;

public interface MessageEvent extends Event {

	Message[] getMessages();
	
}
