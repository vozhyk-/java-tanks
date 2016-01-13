package re.neutrino.java_tanks.client;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.GroupLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener, ItemListener {
	final static int map_x = 127*GameApplet.mul_h;
	final static int map_y = 32*GameApplet.mul_v;
	JButton shoot_b = new JButton("shoot");
	JButton inc_power_b = new JButton("^");
	JButton dec_power_b = new JButton("v");
	JButton inc_ang_b = new JButton("^");
	JButton dec_ang_b = new JButton("v");
	JApplet game_a;
	Timer timer;
	JLabel timer_l;
	time t = new time();
	JButton[] buttons = {shoot_b, inc_power_b, dec_power_b, inc_ang_b, dec_ang_b};
	JLabel power_l = new JLabel();
	JLabel angle_l = new JLabel();
	shot s = new shot();

	class shot {
		Integer angle = 90;
		Integer power = 50;
	}

	class time {
		Integer seconds = 0;
		Integer minutes = 0;

		void update() {
			if (seconds >= 60) {
				minutes++;
				seconds-=60;
			}
			if (seconds < 10) {
				timer_l.setText(minutes + ":0" + seconds);
			} else {
				timer_l.setText(minutes + ":" + seconds);
			}
		}
	}

	void set_shoot_buttons(boolean b) {
		for (JButton i : buttons) {
			i.setEnabled(b);
		}
	}

	GamePanel() {
		setDoubleBuffered(true);

		timer = new Timer(1000, this);
		timer_l = new JLabel("0:00");

		power_l.setText("Power: " + s.power);
		angle_l.setText("Angle: " + s.angle);

		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		set_shoot_buttons(false);
		for (JButton i : buttons) {
			i.addActionListener(this);
		}

		game_a = new GameApplet();
		game_a.setPreferredSize(new Dimension(map_x, map_y));

		layout.setHorizontalGroup(
				layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(game_a)
						.addGroup(layout.createSequentialGroup()
								.addComponent(timer_l)
								.addGroup(layout.createParallelGroup()
									.addComponent(inc_power_b)
									.addComponent(power_l)
									.addComponent(dec_power_b))
								.addGroup(layout.createParallelGroup()
									.addComponent(inc_ang_b)
									.addComponent(angle_l)
									.addComponent(dec_ang_b))
								.addComponent(shoot_b)))
					);
		layout.setVerticalGroup(
					layout.createSequentialGroup()
					.addComponent(game_a)
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
							.addComponent(timer_l)
							.addGroup(layout.createSequentialGroup()
								.addComponent(inc_power_b)
								.addComponent(power_l)
								.addComponent(dec_power_b))
							.addGroup(layout.createSequentialGroup()
								.addComponent(inc_ang_b)
								.addComponent(angle_l)
								.addComponent(dec_ang_b))
							.addComponent(shoot_b))
					);
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (timer == e.getSource()) {
			t.seconds++;
			t.update();
		} else if (shoot_b == e.getSource()){
			Main.con.send_shot(s.power, s.angle);
			set_shoot_buttons(false);
		} else {
			if (inc_power_b == e.getSource() && s.power < 100) {
				s.power++;
			} else if (dec_power_b == e.getSource() && s.power > 0) {
				s.power--;
			} else if (inc_ang_b == e.getSource() && s.angle < 180) {
				s.angle++;
			} else if (dec_ang_b == e.getSource() && s.angle > 0) {
				s.angle--;
			}
			power_l.setText("Power: " + s.power);
			angle_l.setText("Angle: " + s.angle);
			game_a.repaint();
		}
	}

}
