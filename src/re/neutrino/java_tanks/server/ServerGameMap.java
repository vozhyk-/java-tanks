package re.neutrino.java_tanks.server;

import re.neutrino.java_tanks.Config;
import re.neutrino.java_tanks.debug.*;
import re.neutrino.java_tanks.types.*;
import re.neutrino.java_tanks.types.basic.Mutable;

public class ServerGameMap extends GameMap {
	final short collisionXPrecision = 16;

	private Config config;
	private DebugStream debug;

	public ServerGameMap(Info info, Config config, DebugStream debug) {
		super(info);
		this.config = config;
		this.debug = debug;
	}

	public Impact shotWithoutDamage(Player player, Shot shot) {
		Impact impact = getImpact(player, shot);
		double impactT = impact.getTime();
		MapPosition impactPos = impact.getPos();
	    debug.print(DebugLevel.Debug, "shot: impact t", impactT);

	    MapPosition d_pos =
	        Shot.getShotPos(
	        		player.getPos().toFloatPair(),
	        		config.getInitialV(shot),
	        		config.getAcceleration(),
	        		impactT).round();
	    debug.print(DebugLevel.Debug, "shot", "pos @ impact t:" + d_pos);
	    debug.print(DebugLevel.Debug, "shot", "impact pos:" + impactPos);
	    if (isInside(impactPos))
	        debug.print(DebugLevel.Debug,
	        		"shot: map y @ impact pos", get(impactPos.getX()));
	    else
	    	debug.print(DebugLevel.Debug,
	    			"shot", "Impact position outside map");

	    return impact;
	}

	Impact getImpact(Player player, Shot shot)
	{
		FloatPair init_v = config.getInitialV(shot);
		FloatPair acc = config.getAcceleration();
		short initDirection = (short) (Math.abs(init_v.x) / init_v.y);

		FloatPair init_pos = player.getPos().toFloatPair();
		Mutable<Double> x_step =
				new Mutable<>((double)initDirection / collisionXPrecision);

		Mutable<Boolean> one_side_clear = new Mutable<>(false);
		double cur_delta_x = 0;
		double cur_t = 0;

		debug.print(DebugLevel.Debug, "shot", "initial pos: " + init_pos);
		debug.print(DebugLevel.Debug, "shot", "initial v: " + init_v);
		debug.print(DebugLevel.Debug, "shot", "wind: " + acc.x);

		while (true) /* exit with return */
		{
			debug.print(DebugLevel.Debug, "current delta_x", cur_delta_x);
			double t_step = getTStep(cur_delta_x, cur_t,
					x_step, one_side_clear,
					init_v, acc);

			if (t_step != 0)
			{
				cur_t += t_step;
				FloatPair f_pos = Shot.getShotPos(init_pos, init_v, acc, cur_t);
				debug.print(DebugLevel.Debug, "current x", f_pos.x);
				debug.print(DebugLevel.Debug, "current y", f_pos.y);
				MapPosition map_pos = f_pos.round();
				int map_y;

				/* The bullet might return from the edge of the map,
				 * so stop only when it falls to the bottom */
				if (!isInside(map_pos))
					map_y = info.getHeight();
				else
					map_y = get(map_pos.getX());

				if (map_pos.getY() >= map_y)
				{
					return new Impact(map_pos, cur_t);
				}
			}
			cur_delta_x += x_step.get();
		}
	}

	/* helper for getImpact() */
	double getTStep(double prevDeltaX, double prevT,
	                  Mutable<Double> xStep, Mutable<Boolean> oneSideClear,
	                  FloatPair initV, FloatPair acc)
	{
	    double c1, c2;
	    double tStep1, tStep2;

	    if (acc.x != 0)
	    {
	        double D = Math.sqrt(initV.x*initV.x
	                        + 2*acc.x*(prevDeltaX + xStep.get()));
	        if (!((Double)D).isNaN())
	        {
	            c1 = -initV.x - acc.x*prevT;
	            c2 = acc.x;

	            tStep1 = (c1 - D) / c2;
	            tStep2 = (c1 + D) / c2;

	            debug.print(DebugLevel.Debug, "t step (1)", tStep1);
	            debug.print(DebugLevel.Debug, "t step (2)", tStep2);
	        }
	        else
	        {
	            /* Need to turn around to the opposite direction */
	            xStep.set(-xStep.get());
	            return getTStep(prevDeltaX, prevT,
	                              xStep, oneSideClear,
	                              initV, acc);
	        }
	    }
	    else
	    {
	        c1 = prevDeltaX + xStep.get() - initV.x*prevT;
	        c2 = initV.x;

	        tStep1 = tStep2 = c1 / c2;

	        debug.print(DebugLevel.Debug, "t step", tStep1);
	    }
	    /* t_step1 <= t_step2 */

	    if (tStep1 >= 0)
	    {
	        return tStep1;
	    }
	    else if (tStep2 >= 0)
	    {
	        return tStep2;
	    }
	    else
	    {
	        /* No valid t_step found, turn back */
	        if (!oneSideClear.get())
	        {
	            oneSideClear.set(true);
	            xStep.set(-xStep.get());
	        }
	        else
	        {
	            debug.print(DebugLevel.Err,
	            		"wtf", "Haven't found a valid t_step!");
	        }
            // Arbitrary value, as we need to return something
            return 1;
	    }
	}

	class Impact {
		final MapPosition pos;
		final double time;

		public Impact(MapPosition pos, double time) {
			this.pos = pos;
			this.time = time;
		}

		public MapPosition getPos() {
			return pos;
		}

		public double getTime() {
			return time;
		}
	}
}
