package re.neutrino.java_tanks.types.updates;

import java.io.IOException;

import re.neutrino.java_tanks.types.*;
import re.neutrino.java_tanks.types.basic.*;

public class ConfigUpdate extends Update {
	@Override
	public Type getType() {
		return Type.Config;
	}

	final NetString optionName;
	final Int32 optionValue;

	public ConfigUpdate(NetString optionName, Int32 optionValue) {
		this.optionName = optionName;
		this.optionValue = optionValue;
	}

	public ConfigUpdate(String optionName, int optionValue) {
		this(new NetString(optionName), new Int32(optionValue));
	}

	public String getOptionName() {
		return optionName.getValue();
	}

	public int getOptionValue() {
		return optionValue.getValue();
	}

	@Override
	public void sendRest(CommunicationStream comm) throws IOException {
		optionName.send(comm);
		optionValue.send(comm);
	}

	public static ConfigUpdate recvRest(CommunicationStream comm) throws IOException {
		return new ConfigUpdate(NetString.recv(comm), Int32.recv(comm));
	}

	@Override
	public String toString() {
		return getType() + "(" + optionName + " = " + optionValue + ")";
	}
}
