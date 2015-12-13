package myGameEngine;

import java.util.UUID;

import graphicslib3D.Matrix3D;
import sage.scene.SceneNode;
import sage.scene.shape.Cube;

public class GhostAvatar {
	private Cube avatar;
	private UUID id;
	
	public GhostAvatar(Matrix3D m, UUID id){
		this.id = id;
		avatar = new Cube();
		avatar.setLocalTranslation(m);
	}
	
	public UUID getID(){
		return id;
	}
	
	public void update(Matrix3D m){
		avatar.setLocalTranslation(m);
	}
	
	public SceneNode getGhost(){
		return avatar;
	}
	
}
