package inputActions;

import graphicslib3D.Vector3D;
import net.java.games.input.Event;
import sage.input.action.AbstractInputAction;
import sage.scene.SceneNode;

public class PitchPosAction extends AbstractInputAction {
	private Vector3D yAxis = new Vector3D(1,0,0);
	private float rot = .01f;
	
	private SceneNode avatar;
	
	public PitchPosAction(SceneNode avatar){
		this.avatar = avatar;
	}
	@Override
	public void performAction(float arg0, Event arg1) {
		avatar.rotate(rot, yAxis);
	}

}
