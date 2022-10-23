package everyos.ggd.server.bot.pathfinder;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.Function;

import everyos.ggd.server.bot.Direction;
import everyos.ggd.server.bot.PathFinder;
import everyos.ggd.server.map.MatchMap;
import everyos.ggd.server.map.Tile;
import everyos.ggd.server.physics.Location;
import everyos.ggd.server.physics.util.LocationUtil;

public class AStarPathFinder implements PathFinder {
	
	private final Deque<Direction> path;

	public AStarPathFinder(MatchMap map, Function<Location, Boolean> goalChecker, Location initialLocation) {
		if (goalChecker.apply(initialLocation)) {
			this.path = new ArrayDeque<>();
		} else {
			this.path = findBestRoute(map, initialLocation, goalChecker);
		}
	}
	
	@Override
	public Direction nextDirection() {
		if (path.isEmpty()) {
			return Direction.NONE;
		}
		return path.pop();
	}
	
	private Deque<Direction> findBestRoute(MatchMap map, Location initialLocation, Function<Location, Boolean> goalChecker) {
		TileEntry endTileEntry = spreadUntilGoalFound(map, initialLocation, goalChecker);
		return getFinalPath(endTileEntry);
	}
	
	private TileEntry spreadUntilGoalFound(MatchMap map, Location initialLocation, Function<Location, Boolean> goalChecker) {
		PriorityQueue<TileEntry> triedTiles = new PriorityQueue<>((o1, o2) -> (int) Math.signum(o1.cost() - o2.cost()));
		TileEntry initialTile = new TileEntry(null, initialLocation, null, 0);
		triedTiles.add(initialTile);
		List<Location> triedLocations = new ArrayList<>();
		triedLocations.add(initialLocation);
		while (true) {
			//TODO: This will cause undefined behaviour if no accessable tiles meet the goal
			if (triedTiles.isEmpty()) {
				return initialTile;
			}
			TileEntry result = tryNextLocations(map, triedTiles, triedLocations, goalChecker);
			if (result != null) {
				return result;
			}
		}
	}

	private TileEntry tryNextLocations(
		MatchMap map,
		PriorityQueue<TileEntry> triedTiles,
		List<Location> triedLocations,
		Function<Location, Boolean> goalChecker
	) {
		TileEntry entry = triedTiles.poll();
		for (Direction direction: Direction.DIRECTIONS) {
			TileEntry tryResult = tryTileLocation(map, entry, direction, triedLocations);
			if (tryResult == null) {
				continue;
			}
			if (goalChecker.apply(tryResult.location())) {
				return tryResult;
			}
			triedTiles.add(tryResult);
		}
		
		return null;
	}
	
	private TileEntry tryTileLocation(MatchMap map, TileEntry source, Direction direction, List<Location> triedLocations) {
		Location tileLocation = LocationUtil.addDirection(source.location(), direction);
		if (triedLocations.contains(tileLocation)) {
			return null;
		}
		triedLocations.add(tileLocation);
		if (!tileIsValid(map, tileLocation)) {
			return null;
		}
		return new TileEntry(source, tileLocation, direction, source.cost() + 1);
	}

	private boolean tileIsValid(MatchMap map, Location location) {
		Tile tile = map.getTile(location);
		return tile.purpleCanPass() || tile.greenCanPass(); //TODO
	}
	
	private Deque<Direction> getFinalPath(TileEntry endTileEntry) {
		Deque<Direction> finalPath = new ArrayDeque<>();
		TileEntry currentEntry = endTileEntry;
		while (currentEntry.direction() != null) {
			finalPath.push(currentEntry.direction());
			currentEntry = currentEntry.parent();
		}
		
		return finalPath;
	}
	
	private static record TileEntry(TileEntry parent, Location location, Direction direction, int cost) {}
	
}
