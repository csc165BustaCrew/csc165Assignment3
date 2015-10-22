package myGameEngine;

import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import net.java.games.input.Event;
import net.java.games.input.Component.Identifier.Axis;
import sage.camera.ICamera;
import sage.input.IInputManager;
import sage.input.action.AbstractInputAction;
import sage.input.action.IAction;
import sage.scene.SceneNode;
import sage.util.MathUtils;

public class Camera3Pcontroller {
	private ICamera cam;
	private SceneNode target;
	private float cameraAzimuth;
	private float cameraElevation;
	private float cameraDistanceFromTarget;
	private Point3D targetPos;
	private Vector3D worldUpVec;
	
	public Camera3Pcontroller(ICamera cam, SceneNode target, IInputManager inputMgr, String controllerName){
		this.cam = cam;
		this.target = target;
		worldUpVec = new Vector3D(0,1,0);
		cameraDistanceFromTarget = 5.0f;
		cameraAzimuth = 180;
		cameraElevation = 20.0f;
		update(0.0f);
		if(controllerName.equals("HID Keyboard Device")){
			setupInputKeyboard(inputMgr, controllerName);
		}
		if(controllerName.equals("Controller (XBOX 360 For Windows)")){
			setupInputController(inputMgr, controllerName);
		}
	}
	public void update(float time){
		updateTarget();
		updateCameraPosition();
		cam.lookAt(targetPos, worldUpVec);
	}
	private void updateTarget(){
		targetPos = new Point3D(target.getWorldTranslation().getCol(3));
	}
	private void updateCameraPosition(){
		double theta = cameraAzimuth;
		double phi = cameraElevation;
		double r = cameraDistanceFromTarget;
		
		Point3D relativePosition = MathUtils.sphericalToCartesian(theta, phi, r);
		Point3D desiredCameraLoc = relativePosition.add(targetPos);
		cam.setLocation(desiredCameraLoc);
	}
	private void setupInputController(IInputManager im, String cn){
		IAction orbitAction = new OrbitAroundActionController();
		im.associateAction(cn, Axis.RX, orbitAction, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	}
	private void setupInputKeyboard(IInputManager im, String cn){
		IAction orbitRight = new OrbitRightAction();
		im.associateAction(cn, net.java.games.input.Component.Identifier.Key.RIGHT, orbitRight, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		IAction orbitLeft = new OrbitLeftAction();
		im.associateAction(cn, net.java.games.input.Component.Identifier.Key.LEFT, orbitLeft, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	}
	private class OrbitAroundActionController extends AbstractInputAction{
		public void performAction(float time, Event evt) {
			float rotAmount;
			if(evt.getValue() < -0.2){
				rotAmount = -0.1f;
			}else{
				if(evt.getValue() > 0.2){
					rotAmount = 0.1f;
				}else{
					rotAmount = 0.0f;
				}
			}
			cameraAzimuth += rotAmount;
			cameraAzimuth = cameraAzimuth % 360;
			
		}
	}
	private class OrbitRightAction extends AbstractInputAction{
		public void performAction(float time, Event evt){
			cameraAzimuth += 0.1f;
			cameraAzimuth = cameraAzimuth % 360;
		}
	}
	private class OrbitLeftAction extends AbstractInputAction{
		public void performAction(float time, Event evt){
			cameraAzimuth += -0.1f;
			cameraAzimuth = cameraAzimuth % 360;
		}
	}
}
