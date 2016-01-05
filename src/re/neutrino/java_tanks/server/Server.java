package re.neutrino.java_tanks.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import re.neutrino.java_tanks.debug.*;

public class Server {
	ArrayList<Game> games = new ArrayList<Game>();
	private Config config;
	private DebugStream debug;

	public Server(Config config, DebugStream debug) {
		this.config = config;
		this.debug = debug;
	}

	public void listen() {
		games.add(new Game(config));

		// TODO specify port
		int port = 7979;
		
		try (
			/* start listening, allowing a queue of up to 50 pending connections */
		    ServerSocket serverSocket = new ServerSocket(port);
		) {
		    debug.print(DebugLevel.Info, "listen", "Server started listening");
		    debug.print(DebugLevel.Info,
		    		"listen port", serverSocket.getLocalPort());
		    while (true) {
		    	Socket socket = null;
		    	try {
		    		socket = serverSocket.accept();
		    	} catch (IOException e) {
		    		continue;
		    	}
		    	
		    	debug.print(DebugLevel.Info,
		    			"incoming connection", socket.getInetAddress());
		    	
		    	new Thread(new ConnectionThread(socket, debug)).start();
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
