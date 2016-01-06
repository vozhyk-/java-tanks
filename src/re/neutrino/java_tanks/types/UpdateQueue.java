package re.neutrino.java_tanks.types;

import java.io.IOException;
import java.util.LinkedList;

import re.neutrino.java_tanks.types.updates.EmptyUpdate;
import re.neutrino.java_tanks.types.updates.Update;

public class UpdateQueue extends LinkedList<Update> implements Communicable {
	private static final long serialVersionUID = -6915660740227306272L;

	@Override
	public void send(CommunicationStream comm) throws IOException {
		for (Update u : this) {
			// TODO Replace with global debug or delete
			//System.out.println("Sending " + u);
			u.send(comm);
		}
		//System.out.println("Sending " + new EmptyUpdate());
		new EmptyUpdate().send(comm);
	}

	public void sendAndClear(CommunicationStream comm) throws IOException {
		// TODO locking
		send(comm);
		clear();
	}

	public static UpdateQueue recv(CommunicationStream comm) throws IOException {
		UpdateQueue result = new UpdateQueue();

		Update u;
		while ((u = Update.recv(comm)).getType() != Update.Type.Empty)
			result.add(u);

		return result;
	}
}
