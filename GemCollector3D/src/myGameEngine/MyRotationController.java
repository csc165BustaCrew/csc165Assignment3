package myGameEngine;

import graphicslib3D.Matrix3D;
import sage.scene.Controller;
import sage.scene.SceneNode;

public class MyRotationController extends Controller{
	private double rotationRate = 0.02;
	private double cycleTime = 2000.0;
	private double totalTime;
	private double rot = 0.0;
	
	public void update(double time) {
		totalTime += time;
		double rotAmount = rotationRate*time;
		
		if(totalTime > cycleTime){
			totalTime = 0.0;
		}
		rotAmount = rotAmount + rot;
		
		Matrix3D newRot = new Matrix3D();
		newRot.rotateY(rotAmount);
		
		for(SceneNode node : controlledNodes){
			Matrix3D curRot = node.getLocalRotation();
			curRot.rotateY(rotAmount);
			node.setLocalRotation(curRot);
		}
		
	}
}
