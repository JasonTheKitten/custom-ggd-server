package everyos.ggd.server.server;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.util.Optional;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import everyos.api.getopts.ParserFailedException;
import everyos.ggd.server.server.config.ServerConfig;
import everyos.ggd.server.server.config.ServerConfigParser;

public final class EntryPoint {

	private EntryPoint() {}
	
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
