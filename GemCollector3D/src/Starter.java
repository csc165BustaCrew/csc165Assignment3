import game.GemCollector;

public class Starter {
	public static void main(String[] args) {
		new GemCollector("127.0.0.1", 502).start();
	}
}
