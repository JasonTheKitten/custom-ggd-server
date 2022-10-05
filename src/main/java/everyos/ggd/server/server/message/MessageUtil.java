package everyos.ggd.server.server.message;

import everyos.ggd.server.event.MessageEvent;
import everyos.ggd.server.event.imp.MessageEventImp;
import everyos.ggd.server.message.Message;

public final class MessageUtil {
	
	private MessageUtil() {}

	public static MessageEvent message(Message message) {
		return new MessageEventImp(new Message[] {
			message
		});
	}
	
}
