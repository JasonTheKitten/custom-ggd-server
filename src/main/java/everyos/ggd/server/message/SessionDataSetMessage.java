package everyos.ggd.server.message;

public interface SessionDataSetMessage extends Message {

	int getEntityId();
	
	int getMatchId();
	
	String getServerName();
	
}
