package re.neutrino.java_tanks.client;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JButton;

public class LobbyPanel extends JPanel implements ActionListener, ItemListener {
	JButton return_b;
	JButton ready_b;

	public LobbyPanel() {
		GridLayout layout = new GridLayout(1,2);
		layout.setVgap(20);
		setLayout(layout);
		
		return_b = new JButton("return");
		return_b.setEnabled(true);
		return_b.addActionListener(this);
		add(return_b);

		ready_b = new JButton("ready");
		ready_b.setEnabled(true);
		ready_b.addActionListener(this);
		add(ready_b);

		setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
	}
	
	@Override
	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (ready_b == e.getSource()) {
			Main.con.send_ready();
			ready_b.setEnabled(false);
		} else if (return_b == e.getSource()) {
			Main.GUIframe.changePane(Main.GUIframe.MainMenu);
		}
	}

}
