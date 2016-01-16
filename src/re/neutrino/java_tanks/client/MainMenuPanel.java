package re.neutrino.java_tanks.client;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import re.neutrino.java_tanks.Config;
import re.neutrino.java_tanks.debug.DebugLevel;
import re.neutrino.java_tanks.types.NetConfigOption;

public class MainMenuPanel extends JPanel implements ActionListener, ItemListener {
	JTextField ip_entry;
	JTextField name_entry;
	JButton new_game;
	JButton random_game;
	JLabel conf_map_la;
	String[] conf_map_types = {"flat", "hill", "valley"};
	JComboBox<String> conf_map_cb;
	JLabel conf_size_la;
	JTextField conf_size_tf;
	JButton conf_size_b;

	public MainMenuPanel() {
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		ip_entry = new JTextField("127.0.0.1");
		ip_entry.addActionListener(this);
		ip_entry.setHorizontalAlignment(JTextField.CENTER);
		ip_entry.setPreferredSize(new Dimension(80,40));

		name_entry = new JTextField(System.getProperty("user.name"));
		name_entry.addActionListener(this);
		name_entry.setHorizontalAlignment(JTextField.CENTER);
		name_entry.setPreferredSize(new Dimension(80,40));

		new_game = new JButton("New game");
		new_game.setEnabled(false);
		new_game.addActionListener(this);

		random_game = new JButton("Random game");
		random_game.setEnabled(false);
		random_game.addActionListener(this);

		conf_map_la = new JLabel("Map type:");

		conf_map_cb = new JComboBox<String>(conf_map_types);
		conf_map_cb.addActionListener(this);
		conf_map_cb.setSelectedIndex(Game.conf.get("map_type"));

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
						.addComponent(ip_entry)
						.addComponent(name_entry)
						.addGroup(layout.createSequentialGroup()
								.addComponent(conf_map_la)
								.addComponent(conf_map_cb))
						.addGroup(layout.createSequentialGroup()
								.addComponent(conf_size_la)
								.addComponent(conf_size_tf)
								.addComponent(conf_size_b))
						.addGroup(layout.createSequentialGroup()
								.addComponent(new_game)
								.addComponent(random_game)))
				);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(ip_entry)
				.addComponent(name_entry)
				.addGroup(layout.createParallelGroup()
						.addComponent(conf_map_la)
						.addComponent(conf_map_cb))
				.addGroup(layout.createParallelGroup()
						.addComponent(conf_size_la)
						.addComponent(conf_size_tf)
						.addComponent(conf_size_b))
				.addGroup(layout.createParallelGroup()
						.addComponent(new_game)
						.addComponent(random_game))
				);

        tryConnect();
	}

	public MainMenuPanel(LayoutManager arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public MainMenuPanel(boolean arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public MainMenuPanel(LayoutManager arg0, boolean arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (ip_entry == arg0.getSource()) {
			tryConnect();
		} else if (new_game == arg0.getSource()) {
			Main.con.send_newGame();
			Main.game = new Game(name_entry.getText());
		} else if (random_game == arg0.getSource()) {
			Main.game = new Game(name_entry.getText());
		} else if (conf_map_cb == arg0.getSource()) {
			if (Main.con != null && !Main.con.socket.isClosed())
				Main.con.send_setConf(new NetConfigOption("map_type", conf_map_cb.getSelectedIndex()));
		} else if (conf_size_b == arg0.getSource() || conf_size_tf == arg0.getSource()) {
			setSize();
		}
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

	public void tryConnect() {
		try {
			if (Main.con != null && !Main.con.socket.isClosed()) {
				Main.con.close();
			}
			Main.con = new Net(ip_entry.getText());
			new_game.setEnabled(true);
			random_game.setEnabled(true);
		} catch (Exception e) {
			new_game.setEnabled(false);
			random_game.setEnabled(false);
		}
	}
}
