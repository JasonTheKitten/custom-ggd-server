package everyos.ggd.server.message;

public interface MatchStateUpdateMessage extends Message {
	
	int getGreenTeamScore();
	
	int getPurpleTeamScore();
	
	int getTimeRemaining();
	
}
