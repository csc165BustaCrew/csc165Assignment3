package myGameEngine;

import sage.ai.behaviortrees.BTCondition;
import sage.scene.shape.Cube;

public class SeventySeconds extends BTCondition{
	private NPCcontroller npcc;
	private Cube npc;
	private long lastUpdateTime;

	
	public SeventySeconds(NPCcontroller c, Cube n, boolean toNegate){
		super(toNegate);
		npcc = c;
		npc = n;
		lastUpdateTime = System.nanoTime();
	 }

	protected boolean check() {
		float elapsedMilliSecs = (System.nanoTime() - lastUpdateTime) / (1000000.0f);
		if (elapsedMilliSecs >= 30000) {
			lastUpdateTime = System.nanoTime();
			return true;
		} else {
			return false;
		}
	}
}
