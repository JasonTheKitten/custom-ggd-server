package everyos.ggd.server.message;

public interface MatchFinishedMessage extends Message {
	
	int getGreenTeamScore();
	
	int getPurpleTeamScore();
	
	PlayerAward[] getAwards();
	
	public static interface PlayerAward {
		
		int getEntityId();
		
		Award getAward();
		
		public static enum Award {
			MOST_TRANSPARENT,
			MOST_SPIRITED,
			SHYEST,
			MOST_POPULAR,
			SPIRIT_HOARDER,
			POLTERHEIST,
			MOST_GENEROUS,
			BEST_FRIEND
		}
		
	}
	
}
