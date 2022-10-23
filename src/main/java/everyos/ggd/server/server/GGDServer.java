package everyos.ggd.server.server;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Map;
import java.util.Optional;

import javax.net.ssl.SSLContext;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import everyos.ggd.server.common.TickTimer;
import everyos.ggd.server.common.imp.TickTimerImp;
import everyos.ggd.server.event.Event;
import everyos.ggd.server.game.Match;
import everyos.ggd.server.game.vanilla.VanillaMatch;
import everyos.ggd.server.matchmaker.MatchMaker;
import everyos.ggd.server.player.HumanPlayer;
import everyos.ggd.server.server.event.EventDecoder;
import everyos.ggd.server.server.event.EventEncoder;
import everyos.ggd.server.server.event.imp.EventDecoderImp;
import everyos.ggd.server.server.event.imp.EventEncoderImp;
import everyos.ggd.server.server.message.MessageEncoder;
import everyos.ggd.server.server.message.imp.MessageDecoderImp;
import everyos.ggd.server.server.message.imp.MessageEncoderImp;
import everyos.ggd.server.server.state.MatchMakerSocketState;
import everyos.ggd.server.server.state.MatchSocketState;
import everyos.ggd.server.server.state.SocketState;
import everyos.ggd.server.server.tls.TLSChannelWebSocketServerFactory;
import everyos.ggd.server.session.SessionData;
import everyos.ggd.server.session.SessionManager;
import everyos.ggd.server.socket.SocketArray;
import everyos.ggd.server.socket.decoder.SocketDecoder;
import everyos.ggd.server.socket.decoder.imp.SocketDecoderImp;
import everyos.ggd.server.socket.encoder.SocketEncoder;
import everyos.ggd.server.socket.encoder.imp.SocketEncoderImp;

public class GGDServer extends WebSocketServer {
	
	public static final int FRAME_RATE = 60;
	
	//TODO: Error handling
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final Map<WebSocket, Client> clients = new HashMap<>();
	private final TickTimer tickTimer = new TickTimerImp(FRAME_RATE);
	private final SessionManager sessionManager = new SessionManager();
	private final MatchMaker matchMaker = new MatchMaker(sessionManager, id -> new VanillaMatch(id, tickTimer));
	
	private final SocketDecoder decoder = new SocketDecoderImp();
	private final SocketEncoder encoder = new SocketEncoderImp();
	private final EventEncoder eventEncoder = createEventEncoder();
	private final EventDecoder eventDecoder = new EventDecoderImp(new MessageDecoderImp());
	
	public GGDServer(int port, Optional<SSLContext> sslContext) throws UnknownHostException {
	    super(new InetSocketAddress(port));
	    sslContext.ifPresent(context -> setWebSocketFactory(new TLSChannelWebSocketServerFactory(context)));
	}

	@Override
	public void onStart() {
		logger.info("Websocket has started! (Port: " + getPort() + ")");
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		logger.info("Client connnected! (Address: " + conn.getRemoteSocketAddress() + ")");
		clients.put(conn, new Client(event -> conn.send(encodeEvent(event)), tickTimer));
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		String reasonMsg = reason.isEmpty() ? "" : "#" + reason;
		logger.info("Client disconnnected! (Address: " + conn.getRemoteSocketAddress() + ", reason: " + code + reasonMsg + ")");
		clients.get(conn).stop();
	}
	
	@Override
	public void onMessage(WebSocket conn, String message) {
		Client client = clients.get(conn);
		if (message.contains("matchmaker")) {
			client.setState(new MatchMakerSocketState(matchMaker));
		} else {
			logger.info("Connection to existing match requested (Address: " + conn.getRemoteSocketAddress() + ")");
			client.setState(createMatchState(message));
		}
		
		client.start();
	}

	@Override
	public void onMessage(WebSocket conn, ByteBuffer packetBuffer) {
		Event event = decodeEvent(packetBuffer);
		if (event.code() == Event.PING) {
			conn.send(encodeEvent(Event.createPongEvent()));
		}
		clients.get(conn).onEvent(event);
	}
	
	@Override
	public void onError(WebSocket conn, Exception ex) {
		
	}
	
	private SocketState createMatchState(String message) {
		SessionData session = SessionData.fromString(message);
		Match match = sessionManager.getMatch(session.matchId());
		HumanPlayer player = (HumanPlayer) match.getPlayer(session.playerId());
		if (!session.authenticationKey().equals(player.getAuthenticationKey())) {
			throw new RuntimeException("Player authentication failed");
		}
		
		return new MatchSocketState(match, player);
	}
	
	private EventEncoder createEventEncoder() {
		MessageEncoder messageEncoder = new MessageEncoderImp(encoder, decoder);
		
		return new EventEncoderImp(encoder, decoder, messageEncoder);
	}

	private Event decodeEvent(ByteBuffer packetBuffer) {
		byte[] packet = getPacket(packetBuffer);
		logger.trace(HexFormat.of().formatHex(packet));
		SocketArray packetData = decoder.decodeArray(packet, 0, packet.length);
		
		return eventDecoder.decodeEvent(packetData);
	}
	
	private byte[] encodeEvent(Event event) {
		SocketArray packetData = eventEncoder.encodeEvent(event);
		
		return encoder.encodeArray(packetData);
	}
	
	private byte[] getPacket(ByteBuffer packetBuffer) {
		byte[] packet = new byte[packetBuffer.remaining()];
		packetBuffer.get(packet);
		
		return packet;
	}
	
}
