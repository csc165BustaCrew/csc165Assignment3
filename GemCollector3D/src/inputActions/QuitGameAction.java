package inputActions;

import sage.input.action.AbstractInputAction;
import sage.app.AbstractGame;
import net.java.games.input.*;

public class QuitGameAction extends AbstractInputAction{
	private AbstractGame game;
	
	public QuitGameAction(AbstractGame game){
		this.game = game;
	}
	
	@Override
	public void performAction(float time, Event event) {
		game.setGameOver(true);
	}

}
