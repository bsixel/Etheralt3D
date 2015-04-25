package worldPopulator;

import java.util.ArrayList;
import java.util.List;

import gui.GUITexture;

import org.lwjgl.util.vector.Vector2f;

import renderEngine.DisplayHandler;
import renderEngine.Loader;

public class GUIPopulator {
	
	//Loaders and Renderers
	private Loader loader;
	
	//Lists
	public List<GUITexture> GUIElements = new ArrayList<GUITexture>();
	
	public GUIPopulator(Loader loader) {
		this.loader = loader;
	}
	
	//GUI Elements
	GUITexture emblem;
	GUITexture cursor;
	
	public void initGUIs() {
		
		emblem = new GUITexture(this.loader.loadTexture("emblem"), new Vector2f(-0.9f, 0.8f), new Vector2f(128 / (float) (DisplayHandler.getRenderWidth()), 128 / (float) (DisplayHandler.getRenderHeight())), true, false);
		cursor = new GUITexture(this.loader.loadTexture("pointer"), new Vector2f(0f, 0f), new Vector2f(16 / (float) (DisplayHandler.getRenderWidth()), 16 / (float) (DisplayHandler.getRenderHeight())), false, true);
		
		GUIElements.add(cursor);
		GUIElements.add(emblem);
		
	}
	
	public GUITexture getEmblem() {
		return this.emblem;
	}
	
	public List<GUITexture> getGUIs() {
		return this.GUIElements;
	}
	
}