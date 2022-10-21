package everyos.ggd.server.game.vanilla.state.game.play;

import java.util.function.BiConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import everyos.ggd.server.game.Player;
import everyos.ggd.server.game.vanilla.MatchContext;
import everyos.ggd.server.game.vanilla.state.player.PlayerState;
import everyos.ggd.server.message.EntityMoveMessage;
import everyos.ggd.server.message.EntityTeleportMessage;
import everyos.ggd.server.message.Message;
import everyos.ggd.server.physics.PhysicsBody;
import everyos.ggd.server.physics.Position;

public class PlayerMessageProcessor {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final MatchContext matchContext;
	private final PlayerState[] playerStates;
	private final BiConsumer<Integer, Position> playerPositionUpdateHandler;
	
	
	public PlayerMessageProcessor(MatchContext matchContext, PlayerState[] playerStates, BiConsumer<Integer, Position> playerPositionUpdateHandler) {
		this.matchContext = matchContext;
		this.playerStates = playerStates;
		this.playerPositionUpdateHandler =  playerPositionUpdateHandler;
	}
	
	public void tick() {
		processPlayerMessages();
		processPlayerPositions();
	}

	private void processPlayerMessages() {
		Player[] players = matchContext.getPlayers();
		for (Player player: players) {
			for (Message message: player.getQueuedMessagesFromClient()) {
				processPlayerMessage(player, message);
			}
		}
	}

	private void processPlayerMessage(Player player, Message message) {
		switch (message.getType()) {
		case Message.ENTITY_MOVE:
			processEntityMoveMessage((EntityMoveMessage) message, player.getId());
			break;
		case Message.ENTITY_TELEPORT:
			processEntityTeleportMessage((EntityTeleportMessage) message, player.getId());
			break;
		default:
			logger.warn("Did not process message [type=" + message.getType() + "]");
		}
	}

	private void processEntityMoveMessage(EntityMoveMessage message, int playerId) {
		matchContext.rebroadcast(message, playerId);
		int playerEntityId = message.getEntityId();
		playerStates[playerEntityId]
			.getPhysicsBody()
			.setCurrentVelocity(message.getVelocity());
	}
	
	private void processEntityTeleportMessage(EntityTeleportMessage message, int playerId) {
		matchContext.rebroadcast(message, playerId);
		int playerEntityId = message.getEntityId();
		PhysicsBody playerPhysicsBody = playerStates[playerEntityId].getPhysicsBody();
		playerPhysicsBody.setCurrentPosition(message.getPosition());
		playerPhysicsBody.setCurrentVelocity(message.getVelocity());
	}
	
	void processPlayerPositions() {
		for (PlayerState playerState: playerStates) {
			Position newPosition = playerState.getPhysicsBody().getCurrentPosition();
			handlePlayerPositionUpdate(playerState.getEntityId(), newPosition);
		}
	}

	private void handlePlayerPositionUpdate(int playerEntityId, Position playerPosition) {
		playerPositionUpdateHandler.accept(playerEntityId, playerPosition);
	}
	
}
