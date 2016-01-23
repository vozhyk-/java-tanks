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

		@Override
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
		return get((short)x);
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
		// TODO get type from config
		int type = 0;
		if (type == 0) {
			generate_flat();
		} else if (type == 1) {
			generate_with_slope(true);
		} else if (type == 2) {
			generate_with_slope(false);
		}
	}

	private void generate_flat() {
		int cur_height = info.getHeight() / 2;

		Random rand = new Random(info.getSeed());

		for (int i = 0; i < info.getLength(); i++)
		{
			content[i] = (short) (cur_height + (rand.nextInt() % 2));
			cur_height = content[i];
		}
	}

	private void generate_with_slope(boolean up) {
		int cur_height = info.getHeight() / 2;
		int change_start;
		int change_end;
		if (up) {
			change_start = info.getLength() / 3;
			change_end =  info.getLength() - change_start;
		} else {
			change_end =  info.getLength() / 3;
			change_start = info.getLength() - change_end;
		}
		int slope = info.getLength() / 10;

		Random rand = new Random(info.getSeed());

		for (int i = 0; i < info.getLength(); i++)
		{
			if (i > change_start && i < change_start+slope) {
				content[i] = (short) (cur_height - Math.abs((rand.nextInt() % 3)));
			} else if (i > change_end-slope && i < change_end) {
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
