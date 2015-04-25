package engineTester;

import renderEngine.DisplayHandler;

public class WorldControl {
	
	//Booleans
	private boolean paused;
	private boolean pausedTime;
	private boolean timerStarted = false;
	
	private float time = 8000;
	
	public WorldControl() {
		
	}
	
	public void updateWorldControl() {
		if (!this.getPaused() && !this.getPausedTime()) {
			this.time += DisplayHandler.getFrameTimeSeconds() * 10;
			this.time %= 24000;
		}
	}
	
	public void setPaused(boolean paused) {
		this.paused = paused;
	}
	
	public void togglePaused() {
		this.paused = !this.paused;
	}
	
	public void setPausedTime(boolean pausedTime) {
		this.pausedTime = pausedTime;
	}
	
	public void togglePausedTime() {
		this.pausedTime = !this.pausedTime;
	}
	
	public void setTimerStarted(boolean timerStarted) {
		this.timerStarted = timerStarted;
	}
	
	public void toggleTimerStarted() {
		this.timerStarted = !this.timerStarted;
	}
	
	public boolean getPaused() {
		return this.paused;
	}
	
	public boolean getPausedTime() {
		return this.pausedTime;
	}
	
	public boolean getTimerStarted() {
		return this.timerStarted;
	}
	
	public void setTime(float time) {
		this.time = time;
	}
	
	public void addTime(float time) {
		this.time += time;
	}
	
	public float getTime() {
		return this.time;
	}
	
}