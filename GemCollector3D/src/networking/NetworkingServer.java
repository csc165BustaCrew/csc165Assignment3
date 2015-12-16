package networking;
import java.io.IOException;

import myGameEngine.NPCcontroller;
import sage.ai.behaviortrees.BTCompositeType;
import sage.ai.behaviortrees.BehaviorTree;
import sage.networking.server.GameConnectionServer;

public class NetworkingServer {
	private NPCcontroller npcCtrl;
	private long startTime;
	private long lastUpdateTime;
	private GameServerTCP gameServer;

	public NetworkingServer(){
		int port = 502;	
		startTime = System.nanoTime();
		lastUpdateTime = startTime;
		npcCtrl = new NPCcontroller();
		npcCtrl.setupNPC();
		try {
			gameServer = new GameServerTCP(port, npcCtrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
		npcCtrl.npcLoop(gameServer);
	}
}