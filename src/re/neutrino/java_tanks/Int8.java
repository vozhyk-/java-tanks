package re.neutrino.java_tanks;

import java.io.IOException;

public class Int8 extends WrappedType<Byte> implements Communicable {
	Int8(Byte value) {
		super(value);
	}
	
	public byte getSimpleValue() {
		return value;
	}

	public void send(CommunicationStream comm) {
		// TODO Auto-generated method stub
		
	}
	
	public static Int8 recv(CommunicationStream comm) throws IOException {
		byte[] buf = new byte[1];
		
		comm.recvAll(buf, buf.length);
		
		return new Int8(buf[0]);
	}
}
