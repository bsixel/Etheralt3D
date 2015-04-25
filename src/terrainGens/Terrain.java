package terrainGens;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import models.RawModel;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.Loader;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import tools.Maths;

public class Terrain {
	
	//Numbers
	private static final float terrainSize = 512;
	private static final float maxTerrainHeight = 50;
	private static final float maxColorSetting = (float) 256 * 256 * 256;
	private float x;
	private float z;
	
	//Arrays
	private float[][] heights;
	
	//Vectors
	
	//Modeling Things
	private RawModel model;
	private TerrainTexturePack texturePack;
	private TerrainTexture blendMap;
	
	public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap, String heightMapName) {
		
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * terrainSize;
		this.z = gridZ * terrainSize;
		this.model = generateTerrain(loader, heightMapName);
		
	}
	
	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public RawModel getModel() {
		return model;
	}
	
	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}

	public TerrainTexture getBlendMap() {
		return blendMap;
	}
	
	public static Terrain getCurrentTerrain(List<Terrain> terrainList, float worldX, float worldZ) {
		for (Terrain terrain:terrainList) {
			if (worldX >= terrain.getX() && worldX < terrain.getX() + terrainSize && worldZ >= terrain.getZ() && worldZ < terrain.getZ() + terrainSize) {
				return terrain;
			}
		}
		return null;
	}
	
	/*public float getHeightOfTerrain(float worldX, float worldZ) {
		
		float terrainX = worldX - this.x;
		float terrainZ = worldZ - this.z;
		float gridSquareSize = terrainSize / ((float) heights.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
		
		if (gridX >= heights.length - 1 || gridZ >= heights.length || gridX < 0 || gridZ < 0) {
			return 0;
		}
		
		float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
		float answer;
		if (xCoord <= (1-zCoord)) {
			answer = Maths.barryCentric(
					new Vector3f(0, heights[gridX][gridZ], 0),
					new Vector3f(1, heights[gridX + 1][gridZ], 0),
					new Vector3f(0, heights[gridX][gridZ + 1], 1),
					new Vector2f(xCoord, zCoord));
		} else {
			answer = Maths.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
							heights[gridX + 1]
									[gridZ + 1], 1), 
									new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}
		
		return answer;
		
	}*/
	
public float getHeightOfTerrain(float worldX, float worldZ) {
		
		float terrainX = worldX - this.x;
		float terrainZ = worldZ - this.z;
		float gridSquareSize = terrainSize / ((float) heights.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
		
		if (gridX >= heights.length - 1 || gridZ >= heights.length || gridX < 0 || gridZ < 0) {
			return 0;
		}
		
		float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
		float answer;
		if (xCoord <= (1-zCoord)) {
			answer = Maths.barryCentric(
					new Vector3f(0, heights[gridX][gridZ], 0),
					new Vector3f(1, heights[gridX][gridZ], 0),
					new Vector3f(0, heights[gridX][gridZ], 1),
					new Vector2f(xCoord, zCoord));
		} else {
			answer = Maths.barryCentric(new Vector3f(1, heights[gridX][gridZ], 0), new Vector3f(1,
							heights[gridX]
									[gridZ], 1), 
									new Vector3f(0,
							heights[gridX][gridZ], 1), new Vector2f(xCoord, zCoord));
		}
		
		return answer;
		
	}
	
	@SuppressWarnings("static-access")
	private RawModel generateTerrain(Loader loader, String heightMapName){
		
		BufferedImage heightMap = null;
		try {
			heightMap = ImageIO.read(new File("resources/images/" + heightMapName + ".png"));
		} catch (IOException e) {
			System.out.println("Double check heightMap image file! May not have the right path.");
			e.printStackTrace();
		}
		
		int vertexCount = heightMap.getHeight();
		heights = new float[vertexCount][vertexCount];
		
		int count = vertexCount * vertexCount;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6 * (vertexCount - 1) * (vertexCount * 1)];
		int vertexPointer = 0;
		for(int i=0;i<vertexCount; i++){
			for(int j=0; j<vertexCount; j++){
				vertices[vertexPointer * 3] = (float) j / ((float)vertexCount - 1) * terrainSize;
				float height = getTerrainHeight(j, i, heightMap);
				heights[j][i] = height;
				vertices[vertexPointer * 3 + 1] = height;
				vertices[vertexPointer * 3 + 2] = (float) i / ((float)vertexCount - 1) * terrainSize;
				Vector3f normal = calculateHeightNormal(j, i, heightMap);
				normals[vertexPointer *  3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				textureCoords[vertexPointer * 2] = (float) j / ((float)vertexCount - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) i / ((float)vertexCount - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz = 0; gz < vertexCount - 1; gz++){
			for(int gx=0; gx < vertexCount - 1; gx++){
				int topLeft = (gz*vertexCount)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * vertexCount)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}
	
	private Vector3f calculateHeightNormal(int x, int z, BufferedImage heightmap) {
		
		int vertexCount = heightmap.getHeight();
		float heightL = getTerrainHeight(x - 1, z, heightmap);
		float heightR = getTerrainHeight(x + 1, z, heightmap);
		float heightU = getTerrainHeight(x, z + 1, heightmap);
		float heightD = getTerrainHeight(x, z - 1, heightmap);
		
		//X handler
		if (x == 0) {
			
			heightL = getTerrainHeight(x - 1 + vertexCount, z, heightmap);
			heightR = getTerrainHeight(x + 1, z, heightmap);
			
		} else if (x == 1) {
			
			heightL = getTerrainHeight(x - 1 + vertexCount - 1, z, heightmap);
			heightR = getTerrainHeight(x + 1, z, heightmap);
			
		} else if (x == vertexCount) {
			
			heightL = getTerrainHeight(x - 1, z, heightmap);
			heightR = getTerrainHeight(x + 1 - vertexCount, z, heightmap);
			
		} else if (x == vertexCount - 1) {
			
			heightL = getTerrainHeight(x - 1, z, heightmap);
			heightR = getTerrainHeight(x + 1 - vertexCount + 1, z, heightmap);
			
		} else {
			
			heightL = getTerrainHeight(x - 1, z, heightmap);
			heightR = getTerrainHeight(x + 1, z, heightmap);
			
		}
		
		//Z handler
		if (z == 0) {
			
			heightU = getTerrainHeight(x, z-1 + vertexCount, heightmap);
			heightD = getTerrainHeight(x, z + 1, heightmap);
			
		} else if (z == 1) {
			
			heightU = getTerrainHeight(x, z - 1 + vertexCount - 1, heightmap);
			heightD = getTerrainHeight(x, z + 1, heightmap);
			
		} else if (z == vertexCount) {
			
			heightU = getTerrainHeight(x, z - 1, heightmap);
			heightD = getTerrainHeight(x, z + 1 - vertexCount, heightmap);
			
		} else if (z == vertexCount - 1) {
			
			heightU = getTerrainHeight(x, z - 1, heightmap);
			heightD = getTerrainHeight(x, z + 1 - vertexCount + 1, heightmap);
			
		} else {
			
			heightU = getTerrainHeight(x, z - 1, heightmap);
			heightD = getTerrainHeight(x, z + 1, heightmap);
			
		}
		
		Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		normal.normalise();
		return normal;
		
	}
	
	private float getTerrainHeight(int x, int z, BufferedImage heightMap) {
		
		if (x < 0 || x >= heightMap.getHeight() || z < 0 || z >= heightMap.getWidth()) {
			return 0;
		}
		
		float height = heightMap.getRGB(x, z);
		height += maxColorSetting / 2f;
		height /= maxColorSetting / 2f;
		height *= maxTerrainHeight;
		return height;
		
	}
	
}
