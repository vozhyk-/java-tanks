package re.neutrino.java_tanks.server;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

import re.neutrino.java_tanks.Config;
import re.neutrino.java_tanks.debug.*;
import re.neutrino.java_tanks.types.*;
import re.neutrino.java_tanks.types.updates.*;

import re.neutrino.java_tanks.types.Player.State;
import re.neutrino.java_tanks.types.commands.SetConfigCommand;

public class Game {
	ArrayList<Client> clients = new ArrayList<>();
	ServerGameMap map;
	boolean started;

	Config config;
	DebugStream debug;

	Random rand;
	short playerIdCounter = 0;

	public ArrayList<Client> getClients() {
		return clients;
	}

	public ServerGameMap getMap() {
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
		generateNewMap();
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
	    int notankMargin = config.get("map_margin");
	    short x;

	    /* Generate position until it isn't too close to other tanks */
	    do
	        x = (short) (notankMargin
	            + Math.abs(rand.nextInt()) %
	            (map.getInfo().getLength() - 2 * notankMargin));
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

	private Player newPlayer(String nickname, short id) {
		return new Player(
				State.Joined,
				true,
				id,
				nickname,
				(short)config.get("tank_hp"),
				newPlayerPos(),
				(short)0,
				(short)0);
	}

	private Player newPlayer(String nickname) {
		return newPlayer(nickname, nextPlayerId());
	}

	private Player newPlayer(Player old) {
		return newPlayer(old.getNickname(), old.getId().getValue());
	}

	/*
	public void playerChangeState(Player player, Player.State state) {
		player.setState(state);

		allAddUpdate(new PlayerUpdate(Update.Type.Player, player));
	}
	*/

	public Bot newBot(String nickname) {
		Bot bot = new Bot(newPlayer(nickname), this);
		debug.print(DebugLevel.Info,
				"join", "New Bot: " + bot.getPlayer());
		publicLog("join", "New Bot: "
				+ bot.getPlayer().getNickname());

		/* Notify all other clients of the new player
         * and then add the new client to the array */
        /* In that way only clients that have already joined
         * are going to receive the notification */
		bot.changeState(Player.State.Ready);

		allAddUpdate(new PlayerUpdate(
				Update.Type.AddPlayer, bot.getPlayer()));

		clients.add(bot);

		tryStart();

		return bot;
	}

	public JoinReply tryJoin(String nickname) {
		Optional<Client> found = findClientByNickname(nickname);
		boolean nicknameFound = found.isPresent();
		Client cl = nicknameFound ? found.get() : null;

		if (started) {
			debug.print(DebugLevel.Info, "join", "Game already in progress");

			if (nicknameFound) {
				if (!cl.getPlayer().isConnected()) {
					debug.print(DebugLevel.Info,
							"join", cl.getPlayer() + " rejoins");
					publicLog("join",
							cl.getPlayer().getNickname() + " rejoins");
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
				cl = new Client(newPlayer(nickname), this);
				debug.print(DebugLevel.Info,
						"join", "New player: " + cl.getPlayer());
				publicLog("join", "New player: "
						+ cl.getPlayer().getNickname());

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

		return JoinReply.ok(cl, this);
	}

	public Optional<Client> findClientByNickname(String nickname) {
		return clients.stream()
				.filter(c -> c.getPlayer().getNickname().equals(nickname))
				.findAny();
	}

	public void tryStart() {
		if (clients.stream().allMatch(cl ->
				cl.getPlayer().getState() == State.Ready))
		{
			debug.print(DebugLevel.Info,
					"starting game", "All players ready");
			publicLog("starting game", "All players ready");
			start();
		}
	}

	private boolean tryEnd() {
		/* Check for end of game */
	    long numLivingPlayers = clients.stream().filter(
	    		cl -> cl.getPlayer().getState() != State.Dead)
	    		.count();
	    if (numLivingPlayers > 1)
	    	return false;

	    /* At this point, only one living player remains */

	    /***** GAME OVER *****/
	    debug.print(DebugLevel.Info,
	    		"only one living player remains", "Game over");

	    end();

	    return true;
	}

	void start() {
		/* TODO move tanks_map creation to process_shoot_command? */
	    //tanks_map = map_with_tanks();

//		lock_clients_array();                                        /* {{{ */
	    /* Mark all players as waiting for their turns */
	    for (Client cl : clients)
	    	cl.changeState(State.Waiting);

	    /* Give turn to the first player */
	    /* Assume the first player is the first client.
	     * May become not true in the future?
	     */
		Client cl = clients.get(0);
//	    unlock_clients_array();                                      /* }}} */

		cl.changeState(State.Active);

	    started = true;
	}

	void end() {
		/* Mark dead players as Loser and living players as Winner */
	    //lock_clients_array();                                        /* {{{ */
	    for (Client cl : clients)
	    {
	        cl.changeState(cl.getPlayer().getState() != State.Dead ?
	        		State.Winner : State.Loser);
	    }
	    //unlock_clients_array();                                      /* }}} 2 */

	    //game_cleanup();
	    //reset_game();
	}

	/* Advances turn to the next player */
	void nextTurn()
	{
		// TODO locking
		int activeI = IntStream.range(0, clients.size())
				.filter(i -> clients.get(i).getPlayer().getState()
						== State.Active)
				.findAny().getAsInt();

		clients.get(activeI).changeState(State.Waiting);

		Client next;

		if (activeI == clients.size() - 1)
			next = clients.get(0);
		else
			next = clients.get(activeI + 1);

		next.changeState(State.Active);

		if (next.getPlayer().getNickname().startsWith("bot")) {
			allAddUpdate(new PlayerUpdate(Update.Type.Player, next.getPlayer()));
			((Bot) next).shoot();
		}
	}

	/**
	 * Marks the player as disconnected
	 * or removes it if the game hasn't started yet
	 */
	public void disconnectClient(Client cl)
	{
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
	        publicLog("player disconnected", player.getNickname());

	        player.setConnected(false);
	    }
	}

	private void deleteClient(Client cl) {
		// TODO locking
		/* Notify clients of the player being deleted */
        debug.print(DebugLevel.Info,
        		"removing player", cl.getPlayer());
        publicLog("player left", cl.getPlayer().getNickname());
        allAddUpdate(new PlayerUpdate(
        		Update.Type.DelPlayer, cl.getPlayer()));

		clients.removeIf(c ->
			c.getPlayer().equals(cl.getPlayer()));
	}

	public void shoot(Client client, Shot shot) {
		debug.print(DebugLevel.Info, "shot", client.getPlayer());
		debug.print(DebugLevel.Debug, "shot", shot);

		allAddUpdate(new ShotUpdate(client.getPlayer(), shot));

		processImpact(shotWithoutDamage(client.getPlayer(), shot));
	}

	private MapPosition shotWithoutDamage(Player player, Shot shot) {
		Impact i = map.shotWithoutDamage(player, shot);

	    allAddUpdate(new ShotImpactUpdate(i.getTime()));
	    return i.getPos();
	}

	private void processImpact(MapPosition impactPos) {
		shotDealDamage(impactPos);
		map.shotUpdateMap(impactPos);
		landPlayersFromTheSky();

	    if (!tryEnd()) {
	        //tanks_map = map_with_tanks();

	        nextTurn();
	    }
	}

	private void shotDealDamage(MapPosition impactPos) {
		if (!map.isInside(impactPos))
	        return;

	    FloatPair impactPosF = impactPos.toFloatPair();

	    //lock_clients_array();                                        /* {{{ */
	    for (Client cl : clients) {
	        Player player = cl.getPlayer();

	        if (player.getState() != State.Dead)
	        {
	            FloatPair playerPosF = player.getPos().toFloatPair();

	            short damage = damageToPlayer(impactPosF, playerPosF);
	            if (damage > 0)
	                clientDealDamage(cl, damage);
	        }
	    }
	    //unlock_clients_array();                                      /* }}} */
	}

	// Can be moved into ServerGameMap
	private short damageToPlayer(FloatPair impactPos, FloatPair playerPos)
	{
	    int damageCap = config.get("dmg_cap");
	    int radius = config.get("dmg_radius");
	    FloatPair diff = new FloatPair(
	    		playerPos.x - impactPos.x,
	    		playerPos.y - impactPos.y);
	    double distance = Math.sqrt(diff.x*diff.x + diff.y*diff.y);
	    if (distance > radius) return 0;
	    if (distance <= 2) return (short) damageCap;
	    return (short) (damageCap/(distance-1));
	    /* damage in the impact point - damage_cap, on the edge of the radius - 0 */
	    //return damage_cap - distance * ((double)damage_cap / radius);
	}

	void clientDealDamage(Client cl, short damage)
	{
		Player player = cl.getPlayer();

	    player.dealDamage(damage);
	    if (player.getHitpoints() <= 0)
	        /* adds an update as well */
	        cl.changeState(State.Dead);
	    else
	        allAddUpdate(
	        		new PlayerUpdate(Update.Type.Player, player));
	    debug.print(DebugLevel.Info,
	    		"damage", player + " gets " + damage);
	}

	private void landPlayersFromTheSky() {
		/* Place all tanks above ground back onto the ground */
	    //lock_clients_array();                                        /* {{{ */
	    for (Client cl : clients)
	    {
	        Player player = cl.getPlayer();
	        MapPosition pos = player.getPos();
	        short mapY = map.get(pos.getX());

	        if (player.getPos().getY() < mapY - 1) {
	        	player.setPos(new MapPosition(
	        			pos.getX(), newPlayerY(pos.getX())));
	            allAddUpdate(new PlayerUpdate(
	            		Update.Type.Player, player));
	        }
	    }
	    //unlock_clients_array();                                      /* }}} */
	}

	public void sendChat(Client cl, String msg) {
		publicLog("chat",
				String.format("%25s: %s", cl.getPlayer().getNickname(), msg));
	}

	public void processConfigUpdate(SetConfigCommand cmd) {
		String optName = cmd.getOption().getName();

		if (optName.startsWith("map_")) {
			generateNewMap();
			regeneratePlayers();
		} else if (optName.startsWith("tank_")) {
			regeneratePlayers();
		} else if (optName.equals("bot_nr")) {
			debug.print(DebugLevel.Info, "Regenerate bots");
			regenerateBots();
		}
	}

	private void regenerateBots() {
		// TODO: Remove all bots
		for (Client cl : clients) {
			if (cl.getPlayer().getNickname().startsWith("bot")) {
				clients.remove(cl);
			}
		}
		// Add new bots
		for (Integer i=0; i<config.get("bot_nr"); i++) {
			newBot("bot" + i);
		}
	}

	private void regeneratePlayers() {
		for (Client cl : clients) {
			cl.setPlayer(newPlayer(cl.getPlayer()));
			allAddUpdate(new PlayerUpdate(
					Update.Type.Player, cl.getPlayer()));
		}
	}

	private void generateNewMap() {
		GameMap.Info mapInfo = new GameMap.Info(
				rand.nextInt(),
				config.get("map_width"),
				config.get("map_height"),
				config.getMapType());
		map = new ServerGameMap(mapInfo, this, config, debug);
	}

	private void publicLog(String title, Object value) {
		allAddUpdate(new LogUpdate(debug.format(title, value)));
	}
}
