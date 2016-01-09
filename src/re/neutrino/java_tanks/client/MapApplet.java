package re.neutrino.java_tanks.client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;

import javax.swing.JApplet;

import re.neutrino.java_tanks.types.GameMap;

public class MapApplet extends JApplet {
	final static Color bg = Color.white;
	final static Color fg = Color.black;
	final static int mul_h = 10;
	final static int mul_v = 20;
	final static int off_v = 10*mul_v;
	final static int brush_width = 20;
	Color grey = Color.lightGray;

	public MapApplet() {
		setBackground(bg);
		setForeground(fg);
	}

	public void paint(Graphics gg) {
		Graphics2D g = (Graphics2D) gg;
		GameMap m = Game.map;
		double points[][] = new double[m.getInfo().getLength()][2];
		for (short i=0; i<m.getInfo().getLength(); i++) {
			points[i][0]=i*mul_h;
			points[i][1]=m.get(i)*mul_v-off_v;
		}
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

		g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
		g.setPaint(grey);
		GeneralPath terrain = new GeneralPath();
		terrain.moveTo(points[0][0], points[0][1]);
		for (int k = 2; k < points.length; k+=2)
			terrain.curveTo(points[k-2][0], points[k-2][1],
					points[k-1][0], points[k-1][1],
					points[k][0], points[k][1]);
		int k = m.getInfo().getLength()-1;
		terrain.curveTo(points[k-2][0], points[k-2][1],
				points[k-1][0], points[k-1][1],
				points[k][0], points[k][1]);
		terrain.lineTo(GamePanel.map_x, GamePanel.map_y);
		terrain.lineTo(0, GamePanel.map_y);
		terrain.closePath();
		g.fill(terrain);
		g.setPaint(fg);
	}
}
