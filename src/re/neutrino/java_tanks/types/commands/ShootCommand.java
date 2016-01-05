package re.neutrino.java_tanks.types.commands;

import java.io.IOException;

import re.neutrino.java_tanks.types.*;

public class ShootCommand extends CommandWithArgument<Shot> {
	public Type getType() {
		return Type.Shoot;
	}

	public ShootCommand(Shot shot) {
		arg = shot;
	}
	
	public static ShootCommand recvRest(CommunicationStream comm) throws IOException {
		return new ShootCommand(Shot.recv(comm));
	}
}
