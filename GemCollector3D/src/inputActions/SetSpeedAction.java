package inputActions;

import net.java.games.input.Event;
import sage.input.action.AbstractInputAction;

public class SetSpeedAction extends AbstractInputAction {
	private boolean running = false;
	
	public boolean isRunning(){
		return running;
	}

	@Override
	public void performAction(float arg0, Event arg1) {
		running = !running;
	}
	
	
}
