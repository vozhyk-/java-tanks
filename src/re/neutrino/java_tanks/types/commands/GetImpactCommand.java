package re.neutrino.java_tanks.types.commands;

import java.io.IOException;

import re.neutrino.java_tanks.types.*;

public class GetImpactCommand extends CommandWithArgument<Shot> {
	public Type getType() {
		return Type.GetImpact;
	}

	public GetImpactCommand(Shot shot) {
		arg = shot;
	}

	public static GetImpactCommand recvRest(CommunicationStream comm) throws IOException {
		return new GetImpactCommand(Shot.recv(comm));
	}
}
