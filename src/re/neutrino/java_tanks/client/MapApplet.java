package re.neutrino.java_tanks.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import javax.swing.JApplet;

import re.neutrino.java_tanks.types.GameMap;

public class MapApplet extends JApplet {
	final static Color bg = Color.white;
	final static Color fg = Color.black;
	final static int mul = 8;
	Color grey = Color.lightGray;

	public MapApplet() {
		setBackground(bg);
		setForeground(fg);
	}

	public void paint(Graphics gg) {
		Graphics2D g = (Graphics2D) gg;
		GameMap m = Game.map;
		g.setPaint(grey);
		for (short i=1; i<m.getInfo().getLength(); i++) {
			g.draw(new Line2D.Double((i-1)*mul, m.get((short) (i-1)), i*mul, m.get(i)));
		}
		g.setPaint(fg);
		setPreferredSize(new Dimension(m.getInfo().getLength()*mul, m.getInfo().getHeight()*mul));
	}
}
