package myGameEngine;

import java.util.Random;
import java.util.UUID;

import graphicslib3D.Matrix3D;
import sage.scene.shape.Cube;

public class NPC {
	private Cube npc = new Cube();
	private Matrix3D location;
	private UUID id;
	private int spawnValue;
	private double difficulty;
	private int moveDirection;
	
	public NPC(UUID val, double d, int home){
		initLocation(home);
		this.id = val;
		this.difficulty = d;
		System.out.println(difficulty);
	}
	public void updateLocation() {
		
	}
	public double getX(){
		return location.getCol(3).getX();
	}
	public double getY(){
		return location.getCol(3).getY();
	}
	public double getZ(){
		return location.getCol(3).getZ();
	}
	public Matrix3D getLocation(){
		return location;
	}
	public void initLocation(int home){
		Matrix3D trans = npc.getLocalTranslation();
		int mix = Math.min(10, -10) + (int)(Math.abs(10 + 12) * Math.random());
		
		
		switch(home){
		case 1: spawnValue = 153;
				moveDirection = 1;
				trans.translate(153+mix, 0, 0);
				break;
		case 2: spawnValue = 203;
				moveDirection = 1;
				trans.translate(203+mix, 0, 0);
				break;
		case 3: spawnValue = 322;
				moveDirection = 1;
				trans.translate(322+mix, 0, 0);
				break;
		case 4: spawnValue = 265;
				moveDirection = 1;
				trans.translate(265+mix, 0, 0);
				break;
		}
		
		location = trans;
		npc.setLocalTranslation(trans);
		npc.updateWorldBound();
	}
	public void scale(int i, int j, int k) {
		Matrix3D scaler = npc.getLocalScale();
		scaler.scale(i, j, k);
		npc.setLocalScale(scaler);
	}
	public UUID getID(){
		return id;
	}
	public Matrix3D getScale(){
		return npc.getLocalScale();
	}
	public void moveToSpawn() {
		Matrix3D npcMove = npc.getLocalTranslation();
		npcMove.translate(0,0, -100);
		npc.setLocalTranslation(npcMove);
	}
	public void moveForward() {
		Matrix3D npcMove = npc.getLocalTranslation();
		npcMove.translate(0, 0, (1*difficulty*moveDirection));
		npc.setLocalTranslation(npcMove);
	}
	public int getSpawnValue(){
		return spawnValue;
	}
	public int getDirection(){
		return moveDirection;
	}
}