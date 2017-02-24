package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * A simple panel containing arrows that allow the user to control the inventory panel
 *
 * @author Andy
 *
 */
public class ScrollInvPanel extends JPanel{
	//references to other panels
	private GameFrame home;
	private InventoryPanel invPanel;
	private UIImageMap imageMap;
	//icons for buttons
	private ImageIcon[] icons;
	//mouse listener
	private ButtonListen listen;
	//buttons
	private JLabel up;
	private JLabel down;

	public ScrollInvPanel(GameFrame home, InventoryPanel invPanel, UIImageMap imageMap){
		super(new BorderLayout());
		this.home = home;
		//sets inventory panel
		this.invPanel = invPanel;
		//sets image map
		this.imageMap = imageMap;

		//allignment of buttons
		JPanel fit = new JPanel(new FlowLayout());

		fit.setOpaque(false);
		fit.setPreferredSize(new Dimension(40, 100));
		//sets icons for the buttons
		setIcons();
		//creates mouse listener
		listen = new ButtonListen();

		//creates both buttons
		up = new JLabel(icons[0]);
		down = new JLabel(icons[2]);

		//adds mouse listeners
		up.addMouseListener(listen);
		down.addMouseListener(listen);

		//adds buttons to panel
		fit.add(up);
		fit.add(down);
		this.add(fit, BorderLayout.EAST);

		setOpaque(false);
	}

	/**
	 * Sets the default and highlighted states for each button
	 */
	public void setIcons(){
		icons = new ImageIcon[6];

		icons[0] = new ImageIcon(imageMap.getImage("ub"));
		icons[1] = new ImageIcon(imageMap.getImage("ubhi"));
		icons[2] = new ImageIcon(imageMap.getImage("db"));
		icons[3] = new ImageIcon(imageMap.getImage("dbhi"));

	}

	/**
	 * This is the buttonListener for the ScrollInvPanel
	 *
	 * @author Andy
	 *
	 */
	private class ButtonListen implements MouseListener{
		@Override
		public void mouseClicked(MouseEvent e) {
			if(home.getInteract()){
				if(e.getSource() == up){ //up has been pressed
					invPanel.upOne();
				} else { //down game has been pressed
					invPanel.downOne();
				}
			}
		}


		@Override
		public void mouseEntered(MouseEvent e) {
			if(home.getInteract()){
				if(e.getSource() == up){ //up is highlighted
					up.setIcon(icons[1]);
				} else { //down  is highlighted
					down.setIcon(icons[3]);
				}
			}
		}
		@Override
		public void mouseExited(MouseEvent e) {
			if(home.getInteract()){
				if(e.getSource() == up){ //up is unhighlighted
					up.setIcon(icons[0]);
				} else { //down is highlighted
					down.setIcon(icons[2]);
				}
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}
	}
}
