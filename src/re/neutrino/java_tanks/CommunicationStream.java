package re.neutrino.java_tanks;

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
	
	public int recvAll(byte[] data, int len) throws IOException {
		int processed = 0;
	    int bytesleft = len; // how many we have left to send/receive
	    int n = -1;

	    while (processed < len)
	    {
	        n = in.read(data, processed, bytesleft);
	        if (n == -1)
	        	throw new java.io.EOFException("Connection closed gracefully by peer");
	        processed += n;
	        bytesleft -= n;
	    }

	    return processed;
	}
}
