package re.neutrino.java_tanks.types;

import java.io.IOException;

import re.neutrino.java_tanks.types.basic.Int8;

/**
 *
 * @param <Subclass> the implementing class
 */
public interface CommunicableEnum<Subclass extends Enum<?>> extends Communicable {
	// TODO C version uses UInt8. But it still works, as the enums are small
	@Override
	@SuppressWarnings("unchecked")
	public default void send(CommunicationStream comm) throws IOException {
		//System.out.println("Sending " + this + " = " + (byte)((T)this).ordinal());
		new Int8((byte)((Subclass)this).ordinal()).send(comm);
	}

	/**
	 * Receives a Subclass.
	 * @param values a list obtained from Subclass.values()
	 * @throws IOException
	 */
	public static<Subclass extends Enum<?>>
	Subclass recv(CommunicationStream comm, Subclass[] values)
			throws IOException {
		return values[Int8.recv(comm).getValue()];
	}
}
