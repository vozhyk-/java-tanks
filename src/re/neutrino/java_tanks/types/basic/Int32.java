package re.neutrino.java_tanks.types.basic;

import java.io.IOException;
import java.nio.ByteBuffer;

import re.neutrino.java_tanks.types.*;

public class Int32 extends WrappedType<Integer> implements Communicable {
	public Int32(Integer value) {
		super(value);
	}

	public int getSimpleValue() {
		return value;
	}

	public void send(CommunicationStream comm) throws IOException {
		byte[] buf = ByteBuffer.allocate(Integer.BYTES).putInt(value).array();

		comm.sendAll(buf);
	}

	public static Int32 recv(CommunicationStream comm) throws IOException {
		byte[] buf = new byte[Integer.BYTES];

		comm.recvAll(buf);

		return new Int32(ByteBuffer.wrap(buf).getInt());
	}
}
