package everyos.ggd.server.event.imp;

import everyos.ggd.server.event.Event;
import everyos.ggd.server.event.MessageEvent;
import everyos.ggd.server.message.Message;

public class MessageEventImp implements MessageEvent {

	private final Message[] messages;

	public MessageEventImp(Message[] messages) {
		this.messages = messages;
	}

	@Override
	public int code() {
		return Event.MESSAGE;
	}

	@Override
	public Message[] getMessages() {
		return messages;
	}

}
