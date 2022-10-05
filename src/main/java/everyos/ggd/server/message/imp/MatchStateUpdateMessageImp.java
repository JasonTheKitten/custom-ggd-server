package everyos.ggd.server.message.imp;

import everyos.ggd.server.message.MatchStateUpdateMessage;
import everyos.ggd.server.message.Message;

public class MatchStateUpdateMessageImp implements MatchStateUpdateMessage {
	
	private final int greenTeamScore;
	private final int purpleTeamScore;
	private final int timeRemaining;
	
	public MatchStateUpdateMessageImp(int greenTeamScore, int purpleTeamScore, int timeRemaining) {
		this.greenTeamScore = greenTeamScore;
		this.purpleTeamScore = purpleTeamScore;
		this.timeRemaining = timeRemaining;
	}

	@Override
	public int getType() {
		return Message.MATCH_UPDATE_OR_FINISH;
	}

	@Override
	public int getGreenTeamScore() {
		return greenTeamScore;
	}

	@Override
	public int getPurpleTeamScore() {
		return purpleTeamScore;
	}

	@Override
	public int getTimeRemaining() {
		return timeRemaining;
	}

}
