package myGameEngine;

import sage.ai.behaviortrees.BTAction;
import sage.ai.behaviortrees.BTStatus;

public class MoveToSpawn extends BTAction{
	private NPCcontroller npcc;
	private NPC npc;
	public MoveToSpawn(NPC npcReal){
		npc = npcReal;
	}
	@Override
	protected BTStatus update(float arg0) {
		npc.moveToSpawn();
//		System.out.println("move to start action");
		return BTStatus.BH_SUCCESS;
	}
}
