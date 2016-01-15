package re.neutrino.java_tanks.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import re.neutrino.java_tanks.Config;
import re.neutrino.java_tanks.debug.*;

public class Server {
	private Config config;
	private DebugStream debug;
	GameList games;

	public Server(Config config, DebugStream debug) {
		this.config = config;
		this.debug = debug;
		games = new GameList(config, debug);
	}

	public void listen() {
		Game game = new Game(config, debug);
		games.add(game);

		// TODO specify port
		int port = 7979;

		try (
			/* start listening, allowing a queue of up to 50 pending connections */
		    ServerSocket serverSocket = new ServerSocket();
		) {
			serverSocket.setReuseAddress(true);
			serverSocket.bind(new InetSocketAddress(port));

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
		    			"incoming connection", socket.getRemoteSocketAddress());

		    	new Thread(new ConnectionThread(games, socket, debug)).start();
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
