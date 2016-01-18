package re.neutrino.java_tanks.client;

import java.io.IOException;

import re.neutrino.java_tanks.debug.*;

public class Main {
	public static GUI GUIframe;
	public static Net con = null;
	public static DebugStream debug;
	public static Game game;

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
