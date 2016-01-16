package re.neutrino.java_tanks.client;

import re.neutrino.java_tanks.Config;
import re.neutrino.java_tanks.types.GameMap;
import re.neutrino.java_tanks.types.basic.Int16;

public class Game {
	public static GameMap map;
	public static Int16 PlayerID;
	public static Config conf = new Config(Main.debug);
	public static PlayersList players = new PlayersList();
	Thread changesThread;

	Game(String nick) {
		if (Main.con.joinServer(nick)) {
			Main.con.fetch_map();
			changesThread = new Thread(Main.con.ct, "");
			changesThread.start();
			Main.GUIframe.changePane(Main.GUIframe.lobby);
		}
	}
}
