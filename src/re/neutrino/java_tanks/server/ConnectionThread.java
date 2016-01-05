package re.neutrino.java_tanks.server;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

import re.neutrino.java_tanks.debug.*;
import re.neutrino.java_tanks.types.*;
import re.neutrino.java_tanks.types.commands.*;

public class ConnectionThread implements Runnable {
	Game game;
	Socket socket;
	CommunicationStream comm;
	DebugStream debug;

	Client client;

	public ConnectionThread(Game game, Socket socket, DebugStream debug) {
		this.game = game;
		this.socket = socket;
		this.debug = debug;
	}

	public void run() {
		try {
			comm = new CommunicationStream(
					socket.getInputStream(),
					socket.getOutputStream());

			/* receive command */
			/* process commands until disconnect */
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

	    disconnectClient();
	}

	private void processCommand(Command cmd) throws IOException {
		switch (cmd.getType()) {
		case Join:
			processCommand((JoinCommand)cmd);
			break;

		// TODO access real value
		case Unknown: default:
			debug.print(DebugLevel.Err, "unrecognized command", cmd.getType());
		}
	}

	private void processCommand(JoinCommand cmd) throws IOException {
		String nickname = cmd.getNick();
		Optional<Client> found = game.getClients().stream()
				.filter(c -> c.getPlayer().getNickname() == nickname)
				.findAny();
		boolean nicknameFound = found.isPresent();
		Client cl = nicknameFound ? found.get() : null;

		if (game.isStarted()) {
			debug.print(DebugLevel.Info, "join", "Game already in progress");

			if (nicknameFound) {
				if (cl.getPlayer().isConnected()) {
					debug.print(DebugLevel.Info, "join", "Player rejoins");
					cl.getPlayer().setConnected(true);

					joinOk(cl);
				} else {
					returnNicknameTaken(nickname);
				}
			} else {
				debug.print(DebugLevel.Info,
						"join", "new player not allowed to join during game: "
								+ nickname);
			    JoinReply.GameInProgress.send(comm);
			}
		} else {
			if (nicknameFound) {
				returnNicknameTaken(nickname);
			} else {
				debug.print(DebugLevel.Info, "join", "New player: " + nickname);
				cl = new Client(nickname, game);

				/* Notify all other clients of the new player
	             * and then add the new client to the array */
	            /* In that way only clients that have already joined
	             * are going to receive the notification */
				game.allAddUpdate(new PlayerUpdate(
						Update.Type.AddPlayer, cl.getPlayer()));

				game.getClients().add(cl);

				joinOk(cl);
			}
		}
	}

	private void joinOk(Client cl) throws IOException {
		client = cl;

		JoinReply.Ok.send(comm);
		cl.getPlayer().getId().send(comm);

		/* Tell the new client about
		 * all existing clients,
		 * current config,
		 * possible abilities
		 */
		// TODO locking
		for (Client other : game.getClients())
			cl.getUpdates().add(
					new PlayerUpdate(Update.Type.AddPlayer,
							other.getPlayer()));
		// TODO add config updates
		// TODO add ability updates
	}

	private void returnNicknameTaken(String nickname) throws IOException {
		debug.print(DebugLevel.Info, "join", "Nickname already taken: " + nickname);
		JoinReply.NicknameTaken.send(comm);
	}

	private void disconnectClient() {
		// TODO Auto-generated method stub

	}
}
