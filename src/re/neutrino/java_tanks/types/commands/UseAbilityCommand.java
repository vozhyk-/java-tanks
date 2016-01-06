package re.neutrino.java_tanks.types.commands;

import java.io.IOException;

import re.neutrino.java_tanks.types.*;

public class UseAbilityCommand extends CommandWithArgument<Shot> {
	@Override
	public Type getType() {
		return Type.UseAbility;
	}

	public UseAbilityCommand(Shot shot) {
		super(shot);
	}

	public static UseAbilityCommand recvRest(CommunicationStream comm) throws IOException {
		return new UseAbilityCommand(Shot.recv(comm));
	}
}
