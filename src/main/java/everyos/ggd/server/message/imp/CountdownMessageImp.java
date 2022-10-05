package everyos.ggd.server.message.imp;

import everyos.ggd.server.message.CountdownMessage;
import everyos.ggd.server.message.Message;

public class CountdownMessageImp implements CountdownMessage {

	private final int secondsLeft;

	public CountdownMessageImp(int secondsLeft) {
		this.secondsLeft = secondsLeft;
	}
	
	@Override
	public int getType() {
		return Message.COUNTDOWN_TIMER;
	}

	@Override
	public int getSecondsLeft() {
		return this.secondsLeft;
	}
	
}
