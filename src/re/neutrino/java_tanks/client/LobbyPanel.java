package re.neutrino.java_tanks.client;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JPanel;
import javax.swing.BorderFactory;

public class LobbyPanel extends JPanel implements ActionListener, ItemListener {

	public LobbyPanel() {
		GridLayout layout = new GridLayout(1,1);
		layout.setVgap(20);
		setLayout(layout);
		
		setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
	}
	
	@Override
	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
