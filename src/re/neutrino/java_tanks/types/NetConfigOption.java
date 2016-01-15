package re.neutrino.java_tanks.types;

import java.io.IOException;

import re.neutrino.java_tanks.types.basic.Int32;
import re.neutrino.java_tanks.types.basic.NetString;

public class NetConfigOption implements Communicable {
	final NetString name;
	final Int32 value;

	public NetConfigOption(NetString optionName, Int32 optionValue) {
		this.name = optionName;
		this.value = optionValue;
	}

	public NetConfigOption(String optionName, int optionValue) {
		this(new NetString(optionName), new Int32(optionValue));
	}

	public String getName() {
		return name.getValue();
	}

	public int getValue() {
		return value.getValue();
	}

	@Override
	public void send(CommunicationStream comm) throws IOException {
		name.send(comm);
		value.send(comm);
	}

	public static NetConfigOption recv(CommunicationStream comm)
			throws IOException {
		return new NetConfigOption(NetString.recv(comm), Int32.recv(comm));
	}

	@Override
	public String toString() {
		return name + " = " + value;
	}
}
