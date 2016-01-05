package re.neutrino.java_tanks.server;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

import re.neutrino.java_tanks.*;
import re.neutrino.java_tanks.debug.*;

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
			
			/* receive command - 1 char */
			/* process commands until disconnect */
			while (true) {
				char command;
				try {
					command = (char)Int8.recv(comm).getSimpleValue();
				} catch (EOFException e) {
					break;
				}
			    debug.print(DebugLevel.Debug, "received command", command);
			    processCommand(command);
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

	private void processCommand(char cmd) {
		switch (cmd) {
		default:
			debug.print(DebugLevel.Err, "unrecognized command", cmd);
		}
	}

	private void disconnectClient() {
		// TODO Auto-generated method stub
		
	}
}
