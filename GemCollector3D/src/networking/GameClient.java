package networking;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;
import java.util.Vector;

import game.GemCollector;
import graphicslib3D.Matrix3D;
import graphicslib3D.Vector3D;
import myGameEngine.GhostAvatar;
import myGameEngine.GhostNPC;
import sage.networking.client.GameConnectionClient;

public class GameClient extends GameConnectionClient{
	private GemCollector game;
	private UUID id;
	private Vector<GhostAvatar> ghostAvatars;
	private Vector<GhostNPC> ghostNPCs;
	
	public GameClient(InetAddress remoteAddr, int remotePort, ProtocolType protocolType, GemCollector game) throws IOException {
		super(remoteAddr, remotePort, protocolType);
		this.game = game;
		this.id = UUID.randomUUID();
		this.ghostAvatars = new Vector<GhostAvatar>();	
		this.ghostNPCs = new Vector<GhostNPC>();
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
		
		if(msgTokens[0].compareTo("startPos") == 0 ){
			double x = Double.parseDouble(msgTokens[1]);
			double y = Double.parseDouble(msgTokens[2]);
			double z = Double.parseDouble(msgTokens[3]);
			game.initPlayerLocation(new Vector3D(x, y, z));
		}
		
		if(msgTokens[0].compareTo("bye") == 0){
			UUID ghostID = UUID.fromString(msgTokens[1]);
			removeGhostAvatar(ghostID);
		}
		
		if(msgTokens[0].compareTo("create") == 0){
			System.out.println("Create command succesful?");
			UUID ghostID = UUID.fromString(msgTokens[1]);
			createGhostAvatar(ghostID, stringToMatrix(msgTokens[2]));
		}
		
		if(msgTokens[0].compareTo("createGhostNPC") == 0){
			System.out.println("Create command succesful?");
			UUID ghostID = UUID.fromString(msgTokens[1]);
			createGhostNPC(ghostID, stringToMatrix(msgTokens[2]));
		}
		
		if(msgTokens[0].compareTo("updateGhost") == 0){
			UUID ghostID = UUID.fromString(msgTokens[1]);
			updateGhost(ghostID, stringToMatrix(msgTokens[2]));
		}
		
		if(msgTokens[0].compareTo("needDetails") == 0){
			UUID ghostID = UUID.fromString(msgTokens[1]);
			sendDetails(ghostID);
		}
		
		if(msgTokens[0].compareTo("mnpc")==0){
			UUID ghostID = UUID.fromString(msgTokens[1]);
			updateGhostNPCS(ghostID, stringToMatrix(msgTokens[2]), stringToMatrix(msgTokens[3]));
		}
}

	private void sendDetails(UUID ghostID){
		try{
			String msg = "details4," + ghostID.toString() + "," + id.toString() + ",";
			msg += game.getPlayerPosition().toString();
			sendPacket(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendJoinMessage(){
		try{
			sendPacket(new String("join," + id.toString()));
		} catch (IOException e) {
			e.printStackTrace(); 
		}
	}
	
	public void sendUpdate(Matrix3D m){
		try{
			String update = "updateGhost," +id.toString() +"," + m.toString();
			sendPacket(update);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void updateGhost(UUID ghostID, Matrix3D m){
		for(GhostAvatar g: ghostAvatars){
			if(g.getID().equals(ghostID)){
				g.update(m);
			}
		}
	}
	
	private void updateGhostNPCS(UUID ghostID, Matrix3D m, Matrix3D scale){
		for(GhostNPC g: ghostNPCs){
			if(g.getID().equals(ghostID)){
				g.update(m, scale);
			}
		}
	}
	
	private Matrix3D stringToMatrix(String str){
		String[] loc = str.split(" ");
		
		Matrix3D ghostM = new Matrix3D();
		int index = 0;
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				ghostM.setElementAt(i, j, Double.parseDouble(loc[index++]));
			}
		}
		
		return ghostM;
	}
	
	private void createGhostAvatar(UUID ghostID, Matrix3D ghostM) {
		GhostAvatar ghost = new GhostAvatar(ghostM, ghostID);
		game.addGhost(ghost.getGhost());
		ghostAvatars.add(ghost);
		System.out.println("yes");
	}
	
	private void createGhostNPC(UUID ghostID, Matrix3D ghostM){
		GhostNPC ghost = new GhostNPC(ghostM, ghostID);
		game.addGhost(ghost.getGhost());
		ghostNPCs.addElement(ghost);
	}

	private void removeGhostAvatar(UUID ghostID) {
		for(GhostAvatar g: ghostAvatars){
			if(g.getID().equals(ghostID)){
				game.removeGhost(g.getGhost());
			}
		}
	}

	public void sendCreateMessage(Matrix3D m){
		try{
			String message = new String("create," + id.toString());
			message += "," + m.toString();
			sendPacket(message);
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void askForNPCinfo(){
		try{
			sendPacket(new String("needNPC," + id.toString()));
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
