package models;

import java.util.List;

import objConverter.Vertex;

public class RawModel {
	
	//Numbers
	private int vaoID;
	private int vertexCount;
	
	//Lists
	List<Vertex> vertices;
	
	public RawModel(int vaoID, int vertexCount, List<Vertex> vertices) {

		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		this.vertices = vertices;

	}
	
	public RawModel(int vaoID, int vertexCount) {

		this.vaoID = vaoID;
		this.vertexCount = vertexCount;

	}

	public List<Vertex> getVertices() {

		return this.vertices;

	}

	public int getVaoID() {
		return this.vaoID;
	}

	public int getVertexCount() {
		return this.vertexCount;
	}

}