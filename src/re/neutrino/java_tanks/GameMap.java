package re.neutrino.java_tanks;

public class GameMap {
	public static class Info {
		//u_int32_t
		final long seed;
	    //u_int16_t
		final int length;
	    //u_int16_t
		final int height;
		
		public Info(long seed, int length, int height) {
			this.seed = seed;
			this.length = length;
			this.height = height;
		}

		public long getSeed() {
			return seed;
		}

		public int getLength() {
			return length;
		}

		public int getHeight() {
			return height;
		}
	}

	public static GameMap generate(Info mapInfo) {
		// TODO Auto-generated method stub
		return null;
	}
}
