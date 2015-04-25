package effects;

import java.util.List;

import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;

import engineTester.MainGameLoop;

public class Effect {

	//Numbers
	private float strength;
	private float range;
	private int ID;
	private long duration;
	private long timeRemaining;
	private long sysStartTime;

	//Booleans
	private boolean isActive;
	private boolean timerStarted;

	//Objects

	public Effect(int ID, int duration, float strength, float range) {

		this.ID = ID;
		this.duration = duration;
		this.strength = strength;
		this.range = range;
		this.isActive = true;

	}

	public Effect(int ID, int duration, float strength, float range, boolean isActive) {

		this.ID = ID;
		this.duration = duration;
		this.strength = strength;
		this.range = range;
		this.isActive = isActive;

	}

	public Effect(int ID, float strength, float range, boolean isActive) {

		this.ID = ID;
		this.strength = strength;
		this.range = range;
		this.isActive = isActive;

	}

	public void increaseDuration(int time) {
		this.duration += time;


	}

	public void tickTimer() {

		if (this.timerStarted) {
			this.timeRemaining = (this.duration + this.sysStartTime - getCurrentTime() / 1000);
		}

	}

	public void updateSysStartTime() {
		this.sysStartTime = getCurrentTime() / 1000;
	}

	public void updateTimerStart() {
		if (timeRemaining < duration) {
			duration = timeRemaining;
		}
	}

	private long getCurrentTime() {

		return Sys.getTime()*1000/Sys.getTimerResolution();

	}

}