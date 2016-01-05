package re.neutrino.java_tanks.types;

import java.io.IOException;

import re.neutrino.java_tanks.types.basic.*;

public class GameMap {
	public static class Info implements Communicable {
		final Int32 seed;
		final UInt16 length;
		final UInt16 height;

		public Info(Int32 seed, UInt16 length, UInt16 height) {
			this.seed = seed;
			this.length = length;
			this.height = height;
		}

		public Info(int seed, int length, int height) {
			this.seed = new Int32(seed);
			this.length = new UInt16(length);
			this.height = new UInt16(height);
		}

		public int getSeed() {
			return seed.getSimpleValue();
		}

		public int getLength() {
			return length.getSimpleValue();
		}

		public int getHeight() {
			return height.getSimpleValue();
		}

		public void send(CommunicationStream comm) throws IOException {
			seed.send(comm);
			length.send(comm);
			height.send(comm);
		}

		public static Info recv(CommunicationStream comm) throws IOException {
			return new Info(
					Int32.recv(comm),
					UInt16.recv(comm),
					UInt16.recv(comm));
		}
	}

	public static GameMap generate(Info mapInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	public MapPosition newPlayerPos() {
		// TODO Auto-generated method stub
		return null;
	}
}
