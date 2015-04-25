package worldPopulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;

import org.lwjgl.util.vector.Vector3f;

import renderEngine.FinalRenderer;
import renderEngine.Loader;
import terrainGens.Terrain;
import textures.ModelTexture;
import tools.MousePicker;
import engineTester.WorldControl;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Marker;
import entities.Player;

@SuppressWarnings("static-access")
public class EntityPopulator {
	
	public EntityPopulator(List<Terrain> terrainsToRender, WorldControl worldControl, Loader loader, FinalRenderer renderer) {
		this.loader = loader;
		this.worldControl = worldControl;
		this.terrainsToRender = terrainsToRender;
	}
	
	//Booleans
	private boolean initFlora = true;
	
	//Lists (entities, terrains, etc.)
	public List<Entity> entitiesToRender = new ArrayList<Entity>();
	public List<Light> lights = new ArrayList<Light>();
	public List<Player> players = new ArrayList<Player>();
	public List<Camera> cameras = new ArrayList<Camera>();
	public List<Marker> markers = new ArrayList<Marker>();
	public List<Terrain> terrainsToRender;
	
	//Renderers and Loaders
	private Loader loader;
	private WorldControl worldControl;
	
	//Spare Vectors
	Vector3f terrainPoint1;
	
	//Primary Lights Lights (Sun, moon(s?))
	Light sun;
	
	//Derp Lamp
	ModelData derpLampData;
	RawModel derpLampModel;
	ModelTexture derpLampTexture;
	TexturedModel texturedDerpLamp;
			
	//Dragon
	ModelData dragonData;
	RawModel dragonModel;
	TexturedModel texturedDragon;
	ModelTexture dragonTexture;
	
	//Sample Human
	ModelData humanData;
	RawModel humanModel;
	TexturedModel humanTexturedModel;
	
	//Spherical Marker
	ModelData sphereData;
	RawModel sphereModel;
	TexturedModel texturedSphereModel;
	ModelTexture sphereTexture;
			
	//Sub-Terrain Things: Grass n stuff
	ModelData grassData;
	RawModel grassModel;
	TexturedModel grassTexturedModel;

	ModelData fernData;
	ModelTexture fernTextureAtlas;
	RawModel fernModel;
	TexturedModel fernTexturedModel;
	
	ModelData genOak1Data = OBJFileLoader.loadOBJ("genOak1");
	RawModel genOak1Model;
	TexturedModel oakTexturedModel;
	
	//Initialize Spare Entities
	Entity dragon;
	Entity testHuman;

	//Players
	Player activePlayer;
	//1
	Player player1;
	Camera camera1;
	//2
	Player player2;
	Camera camera2;
	//3
	Player player3;
	Camera camera3;

	//Mouse Picker
	MousePicker mousePicker1;

	Random generationRandom = new Random();
	Random lightColorRandom = new Random();

	public void initLights() {
		sun = new Light(new Vector3f(2000, 2000, 3000), new Vector3f(0.01f, 0.01f, 0.01f));
		lights.add(sun);
	}
	
	public void initModels() {
		
		//Derp Lamp
		this.derpLampData = OBJFileLoader.loadOBJ("derpLamp");
		this.derpLampModel = loader.loadToVAO(derpLampData.getVertices(), derpLampData.getTextureCoords(), derpLampData.getNormals(), derpLampData.getIndices(), derpLampData.getVertexList());
		
		//Dragon
		this.dragonData = OBJFileLoader.loadOBJ("dragon");
		this.dragonModel = loader.loadToVAO(dragonData.getVertices(), dragonData.getTextureCoords(), dragonData.getNormals(), dragonData.getIndices(), dragonData.getVertexList());
		
		//Sample Human
		this.humanData = OBJFileLoader.loadOBJ("human1");
		this.humanModel = loader.loadToVAO(humanData.getVertices(), humanData.getTextureCoords(), humanData.getNormals(), humanData.getIndices(), humanData.getVertexList());
		
		//Spherical Marker
		this.sphereData = OBJFileLoader.loadOBJ("sphere");
		this.sphereModel = loader.loadToVAO(sphereData.getVertices(), sphereData.getTextureCoords(), sphereData.getNormals(), sphereData.getIndices());
		
		if (initFlora) {

			//Grass
			this.grassData = OBJFileLoader.loadOBJ("grassModel");
			this.grassModel = loader.loadToVAO(grassData.getVertices(), grassData.getTextureCoords(), grassData.getNormals(), grassData.getIndices());

			//Fern
			this.fernData = OBJFileLoader.loadOBJ("fern");
			this.fernModel = loader.loadToVAO(fernData.getVertices(), fernData.getTextureCoords(), fernData.getNormals(), fernData.getIndices());

			//Sample Oak
			this.genOak1Data = OBJFileLoader.loadOBJ("genOak1");
			this.genOak1Model = loader.loadToVAO(genOak1Data.getVertices(), genOak1Data.getTextureCoords(), genOak1Data.getNormals(), genOak1Data.getIndices(), genOak1Data.getVertexList());

		}
		
	}
	
	public void initTextures() {
		
		//Derp Lamp
		this.derpLampTexture = new ModelTexture(loader.loadTexture("derpLamp"));
		this.texturedDerpLamp = new TexturedModel(derpLampModel, derpLampTexture);
		this.derpLampTexture.setReflectivity(1);
		this.derpLampTexture.setShineDamper(7);
		
		//Dragon
		this.texturedDragon = new TexturedModel(dragonModel, new ModelTexture(loader.loadTexture("BlueThing")));
		this.dragonTexture = texturedDragon.getTexture();
		this.dragonTexture.setShineDamper(10);
		this.dragonTexture.setReflectivity(1);
		
		//Human
		this.humanTexturedModel = new TexturedModel(humanModel, new ModelTexture(loader.loadTexture("human1Texture")));
		this.humanTexturedModel.getTexture().setHasTransparency(true);
		this.humanTexturedModel.getTexture().setUseFakeLighting(true);
		
		//Sphere
		this.texturedSphereModel = new TexturedModel(sphereModel, new ModelTexture(loader.loadTexture("red")));
		this.sphereTexture = texturedSphereModel.getTexture();
		this.sphereTexture.setShineDamper(10);
		this.sphereTexture.setReflectivity(1);
		
		if (initFlora) {
			//Grass
			this.grassTexturedModel = new TexturedModel(grassModel, new ModelTexture(loader.loadTexture("tallGrass")));
			this.grassTexturedModel.getTexture().setHasTransparency(true);
			this.grassTexturedModel.getTexture().setUseFakeLighting(true);
			
			//Fern
			this.fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
			this.fernTexturedModel = new TexturedModel(fernModel, fernTextureAtlas);
			this.fernTextureAtlas.setNumberOfRows(2);
			this.fernTexturedModel.getTexture().setHasTransparency(true);
			this.fernTexturedModel.getTexture().setUseFakeLighting(true);

			//Sample Oak
			this.oakTexturedModel = new TexturedModel(genOak1Model, new ModelTexture(loader.loadTexture("genOakWhole")));

		}
		
	}
	
	public void addPlayers() {
		
		this.player1 = new Player(this.worldControl, humanTexturedModel, new Vector3f(10, 0, 10), 0, 0, 0, 1.5f, 3, players, markers, true, 1, 62f);
		this.camera1 = new Camera(player1, "Test Player1", mousePicker1);
		this.player2 = new Player(this.worldControl, humanTexturedModel, new Vector3f(100, 0, 0), 0, 0, 0, 1, 5, players, markers, false, 2, 62f);
		this.camera2 = new Camera(player2, "Test Player 2", mousePicker1);
		this.player3 = new Player(this.worldControl, humanTexturedModel, new Vector3f(-100, 50, -100), 0, 0, 0, 1, 5, players, markers, false, 3, 62f);
		this.camera3 = new Camera(player3, "Test Player 3", mousePicker1);
		
		//Adding Players and Cameras
		this.players.add(player1);
		this.cameras.add(camera1);
		this.players.add(player2);
		this.cameras.add(camera2);
		this.players.add(player3);
		this.cameras.add(camera3);
		
		mousePicker1 = new MousePicker(Camera.currentCamera(this.cameras), this.renderer.getProjectionMatrix(), this.terrainsToRender);
		
	}
	
	public void initDerpLamps() {
		
		Random genRandom = new Random();
		
		for (int i = 0; i < 10; i ++) {
			
			float x = genRandom.nextFloat() * 800;
			float z = genRandom.nextFloat() * 800;
			float y = Terrain.getCurrentTerrain(getTerrains(), x, z).getHeightOfTerrain(x, z);
			
			this.entitiesToRender.add(new Entity(this.texturedDerpLamp, new Vector3f(x, y, z), 0, 0, 0, 5, true, false, 125f));
			this.lights.add(new Light(new Vector3f(x, y + 7f, z), new Vector3f(genRandom.nextFloat() * 6, genRandom.nextFloat() * 5, genRandom.nextFloat() * 5), new Vector3f(1f, 0.01f, 0.002f)));
		}
		
	}
	
	public void initEntities() {
		
		initModels();
		initTextures();
		initLights();
		initDerpLamps();
		
		//Adding Spare Entities
		this.dragon = new Entity(this.texturedDragon, new Vector3f(0, 0, -25), 0, 0, 0, 5, true, false, 500f);
		this.testHuman = new Entity(this.humanTexturedModel, new Vector3f(0, 2.5f, 0), 0, 0, 0, 1.5f, true, false, 62f);
		this.entitiesToRender.add(this.testHuman);
		this.entitiesToRender.add(this.dragon);
		
		addPlayers();
		initFlora();
		initCollisionShells();
		
		for (Vector3f pos:this.dragon.getCollisionShell().getWidePassBox()) {

			this.entitiesToRender.add(new Entity(this.texturedSphereModel, new Vector3f(pos.x, pos.y, pos.z), 0, 0, 0, 1, false, false, 3f));
			this.markers.add(new Marker(new Vector3f(pos.x, pos.y, pos.z)));
			System.out.println(pos.getZ());

		}
		
	}
	
	public void initFlora() {
		
		for (int i = 0; i < 5000; i++) {
		
		float x = generationRandom.nextFloat() * 800;
		float z = generationRandom.nextFloat() * 800;
		float y = Terrain.getCurrentTerrain(this.terrainsToRender, x, z).getHeightOfTerrain(x, z);
		this.entitiesToRender.add(new Entity(this.grassTexturedModel, new Vector3f(x, y, z), 0, 0, 0, 5f, false, false, 0.5f));
		this.entitiesToRender.add(new Entity(this.grassTexturedModel, new Vector3f(-x, Terrain.getCurrentTerrain(this.terrainsToRender, -x, -z).getHeightOfTerrain(-x, -z), -z), 0, 0, 0, 5f, false, false, 0.5f));
		this.entitiesToRender.add(new Entity(this.grassTexturedModel, new Vector3f(-x, Terrain.getCurrentTerrain(this.terrainsToRender, -x, z).getHeightOfTerrain(-x, z), z), 0, 0, 0, 5f, false, false, 0.5f));
		this.entitiesToRender.add(new Entity(this.grassTexturedModel, new Vector3f(x, Terrain.getCurrentTerrain(this.terrainsToRender, x, -z).getHeightOfTerrain(x, -z), -z), 0, 0, 0, 5f, false, false, 0.5f));
		
		}
		
		for (int i = 0; i < 5000; i++) {
			
		float x = generationRandom.nextFloat() * 800;
		float z = generationRandom.nextFloat() * 800;
		float y = Terrain.getCurrentTerrain(this.terrainsToRender, x, z).getHeightOfTerrain(x, z);
		this.entitiesToRender.add(new Entity(this.fernTexturedModel, generationRandom.nextInt(4), new Vector3f(x, y, z), 0, 0, 0, 1f, false, false, 0.5f));
		this.entitiesToRender.add(new Entity(this.fernTexturedModel, generationRandom.nextInt(4), new Vector3f(-x, Terrain.getCurrentTerrain(this.terrainsToRender, -x, -z).getHeightOfTerrain(-x, -z), -z), 0, 0, 0, 1f, false, false, 0.5f));
		this.entitiesToRender.add(new Entity(this.fernTexturedModel, generationRandom.nextInt(4), new Vector3f(-x, Terrain.getCurrentTerrain(this.terrainsToRender, -x, z).getHeightOfTerrain(-x, z), z), 0, 0, 0, 1f, false, false, 0.5f));
		this.entitiesToRender.add(new Entity(this.fernTexturedModel, generationRandom.nextInt(4), new Vector3f(x, Terrain.getCurrentTerrain(this.terrainsToRender, x, -z).getHeightOfTerrain(x, -z), -z), 0, 0, 0, 1f, false, false, 0.5f));
		
		}
		
		for (int i = 0; i < 40; i++) {
			
		float x = generationRandom.nextFloat() * 800;
		float z = generationRandom.nextFloat() * 800;
		float y = Terrain.getCurrentTerrain(this.terrainsToRender, x, z).getHeightOfTerrain(x, z);
		this.entitiesToRender.add(new Entity(this.oakTexturedModel, new Vector3f(x, y, z), 0, 0, 0, 7f, true, false, 125f));
		this.entitiesToRender.add(new Entity(this.oakTexturedModel, new Vector3f(-x, Terrain.getCurrentTerrain(this.terrainsToRender, -x, -z).getHeightOfTerrain(-x, -z), -z), 0, 0, 0, 7f, false, false, 125f));
		this.entitiesToRender.add(new Entity(this.oakTexturedModel, new Vector3f(x, Terrain.getCurrentTerrain(this.terrainsToRender, x, -z).getHeightOfTerrain(x, -z), -z), 0, 0, 0, 7f, false, false, 125f));
		this.entitiesToRender.add(new Entity(this.oakTexturedModel, new Vector3f(-x, Terrain.getCurrentTerrain(this.terrainsToRender, -x, z).getHeightOfTerrain(-x, z), z), 0, 0, 0, 7f, false, false, 125f));
		
		}
		
	}
	
	public void initCollisionShells() {
		
		for (Entity entity:this.entitiesToRender) {
			if (entity.canClip()) {
				entity.getCollisionShell().generateCorners(entity);
				entity.getCollisionShell().updateCollisionShell();
			}
		}

		for (Player player:this.players) {
			player.getCollisionShell().generateCorners(player);
			player.getCollisionShell().updateCollisionShell();
		}
		
	}
	
	public void updatePopulation() {
		
		this.activePlayer = Player.activePlayer(getPlayers());
		this.player3.increaseRotation(0, 0.1f, 0);
		this.activePlayer.move(Terrain.getCurrentTerrain(this.terrainsToRender, activePlayer.getPosition().x, activePlayer.getPosition().z), mousePicker1);
		Camera.currentCamera(cameras).updateCameraPos();
		
		this.sun.updateSunPosition(this.worldControl.getTime(), Player.activePlayer(players));
		
		this.updatePickers();
		
		for (Entity entity:this.entitiesToRender) {
			if (entity.canClip()) {
				entity.getCollisionShell().updateCollisionShell();
			}
		}

		for (Player player:this.players) {
			player.getCollisionShell().updateCollisionShell();
		}
		
		this.gravitizeEntities();
		
	}
	
	public void gravitizeEntities() {
		
		for (Entity entity:this.getEntities()) {
			if (!entity.getDoesHover()) {
				entity.gravitize(Terrain.getCurrentTerrain(this.getTerrains(), entity.getPosition().x, entity.getPosition().z));
			}
		}
		
	}
	
	public void updatePickers() {
		
		mousePicker1.update();
		
		terrainPoint1 = mousePicker1.getCurrentTerrainPoint();
		
	}
	
	public Camera activeCamera() {
		return Camera.currentCamera(getCameras());
	}
	
	public Player activePlayer() {
		return this.activePlayer;
	}
	
	public List<Entity> getEntities() {
		return this.entitiesToRender;
	}
	
	public List<Player> getPlayers() {
		return this.players;
	}
	
	public List<Camera> getCameras() {
		return this.cameras;
	}
	
	public List<Light> getLights() {
		return this.lights;
	}
	
	public List<Terrain> getTerrains() {
		return this.terrainsToRender;
	}
	
	public List<Marker> getMarkers() {
		return this.markers;
	}
	
	public Entity getDragon() {
		return this.dragon;
	}
	
	public Entity getTestHuman() {
		return this.testHuman;
	}
	
	public WorldControl getWorldControl() {
		return this.worldControl;
	}
	
}