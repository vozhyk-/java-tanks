package re.neutrino.java_tanks.types;

import java.io.IOException;

import re.neutrino.java_tanks.types.basic.*;

public enum JoinReply implements Communicable {
	Ok, GameInProgress, NicknameTaken;

	static JoinReply[] values = JoinReply.values();

	public void send(CommunicationStream comm) throws IOException {
		new Int8((byte)ordinal()).send(comm);
	}

	public static JoinReply recv(CommunicationStream comm) throws IOException {
		return values[Int8.recv(comm).getValue()];
	}
}
