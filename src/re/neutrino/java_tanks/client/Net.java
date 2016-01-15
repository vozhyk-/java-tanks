package re.neutrino.java_tanks.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import re.neutrino.java_tanks.debug.DebugLevel;
import re.neutrino.java_tanks.types.*;
import re.neutrino.java_tanks.types.commands.*;
import re.neutrino.java_tanks.types.updates.*;

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

	void after_loc_player_update() {
		Main.debug.print(DebugLevel.Debug, "State", PlayersList.loc_player.getState());
		if (Main.GUIframe.cur_panel == Main.GUIframe.Lobby) {
			switch (PlayersList.loc_player.getState()) {
				case Active:
				case Waiting:
					Main.debug.print(DebugLevel.Debug, "Game start");
					Main.GUIframe.changePane(Main.GUIframe.Game);
					((GamePanel) Main.GUIframe.Game).timer.start();
					break;
				default:
					((LobbyPanel) Main.GUIframe.Lobby).update_player_list();
			}
		}
		if (Main.GUIframe.cur_panel == Main.GUIframe.Game) {
			((GamePanel) Main.GUIframe.Game).update_scoreboard();
			switch (PlayersList.loc_player.getState()) {
				case Active:
					Main.debug.print(DebugLevel.Debug, "Activate buttons");
					((GamePanel) Main.GUIframe.Game).set_shoot_buttons(true);
					break;
				case Waiting:
					Main.debug.print(DebugLevel.Debug, "Deactivate buttons");
					((GamePanel) Main.GUIframe.Game).set_shoot_buttons(false);
					break;
				default:
					Main.debug.print(DebugLevel.Warn, "Unknown state");
			}
		}

	}

	public class ChangesThread implements Runnable {
		private volatile boolean running = true;
		Thread shotThread;
		ShotUpdate su;

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
						case Player:
							Game.players.update((PlayerUpdate) i);
							if (Game.players.is_loc_player((PlayerUpdate) i)) {
								after_loc_player_update();
							}
							break;
						case DelPlayer:
							Game.players.delete((PlayerUpdate) i);
							break;
						case Shot:
							su = (ShotUpdate) i;
							break;
						case ShotImpact:
							shotThread = new Thread(((GameApplet) ((GamePanel)
									Main.GUIframe.Game).game_a)
									.create_shot_thread(su, (ShotImpactUpdate) i));
							shotThread.start();
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
				synchronized (comm) {
					if (!socket.isConnected() || socket.isClosed())
						break;
					fetch_changes();
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					running=false;
				}
			}
		}
	}

	public void send_shot(Integer power, Integer angle) {
		Shot s = new Shot((short) angle.intValue(), (short) power.intValue());
		synchronized (comm) {
			send_command(new ShootCommand(s));
		}
	}
}
