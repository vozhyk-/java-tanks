package re.neutrino.java_tanks.server;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

import re.neutrino.java_tanks.*;
import re.neutrino.java_tanks.debug.*;
import re.neutrino.java_tanks.types.*;
import re.neutrino.java_tanks.types.commands.*;

public class ConnectionThread implements Runnable {
	Socket socket;
	DebugStream debug;
	
	public ConnectionThread(Socket socket, DebugStream debug) {
		this.socket = socket;
		this.debug = debug;
	}

	public void run() {
		try {
			CommunicationStream comm = new CommunicationStream(
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

	private void processCommand(Command cmd) {
		switch (cmd.getType()) {
		// TODO access real value
		case Unknown: default:
			debug.print(DebugLevel.Err, "unrecognized command", cmd.getType());
		}
	}

	private void disconnectClient() {
		// TODO Auto-generated method stub
		
	}
}
