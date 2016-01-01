package re.neutrino.java_tanks;

public class Ability {
	//int16_t
	short id;
	String name;
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
