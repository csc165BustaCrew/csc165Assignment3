package game;


import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.script.*;

import com.sun.xml.internal.ws.api.pipe.Engine;

import bullet.CubeBullet;
import events.CrashEvent;
import gameObjects.Arrow;
import gameObjects.Gem;
import gameObjects.MyCube;
import gameObjects.MyCylinder;
import gameObjects.MyPyramid;
import gameObjects.Plane;
import sage.input.*;
import sage.input.IInputManager.INPUT_ACTION_TYPE;
import sage.input.action.IAction;
import sage.model.loader.OBJLoader;
import sage.networking.IGameConnection.ProtocolType;
import sage.physics.IPhysicsEngine;
import sage.physics.IPhysicsObject;
import sage.physics.PhysicsEngineFactory;
import sage.renderer.IRenderer;
import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import inputActions.MoveBackwardAction;
import inputActions.MoveFowardAction;
import inputActions.MoveOnXAxisAction;
import inputActions.MoveOnYAxisAction;
import inputActions.PitchNegAction;
import inputActions.PitchPosAction;
import inputActions.QuitGameAction;
import inputActions.StrafeLeftAction;
import inputActions.StrafeRightAction;
import inputActions.YawNegAction;
import inputActions.YawPosAction;
import myGameEngine.Camera3Pcontroller;
import myGameEngine.Camera3PMouseKeyboard;
import myGameEngine.MyDisplaySystem;
import myGameEngine.MySpinController;
import myGameEngine.MyTranslateController;
import myGameEngine.MyTerrain;
import sage.display.IDisplaySystem;
import sage.event.EventManager;
import sage.event.IEventManager;
import sage.scene.shape.Cube;
import sage.scene.shape.Line;
import sage.scene.shape.Pyramid;
import sage.terrain.TerrainBlock;
import sage.texture.Texture;
import sage.texture.TextureManager;
import sage.scene.Group;
import sage.scene.HUDString;
import sage.scene.SceneNode;
import sage.scene.SceneNode.CULL_MODE;
import sage.scene.SkyBox;
import sage.scene.TriMesh;
import sage.app.BaseGame;
import sage.camera.ICamera;
import sage.camera.JOGLCamera;
import net.java.games.input.Component.Identifier;
import networking.GameClient;

public class GemCollector extends BaseGame {
	private final INPUT_ACTION_TYPE ON_PRESS_ONLY = IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY;
	private final INPUT_ACTION_TYPE REPEAT_WHILE_DOWN = IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN;
	
	private boolean gameOver = false;
	private boolean player1Won = false;
	
	private int player1Score = 0;
	private int player1HP = 3;
	
	CubeBullet player1Bullet;
	
	private HUDString player1ScoreString;
	private HUDString player1HPString;
	private HUDString player1GameOverString;
	
	private MyTerrain tb = new MyTerrain();
	private TerrainBlock worldMap;
	private Plane ground;
	private Plane backWall;
	private Plane frontWall;
	private Plane leftWall;
	private Plane rightWall;
	private SkyBox skybox;
	private IPhysicsEngine physicsEngine;
	private IPhysicsObject groundPlaneP;
	private IPhysicsObject player;
	private IPhysicsObject truckP;
	private IPhysicsObject truckPArray[] = new IPhysicsObject[5];
	private IPhysicsObject carP;
	private IPhysicsObject carPArray[] = new IPhysicsObject[5];
	
	private Arrow arrow;
	private TriMesh cubeList[] = new TriMesh[5];
	private TriMesh cylinderList[] = new TriMesh[5];
	private MyPyramid pyramidList[] = new MyPyramid[5]; 
	
	private TriMesh player1;
	private IDisplaySystem display;
	private ICamera camera1;
	private Camera3PMouseKeyboard cam1Controller;
	private IEventManager eventMgr;
	private IRenderer renderer;
	
	private Group group1;
	private Group group2;
	
	private MySpinController spinController;
	
	private GameClient gameClient;
	private String serverAddr;
	private int serverPort;
	private ProtocolType serverProtocol;
	
	private boolean scriptCheck = true;
	ScriptEngineManager factory = new ScriptEngineManager();
	private ScriptEngine engine = factory.getEngineByName("js");
	
	private OBJLoader objLoader = new OBJLoader();
	
	
	public GemCollector(String serverAddr, int serverPort){
		super();
		this.serverAddr = serverAddr;
		this.serverPort = serverPort;
		this.serverProtocol = ProtocolType.TCP;
	}
	
	//TODO Add these values to script
	float ballMass = 1.0f;
	float up[] = {0,1,0};
	
	
	public void initGame(){
		try{
			this.gameClient = new GameClient(InetAddress.getByName(serverAddr), serverPort, serverProtocol, this);
		} catch (UnknownHostException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(gameClient != null) {
			gameClient.sendJoinMessage();
		}
		display = getDisplaySystem();
		eventMgr = EventManager.getInstance();
		
		display.setTitle("Super Duper Gem Collector X");
		
		renderer = getDisplaySystem().getRenderer();
		
		if(scriptCheck){
			initPhysics();
			initSkyBox();
			initTerrain();
			initGameObjects();
			initPlayers();
			initScript();
			initInput();
			initEvents();
		}else{
			initPhysics();
			initSkyBox();
			initTerrain();
			initGameObjects();
			initPlayers();
			initWorldAxis();
			initInput();
			initEvents();
		}
		//HUD
		player1ScoreString = new HUDString("Score= " + player1Score);
		player1ScoreString.setLocation(0.9, 0.05);
		player1ScoreString.setColor(Color.GREEN);
		camera1.addToHUD(player1ScoreString);
		
		player1HPString = new HUDString("HP: " + player1HP);
		camera1.addToHUD(player1HPString);
	}
	
	private void initScript() {
		String scriptName = "src" + File.separator + "jscripts" + File.separator + "CreateWorld.js";
		List<ScriptEngineFactory> list = factory.getEngineFactories();
		File scriptFile = new File(scriptName);
		
		try{
			FileReader fileReader = new FileReader(scriptFile);
			engine.eval(fileReader);
			fileReader.close();
		}catch (FileNotFoundException e1){
			System.out.println(scriptName + " not found "+ e1);
		}catch (IOException e2){
			System.out.println("IO issue detected "+ scriptName + e2);
		}catch (ScriptException e3){
			System.out.println("ScriptException in " + scriptName + e3);
		}catch (NullPointerException e4){
			System.out.println("Null pointer in" + scriptName + e4);
		}
		addGameWorldObject((SceneNode) engine.get("worldAxis"));
	}

	public void start(){
		super.start();
	}
	
	private void initPhysics(){
		String engine = "sage.physics.JBullet.JBulletPhysicsEngine";
		physicsEngine = PhysicsEngineFactory.createPhysicsEngine(engine);
		physicsEngine.initSystem();
		float[] gravity = {0, -10f, 0};
		physicsEngine.setGravity(gravity);
	}
	
	protected void initSystem(){
		IDisplaySystem display = createDisplaySystem();
		setDisplaySystem(display);
		
		IInputManager inputManager = new InputManager();
		setInputManager(inputManager);
		
		ArrayList<SceneNode> gameWorld = new ArrayList<SceneNode>();
		setGameWorld(gameWorld);
	}
	
	private IDisplaySystem createDisplaySystem() {
		IDisplaySystem display = new MyDisplaySystem(800, 600, 32, 60, false, "sage.renderer.jogl.JOGLRenderer");
		System.out.print("\nWaiting for display creation");
		int count = 0;
		
		while(!display.isCreated()){
			try{
				Thread.sleep(10);
			} catch(InterruptedException e){
				throw new RuntimeException("Diplay creation interrupted");
			}
			
			count++;
			System.out.print("+");
			if(count % 80 == 0){
				System.out.println();
			}
			
			if(count > 2000){
				throw new RuntimeException("Unable to create display");
			}
		}
		System.out.println("\nDone");
		return display;
	}

	private void initPlayers(){
		player1 = objLoader.loadModel("src/Models/chicken.obj");
		player1.updateLocalBound();
		//player1.rotate(180, new Vector3D(0,1,0));
		player1.translate(30, 15, 30);
		player1.setWorldTranslation(player1.getLocalTranslation());
		//player1.updateGeometricState(1.0f, true);
		player = physicsEngine.addSphereObject(physicsEngine.nextUID(), ballMass, player1.getWorldTransform().getValues(),1.0f);
		player.setBounciness(0.0f);
		player1.setPhysicsObject(player);
		addGameWorldObject(player1);
		
		camera1 = new JOGLCamera(renderer);
		camera1.setPerspectiveFrustum(60, 1, 1, 1000);
		camera1.setViewport(0.0, 1.0, 0.0, 1);
	}
	
	public void initPlayerLocation(Vector3D loc){
		player1.translate((float)loc.getX(), (float)loc.getY(), (float)loc.getZ());
	}
	
	private void initSkyBox(){
		skybox = new SkyBox("SkyBox", 20.0f, 20.0f, 20.0f);
		Texture nTexture = TextureManager.loadTexture2D("src/Textures/SkyBox/northSkybox.bmp");
		Texture sTexture = TextureManager.loadTexture2D("src/Textures/SkyBox/southSkybox.bmp");
		Texture eTexture = TextureManager.loadTexture2D("src/Textures/SkyBox/eastSkybox.bmp");
		Texture wTexture = TextureManager.loadTexture2D("src/Textures/SkyBox/westSkybox.bmp");
		Texture uTexture = TextureManager.loadTexture2D("src/Textures/SkyBox/upSkybox.bmp");
		skybox.setTexture(SkyBox.Face.North, nTexture);
		skybox.setTexture(SkyBox.Face.South, sTexture);
		skybox.setTexture(SkyBox.Face.East, eTexture);
		skybox.setTexture(SkyBox.Face.West, wTexture);
		skybox.setTexture(SkyBox.Face.Up, uTexture);
		addGameWorldObject(skybox);
	}
	
	private void initTerrain(){
		worldMap = tb.initTerrain(display);
		//worldMap.setCullMode(CULL_MODE.NEVER);
		//worldMap.updateLocalBound();
		//worldMap.setShowBound(true);

		groundPlaneP = physicsEngine.addStaticPlaneObject(physicsEngine.nextUID(), worldMap.getWorldTransform().getValues(), up, 12.0f);
		groundPlaneP.setBounciness(1.0f);
		worldMap.setPhysicsObject(groundPlaneP);
		addGameWorldObject(worldMap);
	}
	
	private void initGameObjects(){
		//group1 = new Group();
		//group2 = new Group();
		//group1.addChild(group2);
		arrow = new Arrow();
		addGameWorldObject(arrow);
		Matrix3D tempM;
		TriMesh otherCube;
		Random rand = new Random();
		for(int i = 0; i < cubeList.length; i++){
			otherCube = objLoader.loadModel("src/Models/car.obj");
			tempM = otherCube.getLocalTranslation();
			if(i > 10){
				tempM.translate(rand.nextInt(30)*1, 30, rand.nextInt(30)*1);
			}
			tempM.translate(rand.nextInt(30)+1, 30, rand.nextInt(30)+1);
			otherCube.setLocalTranslation(tempM);
			otherCube.updateWorldBound();
			otherCube.updateGeometricState(1.0f, true);
			float angVeloc[] = {rand.nextInt(10), rand.nextInt(10), rand.nextInt(10)};
			carPArray[i] = physicsEngine.addSphereObject(physicsEngine.nextUID(), ballMass, otherCube.getWorldTransform().getValues(),1.0f);
			carPArray[i].setBounciness(1.0f);
			carPArray[i].setAngularVelocity(angVeloc);
			otherCube.setPhysicsObject(carPArray[i]);
			addGameWorldObject(otherCube);
			//group2.addChild(otherCube);
			cubeList[i] = otherCube;
		}
		
		TriMesh otherCylinder;
		for(int i = 0; i < cylinderList.length; i++){
			otherCylinder = objLoader.loadModel("src/Models/truck.obj");
			tempM = otherCylinder.getLocalTranslation();
			//if(i > 10){
			//	tempM.translate(rand.nextInt(30)*1, 30, rand.nextInt(30)*1);
			//}
			tempM.translate(rand.nextInt(90), 90, rand.nextInt(90));
			otherCylinder.setLocalTranslation(tempM);
			otherCylinder.updateWorldBound();
			truckPArray[i] = physicsEngine.addSphereObject(physicsEngine.nextUID(), ballMass, otherCylinder.getWorldTransform().getValues(),1.0f);
			truckPArray[i].setBounciness(0.5f);
			otherCylinder.setPhysicsObject(truckPArray[i]);
			addGameWorldObject(otherCylinder);
			//group2.addChild(otherCylinder);
			cylinderList[i] = otherCylinder;
		}
		
		MyPyramid otherPyramid;
		for(int i = 0; i < pyramidList.length; i++){
			otherPyramid = new MyPyramid();
			tempM = otherPyramid.getLocalTranslation();
			//if(i > 10){
			//	tempM.translate(rand.nextInt(30)*-1, 0, rand.nextInt(45)*-1);
			//}
			//tempM.translate(rand.nextInt(30), 0, rand.nextInt(45)*-1);
			if(i > 10){
				tempM.translate(rand.nextInt(30)*1, 30, rand.nextInt(30)*1);
			}
			tempM.translate(rand.nextInt(30)+1, 30, rand.nextInt(30)+1);
			otherPyramid.setLocalTranslation(tempM);
			otherPyramid.updateWorldBound();
			addGameWorldObject(otherPyramid);
			//group2.addChild(otherPyramid);
			pyramidList[i] = otherPyramid;
		}
		
		//MyTranslateController transController = new MyTranslateController();
		//spinController = new MySpinController();
		
		//group1.addController(transController);
		//transController.addControlledNode(group1);
		
		//group2.addController(spinController);
		//spinController.addControlledNode(group2);
		
		
		//ballP = physicsEngine.addSphereObject(physicsEngine.nextUID(), ballMass, group1.getWorldTransform().getValues(),1.0f);
		//ballP.setBounciness(1.0f);
		//group1.setPhysicsObject(ballP);
		
		//addGameWorldObject(group1);
		super.update(0);
	}
	
	//Adds world axis to the game
	public void initWorldAxis(){
		Point3D origin = new Point3D(0,0,0);
		Point3D xEnd = new Point3D(100,0,0);
		Point3D yEnd = new Point3D(0,100,0);
		Point3D zEnd = new Point3D(0,0,100);
		
		Line xAxis = new Line(origin, xEnd, Color.red, 2);
		Line yAxis = new Line(origin, yEnd, Color.green, 2);
		Line zAxis = new Line(origin, zEnd, Color.blue, 2);
		addGameWorldObject(xAxis);
		addGameWorldObject(yAxis);
		addGameWorldObject(zAxis);
	}
	
	//Initializes controls
	public void initInput(){
		IInputManager im = getInputManager();
		String gpName = im.getFirstGamepadName();
		String kbName = im.getKeyboardName();
	
		IAction quitGame = new QuitGameAction(this);
		
		MoveFowardAction mvFoward = new MoveFowardAction(player1, worldMap);
		MoveBackwardAction mvBackward = new MoveBackwardAction(player1, worldMap);
		StrafeLeftAction strafeLeftAction = new StrafeLeftAction(player1, worldMap);
		StrafeRightAction strafeRightAction = new StrafeRightAction(player1, worldMap);
		
		YawPosAction yawPosAction = new YawPosAction(player1);
		YawNegAction yawNegAction = new YawNegAction(player1);
		PitchPosAction pitchPosAction = new PitchPosAction(player1);
		PitchNegAction pitchNegAction = new PitchNegAction(player1);
		
		im.associateAction(kbName, Identifier.Key.W, mvFoward, REPEAT_WHILE_DOWN);
		im.associateAction(kbName, Identifier.Key.S, mvBackward, REPEAT_WHILE_DOWN); 
		im.associateAction(kbName, Identifier.Key.A, strafeLeftAction, REPEAT_WHILE_DOWN); 
		im.associateAction(kbName, Identifier.Key.D, strafeRightAction, REPEAT_WHILE_DOWN); 

		im.associateAction(kbName, Identifier.Key.LEFT, yawPosAction, REPEAT_WHILE_DOWN);
		im.associateAction(kbName, Identifier.Key.RIGHT, yawNegAction, REPEAT_WHILE_DOWN);
		im.associateAction(kbName, Identifier.Key.UP, pitchPosAction, REPEAT_WHILE_DOWN);
		im.associateAction(kbName, Identifier.Key.DOWN, pitchNegAction, REPEAT_WHILE_DOWN);
		im.associateAction(kbName, Identifier.Key.ESCAPE, quitGame, ON_PRESS_ONLY);

		String mouseName = im.getMouseName();
		cam1Controller = new Camera3PMouseKeyboard(camera1, player1, im, mouseName);
		
		player1Bullet = new CubeBullet(this.getRenderer(), player1);
		im.associateAction(kbName, Identifier.Key.F, player1Bullet, ON_PRESS_ONLY);
		addGameWorldObject(player1Bullet.getBullet());
		
//		player2Bullet = new CubeBullet(this.getRenderer(), cam2Controller.getCamera(), player2);
//		im.associateAction(gpName, Identifier.Button._2, player2Bullet, ON_PRESS_ONLY);
//		addGameWorldObject(player2Bullet.getBullet());
		
	}
	
	private void initEvents(){
		eventMgr.addListener(ground, CrashEvent.class);
		eventMgr.addListener(backWall, CrashEvent.class);
		eventMgr.addListener(frontWall, CrashEvent.class);
		eventMgr.addListener(leftWall, CrashEvent.class);
		eventMgr.addListener(rightWall, CrashEvent.class);

	}
	
	public void update(float elapsedTimeMS){
		super.update(elapsedTimeMS);
		cam1Controller.update(elapsedTimeMS);
		
		if(gameClient != null){
			gameClient.sendUpdate(getPlayerPosition());
			gameClient.processPackets();
		}
		physicsEngine.update(20.0f);
		Matrix3D mat;
		Vector3D translateVec;
		for (SceneNode s : getGameWorld()){
			
			if (s.getPhysicsObject() != null){
				if(s.getWorldBound().intersects(player1.getWorldBound()) && (s.getName().equals("src/Models/car.obj") || s.getName().equals("src/Models/truck.obj"))){
					p1Scored();
					
					s.setLocalTranslation(new Matrix3D());
					s.translate(0, 10, 0);
				}
				if(s.getName().equals(player1.getName())){
					mat = s.getLocalTranslation();
					translateVec = mat.getCol(3);
					s.getLocalTranslation().setCol(3, translateVec);
					
				}else{
					mat = new Matrix3D(s.getPhysicsObject().getTransform());
					translateVec = mat.getCol(3);
					s.getLocalTranslation().setCol(3,translateVec);
				}
			}
		}
		player1ScoreString.setText("Score = " + player1Score);
		player1HPString.setText("HP: " + player1HP);
		
		Matrix3D camTranslation = new Matrix3D();
		camTranslation.translate(camera1.getLocation().getX(), camera1.getLocation().getY(), camera1.getLocation().getZ());
		skybox.setLocalTranslation(camTranslation);
	}
		
	private void p1Scored(){
		player1Score++;
		//spinController.spin();
	}
	
	protected void render(){
		renderer.setCamera(camera1);
		super.render();
	}
	
	protected void shutdown(){
		super.shutdown();
		if(gameClient != null){
			gameClient.sendByeMessage();
			try{
				gameClient.shutdown();
			} catch(IOException e){
				e.printStackTrace();
			}
		}
		display.close();
	}
	
	public void addGhost(SceneNode ghost){
		System.out.println("ghost added");
		ghost.updateWorldBound();
		addGameWorldObject(ghost);
	}
	
	public void removeGhost(SceneNode ghost){
		removeGameWorldObject(ghost);
	}
	
	public void setIsConnected(boolean b) {
		// TODO Auto-generated method stub
	}
	
	public Matrix3D getPlayerPosition() {
		Matrix3D playerM = new Matrix3D();
		playerM.concatenate(player1.getLocalTranslation());
		playerM.concatenate(player1.getLocalRotation());
		return playerM;
	}

}