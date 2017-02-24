package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import network.Client;

/**
 * ChatPanel is a jpanel that contains the chat window. This window displays any chat messages or system messages received
 * in the bundle, and displays them. The user can also send chat messages to the server to be displayed
 *
 * @author Andy
 *
 */
public class ChatPanel extends JPanel{
	//references to other panels
	private GameFrame home;
	private String playerName;
	private Client client;
	private UIImageMap imageMap;

	//Text area and message area
	private JTextField typeArea;
	private JTextPane messageList;

	//Text style
	private StyledDocument styled;
	private Style message;
	private Style systemMessage;

	//Send Button
	private ImageIcon[] send;
	private JLabel sendButton;

	//listeners
	private ButtonListen listen;
	private KeyListen keyListen;

	//button interaction
	private boolean interact;

	public ChatPanel(GameFrame display, String playerName, Client client, UIImageMap imageMap) {
		super(new BorderLayout());

		//sets gameframe
		home = display;
		//sets playername
		this.playerName = playerName;
		//sets client
		this.client = client;
		//sets ImageMap
		this.imageMap = imageMap;

		//Panel to hold message area and send button
		JPanel southPanel = new JPanel();
		Color newGrey = new Color(49, 45, 43);
		southPanel.setBackground(newGrey);
		southPanel.setLayout(new GridBagLayout());

		//sets icons for send button
		setSend();

		//creates listeners
		listen = new ButtonListen();
		keyListen = new KeyListen();

		//creates type area with keylistener
		typeArea = new JTextField(30);
		typeArea.addKeyListener(keyListen);

		//creates text button with mouselistener
		sendButton = new JLabel(send[0]);
		sendButton.addMouseListener(listen);

		//creates text area
		messageList = new JTextPane();
		messageList.setEditable(false);
		messageList.setOpaque(true);

		//ensure text area scrolls down when a new message is received
		DefaultCaret caret = (DefaultCaret)messageList.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		//ensures text area can have different colors for different kinds of messages
		styled = messageList.getStyledDocument();

		//message style
		message = messageList.addStyle("Message Style", null);
		StyleConstants.setForeground(message, Color.darkGray);

		//system message style
		systemMessage = messageList.addStyle("System Message", null);
		StyleConstants.setForeground(systemMessage, Color.RED);

		//assigns custom font to chat area and text area
		try {
			Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("slkscr.ttf"));
			typeArea.setFont(font.deriveFont(Font.TRUETYPE_FONT, 12f));
			messageList.setFont(font.deriveFont(Font.TRUETYPE_FONT, 12f));

		} catch (Exception e) {}

		//ensures user can scroll up and down in text area
		JScrollPane area = new JScrollPane(messageList);

		//adds chat panel
		this.add(area, BorderLayout.CENTER);

		//layout for message area
		GridBagConstraints left = new GridBagConstraints();
		left.anchor = GridBagConstraints.LINE_START;
		left.fill = GridBagConstraints.HORIZONTAL;
		left.weightx = 512.0D;
		left.weighty = 1.0D;

		//layout for send button
		GridBagConstraints right = new GridBagConstraints();
		right.insets = new Insets(0, 10, 0, 0);
		right.anchor = GridBagConstraints.LINE_END;
		right.fill = GridBagConstraints.NONE;
		right.weightx = 1.0D;
		right.weighty = 1.0D;

		//add message area to southpanel
		southPanel.add(typeArea, left);
		// add send button to southpanel
		southPanel.add(sendButton, right);

		//add south panel to panel
		this.add(BorderLayout.SOUTH, southPanel);

		//visibility
		this.setOpaque(true);
		this.setVisible(true);
	}

	/**
	 * Adds a received message to the chat window, and applies a style based on what kind of message it is
	 *
	 * @param changes List of messages to add
	 */
	public void addChange(List<String> changes){
		for(String change: changes){ //for each message
			if(change != null){
				if(change.length() > 0){
					if(change.charAt(0) == '<'){ //if message is a chat message
						try { styled.insertString(styled.getLength(), change+"\n" ,message); } //apply chat message style
						catch (BadLocationException e){}
					} else { //message is a system message
						try { styled.insertString(styled.getLength(), change+"\n" ,systemMessage); } //apply system message style
						catch (BadLocationException e){}
					}
				}
			}
		}
	}

	/**
	 * Assigns icon for send button
	 */
	public void setSend(){
		send = new ImageIcon[2];

		send[0] = new ImageIcon(imageMap.getImage("sb"));
		send[1] = new ImageIcon(imageMap.getImage("sbhi"));
	}

	/**
	 * This message gets the client
	 *
	 * @return client
	 */
	public Client getClient(){
		return client;
	}

	/**
	 * Key Listener for the Chat panel
	 *
	 * @author Andy
	 *
	 */
	private class KeyListen implements KeyListener{
		@Override
		public void keyPressed(KeyEvent e) { //if Key is pressed
			if(home.getInteract()){
				if(e.getKeyCode() == KeyEvent.VK_ENTER){ //if key is Enter
					if (typeArea.getText().length() < 1) { //check that an actual message has been entered
						// do nothing if empty
					} else {
						String chat = "CHAT :  " + typeArea.getText(); //gets text from message area
						typeArea.setText(""); //removes text from message area
						getClient().sendCommand(chat); //sends Chat command to client with text
					}
					home.refocus(); //refocuses on AreaDisplay Panel
				} else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){ //if user presses escape
					home.refocus(); //refocuses on AreaDisplayPanel
				}
			}
		}

		//UNUSED
		@Override
		public void keyReleased(KeyEvent arg0) {}

		@Override
		public void keyTyped(KeyEvent arg0) {}

	}

	/**
	 * Button Listener for Chat panel
	 *
	 * @author Andy
	 *
	 */
	private class ButtonListen implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) { //if user clicks send
			if(home.getInteract()){
				if (typeArea.getText().length() < 1) { //check that an actual message has been entered
					// do nothing if empty
				} else {
					String chat = "CHAT :  " + typeArea.getText(); //gets text from message area
					typeArea.setText("");//removes text from message area
					getClient().sendCommand(chat);//sends Chat command to client with text
				}
				home.refocus(); //refocuses on AreaDisplay Panel
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) { //if User highlights Send Button
			if(home.getInteract())
				sendButton.setIcon(send[1]); //changes icon to highlighted

		}

		@Override
		public void mouseExited(MouseEvent e) { //if user unhighlights Send Button
			if(home.getInteract())
				sendButton.setIcon(send[0]); //changes icon to unhighlighted
		}

		//UNUSED
		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}
	}
}
