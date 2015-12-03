package myGameEngine;

import sage.ai.behaviortrees.BTAction;
import sage.ai.behaviortrees.BTStatus;
import sage.scene.shape.Cube;

public class GetWeak extends BTAction{
	private NPCcontroller npcc;
	private Cube npc;
	public GetWeak(Cube n){
		npc = n;
		
	}
	@Override
	protected BTStatus update(float arg0) {
		npc.scale(1, 1, 1);
		return BTStatus.BH_SUCCESS;
	}

}
