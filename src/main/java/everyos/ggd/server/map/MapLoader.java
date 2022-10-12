package everyos.ggd.server.map;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

public final class MapLoader {

	private MapLoader() {}
	
	public static MatchMap loadFromString(String string) {
		int[][] details = stringToMultiArray(string);
		return new ArrayMap(details);
	}

	public static MatchMap loadFromResourceFile(String resourceName) {
		InputStream stream = ClassLoader
			.getSystemClassLoader()
			.getResourceAsStream(resourceName);
		
		try {
			String mapString = new String(stream.readAllBytes());
			return loadFromString(mapString);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static MatchMap loadFromResourceByName(String name) {
		return loadFromResourceFile("maps/" + name + ".map");
	}
	
	private static int[][] stringToMultiArray(String string) {
		return Stream
			.of(string.split("\r?\n"))
			.map(s -> stringToArray(s))
			.toArray(i -> new int[i][]);
	}

	private static int[] stringToArray(String string) {
		return Stream
			.of(string.split(""))
			.mapToInt(Integer::valueOf)
			.toArray();
	};
	
}
