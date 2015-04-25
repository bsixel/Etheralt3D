package engineTester;

import entities.Camera;
import entities.Player;
import gui.GUIRenderer;

import org.lwjgl.opengl.Display;

import renderEngine.DisplayHandler;
import renderEngine.FinalRenderer;
import renderEngine.Loader;
import tools.ViewFrustum;
import worldPopulator.EntityPopulator;
import worldPopulator.GUIPopulator;
import worldPopulator.TerrainPopulator;

public class MainGameLoop {
	
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		
		DisplayHandler.createDisplay();
		
		Loader loader = new Loader();
		WorldControl worldControl = new WorldControl();
		FinalRenderer renderer = new FinalRenderer(loader, worldControl);
		GUIRenderer guiRenderer = new GUIRenderer(loader);
		
		//Populators
		TerrainPopulator terrainPopulator = new TerrainPopulator(loader);
		EntityPopulator entityPopulator = new EntityPopulator(terrainPopulator.getTerrains(), worldControl, loader, renderer);
		GUIPopulator guiPopulator = new GUIPopulator(loader);
		
		terrainPopulator.initTerrain();
		entityPopulator.initEntities();
		guiPopulator.initGUIs();
		ViewFrustum viewFrustum = new ViewFrustum(entityPopulator.activeCamera());
		
		//On Tick Things
		while (!Display.isCloseRequested()) {
			
			worldControl.updateWorldControl();
			
			entityPopulator.updatePopulation();
			
			if (entityPopulator.getDragon().getCollisionShell().checkWithinWidePassBox(entityPopulator.activePlayer().getPosition())) {
				guiPopulator.getEmblem().setEnabled(true);
				
			} else if (!entityPopulator.getDragon().getCollisionShell().checkWithinWidePassBox(entityPopulator.activePlayer().getPosition())) {
				guiPopulator.getEmblem().setEnabled(false);
			}
			
			renderer.render(entityPopulator.getLights(), Camera.currentCamera(entityPopulator.getCameras()));
			
			guiRenderer.render(guiPopulator.getGUIs(), Player.activePlayer(entityPopulator.getPlayers()));
			
			renderer.processAll(entityPopulator.getEntities(), entityPopulator.getPlayers(), entityPopulator.getTerrains());
			
			DisplayHandler.updateDisplay(worldControl);
			
		}
		
		guiRenderer.cleaner();
		renderer.cleaner();
		loader.cleaner();
		
		DisplayHandler.closeDisplay();
		
	}
	
}