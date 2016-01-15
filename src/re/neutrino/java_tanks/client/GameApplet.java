package re.neutrino.java_tanks.client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

import javax.swing.JApplet;

import re.neutrino.java_tanks.debug.DebugLevel;
import re.neutrino.java_tanks.types.FloatPair;
import re.neutrino.java_tanks.types.GameMap;
import re.neutrino.java_tanks.types.Player;
import re.neutrino.java_tanks.types.Shot;
import re.neutrino.java_tanks.types.updates.ShotImpactUpdate;
import re.neutrino.java_tanks.types.updates.ShotUpdate;

public class GameApplet extends JApplet implements MouseListener, KeyListener {
	final static Color bg = Color.white;
	final static Color fg = Color.black;
	final static int mul_h = 10;
	final static int mul_v = 20;
	final static int off_v = 14*mul_v;
	final static int brush_width = 10;
	final static int width = 5*mul_h;
	final static int height = mul_v;
	final static int t_len = height;
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
		for (Player i:pl) {
			if (PlayersList.loc_player == i) {
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
			int tx = x + width/2 + (int) (t_len*Math.cos(Math.toRadians(((GamePanel) Main.GUIframe.Game).s.angle)));
			int ty = y - (int) (t_len*Math.sin(Math.toRadians(((GamePanel) Main.GUIframe.Game).s.angle)));
			g.drawLine(x + width/2, y, tx, ty);
			g.setPaint(fg);
			g.drawString(i.getNickname(), x, y-height);
		}
	}

	@Override
	public void paint(Graphics gg) {
		Graphics2D g = (Graphics2D) gg;
		GameMap m = Game.map;
		ArrayList<Player> pl = Game.players.getL();
		g.setColor(bg);
		g.fillRect(0, 0, getSize().width, getSize().height);
		draw_map(g, m);
		draw_tanks(g, pl);
	}

	@Override
	public void update(Graphics g) {
		Image image = createImage(this.getWidth(), this.getHeight());
	    Graphics graphics = image.getGraphics();
	    graphics.setColor(getBackground());
	    graphics.fillRect(0,  0,  this.getWidth(),  this.getHeight());
	    graphics.setColor(getForeground());
	    paint(graphics);
	    g.drawImage(image, 0, 0, this);
	}

	public GameApplet getOuter() {
	    return GameApplet.this;
	}

	ShotThread create_shot_thread(ShotUpdate su, ShotImpactUpdate siu) {
		Player sh = Game.players.find_p(su.getPlayerId());
		return new ShotThread(sh, su.getShot(), siu.getImpactT());
	}

	public class ShotThread implements Runnable {
		private volatile boolean running = true;
		Color shot_colour = Color.ORANGE;
		final static short fps = 20;
		final static int r = 10;
		static final double inc = (double) r/fps;
		Shot sh;
		Player sp;
		double it;
		double cur_t = 0;
		Image image = createImage(getWidth(), getHeight());
		Graphics graphics = image.getGraphics();

		ShotThread(Player p, Shot s, double t) {
			sp = p;
			sh = s;
			it = t;
			graphics.setColor(getBackground());
		    graphics.fillRect(0,  0,  getOuter().getWidth(),  getOuter().getHeight());
		    graphics.setColor(getForeground());
		    paint(graphics);
		}

		void draw_shot(Graphics2D g, FloatPair floatPair, Color col) {
			g.setPaint(col);
			double x = floatPair.getX()*mul_h;
			double y = floatPair.getY()*mul_v - off_v;
			if (y < mul_v) {
				int[] xx = {((int) x) - r, (int) (x) + r, (int) x};
				int[] yy = {mul_v, mul_v, mul_v/2};
				g.fillPolygon(xx, yy, 3);
			} else {
				g.fill(new Ellipse2D.Double(x, y, r, r));
			}
			g.setPaint(fg);
		}

		@Override
		public void run() {
			while (cur_t < it && running) {
				cur_t+=inc;
				FloatPair fp = Shot.getShotPos(sp, sh, cur_t, Game.conf);
				draw_shot((Graphics2D) graphics, fp, shot_colour);
				getGraphics().drawImage(image, 0, 0, getOuter());
				try {
					Thread.sleep(1000/fps);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					running=false;
				}
				draw_shot((Graphics2D) graphics, fp, bg);
			}
			repaint();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int px = PlayersList.loc_player.getPos().getX()*mul_h;
		int py = PlayersList.loc_player.getPos().getY()*mul_v-off_v;
		int dx = px-e.getX();
		int dy = py-e.getY();
		Integer angl = 90 + (int) Math.toDegrees(Math.atan2(dx, dy));
		if (angl > 180) angl = 180;
		else if (angl < 0) angl = 0;
		((GamePanel) Main.GUIframe.Game).s.angle = angl;
		((GamePanel) Main.GUIframe.Game).angle_l.setText("Angle: " + ((GamePanel) Main.GUIframe.Game).s.angle);
		e.consume();
		repaint();
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
		boolean a=false;
		switch (arg0.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (angl < 180) {
					angl++;
					((GamePanel) Main.GUIframe.Game).angle_l.setText("Angle: " + angl);
					((GamePanel) Main.GUIframe.Game).s.angle = angl;
				}
				a=true;
				break;
			case KeyEvent.VK_RIGHT:
				if (angl > 0) {
					angl--;
					((GamePanel) Main.GUIframe.Game).angle_l.setText("Angle: " + angl);
					((GamePanel) Main.GUIframe.Game).s.angle = angl;
				}
				a=true;
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
			case KeyEvent.VK_ENTER:
				Main.con.send_shot(((GamePanel) Main.GUIframe.Game).s.power, ((GamePanel) Main.GUIframe.Game).s.angle);
				((GamePanel) Main.GUIframe.Game).set_shoot_buttons(false);
		}
		arg0.consume();
		if (a) repaint();
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
