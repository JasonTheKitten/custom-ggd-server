package everyos.ggd.server.message;

public interface Message {
	
	public static final int SERVER_CONNECT = 1;
	public static final int SESSION_DATA_SET = 2;
	public static final int MATCH_INIT = 4;
	public static final int COUNTDOWN_TIMER = 5;
	public static final int ENTITY_INIT = 6;
	public static final int MATCH_UPDATE_OR_FINISH = 8;
	public static final int ENTITY_MOVE = 9;
	public static final int ENTITY_TELEPORT = 11;
	public static final int CLEAR_SESSION_DATA = 15;
	
	public static final int INTERNAL = -1;

	int getType();
	
}
