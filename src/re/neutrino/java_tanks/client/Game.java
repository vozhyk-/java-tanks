package re.neutrino.java_tanks.client;

import re.neutrino.java_tanks.types.GameMap;
import re.neutrino.java_tanks.types.basic.Int16;

public class Game {
	public static GameMap map;
	public static Int16 PlayerID;
	
	Game(String nick) {
		Main.con.joinServer(nick);
		//Main.con.fetch_map();
		//Main.con.fetch_changes();
		Main.GUIframe.changePane(Main.GUIframe.Lobby);
	}
}
