package re.neutrino.java_tanks.server;

import java.util.List;
import java.util.Random;

import re.neutrino.java_tanks.*;
import re.neutrino.java_tanks.types.GameMap;
import re.neutrino.java_tanks.types.Player;
import re.neutrino.java_tanks.types.Update;

public class Game {
	List<Client> clients;
	GameMap map;
	GameMap.Info mapInfo;
	boolean started;
	
	Random rand;
	
	public Game(Config config) {
		rand = new Random();
		mapInfo = new GameMap.Info(
				rand.nextLong(),
				config.get("map_width"),
				config.get("map_height"));
		map = GameMap.generate(mapInfo);
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
}
