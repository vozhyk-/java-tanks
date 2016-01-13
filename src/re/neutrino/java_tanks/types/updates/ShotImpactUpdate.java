package re.neutrino.java_tanks.types.updates;

import java.io.IOException;

import re.neutrino.java_tanks.types.CommunicationStream;
import re.neutrino.java_tanks.types.basic.Int16;

public class ShotImpactUpdate extends UpdateWithArgument<Int16> {
	@Override
	public Type getType() {
		return Type.ShotImpact;
	}

	private static final double impactTNetPrecision = 128;

	private ShotImpactUpdate(Int16 impactT) {
		super(impactT);
	}

	public ShotImpactUpdate(double impactT) {
		this(new Int16((short)
				Math.round(impactT * impactTNetPrecision)));
	}

	public double getImpactT() {
		return arg.getValue() / impactTNetPrecision;
	}

	public static ShotImpactUpdate recvRest(CommunicationStream comm) throws IOException {
		return new ShotImpactUpdate(Int16.recv(comm));
	}
}
