package re.neutrino.java_tanks.types.updates;

import re.neutrino.java_tanks.types.Communicable;

public class TypedUpdateWithArgument<T extends Communicable> extends UpdateWithArgument<T> {
	Type type;

	public TypedUpdateWithArgument(Type type, T arg) {
		super(arg);
		this.type = type;
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return type;
	}
}
