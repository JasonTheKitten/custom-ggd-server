package everyos.ggd.server.message.imp;

import everyos.ggd.server.message.ClearSessionDataMessage;
import everyos.ggd.server.message.Message;

public class ClearSessionDataMessageImp implements ClearSessionDataMessage {

	@Override
	public int getType() {
		return Message.CLEAR_SESSION_DATA;
	}

}
