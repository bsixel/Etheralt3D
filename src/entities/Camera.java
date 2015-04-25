package entities;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import tools.MousePicker;

public class Camera {

	// Booleans
	boolean paused = false;
	boolean sprinting = false;
	public boolean isFreeCam = false;
	boolean turbo = false;

	// Numbers
	private Vector3f position;
	private float pitch = 0;
	private float yaw = 0;
	private float roll = 0;
	private float halfWidth = Display.getWidth() / 2;
	private float halfHeight = Display.getHeight() / 2;
	float lastYaw, lastPitch = 0;
	private float distance;
	public int currentCamNum = 0;
	float thirdPersonDist = 50;
	float playerCameraAngle = 0;
	
	//Player
	private Player player;

	public Camera(Player player, String cameraName, MousePicker mousePicker) {
		
		this.mousePicker = mousePicker;
		this.player = player;
		this.cameraName = cameraName;
		this.position = new Vector3f();
		this.pitch = player.getRotX();
		this.yaw = player.getRotY();
		this.roll = player.getRotZ();

	}

	// Strings / Objects
	String cameraName;
	String movementMode = "normal";
	Camera currentCamera;
	MousePicker mousePicker;
	
	public static Camera currentCamera(List<Camera> cameras) {
		Camera currentCamera = null;
		
		for (Camera camera:cameras) {
			if (camera.player.activePlayer) {
				currentCamera = camera;
			}
		}
		
		return currentCamera;
		
	}
	
	public MousePicker getMousePicker() {
		return this.mousePicker;
	}
	
	public float maxLook() {

		if (movementMode == "flyCam") {
			return 900000000;
		} else
			return 90;

	}

	public void calcPlayerCamAngle() {
		float angleChange = Mouse.getDX();
		playerCameraAngle -= angleChange;
	}
	
	public float calcHorizontalDist() {
		return (float) (this.player.getThirdPersonDist() * Math.cos(Math.toRadians(this.pitch)));
	}
	
	public float calcVerticalDist() {
		return (float) (this.player.getThirdPersonDist() * Math.sin(Math.toRadians(this.pitch)));
	}
	
	public void followPlayer() {

		if (!this.player.isThirdPerson) {
			
			this.getPosition().x = this.player.getPosition().getX();
			this.getPosition().y = this.player.getPosition().getY() + this.player.getHeight() * this.player.getScale();
			this.getPosition().z = this.player.getPosition().getZ();
			this.yaw = this.player.getYaw() - 180;
			this.pitch = this.player.getPitch();
			this.roll = this.player.getRoll();
			
		}
		
		if (this.player.isThirdPerson) {
			
			this.getPosition().x = (float) (-this.player.getThirdPersonDist() / 2 * sin(Math.toRadians(this.player.getRotY()))) * this.player.getScale() + this.player.getPosition().getX();
			this.getPosition().y = (float) (this.player.getThirdPersonDist() * sin(Math.toRadians(this.pitch))) + this.player.getHeight() + this.player.getPosition().getY();
			this.getPosition().z = (float) (-this.player.getThirdPersonDist() / 2 * cos(Math.toRadians(this.player.getRotY())))  * this.player.getScale() + this.player.getPosition().getZ();
			
			this.yaw = this.player.sAngleVers() - 270;
			this.pitch = this.player.getPitch();
			
		}
		
		if (this.player.getWorldControl().getPaused()) {
			Mouse.setCursorPosition((int) halfWidth, (int) halfHeight);
		}
		
	}
	
	public void updateCameraPos() {
		
			if (!this.player.getWorldControl().getPaused()) {
				followPlayer();
			}
		
	}

	public float getLastYaw() {
		return lastYaw;
	}

	public float getLastPitch() {
		return lastPitch;
	}

	public float getDistance() {
		return distance;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}

	public String getCameraName() {
		return cameraName;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}

}