package re.neutrino.java_tanks.types.commands;

import re.neutrino.java_tanks.types.CommunicationStream;

public class ReadyCommand extends CommandWithoutArguments {
	public Type getType() {
		return Type.Ready;
	}
	
	public static ReadyCommand recvRest(CommunicationStream comm) {
		return new ReadyCommand();
	}
}
