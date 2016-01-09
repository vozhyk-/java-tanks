package re.neutrino.java_tanks.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import re.neutrino.java_tanks.debug.DebugLevel;
import re.neutrino.java_tanks.types.*;
import re.neutrino.java_tanks.types.commands.*;
import re.neutrino.java_tanks.types.updates.ConfigUpdate;
import re.neutrino.java_tanks.types.updates.PlayerUpdate;
import re.neutrino.java_tanks.types.updates.Update;

public class Net {
	Socket socket;
	CommunicationStream comm;
	private Exception iOException;
	ChangesThread ct = new ChangesThread();

	public Net(String ip) throws Exception {
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
			synchronized (comm) {
				socket.close();
			}
		} catch (IOException e) {
			Main.debug.print(DebugLevel.Err, "socket close");
		}
	}

	@SuppressWarnings("finally")
	boolean joinServer(String nick) {
		synchronized (comm) {
			send_command(new JoinCommand(nick));
			Boolean ret=true;
			try {
				JoinReply jr = JoinReply.recv(comm);
				switch (jr.getType()) {
				case Ok:
					Game.PlayerID = jr.getPlayerId();
					Main.debug.print(DebugLevel.Debug, "Join");
					break;
				case GameInProgress:
					ret=false;
					Main.debug.print(DebugLevel.Debug, "Game in progress");
					break;
				case NicknameTaken:
					Main.debug.print(DebugLevel.Debug, "Nickname is taken");
					ret=false;
					break;
				default:
					Main.debug.print(DebugLevel.Warn, "Invalid JoinReply");
					ret=false;
					break;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				return ret;
			}
		}
	}

	void fetch_map() {
		synchronized (comm) {
			send_command(new GetMapCommand());
			try {
				Game.map = GameMap.recv(comm);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	void send_ready() {
		synchronized (comm) {
			send_command(new ReadyCommand());
		}
	}

	void send_command(Command cmd) {
		try {
			Main.debug.print(DebugLevel.Debug, "send cmd", cmd);
			cmd.send(comm);
		} catch (IOException e) {
			Main.debug.print(DebugLevel.Err, "cmd send err", cmd);
		}
	}

	public class ChangesThread implements Runnable {
		private volatile boolean running = true;

		void terminate() {
			running=false;
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
							Game.conf.update((ConfigUpdate) i);
							break;
						case AddPlayer:
							Game.players.add((PlayerUpdate) i);
							break;
						case Player:
							Game.players.update((PlayerUpdate) i);
							break;
						default:
							Main.debug.print(DebugLevel.Warn, "Received unknown update", i.getType());
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			while (running) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					running=false;
				}
				synchronized (comm) {
					if (!socket.isConnected() || socket.isClosed() || !running)
						break;
					fetch_changes();
				}
			}
		}
	}
}
