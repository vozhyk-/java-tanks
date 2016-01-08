package re.neutrino.java_tanks.client;

import java.io.IOException;
import java.net.Socket;

import re.neutrino.java_tanks.debug.DebugLevel;
import re.neutrino.java_tanks.types.*;
import re.neutrino.java_tanks.types.basic.*;
import re.neutrino.java_tanks.types.commands.*;
import re.neutrino.java_tanks.types.updates.Update;

public class ClientConnection {
	Socket socket;
	CommunicationStream comm;
	
	public ClientConnection(Socket socket) {
		this.socket = socket;
		try {
			comm = new CommunicationStream(
					socket.getInputStream(),
					socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
				//Main.PlayerID=jr.getClient().getPlayer().getId();
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
			Main.map = GameMap.recv(comm);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void fetch_changes() {
		try {
			Main.updates = UpdateQueue.recv(comm);
			for (Update i:Main.updates) {
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
