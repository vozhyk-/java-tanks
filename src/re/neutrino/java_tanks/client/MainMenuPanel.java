package re.neutrino.java_tanks.client;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainMenuPanel extends JPanel implements ActionListener, ItemListener {
	JTextField ip_entry;
	JTextField name_entry;
	JButton new_game;
	JButton random_game;

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

		layout.setHorizontalGroup(
				   layout.createSequentialGroup()
				   	.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
					   	.addComponent(ip_entry)
					   	.addComponent(name_entry)
					   	.addGroup(layout.createSequentialGroup()
					   			.addComponent(new_game)
					   			.addComponent(random_game)))
				   	);
		layout.setVerticalGroup(
				   layout.createSequentialGroup()
				   	.addComponent(ip_entry)
				   	.addComponent(name_entry)
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
		}
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
