package everyos.ggd.server.server;

import java.util.function.Consumer;

import everyos.ggd.server.common.TickTimer;
import everyos.ggd.server.event.Event;
import everyos.ggd.server.server.state.SocketState;

public class Client {

	private final Consumer<Event> out;
	private final TickTimer tickTimer;
	
	private SocketState socketState;
	private Runnable stateRunnerCancelFunc;
	
	public Client(Consumer<Event> out, TickTimer tickTimer) {
		this.out = out;
		this.tickTimer = tickTimer;
	}

	public void setState(SocketState socketState) {
		this.socketState = socketState;
	}

	public void start() {
		startStateRunner();
	}
	
	public void onEvent(Event event) {
		socketState.handleEvent(event, out);
	}

	public void stop() {
		stateRunnerCancelFunc.run();
	}
	
	private void startStateRunner() {
		stateRunnerCancelFunc = tickTimer.addTickTask(() -> {
			socketState.ping(oevent -> out.accept(oevent));
		});
	}

}
