package myGameEngine;

import java.nio.FloatBuffer;

import graphicslib3D.Matrix3D;
import sage.event.EventManager;
import sage.event.IEventManager;
import sage.scene.Controller;
import sage.scene.SceneNode;
import myGameEngine.CrashEvent;
import myGameEngine.MyPyramid;

public class MyCollisionController extends Controller{
	IEventManager eventMgr = EventManager.getInstance();
	private double cycleTime = 2000.0;
	private double totalTime;
	
	public void update(double time) {
		totalTime += time;
		if(totalTime > cycleTime){
			totalTime = 0.0;
		}
	}
	public void collisionTriger(CrashEvent c){
		CrashEvent newCrash = c;
		eventMgr.triggerEvent(newCrash);
	}

}
