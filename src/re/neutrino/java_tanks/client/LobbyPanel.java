package re.neutrino.java_tanks.client;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JPanel;

import re.neutrino.java_tanks.types.Player;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JList;

public class LobbyPanel extends JPanel implements ActionListener, ItemListener {
	JButton return_b;
	JButton ready_b;
	DefaultListModel<String> p_listModel = new DefaultListModel<String>();
	JList<String> p_list = new JList<String>(p_listModel);

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

		layout.setHorizontalGroup(
				   layout.createSequentialGroup()
				   	.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
					   	.addComponent(p_list)
					   	.addGroup(layout.createSequentialGroup()
					   			.addComponent(return_b)
					   			.addComponent(ready_b)))
				   	);
		layout.setVerticalGroup(
				   layout.createSequentialGroup()
				   	.addComponent(p_list)
				   	.addGroup(layout.createParallelGroup()
				   			.addComponent(return_b)
				   			.addComponent(ready_b))
				   	);
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
			Main.con.close();
			Main.GUIframe.changePane(Main.GUIframe.MainMenu);
			((MainMenuPanel) Main.GUIframe.MainMenu).tryConnect();
		}
	}

	public void update_player_list() {
		p_listModel.removeAllElements();
		for (Player i:Game.players.getL()) {
			p_listModel.addElement(i.getNickname().trim() + ", state: " + i.getState());
		}
		p_list = new JList<String>(p_listModel);
		p_list.setPreferredSize(new Dimension(100, 40*Game.players.getL().size()));
		revalidate();
		Main.GUIframe.pack();
	}
}
