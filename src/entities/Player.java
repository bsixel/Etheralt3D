package entities;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

import java.util.List;

import models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayHandler;
import terrainGens.Terrain;
import tools.Maths;
import tools.MousePicker;
import engineTester.WorldControl;

public class Player extends Entity {

	public Player(WorldControl worldControl, TexturedModel model,
			Vector3f position, float rotX, float rotY, float rotZ, float scale, int playerType, List<Player> players, List<Marker> markers, boolean activePlayer, int playerID, float mass) {
		
		super(model, position, rotX, rotY, rotZ, scale, true, false, mass);
		this.markers = markers;
		this.players = players;
		this.playerType = playerType;
		this.activePlayer = activePlayer;
		this.playerID = playerID;
		this.worldControl = worldControl;

	}
	
	// Booleans
	boolean activePlayer;
	public boolean paused = false;
	boolean sprinting = false;
	public boolean isThirdPerson = false;
	public boolean isAirborne = false;
	boolean controlPressed;
	boolean turbo = false;
	
	// Numbers
	private int playerID;
	private int playerType;
	private float pitch = 0;
	private float yaw = 0;
	private float roll = 0;
	private float halfWidth = Display.getWidth() / 2;
	private float halfHeight = Display.getHeight() / 2;
	private float sensitivity = 0.5f;
	private float speed = 30f;
	private int movementModeNumber = 0;
	private float sprintMult = (float) 2.5;
	private int nextMarkerID = 0;
	private float thirdPersonDist = 25;
	private float jumpStrength = 50;
	private float distance;
	private float dx;
	private float dz;
	private float sdx;
	private float sdz;
	
	//Vectors
	
	// Strings / Objects
	private String movementMode = "normal";
	private String[] movementModes = { "normal", "flyCam" };
	private Terrain currentTerrain;
	private WorldControl worldControl;

	//Lists
	private List<Player> players;
	private List<Marker> markers;

	public static Player activePlayer(List<Player> players) {
		Player currentPlayer = null;
		for (Player player:players) {
			if (player.activePlayer) {
				currentPlayer = player;
			}
		}
		return currentPlayer;
	}
	
	public int getPlayerType() {
		return this.playerType;
	}
	
	public float getThirdPersonDist() {
		return this.thirdPersonDist;
	}
	
	public float getPitch() {
		return this.pitch;
	}
	
	public float getYaw() {
		return this.yaw;
	}
	
	public float getRoll() {
		return this.roll;
	}
	
	public WorldControl getWorldControl() {
		return this.worldControl;
	}

	public float maxLook() {
		
		if (this.movementMode == "flyCam" || this.playerType == 4) {
			return 999999999;
		} else
			return 90;
		
	}

	public float tick() {
		return DisplayHandler.getFrameTimeSeconds();
	}
	
	public String getMovementMode() {
		return this.movementMode;
	}
	
	public void jump(Float jumpMult) {
		if (!this.isAirborne && this.getVelocity().y == 0) {
			this.getVelocity().y += this.jumpStrength * jumpMult;
			this.isAirborne = true;
		}
	}

	public float movementSpeed() {
		if (this.turbo) {
			return this.speed * 4.5f;
		} else return this.speed;
	}

	public Player nextPlayer() {
		Player nextPlayer;
		if (this.playerID < players.size()) {
			nextPlayer = players.get(this.playerID);
		} else
			nextPlayer = players.get(0);
		return nextPlayer;
	}

	public void increaseRotation(float pitchChange, float yawChange, float rollChange) {

		this.pitch += pitchChange;
		this.yaw += yawChange;

	}
	
	private float terrainHeight() {
		float terrainHeight;
		if (this.currentTerrain == null) {
			terrainHeight = 0;
		} else terrainHeight = this.currentTerrain.getHeightOfTerrain(this.getPosition().x, this.getPosition().z);
		
		return terrainHeight;
		
	}
	
	public void gravitize() {
		
		if (this.movementMode != "flyCam" && this.isAirborne && (playerType == 1 || playerType == 2 || playerType == 3)) {
			this.getVelocity().y += Maths.gravity * tick();
		}
		
		if (this.getPosition().y + this.getVelocity().y * tick() <= terrainHeight()) {
			this.getVelocity().y = 0f;
			this.getPosition().y = terrainHeight();
			this.isAirborne = false;
		}

	}
	
	public void checkCollision() {
		
		this.gravitize();
		
		super.getPosition().x += this.getVelocity().x;
		super.getPosition().y += this.getVelocity().y * this.tick();
		super.getPosition().z += this.getVelocity().z;
		
		this.getVelocity().setX(0f);
		this.getVelocity().setZ(0f);
		
	}
	
	public void move(Terrain terrain, MousePicker picker) {

		if (!this.worldControl.getPaused()) {
			if (this.getPosition().y - 1 > terrainHeight()) {
				this.isAirborne = true;
			} else if (this.getPosition().y - 1 <= terrainHeight()) {
				this.isAirborne = false;
			}
			this.currentTerrain = terrain;
			this.getCollisionShell().generateCorners(this);
			controlMovement(picker);
			getMouseInput(picker);
			checkCollision();
		}
		Mouse.setGrabbed(!this.worldControl.getPaused());
		getOtherKeys(picker);
	}

	public float angleVers() {
		float a = 0;
		if (this.isThirdPerson == true) {
			a = -(this.getRotY());
		} else if (this.isThirdPerson == false) {
			a = this.yaw;
		}
		return a;
	}

	public float sAngleVers() {
		float a = 0;
		if (this.isThirdPerson == true) {
			a = -(super.getRotY() - 90);
		} else if (this.isThirdPerson == false) {
			a = this.yaw + 90;
		}
		return a;
	}

	public void getOtherKeys(MousePicker picker) {

		while (Keyboard.next()) {
			
			if (Keyboard.isKeyDown(Keyboard.KEY_3)) {
				this.worldControl.toggleTimerStarted();
				//DisplayHandler.updateTimerStart();
				DisplayHandler.updateStartTime();
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				this.worldControl.togglePaused();
				Mouse.setCursorPosition((int) Display.getWidth() / 2, (int) Display.getHeight() / 2);
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_F11)) {
				DisplayHandler.setDisplayMode(Display.getDesktopDisplayMode().getWidth(), Display.getDesktopDisplayMode().getHeight(), true);
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_F12)) {
				DisplayHandler.setDisplayMode(Display.getDesktopDisplayMode().getWidth(), Display.getDesktopDisplayMode().getHeight(), false);
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_H)) {
				System.out.println("X-Angle: " + this.pitch);
				System.out.println("Y-Angle: " + this.yaw);
				System.out.println("Z-Angle: " + this.roll);
				System.out.println("RotY: " + this.getRotY());
				System.out.println("RotX: " + this.getRotX());
				System.out.println("TPD: " + this.thirdPersonDist);
				System.out.println("Turbo: " + turbo);
				System.out.println("Render Width:" + DisplayHandler.getRenderWidth());
				System.out.println("Render Height:" + DisplayHandler.getRenderHeight());
				System.out.println(activePlayer);
				System.out.println(this.playerID);
				System.out.println("X-Pos: " + super.getPosition().x);
				System.out.println("Y-Pos: " + super.getPosition().y);
				System.out.println("Z-Pos: " + super.getPosition().z);
				System.out.println("X-Vel: " + this.getVelocity().x);
				System.out.println("Y-Vel: " + this.getVelocity().y);
				System.out.println("Z-Vel: " + this.getVelocity().z);
				System.out.println("Supposed Model Height: " + this.getHeight());
			}
			if (!this.worldControl.getPaused()) {
				if (Keyboard.isKeyDown(Keyboard.KEY_DECIMAL)) {
					this.worldControl.togglePausedTime();
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_M)) {
					if (this.playerType ==  3) {
						this.movementMode = this.movementModes[this.movementModeNumber];
						if (this.movementModeNumber + 1 >= this.movementModes.length) {
							this.movementModeNumber = 0;
						} else {
							this.movementModeNumber += 1;
						}
						this.getVelocity().y = 0;
					}
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
					if (this.playerType == 2 || this.playerType == 3) {
						this.isThirdPerson = !this.isThirdPerson;
					}
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_ADD)) {
					worldControl.addTime(1000f);
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_SUBTRACT)) {
					worldControl.addTime(-1000f);
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_7)) {
					activePlayer = !activePlayer;
					nextPlayer().activePlayer = true;
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
					super.setPosition(this.markers.get(nextMarkerID).getPosition());
					if (this.nextMarkerID <  this.markers.size() - 1) {
						this.nextMarkerID ++;
					} else {
						this.nextMarkerID = 0;
					}
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
					this.turbo = !this.turbo;
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
					this.setScale(this.getScale() + 0.1f);
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
					this.setScale(this.getScale() - 0.1f);
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_J)) {
					this.setScale(1.5f);
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
					Maths.gravity += 10;
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
					Maths.gravity -= 10;
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_G)) {
					Maths.gravity = -100;
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_T)) {
					this.setPosition(new Vector3f(0, 0, 0));
					this.setVelocity(new Vector3f(0, 0, 0));
				}
			}
		}

	}
	
	public void getMouseInput(MousePicker picker) {

		while (Mouse.next()) {
			if (Mouse.isButtonDown(0)) {
				this.setVelocity(new Vector3f(0, 0, 0));
				super.setPosition(picker.getCurrentTerrainPoint());
			}
		}
	}
	
	public void controlMovement(MousePicker picker) {
		
		this.distance = movementSpeed() * DisplayHandler.getFrameTimeSeconds();
		this.dx = (float) (sin(toRadians(angleVers())) * this.distance);
		this.dz = (float) (cos(toRadians(angleVers())) * this.distance);
		this.sdx = (float) (sin(toRadians(sAngleVers())) * this.distance);
		this.sdz = (float) (cos(toRadians(sAngleVers())) * this.distance);

		if (movementMode == "flyCam") {

			float dy = (float) -(cos(toRadians(pitch + 90)) * this.distance);

			// Ascend or Descend
			if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
				if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
					super.getPosition().y += sprintMult * 2 * this.distance;
				} else {
					super.getPosition().y += this.distance;
				}
			} else if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
				if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
					super.getPosition().y += sprintMult * 2 * -this.distance;
				} else {
					super.getPosition().y += -this.distance;
				}
			} else {
				this.getVelocity().y += 0;
			}
			
			// Front and Back
			if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
				if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
					super.getPosition().z -= sprintMult * 2 * -dz;
					super.getPosition().x -= sprintMult * 2 * dx;
					super.getPosition().y -= sprintMult * 2 * dy;
				} else {
					super.getPosition().z -= -dz;
					super.getPosition().x -= dx;
					super.getPosition().y -= dy;
				}
			} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
				if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
					super.getPosition().z -= sprintMult * 2 * dz;
					super.getPosition().x -= sprintMult * 2 * -dx;
					super.getPosition().y -= sprintMult * 2 * -dy;
				} else {
					super.getPosition().z -= dz;
					super.getPosition().x -= -dx;
					super.getPosition().y -= -dy;
				}
			} else {
				super.getPosition().z -= 0;
				super.getPosition().x -= 0;
				super.getPosition().y -= 0;
			}

		}

		//Jumping
		if (this.movementMode != "flyMode" && (playerType == 1 || playerType == 2 || playerType == 3)) {
			if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
				if (this.turbo) {
					jump(2f);
				} else {
					jump(1f);
				}
			}
		}

		// Front and Back
		if (playerType != 4 && playerType != 5) {
			if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
				if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
					this.getVelocity().z -= sprintMult * -dz;
					this.getVelocity().x -= sprintMult * dx;
				} else {
					this.getVelocity().z -= -dz;
					this.getVelocity().x -= dx;
				}
			} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
				if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
					this.getVelocity().z -= sprintMult * dz;
					this.getVelocity().x -= sprintMult * -dx;
				} else {
					this.getVelocity().z -= dz;
					this.getVelocity().x -= -dx;
				}
			} else {
				this.getVelocity().z -= 0;
				this.getVelocity().x -= 0;
			}
		}

		// Sideways
		if (playerType != 4 && playerType != 5) {
			if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
				this.getVelocity().z -= -sdz;
				this.getVelocity().x -= sdx;
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
				this.getVelocity().z -= sdz;
				this.getVelocity().x -= -sdx;
			}
		}
		
		if (!this.worldControl.getPaused() && playerType != 5) {
			
			if (isThirdPerson == false) {
				super.setRotY(-yaw);
				
				float maxLookUp = maxLook();
				float maxLookDown = -maxLook();
				float mouseDX = (Mouse.getX() - halfWidth) * sensitivity;
				float mouseDY = (Mouse.getY() - halfHeight) * sensitivity;
				if (yaw + mouseDX >= 360) {
					yaw = yaw + mouseDX - 360;
				} else if (yaw + mouseDX < 0) {
					yaw = 360 - yaw + mouseDX;
				} else {
					yaw += mouseDX;
				}
				if (pitch - mouseDY >= maxLookDown
						&& pitch - mouseDY <= maxLookUp) {
					pitch += -mouseDY;
				} else if (pitch - mouseDY < maxLookDown) {
					pitch = maxLookDown;
				} else if (pitch - mouseDY > maxLookUp) {
					pitch = maxLookUp;
				}
				
			}
			
			if (isThirdPerson == true) {
				float mouseDX = (Mouse.getX() - halfWidth) * sensitivity;
				float mouseDY = (Mouse.getY() - halfHeight) * sensitivity;
				if (yaw + mouseDX >= 360) {
					yaw = yaw + mouseDX - 360;
				} else if (yaw + mouseDX < 0) {
					yaw = 360 - yaw + mouseDX;
				} else {
					yaw += mouseDX;
				}
				if (pitch - mouseDY >= -90 && pitch - mouseDY <= 90) {
					pitch += -mouseDY / 2;
				} else if (pitch - mouseDY < -90) {
					pitch = -90;
				} else if (pitch - mouseDY > 90) {
					pitch = 90;
				}
				super.setRotY(-yaw);
			}
			
			Mouse.setCursorPosition((int) halfWidth, (int) halfHeight);
			
		}
		
	}
	
}