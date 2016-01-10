package re.neutrino.java_tanks.types.updates;

import java.io.IOException;

import re.neutrino.java_tanks.types.CommunicationStream;
import re.neutrino.java_tanks.types.basic.Int16;

public class ShotImpactUpdate extends UpdateWithArgument<Int16> {
	@Override
	public Type getType() {
		return Type.ShotImpact;
	}

	public ShotImpactUpdate(Int16 impactT) {
		super(impactT);
	}

	public ShotImpactUpdate(short impactT) {
		this(new Int16(impactT));
	}

	public short getImpactT() {
		return arg.getValue();
	}

	public static ShotImpactUpdate recvRest(CommunicationStream comm) throws IOException {
		return new ShotImpactUpdate(Int16.recv(comm));
	}
}
