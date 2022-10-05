package everyos.ggd.server.message.imp;

import everyos.ggd.server.message.Message;
import everyos.ggd.server.message.ServerConnectMessage;

public class ServerConnectMessageImp implements ServerConnectMessage {

	private final String url;
	private final String matchName;

	public ServerConnectMessageImp(String url, String matchName) {
		this.url = url;
		this.matchName = matchName;
	}

	@Override
	public int getType() {
		return Message.SERVER_CONNECT;
	}

	@Override
	public String getURL() {
		return url;
	}

	@Override
	public String getMatchName() {
		return matchName;
	}

}
