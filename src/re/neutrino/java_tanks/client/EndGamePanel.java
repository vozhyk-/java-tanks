package re.neutrino.java_tanks.client;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;

import re.neutrino.java_tanks.types.Player;

public class EndGamePanel extends JPanel implements ActionListener {
	DefaultListModel<String> p_listModel = new DefaultListModel<String>();
	JList<String> p_list = new JList<String>(p_listModel);
	JButton return_b;

	EndGamePanel() {
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		return_b = new JButton("return");
		return_b.addActionListener(this);

		layout.setHorizontalGroup(
				   layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				   .addComponent(p_list)
				   .addComponent(return_b));
		layout.setVerticalGroup(
				   layout.createSequentialGroup()
				   .addComponent(p_list)
				   .addComponent(return_b));
	}

	public void update_player_list() {
		p_listModel.removeAllElements();
		for (Player i:Game.players.getL()) {
			p_listModel.addElement(i.getNickname() + ", " + i.getHitpoints() + ", " + i.getState());
		}
		p_list = new JList<String>(p_listModel);
		p_list.setPreferredSize(new Dimension(100, 40*Game.players.getL().size()));
		revalidate();
		Main.GUIframe.pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (return_b == e.getSource()) {
			Main.GUIframe.changePane(Main.GUIframe.mainMenu);
		}
	}
}
