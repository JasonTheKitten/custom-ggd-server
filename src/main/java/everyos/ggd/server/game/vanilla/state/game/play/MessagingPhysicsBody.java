package everyos.ggd.server.game.vanilla.state.game.play;

import java.util.List;

import everyos.ggd.server.message.Message;
import everyos.ggd.server.message.imp.EntityMoveMessageImp;
import everyos.ggd.server.message.imp.EntityTeleportMessageImp;
import everyos.ggd.server.physics.PhysicsBody;
import everyos.ggd.server.physics.Position;
import everyos.ggd.server.physics.Velocity;
import everyos.ggd.server.physics.imp.PositionImp;
import everyos.ggd.server.physics.imp.VelocityImp;

public class MessagingPhysicsBody implements PhysicsBody {

	private Position position;
	private Velocity velocity;
	private Velocity lastVelocity;
	private PhysicsChangeLevel changeLevel;
	
	public MessagingPhysicsBody() {
		reset();
	}
	
	@Override
	public Position getCurrentPosition() {
		return position;
	}

	@Override
	public void setCurrentPosition(Position position) {
		this.position = position;
		changeLevel = PhysicsChangeLevel.TELEPORT;
	}
	
	@Override
	public void recordCurrentPosition(Position position) {
		this.position = position;
	}

	@Override
	public Velocity getCurrentVelocity() {
		return this.velocity;
	}

	@Override
	public void setCurrentVelocity(Velocity velocity) {
		this.velocity = velocity;
		if (changeLevel != PhysicsChangeLevel.TELEPORT) {
			changeLevel = PhysicsChangeLevel.MOVE;
		}
	}
	
	@Override
	public Velocity getLastVelocity() {
		return this.lastVelocity;
	}

	@Override
	public void storeLastVelocity() {
		this.lastVelocity = this.velocity;
	}
	
	public List<Message> getQueuedMessages(int entityId) {
		PhysicsChangeLevel oldPhysicsChangeLevel = changeLevel;
		changeLevel = PhysicsChangeLevel.NONE;
		switch (oldPhysicsChangeLevel) {
		case MOVE:
			return List.of(new EntityMoveMessageImp(entityId, velocity, true));
		case TELEPORT:
			return List.of(new EntityTeleportMessageImp(entityId, position, velocity, true));
		default:
			return List.of();
		}
	}
	
	public void reset() {
		position = new PositionImp(0, 0);
		velocity = new VelocityImp(0, 0);
		lastVelocity = velocity;
		changeLevel = PhysicsChangeLevel.TELEPORT;
	}

}
