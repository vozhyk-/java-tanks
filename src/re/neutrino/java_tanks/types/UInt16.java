package re.neutrino.java_tanks.types;

import java.io.IOException;
import java.nio.ByteBuffer;

import re.neutrino.java_tanks.types.basic.*;

public class UInt16 extends WrappedType<Integer> implements Communicable {
	public UInt16(Integer value) {
		super(value);
	}

	public int getSimpleValue() {
		return value;
	}

	public void send(CommunicationStream comm) throws IOException {
		ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES).putInt(value);
		// Take 2 least-significant bytes
		byte[] truncated = ByteBuffer.wrap(buf.array(), 2, 2).array();
		
		comm.sendAll(truncated);
	}
	
	public static UInt16 recv(CommunicationStream comm) throws IOException {
		byte[] buf = new byte[Short.BYTES];
		
		comm.recvAll(buf);
		
		short signed = ByteBuffer.wrap(buf).getShort();
		int result = signed > 0 ?
				signed :
				(int)-signed + 2 << 16;
		
		return new UInt16(result);
	}
}
