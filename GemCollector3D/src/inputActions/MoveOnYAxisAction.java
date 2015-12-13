package inputActions;


import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import net.java.games.input.Event;
import sage.input.action.AbstractInputAction;
import sage.scene.Model3DTriMesh;
import sage.scene.SceneNode;
import sage.terrain.TerrainBlock;


public class MoveOnYAxisAction extends AbstractInputAction{
	private Model3DTriMesh avatar;
	private float speed = -0.01f;
	private boolean isAnimating;
	private TerrainBlock terrain;
	
	public MoveOnYAxisAction(Model3DTriMesh avatar, TerrainBlock ter){
		this.avatar = avatar;
		isAnimating = false;
		terrain = ter;
	}
	
	@Override
	public void performAction(float time, Event e) {
		if(e.getValue() < -0.095f){
			if(!isAnimating){
				avatar.startAnimation("walk");
				isAnimating = true;
			}
			if(!(avatar.getLocalTranslation().getCol(3).getZ() <= .35f)){
				Matrix3D rot = avatar.getLocalRotation();
				Vector3D dir = new Vector3D(0,0,1);
				dir = dir.mult(rot);
				dir.scale((double)speed*time);
				avatar.translate((float)dir.getX(), (float)dir.getY(), (float)dir.getZ());
				updateVerticalPosition();
			}
		}
		else if(e.getValue() > -0.059f){
			if(!isAnimating){
				avatar.startAnimation("walk");
				isAnimating = true;
			}
			if(!(avatar.getLocalTranslation().getCol(3).getZ() >= 498f)){
				Matrix3D rot = avatar.getLocalRotation();
				Vector3D dir = new Vector3D(0,0,1);
				dir = dir.mult(rot);
				dir.scale((double)-speed*time);
				avatar.translate((float)dir.getX(), (float)dir.getY(), (float)dir.getZ());
				updateVerticalPosition();
			}
		}
		else{
			avatar.stopAnimation();
			isAnimating = false;
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