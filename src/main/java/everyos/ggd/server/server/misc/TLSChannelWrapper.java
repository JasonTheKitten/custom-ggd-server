package everyos.ggd.server.server.misc;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

import javax.net.ssl.SSLEngine;

import org.java_websocket.interfaces.ISSLChannel;

import tlschannel.NeedsReadException;
import tlschannel.ServerTlsChannel;

public class TLSChannelWrapper implements ByteChannel, ISSLChannel {

	private ServerTlsChannel internalChannel;

	public TLSChannelWrapper(ServerTlsChannel internalChannel) {
		this.internalChannel = internalChannel;
	}

	@Override
	public int read(ByteBuffer dst) throws IOException {
		try {
			return internalChannel.read(dst);
		} catch (NeedsReadException e) {
			return 0;
		}
	}

	@Override
	public boolean isOpen() {
		return internalChannel.isOpen();
	}

	@Override
	public void close() throws IOException {
		internalChannel.close();
	}

	@Override
	public int write(ByteBuffer src) throws IOException {
		return internalChannel.write(src);
	}

	@Override
	public SSLEngine getSSLEngine() {
		return internalChannel.getSslEngine();
	}

}
