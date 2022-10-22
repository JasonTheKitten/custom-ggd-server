package everyos.ggd.server.server.state;

import java.util.function.Consumer;

import everyos.ggd.server.event.Event;

public interface SocketState {

	void handleEvent(Event event, Consumer<Event> out);

	void ping(Consumer<Event> out);
	
	void onDisconnect();
	
}
