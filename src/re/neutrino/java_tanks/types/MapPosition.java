package re.neutrino.java_tanks.types;

import java.io.IOException;

import re.neutrino.java_tanks.types.basic.*;

public class MapPosition implements Communicable {
	final Int16 x;
	final Int16 y;

	public MapPosition(Int16 x, Int16 y) {
		this.x = x;
		this.y = y;
	}

	public MapPosition(short x, short y) {
		this.x = new Int16(x);
		this.y = new Int16(y);
	}

	public short getX() {
		return x.getSimpleValue();
	}

	public short getY() {
		return y.getSimpleValue();
	}

	@Override
	public void send(CommunicationStream comm) throws IOException {
		x.send(comm);
		y.send(comm);
	}

	public static MapPosition recv(CommunicationStream comm) throws IOException {
		return new MapPosition(Int16.recv(comm), Int16.recv(comm));
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	public FloatPair toFloatPair() {
		return new FloatPair(x.getValue() + 0.5, y.getValue() + 0.5);
	}
}
