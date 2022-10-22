package everyos.ggd.server.message.imp;

import everyos.ggd.server.message.MatchFinishedMessage;
import everyos.ggd.server.message.Message;

public class MatchFinishedMessageImp implements MatchFinishedMessage {
	
	private final int greenTeamScore;
	private final int purpleTeamScore;
	private final PlayerAward[] awards;

	public MatchFinishedMessageImp(int greenTeamScore, int purpleTeamScore, PlayerAward[] awards) {
		this.greenTeamScore = greenTeamScore;
		this.purpleTeamScore = purpleTeamScore;
		this.awards = awards;
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
	public PlayerAward[] getAwards() {
		return awards;
	}
	
	public static class PlayerAwardImp implements PlayerAward {
		
		private final int entityId;
		private final Award award;

		public PlayerAwardImp(int entityId, Award award) {
			this.entityId = entityId;
			this.award = award;
		}

		@Override
		public int getEntityId() {
			return this.entityId;
		}

		@Override
		public Award getAward() {
			return this.award;
		}
		
	}

}
