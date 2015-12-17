package myGameEngine;

import sage.ai.behaviortrees.BTCondition;

public class OffTerrain extends BTCondition{

	private NPCcontroller npcc;
	private NPC npc;
	private long lastUpdateTime;
	
	public OffTerrain(NPCcontroller c, NPC npcReal, boolean toNegate){
		super(toNegate);
		npcc = c;
		npc = npcReal;
		lastUpdateTime = System.nanoTime();
	 }

	@Override
	protected boolean check() {
		if(npc.getDirection() == 1){
			if (npc.getLocation().getCol(3).getZ() >= (npc.getSpawnValue() + 300)) {
				return true;
			} else {
				return false;
			}
		}else{
			if (npc.getLocation().getCol(3).getZ() <= (npc.getSpawnValue() + 300)) {
				return true;
			} else {
				return false;
			}
		}
	}
	
}
