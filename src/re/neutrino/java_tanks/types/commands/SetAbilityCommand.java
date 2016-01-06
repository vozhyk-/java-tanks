package re.neutrino.java_tanks.types.commands;

import java.io.IOException;

import re.neutrino.java_tanks.types.*;
import re.neutrino.java_tanks.types.basic.*;

public class SetAbilityCommand extends CommandWithArgument<Int16> {
	public SetAbilityCommand(Int16 abilityId) {
		super(abilityId);
	}

	@Override
	public Type getType() {
		return Type.SetAbility;
	}

	public short getAbilityId() {
		return arg.getValue();
	}

	public static SetAbilityCommand recvRest(CommunicationStream comm) throws IOException {
		return new SetAbilityCommand(Int16.recv(comm));
	}
}
