package skybox;

import models.RawModel;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import renderEngine.DisplayHandler;
import renderEngine.Loader;
import engineTester.WorldControl;
import entities.Camera;

public class SkyboxRenderer {
	
private static final float SIZE = 2000f;
	
	private static final float[] VERTICES = {
		
	    -SIZE,  SIZE, -SIZE,
	    -SIZE, -SIZE, -SIZE,
	    SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	    -SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE
	     
	};
	
	private static String[] DAY_TEXTURE_FILES = {"dayRight", "dayLeft", "dayTop", "dayBottom", "dayBack", "dayFront"};
	private static String[] NIGHT_TEXTURE_FILES = {"nightRight", "nightLeft", "nightTop", "nightBottom", "nightBack", "nightFront"};
	
	private RawModel cube;
	private int dayTexture;
	private int nightTexture;
	public float time;
	private SkyboxShader shader;
	private WorldControl worldControl;
	
	public SkyboxRenderer(WorldControl worldControl, Loader loader, Matrix4f projectionMatrix) {
		
		this.worldControl = worldControl;
		this.time = worldControl.getTime();
		this.cube = loader.loadToVAO(VERTICES, 3);
		this.dayTexture = loader.loadCubeMap(DAY_TEXTURE_FILES);
		this.nightTexture = loader.loadCubeMap(NIGHT_TEXTURE_FILES);
		this.shader = new SkyboxShader();
		this.shader.start();
		this.shader.connectTextureUnits();
		this.shader.loadProjectionMatrix(projectionMatrix);
		this.shader.stop();
		
	}
	
	private float getTime() {
		return this.worldControl.getTime();
	}
	
	public void render(Camera camera, float r, float g, float b) {
		
		this.shader.start();
		this.shader.loadViewMatrix(camera);
		this.shader.loadFogColor(r, g, b);
		GL30.glBindVertexArray(cube.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		bindTextures();
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		this.shader.stop();
		
	}
	
	private void bindTextures(){

		if (!this.worldControl.getPaused() && !this.worldControl.getPausedTime()) {
			int texture1;
			int texture2;
			float blendFactor;	

			if (this.time >= 0 && this.time < 5000){

				texture1 = this.nightTexture;
				texture2 = this.nightTexture;
				blendFactor = (this.time - 0)/(5000 - 0);

			} else if (this.time >= 5000 && this.time < 8000){

				texture1 = this.nightTexture;
				texture2 = this.dayTexture;
				blendFactor = (this.time - 5000)/(8000 - 5000);

			} else if (this.time >= 8000 && this.time < 19000){

				texture1 = this.dayTexture;
				texture2 = this.dayTexture;
				blendFactor = (this.time - 8000)/(19000 - 8000);

			} else {

				texture1 = this.dayTexture;
				texture2 = this.nightTexture;
				blendFactor = (this.time - 19000)/(24000 - 19000);

			}

			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
			GL13.glActiveTexture(GL13.GL_TEXTURE1);
			GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);
			this.shader.loadBlendFactor(blendFactor);
			
		}
	}
	
}
