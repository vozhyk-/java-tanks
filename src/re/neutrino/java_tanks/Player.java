package re.neutrino.java_tanks;

public class Player {
	State state;
	boolean isConnected;
	//int16_t
	final short id;
	String nickname;
	//int16_t
	short hitpoints;
	MapPosition pos;
	// ability_id
	Ability ability;
	//int16_t
	short abilityCooldown;
	
	public Player(short id, String nickname, short hitpoints, MapPosition pos, Ability ability) {
		this.id = id;
		this.nickname = nickname;
		this.hitpoints = hitpoints;
		this.pos = pos;
		this.ability = ability;
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
