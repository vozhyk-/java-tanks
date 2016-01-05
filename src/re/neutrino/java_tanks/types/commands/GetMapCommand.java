package re.neutrino.java_tanks.types.commands;

import re.neutrino.java_tanks.types.CommunicationStream;

public class GetMapCommand extends CommandWithoutArguments {
	public Type getType() {
		return Type.GetMap;
	}

	public static GetMapCommand recvRest(CommunicationStream comm) {
		return new GetMapCommand();
	}
}
