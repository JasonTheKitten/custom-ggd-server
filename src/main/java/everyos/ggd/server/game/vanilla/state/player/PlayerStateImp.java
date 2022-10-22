package everyos.ggd.server.game.vanilla.state.player;

import java.util.ArrayList;
import java.util.List;

import everyos.ggd.server.game.vanilla.state.game.play.MessagingPhysicsBody;
import everyos.ggd.server.game.vanilla.state.spirit.SpiritState;
import everyos.ggd.server.message.Message;
import everyos.ggd.server.message.PlayerStateUpdate;
import everyos.ggd.server.message.PlayerStateUpdate.Animation;
import everyos.ggd.server.message.PlayerStateUpdate.Emotion;
import everyos.ggd.server.message.PlayerStateUpdate.Upgrade;
import everyos.ggd.server.message.PlayerStateUpdate.UpgradeAnimation;
import everyos.ggd.server.message.imp.PlayerInitMessageImp;
import everyos.ggd.server.message.imp.PlayerStateUpdateBuilder;
import everyos.ggd.server.message.imp.PlayerStateUpdateMessageImp;
import everyos.ggd.server.physics.PhysicsBody;

public class PlayerStateImp implements PlayerState {
	
	private final PlayerStats stats = new PlayerStatsImp();
	private final List<SpiritState> spiritList = new ArrayList<>();
	private final PhysicsBody physicsBody = new MessagingPhysicsBody();
	private final int entityId;
	private final PlayerStateEventListener listener;
	
	private boolean needsUpdate = true;
	private PlayerStateUpdate update;

	private Upgrade upgradeLevel = Upgrade.NONE;
	
	public PlayerStateImp(int entityId, PlayerStateEventListener listener) {
		this.entityId = entityId;
		this.update = createInitialUpdate();
		this.listener = listener;
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
	public Upgrade getUpgradeLevel() {
		return this.upgradeLevel;
	}

	@Override
	public void setUpgradeLevel(Upgrade upgrade) {
		this.upgradeLevel = upgrade;
		needsUpdate = true;
		update = PlayerStateUpdateBuilder.clone(update)
			.setUpgradeAnimation(UpgradeAnimation.ACHIEVED, upgrade)
			.setUpgrade(upgrade)
			.build();
	}
	
	@Override
	public void setUpgradeHint(Upgrade upgrade) {
		needsUpdate = true;
		update = PlayerStateUpdateBuilder.clone(update)
			.setUpgradeAnimation(UpgradeAnimation.RETURN_TO_BASE, upgrade)
			.build();
	}
	
	@Override
	public void setSpeed(float speed) {
		needsUpdate = true;
		update = PlayerStateUpdateBuilder.clone(update)
			.setSpeed(speed)
			.build();
	}
	
	@Override
	public void setLight(int glowRadius) {
		needsUpdate = true;
		update = PlayerStateUpdateBuilder.clone(update)
			.setGlowRadius(glowRadius)
			.build();
	}

	@Override
	public void gain(int amount, SpiritGainReason reason) {
		needsUpdate = true;
		PlayerStateUpdateBuilder updateBuilder = PlayerStateUpdateBuilder.clone(update)
			.setTotalSpiritsCollected(stats.getScore() + spiritList.size());
		switch (reason) {
		case STEAL_SPIRIT:
			stats.incrementStolen(amount);
		case GOAL_RETURN:
			updateBuilder
				.setAnimation(Animation.SPIRITS_COLLECTED, amount)
				.setEmotion(Emotion.HAPPY);
			break;
		default:
			break;
		}
		update = updateBuilder.build();
		
		listener.onGain(this, amount, reason);
	}
	
	@Override
	public void loose(int amount) {
		needsUpdate = true;
		update = PlayerStateUpdateBuilder.clone(update)
			.setTotalSpiritsCollected(stats.getScore() + spiritList.size())
			.setAnimation(Animation.SPIRITS_LOST, -amount)
			.setEmotion(Emotion.SAD)
			.build();
		stats.incrementStolenFrom(amount);
	}

	@Override
	public void indicateMatchFinished() {
		needsUpdate = true;
		update = PlayerStateUpdateBuilder.clone(update)
			.setTotalSpiritsCollected(stats.getScore())
			.setSpeed(0f)
			.build();
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
			.setUpgradeAnimation(UpgradeAnimation.NONE, Upgrade.NONE)
			.build();
		
		return message;
	}

	private PlayerStateUpdate createInitialUpdate() {
		needsUpdate = false;
		return new PlayerStateUpdateBuilder()
			.setEntityId(entityId)
			.setSpeed(15f)
			.setGlowRadius(5)
			.setUpgrade(Upgrade.NONE)
			.setTotalSpiritsCollected(0)
			.setConnected(true)
			.setAnimation(Animation.NONE, 0)
			.setEmotion(Emotion.NONE)
			.setUpgradeAnimation(UpgradeAnimation.NONE, Upgrade.NONE)
			.build();
	}

}
