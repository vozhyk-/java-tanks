package re.neutrino.java_tanks.types.commands;

import java.io.IOException;

import re.neutrino.java_tanks.types.CommunicationStream;

public abstract class CommandWithoutArguments extends Command {
	public abstract Type getType();

	public void sendRest(CommunicationStream comm) throws IOException {
		// Do nothing
	}

	public String toString() {
		return getType().toString();
	}
}
