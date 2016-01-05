package re.neutrino.java_tanks.types.commands;

import re.neutrino.java_tanks.types.*;

public class GetChangesCommand extends CommandWithoutArguments {
	public Type getType() {
		return Type.GetChanges;
	}

	public static GetChangesCommand recvRest(CommunicationStream comm) {
		return new GetChangesCommand();
	}
}
