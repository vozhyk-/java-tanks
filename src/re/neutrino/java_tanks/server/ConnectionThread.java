package re.neutrino.java_tanks.server;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import re.neutrino.java_tanks.debug.*;
import re.neutrino.java_tanks.types.*;
import re.neutrino.java_tanks.types.commands.*;

public class ConnectionThread implements Runnable {
	GameList games;
	Game game;
	Socket socket;
	CommunicationStream comm;
	DebugStream debug;

	Client client;

	public ConnectionThread(GameList games, Socket socket, DebugStream debug) {
		this.games = games;
		this.socket = socket;
		this.debug = debug;
	}

	@Override
	public void run() {
		try {
			comm = new CommunicationStream(
					socket.getInputStream(),
					socket.getOutputStream());

			/* receive and process commands until disconnect */
			while (true) {
				Command cmd;
				try {
					cmd = Command.recv(comm);
				} catch (EOFException e) {
					break;
				}
			    debug.print(DebugLevel.Debug, "received command", cmd);
			    processCommand(cmd);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	    /* TODO print (stored) client IP */
	    debug.print(DebugLevel.Info, "client closed connection");

	    if (client != null)
	    	game.disconnectClient(client);
	}

	private void processCommand(Command cmd) throws IOException {
		switch (cmd.getType()) {
		case Join:
			processCommand((JoinCommand)cmd);
			break;

		case NewGame:
			processCommand((NewGameCommand)cmd);

		case Ready:
			processCommand((ReadyCommand)cmd);
			break;

		case Shoot:
			processCommand((ShootCommand)cmd);
			break;

		case GetChanges:
			processCommand((GetChangesCommand)cmd);
			break;

		case GetMap:
			processCommand((GetMapCommand)cmd);
			break;

		// TODO access real value
		case Unknown: default:
			debug.print(DebugLevel.Err, "unrecognized command", cmd);
		}
	}

	private void processCommand(JoinCommand cmd) throws IOException {
		String nickname = cmd.getNickname();

		JoinReply reply;

		if (game != null)
			// join the chosen game
			reply = game.tryJoin(nickname);
		else
			// join a default suitable game
			reply = games.tryJoin(nickname);

		if (reply.getType() == JoinReply.Type.Ok) {
			client = reply.getClient();
			game = reply.getGame();
		}

		reply.send(comm);
	}

	private void processCommand(NewGameCommand cmd) throws IOException {
		game = games.addNewGame();

		processCommand(new JoinCommand(cmd.getNickname()));
	}

	private void processCommand(ReadyCommand cmd) {
		client.changeState(Player.State.Ready);

		game.tryStart();
	}

	private void processCommand(ShootCommand cmd) {
		game.shoot(client, cmd.getShot());
	}

	private void processCommand(GetMapCommand cmd) throws IOException {
		debug.print(DebugLevel.Info,
				"send map", "Received GetMap. Sending map...");
		game.getMap().send(comm);
	}

	private void processCommand(GetChangesCommand cmd) throws IOException {
		client.getUpdates().sendAndClear(comm);
	}
}
