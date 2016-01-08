package re.neutrino.java_tanks.types;

import java.io.IOException;

import re.neutrino.java_tanks.server.Client;
import re.neutrino.java_tanks.types.basic.Int16;

/**
 * Used for two purposes:
 * * In the server,
 *   as the return value of server.Game.tryJoin().
 *   In this case it contains client if
 *   joining was successful.
 * * In the client,
 *   as the reply from the server for
 *   JoinCommand. In this case it contains
 *   playerId if joining was successful.
 *
 * "joining was successful" === type == Ok
 */
public class JoinReply implements Communicable {
	public static final JoinReply GameInProgress =
			new JoinReply(Type.GameInProgress);
	public static final JoinReply NicknameTaken =
			new JoinReply(Type.NicknameTaken);

	public static JoinReply ok(Client client) {
		return new JoinReply(Type.Ok, client);
	}

	Type type;
	/**
	 * Should be non-null only if type == Ok
	 * and the instance was constructed
	 * (returned from server.Game.tryJoin()).
	 *
	 * Used by the server.
	 */
	Client client;
	/**
	 * Should be non-null only if type == Ok
	 * and the instance was received from the network.
	 *
	 * Used by the client.
	 */
	Int16 playerId;

	public JoinReply(Type type) {
		this.type = type;
	}

	public JoinReply(Type type, Client client) {
		this(type);
		this.client = client;
	}

	private JoinReply(Type type, Int16 playerId) {
		this(type);
		this.playerId = playerId;
	}

	public Type getType() {
		return type;
	}

	public Client getClient() {
		return client;
	}

	public Int16 getPlayerId() {
		return playerId;
	}

	@Override
	public void send(CommunicationStream comm) throws IOException {
		type.send(comm);
		if (type == Type.Ok)
			client.getPlayer().getId().send(comm);
	}

	public static JoinReply recv(CommunicationStream comm) throws IOException {
		Type t = Type.recv(comm);
		if (t == Type.Ok)
			return new JoinReply(t, Int16.recv(comm));
		else
			return new JoinReply(t);
	}

	public enum Type implements CommunicableEnum<Type> {
		Ok, GameInProgress, NicknameTaken;

		static Type[] values = values();

		public static Type recv(CommunicationStream comm) throws IOException {
			return CommunicableEnum.recv(comm, values);
		}
	}
}
