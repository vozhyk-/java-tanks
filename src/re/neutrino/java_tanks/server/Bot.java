package re.neutrino.java_tanks.server;

import re.neutrino.java_tanks.types.Player;
import re.neutrino.java_tanks.types.Shot;

public class Bot extends Client {

	public Bot(Player player, Game game) {
		super(player, game);
		// TODO Auto-generated constructor stub
	}

	public void shoot() {
		short power = 40, angle;

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

		if (my_x > target_x) {
			angle = 120;
		} else {
			angle = 60;
		}

		Shot shot = new Shot(angle, power);
		game.shoot(this, shot);
	}
}
