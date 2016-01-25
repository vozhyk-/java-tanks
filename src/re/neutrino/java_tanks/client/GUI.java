package re.neutrino.java_tanks.client;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GUI extends JFrame {
	JPanel cur_panel = null;
	JPanel mainMenu;
	JPanel lobby;
	JPanel game;
	JPanel endGame;

	void join_dialog(String msg) {
		dialog("Join: Warning", msg);
	}

	void conf_dialog(String msg) {
		dialog("Config: Warning", msg);
	}

	void err_dialog(String msg) {
		dialog("Error:", msg);
	}

	private void dialog(String header, String msg) {
		JOptionPane.showMessageDialog(this, msg, header, JOptionPane.WARNING_MESSAGE);
	}

	public GUI() throws HeadlessException {
		super();
		mainMenu = new MainMenuPanel();
		lobby = new LobbyPanel();
		game = new GamePanel();
		endGame = new EndGamePanel();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		changePane(mainMenu);
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
