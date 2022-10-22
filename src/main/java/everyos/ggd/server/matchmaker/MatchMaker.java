package everyos.ggd.server.matchmaker;

import java.util.function.Function;

import everyos.ggd.server.game.Match;
import everyos.ggd.server.game.Player;
import everyos.ggd.server.game.imp.HumanPlayerImp;
import everyos.ggd.server.game.vanilla.state.bot.BotPlayerImp;
import everyos.ggd.server.session.SessionData;
import everyos.ggd.server.session.SessionManager;

public class MatchMaker {
	
	private final SessionManager sessionManager;
	private final Function<Integer, Match> matchGenerator;
	
	private Match currentMatch;
	private int nextMatchId = 0;
	private int nextPlayerId = 0;

	public MatchMaker(SessionManager sessionManager, Function<Integer, Match> matchGenerator) {
		this.sessionManager = sessionManager;
		this.matchGenerator = matchGenerator;
		this.currentMatch = createNewMatch();
	}

	public SessionData createSession() {
		Player player = createNewPlayer(false);
		Match match = addPlayerToMatch(player);
		
		return new SessionData(
			match.getId(),
			player.getId(),
			player.getAuthenticationKey());
	}
	
	public void fulfillCurrentMatch() {
		while (currentMatch.addPlayer(createNewPlayer(true)));
		nextPlayerId--;
	}
	
	private Player createNewPlayer(boolean isBot) {
		int playerId = nextPlayerId++;
		String playerAuthenticationKey = generateRandomAuthenticationKey();
		
		if (isBot) {
			return new BotPlayerImp(playerId,  playerAuthenticationKey);
		} else {
			return new HumanPlayerImp(playerId, playerAuthenticationKey);
		}
	}
	
	private String generateRandomAuthenticationKey() {
		StringBuilder builtToken = new StringBuilder();
		for (int i = 0; i < 16; i++) {
			if (i % 4 == 0 && i > 0) {
				builtToken.append("-");
			}
			char randomChar = (char) ('A' + Math.floor(Math.random() * ('Z'-'A')));
			builtToken.append(randomChar);
		}

		return builtToken.toString();
	}

	private Match createNewMatch() {
		int matchId = nextMatchId++;
		Match match = matchGenerator.apply(matchId);
		sessionManager.registerMatch(match);
		
		return match;
	}

	private Match addPlayerToMatch(Player player) {
		if (currentMatch.addPlayer(player)) {
			return currentMatch;
		}
		
		currentMatch = createNewMatch();
		currentMatch.addPlayer(player);
		
		return currentMatch;
	}

}
