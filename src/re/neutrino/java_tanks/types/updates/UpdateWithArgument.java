package re.neutrino.java_tanks.types.updates;

import java.io.IOException;

import re.neutrino.java_tanks.types.Communicable;
import re.neutrino.java_tanks.types.CommunicationStream;

public abstract class UpdateWithArgument<T extends Communicable> extends Update {
	T arg;

	public UpdateWithArgument(T arg) {
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
