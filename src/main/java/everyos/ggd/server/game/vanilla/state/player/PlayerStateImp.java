package everyos.ggd.server.game.vanilla.state.player;

import java.util.ArrayList;
import java.util.List;

import everyos.ggd.server.game.vanilla.state.game.play.MessagingPhysicsBody;
import everyos.ggd.server.game.vanilla.state.spirit.SpiritState;
import everyos.ggd.server.message.Message;
import everyos.ggd.server.message.PlayerStateUpdate;
import everyos.ggd.server.message.PlayerStateUpdate.Animation;
import everyos.ggd.server.message.PlayerStateUpdate.Emotion;
import everyos.ggd.server.message.imp.PlayerInitMessageImp;
import everyos.ggd.server.message.imp.PlayerStateUpdateBuilder;
import everyos.ggd.server.message.imp.PlayerStateUpdateMessageImp;
import everyos.ggd.server.physics.PhysicsBody;

public class PlayerStateImp implements PlayerState {
	
	private final PlayerStats stats = new PlayerStatsImp();
	private final List<SpiritState> spiritList = new ArrayList<>();
	private final PhysicsBody physicsBody = new MessagingPhysicsBody();
	private final int entityId;
	
	private boolean needsUpdate = true;
	private PlayerStateUpdate update;
	
	public PlayerStateImp(int entityId) {
		this.entityId = entityId;
		this.update = createInitialUpdate();
	}
	
	@Override
	public int getEntityId() {
		return this.entityId;
	}

	@Override
	public PlayerStats getStats() {
		return this.stats;
	}
	
	@Override
	public PhysicsBody getPhysicsBody() {
		return physicsBody;
	}
	
	@Override
	public List<SpiritState> getSpiritList() {
		return this.spiritList;
	}

	@Override
	public void gain(int amount, SpiritGainReason reason) {
		needsUpdate = true;
		PlayerStateUpdateBuilder updateBuilder = PlayerStateUpdateBuilder.clone(update)
			.setNumSpiritsHeld(spiritList.size());
		switch (reason) {
		case STEAL_SPIRIT:
		case GOAL_RETURN:
			updateBuilder
				.setAnimation(Animation.SPIRITS_COLLECTED, amount)
				.setEmotion(Emotion.HAPPY);
			break;
		default:
			break;
		}
		update = updateBuilder.build();
	}
	
	@Override
	public Message createInitMessage(boolean isBot) {
		return new PlayerInitMessageImp(
			physicsBody.getCurrentPosition(),
			isBot,
			update);
	}

	@Override
	public List<Message> getQueuedMessages() {
		if (needsUpdate) {
			needsUpdate = false;
			return List.of(createPlayerStateUpdateMessage());
		}
		
		return List.of();
	}

	private Message createPlayerStateUpdateMessage() {
		Message message = new PlayerStateUpdateMessageImp(update);
		update = PlayerStateUpdateBuilder.clone(update)
			.setAnimation(Animation.NONE, 0)
			.setEmotion(Emotion.NONE)
			.build();
		
		return message;
	}

	private PlayerStateUpdate createInitialUpdate() {
		needsUpdate = false;
		return new PlayerStateUpdateBuilder()
			.setEntityId(entityId)
			.setSpeed(15f)
			.setNumSpiritsHeld(spiritList.size())
			.setConnected(true)
			.setAnimation(Animation.NONE, 0)
			.setEmotion(Emotion.NONE)
			.build();
	}

}
