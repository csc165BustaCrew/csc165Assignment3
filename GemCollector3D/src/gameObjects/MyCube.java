package gameObjects;

import graphicslib3D.Matrix3D;
import sage.scene.shape.Cube;

public class MyCube extends Cube implements Gem{
	private boolean alive = true;
	@Override
	public boolean isAlive() {
		return alive;
	}

	@Override
	public void setDead(boolean dead, int count) {
		alive = dead;
		if(!alive)
			moveLocation(count);
	}
	
	private void moveLocation(int count){
		this.setLocalTranslation(new Matrix3D());
		this.translate(count*2, 10, 0);
	}
}
