package re.neutrino.java_tanks.client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import javax.swing.JApplet;

public class MapApplet extends JApplet {
	final static Color bg = Color.white;
	final static Color fg = Color.black;
	Color grey = Color.lightGray;

	public MapApplet() {
		setBackground(bg);
		setForeground(fg);
	}

	public void paint(Graphics gg) {
		Graphics2D g = (Graphics2D) gg;
		g.setPaint(grey);
		for (short i=1; i<Game.map.getInfo().getLength(); i++) {
			g.draw(new Line2D.Double((i-1)*20, Game.map.get((short) (i-1)), i*20, Game.map.get(i)));
		}
		g.setPaint(fg);
	}
}
