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
	String[] conf_map_types = {"flat", "hill", "valley"};
	JComboBox<String> conf_map_cb;
	JLabel conf_size_la;
	JTextField conf_size_tf;
	JButton conf_size_b;

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

		conf_map_cb = new JComboBox<String>(conf_map_types);
		conf_map_cb.setSelectedIndex(Game.conf.get("map_type"));
		conf_map_cb.addActionListener(this);

		conf_size_la = new JLabel("Map size: ");

		conf_size_tf = new JTextField(((Integer) Game.conf.get("map_width")).toString());
		conf_size_tf.addActionListener(this);
		conf_size_tf.setHorizontalAlignment(JTextField.CENTER);
		conf_size_tf.setPreferredSize(new Dimension(40,20));

		conf_size_b = new JButton("set");
		conf_size_b.setEnabled(true);
		conf_size_b.addActionListener(this);

		layout.setHorizontalGroup(
			layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
					.addComponent(p_list)
					.addGroup(layout.createSequentialGroup()
							.addComponent(conf_map_la)
							.addComponent(conf_map_cb))
					.addGroup(layout.createSequentialGroup()
							.addComponent(conf_size_la)
							.addComponent(conf_size_tf)
							.addComponent(conf_size_b))
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
					.addComponent(conf_size_la)
					.addComponent(conf_size_tf)
					.addComponent(conf_size_b))
			.addGroup(layout.createParallelGroup()
					.addComponent(return_b)
					.addComponent(ready_b))
				);
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub

	}

	void setSize() {
		Integer val = null;
		try {
			val = new Integer(conf_size_tf.getText());
		} catch (NumberFormatException nfe) {
			Main.debug.print(DebugLevel.Debug, "wrong_input", conf_size_tf.getText());
			Main.GUIframe.conf_dialog("Input must be a number");
			return;
		}
		Config.Item it = Game.conf.getItem("map_width");
		if (it == null) {
			Main.debug.print(DebugLevel.Warn, "Item not found");
			return;
		}
		if (val.intValue() < it.getMax() && val.intValue() > it.getMin())
			Main.con.send_setConf(new NetConfigOption("map_width", val));
		else
			Main.GUIframe.conf_dialog("Input out of bounds (" + it.getMin() + ", " + it.getMax() + ")");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (ready_b == e.getSource()) {
			Main.con.send_ready();
			ready_b.setEnabled(false);
		} else if (return_b == e.getSource()) {
			Main.con.close();
			Game.players.clear_l();
			Main.GUIframe.changePane(Main.GUIframe.mainMenu);
			((MainMenuPanel) Main.GUIframe.mainMenu).tryConnect();
		} else if (conf_map_cb == e.getSource()) {
			Main.con.send_setConf(new NetConfigOption("map_type", conf_map_cb.getSelectedIndex()));
		} else if (conf_size_b == e.getSource() || conf_size_tf == e.getSource()) {
			setSize();
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
