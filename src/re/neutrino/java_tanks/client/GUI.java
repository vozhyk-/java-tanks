package re.neutrino.java_tanks.client;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUI extends JFrame {
	JPanel cur_panel = null;
	JPanel MainMenu;
	JPanel Lobby;
	JPanel Game;

	public GUI() throws HeadlessException {
		// TODO Auto-generated constructor stub
		super();
		MainMenu = new MainMenuPanel();
		Lobby = new LobbyPanel();
		Game = new GamePanel();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		changePane(MainMenu);
	}

	void changePane(JPanel new_panel) {
		cur_panel = new_panel;
		dispose();
		setContentPane(new_panel);
		pack();
		setLocationRelativeTo( null );
		setVisible(true);
	}

	public GUI(GraphicsConfiguration arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public GUI(String arg0) throws HeadlessException {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public GUI(String arg0, GraphicsConfiguration arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

}
