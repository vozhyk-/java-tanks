package re.neutrino.java_tanks.server;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

import re.neutrino.java_tanks.CommunicationStream;

public class ConnectionThread implements Runnable {
	Socket socket;
	
	public ConnectionThread(Socket socket) {
		this.socket = socket;
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
					command = (char)comm.readInt8();
				} catch (EOFException e) {
					break;
				}
			    //debug_c( 0, "received command", command);
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
	    //debug_s( 3, "client closed connection", "");

	    disconnectClient();
	}

	private void processCommand(char command) {
		// TODO Auto-generated method stub
		
	}

	private void disconnectClient() {
		// TODO Auto-generated method stub
		
	}
}
