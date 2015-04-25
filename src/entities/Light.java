package entities;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import tools.Maths;

public class Light {

	private Vector3f pos;
	private Vector3f color;
	private Vector3f attenuation = new Vector3f(1, 0, 0);
	
	public Light(Vector3f pos, Vector3f color) {
		
		this.pos = pos;
		this.color = color;
		
	}
	
	public Light(Vector3f pos, Vector3f color, Vector3f attenuation) {
		
		this.pos = pos;
		this.color = color;
		this.attenuation = attenuation;
		
	}
	
	public void updateSunPosition(float time, Player player) {
		
		Vector4f transPos = new Vector4f(this.pos.x, this.pos.y, this.pos.z, 1);
		Matrix4f transMatrix = Maths.createTransformationMatrix(new Vector3f(), 0, (float) (1/24000), 0, 1);
		
		this.pos.x = player.getPosition().x + 2000;
		this.pos.y = player.getPosition().y + 2000;
		this.pos.z = player.getPosition().z + 2000;
		
		if (!player.getWorldControl().getPaused() && !player.getWorldControl().getPausedTime()) {
			
			Matrix4f.transform(transMatrix, transPos, transPos);
			this.pos.x = transPos.x;
			this.pos.y = transPos.y;
			this.pos.z = transPos.z;
			
			if (time >= 0 && time < 5000){
				
				this.color.set(0.01f, 0.01f, 0.01f);
				
			} else if (time >= 5000 && time < 8000){
				
				if (this.color.x < 0.7) {
					this.color.x += 0.01;
				}
				
				if (this.color.y < 0.7) {
					this.color.y += 0.01;
				}
				
				if (this.color.z < 0.7) {
					this.color.z += 0.01;
				}
				
			} else if (time >= 8000 && time < 19000){
				
				this.color.set(0.7f, 0.7f, 0.8f);
				
			} else {
				
				if (this.color.x > 0.01) {
					this.color.x -= 0.01;
				}
				
				if (this.color.y > 0.01) {
					this.color.y -= 0.01;
				}
				
				if (this.color.z > 0.01) {
					this.color.z -= 0.01;
				}

			}
			
		}
		
	}
	
	public void setAttenuation(Vector3f attenuation) {
		this.attenuation = attenuation;
	}
	
	public Vector3f getAttenuation() {
		return this.attenuation;
	}
	
	public Vector3f getPos() {
		return pos;
	}
	
	public void setPos(Vector3f pos) {
		this.pos = pos;
	}
	
	public Vector3f getColor() {
		return color;
	}
	
	public void setColor(Vector3f color) {
		this.color = color;
	}
	
}
