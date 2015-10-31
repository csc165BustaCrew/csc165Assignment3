package inputActions;

import graphicslib3D.Matrix3D;
import graphicslib3D.Vector3D;
import net.java.games.input.Event;
import sage.input.action.AbstractInputAction;
import sage.scene.SceneNode;

public class MoveBackwardAction extends AbstractInputAction{
	private SceneNode avatar;
	float speed = -0.01f;

	public MoveBackwardAction(SceneNode avatar){
		this.avatar = avatar;
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
