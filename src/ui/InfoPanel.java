package ui;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

/**
 * InfoPanel is displayed when user selects to display Information or About. It is inserted on the Glasspane of the frame
 *
 * @author Andy
 *
 */
public class InfoPanel extends JPanel{
	//references to other panels
	private Container contentPane;
	private GameFrame home;
	private UIImageMap imageMap;
	//icons for button
	private ImageIcon[] icons;
	//OK button
	private JLabel ok;
	//textpane and storage Panel
	private JTextPane infoTest;
	private JPanel textPanel;
	//button listener
	private ButtonListen listen;

	private Image background;

	public InfoPanel(Container contentPane, GameFrame home, UIImageMap imageMap){
		super();
		
		//sets game frame
		this.home = home;
		//sets where this will be displayed
		this.contentPane = contentPane;
		//sets imagemap
		this.imageMap = imageMap;
		//sets icons for OK button
		setIcons();
		//creates new textpane to display information
		infoTest = new JTextPane();
		infoTest.setBackground(new Color(0,0,0,0));
		textPanel = new JPanel();
		textPanel.add(infoTest);
		textPanel.setOpaque(true);

		//assigns custom font to the textpane
		try {
			Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("slkscr.ttf"));
			infoTest.setFont(font.deriveFont(Font.TRUETYPE_FONT, 12f));
		} catch (Exception e) {}

		//sets formatting of text in textpane
		infoTest.setForeground(Color.WHITE);
		//ensures text cannot be changed
		infoTest.setEditable(false);
		//prevent the default textpane window from being displayed
		infoTest.setOpaque(false);
		//listener for OK button
		listen = new ButtonListen();

		//creates OK button and assigns it a mouse listener
		ok = new JLabel(icons[0]);
		ok.setPreferredSize(new Dimension(icons[0].getIconWidth(), icons[0].getIconHeight()));
		ok.addMouseListener(listen);
		this.setOpaque(false);
		//adds text to panel

		
		this.add(Box.createRigidArea(new Dimension(160,1)));
		this.add(new UIPanel(textPanel, 400, 350, imageMap));
		this.add(Box.createRigidArea(new Dimension(160,1)));
		//this.add(Box.createRigidArea(new Dimension(200,1)));
		//adds ok button to panel
		this.add(ok);
		
	}

	/**
	 * Sets the default and highlighted states for each button
	 */
	public void setIcons(){
		icons = new ImageIcon[6];

		icons[0] = new ImageIcon(imageMap.getImage("ob"));
		icons[1] = new ImageIcon(imageMap.getImage("obhi"));
		background = imageMap.getImage("infBack");

	}
	/**
	 * Determines if this panel should display Info or About
	 *
	 * @param info if panel will be Info Panel or not
	 */
	public void setInfo(boolean info){
		if(info){
			setAsInfo();
		} else {
			setAsAbout();
		}
	}

	/**
	 * Sets text to info window
	 */
	public void setAsInfo(){
		infoTest.setText("Spooky School! \n\n"+
						"Controls: \n\n"+
						"Use arrow keys to move.\n'l' : rotate screen anti-clockwise\n'r' : rotate screen clockwise\n'z' : perform an action (examine/open a door)");
	}

	/**
	 * Sets text to about window
	 */
	public void setAsAbout(){
		infoTest.setText("SWEN 222 - Assignment 03 - Team 8 - \nSpooky School - Created by:\n\n"+
				"- Cameron McLachlan ( mclachcame ) \nCameron.McLachlan@ecs.vuw.ac.nz\n\n"+
				"- Andrew Mundt ( mundtandy ) \nAndrew.Mundt@ecs.vuw.ac.nz\n\n"+
				"- Priteshbhai Patel ( patelprit2 ) \npatelprit2@ecs.vuw.ac.nz\n\n"+
				"- Rong Wang ( wangrong ) \nRong.Wang@ecs.vuw.ac.nz\n\n"+
		        "- Chethana Wijesekera ( wijesechet ) \nChethana.Wijesekera@ecs.vuw.ac.nz");
	}


	@Override
	public void paintComponent(Graphics gr) {
		super.paintComponent(gr);//makes sure MyGlass's widgets are drawn automatically
		gr.drawImage(background, 0, 0, null);

	}

	/**
	 * Mouse listener for InfoPanel
	 *
	 * @author Andy
	 *
	 */
	private class ButtonListen implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) { //if user clicks the button
			if(e.getSource() == ok){
				home.toggleButtons();
				setVisible(false); //hide panel
			}
		}


		@Override
		public void mouseEntered(MouseEvent e) { //if user highlights OK button
			if(e.getSource() == ok)
				ok.setIcon(icons[1]); //changes icon

		}

		@Override
		public void mouseExited(MouseEvent e) { //if user unhighlights OK button
			if(e.getSource() == ok)
				ok.setIcon(icons[0]); //changes icon
		}

		//UNUSED
		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}
	}
}
