package re.neutrino.java_tanks.server;

import java.util.LinkedList;
import re.neutrino.java_tanks.types.*;

public class Client {
	final Player player;
    LinkedList<Update> updates = new LinkedList<>();

	//pthread_mutex_t updates_mutex;

    Game game;

    public Client(Player player, Game game) {
		this.player = player;
		this.game = game;
	}

    public Client(String nickname, Game game) {
    	this(game.newPlayer(nickname), game);
    }

    public Player getPlayer() {
		return player;
	}

    public LinkedList<Update> getUpdates() {
		return updates;
	}

	public void setUpdates(LinkedList<Update> updates) {
		this.updates = updates;
	}

	public void changeState(Player.State state) {
		player.setState(state);

		game.allAddUpdate(new PlayerUpdate(Update.Type.Player, player));
	}
}
