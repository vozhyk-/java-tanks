package re.neutrino.java_tanks.types.commands;

import java.io.IOException;

import re.neutrino.java_tanks.types.*;

public abstract class CommandWithArgument<T extends Communicable> extends Command {
	T arg;

	public abstract Type getType();

	public void sendRest(CommunicationStream comm) throws IOException {
		arg.send(comm);
	}

	public String toString() {
		return getType() + "(" + arg + ")";
	}
}
