package re.neutrino.java_tanks.debug;

import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DebugStream {
	// TODO Make configurable
	DebugLevel minDebugLevel = DebugLevel.Debug;
	DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
			out.println();

			out.flush();
		}
	}

	void printTitle(DebugLevel level, String title) {
		String date = LocalDateTime.now().format(dateFormat);

		out.print(date + " [" + title + "]");
	}

	public void close() {
		out.close();
	}
}
