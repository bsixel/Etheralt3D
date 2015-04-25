package tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import objConverter.Vertex;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Entity;

public class CollisionChecker {

	private Vector3f bottomFrontLeft = new Vector3f();
	private Vector3f bottomFrontRight = new Vector3f();
	private Vector3f bottomBackLeft = new Vector3f();
	private Vector3f bottomBackRight = new Vector3f();
	private Vector3f topFrontLeft = new Vector3f();
	private Vector3f topFrontRight = new Vector3f();
	private Vector3f topBackLeft = new Vector3f();
	private Vector3f topBackRight = new Vector3f();

	Vector3f[] widePassBox = {bottomFrontLeft, bottomFrontRight, bottomBackLeft, bottomBackRight, topFrontLeft, topFrontRight, topBackLeft, topBackRight};
	List<Vertex> encompassedVertices;
	Entity entity;

	private List<Vertex> generateEncompassedVertices() {
		return entity
				.getModel()
				.getRawModel()
				.getVertices();
	}

	public void generateCorners(Entity entity) {

		this.entity = entity;

		List<Float> XFloats = new ArrayList<Float>();
		List<Float> YFloats = new ArrayList<Float>();
		List<Float> ZFloats = new ArrayList<Float>();

		for (Vertex vertex:generateEncompassedVertices()) {

			XFloats.add((vertex.getPosition().x));
			YFloats.add((vertex.getPosition().y));
			ZFloats.add((vertex.getPosition().z));

		}

		Collections.sort(XFloats);
		Collections.sort(YFloats);
		Collections.sort(ZFloats);

		this.bottomFrontLeft.x = XFloats.get(0);
		this.bottomFrontLeft.y = YFloats.get(0);
		this.bottomFrontLeft.z = ZFloats.get(0);
		
		this.bottomFrontRight.x = XFloats.get(XFloats.size() - 1);
		this.bottomFrontRight.y = YFloats.get(0);
		this.bottomFrontRight.z = ZFloats.get(0);
		
		this.bottomBackLeft.x = XFloats.get(0);
		this.bottomBackLeft.y = YFloats.get(0);
		this.bottomBackLeft.z = ZFloats.get(ZFloats.size() - 1);
		
		this.bottomBackRight.x = XFloats.get(XFloats.size() - 1);
		this.bottomBackRight.y = YFloats.get(0);
		this.bottomBackRight.z = ZFloats.get(ZFloats.size() - 1);
		
		this.topFrontLeft.x = XFloats.get(0);
		this.topFrontLeft.y = YFloats.get(YFloats.size() - 1);
		this.topFrontLeft.z = ZFloats.get(0);
		
		this.topFrontRight.x = XFloats.get(XFloats.size() - 1);
		this.topFrontRight.y = YFloats.get(YFloats.size() - 1);
		this.topFrontRight.z = ZFloats.get(0);
		
		this.topBackLeft.x = XFloats.get(0);
		this.topBackLeft.y = YFloats.get(YFloats.size() - 1);
		this.topBackLeft.z = ZFloats.get(ZFloats.size() - 1);
		
		this.topBackRight.x = XFloats.get(XFloats.size() - 1);
		this.topBackRight.y = YFloats.get(YFloats.size() - 1);
		this.topBackRight.z = ZFloats.get(ZFloats.size() - 1);

	}

	public void updateCollisionShell() {

		for (Vector3f pos:this.getWidePassBox()) {

			Vector4f transPos = new Vector4f(pos.x, pos.y, pos.z, 1);
			Matrix4f transMat = Maths.createTransformationMatrix(this.entity.getPosition(), this.entity.getRotX(), this.entity.getRotY(), this.entity.getRotZ(), this.entity.getScale());
			Matrix4f.transform(transMat, transPos, transPos);
			pos.x = transPos.x;
			pos.y = transPos.y;
			pos.z = transPos.z;

		}

	}

	public boolean checkWithinWidePassBox(Vector3f point) {
		
		if (this.bottomBackLeft.x <= point.x && point.x <= this.topFrontRight.x &
				this.bottomBackLeft.y <= point.y && point.y <= this.topFrontRight.y &
				this.bottomBackLeft.z <= point.z && point.z <= this.topFrontRight.z) {
			return true;
		} else {
			return false;
		}
		
	}

	public float getEntityHeight() {
		return (float) (this.topBackLeft.y - this.bottomBackLeft.y);
	}

	public Vector3f[] getWidePassBox() {
		return this.widePassBox;
	}

	public Vector3f[] getUpdatedWidePassBox() {
		return this.widePassBox;
	}
	
	public void printCorners() {
		
		System.out.println();
		
	}

}