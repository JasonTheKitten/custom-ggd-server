package everyos.ggd.server.game.vanilla.state.game.play;

import java.util.List;

import everyos.ggd.server.game.Player;
import everyos.ggd.server.game.vanilla.MatchContext;
import everyos.ggd.server.game.vanilla.state.player.PlayerState;
import everyos.ggd.server.game.vanilla.state.spirit.SpiritState;
import everyos.ggd.server.game.vanilla.util.MathUtil;
import everyos.ggd.server.message.SpiritStateUpdate.SpiritTeam;
import everyos.ggd.server.physics.Position;

public class SpiritTracker {
	
	private final SpiritTrail spiritTrail = new SpiritTrail();
	private final MatchContext matchContext;
	private final PlayerState[] playerStates;
	private final List<SpiritState> spiritStates;
	
	public SpiritTracker(MatchContext matchContext, PlayerState[] playerStates, List<SpiritState> spiritStates) {
		this.matchContext = matchContext;
		this.playerStates = playerStates;
		this.spiritStates = spiritStates;
	}

	public void tick() {
		for (SpiritState spiritState: spiritStates) {
			matchContext.broadcastMessages(spiritState.getQueuedMessages());
 		}
	}
	
	public void handlePlayerPositionUpdate(int playerEntityId, Position playerPosition) {
		givePlayerNearbySpirits(playerEntityId, playerPosition);
		spiritTrail.handlePlayerPositionUpdate(playerStates[playerEntityId], playerPosition);
	}

	private void givePlayerNearbySpirits(int playerEntityId, Position playerPosition) {
		for (SpiritState spiritState: spiritStates) {
			if (spiritState.getOwnerEntityId() == -1 && playerInRangeOfSpirit(playerEntityId, playerPosition, spiritState)) {
				spiritState.setTeam(getSpiritTeam(playerEntityId));
				spiritState.setOwnerEntityId(playerEntityId);
				playerStates[playerEntityId]
					.getSpiritList()
					.add(spiritState);
			}
		}
	}
	
	private SpiritTeam getSpiritTeam(int playerEntityId) {
		Player[] players = matchContext.getPlayers();
		return playerEntityId < players.length / 2 ?
			SpiritTeam.GREEN_TEAM :
			SpiritTeam.PURPLE_TEAM;
	}

	private boolean playerInRangeOfSpirit(int playerEntityId, Position playerPosition, SpiritState spiritState) {
		//TODO: Magnetism
		Position spiritPosition = spiritState.getPhysicsBody().getCurrentPosition();
		return MathUtil.getDistanceBetweenPositions(playerPosition, spiritPosition) <= 3f;
	}
	
}
