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

	private Position position = new PositionImp(0, 0);
	private Velocity velocity = new VelocityImp(0, 0);
	private ChangeLevel changeLevel = ChangeLevel.NONE;
	
	@Override
	public Position getCurrentPosition() {
		return position;
	}

	@Override
	public void setCurrentPosition(Position position) {
		this.position = position;
		changeLevel = ChangeLevel.TELEPORT;
	}

	@Override
	public Velocity getCurrentVelocity() {
		return this.velocity;
	}

	@Override
	public void setCurrentVelocity(Velocity velocity) {
		this.velocity = velocity;
		changeLevel = ChangeLevel.MOVE;
	}
	
	public List<Message> getQueuedMessages(int entityId) {
		switch (changeLevel) {
		case MOVE:
			return List.of(new EntityMoveMessageImp(entityId, velocity, true));
		case TELEPORT:
			return List.of(new EntityTeleportMessageImp(entityId, position, velocity, true));
		default:
			return List.of();
		}
	}
	
	private enum ChangeLevel {
		NONE, MOVE, TELEPORT
	}

}
