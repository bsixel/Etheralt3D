package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;

import shaders.StaticShader;
import shaders.TerrainShader;
import skybox.SkyboxRenderer;
import terrainGens.Terrain;
import tools.ViewFrustum;
import engineTester.WorldControl;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;

public class FinalRenderer {
	
	public float FOV = 90;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 3500;
	
	private float FOG_RED = 0.544f;
	private float FOG_GREEN = 0.62f;
	private float FOG_BLUE = 0.69f;
	
	//Lists
	//public List<Camera> cameras = new ArrayList<Camera>();
	
	private Matrix4f projectionMatrix;
	
	private StaticShader shader = new StaticShader();
	private EntityRenderer entityRenderer; 
	
	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader();
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	private SkyboxRenderer skyboxRenderer;
	private ViewFrustum viewFrustum;
	
	public FinalRenderer(Loader loader, WorldControl worldControl) {
		
		enableCulling();
		GL11.glEnable(GL13.GL_MULTISAMPLE);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		createProjectionMatrix();
		entityRenderer = new EntityRenderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		this.skyboxRenderer = new SkyboxRenderer(worldControl, loader, projectionMatrix);
		
	}
	
	public ViewFrustum getViewFrustum() {
		return this.viewFrustum;
	}
	
	public SkyboxRenderer getSkyboxRenderer() {
		
		return this.skyboxRenderer;
		
	}
	
	public Matrix4f getProjectionMatrix() {
		
		return this.projectionMatrix;
		
	}
	
	public static void enableCulling() {
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		
	}
	
	public static void disableCulling() {
		
		GL11.glDisable(GL11.GL_CULL_FACE);
		
	}
	
	public void render(List<Light> lights, Camera camera) {
		
		preRender();
		shader.start();
		shader.loadSkyColor(FOG_RED, FOG_GREEN, FOG_BLUE);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		entityRenderer.render(entities);
		shader.stop();
		terrainShader.start();
		terrainShader.loadSkyColor(FOG_RED, FOG_GREEN, FOG_BLUE);
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		this.skyboxRenderer.render(camera, FOG_RED, FOG_GREEN, FOG_BLUE);
		setFogColor();
		terrains.clear();
		
		entities.clear();
		
	}
	
	public void setFogColor() {
		
		if (this.skyboxRenderer.time >= 0 && skyboxRenderer.time < 5000){
			
			FOG_RED = 0.0f;
			FOG_GREEN = 0.0f;
			FOG_BLUE = 0.0f;
			
		} else if (this.skyboxRenderer.time >= 5000 && skyboxRenderer.time < 8000){
			
			if (FOG_RED >= 0.544f) {
				FOG_RED = 0.544f;
			} else {
				FOG_RED += 0.009f;
			}
			
			if (FOG_GREEN >= 0.62f) {
				FOG_GREEN = 0.62f;
			} else {
				FOG_GREEN += 0.01f;
			}
			
			if (FOG_BLUE >= 0.69f) {
				FOG_BLUE = 0.69f;
			} else {
				FOG_BLUE += 0.012f;
			}
			
		} else if (this.skyboxRenderer.time >= 8000 && this.skyboxRenderer.time < 21000){
			
			FOG_RED = 0.544f;
			FOG_GREEN = 0.62f;
			FOG_BLUE = 0.69f;
			
		} else {

			if (FOG_RED <= 0) {
				FOG_RED = 0;
			} else {
				FOG_RED -= 0.009f;
			}
			
			if (FOG_GREEN <= 0) {
				FOG_GREEN = 0;
			} else {
				FOG_GREEN -= 0.01f;
			}
			
			if (FOG_BLUE <= 0) {
				FOG_BLUE = 0;
			} else {
				FOG_BLUE -= 0.012f;
			}

		}
		
	}
	
	public void processTerrain(Terrain terrain) {
		
		terrains.add(terrain);
		
	}
	
	public void processEntity(Entity entity) {
		
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if (batch != null) {
			
			batch.add(entity);
			
		} else {
			
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
			
		}
		
	}
	
	public void processAll(List<Entity> entities, List<Player> players, List<Terrain> terrains) {
		
		for (Terrain terrain:terrains) {
			this.processTerrain(terrain);
		}
		
		for (Player player:players) {
			player.getCollisionShell().updateCollisionShell();
			if (player.isThirdPerson) {
				this.processEntity(player);
			}
		}
		
		for (Entity entity:entities) {
			this.processEntity(entity);
		}
		
	}
	
	public void cleaner() {
		
		shader.cleaner();
		terrainShader.cleaner();
		
	}
	
	public void preRender() {
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClearColor(FOG_RED, FOG_GREEN, FOG_BLUE, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glScissor(2000, 2000, DisplayHandler.getRenderWidth(), DisplayHandler.getRenderHeight());
		GL11.glViewport(0, 0, DisplayHandler.getRenderWidth(), DisplayHandler.getRenderHeight());
		//GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		
	}
	
	private void createProjectionMatrix() {
		
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) (1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio;
		float x_scale = y_scale / aspectRatio;
		float frustrum_length = FAR_PLANE - NEAR_PLANE;
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustrum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustrum_length);
		projectionMatrix.m33 = 0;
		
	}
	
}
