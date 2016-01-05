package re.neutrino.java_tanks.debug;

import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class DebugStream {
	// TODO Make configurable
	DebugLevel minDebugLevel = DebugLevel.Info;
	
	PrintWriter out;
	
	public DebugStream(String filename) throws IOException {
		out = new PrintWriter(
				new BufferedWriter(
						new FileWriter(filename, true)));
	}
	
	public void print(DebugLevel level, String title, Object value) {
		if (level.compareTo(minDebugLevel) >= 0) {
			printTitle(level, title);
			out.println(" " + value);
			
			out.flush();
		}
	}
	
	public void print(DebugLevel level, String title) {
		if (level.compareTo(minDebugLevel) >= 0) {
			printTitle(level, title);
			
			out.flush();
		}
	}
	
	void printTitle(DebugLevel level, String title) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = format.format(LocalDateTime.now());
		
		out.print(date + " [" + title + "]");
	}
	
	public void close() {
		out.close();
	}
}
