package re.neutrino.java_tanks.types.basic;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import re.neutrino.java_tanks.types.Communicable;
import re.neutrino.java_tanks.types.CommunicationStream;

public class UInt16 extends WrappedType<Integer> implements Communicable {
	public UInt16(Integer value) {
		super(value);
	}

	public int getSimpleValue() {
		return value;
	}

	@Override
	public void send(CommunicationStream comm) throws IOException {
		byte[] buf = ByteBuffer.allocate(Integer.BYTES).putInt(value).array();
		// Take 2 least-significant bytes
		byte[] truncated = Arrays.copyOfRange(buf, 2, 4);

		comm.sendAll(truncated);
	}

	public static UInt16 recv(CommunicationStream comm) throws IOException {
		byte[] buf = new byte[Short.BYTES];

		comm.recvAll(buf);

		short signed = ByteBuffer.wrap(buf).getShort();
		int result = signed > 0 ?
				signed :
				-signed + (2 << 16);

		return new UInt16(result);
	}
}
