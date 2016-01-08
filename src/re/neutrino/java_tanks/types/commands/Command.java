package re.neutrino.java_tanks.types.commands;

import java.io.IOException;

import re.neutrino.java_tanks.types.*;
import re.neutrino.java_tanks.types.basic.*;

public abstract class Command implements Communicable {
	public abstract Type getType();
	@Override
	public abstract String toString();

	public abstract void sendRest(CommunicationStream comm) throws IOException;
	// Subclasses must also implement:
	// public static T recvRest(CommunicationStream comm) throws IOException

	@Override
	public void send(CommunicationStream comm) throws IOException {
		getType().send(comm);
		sendRest(comm);
	}

	public static Command recv(CommunicationStream comm) throws IOException {
		Type type = Type.recv(comm);

		switch(type) {
		case Join:       return JoinCommand.recvRest(comm);
		case Ready:      return ReadyCommand.recvRest(comm);
		case SetAbility: return SetAbilityCommand.recvRest(comm);
		case GetMap:     return GetMapCommand.recvRest(comm);
		case GetChanges: return GetChangesCommand.recvRest(comm);
		case Shoot:      return ShootCommand.recvRest(comm);
		case UseAbility: return UseAbilityCommand.recvRest(comm);
		case GetImpact:  return GetImpactCommand.recvRest(comm);
		case Unknown:    return UnknownCommand.recvRest(comm);
		default:         return new UnknownCommand();
		}
	}

	/*
	 * command         args                 reply
	 *   \- requirements
	 * Join            NetString nickname   JoinReply[, int16_t id]
	 *   sends id only if JoinReply is JR_OK
	 * SetAbility      Int16 ability_id
	 * Ready
	 * GetMap                               MapInfo
	 * GetChanges                           list(Update)
	 *   \- client joined
	 * Shoot           Shot
	 *   \- game started, state == PS_ACTIVE
	 * UseAbility      Shot
	 * GetImpact       Shot                 Int16 impactX
	 *
	 * list(X) means sending / receiving a series of X with an empty X in the end.
	 */
	public enum Type implements Communicable {
		Join('J'),
		Ready('R'),

		SetAbility('a'),

	    GetMap('M'),
	    GetChanges('C'),

	    Shoot('F'),
	    UseAbility('A'),

	    GetImpact('i'), /* used only by bots */

		Unknown('\0');

		private final Int8 realValue;

		private Type(char realValue) {
			this.realValue = new Int8((byte)realValue);
		}

		public char getRealValue() {
			return (char) realValue.getSimpleValue();
		}

		public static Type valueOf(char realValue) {
			switch(realValue) {
			case 'J': return Join;
			case 'R': return Ready;
			case 'a': return SetAbility;
			case 'M': return GetMap;
			case 'C': return GetChanges;
			case 'F': return Shoot;
			case 'A': return UseAbility;
			case 'i': return GetImpact;
			default: return Unknown;
			}
		}

		@Override
		public void send(CommunicationStream comm) throws IOException {
			realValue.send(comm);
		}

		public static Type recv(CommunicationStream comm) throws IOException {
			return valueOf((char)Int8.recv(comm).getSimpleValue());
		}
	}
}
