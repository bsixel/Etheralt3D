package entities;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import effects.Effect;

public class Marker {

	//Vectors
	private Vector3f position;

	//Numbers
	private int ID;

	//Booleans
	private boolean isLocked;

	//Strings
	private String name;

	//Lists
	private List<Effect> activeEffects = new ArrayList<Effect>();

	public Marker(Vector3f position, int ID, boolean isLocked, String name) {

		this.position = position;
		this.ID = ID;
		this.isLocked = isLocked;
		this.name = name;

	}
	
	public Marker(Vector3f position) {

		this.position = position;

	}
	
	public Marker(Vector3f position, int ID) {

		this.position = position;
		this.ID = ID;

	}

	public void addEffect(Effect effect) {

		this.activeEffects.add(effect);

	}

	public void removeEffect(Effect effect) {

		this.activeEffects.remove(effect);

	}
	
	public Vector3f getPosition() {
		
		return this.position;
		
	}
	
	public List<Effect> getEffects() {
		
		return this.activeEffects;
		
	}
	
	public String getName() {
		
		return this.name;
		
	}
	
	public int getID() {
		
		return this.ID;
		
	}
	
	public void increasePosition(Vector3f vec) {
		
		Vector3f.add(this.position, vec, this.position);
		
	}
	
	public void setPosition(Vector3f pos) {
		
		this.position = pos;
		
	}
	
	public void setName(String name) {
		
		this.name = name;
		
	}
	
	public void setID(int id) {
		
		this.ID = id;
		
	}

}