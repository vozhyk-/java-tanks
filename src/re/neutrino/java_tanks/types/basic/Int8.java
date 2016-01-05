package re.neutrino.java_tanks.types.basic;

import java.io.IOException;
import java.nio.ByteBuffer;

import re.neutrino.java_tanks.types.*;

public class Int8 extends WrappedType<Byte> implements Communicable {
	public Int8(Byte value) {
		super(value);
	}
	
	public byte getSimpleValue() {
		return value;
	}

	public void send(CommunicationStream comm) throws IOException {
		byte[] buf = ByteBuffer.allocate(Byte.BYTES).put(value).array();
		
		comm.sendAll(buf);
	}
	
	public static Int8 recv(CommunicationStream comm) throws IOException {
		byte[] buf = new byte[1];
		
		comm.recvAll(buf);
		
		return new Int8(buf[0]);
	}
}
