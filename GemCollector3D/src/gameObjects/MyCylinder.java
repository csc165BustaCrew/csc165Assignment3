package gameObjects;

import graphicslib3D.Matrix3D;
import sage.scene.shape.Cylinder;

public class MyCylinder extends Cylinder implements Gem{

	private boolean alive = true;
	@Override
	public boolean isAlive() {
		return alive;
	}

	@Override
	public void setDead(boolean deadorAlive, int count) {
		this.setLocalTranslation(new Matrix3D());
		this.translate(count*2, 10, 0);		
	}
}
