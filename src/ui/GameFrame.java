package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import game.Bundle;
import network.Client;

/**
 * GameFrame is is where the game is viewed and controlled. It holds references to all panels invovled with the running of an individual game
 * and passes bundles from the client to the required panel.
 *
 * @author Andy
 *
 */
public class GameFrame extends JFrame implements WindowListener {
	//main display penels
	//Main Panel
	private MainPanel mainPanel;
	//Info Panel
	private InfoPanel infoPanel;
	//Inventory Panel
	private InventoryPanel invPanel;
	//Chat Panel
	private ChatPanel chatPanel;
	//UI Image Map
	private UIImageMap imageMap;
	//Sprite Map
	private SpriteMap spriteMap;
	//Button Panel
	private ButtonPanel buttons;
	//Area Display Panel (Game renderer
	private AreaDisplayPanel areaDisplayPanel;

	//Client
	private Client client;
	//Player name
	private String name;
	//Overlay Panel
	private OverlayPanel overlayPanel;

	private boolean interact;

	public GameFrame(String title, Client client, String name) {
		super(title); // Set window title.
		this.interact = true;
		//Creates both image maps
		this.imageMap = new UIImageMap();
		this.spriteMap = new SpriteMap();

		//Game client
		this.client = client;

		//sets up layout
		this.setLayout(new BorderLayout());
		this.setResizable(false); //Do not allow window resizing.

		//creates the various panels, passing them required references to other panels
		this.areaDisplayPanel = new AreaDisplayPanel(this.client, this, spriteMap); //Renderer panel
		this.overlayPanel = new OverlayPanel(areaDisplayPanel, spriteMap); //overlay panel
		this.areaDisplayPanel.setOverLay(this.overlayPanel); //assigns overlay to renderer
		this.infoPanel = new InfoPanel(getContentPane(), this, imageMap); //InfoPanel
		this.buttons = new ButtonPanel(this, this.client, imageMap); //button panel
		this.invPanel = new InventoryPanel(this, this.imageMap, this.client, this.overlayPanel); //inventory panel
		this.chatPanel = new ChatPanel(this, this.name, this.client, this.imageMap);//chat panel

		//adds all panels to the main Panel;
		mainPanel = new MainPanel(this, areaDisplayPanel, chatPanel, invPanel, buttons, imageMap);
		this.add(mainPanel, BorderLayout.NORTH);

		//packs panel sizes
		this.pack();

		//creates assigns overlay panel to glasspane
		setGlassPane(infoPanel);

		//adds window listener to this frame
		this.addWindowListener(this);
		//prevents frame from closing when user clicks the X button
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		// Center window in screen
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension scrnsize = toolkit.getScreenSize();
		setBounds((scrnsize.width - getWidth()) / 2, (scrnsize.height - getHeight()) / 2, getWidth(), getHeight());

		//Display the window
		this.setVisible(true);

	}

	/**
	 * Getter for interact boolean
	 */
	public boolean getInteract(){
		return this.interact;
	}

	/**
	 * Toggles if panel interaction is on or off
	 */
	public void toggleButtons(){
		this.interact = !this.interact;
	}

	/**
	 * Refocuses on the area display panel
	 */
	public void refocus() {
		areaDisplayPanel.requestFocusInWindow();
	}

	/**
	 * Activates the glass panel, which is overlayed in this frame over the game.
	 *
	 * @param info if the panel will be
	 */
	public void setGlass(boolean info){
		infoPanel.setInfo(info);
		getGlassPane().setVisible(true);
		getGlassPane().requestFocus();
	}

	/**
	 * Process the bundle by passing its contents to relevant panels.
	 */
	public void processBundle(Bundle bundle) {
		//passes chat panel any log updates
		if (bundle.getLog() != null && !bundle.getLog().isEmpty())
			chatPanel.addChange(bundle.getLog());

		//passes bundle's player's inventory to inventory panel
		invPanel.addItems(bundle.getPlayerObj().getInventory());

		//passes bundle to render window
		this.areaDisplayPanel.processBundle(bundle);//Temporarily only passing bundle to the renderer.
	}

	/**
	 * If user attempts to close window, displays a dialogue instead.
	 *
	 * @param arg0
	 */
	@Override
	public void windowClosing(WindowEvent arg0) {
		this.closeWindow();
	}

	/**
	 * Confirms with user before closing the game window.
	 */
	public void closeWindow() {
		String optionButtons[] = { "Yes", "No" };
		int PromptResult = JOptionPane.showOptionDialog(null, "Are you sure you want to close the game?",
				"Close Spooky School?", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, optionButtons,
				optionButtons[1]);
		if (PromptResult == 0) {
			this.client.closeSocket();
			System.exit(0); //User has confirmed to close. Close the program completely.
		}
	}

	/**
	 * Force close the game window due to disconnection.
	 */
	public void disconnected() {
		JOptionPane.showMessageDialog(this, "GAME DISCONNECTED FROM SERVER!");
		System.exit(0);

	}

	// UNUSED WINDOW LISTENER METHODS
	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
	}


}
