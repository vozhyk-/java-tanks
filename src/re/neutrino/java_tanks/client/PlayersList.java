package re.neutrino.java_tanks.client;

import java.util.ArrayList;

import re.neutrino.java_tanks.debug.DebugLevel;
import re.neutrino.java_tanks.types.Player;
import re.neutrino.java_tanks.types.basic.Int16;
import re.neutrino.java_tanks.types.updates.PlayerUpdate;

public class PlayersList {
	public static ArrayList<Player> l = new ArrayList<Player>();

	void add(PlayerUpdate p) {
		Player pp = p.getPlayer();
		//Main.debug.print(DebugLevel.Debug, "Find", pp.getId());
		int index = find(pp.getId());
		if (index < 0) {
			Main.debug.print(DebugLevel.Debug, "Add player", pp);
			l.add(pp);
			Main.debug.print(DebugLevel.Debug, "ListSize", l.size());
		} else {
			Main.debug.print(DebugLevel.Warn, "Player exists, updating");
			l.set(index, pp);
		}
	}

	void update(PlayerUpdate p) {
		Player pp = p.getPlayer();
		int index = find(pp.getId());
		if (index < 0) {
			Main.debug.print(DebugLevel.Warn, "Player does not exist, adding", pp);
			l.add(pp);
			Main.debug.print(DebugLevel.Debug, "ListSize", l.size());
		} else {
			Main.debug.print(DebugLevel.Debug, "Update player", pp);
			l.set(index, pp);
		}
	}

	int find(Int16 id) {
		for (Player i:l) {
			if (i.getId().equals(id)) {
				return l.indexOf(i);
			}
		}
		return -1;
	}

	void clear_l() {
		l.clear();
	}
}
