package re.neutrino.java_tanks.server;

import java.util.ArrayList;

public class Server {
	ArrayList<Game> games = new ArrayList<Game>();
	private Config config;

	public Server(Config config) {
		this.config = config;
	}

	public void start() {
		games.add(new Game(config));
		
		
	}
}
