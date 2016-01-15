package re.neutrino.java_tanks.types.commands;

import java.io.IOException;

import re.neutrino.java_tanks.types.CommunicationStream;

public class NewGameCommand extends CommandWithoutArguments {
	@Override
	public Type getType() {
		return Type.NewGame;
	}

	public static NewGameCommand recvRest(CommunicationStream comm) throws IOException {
		return new NewGameCommand();
	}
}
