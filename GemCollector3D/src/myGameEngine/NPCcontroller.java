package myGameEngine;

import java.util.UUID;

import networking.GameServerTCP;
import graphicslib3D.Matrix3D;
import graphicslib3D.Vector3D;
import sage.ai.behaviortrees.BTCompositeType;
import sage.ai.behaviortrees.BTSequence;
import sage.ai.behaviortrees.BehaviorTree;

public class NPCcontroller {
	BehaviorTree bt0 = new BehaviorTree(BTCompositeType.SELECTOR);
	BehaviorTree bt1 = new BehaviorTree(BTCompositeType.SELECTOR);
	BehaviorTree bt2 = new BehaviorTree(BTCompositeType.SELECTOR);
	BehaviorTree bt3 = new BehaviorTree(BTCompositeType.SELECTOR);
	BehaviorTree bt4 = new BehaviorTree(BTCompositeType.SELECTOR);
	private long startTime;
	private long lastUpdateTime;
	private NPC[] NPClist = new NPC[5];
	private int numNPCs;
	
	public NPCcontroller(){
		System.out.println("NPCcontroller constructor");
		startTime = System.nanoTime();
		lastUpdateTime = startTime;
	}
	
	public void updateNPCs(){
		for(int i=0; i<numNPCs; i++){
			NPClist[i].updateLocation();
		}
	}

	public void setupNPC(){
		System.out.println("setupNPC method");
		NPClist[0] = new NPC(UUID.randomUUID(), 10, 1);
		NPClist[1] = new NPC(UUID.randomUUID(), 10, 1);
		NPClist[2] = new NPC(UUID.randomUUID(), 10, 1);
		NPClist[3] = new NPC(UUID.randomUUID(), 10, 3);
		NPClist[4] = new NPC(UUID.randomUUID(), 10, 3);
		setupBehaviorTree(NPClist[0], bt0);
		setupBehaviorTree(NPClist[1], bt1);
		setupBehaviorTree(NPClist[2], bt2);
		setupBehaviorTree(NPClist[3], bt3);
		setupBehaviorTree(NPClist[4], bt4);
	}

	public void npcLoop(GameServerTCP gameServer){
		while(true){
			long frameStartTime = System.nanoTime();
			float elapMilSecs = (frameStartTime-lastUpdateTime)/(1000000.0f);
			if(elapMilSecs >= 50.0f){
				lastUpdateTime = frameStartTime;
				updateNPCs();
				bt0.update(elapMilSecs);
				bt1.update(elapMilSecs);
				bt2.update(elapMilSecs);
				bt3.update(elapMilSecs);
				bt4.update(elapMilSecs);
				gameServer.sendNPCinfo();
			}
			//Thread.yield();
		}
	}

	public void setupBehaviorTree(NPC npc, BehaviorTree bt) {
		bt.insertAtRoot(new BTSequence(10));
		bt.insertAtRoot(new BTSequence(20));
		
		bt.insert(10, new OnTerrain(this, npc, false));
		bt.insert(10, new MoveForward(npc));
		bt.insert(20, new OffTerrain(this, npc, false));
		bt.insert(20, new MoveToSpawn(npc));
		
		System.out.println("finished behavior tree");
	}

	public boolean getNearFlag(Vector3D npcP, Vector3D playerP) {
		if (playerP.getX() - npcP.getX() == 0) {
			return true;
		}
		if (playerP.getY() - npcP.getY() == 0) {
			return true;
		}
		if (playerP.getZ() - npcP.getZ() == 0) {
			return true;
		}
		return false;
	}

	public boolean getFarFlag(Vector3D npcP, Vector3D playerP) {
		if (playerP.getX() - npcP.getX() != 0) {
			return true;
		}
		if (playerP.getY() - npcP.getY() != 0) {
			return true;
		}
		if (playerP.getZ() - npcP.getZ() != 0) {
			return true;
		}
		return false;
	}
	
	public int getNumOfNPCs() {
		return 5;
	}
	
	public Matrix3D getNPClocation(int i) {
		return NPClist[i].getLocation();
	}
	public UUID getNPCUUID(int i){
		return NPClist[i].getID();
	}
	public Matrix3D getNPCsize(int i){
		return NPClist[i].getScale();
	}
}
