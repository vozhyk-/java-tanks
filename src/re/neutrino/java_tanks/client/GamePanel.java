package re.neutrino.java_tanks.client;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Timer;

import re.neutrino.java_tanks.types.Player;
import re.neutrino.java_tanks.types.updates.LogUpdate;

public class GamePanel extends JPanel implements ActionListener, ItemListener {
	static int map_x;
	static int map_y;
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
	chat c = new chat();
	DefaultListModel<String> s_listModel = new DefaultListModel<String>();
	JList<String> s_list = new JList<String>(s_listModel);

	class chat {
		JTextField f = new JTextField();
		JButton b = new JButton("send");
		DefaultListModel<String> lm = new DefaultListModel<String>();
		JList<String> l = new JList<String>(lm);
		JScrollPane sp = new JScrollPane(l);

		void add_msg(LogUpdate lu) {
			lm.addElement(lu.getLine());
			l.ensureIndexIsVisible(lm.getSize()-1);
		}
	}

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

	public void update_game_applet_size() {
		map_x = Game.conf.get("map_width")*GameApplet.mul_h;
		map_y = Game.conf.get("map_height")*GameApplet.mul_h;
		game_a.setPreferredSize(new Dimension(map_x, map_y));
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
		update_game_applet_size();

		c.b.addActionListener(this);
		c.f.addActionListener(this);
		c.f.setHorizontalAlignment(JTextField.CENTER);
		c.f.setPreferredSize(new Dimension(80,20));
		c.sp.setPreferredSize(new Dimension(80,40));

		layout.setHorizontalGroup(
				layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(game_a)
						.addGroup(layout.createSequentialGroup()
								.addComponent(timer_l)
								.addGroup(layout.createParallelGroup()
										.addComponent(c.sp)
										.addGroup(layout.createSequentialGroup()
												.addComponent(c.f)
												.addComponent(c.b)))
								.addGroup(layout.createParallelGroup()
									.addComponent(inc_power_b)
									.addComponent(power_l)
									.addComponent(dec_power_b))
								.addGroup(layout.createParallelGroup()
									.addComponent(inc_ang_b)
									.addComponent(angle_l)
									.addComponent(dec_ang_b))
								.addComponent(shoot_b)
								.addComponent(s_list)))
					);
		layout.setVerticalGroup(
					layout.createSequentialGroup()
					.addComponent(game_a)
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
							.addComponent(timer_l)
							.addGroup(layout.createSequentialGroup()
									.addComponent(c.sp)
									.addGroup(layout.createParallelGroup()
											.addComponent(c.f)
											.addComponent(c.b)))
							.addGroup(layout.createSequentialGroup()
								.addComponent(inc_power_b)
								.addComponent(power_l)
								.addComponent(dec_power_b))
							.addGroup(layout.createSequentialGroup()
								.addComponent(inc_ang_b)
								.addComponent(angle_l)
								.addComponent(dec_ang_b))
							.addComponent(shoot_b)
							.addComponent(s_list))
					);
	}

	void update_scoreboard() {
		s_listModel.removeAllElements();
		Game.players.getL().sort(new PlayerHitpointsComparator());
		for (Player i:Game.players.getL()) {
			s_listModel.addElement(i.getNickname() + ", hitpoints: " + i.getHitpoints());
		}
		s_list = new JList<String>(s_listModel);
		s_list.setPreferredSize(new Dimension(100, 40*Game.players.getL().size()));
		revalidate();
		Main.GUIframe.pack();
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
		} else if (c.f == e.getSource() || c.b == e.getSource()) {
			Main.con.send_chatmsg(c.f.getText());
			c.f.setText("");
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
