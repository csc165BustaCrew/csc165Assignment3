import java.io.IOException;

import networking.GameServerTCP;
import sage.networking.server.GameConnectionServer;

public class NetworkingServer {
	public static void main(String[] args) throws IOException{
		int port = 502;
		new GameServerTCP(port);
	}
}