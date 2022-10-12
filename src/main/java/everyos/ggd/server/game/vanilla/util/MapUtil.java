package everyos.ggd.server.game.vanilla.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import everyos.ggd.server.map.MatchMap;
import everyos.ggd.server.map.Tile;
import everyos.ggd.server.physics.Location;
import everyos.ggd.server.physics.Position;
import everyos.ggd.server.physics.imp.LocationImp;
import everyos.ggd.server.physics.imp.PositionImp;

public final class MapUtil {

	private MapUtil() {}
	
	public static Location[] getGreenBaseTileLocations(MatchMap map) {
		return getFilteredTileLocations(map, MapUtil::isGreenBaseTile);
	}
	
	public static Location[] getPurpleBaseTileLocations(MatchMap map) {
		return getFilteredTileLocations(map, MapUtil::isPurpleBaseTile);
	}
	
	public static Location[] getSpiritFlameTileLocations(MatchMap map) {
		return getFilteredTileLocations(map, MapUtil::isSpiritFlameTile);
	}
	
	public static Position locationToPosition(MatchMap map, Location location) {
		float xComponent = (location.getX() - map.getWidth() / 2f) * 2;
		float yComponent = (location.getY() - map.getHeight() / 2f) * 2;
		
		return new PositionImp(xComponent, yComponent);
	}
	
	private static Location[] getFilteredTileLocations(MatchMap map, Function<Tile, Boolean> filter) {
		List<Location> locations = new ArrayList<>();
		for (int x = 0; x < map.getWidth(); x++) {
			for (int y = 0; y < map.getHeight(); y++) {
				Tile tile = map.getTile(x, y);
				if (filter.apply(tile)) {
					locations.add(new LocationImp(x, y));
				}
			}
		}
		
		return locations.toArray(new Location[locations.size()]);
	}

	private static boolean isGreenBaseTile(Tile tile) {
		return
			tile.greenCanPass() &&
			!tile.purpleCanPass();
	}
	
	private static boolean isPurpleBaseTile(Tile tile) {
		return
			!tile.greenCanPass() &&
			tile.purpleCanPass();
	}
	
	private static boolean isSpiritFlameTile(Tile tile) {
		return tile.hasSpiritFlame();
	}
	
}
