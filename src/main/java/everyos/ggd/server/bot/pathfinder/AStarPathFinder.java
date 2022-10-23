package everyos.ggd.server.bot.pathfinder;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.Function;

import everyos.ggd.server.bot.Direction;
import everyos.ggd.server.bot.PathFinder;
import everyos.ggd.server.physics.Location;
import everyos.ggd.server.physics.util.LocationUtil;

public class AStarPathFinder implements PathFinder {
	
	private final Deque<Direction> path;

	public AStarPathFinder(Function<Location, Boolean> goalChecker, Function<Location, Boolean> wallChecker, Location initialLocation) {
		if (goalChecker.apply(initialLocation)) {
			this.path = new ArrayDeque<>();
		} else {
			this.path = findBestRoute(initialLocation, goalChecker, wallChecker);
		}
	}
	
	@Override
	public Direction nextDirection() {
		if (path.isEmpty()) {
			return Direction.NONE;
		}
		return path.pop();
	}
	
	private Deque<Direction> findBestRoute(
		Location initialLocation,
		Function<Location, Boolean> goalChecker,
		Function<Location, Boolean> wallChecker
	) {
		TileEntry endTileEntry = spreadUntilGoalFound(initialLocation, goalChecker, wallChecker);
		return getFinalPath(endTileEntry);
	}
	
	private TileEntry spreadUntilGoalFound(
		Location initialLocation,
		Function<Location, Boolean> goalChecker,
		Function<Location, Boolean> wallChecker
	) {
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
			TileEntry result = tryNextLocations(triedTiles, triedLocations, goalChecker, wallChecker);
			if (result != null) {
				return result;
			}
		}
	}

	private TileEntry tryNextLocations(
		PriorityQueue<TileEntry> triedTiles,
		List<Location> triedLocations,
		Function<Location, Boolean> goalChecker,
		Function<Location, Boolean> wallChecker
	) {
		TileEntry entry = triedTiles.poll();
		for (Direction direction: Direction.DIRECTIONS) {
			TileEntry tryResult = tryTileLocation(entry, direction, triedLocations, wallChecker);
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
	
	private TileEntry tryTileLocation(
		TileEntry source,
		Direction direction,
		List<Location> triedLocations,
		Function<Location, Boolean> wallChecker
	) {
		Location tileLocation = LocationUtil.addDirection(source.location(), direction);
		if (triedLocations.contains(tileLocation)) {
			return null;
		}
		triedLocations.add(tileLocation);
		if (wallChecker.apply(tileLocation)) {
			return null;
		}
		return new TileEntry(source, tileLocation, direction, source.cost() + 1);
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
