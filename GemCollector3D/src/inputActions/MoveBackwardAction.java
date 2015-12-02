package inputActions;

import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import net.java.games.input.Event;
import sage.input.action.AbstractInputAction;
import sage.physics.IPhysicsObject;
import sage.scene.SceneNode;
import sage.terrain.TerrainBlock;

public class MoveBackwardAction extends AbstractInputAction{
	//private IPhysicsObject player;
	private TerrainBlock terrain;
	private SceneNode avatar;
	float speed = 0.01f;

	public MoveBackwardAction(SceneNode avatar, TerrainBlock ter){
		//this.player = obj;
		this.terrain = ter;
		this.avatar = avatar;
	}

	@Override
	public void performAction(float time, Event e) {
		Matrix3D rot = avatar.getLocalRotation();
		Vector3D dir = new Vector3D(0,0,1);
		dir = dir.mult(rot);
		dir.scale((double)speed*time);
		avatar.translate((float)dir.getX(), (float)dir.getY(), (float)dir.getZ());
		updateVerticalPosition();
	}
	private void updateVerticalPosition(){
		Point3D avLoc = new Point3D(avatar.getLocalTranslation().getCol(3));
		float x = (float) avLoc.getX();
		float z = (float) avLoc.getZ();
		float terHeight = terrain.getHeight(x,z);
		boolean nanCheck = Float.isNaN(terHeight);
		if(!nanCheck){
			System.out.println(terHeight);
			float desiredHeight = terHeight + (float)terrain.getOrigin().getY() + 13.0f;
			avatar.getLocalTranslation().setElementAt(1, 3, desiredHeight);
		}
	}
}
