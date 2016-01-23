package re.neutrino.java_tanks.types;

import java.io.IOException;
import java.util.Random;

import re.neutrino.java_tanks.types.basic.Int32;
import re.neutrino.java_tanks.types.basic.UInt16;
import re.neutrino.java_tanks.types.updates.MapUpdate;

public class GameMap implements Communicable {
	public static class Info implements Communicable {
		// TODO Change to UInt32
		final Int32 seed;
		final UInt16 length;
		final UInt16 height;
		final Type type;

		public Info(Int32 seed, UInt16 length, UInt16 height, Type type) {
			this.seed = seed;
			this.length = length;
			this.height = height;
			this.type = type;
		}

		public Info(int seed, int length, int height, Type type) {
			this(
					new Int32(seed),
					new UInt16(length),
					new UInt16(height),
					type);
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

		public Type getType() {
			return type;
		}

		@Override
		public void send(CommunicationStream comm) throws IOException {
			seed.send(comm);
			length.send(comm);
			height.send(comm);
			// Incompatible with C version
			type.send(comm);
		}

		public static Info recv(CommunicationStream comm) throws IOException {
			return new Info(
					Int32.recv(comm),
					UInt16.recv(comm),
					UInt16.recv(comm),
					Type.recv(comm));
		}

		public static enum Type implements CommunicableEnum<Type> {
			Flat, Hill, Valley;

			static Type[] values = values();

			public static Type recv(CommunicationStream comm) throws IOException {
				return CommunicableEnum.recv(comm, values);
			}
		}
	}

	protected final Info info;
	short[] content;

	public GameMap(Info info) {
		this.info = info;

		content = new short[info.getLength()];

		generate();
	}

	public Info getInfo() {
		return info;
	}

	public short get(short x) {
		return content[x];
	}

	public short get(int x) {
		return get((short) x);
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	public short[] get() {
		return content;
	}

	public void set(short x, short newHeight) {
		content[x] = newHeight;
	}

	public void update(MapUpdate u) {
		set(u.getX(), u.getNewHeight());
	}

	public boolean isInside(MapPosition pos) {
		short x = pos.getX();
		return x >= 0 && x < info.getLength();
	}

	private void generate() {
		switch(info.getType()) {
		case Flat:
			generateFlat();
			break;

		case Hill:
			generateWithSlope(true);
			break;

		case Valley:
			generateWithSlope(false);
			break;
		}
	}

	private void generateFlat() {
		int cur_height = info.getHeight() / 2;

		Random rand = new Random(info.getSeed());

		for (int i = 0; i < info.getLength(); i++) {
			content[i] = (short) (cur_height + (rand.nextInt() % 2));
			cur_height = content[i];
		}
	}

	private void generateWithSlope(boolean up) {
		int cur_height = info.getHeight() / 2;
		int change_start;
		int change_end;
		if (up) {
			change_start = info.getLength() / 3;
			change_end = info.getLength() - change_start;
		} else {
			change_end = info.getLength() / 3;
			change_start = info.getLength() - change_end;
		}
		int slope = info.getLength() / 10;

		Random rand = new Random(info.getSeed());

		for (int i = 0; i < info.getLength(); i++) {
			if (i > change_start && i < change_start + slope) {
				content[i] = (short) (cur_height - Math.abs((rand.nextInt() % 3)));
			} else if (i > change_end - slope && i < change_end) {
				content[i] = (short) (cur_height + Math.abs((rand.nextInt() % 3)));
			} else
				content[i] = (short) (cur_height + (rand.nextInt() % 2));
			cur_height = content[i];
		}
	}

	@Override
	public void send(CommunicationStream comm) throws IOException {
		info.send(comm);
	}

	public static GameMap recv(CommunicationStream comm) throws IOException {
		return new GameMap(Info.recv(comm));
	}
}
