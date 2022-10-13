package everyos.ggd.server.game.vanilla.state.spirit;

import everyos.ggd.server.message.SpiritStateUpdate;
import everyos.ggd.server.message.SpiritStateUpdate.SpiritTeam;
import everyos.ggd.server.message.imp.SpiritStateUpdateImp;
import everyos.ggd.server.physics.Position;

public class SpiritStateImp implements SpiritState {

	private final int entityId;
	private final Position initialPosition;
	
	private boolean needsUpdate = true;
	private SpiritTeam team = SpiritTeam.NO_TEAM;
	private Position currentPosition;

	public SpiritStateImp(int entityId, Position initialPosition) {
		this.entityId = entityId;
		this.initialPosition = initialPosition;
		this.currentPosition = initialPosition;
	}
	
	@Override
	public int getEntityId() {
		return this.entityId;
	}

	@Override
	public int getOwnerEntityId() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public Position getInitialPosition() {
		return this.initialPosition;
	}

	@Override
	public Position getCurrentPosition() {
		return this.currentPosition;
	}
	
	@Override
	public SpiritTeam getTeam() {
		return team;
	}
	
	@Override
	public void setTeam(SpiritTeam team) {
		needsUpdate = true;
		this.team = team;
	}
	
	@Override
	public boolean needsUpdate() {
		return needsUpdate;
	}
	
	@Override
	public SpiritStateUpdate createUpdate() {
		needsUpdate = false;
		return new SpiritStateUpdateImp(entityId, team);
	}

}
