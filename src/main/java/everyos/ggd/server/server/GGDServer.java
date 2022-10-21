package everyos.ggd.server.server;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.DefaultSSLWebSocketServerFactory;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import everyos.api.getopts.ParserFailedException;
import everyos.ggd.server.event.Event;
import everyos.ggd.server.game.HumanPlayer;
import everyos.ggd.server.game.Match;
import everyos.ggd.server.matchmaker.MatchMaker;
import everyos.ggd.server.server.event.EventDecoder;
import everyos.ggd.server.server.event.EventEncoder;
import everyos.ggd.server.server.event.imp.EventDecoderImp;
import everyos.ggd.server.server.event.imp.EventEncoderImp;
import everyos.ggd.server.server.message.MessageEncoder;
import everyos.ggd.server.server.message.imp.MessageDecoderImp;
import everyos.ggd.server.server.message.imp.MessageEncoderImp;
import everyos.ggd.server.server.misc.TLSChannelWebSocketServerFactory;
import everyos.ggd.server.server.state.MatchMakerSocketState;
import everyos.ggd.server.server.state.MatchSocketState;
import everyos.ggd.server.server.state.SocketState;
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
	private final Map<WebSocket, SocketState> states = new HashMap<>();
	private final SessionManager sessionManager = new SessionManager();
	private final MatchMaker matchMaker = new MatchMaker(sessionManager);
	
	private final SocketDecoder decoder = new SocketDecoderImp();
	private final SocketEncoder encoder = new SocketEncoderImp();
	private final EventEncoder eventEncoder = createEventEncoder();
	private final EventDecoder eventDecoder = new EventDecoderImp(new MessageDecoderImp());
	
	public static void main(String[] args) {
		try {
			ServerConfig config = ServerConfigParser.parse(args);
			if (config.isVerbose()) {
				System.setProperty("logging.level", config.isVerbose() ? "TRACE" : "INFO");
			}
			Optional<SSLContext> sslContext = config
				.getCSStore()
				.map(csStore -> loadCSStore(csStore, config.getCSStorePassword().get()));
			new GGDServer(config.getPortId(), sslContext).start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (ParserFailedException e) {
			// This should have already been handled at this point
		};
	}
	
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
		startPing(conn);
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		String reasonMsg = reason.isEmpty() ? "" : "#" + reason;
		logger.info("Client disconnnected! (Address: " + conn.getRemoteSocketAddress() + ", reason: " + code + reasonMsg + ")");
	}
	
	@Override
	public void onMessage(WebSocket conn, String message) {
		if (message.contains("matchmaker")) {
			states.put(conn, new MatchMakerSocketState(matchMaker));
		} else {
			logger.info("Connection to existing match requested (Address: " + conn.getRemoteSocketAddress() + ")");
			states.put(conn, createMatchState(message));
		}
	}

	@Override
	public void onMessage(WebSocket conn, ByteBuffer packetBuffer) {
		Event event = decodeEvent(packetBuffer);
		if (event.code() == Event.PING) {
			conn.send(encodeEvent(Event.createPongEvent()));
		}
		states
			.get(conn)
			.handleEvent(event, oevent -> conn.send(encodeEvent(oevent)));
	}
	
	@Override
	public void onError(WebSocket conn, Exception ex) {
		
	}
	
	private EventEncoder createEventEncoder() {
		MessageEncoder messageEncoder = new MessageEncoderImp(encoder, decoder);
		
		return new EventEncoderImp(encoder, decoder, messageEncoder);
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
	
	private void startPing(WebSocket conn) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (conn.isClosed()) {
					timer.cancel();
					return;
				}
				if (states.containsKey(conn)) {
					states
						.get(conn)
						.ping(oevent ->
							conn.send(encodeEvent(oevent)));
				}
			}
		}, 1000/FRAME_RATE, 1000/FRAME_RATE);
	}

	private static SSLContext loadCSStore(String csStore, String password) {
		SSLContext sslContext;
		try {
			sslContext = SSLContext.getInstance("TLS");
			
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			try (InputStream keyStoreInputStream = new FileInputStream(csStore)) {
				keyStore.load(keyStoreInputStream, password.toCharArray());
			}
			
			KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			keyManagerFactory.init(keyStore, password.toCharArray());
			
			TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init(keyStore);
			
			sslContext.init(
				keyManagerFactory.getKeyManagers(),
				trustManagerFactory.getTrustManagers(),
				null);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return sslContext;
	}
	
}
