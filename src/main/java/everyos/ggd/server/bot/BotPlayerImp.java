package everyos.ggd.server.bot;

import java.util.ArrayList;
import java.util.List;

import everyos.ggd.server.bot.state.BotPlayState;
import everyos.ggd.server.bot.state.BotState;
import everyos.ggd.server.game.MatchData;
import everyos.ggd.server.game.vanilla.state.player.PlayerState;
import everyos.ggd.server.message.Message;
import everyos.ggd.server.player.BotPlayer;

public class BotPlayerImp implements BotPlayer {

	private final int id;
	private final String authenticationKey;
	private final List<Message> incomingMessages = new ArrayList<>();
	private final List<Message> outgoingMessages = new ArrayList<>();

	private BotState state;
	
	public BotPlayerImp(int id, String authenticationKey) {
		this.id = id;
		this.authenticationKey = authenticationKey;
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public String getAuthenticationKey() {
		return this.authenticationKey;
	}

	@Override
	public boolean isBot() {
		return true;
	}

	@Override
	public void onMessageFromServer(Message message) {
		incomingMessages.add(message);
	}

	@Override
	public List<Message> getQueuedMessagesFromClient() {
		return outgoingMessages;
	}

	@Override
	public void ping() {
		if (state != null) {
			state.ping(incomingMessages);
		}
	}

	@Override
	public void start(MatchData matchData, PlayerState playerState) {
		this.state = new BotPlayState(matchData, playerState);
	}

}
