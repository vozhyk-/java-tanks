package re.neutrino.java_tanks.types.updates;

import java.io.IOException;

import re.neutrino.java_tanks.types.*;

public class ConfigUpdate extends UpdateWithArgument<NetConfigOption> {
	@Override
	public Type getType() {
		return Type.Config;
	}

	public ConfigUpdate(NetConfigOption option) {
		super(option);
	}

	public NetConfigOption getOption() {
		return arg;
	}

	public static ConfigUpdate recvRest(CommunicationStream comm) throws IOException {
		return new ConfigUpdate(NetConfigOption.recv(comm));
	}
}
