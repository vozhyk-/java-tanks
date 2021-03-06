package re.neutrino.java_tanks.types.commands;

import java.io.IOException;

import re.neutrino.java_tanks.types.*;

public class ShootCommand extends CommandWithArgument<Shot> {
	@Override
	public Type getType() {
		return Type.Shoot;
	}

	public ShootCommand(Shot shot) {
		super(shot);
	}

	public Shot getShot() {
		return arg;
	}

	public static ShootCommand recvRest(CommunicationStream comm) throws IOException {
		return new ShootCommand(Shot.recv(comm));
	}
}
