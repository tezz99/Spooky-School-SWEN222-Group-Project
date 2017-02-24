package ui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class DebuggingPanel extends JPanel{

	public DebuggingPanel(){
		super();
		setBackground(Color.BLUE);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
	}
}
