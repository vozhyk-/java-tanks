package re.neutrino.java_tanks.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	ArrayList<Game> games = new ArrayList<Game>();
	private Config config;

	public Server(Config config) {
		this.config = config;
	}

	public void listen() {
		games.add(new Game(config));
		
		try (
			// TODO fix comment or socket options
			/* start listening, allowing a queue of up to 16 pending connection */
		    ServerSocket serverSocket = new ServerSocket();
		    //debug_s( 3, "listen", "Server started listening");
		    //debug_d( 3, "listen port", server_port);
		) {
		    while (true) {
		    	Socket socket = null;
		    	try {
		    		socket = serverSocket.accept();
		    	} catch (IOException e) {
		    		continue;
		    	}
		    	
		    	//debug_s( 3, "incoming connection", inet_ntoa(client_sa.sin_addr));
		    	
		    	new Thread(new ConnectionThread(socket)).start();
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
