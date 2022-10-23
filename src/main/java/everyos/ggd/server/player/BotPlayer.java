package everyos.ggd.server.player;

import everyos.ggd.server.game.MatchData;
import everyos.ggd.server.game.vanilla.state.player.PlayerState;

public interface BotPlayer extends Player {

	void start(MatchData matchData, PlayerState playerState);

}
