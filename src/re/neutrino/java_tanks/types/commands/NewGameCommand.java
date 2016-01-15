package re.neutrino.java_tanks.types.commands;

import java.io.IOException;

import re.neutrino.java_tanks.types.CommunicationStream;
import re.neutrino.java_tanks.types.basic.NetString;

/**
 * Has the same structure as JoinCommand
 */
public class NewGameCommand extends CommandWithArgument<NetString> {
	@Override
	public Type getType() {
		return Type.NewGame;
	}

	public NewGameCommand(NetString nickname) {
		super(nickname);
	}

	public NewGameCommand(String nickname) {
		super(new NetString(nickname));
	}

	public String getNickname() {
		return arg.getValue();
	}

	public static JoinCommand recvRest(CommunicationStream comm) throws IOException {
		return new JoinCommand(NetString.recv(comm));
	}
}
