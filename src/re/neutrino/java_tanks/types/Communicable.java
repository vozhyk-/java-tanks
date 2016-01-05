package re.neutrino.java_tanks.types;

import java.io.IOException;

public interface Communicable {
	public void send(CommunicationStream comm) throws IOException;
	// Implementers should also implement:
	// public static T recv(CommunicationStream comm) throws IOException;
}
