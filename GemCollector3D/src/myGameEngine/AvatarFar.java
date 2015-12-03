package myGameEngine;

import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import sage.ai.behaviortrees.BTCondition;
import sage.scene.TriMesh;
import sage.scene.shape.Cube;

public class AvatarFar extends BTCondition{
	private NPCcontroller npcc;
	private Cube npc;
	private TriMesh player;
	
	public AvatarFar(TriMesh p, NPCcontroller c, Cube n, boolean toNegate){
		super(toNegate);
		player = p;
		npcc = c;
		npc = n;
	}
	protected boolean check(){
		Vector3D npcVector = npc.getWorldTranslation().getCol(3);
		Point3D npcP = new Point3D(npcVector.getX(),npcVector.getY(),npcVector.getZ());
		Vector3D playerVector = player.getWorldTranslation().getCol(3);
		Point3D playerP = new Point3D(playerVector.getX(), playerVector.getY(), playerVector.getZ());
		
		return npcc.getFarFlag(npcP, playerP);
	} 
}
