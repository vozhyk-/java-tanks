package re.neutrino.java_tanks.types;

import re.neutrino.java_tanks.Config;
import re.neutrino.java_tanks.types.updates.ShotImpactUpdate;

public class Impact {
	final MapPosition pos;
	final double time;

	public Impact(MapPosition pos, double time) {
		this.pos = pos;
		this.time = time;
	}

	public MapPosition getPos() {
		return pos;
	}

	public double getTime() {
		return time;
	}

	public static Impact fromUpdate(
			ShotImpactUpdate u, Player player, Shot shot, Config config) {
		double time = u.getImpactT();
		return new Impact(
				Shot.getShotPos(player, shot, time, config).round(),
				time);
	}
}