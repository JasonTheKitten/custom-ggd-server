package everyos.ggd.server.message.imp;

import everyos.ggd.server.message.SpiritStateUpdate;

public class SpiritStateUpdateImp implements SpiritStateUpdate {
	
	private final int entityId;
	private final SpiritTeam team;

	public SpiritStateUpdateImp(int entityId, SpiritTeam team) {
		this.entityId = entityId;
		this.team = team;
	}
	
	@Override
	public int getEntityId() {
		return this.entityId;
	}

	@Override
	public SpiritTeam getTeam() {
		return this.team;
	}

}
