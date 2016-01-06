package re.neutrino.java_tanks.types;

import java.io.IOException;

import re.neutrino.java_tanks.types.basic.Int8;

/**
 *
 * @param <T> the implementing class
 */
public interface CommunicableEnum<T extends Enum<?>> extends Communicable {
	// TODO C version uses UInt8. But it still works, as the enums are small
	@Override
	@SuppressWarnings("unchecked")
	public default void send(CommunicationStream comm) throws IOException {
		//System.out.println("Sending " + this + " = " + (byte)((T)this).ordinal());
		new Int8((byte)((T)this).ordinal()).send(comm);
	}

	/**
	 * Receives a T.
	 * @param values a list obtained from T.values()
	 * @throws IOException
	 */
	public static<T extends Enum<?>> T recv(CommunicationStream comm, T[] values) throws IOException {
		return values[Int8.recv(comm).getValue()];
	}
}
