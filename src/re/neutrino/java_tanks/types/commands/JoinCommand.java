package re.neutrino.java_tanks.types.commands;

import java.io.IOException;

import re.neutrino.java_tanks.types.CommunicationStream;
import re.neutrino.java_tanks.types.basic.NetString;

public class JoinCommand extends CommandWithArgument<NetString> {
	public JoinCommand(NetString nick) {
		arg = nick;
	}

	public Type getType() {
		return Type.Join;
	}
	
	public String getNick() {
		return arg.getValue();
	}
	
	public static JoinCommand recvRest(CommunicationStream comm) throws IOException {
		return new JoinCommand(NetString.recv(comm));
	}
}
