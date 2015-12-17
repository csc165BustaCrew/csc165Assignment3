package myGameEngine;

import sage.ai.behaviortrees.BTAction;
import sage.ai.behaviortrees.BTStatus;

public class MoveForward extends BTAction{
	private NPCcontroller npcc;
	private NPC npc;
	public MoveForward(NPC npcReal){
		npc = npcReal;
	}
	@Override
	protected BTStatus update(float arg0) {
		npc.moveForward();
//		System.out.println("move forward action");
		return BTStatus.BH_SUCCESS;
	}
}
