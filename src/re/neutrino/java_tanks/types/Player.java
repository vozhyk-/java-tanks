package re.neutrino.java_tanks.types;

import re.neutrino.java_tanks.types.Player.State;
import re.neutrino.java_tanks.types.basic.*;

public class Player {
	State state;
	boolean isConnected;
	final Int16 id;
	NetString nickname;
	Int16 hitpoints;
	MapPosition pos;
	Int16 abilityId;
	Int16 abilityCooldown;

	public Player(
			State state,
			boolean isConnected,
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
			isConnected,
			new Int16(id),
			new NetString(nickname),
			new Int16(hitpoints),
			pos,
			new Int16(abilityId),
			new Int16(abilityCooldown));
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	public Int16 getId() {
		return id;
	}

	public String getNickname() {
		return nickname.getValue();
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public enum State {
		NoPlayer, Joined, Ready, Waiting, Active, Dead,
		Winner, Loser
	}
}
