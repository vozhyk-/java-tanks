package re.neutrino.java_tanks.server;

import re.neutrino.java_tanks.types.*;
import re.neutrino.java_tanks.types.updates.*;

public class Client {
	Player player;
    final UpdateQueue updates = new UpdateQueue();

	//pthread_mutex_t updates_mutex;

    Game game;

    public Client(Player player, Game game) {
		this.player = player;
		this.game = game;
	}

    public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public UpdateQueue getUpdates() {
		return updates;
	}

	public void changeState(Player.State state) {
		player.setState(state);

		game.allAddUpdate(new PlayerUpdate(Update.Type.Player, player));
	}
}
