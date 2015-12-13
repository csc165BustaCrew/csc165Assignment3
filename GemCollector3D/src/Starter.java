import java.util.Scanner;

import game.GemCollector;

public class Starter {
	public static void main(String[] args) {
//		Scanner reader = new Scanner(System.in);
//		System.out.println("Enter Server's IP address");
//		String ip = reader.nextLine();
//		System.out.println("Enter Server's port number");
//		int port = reader.nextInt();
//		System.out.println("Do you want to play in full screen mode? y/n ");
//		String fsem = reader.nextLine();
//		System.out.println("Which color would you like to be?\n 1)white\n2)blue\n3)green\n ");
//		int color = reader.nextInt();
		//new GemCollector(ip, port, fsem, color).start();
		new GemCollector("127.0.0.1", 502).start();
	}
}
