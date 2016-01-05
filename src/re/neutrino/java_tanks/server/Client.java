package re.neutrino.java_tanks.server;

import java.util.Queue;

import re.neutrino.java_tanks.types.*;

public class Client {
	final Player player;
    Queue<Update> updates;

	//pthread_mutex_t updates_mutex;

    Game game;

    public Client(Player player, Game game) {
		this.player = player;
		this.game = game;
	}

    public Client(String nickname, Game game) {
    	this.player = game.newPlayer(nickname);
    	this.game = game;
    }

    public Player getPlayer() {
		return player;
	}

    public Queue<Update> getUpdates() {
		return updates;
	}

	public void setUpdates(Queue<Update> updates) {
		this.updates = updates;
	}

	public void changeState(Player.State state) {
		player.setState(state);

		game.allAddUpdate(new PlayerUpdate(Update.Type.Player, player));
	}
}
