package everyos.ggd.server.game.vanilla.state.game.play;

import java.util.ArrayList;
import java.util.List;

import everyos.ggd.server.game.vanilla.MatchContext;
import everyos.ggd.server.game.vanilla.state.player.PlayerState;
import everyos.ggd.server.game.vanilla.state.player.PlayerState.SpiritGainReason;
import everyos.ggd.server.game.vanilla.state.spirit.SpiritState;
import everyos.ggd.server.game.vanilla.state.spirit.SpiritStateImp;
import everyos.ggd.server.game.vanilla.util.MapUtil;
import everyos.ggd.server.game.vanilla.util.MathUtil;
import everyos.ggd.server.map.MatchMap;
import everyos.ggd.server.map.Tile;
import everyos.ggd.server.message.PlayerStateUpdate.Upgrade;
import everyos.ggd.server.message.SpiritStateUpdate.SpiritTeam;
import everyos.ggd.server.physics.Location;
import everyos.ggd.server.physics.Position;
import everyos.ggd.server.player.Player;

public class SpiritTracker {
	
	private static final int BUDDY_BONUS_AMOUNT = 5;
	
	private final SpiritTrailTracker spiritTrailTracker = new SpiritTrailTracker();
	private final MatchContext matchContext;
	private final PlayerState[] playerStates;
	private final List<SpiritState> mapSpiritStates;
	private final List<SpiritState> playerSpiritStates;
	
	public SpiritTracker(MatchContext matchContext, PlayerState[] playerStates, List<SpiritState> mapSpiritStates) {
		this.matchContext = matchContext;
		this.playerStates = playerStates;
		this.mapSpiritStates = mapSpiritStates;
		this.playerSpiritStates = new ArrayList<>();
	}

	public void tick() {
		respawnMapSpiritStates();
	}

	public void handlePlayerPositionUpdate(int playerEntityId, Position playerPosition) {
		givePlayerNearbySpirits(playerEntityId, playerPosition);
		updateSpiritTrail(playerEntityId, playerPosition);
		handleBaseCollision(playerEntityId, playerPosition);
		handlePlayerCollisions(playerEntityId, playerPosition);
	}

	private void respawnMapSpiritStates() {
		for (SpiritState spiritState: mapSpiritStates) {
			if (checkCanRespawnSpirit(spiritState)) {
				spiritState.reset();
			}
		}
	}

	private boolean checkCanRespawnSpirit(SpiritState spiritState) {
		return
			spiritState.getCollectedTime() != 0 &&
			(System.currentTimeMillis() - spiritState.getCollectedTime()) >= 20000;
	}

	private void givePlayerNearbySpirits(int playerEntityId, Position playerPosition) {
		for (SpiritState spiritState: mapSpiritStates) {
			if (spiritNotCollected(spiritState) && playerInRangeOfSpirit(playerEntityId, playerPosition, spiritState)) {
				markSpiritCollected(spiritState);
				PlayerState playerState = playerStates[playerEntityId];
				givePlayerNewSpirit(playerState);
				playerState.gain(1, SpiritGainReason.COLLECT_SPIRIT);
			}
		}
	}
	
	private void givePlayerNewSpirit(PlayerState playerState) {
		int playerEntityId = playerState.getEntityId();
		SpiritState trailSpiritState = createSpirit();
		trailSpiritState.setTeam(getSpiritTeam(playerEntityId));
		trailSpiritState.setOwnerEntityId(playerEntityId);
		playerSpiritStates.add(trailSpiritState);
		playerState
			.getSpiritList()
			.add(trailSpiritState);
	}

	private SpiritState createSpirit() {
		return matchContext
			.getEntityRegister()
			.createEntity(id -> new SpiritStateImp(id, null));
	}

	private boolean spiritNotCollected(SpiritState spiritState) {
		return spiritState.getCollectedTime() == 0;
	}
	
	private void markSpiritCollected(SpiritState spiritState) {
		spiritState.setCollectedTime(System.currentTimeMillis());
		spiritState.setTeam(SpiritTeam.NONE);
	}

	private SpiritTeam getSpiritTeam(int playerEntityId) {
		Player[] players = matchContext.getPlayers();
		return playerEntityId < players.length / 2 ?
			SpiritTeam.GREEN_TEAM :
			SpiritTeam.PURPLE_TEAM;
	}

	private boolean playerInRangeOfSpirit(int playerEntityId, Position playerPosition, SpiritState spiritState) {
		Position spiritPosition = spiritState.getPhysicsBody().getCurrentPosition();
		boolean hasMagnetismUpgrade = playerStates[playerEntityId].getUpgradeLevel().ordinal() >= Upgrade.MAGNET_UPGRADE.ordinal();
		float collectionRadius = hasMagnetismUpgrade ? 6f : 3f;
		return MathUtil.getDistanceBetweenPositions(playerPosition, spiritPosition) <= collectionRadius;
	}
	
	private void updateSpiritTrail(int playerEntityId, Position playerPosition) {
		spiritTrailTracker.handlePlayerPositionUpdate(playerStates[playerEntityId], playerPosition);
	}
	
	private void handleBaseCollision(int playerEntityId, Position playerPosition) {
		PlayerState playerState = playerStates[playerEntityId];
		List<SpiritState> spiritStates = playerState.getSpiritList();
		if (spiritStates.size() == 0 || !playerIsOnBase(playerEntityId, playerPosition)) {
			return;
		}
		int points = spiritStates.size();
		playerState.getStats().incrementScore(points);
		deleteSpirits(spiritStates);
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
	
	private void deleteSpirits(List<SpiritState> spiritStates) {
		for (SpiritState state: spiritStates) {
			playerSpiritStates.remove(state);
			matchContext
				.getEntityRegister()
				.removeEntity(state);
		}
		spiritStates.clear();
	}
	
	private void handlePlayerCollisions(int playerEntityId, Position playerPosition) {
		handleBuddyCollisions(playerEntityId, playerPosition);
		handleEnemyCollisions(playerEntityId, playerPosition);
	}
	
	private void handleBuddyCollisions(int playerEntityId, Position playerPosition) {
		PlayerState playerState = playerStates[playerEntityId];
		for (PlayerState otherPlayerState: playerStates) {
			if (
				!playersAreBuddy(playerState, otherPlayerState) ||
				!playersInRange(playerState, otherPlayerState) ||
				!playerElligibleForBuddyBonus(playerState)) {
				continue;
			}
			
			givePlayerBuddyBonus(playerState);
			break;
		}
	}

	private boolean playersAreBuddy(PlayerState playerState, PlayerState otherPlayerState) {
		return
			playerState.getEntityId() != otherPlayerState.getEntityId() &&
			getSpiritTeam(playerState.getEntityId()) == getSpiritTeam(otherPlayerState.getEntityId());
	}
	
	private boolean playersInRange(PlayerState playerState, PlayerState otherPlayerState) {
		Position playerPosition = playerState.getPhysicsBody().getCurrentPosition();
		Position otherPlayerPosition = otherPlayerState.getPhysicsBody().getCurrentPosition();
		return MathUtil.getDistanceBetweenPositions(playerPosition, otherPlayerPosition) <= 3f;
	}
	
	private boolean playerElligibleForBuddyBonus(PlayerState playerState) {
		MatchMap map = matchContext.getMap();
		Position playerPosition = playerState.getPhysicsBody().getCurrentPosition();
		Location playerLocation = MapUtil.positionToLocation(map, playerPosition);
		Tile tile = map.getTile(playerLocation.getX(), playerLocation.getY());
		return
			System.currentTimeMillis() - playerState.getLastBuddyBonusTime() >= 3000 &&
			tile.greenCanPass() == tile.purpleCanPass();
	}
	
	private void givePlayerBuddyBonus(PlayerState playerState) {
		for (int i = 0; i < BUDDY_BONUS_AMOUNT; i++) {
			givePlayerNewSpirit(playerState);
		}
		playerState.gain(BUDDY_BONUS_AMOUNT, SpiritGainReason.BUDDY_BONUS);
		playerState.setLastBuddyBonusTime(System.currentTimeMillis());
	}

	private void handleEnemyCollisions(int playerEntityId, Position playerPosition) {
		for (SpiritState spiritState: playerSpiritStates) {
			if (playerInRangeOfOtherTeamSpirit(playerEntityId, playerPosition, spiritState)) {
				int otherPlayerId = spiritState.getOwnerEntityId();
				int stolenSpiritCount = setTrailColor(playerEntityId, spiritState);
				playerStates[playerEntityId].gain(stolenSpiritCount, SpiritGainReason.STEAL_SPIRIT);
				playerStates[otherPlayerId].loose(stolenSpiritCount);
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
