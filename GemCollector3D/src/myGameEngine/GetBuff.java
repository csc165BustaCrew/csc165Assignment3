package myGameEngine;

import sage.ai.behaviortrees.BTAction;
import sage.ai.behaviortrees.BTStatus;
import sage.scene.shape.Cube;

public class GetBuff extends BTAction{
	private NPCcontroller npcc;
	private Cube npc;
	public GetBuff(Cube n){
		npc = n;
		
	}
	@Override
	protected BTStatus update(float arg0) {
		npc.scale(100, 100, 100);
		return BTStatus.BH_SUCCESS;
	}
}
