package re.neutrino.java_tanks.client;

import java.io.IOException;

import re.neutrino.java_tanks.debug.*;
import re.neutrino.java_tanks.types.*;
import re.neutrino.java_tanks.types.basic.*;

public class Main {
	public static GUI GUIframe;
	public static ClientConnection con = null;
	public static DebugStream debug;
	public static GameMap map;
	public static Int16 PlayerID;
	public static void main(String[] args) {
		try {
			debug = new DebugStream("client.debug");
		} catch (IOException e) {
			System.err.println("Couldn't open debug log file. Exiting.");
			e.printStackTrace();
			return;
		}
		debug.print(DebugLevel.Debug, "Start client");
		parseCommandLine(args);
		GUIframe = new GUI();
	}

	private static void parseCommandLine(String[] args) {
		// TODO Auto-generated method stub

	}
}
