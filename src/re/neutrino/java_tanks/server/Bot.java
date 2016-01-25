package re.neutrino.java_tanks.server;

import re.neutrino.java_tanks.types.Player;
import re.neutrino.java_tanks.types.Shot;

public class Bot extends Client {

	public Bot(Player player, Game game) {
		super(player, game);
		// TODO Auto-generated constructor stub
	}

	public void shoot() {
		short power, angle;

		/* choose target */
		Player target = game.clients.get(0).getPlayer();
		for (Client i:game.clients) {
			if (i instanceof Bot)
				continue;
			else {
				target = i.getPlayer();
				break;
			}
		}

		short target_x = target.getPos().getX();
		short my_x = getPlayer().getPos().getX();
		power = (short) Math.abs(target_x-my_x);
		if (power > 100) power = 100;
		if (power < 30) power = 30;
		int min = 8, impact_x, diff;

		if (my_x > target_x) {
			angle = 120;
			while (true) {
				impact_x = game.map.getImpact(this.getPlayer(), new Shot(angle, power)).getPos().getX();
				diff = target_x - impact_x;
				if (diff > 0) {
					if (diff > min) {
						angle-=5;
					} else {
						break;
					}
					if (angle < 110)
						break;
				} else {
					if (diff < -min) {
						angle+=5;
					} else {
						break;
					}
					if (angle > 130)
						break;
				}
			}
		} else {
			angle = 60;
			while (true) {
				impact_x = game.map.getImpact(this.getPlayer(), new Shot(angle, power)).getPos().getX();
				diff = impact_x - target_x;
				if (diff > 0) {
					if (diff > min) {
						angle-=5;
					} else {
						break;
					}
					if (angle < 50)
						break;
				} else {
					if (diff < -min) {
						angle+=5;
					} else {
						break;
					}
					if (angle > 80)
						break;
				}
			}
		}

		Shot shot = new Shot(angle, power);
		game.shoot(this, shot);
	}
}
