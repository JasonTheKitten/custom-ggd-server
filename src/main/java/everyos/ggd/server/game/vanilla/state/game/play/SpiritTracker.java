package everyos.ggd.server.game.vanilla.state.game.play;

import java.util.List;

import everyos.ggd.server.game.Player;
import everyos.ggd.server.game.vanilla.MatchContext;
import everyos.ggd.server.game.vanilla.state.player.PlayerState;
import everyos.ggd.server.game.vanilla.state.player.PlayerState.SpiritGainReason;
import everyos.ggd.server.game.vanilla.state.spirit.SpiritState;
import everyos.ggd.server.game.vanilla.util.MapUtil;
import everyos.ggd.server.game.vanilla.util.MathUtil;
import everyos.ggd.server.map.MatchMap;
import everyos.ggd.server.map.Tile;
import everyos.ggd.server.message.SpiritStateUpdate.SpiritTeam;
import everyos.ggd.server.physics.Location;
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
		handleBaseCollision(playerEntityId, playerPosition);
		handlePlayerCollisions(playerEntityId, playerPosition);
	}

	private void givePlayerNearbySpirits(int playerEntityId, Position playerPosition) {
		for (SpiritState spiritState: spiritStates) {
			if (spiritState.getOwnerEntityId() == -1 && playerInRangeOfSpirit(playerEntityId, playerPosition, spiritState)) {
				spiritState.setTeam(getSpiritTeam(playerEntityId));
				spiritState.setOwnerEntityId(playerEntityId);
				PlayerState playerState = playerStates[playerEntityId];
				playerState
					.getSpiritList()
					.add(spiritState);
				playerState.gain(1, SpiritGainReason.COLLECT_SPIRIT);
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
	
	private void handleBaseCollision(int playerEntityId, Position playerPosition) {
		PlayerState playerState = playerStates[playerEntityId];
		List<SpiritState> spiritStates = playerState.getSpiritList();
		if (spiritStates.size() == 0 || !playerIsOnBase(playerEntityId, playerPosition)) {
			return;
		}
		int points = spiritStates.size();
		playerState.getStats().incrementScore(points);
		freeSpirits(spiritStates);
		playerState.gain(points, SpiritGainReason.GOAL_RETURN);
	}
	
	private boolean playerIsOnBase(int playerEntityId, Position playerPosition) {
		MatchMap map = matchContext.getMap();
		Location playerLocation = MapUtil.positionToLocation(map, playerPosition);
		Tile tile = map.getTile(playerLocation.getX(), playerLocation.getY());
		if (getSpiritTeam(playerEntityId) == SpiritTeam.GREEN_TEAM) {
			return tile.greenCanPass() && !tile.purpleCanPass();
		}
		return !tile.greenCanPass() && tile.purpleCanPass();
	}
	
	private void freeSpirits(List<SpiritState> spiritStates) {
		for (SpiritState state: spiritStates) {
			state.setOwnerEntityId(-1);
			state.reset();
		}
		spiritStates.clear();
	}
	
	private void handlePlayerCollisions(int playerEntityId, Position playerPosition) {
		for (SpiritState spiritState: spiritStates) {
			if (spiritState.getOwnerEntityId() != -1 && playerInRangeOfOtherTeamSpirit(playerEntityId, playerPosition, spiritState)) {
				int stolenSpiritCount = setTrailColor(playerEntityId, spiritState);
				playerStates[playerEntityId].gain(stolenSpiritCount, SpiritGainReason.STEAL_SPIRIT);
			}
		}
	}
	
	private int setTrailColor(int playerEntityId, SpiritState spiritState) {
		
		List<SpiritState> playerTrail = playerStates[playerEntityId]
				.getSpiritList();
		List<SpiritState> enemyTrail = playerStates[spiritState.getOwnerEntityId()]
			.getSpiritList();
		int enemySpiritIndex = enemyTrail.indexOf(spiritState);
		if (enemySpiritIndex == -1) {
			return 0;
		}
		
		int stolenSpiritCount = 0;
		SpiritTeam newTeam = getSpiritTeam(playerEntityId);
		while (enemyTrail.size() > enemySpiritIndex) {
			SpiritState state = enemyTrail.remove(enemySpiritIndex);
			playerTrail.add(state);
			state.setTeam(newTeam);
			state.setOwnerEntityId(playerEntityId);
			stolenSpiritCount++;
		}
		
		return stolenSpiritCount;
	}

	private boolean playerInRangeOfOtherTeamSpirit(int playerEntityId, Position playerPosition, SpiritState spiritState) {
		if (getSpiritTeam(playerEntityId) == getSpiritTeam(spiritState.getOwnerEntityId())) {
			return false;
		}
		Position spiritPosition = spiritState.getPhysicsBody().getCurrentPosition();
		return MathUtil.getDistanceBetweenPositions(playerPosition, spiritPosition) <= 1f;
	}
	
}
