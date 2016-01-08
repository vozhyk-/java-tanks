package re.neutrino.java_tanks.types;

import java.io.IOException;

import re.neutrino.java_tanks.types.basic.*;

public class Player implements Communicable {
	State state;
	Bool isConnected;
	final Int16 id;
	NetString nickname;
	Int16 hitpoints;
	MapPosition pos;
	Int16 abilityId;
	Int16 abilityCooldown;

	public Player(
			State state,
			Bool isConnected,
			Int16 id,
			NetString nickname,
			Int16 hitpoints,
			MapPosition pos,
			Int16 abilityId,
			Int16 abilityCooldown) {
		this.state = state;
		this.isConnected = isConnected;
		this.id = id;
		this.nickname = nickname;
		this.hitpoints = hitpoints;
		this.pos = pos;
		this.abilityId = abilityId;
		this.abilityCooldown = abilityCooldown;
	}

	public Player(
			State state,
			boolean isConnected,
			short id,
			String nickname,
			short hitpoints,
			MapPosition pos,
			short abilityId,
			short abilityCooldown) {
		this(
			state,
			new Bool(isConnected),
			new Int16(id),
			new NetString(nickname),
			new Int16(hitpoints),
			pos,
			new Int16(abilityId),
			new Int16(abilityCooldown));
	}

	public boolean equals(Player other) {
		return id.equals(other.id);
	}

	@Override
	public String toString() {
		return "Player#" + id + "(" + nickname + ")";
	}

	@Override
	public void send(CommunicationStream comm) throws IOException {
		state.send(comm);
		isConnected.send(comm);
		id.send(comm);
		nickname.send(comm);
		hitpoints.send(comm);
		pos.send(comm);
		abilityId.send(comm);
		abilityCooldown.send(comm);
	}

	public static Player recv(CommunicationStream comm) throws IOException {
		return new Player(
				State.recv(comm),
				Bool.recv(comm),
				Int16.recv(comm),
				NetString.recv(comm),
				Int16.recv(comm),
				MapPosition.recv(comm),
				Int16.recv(comm),
				Int16.recv(comm));
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public boolean isConnected() {
		return isConnected.getValue();
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = new Bool(isConnected);
	}

	public Int16 getId() {
		return id;
	}

	public String getNickname() {
		return nickname.getValue();
	}

	public MapPosition getPos() {
		return pos;
	}

	public enum State implements CommunicableEnum<State> {
		NoPlayer, Joined, Ready, Waiting, Active, Dead,
		Winner, Loser;

		static State[] values = values();

		public static State recv(CommunicationStream comm) throws IOException {
			return CommunicableEnum.recv(comm, values);
		}
	}
}
