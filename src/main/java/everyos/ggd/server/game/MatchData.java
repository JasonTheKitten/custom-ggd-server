package everyos.ggd.server.game;

import java.util.List;

import everyos.ggd.server.game.vanilla.state.entity.Entity;
import everyos.ggd.server.map.MatchMap;

public record MatchData(MatchMap map, List<Entity> entities) {

}
