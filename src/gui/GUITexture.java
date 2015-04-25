package gui;

import org.lwjgl.util.vector.Vector2f;

public class GUITexture {
	
	//Booleans
	boolean renderedThirdPerson;
	boolean isEnabled;
	
	//Numbers
	private int texture;
	private Vector2f position;
	private Vector2f scale;
	
	public GUITexture(int texture, Vector2f position, Vector2f scale, boolean renderedThirdPerson, boolean enabled) {
		
		this.texture = texture;
		this.position = position;
		this.scale = scale;
		this.renderedThirdPerson = renderedThirdPerson;
		this.isEnabled = enabled;
		
	}

	public int getTexture() {
		return texture;
	}

	public Vector2f getPosition() {
		return position;
	}

	public Vector2f getScale() {
		return scale;
	}
	
	public boolean getEnabled() {
		return this.isEnabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.isEnabled = enabled;
	}
	
	public void toggleEnabled() {
		this.isEnabled = !this.isEnabled;
	}
	
}