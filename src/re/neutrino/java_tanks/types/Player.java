package re.neutrino.java_tanks.types;

import re.neutrino.java_tanks.types.basic.Int16;
import re.neutrino.java_tanks.types.basic.NetString;

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
