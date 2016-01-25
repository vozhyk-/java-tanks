package re.neutrino.java_tanks.client;

import java.util.Comparator;

import re.neutrino.java_tanks.types.Player;

public class PlayerHitpointsComparator implements Comparator<Player> {

	@Override
	public int compare(Player o1, Player o2) {
		return o2.getHitpoints() - o1.getHitpoints();
	}
}
