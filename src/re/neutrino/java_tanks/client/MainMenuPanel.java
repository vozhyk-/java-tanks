package re.neutrino.java_tanks.client;

import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainMenuPanel extends JPanel implements ActionListener, ItemListener {
	JTextField ip_entry;
	JTextField name_entry;
	JButton new_game;
	JButton random_game;
	
	public MainMenuPanel() {
		GridLayout layout = new GridLayout(4,1);
		layout.setVgap(20);
		setLayout(layout);

		ip_entry = new JTextField("127.0.0.1");
		ip_entry.addActionListener(this);
		ip_entry.setHorizontalAlignment(JTextField.CENTER);
		add(ip_entry);

		name_entry = new JTextField(System.getProperty("user.name"));
		name_entry.addActionListener(this);
		name_entry.setHorizontalAlignment(JTextField.CENTER);
		add(name_entry);

		new_game = new JButton("New game");
		new_game.setEnabled(false);
		new_game.addActionListener(this);
		add(new_game);

		random_game = new JButton("Random game");
		random_game.setEnabled(false);
		random_game.addActionListener(this);
		add(random_game);

        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));        
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
			new_game.setEnabled(true);
		}
	}

}
