package re.neutrino.java_tanks.types.commands;

import java.io.IOException;

import re.neutrino.java_tanks.types.*;

public abstract class CommandWithArgument<T extends Communicable> extends Command {
	T arg;

	public CommandWithArgument(T arg) {
		this.arg = arg;
	}

	@Override
	public abstract Type getType();

	@Override
	public void sendRest(CommunicationStream comm) throws IOException {
		arg.send(comm);
	}

	@Override
	public String toString() {
		return getType() + "(" + arg + ")";
	}
}
