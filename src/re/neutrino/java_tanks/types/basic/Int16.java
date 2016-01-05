package re.neutrino.java_tanks.types.basic;

import java.io.IOException;
import java.nio.ByteBuffer;

import re.neutrino.java_tanks.types.*;

public class Int16 extends WrappedType<Short> implements Communicable {
	public Int16(Short value) {
		super(value);
	}

	public short getSimpleValue() {
		return value;
	}

	public void send(CommunicationStream comm) throws IOException {
		byte[] buf = ByteBuffer.allocate(Short.BYTES).putShort(value).array();

		comm.sendAll(buf);
	}

	public static Int16 recv(CommunicationStream comm) throws IOException {
		byte[] buf = new byte[Short.BYTES];

		comm.recvAll(buf);

		return new Int16(ByteBuffer.wrap(buf).getShort());
	}
}
