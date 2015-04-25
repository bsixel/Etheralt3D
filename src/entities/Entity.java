package entities;

import models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayHandler;
import terrainGens.Terrain;
import tools.CollisionChecker;
import tools.Maths;

public class Entity {
	
	//Objects
	private TexturedModel model;
	private CollisionChecker collisionShell;
	private Terrain currentTerrain;
	
	//Vectors
	private Vector3f position;
	private Vector3f velocity = new Vector3f();
	
	//Numbers
	private float rotX, rotY, rotZ;
	private float scale;
	private int textureIndex = 0;
	private float mass;
	
	//Booleans
	private boolean canClip;
	private boolean isAirborne;
	private boolean isHovering;
	
	public Entity(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale, boolean canClip, boolean doesHover, float mass) {

		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		this.canClip = canClip;
		this.collisionShell = new CollisionChecker();
		this.mass = mass;
		
	}
	
	public Entity(TexturedModel model, int index, Vector3f position, float rotX,
			float rotY, float rotZ, float scale, boolean canClip, boolean doesHover, float mass) {

		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		this.textureIndex = index;
		this.canClip = canClip;
		this.isHovering = doesHover;
		this.mass = mass;
		
	}
	
	public float getHeight() {
		return this.getCollisionShell().getEntityHeight();
	}
	
	private float terrainHeight(Terrain terrain) {
		
		if (terrain == null) {
			return 0;
		} else return terrain.getHeightOfTerrain(this.getPosition().x, this.getPosition().z);

	}
	
	public void gravitize(Terrain currentTerrain) {
		
		this.currentTerrain = currentTerrain;
		float terrainHeight = terrainHeight(this.currentTerrain);
		
		if (!this.isHovering && this.isAirborne) {
			this.getVelocity().y += Maths.gravity * DisplayHandler.tick();
		}

		if (this.getPosition().y < terrainHeight) {
			this.getVelocity().setY(0);
			this.getPosition().y = terrainHeight;
			this.isAirborne = false;
		}

	}
	
	public CollisionChecker getCollisionShell() {
		return this.collisionShell;
	}
	
	public float getTextureXOffset() {
		int column = textureIndex % model.getTexture().getNumberOfRows();
		return (float) column / (float) model.getTexture().getNumberOfRows();
	}
	
	public float getTextureYOffset() {
		int row = textureIndex / model.getTexture().getNumberOfRows();
		return (float) row / (float) model.getTexture().getNumberOfRows();
	}

	public void increasePosition(float dx, float dy, float dz) {
		
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
		
	}

	public void increaseRotation(float dx, float dy, float dz) {
		
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
		
	}
	
	public boolean getCanClip() {
		return canClip;
	}
	
	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		//this.mass /= this.scale;
		this.scale = scale;
		this.mass *= scale;
	}
	
	public boolean canClip() {
		return this.canClip;
	}
	
	public boolean getDoesHover() {
		return this.isHovering;
	}
	public float getMass() {
		return this.mass;
	}
	public void setMass(float mass) {
		this.mass = mass;
	}
	public void addMass(float addedMass) {
		this.mass += addedMass;
	}

	public Vector3f getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector3f velocity) {
		this.velocity = velocity;
	}
	
}
