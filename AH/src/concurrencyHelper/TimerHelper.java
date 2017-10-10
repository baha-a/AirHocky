package concurrencyHelper;

import java.util.Timer;
import java.util.TimerTask;

public class TimerHelper {

	private int period;
	private Timer timer;
	private TimerTask timerTask;
	private String name;
	private worker workerProcess;

	public TimerHelper(String name, final worker workerProcess, int period)
			throws Exception {
		if (period < 5) {
			throw new Exception(
					"Period is less than 5 MilliSecond make it bigger!!");
		} else {
			this.workerProcess = workerProcess;
			this.period = period;
			this.name = name;
			timer = new Timer();
			timerTask = new TimerTask() {
				@Override
				public void run() {
					workerProcess.Update();
					System.out.println(this.toString()+"is Running!");
				}
			};
			this.timer.scheduleAtFixedRate(timerTask, 10, period);
		}
	}

	@Override
	public String toString() {
		return ("Task Name :" + name + ",Scheduled at Period" + period
				+ "the Object that are updting" + workerProcess.toString());
	}
	
public void cancel(){
		this.timerTask.cancel();
		this.timer.cancel();
	}
}
