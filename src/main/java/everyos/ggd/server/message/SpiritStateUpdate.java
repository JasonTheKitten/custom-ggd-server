package everyos.ggd.server.message;

public interface SpiritStateUpdate {
	
	int getEntityId();
	
	SpiritTeam getTeam();

	public static enum SpiritTeam {
		NO_TEAM, GREEN_TEAM, PURPLE_TEAM, NONE
	}
	
}
