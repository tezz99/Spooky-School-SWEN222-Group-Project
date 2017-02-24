package ui;


import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * Holds a map of all image objects used in creation of UI
 *
 * @author Andy
 *
 */
public class UIImageMap {
	private static final String IMAGE_PATH = "UIimages/";
	private HashMap<String, Image> spriteMap;

	public UIImageMap(){
		loadMap();
	}

	/**
	 * Returns corresponding image object from given token string
	 *
	 * @param - token
	 * @return - Image
	 */
	public Image getImage(String token){
		Image image = spriteMap.get(token);
		if(image == null){
			System.out.println("null image on input " + token);
		}
		return image;
	}

	/**
	 * Sets up map of images
	 */
	public void loadMap(){
		spriteMap = new HashMap<String, Image>();

		//borders
		spriteMap.put("bB",loadImage("bottomBorder.png"));
		spriteMap.put("lB",loadImage("leftBorder.png"));
		spriteMap.put("rB",loadImage("rightBorder.png"));
		spriteMap.put("tB",loadImage("topBorder.png"));

		//corner
		spriteMap.put("tL",loadImage("topLeft.png"));
		spriteMap.put("tR",loadImage("topRight.png"));
		spriteMap.put("bL",loadImage("bottomLeft.png"));
		spriteMap.put("bR",loadImage("bottomRight.png"));

		//highlight
		spriteMap.put("hi",loadImage("highlight.png"));

		//buttons
		spriteMap.put("ab",loadImage("about.png"));
		spriteMap.put("abhi",loadImage("abouthighlight.png"));
		spriteMap.put("ob", loadImage("ok.png"));
		spriteMap.put("obhi",loadImage("okhighlight.png"));
		spriteMap.put("sab",loadImage("save.png"));
		spriteMap.put("sabhi", loadImage("savehighlight.png"));
		spriteMap.put("ib",loadImage("info.png"));
		spriteMap.put("ibhi",loadImage("infohighlight.png"));
		spriteMap.put("sb",loadImage("send.png"));
		spriteMap.put("sbhi",loadImage("sendhighlight.png"));
		spriteMap.put("ub", loadImage("uparrow.png"));
		spriteMap.put("ubhi",loadImage("uparrowhigh.png"));
		spriteMap.put("db",loadImage("downarrow.png"));
		spriteMap.put("dbhi", loadImage("downarrowhigh.png"));

		//inv background
		spriteMap.put("invBack",loadImage("invBackground.png"));
		spriteMap.put("invPanel", loadImage("invPanel.png"));
		spriteMap.put("invPanel2", loadImage("invPanel2.png"));
		spriteMap.put("infBack", loadImage("infobackground.png"));

	}

	/**
	 * Load an image from the file system, using a given filename.
	 *
	 * @param filename
	 * @return
	 */
	public Image loadImage(String filename) {
		// using the URL means the image loads when stored
		// in a jar or expanded into individual files.
		java.net.URL imageURL = SpriteMap.class.getResource(IMAGE_PATH + filename);
		try {
			Image img = ImageIO.read(imageURL);
			return img;
		} catch (IOException e) {
			// we've encountered an error loading the image. There's not much we
			// can actually do at this point, except to abort the game.
			throw new RuntimeException("Unable to load image: " + filename);
		}
	}

}
