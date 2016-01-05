package re.neutrino.java_tanks.types;

import re.neutrino.java_tanks.types.basic.NetString;

public class Ability {
	//int16_t
	short id;
	NetString name;
    Type type;
    //int16_t
    short cooldown;
    //int8_t
    byte params_count;
    //int32_t[]
    int[] params;
    
	public enum Type {
		None, DoubleShot, Move, Snipe
	}
}
