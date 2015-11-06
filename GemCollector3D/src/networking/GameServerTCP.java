package networking;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

import sage.networking.server.GameConnectionServer;
import sage.networking.server.IClientInfo;

public class GameServerTCP extends GameConnectionServer<UUID>{

	private int clientCount = 0;
	
	public GameServerTCP(int localPort) throws IOException {
		super(localPort, ProtocolType.TCP);
	}

	public void acceptClient(IClientInfo ci, Object o){
		String message = (String)o;
		String[] messageTokens = message.split(",");
		
		if(messageTokens.length > 0){
			if(messageTokens[0].compareTo("join") == 0){
				UUID clientID = UUID.fromString(messageTokens[1]);
				addClient(ci, clientID);
				sendJoinedMessage(clientID, true);
				clientCount++;
				sendInitLocation(clientID);
			}
		}
	}
	
	public void processPacket(Object o, InetAddress senderIP, int sendPort){
		String message = (String) o;
		String[] msgTokens = message.split(",");
		
		if(msgTokens.length > 0){
			if(msgTokens[0].compareTo("bye") == 0){
				UUID clientID = UUID.fromString(msgTokens[1]);
				sendByeMessages(clientID);
				removeClient(clientID);
				clientCount--;
			}
		}
		
		if(msgTokens[0].compareTo("create") == 0) {
			UUID clientID = UUID.fromString(msgTokens[1]);
			String pos = msgTokens[2];
			sendCreateMessages(clientID, pos);
			sendWantsDetailsMessages(clientID);
		}
		
		if(msgTokens[0].compareTo("updateGhost") == 0){
			UUID clientID = UUID.fromString(msgTokens[1]);
			updatePlayers(clientID, msgTokens[2]);
		}
		
		if(msgTokens[0].compareTo("details4") == 0){
			UUID remoteID = UUID.fromString(msgTokens[1]);
			UUID clientID = UUID.fromString(msgTokens[2]);
			sendDetailsFor(remoteID, clientID, msgTokens[3]);
		}
	}
	
	private void sendDetailsFor(UUID remoteID, UUID clientID, String details){
		try{
			String msg = "create," + clientID.toString() + "," + details;
			sendPacket(msg, remoteID);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void sendWantsDetailsMessages(UUID clientID) {
		try{
			String msg = "needDetails," + clientID.toString();
			forwardPacketToAll(msg, clientID);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendByeMessages(UUID clientID) {
		try{
			String msg = "bye," + clientID.toString();
			forwardPacketToAll(msg, clientID);
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public void updatePlayers(UUID clientID, String loc){
		try{
			String msg = "updateGhost," + clientID.toString() + ",";
			msg += loc;
			forwardPacketToAll(msg, clientID);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendInitLocation(UUID clientID){	
		try {
			String pos = "startPos,";
			switch(clientCount){
			case 0:
				pos += "0,0,-10";
				break;
			case 1:
				pos += "0,0,10";
				break;
			case 2:
				pos += "-10,0,0";
				break;
			case 3:
				pos += "10,0,0";
			}
			
			sendPacket(pos, clientID);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void sendJoinedMessage(UUID clientID, boolean success){
		try {
			String message = new String("join,");
			
			if(success) {
				message += "success";
				System.out.println("joined");
			}
			else {
				message += "failure";
			}
			
			sendPacket(message, clientID);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendCreateMessages(UUID clientID, String position){
		try {
			String message = new String("create," + clientID.toString());
			message += "," + position;
			System.out.println("create ghost");
			forwardPacketToAll(message, clientID);
		} catch(IOException e){
			e.printStackTrace();
		}
	}
}
