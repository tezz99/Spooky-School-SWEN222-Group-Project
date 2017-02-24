package ui;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class OverlayPanel extends JPanel {

	private AreaDisplayPanel panel;
	private SpriteMap spriteMap;
	private Thread thread;

	private final int delay = 8;
	private long now; //holds time

	//Header box
	private String headerMessage;
	private int headerX = -155;
	private int headerY = 5;
	private int headerWidth = 155;
	private int headerIncrement;
	private long headerThen; //holds time
	private boolean headerReceived = false; //To stop the null pointer at the start of the game.

	//Footer Message box
	private int footerX = 50;
	private int footerY = 580;
	private String footerMessage;
	private int footerIncrement;
	private long footerThen; //holds time
	private boolean footerReceived = false; //To stop the null pointer at the start of the game.
	private Font font;

	long mainThen;

	public OverlayPanel(AreaDisplayPanel panel, SpriteMap spriteMap) {

		this.panel = panel;
		this.spriteMap = spriteMap;
		this.mainThen = System.currentTimeMillis() + delay;

		this.thread = new Thread() {
			@Override
			public void run() {
				while (true) {
					if (System.currentTimeMillis() > mainThen) {
						tick();
						repaint();
						mainThen = System.currentTimeMillis() + delay;
					}

				}
			}
		};

		try {
			this.font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("slkscr.ttf"));
			Graphics2D g2d = (Graphics2D) getGraphics();
			g2d.setFont(font.deriveFont(Font.TRUETYPE_FONT, 12f));

		} catch (Exception e) {
		}

		thread.start();
	}

	public void setHeaderMessage(String name) {
		this.headerMessage = name;
		this.headerX = -155;
		headerIncrement = 2;
		this.headerReceived = true;
	}

	public void setFooterMessage(String message) {
		this.footerMessage = message;
		this.footerY = 580;
		this.footerIncrement = -4;
		this.footerReceived = true;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;

		//Set the font.
		g2d.setFont(font.deriveFont(Font.TRUETYPE_FONT, 12f));

		//Draw the header
		if (this.headerMessage != null) {

			g2d.drawImage(spriteMap.getImage("H0"), headerX, headerY, null);

			//Draw the header message in the center.
			int stringLen = (int) g2d.getFontMetrics().getStringBounds(headerMessage, g2d).getWidth();
			int start = headerWidth / 2 - stringLen / 2;
			g2d.drawString(headerMessage, start + headerX, headerY + 17);
		}

		//Draw the footer
		if (this.footerMessage != null) {
			g.drawImage(spriteMap.getImage("P0"), footerX, footerY, null);
			this.drawStringMultiLine(g, this.footerMessage, 480, footerX + 10, footerY + 25);
		}
	}

	public void tick() {

		now = System.currentTimeMillis();

		//Display the header message. The boolean is there so that we don't try to print
		//a message when we have never had one before yet as this can cause null pointer.
		if (headerReceived) {
			headerX += headerIncrement;
			if (headerX == 5) {
				headerX = 6;
				headerIncrement = 0;
				headerThen = now + 3000;
			} else if (headerIncrement == 0) {
				if (now > headerThen) {
					headerX = 4;
					headerIncrement = -2;
				}
			} else if (headerX < -160) {
				headerReceived = false;
			}

		}

		//Display the footer message. The boolean is there so that we don't try to print
		//a message when we have never had one before yet as this can cause null pointer.
		if (this.footerReceived) {
			this.footerY = this.footerY + footerIncrement;
			if (footerY == 420) {
				this.footerY = 421;
				this.footerIncrement = 0;
				this.footerThen = now + 3500; //Pause for 3.5secs.
			} else if (this.footerIncrement == 0) {
				if (now > footerThen) {
					this.footerY = 421;
					this.footerIncrement = 4;
				}
			} else if (footerX > 620) {
				this.footerReceived = false; //Used
			}
		}
	}

	/**
	 * This method draws a string on the panel. If the string is too long (measured by parameter "linewidth"), then it will
	 * display the string in multiple lines.
	 * @param g graphics
	 * @param text to be printed onto the panel
	 * @param lineWidth how long a single line of text can be (in pixels).
	 * @param x position of where to start drawing string from.
	 * @param y position of where to start drawing string from.
	 */
	public void drawStringMultiLine(Graphics g, String text, int lineWidth, int x, int y) {
		FontMetrics m = g.getFontMetrics();
		if (m.stringWidth(text) < lineWidth) {
			g.drawString(text, x, y);
		} else {
			String[] words = text.split(" ");
			String currentLine = words[0];
			for (int i = 1; i < words.length; i++) {
				if (m.stringWidth(currentLine + words[i]) < lineWidth) {
					currentLine += " " + words[i];
				} else {
					g.drawString(currentLine, x, y);
					y += m.getHeight();
					currentLine = words[i];
				}
			}
			if (currentLine.trim().length() > 0) {
				g.drawString(currentLine, x, y);
			}
		}
	}

}
