package re.neutrino.java_tanks.client;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JPanel;
import javax.swing.JTextField;

import re.neutrino.java_tanks.Config;
import re.neutrino.java_tanks.debug.DebugLevel;
import re.neutrino.java_tanks.types.GameMap;
import re.neutrino.java_tanks.types.NetConfigOption;
import re.neutrino.java_tanks.types.Player;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;

public class LobbyPanel extends JPanel implements ActionListener, ItemListener {
	JButton return_b;
	JButton ready_b;
	DefaultListModel<String> p_listModel = new DefaultListModel<String>();
	JList<String> p_list = new JList<String>(p_listModel);
	JLabel conf_map_la;
	JComboBox<GameMap.Info.Type> conf_map_cb;
	setting map_size = new setting("Map size: ", "map_width");
	setting bot_nr = new setting("Number of bots: ", "bot_nr");

	class setting {
		JLabel conf_la;
		JTextField conf_tf;
		JButton conf_b;

		setting(String label_name, String conf_name) {
			conf_la = new JLabel(label_name);

			conf_tf = new JTextField(((Integer) Game.conf.get(conf_name)).toString());
			conf_tf.setHorizontalAlignment(JTextField.CENTER);
			conf_tf.setPreferredSize(new Dimension(40,20));

			conf_b = new JButton("set");
			conf_b.setEnabled(true);
		}
	}

	public LobbyPanel() {
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		return_b = new JButton("return");
		return_b.setEnabled(true);
		return_b.addActionListener(this);

		ready_b = new JButton("ready");
		ready_b.setEnabled(true);
		ready_b.addActionListener(this);

		conf_map_la = new JLabel("Map type:");

		conf_map_cb = new JComboBox<GameMap.Info.Type>(GameMap.Info.Type.values());
		conf_map_cb.setSelectedIndex(Game.conf.get("map_type"));
		conf_map_cb.addActionListener(this);

		map_size.conf_tf.addActionListener(this);
		map_size.conf_b.addActionListener(this);

		bot_nr.conf_tf.addActionListener(this);
		bot_nr.conf_b.addActionListener(this);

		layout.setHorizontalGroup(
			layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
					.addComponent(p_list)
					.addGroup(layout.createSequentialGroup()
							.addComponent(conf_map_la)
							.addComponent(conf_map_cb))
					.addGroup(layout.createSequentialGroup()
							.addComponent(map_size.conf_la)
							.addComponent(map_size.conf_tf)
							.addComponent(map_size.conf_b))
					.addGroup(layout.createSequentialGroup()
							.addComponent(bot_nr.conf_la)
							.addComponent(bot_nr.conf_tf)
							.addComponent(bot_nr.conf_b))
					.addGroup(layout.createSequentialGroup()
							.addComponent(return_b)
							.addComponent(ready_b)))
				);
		layout.setVerticalGroup(
			layout.createSequentialGroup()
			.addComponent(p_list)
			.addGroup(layout.createParallelGroup()
					.addComponent(conf_map_la)
					.addComponent(conf_map_cb))
			.addGroup(layout.createParallelGroup()
					.addComponent(map_size.conf_la)
					.addComponent(map_size.conf_tf)
					.addComponent(map_size.conf_b))
			.addGroup(layout.createParallelGroup()
					.addComponent(bot_nr.conf_la)
					.addComponent(bot_nr.conf_tf)
					.addComponent(bot_nr.conf_b))
			.addGroup(layout.createParallelGroup()
					.addComponent(return_b)
					.addComponent(ready_b))
				);
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void enableReady(boolean state) {
		ready_b.setEnabled(state);
	}

	void setConf(String conf, JTextField tf) {
		Integer val = null;
		try {
			val = new Integer(tf.getText());
		} catch (NumberFormatException nfe) {
			Main.debug.print(DebugLevel.Debug, "wrong_input", tf.getText());
			Main.GUIframe.conf_dialog("Input must be a number");
			return;
		}
		Config.Item it = Game.conf.getItem(conf);
		if (it == null) {
			Main.debug.print(DebugLevel.Warn, "Item not found", conf);
			return;
		}
		if (val.intValue() <= it.getMax() && val.intValue() >= it.getMin())
			Main.con.send_setConf(new NetConfigOption(conf, val));
		else
			Main.GUIframe.conf_dialog("Input out of bounds (" + it.getMin() + ", " + it.getMax() + ")");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (ready_b == e.getSource()) {
			Main.con.send_ready();
			enableReady(false);
		} else if (return_b == e.getSource()) {
			Main.con.close();
			Game.players.clear_l();
			Main.GUIframe.changePane(Main.GUIframe.mainMenu);
			((MainMenuPanel) Main.GUIframe.mainMenu).tryConnect();
		} else if (conf_map_cb == e.getSource()) {
			Main.con.send_setConf(new NetConfigOption("map_type", conf_map_cb.getSelectedIndex()));
		} else if (map_size.conf_b == e.getSource() || map_size.conf_tf == e.getSource()) {
			setConf("map_width", map_size.conf_tf);
		} else if (bot_nr.conf_b == e.getSource() || bot_nr.conf_tf == e.getSource()) {
			setConf("bot_nr", bot_nr.conf_tf);
		}
	}

	public void update_player_list() {
		p_listModel.removeAllElements();
		for (Player i:Game.players.getL()) {
			p_listModel.addElement(i.getNickname() + ", state: " + i.getState());
		}
		p_list = new JList<String>(p_listModel);
		p_list.setPreferredSize(new Dimension(100, 40*Game.players.getL().size()));
		revalidate();
		Main.GUIframe.pack();
	}
}
