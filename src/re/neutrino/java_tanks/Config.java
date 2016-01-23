package re.neutrino.java_tanks;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import re.neutrino.java_tanks.debug.*;
import re.neutrino.java_tanks.types.FloatPair;
import re.neutrino.java_tanks.types.NetConfigOption;
import re.neutrino.java_tanks.types.Shot;
import re.neutrino.java_tanks.types.commands.SetConfigCommand;
import re.neutrino.java_tanks.types.updates.ConfigUpdate;

public class Config {
	List<Item> items = Arrays.asList(
		new Item("map_width", 128, 48, 1024),
	    new Item("map_height", 64, 48, 512),
	    new Item("map_type", 0, 0, 2),
	    new Item("tank_hp", 100, 1, 1000),
	    new Item("dmg_radius", 4, 2, 16),
	    new Item("dmg_cap", 50, 1, 1000),
	    new Item("gravity", 50, 1, 10000),
	    new Item("wind", 0, -10000, 10000),
	    new Item("power_c", 40, 20, 100),
	    new Item("map_margin", 4, 2, 128),
	    new Item("tank_distance", 10, 1, 128),
	    new Item("bot_nr", 0, 0, 8)
	);

	DebugStream debug;

	public Config(DebugStream debug) {
		this.debug = debug;
	}

	private Optional<Item> find(String name) {
		return items.stream().filter(i -> i.name.equals(name)).findAny();
	}

	synchronized public int get(String name) {
		/* TODO Do something about the search. Use a hash table? */
		Optional<Item> found = find(name);

		if (found.isPresent())
			return found.get().getValue();
		else
			return 0; /* If nothing found. Not the best way to show it */
	}

	synchronized public Item getItem(String name) {
		/* TODO Do something about the search. Use a hash table? */
		Optional<Item> found = find(name);

		if (found.isPresent())
			return found.get();
		else
			return null; /* If nothing found. Not the best way to show it */
	}

	synchronized public void set(String name, int value) {
		/* find a config item whose name matches this name
	     * and place the value there */
		Optional<Item> found = find(name);

		if (found.isPresent())
			found.get().setValue(value);
		else
			/* No such name found. Doing nothing about it for now. */
			debug.print(DebugLevel.Err,
					"config.set", "No option found in config: " + name);
	}

	synchronized public List<Item> getItems() {
		return items;
	}

	public void update(ConfigUpdate u) {
		set(u.getOption().getName(),
			u.getOption().getValue());
	}

	public void update(SetConfigCommand cmd) {
		set(cmd.getOption().getName(),
			cmd.getOption().getValue());
	}

	public FloatPair getInitialV(Shot shot) {
		int powerC = get("power_c");
	    double angleRad = degToRad(shot.getAngle());

	    return new FloatPair(
	        shot.getPower() * Math.cos(angleRad) / powerC,
	        -shot.getPower() * Math.sin(angleRad) / powerC);
	}

	public FloatPair getAcceleration() {
		return new FloatPair(
	        (double)get("wind") / 1000,
	        (double)get("gravity") / 1000);
	}

	private double degToRad(short deg) {
		switch (deg)
	    {
	        case 0:
	            return 0;
	        case 90:
	            return Math.PI/2;
	        case 180:
	            return Math.PI;
	        default:
	        	// TODO Use Math.toRadians() instead?
	            return deg * Math.PI / 180;
	    }
	}

	public class Item {
		final String name;
		int value;
		final int min;
		final int max;

		public Item(String name, int value, int min, int max) {
			this.name = name;
			this.value = value;
			this.min = min;
			this.max = max;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public int getMin() {
			return min;
		}

		public int getMax() {
			return max;
		}

		public ConfigUpdate toUpdate() {
			return new ConfigUpdate(
					new NetConfigOption(name, value));
		}
	}
}
