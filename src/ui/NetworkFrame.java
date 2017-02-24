package ui;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * 
 * @author Pritesh R. Patel
 *
 */
public class NetworkFrame extends JFrame implements WindowListener {

	private JPanel contentPane; //This pane displays all of the other frames

	public NetworkFrame(String windowTitle) {
		super(windowTitle); // Set window title.

		setSize(500, 600); // default size is 0,0
		this.setResizable(false); //Do not allow window resizing.
		this.addWindowListener(this); //This frame also implements window listener so "add it"
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //Don't close window when x button is pressed. Allows us to get user confirmation.

		// Center window in screen
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension scrnsize = toolkit.getScreenSize();
		setBounds((scrnsize.width - getWidth()) / 2, (scrnsize.height - getHeight()) / 2, getWidth(), getHeight());

		this.setUpMenuBar(); //Sets up the menu bar
		this.setUpGamePanels(); //Set up all of the different game panels that make up the game...

		this.setContentPane(this.contentPane);

		this.setVisible(true); //Display the window
	}


	/**
	 * Sets up all the different panels or "screens" that make up this networking menu using card layout.
	 */
	private void setUpGamePanels() {
		this.contentPane = new JPanel(); //This pane has access to all other panels that are used in this game.
		this.contentPane.setLayout(new CardLayout());

		contentPane.add(new NetworkMenuPanel(contentPane), "NetworkMenuScreen");
		contentPane.add(new CreateServerPanel(contentPane), "CreateServerScreen");
		contentPane.add(new JoinServerPanel(contentPane), "JoinServerScreen");

	}


	private void setUpMenuBar() {
		JMenuBar menuBar = new JMenuBar(); //Create a menu bar
		JMenu fileMenu = new JMenu("File"); //create a menu 
		menuBar.add(fileMenu); //Add the menu to the menu bar
		this.setJMenuBar(menuBar); //Add the menu bar to the frame.


		//Create JMenu Items
		JMenuItem help = new JMenuItem(new AbstractAction("Help") {
			@Override
			public void actionPerformed(ActionEvent event) {
				JOptionPane.showConfirmDialog(null, "Help coming soon", "Help", JOptionPane.DEFAULT_OPTION,
						JOptionPane.PLAIN_MESSAGE);
			}
		});

		JMenuItem exit = new JMenuItem(new AbstractAction("Exit") {
			@Override
			public void actionPerformed(ActionEvent event) {
				closeWindow(); //Confirm close window.
			}
		});

		//Add all menu options to the file menu.
		fileMenu.add(help);
		fileMenu.add(exit);

	}


	@Override
	public void windowClosing(WindowEvent arg0) {
		this.closeWindow();
	}

	/**
	 * Confirms with user before closing the game window.
	 */
	public void closeWindow() {
		String optionButtons[] = { "Yes", "No" };
		int PromptResult = JOptionPane.showOptionDialog(null,
				"Closing this window will disconnect the client/server. Close?", "Close Spooky School?",
				JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, optionButtons, optionButtons[1]);
		if (PromptResult == 0) {
			System.exit(0); //User has confirmed to close. Close the program completely.
		}

	}


	// UNUSED WINDOW LISTENER METHODS
	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {}

	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0) {}



}
