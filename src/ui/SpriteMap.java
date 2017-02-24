package ui;

import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * Holds a map that maps all of the image objects to there representing String 'type' e.g."w0"

 * @author Cameron McLachlan
 *
 */
public class SpriteMap {

	private static final String IMAGE_PATH = "images/";
	private HashMap<String, Image> spriteMap;

	public SpriteMap() {
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

	public void loadMap() {

		spriteMap = new HashMap<String, Image>();

		//walls
		spriteMap.put("w0", loadImage("wall0.png"));
		spriteMap.put("w1", loadImage("wall1.png"));
		spriteMap.put("w2", loadImage("wall2.png"));
		spriteMap.put("w3", loadImage("wall3.png"));
		spriteMap.put("u0", loadImage("bathroomWall0.png"));
		spriteMap.put("u1", loadImage("bathroomWall1.png"));
		spriteMap.put("u2", loadImage("bathroomWall2.png"));
		spriteMap.put("u3", loadImage("bathroomWall3.png"));
		spriteMap.put("f0", loadImage("fence0.png"));
		spriteMap.put("f1", loadImage("fence1.png"));
		spriteMap.put("f2", loadImage("fence2.png"));
		spriteMap.put("f3", loadImage("fence3.png"));

		// wall Corners
		spriteMap.put("W0", loadImage("wallCorner0.png"));
		spriteMap.put("W1", loadImage("wallCorner1.png"));
		spriteMap.put("W2", loadImage("wallCorner2.png"));
		spriteMap.put("W3", loadImage("wallCorner3.png"));
		spriteMap.put("U0", loadImage("bathroomWallCorner0.png"));
		spriteMap.put("U1", loadImage("bathroomWallCorner1.png"));
		spriteMap.put("U2", loadImage("bathroomWallCorner2.png"));
		spriteMap.put("U3", loadImage("bathroomWallCorner3.png"));
		spriteMap.put("F0", loadImage("fenceCorner0.png"));
		spriteMap.put("F1", loadImage("fenceCorner1.png"));
		spriteMap.put("F2", loadImage("fenceCorner2.png"));
		spriteMap.put("F3", loadImage("fenceCorner3.png"));

		// floors
		spriteMap.put("c0", loadImage("carpet0.png"));
		spriteMap.put("c1", loadImage("carpet1.png"));
		spriteMap.put("c2", loadImage("carpet2.png"));
		spriteMap.put("c3", loadImage("carpet3.png"));
		spriteMap.put("g0", loadImage("grass0.png"));
		spriteMap.put("g1", loadImage("grass1.png"));
		spriteMap.put("g2", loadImage("grass2.png"));
		spriteMap.put("g3", loadImage("grass3.png"));
		spriteMap.put("h0", loadImage("hard0.png"));
		spriteMap.put("h1", loadImage("hard1.png"));
		spriteMap.put("h2", loadImage("hard2.png"));
		spriteMap.put("h3", loadImage("hard3.png"));
		spriteMap.put("G0", loadImage("grassLarge0.png"));
		spriteMap.put("G1", loadImage("grassLarge1.png"));
		spriteMap.put("G2", loadImage("grassLarge2.png"));
		spriteMap.put("G3", loadImage("grassLarge3.png"));
		spriteMap.put("L0", loadImage("lino0.png"));
		spriteMap.put("L1", loadImage("lino1.png"));
		spriteMap.put("L2", loadImage("lino2.png"));
		spriteMap.put("L3", loadImage("lino3.png"));

		// doors/windows/chests
		spriteMap.put("d00", loadImage("door00.png"));
		spriteMap.put("d10", loadImage("door10.png"));
		spriteMap.put("d20", loadImage("door20.png"));
		spriteMap.put("d30", loadImage("door30.png"));
		spriteMap.put("d01", loadImage("door01.png"));
		spriteMap.put("d11", loadImage("door11.png"));
		spriteMap.put("d21", loadImage("door21.png"));
		spriteMap.put("d31", loadImage("door31.png"));
		spriteMap.put("w00", loadImage("window00.png"));
		spriteMap.put("w10", loadImage("window10.png"));
		spriteMap.put("w20", loadImage("window20.png"));
		spriteMap.put("w30", loadImage("window30.png"));
		spriteMap.put("w01", loadImage("window01.png"));
		spriteMap.put("w11", loadImage("window11.png"));
		spriteMap.put("w21", loadImage("window21.png"));
		spriteMap.put("w31", loadImage("window31.png"));
		spriteMap.put("ch00", loadImage("chest00.png"));
		spriteMap.put("ch10", loadImage("chest10.png"));
		spriteMap.put("ch20", loadImage("chest20.png"));
		spriteMap.put("ch30", loadImage("chest30.png"));
		spriteMap.put("ch01", loadImage("chest01.png"));
		spriteMap.put("ch11", loadImage("chest11.png"));
		spriteMap.put("ch21", loadImage("chest21.png"));
		spriteMap.put("ch31", loadImage("chest31.png"));

		// bed
		spriteMap.put("o0", loadImage("bed0.png"));
		spriteMap.put("o1", loadImage("bed1.png"));
		spriteMap.put("o2", loadImage("bed2.png"));
		spriteMap.put("o3", loadImage("bed3.png"));

		// key
		spriteMap.put("k0", loadImage("key0.png"));
		spriteMap.put("k1", loadImage("key1.png"));
		spriteMap.put("k2", loadImage("key2.png"));
		spriteMap.put("k3", loadImage("key3.png"));

		// furniture
		spriteMap.put("x0", loadImage("furniture0.png"));
		spriteMap.put("x1", loadImage("furniture1.png"));
		spriteMap.put("x2", loadImage("furniture2.png"));
		spriteMap.put("x3", loadImage("furniture3.png"));

		// furniture
		spriteMap.put("bo0", loadImage("bookcase0.png"));
		spriteMap.put("bo1", loadImage("bookcase1.png"));
		spriteMap.put("bo2", loadImage("bookcase2.png"));
		spriteMap.put("bo3", loadImage("bookcase3.png"));

		// chair
		spriteMap.put("ch0", loadImage("chair0.png"));
		spriteMap.put("ch1", loadImage("chair1.png"));
		spriteMap.put("ch2", loadImage("chair2.png"));
		spriteMap.put("ch3", loadImage("chair3.png"));

		// chairTwo
		spriteMap.put("Ch0", loadImage("chairTwo0.png"));
		spriteMap.put("Ch1", loadImage("chairTwo1.png"));
		spriteMap.put("Ch2", loadImage("chairTwo2.png"));
		spriteMap.put("Ch3", loadImage("chairTwo3.png"));

		// principle chair
		spriteMap.put("pc0", loadImage("principleChair0.png"));
		spriteMap.put("pc1", loadImage("principleChair1.png"));
		spriteMap.put("pc2", loadImage("principleChair2.png"));
		spriteMap.put("pc3", loadImage("principleChair3.png"));

		//principle desk
		spriteMap.put("pd0", loadImage("principleDesk0.png"));
		spriteMap.put("pd1", loadImage("principleDesk1.png"));
		spriteMap.put("pd2", loadImage("principleDesk2.png"));
		spriteMap.put("pd3", loadImage("principleDesk3.png"));

		// pizza
		spriteMap.put("pi0", loadImage("pizza0.png"));
		spriteMap.put("pi1", loadImage("pizza1.png"));
		spriteMap.put("pi2", loadImage("pizza2.png"));
		spriteMap.put("pi3", loadImage("pizza3.png"));

		// pizza
		spriteMap.put("ep0", loadImage("examPaper0.png"));
		spriteMap.put("ep1", loadImage("examPaper1.png"));
		spriteMap.put("ep2", loadImage("examPaper2.png"));
		spriteMap.put("ep3", loadImage("examPaper3.png"));

		//book
		spriteMap.put("bO0", loadImage("book0.png"));
		spriteMap.put("bO1", loadImage("book1.png"));
		spriteMap.put("bO2", loadImage("book2.png"));
		spriteMap.put("bO3", loadImage("book3.png"));

		//Burger
		spriteMap.put("bu0", loadImage("burger0.png"));
		spriteMap.put("bu1", loadImage("burger1.png"));
		spriteMap.put("bu2", loadImage("burger2.png"));
		spriteMap.put("bu3", loadImage("burger3.png"));

		//waterBottle
		spriteMap.put("bu0", loadImage("waterbottle0.png"));
		spriteMap.put("bu1", loadImage("waterbottle1.png"));
		spriteMap.put("bu2", loadImage("waterbottle2.png"));
		spriteMap.put("bu3", loadImage("waterbottle3.png"));

		// tv
		spriteMap.put("tv0", loadImage("tv0.png"));
		spriteMap.put("tv1", loadImage("tv1.png"));
		spriteMap.put("tv2", loadImage("tv2.png"));
		spriteMap.put("tv3", loadImage("tv3.png"));

		// couch
		spriteMap.put("co0", loadImage("couch0.png"));
		spriteMap.put("co1", loadImage("couch1.png"));
		spriteMap.put("co2", loadImage("couch2.png"));
		spriteMap.put("co3", loadImage("couch3.png"));

		// paintings
		spriteMap.put("1P0", loadImage("paintingOne0.png"));
		spriteMap.put("1P1", loadImage("paintingOne1.png"));
		spriteMap.put("1P2", loadImage("paintingOne2.png"));
		spriteMap.put("1P3", loadImage("paintingOne3.png"));
		spriteMap.put("2P0", loadImage("paintingTwo0.png"));
		spriteMap.put("2P1", loadImage("paintingTwo1.png"));
		spriteMap.put("2P2", loadImage("paintingTwo2.png"));
		spriteMap.put("2P3", loadImage("paintingTwo3.png"));
		spriteMap.put("3P0", loadImage("paintingThree0.png"));
		spriteMap.put("3P1", loadImage("paintingThree1.png"));
		spriteMap.put("3P2", loadImage("paintingThree2.png"));
		spriteMap.put("3P3", loadImage("paintingThree3.png"));

		// storageRoom items
		spriteMap.put("ba0", loadImage("barrel0.png"));
		spriteMap.put("ba1", loadImage("barrel1.png"));
		spriteMap.put("ba2", loadImage("barrel2.png"));
		spriteMap.put("ba3", loadImage("barrel3.png"));
		spriteMap.put("la0", loadImage("lamp0.png"));
		spriteMap.put("la1", loadImage("lamp1.png"));
		spriteMap.put("la2", loadImage("lamp2.png"));
		spriteMap.put("la3", loadImage("lamp3.png"));
		spriteMap.put("La0", loadImage("Laptop0.png"));
		spriteMap.put("La1", loadImage("Laptop1.png"));
		spriteMap.put("La2", loadImage("Laptop2.png"));
		spriteMap.put("La3", loadImage("Laptop3.png"));
		spriteMap.put("pe0", loadImage("potEmpty0.png"));
		spriteMap.put("pe1", loadImage("potEmpty1.png"));
		spriteMap.put("pe2", loadImage("potEmpty2.png"));
		spriteMap.put("pe3", loadImage("potEmpty3.png"));
		spriteMap.put("pw0", loadImage("potWater0.png"));
		spriteMap.put("pw1", loadImage("potWater1.png"));
		spriteMap.put("pw2", loadImage("potWater2.png"));
		spriteMap.put("pw3", loadImage("plantPot3.png"));
		spriteMap.put("pp0", loadImage("plantPot0.png"));
		spriteMap.put("pp1", loadImage("plantPot1.png"));
		spriteMap.put("pp2", loadImage("plantPot2.png"));
		spriteMap.put("pp3", loadImage("plantPot3.png"));

		// trees
		spriteMap.put("v0", loadImage("treeOne0.png"));
		spriteMap.put("v1", loadImage("treeOne1.png"));
		spriteMap.put("v2", loadImage("treeOne2.png"));
		spriteMap.put("v3", loadImage("treeOne3.png"));
		spriteMap.put("e0", loadImage("treeTwo0.png"));
		spriteMap.put("e1", loadImage("treeTwo1.png"));
		spriteMap.put("e2", loadImage("treeTwo2.png"));
		spriteMap.put("e3", loadImage("treeTwo3.png"));
		spriteMap.put("l0", loadImage("treeThree0.png"));
		spriteMap.put("l1", loadImage("treeThree1.png"));
		spriteMap.put("l2", loadImage("treeThree2.png"));
		spriteMap.put("l3", loadImage("treeThree3.png"));

		// table - big
		spriteMap.put("T0", loadImage("tableRound0.png"));
		spriteMap.put("T1", loadImage("tableRound1.png"));
		spriteMap.put("T2", loadImage("tableRound2.png"));
		spriteMap.put("T3", loadImage("tableRound3.png"));

		// table - small
		spriteMap.put("t0", loadImage("tableSmall0.png"));
		spriteMap.put("t1", loadImage("tableSmall1.png"));
		spriteMap.put("t2", loadImage("tableSmall2.png"));
		spriteMap.put("t3", loadImage("tableSmall3.png"));

		// table - small
		spriteMap.put("tL0", loadImage("tableLong0.png"));
		spriteMap.put("tL1", loadImage("tableLong1.png"));
		spriteMap.put("tL2", loadImage("tableLong2.png"));
		spriteMap.put("tL3", loadImage("tableLong3.png"));

		// sign
		spriteMap.put("s0", loadImage("sign0.png"));
		spriteMap.put("s1", loadImage("sign1.png"));
		spriteMap.put("s2", loadImage("sign2.png"));
		spriteMap.put("s3", loadImage("sign3.png"));

		// Building
		spriteMap.put("B0", loadImage("building0.png"));
		spriteMap.put("B1", loadImage("building1.png"));
		spriteMap.put("B2", loadImage("building2.png"));
		spriteMap.put("B3", loadImage("building3.png"));

		// Box
		spriteMap.put("b0", loadImage("box0.png"));
		spriteMap.put("b1", loadImage("box1.png"));
		spriteMap.put("b2", loadImage("box2.png"));
		spriteMap.put("b3", loadImage("box3.png"));

		// Building walls
		spriteMap.put("Q0", loadImage("buildingCorner0.png"));
		spriteMap.put("Q1", loadImage("buildingCorner1.png"));
		spriteMap.put("Q2", loadImage("buildingCorner2.png"));
		spriteMap.put("Q3", loadImage("buildingCorner3.png"));
		spriteMap.put("R0", loadImage("roof0.png"));
		spriteMap.put("R1", loadImage("roof1.png"));
		spriteMap.put("R2", loadImage("roof2.png"));
		spriteMap.put("R3", loadImage("roof3.png"));

		// Light
		spriteMap.put("li0", loadImage("light0.png"));
		spriteMap.put("li1", loadImage("light1.png"));
		spriteMap.put("li2", loadImage("light2.png"));
		spriteMap.put("li3", loadImage("light3.png"));

		// Pondy
		spriteMap.put("P00", loadImage("pondy00.png"));
		spriteMap.put("P01", loadImage("pondy01.png"));
		spriteMap.put("P02", loadImage("pondy02.png"));
		spriteMap.put("P03", loadImage("pondy03.png"));

		spriteMap.put("P10", loadImage("pondy10.png"));
		spriteMap.put("P11", loadImage("pondy11.png"));
		spriteMap.put("P12", loadImage("pondy12.png"));
		spriteMap.put("P13", loadImage("pondy13.png"));

		spriteMap.put("P20", loadImage("pondy20.png"));
		spriteMap.put("P21", loadImage("pondy21.png"));
		spriteMap.put("P22", loadImage("pondy22.png"));
		spriteMap.put("P23", loadImage("pondy23.png"));

		spriteMap.put("P30", loadImage("pondy30.png"));
		spriteMap.put("P31", loadImage("pondy31.png"));
		spriteMap.put("P32", loadImage("pondy32.png"));
		spriteMap.put("P33", loadImage("pondy33.png"));

		// player 0

		spriteMap.put("0p00", loadImage("0Player00.png"));
		spriteMap.put("0p01", loadImage("0Player01.png"));
		spriteMap.put("0p02", loadImage("0Player02.png"));
		spriteMap.put("0p03", loadImage("0Player03.png"));

		spriteMap.put("0p10", loadImage("0Player10.png"));
		spriteMap.put("0p11", loadImage("0Player11.png"));
		spriteMap.put("0p12", loadImage("0Player12.png"));
		spriteMap.put("0p13", loadImage("0Player13.png"));

		spriteMap.put("0p20", loadImage("0Player20.png"));
		spriteMap.put("0p21", loadImage("0Player21.png"));
		spriteMap.put("0p22", loadImage("0Player22.png"));
		spriteMap.put("0p23", loadImage("0Player23.png"));

		spriteMap.put("0p30", loadImage("0Player30.png"));
		spriteMap.put("0p31", loadImage("0Player31.png"));
		spriteMap.put("0p32", loadImage("0Player32.png"));
		spriteMap.put("0p33", loadImage("0Player33.png"));

		// player
		spriteMap.put("1p00", loadImage("1Player00.png"));
		spriteMap.put("1p01", loadImage("1Player01.png"));
		spriteMap.put("1p02", loadImage("1Player02.png"));
		spriteMap.put("1p03", loadImage("1Player03.png"));

		spriteMap.put("1p10", loadImage("1Player10.png"));
		spriteMap.put("1p11", loadImage("1Player11.png"));
		spriteMap.put("1p12", loadImage("1Player12.png"));
		spriteMap.put("1p13", loadImage("1Player13.png"));

		spriteMap.put("1p20", loadImage("1Player20.png"));
		spriteMap.put("1p21", loadImage("1Player21.png"));
		spriteMap.put("1p22", loadImage("1Player22.png"));
		spriteMap.put("1p23", loadImage("1Player23.png"));

		spriteMap.put("1p30", loadImage("1Player30.png"));
		spriteMap.put("1p31", loadImage("1Player31.png"));
		spriteMap.put("1p32", loadImage("1Player32.png"));
		spriteMap.put("1p33", loadImage("1Player33.png"));

		// player 2
		spriteMap.put("2p00", loadImage("2Player00.png"));
		spriteMap.put("2p01", loadImage("2Player01.png"));
		spriteMap.put("2p02", loadImage("2Player02.png"));
		spriteMap.put("2p03", loadImage("2Player03.png"));

		spriteMap.put("2p10", loadImage("2Player10.png"));
		spriteMap.put("2p11", loadImage("2Player11.png"));
		spriteMap.put("2p12", loadImage("2Player12.png"));
		spriteMap.put("2p13", loadImage("2Player13.png"));

		spriteMap.put("2p20", loadImage("2Player20.png"));
		spriteMap.put("2p21", loadImage("2Player21.png"));
		spriteMap.put("2p22", loadImage("2Player22.png"));
		spriteMap.put("2p23", loadImage("2Player23.png"));

		spriteMap.put("2p30", loadImage("2Player30.png"));
		spriteMap.put("2p31", loadImage("2Player31.png"));
		spriteMap.put("2p32", loadImage("2Player32.png"));
		spriteMap.put("2p33", loadImage("2Player33.png"));
		// player 2
		spriteMap.put("3p00", loadImage("3Player00.png"));
		spriteMap.put("3p01", loadImage("3Player01.png"));
		spriteMap.put("3p02", loadImage("3Player02.png"));
		spriteMap.put("3p03", loadImage("3Player03.png"));

		spriteMap.put("3p10", loadImage("3Player10.png"));
		spriteMap.put("3p11", loadImage("3Player11.png"));
		spriteMap.put("3p12", loadImage("3Player12.png"));
		spriteMap.put("3p13", loadImage("3Player13.png"));

		spriteMap.put("3p20", loadImage("3Player20.png"));
		spriteMap.put("3p21", loadImage("3Player21.png"));
		spriteMap.put("3p22", loadImage("3Player22.png"));
		spriteMap.put("3p23", loadImage("3Player23.png"));

		spriteMap.put("3p30", loadImage("3Player30.png"));
		spriteMap.put("3p31", loadImage("3Player31.png"));
		spriteMap.put("3p32", loadImage("3Player32.png"));
		spriteMap.put("3p33", loadImage("3Player33.png"));

		/*       Overlay Window      */

		spriteMap.put("H0", loadImage("header.png"));
		spriteMap.put("P0", loadImage("footer.png"));

		/* Rain */

		spriteMap.put("Rain0", loadImage("rain0.png"));
		spriteMap.put("Rain1", loadImage("rain1.png"));
		spriteMap.put("Rain2", loadImage("rain2.png"));
		spriteMap.put("Rain3", loadImage("rain3.png"));
		spriteMap.put("Rain4", loadImage("rain4.png"));
		spriteMap.put("Rain5", loadImage("rain5.png"));
		spriteMap.put("Rain6", loadImage("rain6.png"));
		spriteMap.put("Rain7", loadImage("rain7.png"));

		/* Night overlay */
		spriteMap.put("N0", loadImage("night0.png"));
		spriteMap.put("N1", loadImage("night1.png"));
		spriteMap.put("N2", loadImage("night2.png"));
		spriteMap.put("N3", loadImage("night3.png"));

	}

	/**
	 * Load an image from the file system, using a given filename.
	 *
	 * @param filename
	 * @return
	 */
	public static Image loadImage(String filename) {
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
