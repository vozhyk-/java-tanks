package re.neutrino.java_tanks.client;

import java.util.ArrayList;
import re.neutrino.java_tanks.debug.DebugLevel;
import re.neutrino.java_tanks.types.Player;
import re.neutrino.java_tanks.types.basic.Int16;
import re.neutrino.java_tanks.types.updates.PlayerUpdate;

public class PlayersList {
	private static ArrayList<Player> l = new ArrayList<Player>();
	public static Player loc_player = null;

	void update(PlayerUpdate p) {
		synchronized (l) {
			Player pp = p.getPlayer();
			int index = find(pp.getId());
			if (index < 0) {
				Main.debug.print(DebugLevel.Debug, "Add player", pp);
				l.add(pp);
				if (is_loc_player(p)) {
					loc_player = l.get(l.size()-1);
				}
				Main.debug.print(DebugLevel.Debug, "ListSize", l.size());
			} else {
				Main.debug.print(DebugLevel.Debug, "Update player", pp);
				if (loc_player == l.get(index)) {
					l.set(index, pp);
					loc_player = l.get(index);
				} else {
					l.set(index, pp);
				}
			}
		}
	}

	synchronized boolean is_loc_player(PlayerUpdate p) {
		return p.getPlayer().getId().equals(Game.PlayerID);
	}

	synchronized boolean is_loc_player(Player p) {
		return p.getId().equals(Game.PlayerID);
	}

	void delete(PlayerUpdate p) {
		synchronized (l) {
			Player pp = p.getPlayer();
			int index = find(pp.getId());
			if (index < 0) {
				Main.debug.print(DebugLevel.Warn, "DelPlayer not found", pp);
			} else {
				Main.debug.print(DebugLevel.Debug, "Delete player", pp);
				if (is_loc_player(l.get(index))) {
					Main.debug.print(DebugLevel.Err, "Trying to delete loc_player");
				} else {
					l.remove(index);
					((LobbyPanel) Main.GUIframe.lobby).update_player_list();
				}
			}
		}
	}

	synchronized Player find_p(Int16 id) {
		for (Player i:l) {
			if (i.getId().equals(id)) {
				return i;
			}
		}
		return null;
	}

	synchronized int find(Int16 id) {
		for (Player i:l) {
			if (i.getId().equals(id)) {
				return l.indexOf(i);
			}
		}
		return -1;
	}

	synchronized Player get(Int16 id) {
		int index = find(id);
		if (index < 0) {
			return null;
		} else {
			return l.get(index);
		}
	}

	synchronized void clear_l() {
		l.clear();
	}

	synchronized public ArrayList<Player> getL() {
		return l;
	}
}
