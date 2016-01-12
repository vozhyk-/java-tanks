package re.neutrino.java_tanks.server;

import re.neutrino.java_tanks.Config;
import re.neutrino.java_tanks.debug.*;
import re.neutrino.java_tanks.types.*;
import re.neutrino.java_tanks.types.basic.Mutable;
import re.neutrino.java_tanks.types.updates.MapUpdate;

public class ServerGameMap extends GameMap {
	final short collisionXPrecision = 16;

	private Game game;
	private Config config;
	private DebugStream debug;

	public ServerGameMap(Info info, Game game, Config config, DebugStream debug) {
		super(info);
		this.game = game;
		this.config = config;
		this.debug = debug;
	}

	public void change(short x, short new_height) {
		set(x, new_height);
		game.allAddUpdate(new MapUpdate(x, new_height));
	}

	public Impact shotWithoutDamage(Player player, Shot shot) {
		Impact impact = getImpact(player, shot);
		double impactT = impact.getTime();
		MapPosition impactPos = impact.getPos();
	    debug.print(DebugLevel.Debug, "shot", "impact t: " + impactT);

	    MapPosition dPos =
	        Shot.getShotPos(
	        		player.getPos().toFloatPair(),
	        		config.getInitialV(shot),
	        		config.getAcceleration(),
	        		impactT).round();
	    debug.print(DebugLevel.Debug, "shot", "pos @ impact t: " + dPos);
	    debug.print(DebugLevel.Debug, "shot", "impact pos: " + impactPos);
	    if (isInside(impactPos))
	        debug.print(DebugLevel.Debug,
	        		"shot: map y @ impact pos", get(impactPos.getX()));
	    else
	    	debug.print(DebugLevel.Debug,
	    			"shot", "Impact position outside map");

	    return impact;
	}

	public void shotUpdateMap(MapPosition impactPos) {
		int radius = config.get("dmg_radius");
	    debug.print(DebugLevel.Debug, "damage radius", radius);

	    if (!isInside(impactPos))
	        return;

	    FloatPair origPos = impactPos.toFloatPair();
	    double leftX = origPos.x - radius;
	    double rightX = origPos.x + radius;
	    //debug.print(DebugLevel.Debug, "left x", left_x);
	    //debug.print(DebugLevel.Debug, "right x", right_x);

	    FloatPair pos = new FloatPair(leftX, origPos.y);
		for (; pos.x <= rightX; pos.x += 1) {
			// debug.print(DebugLevel.Debug, "update map: current x", pos.x);
			MapPosition mapPos = pos.round();

			if (isInside(mapPos))
				updateMapAt(pos, mapPos, origPos, radius);
		}
	}

	/* helper for shotUpdateMap() */
	private void updateMapAt(
			FloatPair pos,
			MapPosition mapPos,
			FloatPair origPos,
			int radius)
	{
		short changeAmount = 1;/* this could scale with radius */
	    //debug.print(DebugLevel.Debug, "update_map_at: change amount", changeAmount);
		short x = mapPos.getX();

	    change(x, (short) (get(x) + changeAmount));
	    if (radius >= 3 && isInside(mapPos))
	    {
	        change((short) (x-1), (short) (get(x-1) + changeAmount));
	        change((short) (x+1), (short) (get(x+1) + changeAmount));
	        makeSmooth(x+1, true);
	        makeSmooth(x-1, false);
	    }
	    else
	    {
	        makeSmooth(x, true);
	        makeSmooth(x, false);
	    }
	}

	void makeSmooth(int x, boolean direction)
	{
	    while (x < info.getLength() && x > 0
	           && (get(x)-get(direction ? x+1 : x-1)) >= 3) {
	        change((short) (direction ? x+1 : x-1), (short) (get(x)-2));
	        if (direction)
	        	x++;
        	else
        		x--;
	    }
	}

	Impact getImpact(Player player, Shot shot) {
		FloatPair initV = config.getInitialV(shot);
		FloatPair acc = config.getAcceleration();
		short initDirection = (short) (Math.abs(initV.x) / initV.x);

		FloatPair initPos = player.getPos().toFloatPair();
		Mutable<Double> xStep =
				new Mutable<>((double)initDirection / collisionXPrecision);

		Mutable<Boolean> oneSideClear = new Mutable<>(false);
		double curDeltaX = 0;
		double curT = 0;

		debug.print(DebugLevel.Debug, "shot", "initial pos: " + initPos);
		debug.print(DebugLevel.Debug, "shot", "initial v: " + initV);
		debug.print(DebugLevel.Debug, "shot", "wind: " + acc.x);

		while (true) { /* exit with return */
			debug.print(DebugLevel.Debug, "current deltaX", curDeltaX);
			double tStep = getTStep(curDeltaX, curT,
					xStep, oneSideClear,
					initV, acc);

			if (tStep == 0 && xStep.get() == 0) {
					debug.print(DebugLevel.Warn,
							"shot",
							"both xStep and tStep = 0. "
							+ "Something is horribly wrong. "
							+ "Landing the shell onto the shooter.");
					return new Impact(player.getPos(), curT);
			}

			if (tStep != 0) {
				curT += tStep;
				FloatPair fPos = Shot.getShotPos(initPos, initV, acc, curT);
				debug.print(DebugLevel.Debug, "current pos", fPos);
				MapPosition mapPos = fPos.round();
				int mapY;

				/* The bullet might return from the edge of the map,
				 * so stop only when it falls to the bottom */
				if (!isInside(mapPos))
					mapY = info.getHeight();
				else
					mapY = get(mapPos.getX());

				if (mapPos.getY() >= mapY)
					return new Impact(mapPos, curT);
			}
			curDeltaX += xStep.get();
		}
	}

	/* helper for getImpact() */
	double getTStep(double prevDeltaX, double prevT,
	                  Mutable<Double> xStep, Mutable<Boolean> oneSideClear,
	                  FloatPair initV, FloatPair acc)
	{
	    double c1, c2;
	    double tStep1, tStep2;

		if (acc.x != 0) { // wind present
			double D = Math.sqrt(initV.x*initV.x
	                        + 2*acc.x*(prevDeltaX + xStep.get()));
			if (!((Double) D).isNaN()) {
				c1 = -initV.x - acc.x*prevT;
	            c2 = acc.x;

	            tStep1 = (c1 - D) / c2;
	            tStep2 = (c1 + D) / c2;

	            debug.print(DebugLevel.Debug, "t step (1)", tStep1);
				debug.print(DebugLevel.Debug, "t step (2)", tStep2);
			} else {
				/* Need to turn around to the opposite direction */
	            xStep.set(-xStep.get());
	            return getTStep(prevDeltaX, prevT,
	                              xStep, oneSideClear,
	                              initV, acc);
	        }
		} else {
	        c1 = prevDeltaX + xStep.get() - initV.x*prevT;
	        c2 = initV.x;

	        tStep1 = tStep2 = c1 / c2;

	        debug.print(DebugLevel.Debug, "t step", tStep1);
	    }
	    /* tStep1 <= tStep2 */

		if (tStep1 >= 0) {
			return tStep1;
		} else if (tStep2 >= 0) {
			return tStep2;
		} else {
			/* No valid tStep found, turn back */
			if (!oneSideClear.get()) {
				oneSideClear.set(true);
				xStep.set(-xStep.get());
			} else {
				debug.print(DebugLevel.Err, "wtf", "Haven't found a valid tStep!");
			}
			// Arbitrary value, as we need to return something
			return 1;
	    }
	}
}
