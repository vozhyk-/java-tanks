package re.neutrino.java_tanks.server;

import java.io.IOException;

import re.neutrino.java_tanks.debug.*;

public class Main {
	public static void main(String[] args) {
		parseCommandLine(args);

		DebugStream debug;
		try {
			debug = new DebugStream("server.debug");
		} catch (IOException e) {
			System.err.println("Couldn't open debug log file. Exiting.");
			e.printStackTrace();
			return;
		}
		
		Config config = new Config();
		//config.read();
		//readAbilities();
		
		//initSignals();
		
		//initGame();
		
		Server server = new Server(config, debug);
		server.listen();
	}

	private static void parseCommandLine(String[] args) {
		// TODO Auto-generated method stub
		
	}
}
