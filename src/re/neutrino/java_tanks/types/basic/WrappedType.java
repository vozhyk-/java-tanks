package re.neutrino.java_tanks.types.basic;

public class WrappedType<T> {
	protected T value;
	
	public WrappedType(T value) {
		this.value = value;
	}
	
	public T getValue() {
		return value;
	}
	
	/* Subclasses are advised to implement getSimpleValue()
	 * if the intended wrapped type is intended to be different than given,
	 * e.g. byte for WrappedType<Byte>
	 */
	
	/*
	public TT getSimpleValue() {
		return value;
	}
	*/
}
