package everyos.ggd.server.bot.state;

import java.util.List;

import everyos.ggd.server.bot.Direction;
import everyos.ggd.server.bot.PathFinder;
import everyos.ggd.server.bot.pathfinder.AStarPathFinder;
import everyos.ggd.server.game.MatchData;
import everyos.ggd.server.game.vanilla.state.entity.Entity;
import everyos.ggd.server.game.vanilla.state.player.PlayerState;
import everyos.ggd.server.game.vanilla.state.spirit.SpiritState;
import everyos.ggd.server.game.vanilla.util.MapUtil;
import everyos.ggd.server.game.vanilla.util.MathUtil;
import everyos.ggd.server.map.MatchMap;
import everyos.ggd.server.map.Tile;
import everyos.ggd.server.message.Message;
import everyos.ggd.server.message.PlayerStateUpdate.Upgrade;
import everyos.ggd.server.message.SpiritStateUpdate.SpiritTeam;
import everyos.ggd.server.physics.Location;
import everyos.ggd.server.physics.Position;
import everyos.ggd.server.physics.Velocity;
import everyos.ggd.server.physics.imp.LocationImp;
import everyos.ggd.server.physics.util.LocationUtil;
import everyos.ggd.server.physics.util.PhysicsUtil;

public class BotPlayState implements BotState {
	
	private static final int SPIRIT_TARGET_COOLDOWN_MILLIS = 2500;

	private final MatchMap map;
	private final List<Entity> entities;
	private final PlayerState playerState;
	private final boolean isGreenTeam;
	
	private PathFinder pathFinder;
	private Target target;
	private long lastLocationUpdate = 0;
	private Location nextTargetLocation;
	private int prevNumSpirits = 0;
	private long spiritTargetCooldownStart = 0;

	public BotPlayState(MatchData matchData, PlayerState playerState) {
		this.map = matchData.map();
		this.entities = matchData.entities();
		this.playerState = playerState;
		this.isGreenTeam = playerState.getEntityId() < 4; //TODO
		this.nextTargetLocation = MapUtil.positionToLocation(
			map,
			playerState.getPhysicsBody().getCurrentPosition());
	}

	@Override
	public List<Message> ping(List<Message> serverMessages) {
		checkTargetChanged();
		updateLocation();
		updateVelocity();
		updatePositionIfOutdated();
		
		return List.of();
	}

	private void checkTargetChanged() {
		if (!isInCoolDown() && playerState.getSpiritList().size() < prevNumSpirits) {
			spiritTargetCooldownStart = System.currentTimeMillis();
			target = null;
		}
		if ((pathFinder == null || playerState.getSpiritList().size() < 15) && target != Target.SPIRIT) {
			target = Target.SPIRIT;
			this.pathFinder = new AStarPathFinder(
				location -> hasUncollectedSpirit(location),
				location -> playerCanNotPass(location),
				nextTargetLocation);
		} else if (playerState.getSpiritList().size() >= 15 && target != Target.GOAL) {
			target = Target.GOAL;
			this.pathFinder = new AStarPathFinder(
				location -> isInsideCenterOfPlayerBase(location),
				location -> playerCanNotPass(location),
				nextTargetLocation);
		}
		prevNumSpirits = playerState.getSpiritList().size();
	}

	private boolean isInCoolDown() {
		return System.currentTimeMillis() - spiritTargetCooldownStart < SPIRIT_TARGET_COOLDOWN_MILLIS;
	}

	private boolean hasUncollectedSpirit(Location location) {
		for (Entity entity: entities) {
			if (!(entity instanceof SpiritState)) {
				continue;
			}
			SpiritTeam team = ((SpiritState) entity).getTeam();
			if (canTargetSpirit(team)) {
				Position entityPosition = entity.getPhysicsBody().getCurrentPosition();
				Location entityLocation = MapUtil.positionToLocation(map, entityPosition);
				if (entityLocation.equals(location)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	private boolean canTargetSpirit(SpiritTeam team) {
		return
			team == SpiritTeam.NO_TEAM ||
			(isGreenTeam && team == SpiritTeam.PURPLE_TEAM && !isInCoolDown()) ||
			(!isGreenTeam && team == SpiritTeam.GREEN_TEAM && !isInCoolDown());
	}

	private boolean isInsideCenterOfPlayerBase(Location location) {
		if (!isPlayerBase(location)) {
			return false;
		}
		
		for (Direction direction: Direction.DIRECTIONS) {
			Location neighborLocation = LocationUtil.addDirection(location, direction);
			if (!isPlayerBase(neighborLocation)) {
				return false;
			}
		}
		
		return true;
	}

	private boolean isPlayerBase(Location location) {
		Tile tile = map.getTile(location);
		return
			tile.greenCanPass() != tile.purpleCanPass() &&
			tile.greenCanPass() == isGreenTeam;
	}
	
	private boolean playerCanNotPass(Location location) {
		if (playerState.getUpgradeLevel().ordinal() >= Upgrade.TRANSPARENT_UPGRADE.ordinal()) {
			return !isInBounds(location) || isDifferentPlayerBase(location);
		} else {
			return isWall(location);
		}
	}

	private boolean isInBounds(Location location) {
		int x = location.getX();
		int y = location.getY();
		return !(x < 0 || y < 0 || x >= map.getWidth() || y >= map.getHeight());
	}

	private boolean isWall(Location location) {
		Tile tile = map.getTile(location);
		return
			(isGreenTeam && !tile.greenCanPass()) ||
			(!isGreenTeam && !tile.purpleCanPass());
	}

	private boolean isDifferentPlayerBase(Location location) {
		Tile tile = map.getTile(location);
		return
			tile.greenCanPass() != tile.purpleCanPass() &&
			isGreenTeam != tile.greenCanPass();
	}

	private void updateLocation() {
		if (botInRangeOfTarget()) {
			Direction nextTargetDirection = pathFinder.nextDirection();
			if (nextTargetDirection == Direction.NONE) {
				resetPathfinder();
				nextTargetDirection = pathFinder.nextDirection();
			}
			nextTargetLocation = addDirection(
				nextTargetLocation,
				nextTargetDirection.getOffset());
		}
	}

	private void resetPathfinder() {
		target = null;
		checkTargetChanged();
	}

	private void updateVelocity() {
		Position currentPosition = playerState.getPhysicsBody().getCurrentPosition();
		Position targetPosition = MapUtil.locationToPosition(map, nextTargetLocation);
		Velocity newVelocity = PhysicsUtil.calculateVelocityAngle(
			currentPosition, targetPosition, playerState.getSpeed());
		playerState.getPhysicsBody().setCurrentVelocity(newVelocity);
	}

	private boolean botInRangeOfTarget() {
		Position currentPosition = playerState.getPhysicsBody().getCurrentPosition();
		Position targetPosition = MapUtil.locationToPosition(map, nextTargetLocation);
		return MathUtil.getDistanceBetweenPositions(currentPosition, targetPosition) <= 1f;
	}

	private void updatePositionIfOutdated() {
		if (System.currentTimeMillis() - lastLocationUpdate >= 250) {
			playerState.getPhysicsBody().setCurrentPosition(
				playerState.getPhysicsBody().getCurrentPosition());
			lastLocationUpdate = System.currentTimeMillis();
		}
	}
	
	private Location addDirection(Location location, Location location2) {
		int xComponent = location.getX() + location2.getX();
		int yComponent = location.getY() + location2.getY();
		return new LocationImp(xComponent, yComponent);
	}

	private static enum Target {
		SPIRIT, GOAL
	}
	
}
