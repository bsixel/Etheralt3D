package gui;

import java.util.List;

import models.RawModel;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import renderEngine.Loader;
import tools.Maths;
import entities.Player;

public class GUIRenderer {
	
	private final RawModel quad;
	private GUIShader shader;
	
	public GUIRenderer(Loader loader) {
		
		float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
		quad = loader.loadToVAO(positions, 2);
		shader = new GUIShader();
		
	}
	
	public void render(List<GUITexture> GUIElements, Player player) {

		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		for (GUITexture GUI:GUIElements) {

			if ((!player.isThirdPerson || GUI.renderedThirdPerson) && player.getPlayerType() == 1 || player.getPlayerType() == 2 || player.getPlayerType() == 3 && GUI.isEnabled) {

				GL13.glActiveTexture(GL13.GL_TEXTURE0);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, GUI.getTexture());
				Matrix4f matrix = Maths.createTransformationMatrix(GUI.getPosition(), GUI.getScale());
				shader.loadTransformation(matrix);
				GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());

			}

		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();

	}
	
	public void cleaner() {
		
		shader.cleaner();
		
	}
	
}