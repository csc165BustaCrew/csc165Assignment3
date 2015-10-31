package bullet;

import graphicslib3D.Matrix3D;
import net.java.games.input.Event;
import sage.camera.ICamera;
import sage.input.action.AbstractInputAction;
import sage.renderer.IRenderer;
import sage.scene.SceneNode;

public class CubeBullet extends AbstractInputAction {
	
	private SceneNode player;
	private Bullet bullet;
	private Matrix3D locationM;
	private Matrix3D rotationM;
	
	public CubeBullet(IRenderer renderer, SceneNode player){
		bullet = new Bullet();
		this.player = player;
		locationM = new Matrix3D();
		rotationM = new Matrix3D();
	}
	
	public SceneNode getBullet(){
		return bullet;
	}
	
	public void hit(){
		bullet.setDead();
	}
	
	private void initBullet(){
		locationM = (Matrix3D) player.getLocalTranslation().clone();
		rotationM = (Matrix3D) player.getLocalRotation().clone();
		Matrix3D tempM = new Matrix3D();
		
		//Building matrix based on the avatar's current location and rotation
		//so that it can travel based on the players viewing direction
		tempM.concatenate(locationM);
		tempM.concatenate(rotationM);

		bullet.setLocalTranslation(tempM);
	}

	@Override
	public void performAction(float time, Event arg1) {
		if(!bullet.isAlive()){
			initBullet();
			bullet.fireBullet();
		}
	}
}
