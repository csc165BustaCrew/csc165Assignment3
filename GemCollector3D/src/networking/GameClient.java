package networking;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;
import java.util.Vector;

import game.GemCollector;
import graphicslib3D.Vector3D;
import myGameEngine.GhostAvatar;
import sage.networking.client.GameConnectionClient;

public class GameClient extends GameConnectionClient{
	private GemCollector game;
	private UUID id;
	private Vector<GhostAvatar> ghostAvatrs;
	
	public GameClient(InetAddress remoteAddr, int remotePort, ProtocolType protocolType, GemCollector game) throws IOException {
		super(remoteAddr, remotePort, protocolType);
		this.game = game;
		this.id = UUID.randomUUID();
		this.ghostAvatrs = new Vector<GhostAvatar>();		
	}
	
	protected void processPacket(Object msg){
		String message = (String) msg;
		String[] msgTokens = message.split(",");
		
		
		if(msgTokens[0].compareTo("join") == 0){
			
			if(msgTokens[1].compareTo("success") == 0){
				game.setIsConnected(true);
				sendCreateMessage(game.getPlayerPosition());
			}
			
			if(msgTokens[1].compareTo("failure") == 0){
				game.setIsConnected(false);
			}
		}
		
		if(msgTokens[0].compareTo("bye") == 0){
			UUID ghostID = UUID.fromString(msgTokens[1]);
			removeGhostAvatar(ghostID);
		}
		
		if(msgTokens[0].compareTo("create") == 0){
			UUID ghostID = UUID.fromString(msgTokens[1]);
			double x = Double.parseDouble(msgTokens[2]);
			double y = Double.parseDouble(msgTokens[3]);
			double z = Double.parseDouble(msgTokens[4]);
			Vector3D ghostPosition = new Vector3D(x, y, z);
			createGhostAvatar(ghostID, ghostPosition);
		}
	}

	public void sendJoinMessage(){
		try{
			sendPacket(new String("join," + id.toString()));
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	private void createGhostAvatar(UUID ghostID, Vector3D ghostPosition) {
		// TODO Auto-generated method stub
		
	}

	private void removeGhostAvatar(UUID ghostID) {
		// TODO Auto-generated method stub
		
	}

	public void sendCreateMessage(Vector3D pos){
		try{
			String message = new String("create," + id.toString());
			message += "," + pos.getX() + "," + pos.getY() + "," + pos.getZ();
			sendPacket(message);
		} catch(IOException e){
			e.printStackTrace();
		}
	}

	public void sendByeMessage() {
		try{
			sendPacket(new String("bye," + id.toString()));
		} catch (IOException e) { e.printStackTrace(); }
		
	}
}
