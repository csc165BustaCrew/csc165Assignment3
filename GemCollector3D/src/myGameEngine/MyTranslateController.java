package myGameEngine;

import java.util.Random;

import graphicslib3D.Matrix3D;
import sage.scene.Controller;
import sage.scene.SceneNode;

public class MyTranslateController extends Controller{
	private double translationRate = .002 ; 
	private double cycleTime = 2000.0; 
	private double totalTime;
	private double direction = 1.0;
	private Random rand;
	
	public MyTranslateController(){
		rand = new Random();
	}

	public void setCycleTime(double c){
		cycleTime = c;
	}
	
	@Override
	public void update(double time) {
		totalTime += time;
		rand.setSeed((long)totalTime);
		
		 double transAmount = translationRate * ((rand.nextFloat() - 2) + 1*4)  ;
		 
		 if (totalTime > cycleTime) {
			 direction = -direction;
			 totalTime = 0.0;
		 }
		 
		 transAmount = direction * transAmount;
		 Matrix3D newTrans = new Matrix3D();
		 newTrans.translate(transAmount, 0, transAmount);

		 for (SceneNode node : controlledNodes) {
			 Matrix3D curTrans = node.getLocalTranslation();
			 curTrans.concatenate(newTrans);
			 node.setLocalTranslation(curTrans);
		 	}
	 }
}
