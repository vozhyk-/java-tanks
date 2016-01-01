package re.neutrino.java_tanks;

public class MapPosition {
	//int16_t
	final short x;
	//int16_t
	final short y;

	public MapPosition(short x, short y) {
		this.x = x;
		this.y = y;
	}

	public short getX() {
		return x;
	}

	public short getY() {
		return y;
	}
}
