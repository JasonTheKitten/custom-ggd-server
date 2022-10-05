package everyos.ggd.server.event;

public interface InitialStatePushEvent extends Event {

	int getClientId();

	String getClientToken();

}
