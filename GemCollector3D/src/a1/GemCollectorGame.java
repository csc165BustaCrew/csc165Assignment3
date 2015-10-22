package a1;
import sage.app.BaseGame;
import sage.camera.ICamera;
import sage.camera.JOGLCamera;
import sage.display.*;
import sage.event.EventManager;
import sage.event.IEventManager;
import sage.input.*;
import sage.input.action.*;
import sage.renderer.IRenderer;
import sage.scene.*;
import sage.scene.shape.Cube;
import sage.scene.shape.Line;
import sage.scene.shape.Pyramid;
import sage.scene.shape.Rectangle;
import sage.scene.HUDString;
import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.awt.Color;
import java.awt.event.ActionEvent;

import myGameEngine.Camera3Pcontroller;
import myGameEngine.ControllerLook;
//import myGameEngine.ControllerLook;
import myGameEngine.LookLeftAction;
import myGameEngine.LookRightAction;
import myGameEngine.MoveForwardAction;
import myGameEngine.MyCollisionController;
import myGameEngine.MyDisplaySystem;
import myGameEngine.MoveBackwardAction;
import myGameEngine.ControllerMove;
import myGameEngine.CrashEvent;
import myGameEngine.CustomGem;
import myGameEngine.MyPyramid;
import myGameEngine.MyRotationController;
import net.java.games.input.*;


public class GemCollectorGame extends BaseGame implements IAction{
	private int scorePlayer1 = 0;
	private int scorePlayer2 = 0;
	private HUDString scoreStringPlayer1;
	private HUDString scoreStringPlayer2;
	private float time = 0;
	private int updateCount = 0;
	//private HUDString scoreString;
	private HUDString timeString;
	private int crashCount = 0;
	private IRenderer renderer;
	MyCollisionController mcc = new MyCollisionController();
	MyRotationController mrc = new MyRotationController();
	IInputManager im;
	String gpName;
	String KeyboardName;
	IEventManager eventMgr = EventManager.getInstance();
	IDisplaySystem display;
	ICamera cameraP1;
	ICamera cameraP2;
	Camera3Pcontroller ccP1;
	Camera3Pcontroller ccP2;
	Pyramid Player1Avatar;
	Pyramid Player2Avatar;
	Matrix3D worldOrigin = new Matrix3D();
	
	Group treasure;
	Group beacon;
	
	protected void initGame(){
		initGameObjects();
		initPlayers();
		initInputs();
		
		if(KeyboardName == null){
			System.out.println("Keyboard not found");
		}else{
			ccP1 = new Camera3Pcontroller(cameraP1, Player1Avatar, im, KeyboardName);
		}
		if(gpName == null){
			System.out.println("Game pad not found");
		}else{
			ccP2 = new Camera3Pcontroller(cameraP2, Player2Avatar, im, gpName);
		}
		super.update((float) 0.0);
	}
	
	private IDisplaySystem createDisplaySystem(){
		IDisplaySystem display = new MyDisplaySystem(1920, 1200, 24, 20, true, "sage.renderer.jogl.JOGLRenderer");
		System.out.print("Waiting for display creation...");
		int count = 0;
		while(!display.isCreated()){
			try{
				Thread.sleep(10);
			}catch(InterruptedException e){
				throw new RuntimeException("Display creation interrupted");
			}
			count++;
			System.out.println("+");
			if(count > 2000){
				throw new RuntimeException("Unable to create display");
			}
		}
		System.out.println();
		return display;
	}
	
	protected void initInputs(){
		//INPUTS NOT WORKING IN WINDOWS 10
		im = getInputManager();	
		KeyboardName = im.getKeyboardName();
		gpName = im.getFirstGamepadName();
		im.printControllers(true);
		RunAction run = new RunAction(1);
		IAction lookRightAction = new LookRightAction(Player1Avatar);
		IAction lookLeftAction = new LookLeftAction(Player1Avatar);
		IAction moveForwardAction = new MoveForwardAction(Player1Avatar);
		IAction moveBackwardAction = new MoveBackwardAction(Player1Avatar);
		
		IAction controllerLookAction = new ControllerLook(Player2Avatar);
		IAction controllerMoveAction = new ControllerMove(Player2Avatar);
		
		if(KeyboardName != null){
			im.associateAction(KeyboardName, net.java.games.input.Component.Identifier.Key.D, lookRightAction, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateAction(KeyboardName, net.java.games.input.Component.Identifier.Key.A, lookLeftAction, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateAction(KeyboardName, net.java.games.input.Component.Identifier.Key.W, moveForwardAction, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateAction(KeyboardName, net.java.games.input.Component.Identifier.Key.S, moveBackwardAction, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		}else{
			System.out.println("Keyboard not found!");
		}
		if(gpName != null){
			im.associateAction(gpName, net.java.games.input.Component.Identifier.Axis.Y, controllerMoveAction, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateAction(gpName, net.java.games.input.Component.Identifier.Axis.X, controllerLookAction, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		}else{
			System.out.println("Gamepad not found!");
		}
	}
	
	protected void initGameObjects(){
		display = createDisplaySystem();
		display.setTitle("GemCollector3D");
		renderer = display.getRenderer();	
		
		Rectangle groundPlane = new Rectangle();
		Matrix3D groundPlaneM = groundPlane.getLocalScale();
		groundPlaneM.scale(70, 70, 0);
		groundPlane.setLocalScale(groundPlaneM);
		groundPlaneM = groundPlane.getLocalTranslation();
		groundPlaneM.translate(33, 0, 33);
		groundPlane.setLocalTranslation(groundPlaneM);
		Matrix3D groundPlaneR = groundPlane.getLocalRotation();
		groundPlaneR.rotateX(-90);
		groundPlane.setLocalRotation(groundPlaneR);
		addGameWorldObject(groundPlane);
		
		
		Random r = new Random();
		treasure = new Group("Cube");
		
		for(int i = 0; i++ < 10;){
			Cube cub = new Cube("Cube");
			Matrix3D cubM = cub.getLocalScale();
			cubM.scale(1, 1, r.nextInt((3 - 2)+2));
			cub.setLocalScale(cubM);
			cubM = cub.getLocalTranslation();
			
			cubM.translate(r.nextInt(50), 0, r.nextInt(50));
			cub.setLocalTranslation(cubM);
			treasure.addChild(cub);
		}
		addGameWorldObject(treasure);
		mrc.addControlledNode(treasure);
		treasure.addController(mrc);
		
		MyPyramid aPyr = new MyPyramid();
		Matrix3D pyrM = aPyr.getLocalTranslation();
		pyrM.translate(5, 2, 5);
		aPyr.setLocalTranslation(pyrM);
		
		beacon = new Group("Pyramid0");
		
		eventMgr.addListener(aPyr, CrashEvent.class);
		beacon.addChild(aPyr);
		addGameWorldObject(beacon);
		mcc.addControlledNode(beacon);
		beacon.addController(mcc);
		
		Point3D origin = new Point3D(0,0,0);
		Point3D xEnd = new Point3D(10,0,0);
		Point3D yEnd = new Point3D(0,10,0);
		Point3D zEnd = new Point3D(0,0,10);
		Line xAxis = new Line(origin, xEnd, Color.red, 2);
		Line yAxis = new Line(origin, yEnd, Color.green, 2);
		Line zAxis = new Line(origin, zEnd, Color.blue, 2);
		addGameWorldObject(xAxis);
		addGameWorldObject(yAxis);
		addGameWorldObject(zAxis);
	}
	
	private void initPlayers(){
		Matrix3D pyrM;
		Matrix3D pyrR;
		//Player 1 Avatar creation
		Player1Avatar = new Pyramid();
		pyrM = Player1Avatar.getLocalTranslation();
		pyrM.translate(1, 1, 1);
		Player1Avatar.setLocalTranslation(pyrM);
		pyrR = new Matrix3D();
		pyrR.rotateY(45.0);
		Player1Avatar.setLocalRotation(pyrR);
		addGameWorldObject(Player1Avatar);
		
		cameraP1 = new JOGLCamera(renderer);
		cameraP1.setPerspectiveFrustum(60, 2, 1, 1000);
		cameraP1.setViewport(0.0, 1.0, 0.0, 0.50);
				
		//Player 2 Avatar creation
		Player2Avatar = new Pyramid();
		pyrM = Player2Avatar.getLocalTranslation();
		pyrM.translate(2, 1, 1);
		Player2Avatar.setLocalTranslation(pyrM);
		pyrR = new Matrix3D();
		pyrR.rotateY(45.0);
		Player2Avatar.setLocalRotation(pyrR);
		addGameWorldObject(Player2Avatar);
		
		cameraP2 = new JOGLCamera(renderer);
		cameraP2.setPerspectiveFrustum(60, 2, 1, 1000);
		cameraP2.setViewport(0.0, 1.0, 0.50, 1.0);
		
		createPlayerHUDS();
	}
	private void createPlayerHUDS(){
		HUDString player1ID = new HUDString("Player1");
		scoreStringPlayer1 = new HUDString("Score: ");
		scoreStringPlayer1.setText("Score: " + scorePlayer1);
		scoreStringPlayer1.setLocation(0.0, 0.1);
		player1ID.setName("Player1ID");
		player1ID.setLocation(0.0, 0.95);
		player1ID.setRenderMode(sage.scene.SceneNode.RENDER_MODE.ORTHO);
		player1ID.setColor(Color.red);
		player1ID.setCullMode(sage.scene.SceneNode.CULL_MODE.NEVER);
		cameraP1.addToHUD(scoreStringPlayer1);
		cameraP1.addToHUD(player1ID);
		
		HUDString player2ID = new HUDString("Player2");
		scoreStringPlayer2 = new HUDString("Score: ");
		scoreStringPlayer2.setText("Score: " + scorePlayer2);
		scoreStringPlayer2.setLocation(0.0, 0.1);
		player2ID.setName("Player2ID");
		player2ID.setLocation(0.0, 0.95);
		player2ID.setRenderMode(sage.scene.SceneNode.RENDER_MODE.ORTHO);
		player2ID.setColor(Color.yellow);
		player2ID.setCullMode(sage.scene.SceneNode.CULL_MODE.NEVER);
		cameraP2.addToHUD(scoreStringPlayer2);
		cameraP2.addToHUD(player2ID);
	}
	
	public void update(float ms){
		Iterator sceneNodes1 = treasure.getChildren();

		updateCount++;
		while(sceneNodes1.hasNext()){
			SceneNode element = (SceneNode)sceneNodes1.next();
			if(element.getName().equals("Rectangle0")){
				if(!(element.getWorldBound().intersects(Player1Avatar.getWorldBound()))){
					Player1Avatar.setLocalTranslation(worldOrigin);
				}
			}
			if(element.getName().equals("Cube")){
				if(element.getWorldBound().intersects(Player1Avatar.getWorldBound())){
					crashCount++;
					scorePlayer1++;
					CrashEvent newCrash = new CrashEvent(crashCount);
					mcc.collisionTriger(newCrash);
					element.setLocalTranslation(worldOrigin);
					scoreStringPlayer1.setText("Score: "+scorePlayer1);
				}
				if(element.getWorldBound().intersects(Player2Avatar.getWorldBound())){
					crashCount++;
					scorePlayer2++;
					CrashEvent newCrash = new CrashEvent(crashCount);
					mcc.collisionTriger(newCrash);
					element.setLocalTranslation(worldOrigin);
					scoreStringPlayer2.setText("Score: "+scorePlayer2);
				}
				
				if(element.getName().equals("Pyramid0")){
					if(updateCount % 1000 == 0){
						MyPyramid myPyr = (MyPyramid)element;
						myPyr.setDefaultColor();
					}
				}
			}
		}
		DecimalFormat df = new DecimalFormat("0.0");
		time += ms;
		if(ccP1 == null){
			
		}else{
			ccP1.update(ms);
		}
		if(ccP2 == null){
			
		}else{
			ccP2.update(ms);
		}
		super.update(ms);
	}

	@Override
	public void performAction(float arg0, Event arg1) {
		this.shutdown();
	}
	protected void render(){
		renderer.setCamera(cameraP1);
		super.render();
		
		renderer.setCamera(cameraP2);
		super.render();
	}
}
