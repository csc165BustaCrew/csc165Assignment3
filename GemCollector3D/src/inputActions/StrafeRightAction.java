package inputActions;

import graphicslib3D.Matrix3D;
import graphicslib3D.Vector3D;
import net.java.games.input.Event;
import sage.input.action.AbstractInputAction;
import sage.scene.SceneNode;

public class StrafeRightAction extends AbstractInputAction{
	private SceneNode avatar;
	private float speed = -0.01f;
	
	public StrafeRightAction(SceneNode avatar){
		this.avatar = avatar;
	}

	@Override
	public void performAction(float time, Event e) {
		Matrix3D rot = avatar.getLocalRotation();
		Vector3D dir = new Vector3D(1,0,0);
		dir = dir.mult(rot);
		dir.scale((double)speed*time);
		avatar.translate((float)dir.getX(), (float)dir.getY(), (float)dir.getZ());
	}
}
