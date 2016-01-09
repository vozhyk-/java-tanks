package re.neutrino.java_tanks.types.basic;

public class Mutable<T> extends WrappedType<T> {
	public Mutable(T value) {
		super(value);
	}

	public T get() {
		return getValue();
	}

	public void set(T value) {
		this.value = value;
	}
}
