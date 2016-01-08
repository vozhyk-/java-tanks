package re.neutrino.java_tanks.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import re.neutrino.java_tanks.debug.DebugLevel;
import re.neutrino.java_tanks.types.*;
import re.neutrino.java_tanks.types.commands.*;
import re.neutrino.java_tanks.types.updates.Update;

public class ClientConnection {
	Socket socket;
	CommunicationStream comm;
	private Exception iOException;

	public ClientConnection(String ip) throws Exception {
		iOException = null;
		try {
			Main.debug.print(DebugLevel.Debug, "Try to connect");
			socket = new Socket(ip, 7979);
			comm = new CommunicationStream(
					socket.getInputStream(),
					socket.getOutputStream());
		} catch (UnknownHostException e) {
			Main.debug.print(DebugLevel.Debug, "Host unknown");
			throw iOException;
		} catch (IOException e) {
			Main.debug.print(DebugLevel.Debug, "Can't open socket");
			throw iOException;
		}
		Main.debug.print(DebugLevel.Debug, "Connected");
	}

	void close() {
		try {
			socket.close();
		} catch (IOException e) {
			Main.debug.print(DebugLevel.Err, "socket close");
		}
	}

	void joinServer(String nick) {
		send_command(new JoinCommand(nick));
		try {
			JoinReply jr = JoinReply.recv(comm);
			switch (jr.getType()) {
			case Ok:
				Game.PlayerID = jr.getPlayerId();
				Main.debug.print(DebugLevel.Debug, "Join");
				break;
			case GameInProgress:
				Main.debug.print(DebugLevel.Debug, "Game in progress");
				break;
			case NicknameTaken:
				Main.debug.print(DebugLevel.Debug, "Nickname is taken");
				break;
			default:
				Main.debug.print(DebugLevel.Warn, "Invalid JoinReply");
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void fetch_map() {
		send_command(new GetMapCommand());
		try {
			Game.map = GameMap.recv(comm);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void fetch_changes() {
		send_command(new GetChangesCommand());
		try {
			UpdateQueue uq = UpdateQueue.recv(comm);
			for (Update i:uq) {
				Main.debug.print(DebugLevel.Debug, "recv update", i);
				switch (i.getType()) {
					case Empty:
						break;
					case Config:
						break;
					case Player:
						break;
					default:
						Main.debug.print(DebugLevel.Warn, "Received unknown update");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void send_ready() {
		send_command(new ReadyCommand());
	}

	void send_command(Command cmd) {
		try {
			Main.debug.print(DebugLevel.Debug, "send cmd", cmd);
			cmd.send(comm);
		} catch (IOException e) {
			Main.debug.print(DebugLevel.Err, "cmd send err", cmd);
		}
	}
}
