package everyos.ggd.server.matchmaker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import everyos.ggd.server.common.imp.TickTimerImp;
import everyos.ggd.server.game.Match;
import everyos.ggd.server.game.Player;
import everyos.ggd.server.game.vanilla.VanillaMatch;
import everyos.ggd.server.session.SessionData;
import everyos.ggd.server.session.SessionManager;

public class MatchMakerTest {
	
	private SessionManager sessionManager;
	private MatchMaker matchMaker;
	
	@BeforeEach
	private void beforeEach() {
		sessionManager = new SessionManager();
		//TODO: Mock the match
		matchMaker = new MatchMaker(sessionManager, id -> new VanillaMatch(id, new TickTimerImp(60)));
	}

	@Test
	@DisplayName("Player ID is unique")
	public void sessionIdIsUnique() {
		SessionData sessionData1 = matchMaker.createSession();
		SessionData sessionData2 = matchMaker.createSession();
		Assertions.assertNotEquals(sessionData1.playerId(), sessionData2.playerId());
	}
	
	@Test
	@DisplayName("Session associated with valid match and player")
	public void sessionIsValid() {
		SessionData sessionData = matchMaker.createSession();
		Match match = sessionManager.getMatch(sessionData.matchId());
		Player player = match.getPlayer(sessionData.playerId());
		Assertions.assertNotNull(player);
	}
	
	@Test
	@DisplayName("8 players can enter the same match")
	public void maxPlayersCanEnterMatch() {
		SessionData sessionData1 = matchMaker.createSession();
		for (int i = 0; i < 7; i++) {
			SessionData sessionData2 = matchMaker.createSession();
			Assertions.assertEquals(sessionData1.matchId(), sessionData2.matchId());
		}
	}
	
	@Test
	@DisplayName("9 players can not enter the same match")
	public void overMaxPlayersCannotEnterMatch() {
		SessionData sessionData1 = matchMaker.createSession();
		for (int i = 0; i < 7; i++) {
			matchMaker.createSession();
		}
		SessionData sessionData2 = matchMaker.createSession();
		Assertions.assertNotEquals(sessionData1.matchId(), sessionData2.matchId());
	}
	
	@Test
	@DisplayName("Can fulfill match early")
	public void canFulfillMatchEarly() {
		SessionData sessionData1 = matchMaker.createSession();
		matchMaker.fulfillCurrentMatch();
		SessionData sessionData2 = matchMaker.createSession();
		Assertions.assertNotEquals(sessionData1.matchId(), sessionData2.matchId());
	}
	
}
