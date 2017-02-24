package ui;

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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import network.Server;

/**
 * 
 * @author Pritesh R. Patel
 *
 */
public class CreateServerPanel extends JPanel {

	private JPanel contentPane;

	private Integer port = 4444;
	private Server server;

	private BufferedImage uiBackground;
	private JTextField serverStatusField;
	private JTextArea printTextArea;

	private Font customFont;

	public CreateServerPanel(JPanel contentPane) {
		this.contentPane = contentPane;
		this.setLayout(null); //Use no layout manager in this panel.
		this.setBackground(Color.darkGray);
		try {
			customFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("slkscr.ttf"));
		} catch (Exception e) {
		}
		this.setupPanel(); //Sets up this panel. Adds various buttons and input fields.

		try {
			this.uiBackground = ImageIO.read(new File("src/ui/images/networkui_bg.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Sets up the this (createServerPanel) panel. Adds various buttons and input fields.
	 */
	private void setupPanel() {

		//Server Status field
		this.serverStatusField = new JTextField(" Waiting for Server Creation...", 15);
		serverStatusField.setHorizontalAlignment(SwingConstants.CENTER);
		serverStatusField.setEditable(false);
		serverStatusField.setBounds(100, 150, 300, 30);
		serverStatusField.setFont(customFont.deriveFont(Font.TRUETYPE_FONT, 10f));
		this.add(serverStatusField);

		//Add print panel.
		this.printTextArea = new JTextArea("   Waiting for Server Creation...", 15, 35);
		this.printTextArea.setEditable(false); // set textArea non-editable
		this.printTextArea.setFont(customFont.deriveFont(Font.TRUETYPE_FONT, 10f));
		JScrollPane scroll = new JScrollPane(this.printTextArea);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JPanel panel = new JPanel();
		panel.setBounds(100, 200, 300, 200);
		panel.add(scroll);
		panel.setOpaque(false);
		this.add(panel);

		//port label
		JLabel portLabel = new JLabel("Create on Port:");
		portLabel.setForeground(Color.WHITE);
		portLabel.setFont(new Font("Arial", 1, 15));
		portLabel.setBounds(100, 420, 200, 30);
		portLabel.setFont(customFont.deriveFont(Font.TRUETYPE_FONT, 12f));
		this.add(portLabel);


		//Add Port Field
		JTextField portField = new JTextField(this.port.toString(), 15);
		portField.setHorizontalAlignment(SwingConstants.CENTER);
		portField.setBounds(225, 420, 175, 30);
		portField.setFont(customFont.deriveFont(Font.TRUETYPE_FONT, 11f));
		this.add(portField);

		//Add Create Server Button.
		JButton createServerBtn = new JButton("Create Server");
		createServerBtn.setToolTipText("Click here to create a new server");
		createServerBtn.setBounds(100, 460, 300, 70);
		createServerBtn.setFont(customFont.deriveFont(Font.TRUETYPE_FONT, 12f));
		this.add(createServerBtn);

		createServerBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {

				port = Integer.parseInt(portField.getText());

				server = new Server(port, CreateServerPanel.this);
				server.start(); //Start the server.

				portField.setEditable(false); //Disable the ability to hange the port field.
				createServerBtn.setEnabled(false); //Disable create server button
			}
		});
	}


	/**
	 * Used by the server to create updates.
	 * @param update the update to print in the text field.
	 */
	public void updateServerStatusField(String update) {
		this.serverStatusField.setText(update);
	}


	/**
	 * Print an update to the text print area on the create sever panel.
	 * @param update the update to print.
	 */
	public void printToTextPrintArea(String update) {
		String current = this.printTextArea.getText();
		this.printTextArea.setText(current + "\n    " + update);

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(this.uiBackground, 0, 0, null);
	}

}
