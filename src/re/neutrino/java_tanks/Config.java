package re.neutrino.java_tanks;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import re.neutrino.java_tanks.debug.*;
import re.neutrino.java_tanks.types.updates.ConfigUpdate;

public class Config {
	List<Item> items = Arrays.asList(
		new Item("map_width", 128, 48, 1024),
	    new Item("map_height", 64, 48, 512),
	    new Item("tank_hp", 100, 1, 1000),
	    new Item("dmg_radius", 4, 2, 16),
	    new Item("dmg_cap", 50, 1, 1000),
	    new Item("gravity", 50, 1, 10000),
	    new Item("wind", 0, -10000, 10000),
	    new Item("power_c", 40, 20, 100),
	    new Item("map_margin", 4, 2, 128),
	    new Item("tank_distance", 10, 1, 128)
	);

	DebugStream debug;

	public Config(DebugStream debug) {
		this.debug = debug;
	}

	private Optional<Item> find(String name) {
		return items.stream().filter(i -> i.name == name).findAny();
	}

	public int get(String name) {
		/* TODO Do something about the search. Use a hash table? */
		Optional<Item> found = find(name);

		if (found.isPresent())
			return found.get().getValue();
		else
			return 0; /* If nothing found. Not the best way to show it */
	}

	public void set(String name, int value) {
		/* find a config item whose name matches this name
	     * and place the value there */
		Optional<Item> found = find(name);

		if (found.isPresent())
			found.get().setValue(value);
		else
			/* No such name found. Doing nothing about it for now. */
			debug.print(DebugLevel.Err,
					"config_set", "No option found in config: " + name);
	}

	public List<Item> getItems() {
		return items;
	}

	public void update(ConfigUpdate u) {
		set(u.getOptionName(), u.getOptionValue());
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
			return new ConfigUpdate(name, value);
		}
	}
}
