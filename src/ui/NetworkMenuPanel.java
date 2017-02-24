package ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * The game's main menu. This is where you create new servers, join existing servers and load new games.
 * @author Pritesh R. Patel
 *
 */
public class NetworkMenuPanel extends JPanel {

	private JPanel contentPane;
	private BufferedImage uiBackground;

	//Buttons
	private JButton newServerBtn;
	private JButton joinServerBtn;
	private JButton localGameBtn;
	private Font customFont;

	public NetworkMenuPanel(JPanel contentPane) {
		this.contentPane = contentPane;
		this.setLayout(null); //Use no layout manager in this panel.
		this.setBackground(Color.darkGray);

		try {
			this.uiBackground = ImageIO.read(new File("src/ui/images/networkui_bg.png"));
			customFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("slkscr.ttf"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.addNetworkMenuButtons(); //Add buttons to this panel
	}


	/**
	 * Add the various menu buttons to this panel
	 */
	private void addNetworkMenuButtons() {

		//Create and add newServerButton
		this.newServerBtn = new JButton("New Server");
		this.newServerBtn.setToolTipText("Click here to create a new Spooky School Server");
		this.newServerBtn.setBounds(100, 200, 300, 70);
		this.newServerBtn.setFont(customFont.deriveFont(Font.TRUETYPE_FONT, 12f));
		this.newServerBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				CardLayout cardLayout = (CardLayout) contentPane.getLayout();
				cardLayout.show(contentPane, "CreateServerScreen"); //Player pressed start. Move onto the next screen.
			}
		});
		this.add(this.newServerBtn);

		//Create and add joinServerButton
		this.joinServerBtn = new JButton("Join Server");
		this.joinServerBtn.setToolTipText("Click here to join an existing Spooky School Server");
		this.joinServerBtn.setBounds(100, 300, 300, 70);
		this.joinServerBtn.setFont(customFont.deriveFont(Font.TRUETYPE_FONT, 12f));
		this.joinServerBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				CardLayout cardLayout = (CardLayout) contentPane.getLayout();
				cardLayout.show(contentPane, "JoinServerScreen"); //Player pressed start. Move onto the next screen.
			}
		});
		this.add(this.joinServerBtn);
	}	
	
	/*
		//Create and add play local game button
		this.localGameBtn = new JButton("Begin Local Game");
		this.localGameBtn.setToolTipText("Click here to bein a new local game");
		this.localGameBtn.setBounds(100, 400, 300, 70);
		this.localGameBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				//TODO add ability to play local game here
			}
		});
		this.add(this.localGameBtn);

	}*/

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(this.uiBackground, 0, 0, null);
	}

}
