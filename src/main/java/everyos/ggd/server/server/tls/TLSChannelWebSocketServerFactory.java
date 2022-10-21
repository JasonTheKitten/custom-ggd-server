package everyos.ggd.server.server.tls;

import java.io.IOException;
import java.nio.channels.ByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.java_websocket.WebSocketAdapter;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.WebSocketServerFactory;
import org.java_websocket.drafts.Draft;

import tlschannel.ServerTlsChannel;

public class TLSChannelWebSocketServerFactory implements WebSocketServerFactory {
	
	private final SSLContext sslContext;

	public TLSChannelWebSocketServerFactory(SSLContext sslContext) {
		this.sslContext = sslContext;
	}

	@Override
	public ByteChannel wrapChannel(SocketChannel rawChannel, SelectionKey key) throws IOException {
		return new TLSChannelWrapper(ServerTlsChannel
			.newBuilder(rawChannel, sslContext)
			.build());
	}
	
	@Override
	public WebSocketImpl createWebSocket(WebSocketAdapter a, Draft d) {
		return new WebSocketImpl(a, d);
	}

	@Override
	public WebSocketImpl createWebSocket(WebSocketAdapter a, List<Draft> drafts) {
		return new WebSocketImpl(a, drafts);
	}

	@Override
	public void close() {
		
	}

}
