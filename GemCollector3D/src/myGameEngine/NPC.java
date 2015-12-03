package myGameEngine;

import sage.scene.shape.Cube;

public class NPC {
	private Cube npc;

	public void updateLocation() {
		// TODO Auto-generated method stub
		
		
	}

	public void setLocation(int i, int j) {
		// TODO Auto-generated method stub
		npc.translate(i, 0, j);
	}

}
