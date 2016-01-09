package re.neutrino.java_tanks.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener, ItemListener {
	JButton shoot_b;

	GamePanel() {
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		shoot_b = new JButton("shoot");
		shoot_b.setEnabled(false);
		shoot_b.addActionListener(this);

		layout.setHorizontalGroup(
				   layout.createSequentialGroup()
				   	.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
					   	.addGroup(layout.createSequentialGroup()
					   			.addComponent(shoot_b)))
				   	);
		layout.setVerticalGroup(
				   layout.createSequentialGroup()
				   	.addGroup(layout.createParallelGroup()
				   			.addComponent(shoot_b))
				   	);
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
