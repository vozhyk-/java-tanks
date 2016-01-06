package re.neutrino.java_tanks.types.updates;

import java.io.IOException;

import re.neutrino.java_tanks.types.*;

public class EmptyUpdate extends Update {
	@Override
	public Type getType() {
		return Type.Empty;
	}

	@Override
	public void sendRest(CommunicationStream comm) throws IOException {
		// Do nothing
	}

	public static Update recvRest(CommunicationStream comm) {
		return new EmptyUpdate();
	}

	@Override
	public String toString() {
		return getType().toString();
	}
}
