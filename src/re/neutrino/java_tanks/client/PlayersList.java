package re.neutrino.java_tanks.client;

import java.util.ArrayList;

import re.neutrino.java_tanks.debug.DebugLevel;
import re.neutrino.java_tanks.types.Player;
import re.neutrino.java_tanks.types.basic.Int16;
import re.neutrino.java_tanks.types.updates.PlayerUpdate;

public class PlayersList {
	private static ArrayList<Player> l = new ArrayList<Player>();
	Player loc_player = null;

	synchronized void update(PlayerUpdate p) {
		Player pp = p.getPlayer();
		int index = find(pp.getId());
		if (index < 0) {
			Main.debug.print(DebugLevel.Debug, "Add player", pp);
			getL().add(pp);
			if (pp.getId().equals(Game.PlayerID)) {
				loc_player = getL().get(getL().size()-1);
			}
			Main.debug.print(DebugLevel.Debug, "ListSize", getL().size());
		} else {
			Main.debug.print(DebugLevel.Debug, "Update player", pp);
			if (loc_player == getL().get(index)) {
				getL().set(index, pp);
				loc_player = getL().get(index);
			} else {
				getL().set(index, pp);
			}
		}
		if (loc_player != null) {
			Main.debug.print(DebugLevel.Debug, "State", loc_player.getState());
			if (Main.GUIframe.cur_panel == Main.GUIframe.Lobby) {
				switch (loc_player.getState()) {
					case Active:
					case Waiting:
						Main.debug.print(DebugLevel.Debug, "Game start");
						Main.GUIframe.changePane(Main.GUIframe.Game);
						((GamePanel) Main.GUIframe.Game).timer.start();
						break;
					default:
						Main.debug.print(DebugLevel.Warn, "Unknown state");
				}
			}
			if (Main.GUIframe.cur_panel == Main.GUIframe.Game) {
				switch (loc_player.getState()) {
					case Active:
						Main.debug.print(DebugLevel.Debug, "Activate buttons");
						((GamePanel) Main.GUIframe.Game).activate_shoot_buttons(((GamePanel) Main.GUIframe.Game).buttons);
						break;
					case Waiting:
						Main.debug.print(DebugLevel.Debug, "Deactivate buttons");
						((GamePanel) Main.GUIframe.Game).deactivate_shoot_buttons(((GamePanel) Main.GUIframe.Game).buttons);
						break;
					default:
						Main.debug.print(DebugLevel.Warn, "Unknown state");
				}
			}
			((LobbyPanel) Main.GUIframe.Lobby).update_player_list();
		}
	}

	synchronized void delete(PlayerUpdate p) {
		Player pp = p.getPlayer();
		int index = find(pp.getId());
		if (index < 0) {
			Main.debug.print(DebugLevel.Warn, "DelPlayer not found", pp);
		} else {
			Main.debug.print(DebugLevel.Debug, "Delete player", pp);
			if (loc_player == getL().get(index)) {
				Main.debug.print(DebugLevel.Err, "Trying to delete loc_player");
			} else {
				getL().remove(index);
				((LobbyPanel) Main.GUIframe.Lobby).update_player_list();
			}
		}
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

	synchronized public ArrayList<Player> getL() {
		return l;
	}
}
