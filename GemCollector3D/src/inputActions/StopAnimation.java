package inputActions;

import net.java.games.input.Event;
import sage.input.action.AbstractInputAction;
import sage.scene.Model3DTriMesh;

public class StopAnimation extends AbstractInputAction{

	private Model3DTriMesh avatar;
	private boolean isAnimating;
	
	public StopAnimation(Model3DTriMesh avatar){
		this.avatar = avatar;
		isAnimating = false;
	}
	
	@Override
	public void performAction(float arg0, Event arg1) {
		if(!isAnimating){
			avatar.startAnimation("walk");
			isAnimating = false;
		}
		else{
			isAnimating = true;
		}
	}

}
