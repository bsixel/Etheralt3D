package worldPopulator;

import java.util.ArrayList;
import java.util.List;

import renderEngine.Loader;
import terrainGens.Terrain;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class TerrainPopulator {
	
	public TerrainPopulator(Loader loader) {
		this.loader = loader;
	}
	
	//Loaders, etc
	private Loader loader;
	
	//Lists
	public List<Terrain> terrainsToRender = new ArrayList<Terrain>();
	
	//Sub-TerrainThings: Terrain Itself
	TerrainTexture backgroundTexture;
	TerrainTexture rTexture;
	TerrainTexture gTexture;
	TerrainTexture bTexture;
	TerrainTexturePack texturePack1;
	TerrainTexture blendMap;
	
	public void initTerrain() {

		backgroundTexture = new TerrainTexture(this.loader.loadTexture("grassy4"));
		rTexture = new TerrainTexture(this.loader.loadTexture("mud"));
		gTexture = new TerrainTexture(this.loader.loadTexture("grassFlowers"));
		bTexture = new TerrainTexture(this.loader.loadTexture("path"));

		texturePack1 = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		blendMap = new TerrainTexture(this.loader.loadTexture("blendMap"));

		for (int i = -5; i < 5; i ++) {
			for (int j = -5; j < 5; j ++) {
				this.terrainsToRender.add(new Terrain(i, j, this.loader, texturePack1, blendMap, "heightmapTest2"));
			}
		}
	}
	
	public List<Terrain> getTerrains() {
		return this.terrainsToRender;
	}
		
}