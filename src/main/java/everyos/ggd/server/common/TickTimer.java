package everyos.ggd.server.common;

public interface TickTimer {

	Runnable addTickTask(Runnable runnable);
	
}
