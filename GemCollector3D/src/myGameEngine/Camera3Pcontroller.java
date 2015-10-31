package myGameEngine;

import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Component.Identifier.Axis;
import net.java.games.input.Event;
import sage.camera.ICamera;
import sage.input.IInputManager;
import sage.input.IInputManager.INPUT_ACTION_TYPE;
import sage.input.action.AbstractInputAction;
import sage.input.action.IAction;
import sage.scene.SceneNode;
import sage.util.MathUtils;

public class Camera3PController {
	private ICamera cam;
	private SceneNode target;
	private float tempAzimuth;
	private float cameraAzimuth;
	private float cameraElevation;
	private float defaultCameraElevation;
	private float cameraDistanceFromTarget;
	private Point3D targetPos;
	private Vector3D worldUpVec;
	private final INPUT_ACTION_TYPE REPEAT_WHILE_DOWN = IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN;
	private final INPUT_ACTION_TYPE ON_PRESS_ONLY = IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY;
	private boolean reset = false;
	

	public Camera3PController(ICamera cam, SceneNode target, IInputManager inputMgr, String controllerName){
		this.cam = cam;
		this.target = target;
		worldUpVec = new Vector3D(0, 1, 0);
		cameraDistanceFromTarget = 10.0f;
		cameraAzimuth = 180;
		cameraElevation = 20.0f;
		defaultCameraElevation = cameraElevation;
		update(0.0f);
		setupInput(inputMgr, controllerName);
	}

	public ICamera getCamera(){
		return cam;
	}
	private void setupInput(IInputManager inputMgr, String controllerName) {
		IAction orbitAction = new OrbitAroundAction();
		IAction elevAction = new ElevationAction();
		IAction radAction = new RadiusAction();
		IAction resetAction = new Reset();
	
		inputMgr.associateAction(controllerName, Axis.RX, orbitAction, REPEAT_WHILE_DOWN);
		inputMgr.associateAction(controllerName, Axis.RY, elevAction, REPEAT_WHILE_DOWN);
		inputMgr.associateAction(controllerName, Identifier.Button._0, resetAction, ON_PRESS_ONLY);		
		inputMgr.associateAction(controllerName, Axis.Z, radAction, REPEAT_WHILE_DOWN);
	}

	public void update(float f) {
		upateTarget();
		updateCameraPosition();
		cam.lookAt(targetPos, worldUpVec);
	}

	private void updateCameraPosition() {
		double theta = cameraAzimuth;
		double phi = cameraElevation;
		double r = cameraDistanceFromTarget;
		
		//calculate new camera position in Cartesian coords
		Point3D relativePosition = MathUtils.sphericalToCartesian(theta, phi, r);
		Point3D desiredCameraLoc = relativePosition.add(targetPos);
		cam.setLocation(desiredCameraLoc);
	}

	private void upateTarget() {
		targetPos = new Point3D(target.getWorldTranslation().getCol(3));
	}
	
	private class Reset extends AbstractInputAction{

		@Override
		public void performAction(float arg0, Event arg1) {
			if(!reset){
				reset = true;
				tempAzimuth = cameraAzimuth;
			}
			else{
				reset = false;
				cameraAzimuth = tempAzimuth;
				cameraElevation = defaultCameraElevation;
			}
		}
		
	}
	
	private class OrbitAroundAction extends AbstractInputAction{

		@Override
		public void performAction(float time, Event event) {
			float rotAmount;
			if(event.getValue() < -0.2){
				rotAmount = -0.2f;
			}
			else{
				if(event.getValue() > 0.2){
					rotAmount = 0.2f;
				}
				else{
					rotAmount = 0.0f;
				}
			}
			cameraAzimuth += rotAmount;
			cameraAzimuth = cameraAzimuth % 360;
			if(!reset){
				target.rotate(rotAmount, worldUpVec);
			}
			else{
				
			}
		}
	}
	
	private class ElevationAction extends AbstractInputAction{
			private float increment = .1f;
			private float deadZone = 0.3f;
		@Override
		public void performAction(float arg0, Event event) {
				if(event.getValue() > deadZone){
					cameraElevation += increment;
				}
				else if(event.getValue() < -deadZone){
					cameraElevation -= increment;
				}
		}
		
	}
	
	private class RadiusAction extends AbstractInputAction{
		private float increment = .05f;
		@Override
		public void performAction(float arg0, Event event) {
			if(event.getValue() > 0){
				cameraDistanceFromTarget += increment; 
			}
			else{
				cameraDistanceFromTarget -= increment;
			}
		}
		
	}
	
}
