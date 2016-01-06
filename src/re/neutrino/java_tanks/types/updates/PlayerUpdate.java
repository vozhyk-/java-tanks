package re.neutrino.java_tanks.types.updates;

import java.io.IOException;

import re.neutrino.java_tanks.types.*;

public class PlayerUpdate extends TypedUpdateWithArgument<Player> {
	public PlayerUpdate(Type type, Player player) {
		super(type, player);
	}

	public static PlayerUpdate recvRest(CommunicationStream comm, Type type) throws IOException {
		return new PlayerUpdate(type, Player.recv(comm));
	}

	public Player getPlayer() {
		return arg;
	}
}
