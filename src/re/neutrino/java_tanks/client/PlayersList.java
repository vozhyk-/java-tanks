package re.neutrino.java_tanks.client;

import java.util.ArrayList;

import re.neutrino.java_tanks.debug.DebugLevel;
import re.neutrino.java_tanks.types.Player;
import re.neutrino.java_tanks.types.basic.Int16;
import re.neutrino.java_tanks.types.updates.PlayerUpdate;

public class PlayersList {
	private static ArrayList<Player> l = new ArrayList<Player>();

	synchronized void add(PlayerUpdate p) {
		Player pp = p.getPlayer();
		//Main.debug.print(DebugLevel.Debug, "Find", pp.getId());
		int index = find(pp.getId());
		if (index < 0) {
			Main.debug.print(DebugLevel.Debug, "Add player", pp);
			getL().add(pp);
			Main.debug.print(DebugLevel.Debug, "ListSize", getL().size());
		} else {
			Main.debug.print(DebugLevel.Warn, "Player exists, updating");
			getL().set(index, pp);
		}
		((LobbyPanel) Main.GUIframe.Lobby).update_player_list();
	}

	synchronized void update(PlayerUpdate p) {
		Player pp = p.getPlayer();
		int index = find(pp.getId());
		if (index < 0) {
			Main.debug.print(DebugLevel.Warn, "Player does not exist, adding", pp);
			getL().add(pp);
			Main.debug.print(DebugLevel.Debug, "ListSize", getL().size());
		} else {
			Main.debug.print(DebugLevel.Debug, "Update player", pp);
			getL().set(index, pp);
		}
		((LobbyPanel) Main.GUIframe.Lobby).update_player_list();
	}

	int find(Int16 id) {
		for (Player i:getL()) {
			if (i.getId().equals(id)) {
				return getL().indexOf(i);
			}
		}
		return -1;
	}

	synchronized Player get(Int16 id) {
		int index = find(id);
		if (index < 0) {
			return null;
		} else {
			return getL().get(index);
		}
	}

	synchronized void clear_l() {
		getL().clear();
	}

	public ArrayList<Player> getL() {
		return l;
	}
}
