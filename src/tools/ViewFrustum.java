package tools;

import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayHandler;
import entities.Camera;

public class ViewFrustum {
	
	//Vectors
	private Vector3f farTopLeft;
	private Vector3f farTopRight;
	private Vector3f farBottomLeft;
	private Vector3f farBottomRight;
	private Vector3f nearTopLeft;
	private Vector3f nearTopRight;
	private Vector3f nearBottomLeft;
	private Vector3f nearBottomRight;
	private Vector3f camPos;
	private Vector3f farCenter;
	private Vector3f nearCenter;
	private Vector3f up;
	private Vector3f right;
	
	//Floats
	private float farDist = 3500f;
	private float nearDist = 0.1f;
	private float FOV = 90f;
	private float nearHeight;
	private float nearWidth;
	private float farHeight;
	private float farWidth;
	
	//Objects
	private Camera camera;
	private MousePicker mousePicker;
	
	public ViewFrustum(Camera camera) {
		
		this.camera = camera;
		this.mousePicker = camera.getMousePicker();
		updatePositions();
		
	}
	
	public void genFarCenter() {
		Vector3f vec1 = this.mousePicker.getCurrentRay();
		vec1.scale(farDist);
		this.farCenter = Vector3f.add(camPos, vec1, null);
	}
	
	public void genFarTopLeft() {
		Vector3f vec1 = Vector3f.add(this.farCenter, (Vector3f) this.up.scale(farHeight / 2f), this.farTopLeft);
		Vector3f.sub(vec1, (Vector3f) right.scale(this.farWidth / 2f), vec1);
		this.farTopLeft = vec1;
	}
	
	public void genFarTopRight() {
		Vector3f vec1 = Vector3f.add(this.farCenter, (Vector3f) this.up.scale(farHeight / 2f), this.farTopLeft);
		Vector3f.add(vec1, (Vector3f) right.scale(this.farWidth / 2f), vec1);
		this.farTopRight = vec1;
	}
	
	public void genFarBottomLeft() {
		Vector3f vec1 = Vector3f.sub(this.farCenter, (Vector3f) this.up.scale(farHeight / 2f), this.farTopLeft);
		Vector3f.sub(vec1, (Vector3f) right.scale(this.farWidth / 2f), vec1);
		this.farBottomLeft = vec1;
	}
	
	public void genFarBottomRight() {
		Vector3f vec1 = Vector3f.sub(this.farCenter, (Vector3f) this.up.scale(farHeight / 2f), this.farTopLeft);
		Vector3f.add(vec1, (Vector3f) right.scale(this.farWidth / 2f), vec1);
		this.farBottomRight = vec1;
	}
	////////////////////////////////////////
	////////////////////////////////////////
	////////////////////////////////////////
	public void genNearCenter() {
		Vector3f vec1 = this.mousePicker.getCurrentRay();
		vec1.scale(nearDist);
		this.nearCenter = Vector3f.add(camPos, vec1, null);
	}
	
	public void genNearTopLeft() {
		Vector3f vec1 = Vector3f.add(this.nearCenter, (Vector3f) this.up.scale(nearHeight / 2f), this.nearTopLeft);
		Vector3f.sub(vec1, (Vector3f) right.scale(this.nearWidth / 2f), vec1);
		this.nearTopLeft = vec1;
	}
	
	public void genNearTopRight() {
		Vector3f vec1 = Vector3f.add(this.nearCenter, (Vector3f) this.up.scale(nearHeight / 2f), this.nearTopLeft);
		Vector3f.add(vec1, (Vector3f) right.scale(this.nearWidth / 2f), vec1);
		this.nearTopRight = vec1;
	}
	
	public void genNearBottomLeft() {
		Vector3f vec1 = Vector3f.sub(this.nearCenter, (Vector3f) this.up.scale(nearHeight / 2f), this.nearTopLeft);
		Vector3f.sub(vec1, (Vector3f) right.scale(this.nearWidth / 2f), vec1);
		this.nearBottomLeft = vec1;
	}
	
	public void genNearBottomRight() {
		Vector3f vec1 = Vector3f.sub(this.nearCenter, (Vector3f) this.up.scale(nearHeight / 2f), this.nearTopLeft);
		Vector3f.add(vec1, (Vector3f) right.scale(this.nearWidth / 2f), vec1);
		this.nearBottomRight = vec1;
	}
	
	public void updatePositions() {
		
		nearHeight = (float) (2 * Math.tan(Math.toRadians(this.FOV) / 2) * this.nearDist);
		nearWidth = this.nearHeight * DisplayHandler.getAspectRatio();
		farHeight = (float) (2 * Math.tan(Math.toRadians(this.FOV) / 2) * this.farDist);
		farWidth = this.farHeight * DisplayHandler.getAspectRatio();
		
		this.camPos = camera.getPosition();
		
		genFarCenter();
		genNearCenter();
		
		this.up = this.mousePicker.getCurrentRay().normalise(this.up);
		this.right = Vector3f.cross(this.up, this.mousePicker.getCurrentRay(), this.up);
		
		genNearTopLeft();
		genNearTopRight();
		genNearBottomLeft();
		genNearBottomRight();
		
		genNearTopLeft();
		genNearTopRight();
		genNearBottomLeft();
		genNearBottomRight();
		
	}

	public Vector3f getFarTopLeft() {
		return farTopLeft;
	}

	public Vector3f getFarTopRight() {
		return farTopRight;
	}

	public Vector3f getFarBottomLeft() {
		return farBottomLeft;
	}

	public Vector3f getFarBottomRight() {
		return farBottomRight;
	}

	public Vector3f getNearTopLeft() {
		return nearTopLeft;
	}

	public Vector3f getNearTopRight() {
		return nearTopRight;
	}

	public Vector3f getNearBottomLeft() {
		return nearBottomLeft;
	}

	public Vector3f getNearBottomRight() {
		return nearBottomRight;
	}

	public Vector3f getFarCenter() {
		return farCenter;
	}

	public Vector3f getNearCenter() {
		return nearCenter;
	}

	public float getFarDist() {
		return farDist;
	}

	public float getNearDist() {
		return nearDist;
	}

	public float getNearHeight() {
		return nearHeight;
	}

	public float getNearWidth() {
		return nearWidth;
	}

	public float getFarHeight() {
		return farHeight;
	}

	public float getFarWidth() {
		return farWidth;
	}
	
	
	
}