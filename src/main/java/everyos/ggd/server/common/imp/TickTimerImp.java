package everyos.ggd.server.common.imp;

import java.util.Timer;
import java.util.TimerTask;

import everyos.ggd.server.common.TickTimer;

public class TickTimerImp implements TickTimer {
	
	private final int millisBetweenTicks;
	
	public TickTimerImp(int frameRate) {
		this.millisBetweenTicks = 1000/frameRate;
	}

	@Override
	public Runnable addTickTask(Runnable runnable) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					runnable.run();
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}, millisBetweenTicks, millisBetweenTicks);
		
		return () -> timer.cancel();
	}
	
}
