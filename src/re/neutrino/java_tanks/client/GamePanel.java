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
	JButton inc_power_b = new JButton(">");
	JButton dec_power_b = new JButton("<");
	JButton inc_ang_b = new JButton(">");
	JButton dec_ang_b = new JButton("<");
	JApplet map;
	Timer timer;
	JLabel timer_l;
	time t = new time();
	JButton[] buttons = {shoot_b, inc_power_b, dec_power_b, inc_ang_b, dec_ang_b};

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
				timer_l.setText(minutes + ":0" + seconds);
			}
		}
	}

	void deactivate_shoot_buttons(JButton[] buttons) {
		for (JButton i : buttons) {
			i.setEnabled(false);
		}
	}

	void activate_shoot_buttons(JButton[] buttons) {
		for (JButton i : buttons) {
			i.setEnabled(true);
		}
	}

	GamePanel() {
		timer = new Timer(1000, this);
		timer_l = new JLabel("0:00");

		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		deactivate_shoot_buttons(buttons);
		for (JButton i : buttons) {
			i.addActionListener(this);
		}

		map = new GameApplet();
		map.setPreferredSize(new Dimension(map_x, map_y));

		layout.setHorizontalGroup(
				layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(map)
						.addGroup(layout.createSequentialGroup()
								.addComponent(timer_l)
								.addComponent(dec_power_b)
								.addComponent(inc_power_b)
								.addComponent(dec_ang_b)
								.addComponent(inc_ang_b)
								.addComponent(shoot_b)))
					);
		layout.setVerticalGroup(
					layout.createSequentialGroup()
					.addComponent(map)
					.addGroup(layout.createParallelGroup()
							.addComponent(timer_l)
							.addComponent(dec_power_b)
							.addComponent(inc_power_b)
							.addComponent(dec_ang_b)
							.addComponent(inc_ang_b)
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
		}
	}

}
