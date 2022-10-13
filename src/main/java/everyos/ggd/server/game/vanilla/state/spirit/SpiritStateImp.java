package everyos.ggd.server.game.vanilla.state.spirit;

import java.util.ArrayList;
import java.util.List;

import everyos.ggd.server.game.vanilla.state.game.play.MessagingPhysicsBody;
import everyos.ggd.server.message.Message;
import everyos.ggd.server.message.SpiritStateUpdate.SpiritTeam;
import everyos.ggd.server.message.imp.SpiritInitMessageImp;
import everyos.ggd.server.message.imp.SpiritStateUpdateImp;
import everyos.ggd.server.message.imp.SpiritStateUpdateMessageImp;
import everyos.ggd.server.physics.PhysicsBody;
import everyos.ggd.server.physics.Position;
import everyos.ggd.server.physics.imp.VelocityImp;

public class SpiritStateImp implements SpiritState {

	private final int entityId;
	private final Position initialPosition;
	private final MessagingPhysicsBody physicsBody = new MessagingPhysicsBody();
	
	private boolean needsInit = true;
	private boolean needsUpdate;
	
	private int ownerEntityId;
	private SpiritTeam team;

	public SpiritStateImp(int entityId, Position initialPosition) {
		this.entityId = entityId;
		this.initialPosition = initialPosition;
		reset();
	}
	
	@Override
	public int getEntityId() {
		return this.entityId;
	}

	@Override
	public int getOwnerEntityId() {
		return ownerEntityId;
	}
	
	@Override
	public void setOwnerEntityId(int id) {
		this.ownerEntityId = id;
	}
	
	@Override
	public Position getInitialPosition() {
		return this.initialPosition;
	}

	@Override
	public PhysicsBody getPhysicsBody() {
		return this.physicsBody;
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
	public List<Message> getQueuedMessages() {
		List<Message> queuedMessages = new ArrayList<>();
		
		if (needsInit) {
			needsInit = false;
			needsUpdate = false;
			queuedMessages.add(createSpiritInitMessage());
		} else {
			if (needsUpdate) {
				needsUpdate = false;
				queuedMessages.add(createSpiritUpdateMessage());
			}
			queuedMessages.addAll(physicsBody.getQueuedMessages(entityId));
		}
		
		return queuedMessages;
	}

	private Message createSpiritInitMessage() {
		return new SpiritInitMessageImp(
			entityId,
			physicsBody.getCurrentPosition(),
			new SpiritStateUpdateImp(entityId, team));
	}

	private Message createSpiritUpdateMessage() {
		return new SpiritStateUpdateMessageImp(new SpiritStateUpdateImp(entityId, team));
	}

	@Override
	public void reset() {
		physicsBody.reset();
		if (initialPosition != null) {
			physicsBody.setCurrentPosition(initialPosition);
		}
		physicsBody.setCurrentVelocity(new VelocityImp(0, 0));
		ownerEntityId = -1;
		team = SpiritTeam.NO_TEAM;
		needsUpdate = true;
	}

}
