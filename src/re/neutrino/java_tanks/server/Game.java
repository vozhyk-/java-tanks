package re.neutrino.java_tanks.server;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import re.neutrino.java_tanks.Config;
import re.neutrino.java_tanks.debug.*;
import re.neutrino.java_tanks.types.*;
import re.neutrino.java_tanks.types.updates.*;

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

	public GameMap getMap() {
		return map;
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

	/**
	 * Helper for newPlayerX()
	 * @return true if there is a player
	 * 	       that is too close to x.
	 */
	boolean playerXTooClose(short x) {
	    int tankDistance = config.get("tank_distance");

	    // TODO locking
	    return clients.stream().anyMatch(cl ->
	    	Math.abs(cl.getPlayer().getPos().getX() - x)
	    	< tankDistance);
	}

	short newPlayerX() {
	    int notank_margin = config.get("map_margin");
	    short x;

	    /* Generate position until it isn't too close to other tanks */
	    do
	        x = (short) (notank_margin
	            + Math.abs(rand.nextInt()) %
	            (map.getInfo().getLength() - 2 * notank_margin));
	    while (playerXTooClose(x));

	    return x;
	}

	short newPlayerY(short x) {
		return (short) (map.get(x) - 1);
	}

	MapPosition newPlayerPos() {
		short x = newPlayerX();

		return new MapPosition(x, newPlayerY(x));
	}

	public Player newPlayer(String nickname) {
		return new Player(
				Player.State.Joined,
				true,
				nextPlayerId(),
				nickname,
				(short)config.get("tank_hp"),
				newPlayerPos(),
				(short)0,
				(short)0);
	}

	public JoinReply tryJoin(String nickname) {
		Optional<Client> found = clients.stream()
				.filter(c -> c.getPlayer().getNickname().equals(nickname))
				.findAny();
		boolean nicknameFound = found.isPresent();
		Client cl = nicknameFound ? found.get() : null;

		if (started) {
			debug.print(DebugLevel.Info, "join", "Game already in progress");

			if (nicknameFound) {
				if (!cl.getPlayer().isConnected()) {
					debug.print(DebugLevel.Info,
							"join", cl.getPlayer() + " rejoins");
					cl.getPlayer().setConnected(true);

					return joinOk(cl);
				} else {
					return joinNicknameTaken(nickname);
				}
			} else {
				debug.print(DebugLevel.Info,
						"join", "new player not allowed to join during game: "
								+ nickname);
			    return JoinReply.GameInProgress;
			}
		} else {
			if (nicknameFound) {
				return joinNicknameTaken(nickname);
			} else {
				cl = new Client(nickname, this);
				debug.print(DebugLevel.Info,
						"join", "New player: " + cl.getPlayer());

				/* Notify all other clients of the new player
	             * and then add the new client to the array */
	            /* In that way only clients that have already joined
	             * are going to receive the notification */
				allAddUpdate(new PlayerUpdate(
						Update.Type.AddPlayer, cl.getPlayer()));

				clients.add(cl);

				return joinOk(cl);
			}
		}
	}

	private JoinReply joinNicknameTaken(String nickname) {
		debug.print(DebugLevel.Info, "join", "Nickname already taken: " + nickname);
		return JoinReply.NicknameTaken;
	}

	private JoinReply joinOk(Client cl) {
		/* Tell the new/returning client about
		 * all existing clients,
		 * current config,
		 * available abilities
		 */
		// TODO locking
		UpdateQueue uq = cl.getUpdates();
		for (Client other : clients)
			uq.add(
					new PlayerUpdate(Update.Type.AddPlayer,
							other.getPlayer()));

		for (Config.Item i : config.getItems())
			uq.add(i.toUpdate());

		// TODO add ability updates

		return JoinReply.ok(cl);
	}

	public void tryStart() {
		if (clients.stream().allMatch(cl ->
				cl.getPlayer().getState() == Player.State.Ready))
			start();
	}

	void start() {
		/* TODO move tanks_map creation to process_shoot_command? */
	    //tanks_map = map_with_tanks();

//		lock_clients_array();                                        /* {{{ */
	    /* Mark all players as waiting for their turns */
	    for (Client cl : clients)
	    	cl.changeState(Player.State.Waiting);

	    /* Give turn to the first player */
	    /* Assume the first player is the first client.
	     * May become not true in the future?
	     */
		Client cl = clients.get(0);
//	    unlock_clients_array();                                      /* }}} */

		cl.changeState(Player.State.Active);

	    started = true;
	}

	/**
	 * Marks the player as disconnected
	 * or removes it if the game hasn't started yet
	 */
	public void disconnectClient(Client cl)
	{
		/* calling thread has no client, nothing to do */
	    if (cl == null)
	        return;

	    if (!started) // still in lobby, can delete players
	    {
	        deleteClient(cl);
	    }
	    else
	    {
	        /* Mark current player as disconnected and add an update about it */
	        Player player = cl.getPlayer();

	        debug.print(DebugLevel.Info,
	        		"player disconnected", player);

	        player.setConnected(false);
	    }
	}

	private void deleteClient(Client cl) {
		// TODO locking
		/* Notify clients of the player being deleted */
        debug.print(DebugLevel.Info,
        		"removing player", cl.getPlayer());
        allAddUpdate(new PlayerUpdate(
        		Update.Type.DelPlayer, cl.getPlayer()));

		clients.removeIf(c ->
			c.getPlayer().equals(cl.getPlayer()));
	}
}
