package re.neutrino.java_tanks.server;

import java.util.ArrayList;
import java.util.Random;

import re.neutrino.java_tanks.types.*;
import re.neutrino.java_tanks.types.Player.State;
import re.neutrino.java_tanks.Config;
import re.neutrino.java_tanks.debug.*;

public class Game {
	ArrayList<Client> clients = new ArrayList<>();
	GameMap map;
	boolean started;

	Config config;
	DebugStream debug;

	Random rand;
	short playerIdCounter = 0;

	public ArrayList<Client> getClients() {
		return clients;
	}

	public Config getConfig() {
		return config;
	}

	public boolean isStarted() {
		return started;
	}

	public Game(Config config, DebugStream debug) {
		this.config = config;
		this.debug = debug;

		rand = new Random();
		GameMap.Info mapInfo = new GameMap.Info(
				rand.nextInt(),
				config.get("map_width"),
				config.get("map_height"));
		map = new GameMap(mapInfo);
		started = false;
	}

	void start() {
		/* TODO move tanks_map creation to process_shoot_command? */
	    //tanks_map = map_with_tanks();

//		lock_clients_array();                                        /* {{{ */
//	    /* Mark all players as waiting for their turns */
//	    for (int i = 1; i < clients.count; i++)
//	    {
//	        struct client *cl = p_dyn_arr_get(&clients, i);
//
//	        player_change_state(cl->player, PS_WAITING);
//	    }

	    /* Give turn to the first player */
	    /* Assume the first player is the first client.
	     * May become not true in the future?
	     */
		Client cl = clients.get(0);
//	    unlock_clients_array();                                      /* }}} */

		cl.changeState(Player.State.Active);

	    started = true;
	}

	public void allAddUpdate(Update upd) {
		// TODO lock clients
		for (Client cl : clients)
			cl.getUpdates().add(upd);
	}

	public short nextPlayerId() {
		short result = ++playerIdCounter;

		// TODO Fix this somehow
		if (result == 0)
			debug.print(DebugLevel.Err, "player id",
"Player ID counter overflowed to 0 (\"empty\" value)! Expect breakage!");

		return result;
	}

	public Player newPlayer(String nickname) {
		return new Player(
				State.Joined,
				true,
				nextPlayerId(),
				nickname,
				(short)config.get("tank_hp"),
				map.newPlayerPos(),
				(short)0,
				(short)0);
	}
}
