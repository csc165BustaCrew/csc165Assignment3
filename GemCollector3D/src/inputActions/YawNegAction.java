package inputActions;

import graphicslib3D.Vector3D;
import net.java.games.input.Event;
import sage.input.action.AbstractInputAction;
import sage.scene.SceneNode;

public class YawNegAction extends AbstractInputAction {
	private Vector3D yAxis = new Vector3D(0,1,0);
	private int rot = -1;
	
	private SceneNode avatar;
	
	public YawNegAction(SceneNode avatar){
		this.avatar = avatar;
	}
	@Override
	public void performAction(float arg0, Event arg1) {
		avatar.rotate(rot, yAxis);
	}

}
