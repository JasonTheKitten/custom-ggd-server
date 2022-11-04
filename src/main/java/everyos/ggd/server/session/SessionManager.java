package everyos.ggd.server.session;

import java.util.HashMap;
import java.util.Map;

import everyos.ggd.server.game.Match;

public class SessionManager {

	private final Map<Integer, Match> matches = new HashMap<>();
	
	public void registerMatch(Match vanillaMatch) {
		matches.put(vanillaMatch.getId(), vanillaMatch);
	}
	
	public Match getMatch(int matchId) {
		return matches.get(matchId);
	}

	public void unregisterMatch(int matchId) {
		matches.remove(matchId);
	}

}
