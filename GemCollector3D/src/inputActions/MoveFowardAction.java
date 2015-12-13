package inputActions;



import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import net.java.games.input.Event;
import sage.input.action.AbstractInputAction;
import sage.physics.IPhysicsObject;
import sage.scene.Model3DTriMesh;
import sage.scene.SceneNode;
import sage.terrain.TerrainBlock;

public class MoveFowardAction extends AbstractInputAction {
	//private IPhysicsObject player;
	private Model3DTriMesh avatar;
//	private SceneNode avatar;
	private TerrainBlock terrain;
	private float speed = -0.01f;
	public MoveFowardAction(Model3DTriMesh avatar, TerrainBlock ter){
		this.avatar = avatar;
		this.terrain = ter;
		//this.player = obj;
	}

	@Override
	public void performAction(float time, Event e) {
		//TODO initialize this using physics
		//if(e.getValue() > 0.08f){
			Matrix3D rot = avatar.getLocalRotation();
			Vector3D dir = new Vector3D(0,0,1);
			dir = dir.mult(rot);
			dir.scale((double)speed*time);
			avatar.translate((float)dir.getX(), (float)dir.getY(), (float)dir.getZ());
			updateVerticalPosition();
		/*}
		else if(e.getValue() < -0.08f){
			Matrix3D rot = avatar.getLocalRotation();
			Vector3D dir = new Vector3D(0,0,1);
			dir = dir.mult(rot);
			dir.scale((double)-speed*time);
			avatar.translate((float)dir.getX(), (float)dir.getY(), (float)dir.getZ());
		}*/
//			avatar.stopAnimation();
	}
	
	//TODO check to see if height is the same before calc
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