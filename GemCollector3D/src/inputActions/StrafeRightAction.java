package inputActions;

import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import net.java.games.input.Event;
import sage.input.action.AbstractInputAction;
import sage.scene.SceneNode;
import sage.terrain.TerrainBlock;

public class StrafeRightAction extends AbstractInputAction{
	private TerrainBlock terrain;
	private SceneNode avatar;
	private float speed = 0.05f;
	
	public StrafeRightAction(SceneNode avatar, TerrainBlock ter){
		this.terrain = ter;
		this.avatar = avatar;
	}

	@Override
	public void performAction(float time, Event e) {
		if(!(avatar.getLocalTranslation().getCol(3).getZ() >= 498f)){
			Matrix3D rot = avatar.getLocalRotation();
			Vector3D dir = new Vector3D(1,0,0);
			dir = dir.mult(rot);
			dir.scale((double)speed*time);
			avatar.translate((float)dir.getX(), (float)dir.getY(), (float)dir.getZ());
			updateVerticalPosition();
		}
	}
	private void updateVerticalPosition(){
		Point3D avLoc = new Point3D(avatar.getLocalTranslation().getCol(3));
		float x = (float) avLoc.getX();
		float z = (float) avLoc.getZ();
		float terHeight = terrain.getHeight(x,z);
		boolean nanCheck = Float.isNaN(terHeight);
		if(!nanCheck){
			float desiredHeight = terHeight + (float)terrain.getOrigin().getY() + 13.0f;
			avatar.getLocalTranslation().setElementAt(1, 3, desiredHeight);
		}
	}
}
