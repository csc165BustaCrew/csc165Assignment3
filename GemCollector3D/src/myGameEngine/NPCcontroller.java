package myGameEngine;

import java.util.Random;

import game.GemCollector;
import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import sage.ai.behaviortrees.BTCompositeType;
import sage.ai.behaviortrees.BTSequence;
import sage.ai.behaviortrees.BehaviorTree;
import sage.scene.TriMesh;
import sage.scene.shape.Cube;

public class NPCcontroller {
	BehaviorTree bt = new BehaviorTree(BTCompositeType.SELECTOR);
	private long startTime;
	private long lastUpdateTime;
	private Cube npc;
	private TriMesh player;
	private boolean isLarge;

	public void startNPControl() {
		startTime = System.nanoTime();
		lastUpdateTime = startTime;
		setupNPC();
		setupBehaviorTree();
	}

	public void setupNPC() {
		npc = new Cube();
		Random rand = new Random();
		Matrix3D location = new Matrix3D();
		location.translate(rand.nextInt(30) * 1, 30, rand.nextInt(30) * 1);
		npc.setLocalTranslation(location);
	}

	public void npcLoop(TriMesh p) {
		long frameStartTime = System.nanoTime();
		float elapsedMilliSecs = (frameStartTime - lastUpdateTime) / (1000000.0f);
		if (elapsedMilliSecs >= 50.0f) {
			lastUpdateTime = frameStartTime;
			bt.update(elapsedMilliSecs);
		}
	}

	public void setupBehaviorTree() {
		bt.insertAtRoot(new BTSequence(10));
		bt.insertAtRoot(new BTSequence(20));

		// bt.insert(10, new AvatarNear(game, this, npc, false));
		// bt.insert(10, new GetBuff(npc));
		
		bt.insert(10, new ThirtySeconds(this, npc, false));
		bt.insert(10, new GetBuff(npc));
		bt.insert(10, new SeventySeconds(this, npc, false));
		bt.insert(10, new GetWeak(npc));

		// bt.insert(20, new AvatarFar(game, this, npc, false));
		// bt.insert(20, new GetWeak(npc));

	}

	public boolean getNearFlag(Point3D npcP, Point3D playerP) {
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

	public boolean getFarFlag(Point3D npcP, Point3D playerP) {
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

}
