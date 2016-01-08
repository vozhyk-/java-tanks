package re.neutrino.java_tanks.client;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

import re.neutrino.java_tanks.types.*;
import re.neutrino.java_tanks.types.basic.*;
import re.neutrino.java_tanks.types.commands.*;

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
	}
	
	private void sendCommand(char command) {
		//comm.sendInt8(command);
	}
}
