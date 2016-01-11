package re.neutrino.java_tanks.types.updates;

import java.io.IOException;

import re.neutrino.java_tanks.types.CommunicationStream;
import re.neutrino.java_tanks.types.basic.Int16;

public class MapUpdate extends Update {
	@Override
	public Type getType() {
		return Type.Map;
	}

	final Int16 x;
	final Int16 newHeight;

	public MapUpdate(Int16 x, Int16 newHeight) {
		this.x = x;
		this.newHeight = newHeight;
	}

	public MapUpdate(short x, short newHeight) {
		this(new Int16(x), new Int16(newHeight));
	}

	public short getX() {
		return x.getValue();
	}

	public short getNewHeight() {
		return newHeight.getValue();
	}

	@Override
	public void sendRest(CommunicationStream comm) throws IOException {
		x.send(comm);
		newHeight.send(comm);
	}

	public static MapUpdate recvRest(CommunicationStream comm) throws IOException {
		return new MapUpdate(Int16.recv(comm), Int16.recv(comm));
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
}
