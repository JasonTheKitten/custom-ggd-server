package everyos.ggd.server.message;

public interface ServerConnectMessage extends Message {
	
	String getURL();
	
	String getMatchName();
	
}
