package re.neutrino.java_tanks.types;

public class FloatPair {
	// Public fields - this is a structure used in computations
	public final double x;
	public final double y;

	public FloatPair(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	public MapPosition round() {
		return new MapPosition(
				(short)Math.round(x - 0.5),
		        /* Is using floor right?
		         * why - 0.5? IMO without this it'll be alright*/
		        (short)Math.floor(y - 0.5));
	}
}
