package re.neutrino.java_tanks.client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

import javax.swing.JApplet;

import re.neutrino.java_tanks.debug.DebugLevel;
import re.neutrino.java_tanks.types.GameMap;
import re.neutrino.java_tanks.types.Player;

public class GameApplet extends JApplet implements MouseListener, KeyListener {
	final static Color bg = Color.white;
	final static Color fg = Color.black;
	final static int mul_h = 10;
	final static int mul_v = 20;
	final static int off_v = 14*mul_v;
	final static int brush_width = 20;
	Color grey = Color.lightGray;
	Color idle_tank_colour = Color.BLUE;
	Color active_tank_colour = Color.CYAN;
	Color local_tank_colour = Color.ORANGE;

	public GameApplet() {
		setBackground(bg);
		setForeground(fg);

		addMouseListener(this);
		addKeyListener(this);
		setFocusable(true);
	}

	private void draw_map(Graphics2D g, GameMap m) {
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
	}

	private void draw_tanks(Graphics2D g, ArrayList<Player> pl) {
		int width = 5*mul_h;
		int height = mul_v;
		for (Player i:pl) {
			if (Game.players.loc_player == i) {
				g.setPaint(local_tank_colour);
			} else {
				switch (i.getState()) {
					case Waiting:
					case Dead:
						g.setPaint(idle_tank_colour);
						break;
					case Active:
						g.setPaint(active_tank_colour);
						break;
					default:
						Main.debug.print(DebugLevel.Warn, "Unknown state", i.getState());
				}
			}
			int x = i.getPos().getX()*mul_h-width/2;
			int y = i.getPos().getY()*mul_v-off_v;
			g.fillRoundRect(x, y, width, height, width/2, height);
			g.setStroke(new BasicStroke(mul_v/2));
			g.drawLine(x, y-height/2, x+width/2, y+height/2);
			g.setPaint(fg);
			g.drawString(i.getNickname(), x, y-height);
		}
	}

	public void paint(Graphics gg) {
		Graphics2D g = (Graphics2D) gg;
		GameMap m = Game.map;
		ArrayList<Player> pl = Game.players.getL();
		draw_map(g, m);
		draw_tanks(g, pl);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int px = Game.players.loc_player.getPos().getX()*mul_h;
		int py = Game.players.loc_player.getPos().getY()*mul_v-off_v;
		int dx = px-e.getX();
		int dy = py-e.getY();
		Integer angl = 90 + (int) Math.toDegrees(Math.atan2(dx, dy));
		if (angl > 180) angl = 180;
		else if (angl < 0) angl = 0;
		((GamePanel) Main.GUIframe.Game).s.angle = angl;
		((GamePanel) Main.GUIframe.Game).angle_l.setText("Angle: " + ((GamePanel) Main.GUIframe.Game).s.angle);
		e.consume();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		Integer angl = ((GamePanel) Main.GUIframe.Game).s.angle;
		Integer pwr = ((GamePanel) Main.GUIframe.Game).s.power;
		switch (arg0.getKeyCode()) {
			case KeyEvent.VK_RIGHT:
				if (angl < 180) {
					angl++;
					((GamePanel) Main.GUIframe.Game).angle_l.setText("Angle: " + angl);
					((GamePanel) Main.GUIframe.Game).s.angle = angl;
				}
				break;
			case KeyEvent.VK_LEFT:
				if (angl > 0) {
					angl--;
					((GamePanel) Main.GUIframe.Game).angle_l.setText("Angle: " + angl);
					((GamePanel) Main.GUIframe.Game).s.angle = angl;
				}
				break;
			case KeyEvent.VK_UP:
				if (pwr < 100) {
					pwr++;
					((GamePanel) Main.GUIframe.Game).power_l.setText("Power: " + pwr);
					((GamePanel) Main.GUIframe.Game).s.power = pwr;
				}
				break;
			case KeyEvent.VK_DOWN:
				if (pwr > 0) {
					pwr--;
					((GamePanel) Main.GUIframe.Game).power_l.setText("Power: " + pwr);
					((GamePanel) Main.GUIframe.Game).s.power = pwr;
				}
				break;
		}
		arg0.consume();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}
}
