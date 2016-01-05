package re.neutrino.java_tanks.types;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CommunicationStream {
	private InputStream in;
	private OutputStream out;

	public CommunicationStream(InputStream in, OutputStream out) {
		this.in = in;
		this.out = out;
	}
	
	/*
	@FunctionalInterface
	interface TriFunction<A,B,C,R> {
	    R apply(A a, B b, C c);
	}
	*/
	
	/* do_all: sends/receives the whole buffer.
	 *         Do not use outside this file.
	 *
	 * action:            either read or write
	 * data, len:         standard arguments to read and write
	 *
	 * Returns the number of bytes actually processed
	 */
	/*
	int doAll(TriFunction<char[], Integer, Integer, Integer> action, char[] data, int len)
			throws IOException {
		int processed = 0;
	    int bytesleft = len; // how many we have left to send/receive
	    int n = -1;

	    while (processed < len)
	    {
	        n = action.apply(data, processed, bytesleft);
	        if (n == -1)
	        	throw new java.io.EOFException("Connection closed gracefully by peer");
	        processed += n;
	        bytesleft -= n;
	    }

	    return n == -1 ? -1 : processed;
	}
	*/
	
	/* recvall: receives the whole buffer. See do_all */
	/*
	int readAll(char[] data, int len) throws IOException
	{
	    return doAll(
	    		(char[] data_, Integer off, Integer len_) -> read(data, off, len),
	    		data, len);
	}
	*/

	public void sendAll(byte[] buf, int len) throws IOException {
		// TODO Confirm everything gets sent
		out.write(buf, 0, len);
	}
	
	public void sendAll(byte[] buf) throws IOException {
		sendAll(buf, buf.length);
	}
	
	public int recvAll(byte[] buf, int len) throws IOException {
		int processed = 0;
	    int bytesleft = len; // how many we have left to send/receive
	    int n = -1;

	    while (processed < len)
	    {
	        n = in.read(buf, processed, bytesleft);
	        if (n == -1)
	        	throw new java.io.EOFException("Connection closed gracefully by peer");
	        processed += n;
	        bytesleft -= n;
	    }

	    return processed;
	}
	
	public int recvAll(byte[] buf) throws IOException {
		return recvAll(buf, buf.length);
	}
}
