package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

/**
 * UIPanel contains another panel, and displays a border around it, based on a size value passed to this class.
 *
 * @author Andy
 *
 */
public class UIPanel extends JPanel {
	//references to other panels
	private JPanel panel;
	private UIImageMap imageMap;
	//size of panel
	private int width;
	private int height;
	//corner and border panels
	private JPanel background;
	private JLabel topLeft;
	private JLabel topRight;
	private JLabel bottomLeft;
	private JLabel bottomRight;
	private JLabel leftBorder;
	private JLabel rightBorder;
	private JLabel topBorder;
	private JLabel bottomBorder;

	public UIPanel(JPanel panel, int width, int height, UIImageMap imageMap){
		super();

		//sets size of panel
		this.width = width+12;
		this.height = height+12;
		//Panel to be stored
		this.panel = panel;
		//image map
		this.imageMap = imageMap;

		//creates layered pane to store panel overtop the background panel
		JLayeredPane layers = new JLayeredPane();
		layers.setLayout(null);
		layers.setPreferredSize(new Dimension(width, height));
		layers.setOpaque(false);
		//set labels for each border panel
		setLabels();

		//creates background panel, which contains all the border assets
		background = new JPanel(new BorderLayout(0, 0));
		background.setOpaque(false);
		//top border
		JPanel top = new JPanel(new BorderLayout(0,0));
		top.add(topLeft, BorderLayout.WEST);
		top.add(topBorder, BorderLayout.CENTER);
		top.add(topRight, BorderLayout.EAST);
		top.setOpaque(false);

		//side borders
		JPanel mid = new JPanel(new BorderLayout(0,0));
		mid.add(leftBorder, BorderLayout.WEST);
		mid.add(rightBorder, BorderLayout.EAST);
		mid.setBackground(Color.BLACK);
		mid.setOpaque(true);

		//bottom border
		JPanel bottom = new JPanel(new BorderLayout(0,0));
		bottom.add(bottomLeft, BorderLayout.WEST);
		bottom.add(bottomBorder, BorderLayout.CENTER);
		bottom.add(bottomRight, BorderLayout.EAST);
		bottom.setOpaque(false);

		//adds top border to  background
		background.add(top, BorderLayout.NORTH);
		//adds side borders to background
		background.add(mid, BorderLayout.CENTER);
		//adds bottom border to background
		background.add(bottom, BorderLayout.SOUTH);

		background.setBounds(0, 0, width, height);

		//adds background panel to layered panel
		layers.add(background, new Integer(0), 0);

		//assigns coordinates for panel to be displayed
		this.panel.setBounds(6,6,(width-12),(height-12));
		this.panel.setOpaque(false);
		//adds panel over top background panel
		layers.add(this.panel, new Integer(1), 0);

		//visibility
		setOpaque(false);
		this.add(layers);
		this.setVisible(true);
		validate();
	}

	/**
	 * Sets up the JLabels that make up the border of a UI Panel
	 */
	public void setLabels(){
		Dimension corner = new Dimension(7,7);
		Dimension horizontal = new Dimension(width-14, 7);
		Dimension vertical = new Dimension(7, height-14);

		//corner panels
		topLeft = new JLabel(new ImageIcon(imageMap.getImage("tL")));
		topLeft.setSize(new Dimension(corner));
		topLeft.setOpaque(false);
		topRight = new JLabel(new ImageIcon(imageMap.getImage("tR")));
		topRight.setSize(new Dimension(corner));
		topRight.setOpaque(false);
		bottomLeft = new JLabel(new ImageIcon(imageMap.getImage("bL")));
		bottomLeft.setSize(new Dimension(corner));
		bottomLeft.setOpaque(false);
		bottomRight = new JLabel(new ImageIcon(imageMap.getImage("bR")));
		bottomRight.setSize(new Dimension(corner));
		bottomRight.setOpaque(false);

		//side panels
		leftBorder = new JLabel();
		leftBorder.setSize(new Dimension(vertical));
		rightBorder = new JLabel();
		leftBorder.setSize(new Dimension(vertical));
		topBorder = new JLabel();
		leftBorder.setSize(new Dimension(horizontal));
		bottomBorder = new JLabel();
		leftBorder.setSize(new Dimension(horizontal));

		//scales border image to border panel size
		Image topImage = imageMap.getImage("tB").getScaledInstance(width-14, 7, Image.SCALE_SMOOTH);
		ImageIcon topIcon = new ImageIcon(topImage);
		topBorder.setIcon(topIcon);

		Image leftImage = imageMap.getImage("lB").getScaledInstance(7, height-14, Image.SCALE_SMOOTH);
		ImageIcon leftIcon= new ImageIcon(leftImage);
		leftBorder.setIcon(leftIcon);

		Image bottomImage = imageMap.getImage("bB").getScaledInstance(width-14, 7, Image.SCALE_SMOOTH);
		ImageIcon bottomIcon = new ImageIcon(bottomImage);
		bottomBorder.setIcon(bottomIcon);

		Image rightImage = imageMap.getImage("rB").getScaledInstance(7, height-14, Image.SCALE_SMOOTH);
		ImageIcon rightIcon = new ImageIcon(rightImage);
		rightBorder.setIcon(rightIcon);
	}
}
