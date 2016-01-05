package re.neutrino.java_tanks.types.commands;

import re.neutrino.java_tanks.types.*;

public class UnknownCommand extends CommandWithoutArguments {
	public Type getType() {
		return Type.Unknown;
	}

	public static UnknownCommand recvRest(CommunicationStream comm) {
		return new UnknownCommand();
	}
}
