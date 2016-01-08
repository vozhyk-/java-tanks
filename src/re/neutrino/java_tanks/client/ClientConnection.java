package re.neutrino.java_tanks.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import re.neutrino.java_tanks.debug.DebugLevel;
import re.neutrino.java_tanks.types.*;
import re.neutrino.java_tanks.types.basic.*;
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

	void joinServer(String nick) {
		JoinCommand jc = new JoinCommand(new NetString(nick));
		try {
			jc.send(comm);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
				Main.debug.print(DebugLevel.Debug, "Invalid JoinReply");
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void fetch_map() {
		GetMapCommand gm = new GetMapCommand();
		try {
			gm.send(comm);
			Game.map = GameMap.recv(comm);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void fetch_changes() {
		try {
			UpdateQueue uq = UpdateQueue.recv(comm);
			for (Update i:uq) {
				switch (i.getType()) {
					case Empty:
						Main.debug.print(DebugLevel.Debug, "Received empty update");
						break;
					case Config:
						Main.debug.print(DebugLevel.Debug, "Received config update");
						break;
					case Player:
						Main.debug.print(DebugLevel.Debug, "Received player update");
						break;
					default:
						Main.debug.print(DebugLevel.Debug, "Received unknown update");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
