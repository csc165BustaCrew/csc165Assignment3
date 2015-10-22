package myGameEngine;

import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import net.java.games.input.Component.Identifier.Axis;
import net.java.games.input.Event;
import sage.camera.ICamera;
import sage.input.action.IAction;
import sage.scene.SceneNode;

public class LookLeftAction implements IAction{
	
	private SceneNode avatar;
	private float speed;
	
	
	public LookLeftAction(SceneNode n){
		avatar = n;
		speed = 0.1f;
	}
	
	@Override
	public void performAction(float time, Event e) {
		Matrix3D rotationAmt = new Matrix3D();
		Matrix3D rot = avatar.getLocalRotation();
		Vector3D dir = new Vector3D(0,1,0);
		
		dir = dir.mult(rot);
		avatar.rotate((float) 0.1, dir);
	}
}
