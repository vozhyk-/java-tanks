package re.neutrino.java_tanks.types;

import re.neutrino.java_tanks.types.basic.*;

public class Ability {
	Int16 id;
	NetString name;
    Type type;
    Int16 cooldown;
    Int8 params_count;
    Int32[] params;

	public enum Type {
		None, DoubleShot, Move, Snipe
	}
}
