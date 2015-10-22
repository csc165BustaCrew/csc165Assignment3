package myGameEngine;

import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import net.java.games.input.Event;
import sage.camera.ICamera;
import sage.input.action.IAction;
import sage.input.action.RunAction;
import sage.scene.SceneNode;

public class MoveBackwardAction implements IAction{
	
	private SceneNode avatar;
	private float speed;
	
	
	public MoveBackwardAction(SceneNode n){
		avatar = n;
		speed = -0.01f;
	}
	
	@Override
	public void performAction(float time, Event e) {
		Matrix3D rot = avatar.getLocalRotation();
		Vector3D dir = new Vector3D(0,0,1);
		dir = dir.mult(rot);
		dir.scale((double)speed*time);
		avatar.translate((float)dir.getX(), (float)dir.getY(), (float)dir.getZ());
	}
}
