package myGameEngine;

import java.util.UUID;

import graphicslib3D.Matrix3D;
import graphicslib3D.Vector3D;
import sage.model.loader.OBJLoader;

//import java.util.UUID;

import sage.scene.SceneNode;
import sage.scene.TriMesh;
import sage.scene.shape.Cube;

public class GhostNPC {
	private TriMesh NPC;
	private UUID id;
	private OBJLoader objLoader = new OBJLoader();
	
	
	public GhostNPC(Matrix3D ghostM, UUID ghostID){
		System.out.println(ghostM.getCol(3));
		NPC = objLoader.loadModel("src/Models/car.obj");
		this.id = ghostID;
		setLocation(ghostM);
	}
	public void setLocation(Matrix3D ghostM){
		NPC.setLocalTranslation(ghostM);
	}
	public SceneNode getGhost(){
		return NPC;
	}
	public UUID getID(){
		return id;
	}
	public void update(Matrix3D m, Matrix3D scale) {
		NPC.setLocalTranslation(m);
//		NPC.setLocalScale(scale);
	}
}
