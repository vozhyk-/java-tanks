package re.neutrino.java_tanks.types.updates;

import java.io.IOException;

import re.neutrino.java_tanks.types.CommunicationStream;
import re.neutrino.java_tanks.types.basic.NetString;

public class LogUpdate extends UpdateWithArgument<NetString> {
	@Override
	public Type getType() {
		return Type.Log;
	}

	public LogUpdate(NetString line) {
		super(line);
	}

	public LogUpdate(String line) {
		this(new NetString(line));
	}

	public String getLine() {
		return arg.getValue();
	}

	public static LogUpdate recvRest(CommunicationStream comm) throws IOException {
		return new LogUpdate(NetString.recv(comm));
	}
}
