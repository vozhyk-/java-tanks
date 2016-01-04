package re.neutrino.java_tanks;

public interface Communicable {
	public void send(CommunicationStream comm);
	// Implementers should also implement:
	// public static T recv(CommunicationStream comm);
}
