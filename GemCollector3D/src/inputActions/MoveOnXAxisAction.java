package inputActions;

import graphicslib3D.Matrix3D;
import graphicslib3D.Vector3D;
import net.java.games.input.Event;
import sage.input.action.AbstractInputAction;
import sage.scene.SceneNode;


public class MoveOnXAxisAction extends AbstractInputAction{
	private SceneNode avatar;
	private float speed = 0.01f;
	
	public MoveOnXAxisAction(SceneNode avatar){
		this.avatar = avatar;
	}
	
	@Override
	public void performAction(float time, Event e) {
		if(e.getValue() > 0.73f){
			Matrix3D rot = avatar.getLocalRotation();
			Vector3D dir = new Vector3D(1,0,0);
			dir = dir.mult(rot);
			dir.scale((double)-speed*time);
			avatar.translate((float)dir.getX(), (float)dir.getY(), (float)dir.getZ());
		}
		else if(e.getValue() <-0.121f){
			Matrix3D rot = avatar.getLocalRotation();
			Vector3D dir = new Vector3D(1,0,0);
			dir = dir.mult(rot);
			dir.scale((double)speed*time);
			avatar.translate((float)dir.getX(), (float)dir.getY(), (float)dir.getZ());
		}
	}
}
