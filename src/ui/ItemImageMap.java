package ui;

import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * Holds a map of all image objects used in inventory
 *
 * @author Andy
 *
 */
public class ItemImageMap {
	private static final String IMAGE_PATH = "itemimages/";
	private HashMap<String, Image> spriteMap;

	public ItemImageMap() {
		loadMap();
	}

	/**
	 * Returns corresponding image object from given token string
	 *
	 * @param - token
	 * @return - Image
	 */
	public Image getImage(String token) {
		Image image = spriteMap.get(token);
		if (image == null) {
			System.out.println("null image on input " + token);
		}
		return image;
	}

	/**
	 * Sets up map of images
	 */
	public void loadMap() {
		spriteMap = new HashMap<String, Image>();

		spriteMap.put("bag0", loadImage("bag.png")); //rubbish bag
		spriteMap.put("ba0", loadImage("backpack.png")); //backpack
		spriteMap.put("b0", loadImage("box.png")); //box
		spriteMap.put("bo0", loadImage("book.png")); //book
		spriteMap.put("bu0", loadImage("burger.png")); //burger
		spriteMap.put("ca0", loadImage("cake.png")); //slice of cake
		spriteMap.put("c0", loadImage("coin.png")); //coin
		spriteMap.put("co0", loadImage("coffee.png")); //coffee
		spriteMap.put("h0", loadImage("headphones.png")); //headset
		spriteMap.put("g0", loadImage("gameboy.png")); //gameboy
		spriteMap.put("k0", loadImage("key.png")); //key
		spriteMap.put("La1", loadImage("laptop.png")); //laptop
		spriteMap.put("pa0", loadImage("pad.png")); //pad paper
		spriteMap.put("p0", loadImage("phone.png")); //phone
		spriteMap.put("pi0", loadImage("pizza.png")); //pizza
		spriteMap.put("w0", loadImage("waterbottle.png")); //water bottle
		spriteMap.put("ep0", loadImage("examPaper0.png")); //exam paper

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
