package re.neutrino.java_tanks.types.basic;

import java.io.IOException;

import re.neutrino.java_tanks.types.*;

public class Bool extends WrappedType<Boolean> implements Communicable {
	public Bool(Boolean value) {
		super(value);
	}

	public boolean getSimpleValue() {
		return value;
	}

	@Override
	public void send(CommunicationStream comm) throws IOException {
		new Int8((byte) (value ? 1 : 0)).send(comm);
	}

	public static Bool recv(CommunicationStream comm) throws IOException {
		return new Bool(
				Int8.recv(comm).getValue()
				!= 0 ? true : false);
	}
}
