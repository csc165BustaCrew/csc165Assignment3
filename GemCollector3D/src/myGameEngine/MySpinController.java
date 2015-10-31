package myGameEngine;

import graphicslib3D.Matrix3D;
import sage.scene.Controller;
import sage.scene.SceneNode;

public class MySpinController extends Controller{
	private boolean spin = false;
	
	public MySpinController(){
	}

	public void spin(){
		spin = true;
	}
	
	@Override
	public void update(double time) {
		if(spin){
			Matrix3D newTrans = new Matrix3D();
			newTrans.rotate(0, 90, 0);
			
			for (SceneNode node : controlledNodes) {
				Matrix3D curTrans = node.getLocalTranslation();
				curTrans.concatenate(newTrans);
				node.setLocalTranslation(curTrans);
			}
			spin = false;
		}
		 
		
	}
}
