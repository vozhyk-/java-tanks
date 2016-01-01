package re.neutrino.java_tanks.server;

public class Main {
	public static void main(String[] args) {
		parseCommandLine(args);

		//Debug.open("server.debug");
		
		Config config = new Config();
		//config.read();
		//readAbilities();
		
		//initSignals();
		
		//initGame();
		
		Server server = new Server(config);
		server.start();
	}

	private static void parseCommandLine(String[] args) {
		// TODO Auto-generated method stub
		
	}
}
