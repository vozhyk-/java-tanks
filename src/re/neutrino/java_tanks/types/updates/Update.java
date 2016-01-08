package re.neutrino.java_tanks.types.updates;

import java.io.IOException;

import re.neutrino.java_tanks.types.*;

public abstract class Update implements Communicable {
	public abstract Type getType();
	@Override
	public abstract String toString();

	public abstract void sendRest(CommunicationStream comm) throws IOException;

	@Override
	public void send(CommunicationStream comm) throws IOException {
		getType().send(comm);
		sendRest(comm);
	}

	public static Update recv(CommunicationStream comm) throws IOException {
		Type type = Type.recv(comm);

		switch(type) {
		case Empty: return EmptyUpdate.recvRest(comm);
	    case Config: return ConfigUpdate.recvRest(comm);
	    //case Ability: return AbilityUpdate.recvRest(comm);
	    //case Shot: case AbilityUse:
	    //	return ShotUpdate.recvRest(comm, type);
	    //case Impact: return ImpactUpdate.recvRest(comm);
	    //case Map: return MapUpdate.recvRest(comm);
	    case Player: case AddPlayer: case DelPlayer:
	    	return PlayerUpdate.recvRest(comm, type);
		}
		return null;
	}

	public enum Type implements CommunicableEnum<Type> {
		Empty,
	    Config, Ability,
	    Shot, AbilityUse,
	    Impact, Map,
	    Player, AddPlayer, DelPlayer;

		static Type[] values = values();

		public static Type recv(CommunicationStream comm) throws IOException {
			return CommunicableEnum.recv(comm, values);
		}
	}
}
