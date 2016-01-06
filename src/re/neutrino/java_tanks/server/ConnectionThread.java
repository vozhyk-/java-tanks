package re.neutrino.java_tanks.server;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

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

	@Override
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

		case GetChanges:
			processCommand((GetChangesCommand)cmd);
			break;

		case GetMap:
			processCommand((GetMapCommand)cmd);
			break;

		// TODO access real value
		case Unknown: default:
			debug.print(DebugLevel.Err, "unrecognized command", cmd.getType());
		}
	}

	private void processCommand(JoinCommand cmd) throws IOException {
		String nickname = cmd.getNickname();
		JoinReply jr = game.tryJoin(nickname);

		if (jr.getType() == JoinReply.Type.Ok)
			client = jr.getClient();

		jr.send(comm);
	}

	private void processCommand(GetMapCommand cmd) throws IOException {
		debug.print(DebugLevel.Info,
				"send map", "Received GetMap. Sending map...");
		game.getMap().send(comm);
	}

	private void processCommand(GetChangesCommand cmd) throws IOException {
		client.getUpdates().sendAndClear(comm);
	}

	private void disconnectClient() {
		// TODO Auto-generated method stub

	}
}
