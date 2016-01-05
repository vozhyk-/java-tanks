package re.neutrino.java_tanks.types;

public class PlayerUpdate extends Update {
	final Update.Type type;
	final Player player;

	public PlayerUpdate(Type type, Player player) {
		this.type = type;
		this.player = player;
	}
}
