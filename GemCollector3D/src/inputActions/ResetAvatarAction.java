package inputActions;

import graphicslib3D.Matrix3D;
import net.java.games.input.Event;
import sage.input.action.AbstractInputAction;
import sage.scene.SceneNode;

public class ResetAvatarAction extends AbstractInputAction {
	private SceneNode avatar;
	
	public ResetAvatarAction(SceneNode avatar){
		this.avatar = avatar;
	}

	@Override
	public void performAction(float arg0, Event arg1) {
		avatar.setLocalRotation(new Matrix3D());
	}

}
