package re.neutrino.java_tanks;

public class WrappedType<T> {
	T value;
	
	WrappedType(T value) {
		this.value = value;
	}
	
	public T getValue() {
		return value;
	}
}
