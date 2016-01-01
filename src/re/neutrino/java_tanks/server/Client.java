package re.neutrino.java_tanks.server;

import java.util.Queue;

import re.neutrino.java_tanks.Player;
import re.neutrino.java_tanks.PlayerUpdate;
import re.neutrino.java_tanks.Update;
import re.neutrino.java_tanks.Player.State;

public class Client {
	final ID id;

	final Player player;
    Queue<Update> updates;

	//pthread_mutex_t updates_mutex;
    
    Game game;

    public Client(ID id, Player player, Game game) {
		this.id = id;
		this.player = player;
		this.game = game;
	}

    public Player getPlayer() {
		return player;
	}

	public ID getId() {
		return id;
	}
    
    public Queue<Update> getUpdates() {
		return updates;
	}

	public void setUpdates(Queue<Update> updates) {
		this.updates = updates;
	}

	public class ID {
		final int value;

		public ID(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	public void changeState(State state) {
		player.setState(state);
		
		game.allAddUpdate(new PlayerUpdate(Update.Type.Player, player));
	}
}
