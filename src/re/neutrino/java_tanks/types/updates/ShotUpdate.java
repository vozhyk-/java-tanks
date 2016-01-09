package re.neutrino.java_tanks.types.updates;

import java.io.IOException;

import re.neutrino.java_tanks.types.CommunicationStream;
import re.neutrino.java_tanks.types.Player;
import re.neutrino.java_tanks.types.Shot;
import re.neutrino.java_tanks.types.basic.Int16;

public class ShotUpdate extends Update {
	@Override
	public Type getType() {
		return Type.Shot;
	}

	final Int16 playerId;
	final Shot shot;

	public ShotUpdate(Int16 playerId, Shot shot) {
		this.playerId = playerId;
		this.shot = shot;
	}

	public ShotUpdate(Player player, Shot shot) {
		this(player.getId(), shot);
	}

	public Int16 getPlayerId() {
		return playerId;
	}

	public Shot getShot() {
		return shot;
	}

	@Override
	public void sendRest(CommunicationStream comm) throws IOException {
		playerId.send(comm);
		shot.send(comm);
	}

	public static ShotUpdate recvRest(CommunicationStream comm) throws IOException {
		return new ShotUpdate(Int16.recv(comm), Shot.recv(comm));
	}

	@Override
	public String toString() {
		return getType() + "(Player#" + playerId + ", " + shot + ")";
	}
}
