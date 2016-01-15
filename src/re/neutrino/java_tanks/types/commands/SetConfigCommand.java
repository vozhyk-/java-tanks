package re.neutrino.java_tanks.types.commands;

import java.io.IOException;

import re.neutrino.java_tanks.types.CommunicationStream;
import re.neutrino.java_tanks.types.NetConfigOption;

/**
 * Same structure as ConfigUpdate
 */
public class SetConfigCommand extends CommandWithArgument<NetConfigOption> {
	@Override
	public Type getType() {
		return Type.SetConfig;
	}

	public SetConfigCommand(NetConfigOption option) {
		super(option);
	}

	public NetConfigOption getOption() {
		return arg;
	}

	public static SetConfigCommand recvRest(CommunicationStream comm)
			throws IOException {
		return new SetConfigCommand(NetConfigOption.recv(comm));
	}
}
