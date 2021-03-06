package re.neutrino.java_tanks.types.basic;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import re.neutrino.java_tanks.types.*;

public class NetString extends WrappedType<String> implements Communicable{
	static final Charset charset = Charset.forName("UTF-8");
	static final byte[] zero = { '\0' };

	public NetString(String value) {
		super(value);
	}

	@Override
	public void send(CommunicationStream comm) throws IOException {
		byte[] buf = value.getBytes(charset);
		short len = (short) (buf.length + 1);

		new Int16(len).send(comm);
		comm.sendAll(buf);
		comm.sendAll(zero);
	}

	public static NetString recv(CommunicationStream comm) throws IOException {
		short len = Int16.recv(comm).getSimpleValue();

		byte[] buf = new byte[len];
		comm.recvAll(buf);
		byte[] withoutZero = Arrays.copyOfRange(buf, 0, len - 1);
		return new NetString(
				new String(withoutZero, charset));
	}
}
