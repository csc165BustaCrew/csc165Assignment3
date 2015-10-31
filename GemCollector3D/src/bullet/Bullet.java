package bullet;

import sage.renderer.IRenderer;
import sage.scene.shape.Cube;

public class Bullet extends Cube{
	private float time = 0.001f;
	private boolean isAlive = false;
	public Bullet() {
		this.translate(0, 100, 0);
	}
	
	public boolean isAlive(){
		return isAlive;
	}
	
	public void fireBullet(){
		isAlive = true;
	}
	
	public void setDead(){
		time = 0.001f;
		isAlive = false;
		this.translate(0, 100, 0);
	}
	
	@Override
	public void draw(IRenderer r){
		super.draw(r);
		if(isAlive){
			time += .001f;
			this.translate(0f, 0.0f, time);
		}
		
		if(this.getLocalTranslation().getCol(3).getZ() > 90
				|| this.getLocalTranslation().getCol(3).getZ() < -90){
			setDead();
		}
		
	}

}
