package re.neutrino.java_tanks.types;

import java.io.IOException;

import re.neutrino.java_tanks.Config;
import re.neutrino.java_tanks.types.basic.*;

public class Shot implements Communicable {
	final Int16 angle; /* in degrees, can be 0..359 */
	final Int16 power;

	public Shot(Int16 angle, Int16 power) {
		this.angle = angle;
		this.power = power;
	}

	public Shot(short angle, short power) {
		this(new Int16(angle), new Int16(power));
	}

	public short getAngle() {
		return angle.getSimpleValue();
	}

	public short getPower() {
		return power.getSimpleValue();
	}

	@Override
	public void send(CommunicationStream comm) throws IOException {
		angle.send(comm);
		power.send(comm);
	}

	public static Shot recv(CommunicationStream comm) throws IOException {
		return new Shot(Int16.recv(comm), Int16.recv(comm));
	}

	@Override
	public String toString() {
		return "Shot(angle = " + angle + ", power = " + power + ")";
	}

	public static FloatPair getShotPos(
			FloatPair initPos,
			FloatPair initV, FloatPair acc, double t) {
		return new FloatPair(
	         initPos.x + (acc.x != 0 ? initV.x*t + t*t*acc.x/2 : initV.x*t),
	         initPos.y + initV.y*t + t*t*acc.y/2);
	}

	public static FloatPair getShotPos(
			Player player, Shot shot, double time, Config config) {
		return getShotPos(
				player.getPos().toFloatPair(),
				config.getInitialV(shot),
				config.getAcceleration(),
				time);
	}
}
