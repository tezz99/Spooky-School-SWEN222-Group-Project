package testing;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import game.Area;
import game.ContainerGO;
import game.InventoryGO;
import game.Player;
import game.Position;
import game.SpookySchool;

/**
 * JUnit tests for the game logic package.
 * @author Rongji Wang
 */
public class GameLogicTests {

	//Add a player with a unique name to the game.
	@Test
	public void addPlayerTest() {
		SpookySchool game = new SpookySchool();

		//Test that player is added to the bundle and the player list.
		assertTrue(game.addPlayer("player"));
		assertTrue(game.getPlayer("player") != null);
		assertTrue(game.getBundle("player", false) != null);

		//Test that player has a position.
		Player p = game.getPlayer("player");
		assertTrue(p.getPosition() != null);

		//Test that player is on the correct tile in the area.
		Area a = p.getCurrentArea();
		assertTrue(a.getTile(p.getCurrentPosition()).getOccupant().equals(p));

		//Test that the player is currently the owner of the spawn area.
		assertTrue(a.getOwner().getPlayerName().equals("player"));

	}

	//Add player with name that already exists in the game.
	@Test
	public void addPlayerTest2() {
		SpookySchool game = new SpookySchool();
		assertTrue(game.addPlayer("player"));
		assertFalse(game.addPlayer("player")); //Should not able able to add another player with same name.
	}

	//Remove a player that exists in the game.
	@Test
	public void removePlayerTest() {
		SpookySchool game = new SpookySchool();
		assertTrue(game.addPlayer("player"));

		//Information required to test player removal.
		Player p = game.getPlayer("player");
		Area a = p.getCurrentArea(); //Also the spawn area in this test.
		Position pos = p.getCurrentPosition();

		assertTrue(a.getTile(pos).getOccupant() != null);

		game.removePlayer("player");//Remove the player from the game.

		//Test player has has been removed from their tile.
		assertTrue(a.getTile(pos).getOccupant() == null);

		//Test player no longer has a bundle.
		assertTrue(game.getBundle("player", false) == null);

		//Test the spawn area no longer has an owner.

	}

	//Remove a player that does not exist in the game.
	@Test
	public void removePlayerTest2() {
		SpookySchool game = new SpookySchool();
		game.removePlayer("xyz"); //Should do nothing.
		assertTrue(game.getPlayer("xyz") == null);
	}

	//Add item to player inventory
	@Test
	public void playerInventoryAddingTest() {
		SpookySchool game = new SpookySchool();
		game.addPlayer("abc");
		Player p = game.getPlayer("abc");
		//Test Inventory should be empty
		assertTrue(p.getInventory().isEmpty() == true);
		// Adding one item to Inventory
		p.addToInventory(new InventoryGO("key", "id1", "token1", 1, "area1", new Position(1, 1), "Master Key"));
		// Test Inventory should not be empty
		assertFalse(p.getInventory().isEmpty());
		// Adding second item for size checking
		p.addToInventory(new InventoryGO("Book", "id2", "token2", 2, "area2", new Position(2, 2), "JAVA BOOK"));
		// Test inventory size
		assertTrue(p.getInventory().size() == 2);
		// Test item description
		assertTrue(p.getInventory().get(0).getDescription().equals("Master Key"));
		// Test for duplication
		assertFalse(p.getInventory().get(1).getDescription().equals("Master Key"));

	}

	//Delete item from player inventory
	@Test
	public void playerInventoryDeletingTest() {
		SpookySchool game = new SpookySchool();
		game.addPlayer("abc");
		Player p = game.getPlayer("abc");
		// Adding one item to Inventory
		p.addToInventory(new InventoryGO("key", "id1", "token1", 1, "area1", new Position(1, 1), "Master Key"));
		assertTrue(p.getInventory().size() == 1);
		// drop the same item
		game.processDrop("abc", "id1");
		// player inventory should be empty after item drop.
		assertTrue(p.getInventory().isEmpty());
		// Adding two items
		p.addToInventory(new InventoryGO("key", "id1", "token1", 1, "area1", new Position(1, 1), "Master Key"));
		p.addToInventory(new InventoryGO("Book", "id2", "token2", 2, "area2", new Position(2, 2), "JAVA BOOK"));
		assertTrue(p.getInventory().size() == 2);
		// Drop wrong item id
		game.processDrop("abc", "WRONG ID");
		assertTrue(p.getInventory().size() == 2);

	}

	// Player direction test
	@Test
	public void playerDirectionTest() {
		SpookySchool game = new SpookySchool();
		game.addPlayer("abc");
		Player p = game.getPlayer("abc");
		// Player turn to the current direction
		assertTrue(game.movePlayer(p, p.getDirection()));
		// Player turn to a different direction
		if (!p.getDirection().equalsIgnoreCase("EAST")) {
			assertTrue(game.movePlayer(p, "EAST"));
		} else {
			assertTrue(game.movePlayer(p, "NORTH"));
		}
	}

	// Player bundle
	@Test
	public void playerBundleTest() {
		SpookySchool game = new SpookySchool();
		game.addPlayer("abc");
		Player p = game.getPlayer("abc");
		// player bundle should not be null
		assertTrue(game.getBundle("abc", false) != null);
	}

	// Game chat
	@Test
	public void gameChatLogTest() {
		SpookySchool game = new SpookySchool();
		game.addPlayer("abc");
		Player p = game.getPlayer("abc");
		// distribute message from game side
		game.addChatLogItemToAllBundles("Hello");
		// distribute message from player side
		game.getBundle("abc", false).addToChatLog("World");
		// check for player receiving
		assertTrue(game.getBundle("abc", false).getLog().size() == 2);
		assertTrue(game.getBundle("abc", false).getLog().get(0).equals("Hello"));
		assertTrue(game.getBundle("abc", false).getLog().get(1).equals("World"));

	}

	// Game state
	@Test
	public void gameStateTest() {
		SpookySchool game = new SpookySchool();
		// area maps should not be empty
		assertFalse(game.getAreas().isEmpty());
		// Movable objects should not be empty
		assertFalse(game.getMovableObjects().isEmpty());
		// Door objects should not be empty
		assertFalse(game.getDoorObjects().isEmpty());
		// Inventory objects should be empty
		assertFalse(game.getInventoryObjects().isEmpty());
		// No player exists until join
		assertTrue(game.getPlayers().isEmpty());
	}

	// Room search
	@Test
	public void roomSearchTest() {
		SpookySchool game = new SpookySchool();
		game.addPlayer("abc");
		Player p = game.getPlayer("abc");
		// Should never get a empty room with player in it
		for (int i = 0; i < 10; i++) {
			Area a = game.findEmptySpawnRoom();
			assertFalse(p.getCurrentArea().getAreaName().equals(a.getAreaName()));
		}
	}

	// Player pickup item
	@Test
	public void playerPickupActionTest() {
		SpookySchool game = new SpookySchool();
		game.addPlayer("abc");
		Player p = game.getPlayer("abc");
		// Number of inventory items should not change when pick nothing up
		int oldInven = p.getInventory().size();
		game.processAction("abc");
		assertTrue(oldInven == p.getInventory().size());

	}

	//Player drop item
	@Test
	public void playerDropActionTest() {
		SpookySchool game = new SpookySchool();
		game.addPlayer("abc");
		Player p = game.getPlayer("abc");
		// Add items to player inventory
		p.addToInventory(new ContainerGO("ITEM", "1", "TOKEN", 1, "AREA", new Position(1, 1), "DESCRIPTION"));
		assertTrue(p.getInventory().size() == 1);
		// drop item from player inventory
		game.processDrop("abc", "1");
		assertTrue(p.getInventory().size() == 0);
	}

	//Add items to container
	@Test
	public void addToContainerTest() {
		SpookySchool game = new SpookySchool();
		game.addPlayer("abc");
		Player p = game.getPlayer("abc");
		// add a container to player inventory
		ContainerGO container = new ContainerGO("ITEM", "10", "TOKEN", 10, "AREA", new Position(10, 10), "DESCRIPTION");
		p.addToInventory(container);
		game.getInventoryObjects().put("10", container);
		// add a item to the same inventory

		InventoryGO key = new InventoryGO("ITEM", "1", "TOKEN", 1, "AREA", new Position(1, 1), "DESCRIPTION");
		p.addToInventory(key);
		game.getInventoryObjects().put("1", key);
		// add the item to container
		assertTrue(container.getSizeRemaining() == 10);
		// current inventory size
		assertTrue(p.getInventory().size() == 2);
		game.addToContainer("abc", "10", "1");
		// inventory should have 1 item less
		assertTrue(p.getInventory().size() == 1);
		// container should have 1 less remaining items
		assertTrue(container.getSizeRemaining() == 9);

	}

	// Unpack container
	@Test
	public void unpackContainerTest() {
		SpookySchool game = new SpookySchool();
		game.addPlayer("abc");
		Player p = game.getPlayer("abc");
		// add container into player inventory
		ContainerGO container = new ContainerGO("ITEM", "10", "TOKEN", 10, "AREA", new Position(10, 10), "DESCRIPTION");
		p.addToInventory(container);
		game.getInventoryObjects().put("10", container);
		// add items into container
		InventoryGO key1 = new InventoryGO("ITEM1", "1", "TOKEN1", 1, "AREA", new Position(1, 1), "DESCRIPTION1");
		InventoryGO key2 = new InventoryGO("ITEM2", "2", "TOKEN2", 1, "AREA", new Position(2, 2), "DESCRIPTION2");
		container.addToContainer(key1);
		container.addToContainer(key2);
		//now unpack container in player inventory
		game.unpackContainer("abc", "10");
		System.out.println(p.getInventory().size());
		assertTrue(p.getInventory().size() == 3);

	}

	// Pass item to player
	@Test
	public void passItemToPlayer() {
		SpookySchool game = new SpookySchool();
		game.addPlayer("aaa");
		game.addPlayer("bbb");
		Player aaa = game.getPlayer("aaa");
		Player bbb = game.getPlayer("bbb");
		// add a item to player aaa
		InventoryGO key1 = new InventoryGO("ITEM1", "1", "TOKEN1", 1, "AREA", new Position(1, 1), "DESCRIPTION1");
		game.getInventoryObjects().put("1", key1);
		bbb.addToInventory(key1);
		assertTrue(bbb.getInventory().size() == 1);

		//set both player in the same room
		bbb.setCurrentArea(aaa.getCurrentArea());
		System.out.println(aaa.getCurrentArea() + " " + bbb.getCurrentArea());

		// set both player position next to each other
		aaa.setCurrentPosition(new Position(6, 4));
		aaa.setDirection("NORTH");
		bbb.setCurrentPosition(new Position(5, 7));
		bbb.setDirection("SOUTH");

		// transfer item
		game.passItem("bbb", "1");
		assertTrue(bbb.getInventory().isEmpty());
		assertTrue(aaa.getInventory().size() == 1);

	}

	// Move player
	@Test
	public void movePlayerTest() {
		SpookySchool game = new SpookySchool();
		game.addPlayer("aaa");
		Player aaa = game.getPlayer("aaa");
		// set player to certain position
		aaa.setCurrentPosition(new Position(5, 5));
		// set player direction
		aaa.setDirection("NORTH");
		// move north
		game.movePlayer(aaa, "NORTH");
		// should move close to top of the frame y-1
		assertTrue(aaa.getPosition().getPosX() == 5 && aaa.getPosition().getPosY() == 4);

	}

}
