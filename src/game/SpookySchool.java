package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import parser.Parsernew;

/**
 * This class contains all of the logic Spooky School game. This class controls game state and provides various helper methods
 * for the server.
 * @author Pritesh R. Patel
 *
 */
public class SpookySchool {

	private final Position defaultSpawnPosition = new Position(5, 8); //Default position that a player spawns in, in a spawn room.
	private final int maxPlayers = 4;
	private List<Player> players = new ArrayList<Player>(); //List of players in the game.
	private String[] defaultPlayerTokens = { "0p20", "1p20", "2p20", "3p20" };
	private List<String> usedTokens = new ArrayList<String>();

	//Should make xml implementation easier?!
	private String areasFileLoc = "src/areas/areas.txt";
	private String doorsFileLoc = "src/areas/game_objects/doors.txt";
	private String movableObjectsFileLoc = "src/areas/game_objects/movable_objects.txt";
	private String nonHumanPlayersFileLoc = "src/areas/game_objects/non_human_player_objects.txt";
	private String inventoryObjFileLoc = "src/areas/game_objects/inventory_objects.txt";
	private String fixedContainersFileLoc = "src/areas/game_objects/fixed_container_objects.txt";
	private String fillContainersFileLoc = "src/areas/game_objects/fill_containers.txt";

	//Default Load files - these never change.
	private Map<String, Area> areas = new HashMap<String, Area>();
	private List<NonHumanPlayer> nonHumanPlayers = new ArrayList<NonHumanPlayer>();

	//Mainly for XML
	private Parsernew parser;
	private List<MovableGO> movableObjects = new ArrayList<MovableGO>();
	private List<DoorGO> doorObjects = new ArrayList<DoorGO>();
	private Map<String, InventoryGO> inventoryObjects = new HashMap<String, InventoryGO>();
	private Map<String, FixedContainerGO> fixedContainerObjects = new HashMap<String, FixedContainerGO>();

	//For networking
	private Map<String, Bundle> playerBundles = new HashMap<String, Bundle>();

	public SpookySchool() {
		this.loadAreas(); //Load maps
		this.setDoors(); //Sets up doors on the areas.
		this.loadRemainingGameObjects(); //Load the remaining game objects.
		this.fillContainers(); //Fill the containers in the rooms.

		this.parser = new Parsernew();

		//Create a new clock thread and start it. Used for NPCs.
		Thread clockThread = new ClockThread(this);
		clockThread.start();

		System.out.println("Game Loaded.");
	}

	/**
	 * Load all of the "areas" of the game into the list of areas.
	 */
	public void loadAreas() {
		Scanner scan;
		try {
			scan = new Scanner(new File(areasFileLoc));
			while (scan.hasNextLine()) {
				String areaName = scan.next();
				String fileName = scan.next();
				this.areas.put(areaName, new Area(areaName, fileName));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets up the doors in the areas.
	 */
	public void setDoors() {
		Scanner scan;

		try {
			scan = new Scanner(new File(doorsFileLoc));
			while (scan.hasNextLine()) {

				//Scan Door Specific information
				String doorID = scan.next();
				boolean open = scan.next().equals("open");
				boolean locked = scan.next().equals("locked");
				String keyID = scan.next();
				keyID = keyID.equals("null") ? null : keyID;

				//Scan information about rooms on either side.
				String sideA = scan.next();
				String tokenA = scan.next();
				Position sideADoorPos = new Position(scan.nextInt(), scan.nextInt());
				Position sideAEntryPos = new Position(scan.nextInt(), scan.nextInt());

				String sideB = scan.next();
				String tokenB = scan.next();
				Position sideBDoorPos = new Position(scan.nextInt(), scan.nextInt());
				Position sideBEntryPos = new Position(scan.nextInt(), scan.nextInt());

				//Create the door object.
				DoorGO door = new DoorGO(doorID, open, locked, keyID, sideA, tokenA, sideADoorPos, sideAEntryPos, sideB,
						tokenB, sideBDoorPos, sideBEntryPos);

				//Get the area objects of both sides.
				Area areaA = this.areas.get(sideA);
				Area areaB = this.areas.get(sideB);

				//Add the door objects onto the appropriate tiles in their area...
				areaA.getTile(sideADoorPos).setOccupant(door);
				areaB.getTile(sideBDoorPos).setOccupant(door);

				//Add door to doors list
				this.doorObjects.add(door);

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load all game objects that are not fixed and not door objects.
	 */
	public void loadRemainingGameObjects() {

		Scanner scan;
		try {

			//Scan and load Movable objects
			scan = new Scanner(new File(movableObjectsFileLoc));
			while (scan.hasNextLine()) {

				//Scan movable object information.
				String id = scan.next();
				String token = scan.next();
				String areaName = scan.next();
				Position objPosition = new Position(scan.nextInt(), scan.nextInt());

				//Create the movable object using the scanned information.
				MovableGO movableGO = new MovableGO(id, token, areaName, objPosition);

				Area area = this.areas.get(areaName);
				Tile tile = area.getTile(objPosition); //Get the tile of

				tile.setOccupant(movableGO);

				this.movableObjects.add(movableGO);
			}

			//Scan the non human player objects.
			scan = new Scanner(new File(nonHumanPlayersFileLoc));
			while (scan.hasNextLine()) {

				Scanner lineScanner = new Scanner(scan.nextLine());

				//Scan information about the NPC.
				String id = lineScanner.next();
				String token = lineScanner.next();
				String areaName = lineScanner.next();
				Area area = this.areas.get(areaName);
				Position startingPos = new Position(lineScanner.nextInt(), lineScanner.nextInt());

				//List of directions the npc will move.
				List<String> directions = new ArrayList<String>();
				while (lineScanner.hasNext()) {
					directions.add(lineScanner.next());
				}

				//Create NPC
				NonHumanPlayer npc = new NonHumanPlayer(id, token, area, startingPos, directions);

				//Move NPC.
				area.getTile(startingPos).setOccupant(npc);

				this.nonHumanPlayers.add(npc);
			}

			//Scan all of the inventory objects on the floors.
			scan = new Scanner(new File(inventoryObjFileLoc));
			while (scan.hasNextLine()) {

				Scanner lineScanner = new Scanner(scan.nextLine());

				String type = lineScanner.next(); //Type of inventory object.

				//Scan information about the inventory object
				String name = lineScanner.next();
				String id = lineScanner.next();
				String token = lineScanner.next();
				int size = lineScanner.nextInt();
				String areaName = lineScanner.next();

				Position pos = null;

				//Scan the items position if the area isnt null. If it is, skip the next two tokens.
				if (areaName.equals("null")) {
					lineScanner.next();
					lineScanner.next();
				} else {
					pos = new Position(lineScanner.nextInt(), lineScanner.nextInt());
				}

				String description = lineScanner.nextLine();

				InventoryGO item = null;

				if (type.equals("NORMAL")) {
					item = new InventoryGO(name, id, token, size, areaName, pos, description); //Create the inventory object
				} else if (type.equals("CONTAINER")) {
					item = new ContainerGO(name, id, token, size, areaName, pos, description);
				} else {
					throw new Error("Invalid Inventory object type!");
				}

				//Place the item on the tile in the given area if applicable.
				if (!areaName.equals("null")) {
					Area area = this.areas.get(areaName);
					area.getTile(pos).setOccupant(item);
				}

				this.inventoryObjects.put(id, item); //Add the item to the list of inventory objects.
			}

			//Scan all of the fixed container objects on the floors.
			scan = new Scanner(new File(this.fixedContainersFileLoc));
			while (scan.hasNextLine()) {

				Scanner lineScanner = new Scanner(scan.nextLine());

				//Scan information about the fixed container.
				String name = lineScanner.next();
				String areaName = lineScanner.next();
				String id = lineScanner.next();
				String token = lineScanner.next();
				boolean open = lineScanner.next().equals("open");
				boolean locked = lineScanner.next().equals("locked");
				String keyID = lineScanner.next();
				keyID = keyID.equals("null") ? null : keyID;
				int size = lineScanner.nextInt();
				Position pos = new Position(lineScanner.nextInt(), lineScanner.nextInt());

				//Create the container object.
				FixedContainerGO container = new FixedContainerGO(name, areaName, id, token, open, locked, keyID, size,
						pos);

				//Place the container object
				Area area = this.areas.get(areaName);
				area.getTile(pos).setOccupant(container);

				//Set up the rest of the marker tiles that make up this fixed container game object.
				while (lineScanner.hasNextInt()) {
					Position markerPos = new Position(lineScanner.nextInt(), lineScanner.nextInt());
					GameObject markerObj = new MarkerGO(container, markerPos); //Link marker to original game object.
					area.getTile(markerPos).setOccupant(markerObj);
				}

				this.fixedContainerObjects.put(id, container);
			}

			scan.close();
		} catch (FileNotFoundException e) {
			throw new Error(e.getMessage());
		}
	}

	/**
	 * Fill the containers in the game where required.
	 */
	public void fillContainers() {
		Scanner scan = null;

		try {
			scan = new Scanner(new File(fillContainersFileLoc));

			while (scan.hasNextLine()) {
				Scanner lineScanner = new Scanner(scan.nextLine());

				String itemID = lineScanner.next();
				String type = lineScanner.next();
				String containerID = lineScanner.next();

				InventoryGO item = this.inventoryObjects.get(itemID); //Item to be put into the given container.

				if (item == null) {
					lineScanner.close();
					scan.close();
					throw new Error(
							"Item to be put into the container is null. Note: You can only put InventroGOs into containers.");
				}

				//Add the item into the appropriate container.
				if (type.equals("CONTAINER")) {
					if (!((ContainerGO) this.inventoryObjects.get(containerID)).addToContainer(item)) {
						throw new Error("Item is too big!");
					}

				} else if (type.equals("FIXED_CONTAINER")) {
					if (!this.fixedContainerObjects.get(containerID).addToContainer(item)) {
						throw new Error("Item is too big!");
					}

				} else {
					lineScanner.close();
					scan.close();
					throw new Error("INVALID CONTAINER TYPE!");
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			scan.close();
		}

	}

	/**
	* Adds player to the game if the game is not full and player with this name does not already exist.
	* @param name of the player being added to the game.
	* @return true if player successfully added, otherwise false.
	*/
	public synchronized boolean addPlayer(String name) {

		if (this.players.size() < this.maxPlayers && this.getPlayer(name) == null) {
			Area spawnRoom = this.findEmptySpawnRoom();
			Player newPlayer = new Player(name, spawnRoom.getAreaName(), spawnRoom, this.defaultSpawnPosition);
			spawnRoom.setOwner(newPlayer); //Set player as the owner of the spawn room.

			//Set the player as the occupant of the tile.
			FloorTile spawnTile = (FloorTile) spawnRoom.getTile(this.defaultSpawnPosition);

			assert spawnTile != null;

			spawnTile.setOccupant(newPlayer);

			outer: for (String token : this.defaultPlayerTokens) {
				if (!this.usedTokens.contains(token)) {
					newPlayer.setToken(token);
					this.usedTokens.add(token);
					break outer;
				}
			}

			this.players.add(newPlayer); //Add the player to the list of players in the game.
			this.addChatLogItemToAllBundles(name + " entered the game.");

			//Set up the bundle for the new player.
			Bundle bundle = new Bundle(name);
			bundle.setPlayerObj(newPlayer);

			this.playerBundles.put(name, bundle);

			return true;
		}

		return false;
	}

	/**
	 * Remove player from the game
	 * @param name of the player to remove from the game.
	 */
	public synchronized void removePlayer(String name) {

		//Player doesnt exist in game, do nothing.
		if (this.getPlayer(name) == null) {
			return;
		}

		//Remove player as their spawn area's owner
		if (this.getPlayer(name).getCurrentArea().getAreaName().contains("Spawn")) {
			this.getPlayer(name).getCurrentArea().setOwner(null); //Set the players spawn room owner as null
		}

		this.getPlayer(name).getCurrentArea().getTile(this.getPlayer(name).getCurrentPosition()).removeOccupant(); //Remove player from the tile

		this.usedTokens.remove(this.getPlayer(name).getToken());
		this.players.remove(this.getPlayer(name)); //Remove the player from this game by removing them from players list.
		this.playerBundles.remove(name); //Remove this player's bundle.

		//Add player disconnection information to the chatlog
		this.addChatLogItemToAllBundles(name + " has left the game.");
	}

	/**
	 * Finds and returns a spawn area that is currently not owned by a player.
	 * @return a spawn area that is currently not owned by a player.
	 */
	public Area findEmptySpawnRoom() {
		//Finds an unoccupied/un-owned spawn area and returns it.
		for (Entry<String, Area> entry : this.areas.entrySet()) {
			if (entry.getKey().contains("Spawn") && (!entry.getValue().hasOwner())) {
				return entry.getValue();
			}
		}

		throw new Error("Error: Could not find an empty spawn room. This should not be possible.");
	}

	/**
	 * This is called when a player presses the action button. This method makes any changes that are required to the game state and
	 * adds changes to game bundles if and when required. The action is "done" on the tile that the player is facing.
	 * @param playerName name of the player that pressed the action button.
	 */
	public synchronized void processAction(String playerName) {

		Player player = this.getPlayer(playerName);

		Tile potentialTile = this.getPotentialTile(player.getCurrentArea(), player, player.getDirection(), 1);

		//Not a valid potential tile so return.
		if (potentialTile == null) {
			return;
		}

		GameObject gameObj = potentialTile.getOccupant();

		//If there is no game object on the potential tile, then return since there is no possible action.
		if (gameObj == null) {
			return;
		}

		//If the object in front is an inventory item, pick it up.
		if (gameObj instanceof InventoryGO) {

			InventoryGO item = (InventoryGO) gameObj;

			//Remove the item from the area.
			Area area = this.areas.get(item.getAreaName());
			area.getTile(item.getPosition()).removeOccupant();

			item.setAreaName(null);
			item.setCurrentPosition(null);

			this.getBundle(playerName, false).setMessage("You picked up a " + item.getName() + ".");
			player.addToInventory(item);

			return;
		}

		//If its a marker tile to a fixedContainer, set the game object to fixed container.. As if YOU are
		if (gameObj instanceof MarkerGO && ((MarkerGO) gameObj).getBaseGO() instanceof FixedContainerGO) {
			gameObj = ((MarkerGO) gameObj).getBaseGO();
		}

		//If its a fixed container.
		if (gameObj instanceof FixedContainerGO) {
			FixedContainerGO fixedContainer = (FixedContainerGO) gameObj;

			if (fixedContainer.isLocked()) {
				//Attempt to unlock it.
				for (InventoryGO item : player.getInventory()) {
					if (fixedContainer.getKeyID().equals(item.getId())) {
						fixedContainer.setLocked(false); //Unlock the door.
						this.getBundle(playerName, false).setMessage(
								"You unlocked the " + fixedContainer.getName() + " using the key in your inventory");
						return;
					}
				}

				this.getBundle(playerName, false).setMessage(
						"The " + fixedContainer.getName() + " is locked. You don't seem to have the key to open it.");

				return; //Couldn't unlock chest.
			}

			//If the container is open, empty what is inside, and if its empty, close it.
			//If container is closed, open it.
			if (fixedContainer.isOpen()) {
				if (fixedContainer.isEmpty()) {
					fixedContainer.setOpen(false);
				} else {
					//Add all items from the container to the player's inventory.
					for (InventoryGO item : fixedContainer.getAllItems()) {
						player.addToInventory(item);
					}
					fixedContainer.clearContainer(); //Clear the container now that the player has its contents.
					this.getBundle(playerName, false)
							.setMessage("Items found in the container have been added to your inventory.");
				}
			} else {
				fixedContainer.setOpen(true);
			}

			return; //finished interacting with the container.
		}

		if (!(gameObj instanceof DoorGO)) {
			String objDescription = gameObj.getDescription();

			//If it is a spawn room sign then display the owner name.
			if (objDescription.contains("Spawn_")) {
				objDescription = objDescription.replaceAll("\\s", "");

				if (this.areas.get(objDescription).getOwner() == null) {
					objDescription = "No Occupant";
				} else {
					objDescription = this.areas.get(objDescription).getOwner().getId() + " 's Room";
				}
			}

			if (objDescription.isEmpty()) {
				objDescription = "Nothing to see here.";
			}

			this.getBundle(playerName, false).setMessage(objDescription);

			return; //Finished

		}

		//If the game object the action is done on is a door game object, then process the action.
		if (gameObj instanceof DoorGO) {
			DoorGO door = (DoorGO) gameObj;

			if (door.isLocked()) {
				//Attempt to unlock it.
				for (InventoryGO item : player.getInventory()) {
					if (door.getKeyID().equals(item.getId())) {
						door.setLocked(false); //Unlock the door.
						this.getBundle(playerName, false)
								.setMessage("You unlocked the door using the key in your inventory");
						return;
					}
				}

				if (door.getTokenA().contains("w")) {
					this.getBundle(playerName, false).setMessage("The window is locked.");
				} else {
					this.getBundle(playerName, false)
							.setMessage("The door is locked. You dont have the key to open this door.");
				}

				return; //Couldnt unlock door.
			}

			//Open or close the door depending on current door state
			if (door.isOpen()) {
				door.setOpen(false);
			} else {

				/*
				 * FIXME: make doors lockable.
				//Lock the door if player has the key.
				for (InventoryGO item : player.getInventory()) {
					if (door.getKeyID().equals(item.getId())) {
						door.setLocked(true); //lock the door.
						this.getBundle(playerName).setMessage("You locked the door using the key in your inventory.");
						return;
					}
				}
				*/

				//Open the door if player does not have the key.
				door.setOpen(true);
			}

			return; //finished
		}
	}

	/**
	 *
	 * @param playerName name of the player who would like to drop an inventory GO.
	 * @param itemID the id of the item the player wishes to drop.
	 */
	public void processDrop(String playerName, String itemID) {

		Player player = this.getPlayer(playerName);

		for (InventoryGO item : player.getInventory()) {
			if (item.getId().equals(itemID)) {

				Area area = player.getCurrentArea();
				String direction = player.getDirection();

				Tile potentialTile = this.getPotentialTile(area, player, direction, 1); //Get the tile in fron of the player

				if (potentialTile != null && potentialTile instanceof FloorTile && !potentialTile.isOccupied()) {
					item.setAreaName(area.getAreaName());
					item.setCurrentPosition(potentialTile.getPosition());
					potentialTile.setOccupant(item);
					player.getInventory().remove(item);
					this.getBundle(playerName, false).setMessage("You dropped the item.");
					return;

				} else if (potentialTile != null && potentialTile instanceof FloorTile
						&& (potentialTile.getOccupant() instanceof FixedContainerGO
								|| potentialTile.getOccupant() instanceof MarkerGO)) {

					//If potential tile is a marker object of
					if (potentialTile.getOccupant() instanceof MarkerGO
							&& !(((MarkerGO) potentialTile.getOccupant()).getBaseGO() instanceof FixedContainerGO)) {
						this.getBundle(playerName, false).setMessage("You cannot drop the item here.");
						return;
					}

					//Get the FixedContainerGO.
					FixedContainerGO obj = null;
					if (potentialTile.getOccupant() instanceof MarkerGO) {
						obj = (FixedContainerGO) ((MarkerGO) potentialTile.getOccupant()).getBaseGO();
					} else {
						obj = (FixedContainerGO) potentialTile.getOccupant();
					}

					if (!obj.isOpen()) {
						this.getBundle(playerName, false)
								.setMessage("The " + obj.getName() + " must be open to place items inside.");
						return;
					} else if (obj.isLocked()) {
						this.getBundle(playerName, false)
								.setMessage("The " + obj.getName() + " is locked. Cannot place anything inside.");
						return;
					}

					//Place the item into the container.
					if (obj.addToContainer(item)) {
						//obj.addToContainer(item);
						player.removeFromInventory(item);
						this.getBundle(playerName, false)
								.setMessage("You placed the " + item.getName() + " into the " + obj.getName());
					} else {
						this.getBundle(playerName, false)
								.setMessage("There is not enough space in the " + obj.getName());
					}

					return; //Finished.
				}
			}
		}

		this.getBundle(playerName, false).setMessage("The item you tried to drop is no longer in your inventory.");

	}

	/**
	 * Places a given item into a given container if possible.
	 * @param containerID the id of the container you would like to add the item to.
	 * @param itemID the id of the item that is to be added to the container.
	 */
	public void addToContainer(String playerName, String containerID, String itemID) {

		//You cannot put an item inside itself.
		if (containerID.equals(itemID)) {
			return;
		}

		if (!(this.inventoryObjects.get(containerID) instanceof ContainerGO)) {
			this.getBundle(playerName, false).setMessage(this.inventoryObjects.get(containerID).getName()
					+ " is not a container. You cannot place items in it.");
			return;
		}

		ContainerGO container = (ContainerGO) this.inventoryObjects.get(containerID);

		if (container.addToContainer(this.getInventoryObjects().get(itemID))) {
			//Add the item to the container and remove from the player's inventory.
			this.getPlayer(playerName).removeFromInventory(this.getInventoryObjects().get(itemID));
			this.getBundle(playerName, false).setMessage("You packed the "
					+ this.getInventoryObjects().get(itemID).getName() + " to the " + container.getName() + ".");
		} else {
			this.getBundle(playerName, false)
					.setMessage("There is not enough space in the " + container.getName() + ".");
		}
	}

	/**
	 * Unpack the container given the id. Removes the items from the container and places them in the inventory.
	 * @param playerName name of the player who has sent the unpack command.
	 * @param itemID the id of the container inventory object that is to be unpacked.
	 */
	public void unpackContainer(String playerName, String itemID) {

		if (!(this.inventoryObjects.get(itemID) instanceof ContainerGO)) {
			throw new Error("Can only unpack containers.");
		}

		Player player = this.getPlayer(playerName);
		ContainerGO container = (ContainerGO) this.getInventoryObjects().get(itemID);

		if (container.isEmpty()) {
			this.getBundle(playerName, false).setMessage("The " + container.getName() + " is empty.");
		}

		//Add all items in the container to the players's inventory.
		for (InventoryGO item : container.getAllItems()) {
			player.addToInventory(item);
		}

		container.clearContainer(); //Clear the container now that the items are in the player's inventory.
	}

	/**
	 * This method "passes" the given item to the player in front of them.
	 * If there is no player in front of them, a message is displayed and the item stays in the players inventory.
	 * @param playerName
	 * @param itemID id of the item the player wishes to pass.
	 */
	public void passItem(String playerName, String itemID) {

		Player player = this.getPlayer(playerName);
		InventoryGO item = this.inventoryObjects.get(itemID);

		Tile tile = this.getPotentialTile(player.getCurrentArea(), player, player.getDirection(), 1); //The tile in front of the player.

		//Pass the item if there is a player in front.
		if (tile.getOccupant() instanceof Player) {
			Player receiver = (Player) tile.getOccupant();
			receiver.addToInventory(item);
			player.removeFromInventory(item);
			this.getBundle(playerName, false)
					.setMessage("You passed the " + item.getName() + " to " + receiver.getPlayerName());
			this.getBundle(receiver.getPlayerName(), false)
					.setMessage("You received a " + item.getName() + " from " + player.getPlayerName());
		} else {
			this.getBundle(playerName, false).setMessage("The player must be in front of you to pass an item");
		}
	}

	/**
	 * Moves player in a given direction if possible.
	 * @param playerName the name of the player to move.
	 * @param direction the direction the player needs to move into.
	 * @return true if player moves to a new tile or changes direction.. Otherwise false.
	 */
	public synchronized boolean movePlayer(Player player, String direction) {

		//If player is facing a different direction than the direction given, make the player face the given direction.
		if (!player.getDirection().equals(direction)) {
			player.setDirection(direction);
			return true;
		}

		//Not a direction change... so player is moving in the direction he is facing.
		Tile potentialTile = this.getPotentialTile(player.getCurrentArea(), player, player.getDirection(), 1); //Tile where the player can potentially move.

		//Invalid move.
		if (potentialTile == null) {
			return false;
		}

		//If the potential tile is a floor tile and is not currently occupied, then move the player.
		if (potentialTile instanceof FloorTile && (!((FloorTile) potentialTile).isOccupied())) {
			((FloorTile) player.getCurrentArea().getTile(player.getCurrentPosition())).removeOccupant(); //Remove player from old tile
			this.moveGOToTile(player, potentialTile); //Move the player to the new tile.
			return true; //Player movement complete.
		}

		//If the potential tile has a movable object, then attempt to push it.
		if (potentialTile instanceof FloorTile && potentialTile.getOccupant() instanceof MovableGO) {

			MovableGO movableGO = (MovableGO) potentialTile.getOccupant();

			Tile potentialMovableTile = this.getPotentialTile(player.getCurrentArea(), movableGO, player.getDirection(),
					1);

			//If movable go can be pushed, then move the player and the movable object.
			if (potentialMovableTile instanceof FloorTile && (!((FloorTile) potentialMovableTile).isOccupied())) {
				((FloorTile) player.getCurrentArea().getTile(player.getCurrentPosition())).removeOccupant(); //Remove player from old tile
				this.areas.get(movableGO.getAreaName()).getTile(movableGO.getPosition()).removeOccupant(); //Remove movable tile from the old tile.
				this.moveGOToTile(player, potentialTile); //Move the player to the new tile.
				this.moveGOToTile(movableGO, potentialMovableTile); //Move the player to the new tile.
				return true;
			}

			return false; //Movable tile cannot be pushed.
		}

		return processDoorMovement(potentialTile, player); //Return true if there is a door movement, else false.
	}

	/**
	 * Attempt to move the player to next room if they move on to a door.
	 * @param potentialTile the tile that the player has tried to move on to.
	 * @param player the player that has tried to move.
	 * @return true if player moves to a new room successfully and false otherwise.
	 */
	public boolean processDoorMovement(Tile potentialTile, Player player) {

		//If the potential tile is a wall tile and has a door game object on it, attempt to go through.
		if (potentialTile instanceof WallTile && potentialTile.getOccupant() instanceof DoorGO) {

			String playerName = player.getPlayerName();
			DoorGO door = (DoorGO) potentialTile.getOccupant(); //Get the door object on the wall.

			//Names of the areas of both sides of the door.
			String currentSide = player.getCurrentArea().getAreaName();
			String otherSide = door.getOtherSide(currentSide);

			Area otherSideArea = this.areas.get(otherSide); //The area that is on the other side of the door.
			Tile otherSideTile = otherSideArea.getTile(door.getOtherSideEntryPos(currentSide)); //The tile on the other side of the door.

			//If the door is open and the position on the other side is not occupied, then move player.
			if (door.isOpen() && !otherSideTile.isOccupied()) {

				player.getCurrentArea().getTile(player.getCurrentPosition()).removeOccupant(); //Remove player from this tile.
				player.setCurrentArea(this.areas.get(otherSide)); //Set the player's new area.
				this.getBundle(playerName, false).setPlayerObj(player); //Add the player object to the bundle.
				this.moveGOToTile(player, otherSideTile); //Add player to the new tile.

				//Add movement to new room to the log.
				this.addChatLogItemToAllBundles(
						playerName + " entered the following area: " + otherSide.replace('_', ' '));

				return true; //Movement through door successful
			}
		}

		return false; //Movement through door was unsuccessful.
	}

	/**
	 * Move the game object to the given tile. Method moves game object to new tile and removes them from the old tile.
	 * Also sets game objects position to the new position.
	 * @param gameObj the game object that is to be moved.
	 * @param tile that the game object needs to be moved onto.
	 */
	public void moveGOToTile(GameObject gameObj, Tile tile) {
		FloorTile newTile = (FloorTile) tile;
		newTile.setOccupant(gameObj); //Add player to new tile.
		gameObj.setCurrentPosition(newTile.getPosition()); //Set the player's new position.
	}

	/**
	 *
	 * Get the tile that in front of the player given a distance.
	 * @param area the area that the game object is in.
	 * @param gameObj the game object that you want to move.
	 * @param direction the direction you want to move the game object in.
	 * @param distance which tile to get in front of the player.
	 * @return the tile object that is in "distance" tiles in front of the player in the direction they are facing.
	 */
	public Tile getPotentialTile(Area area, GameObject gameObj, String direction, int distance) {

		int posX = -1;
		int posY = -1;

		if (direction.equals("NORTH")) {
			posX = gameObj.getPosition().getPosX();
			posY = gameObj.getPosition().getPosY() - distance;

		} else if (direction.equals("SOUTH")) {

			posX = gameObj.getPosition().getPosX();
			posY = gameObj.getPosition().getPosY() + distance;

		} else if (direction.equals("EAST")) {
			posX = gameObj.getPosition().getPosX() + distance;
			posY = gameObj.getPosition().getPosY();

		} else if (direction.equals("WEST")) {
			posX = gameObj.getPosition().getPosX() - distance;
			posY = gameObj.getPosition().getPosY();
		}

		return area.getTile(new Position(posX, posY));
	}

	/**
	 * Returns the player associated with the given player name.
	 * @param playerName name of the player to check for.
	 * @return the Player object of the given name if one exists, otherwise return null.
	 */
	public Player getPlayer(String playerName) {
		for (int i = 0; i < this.players.size(); i++) {
			if (this.players.get(i).getPlayerName().equals(playerName)) {
				return this.players.get(i);
			}
		}

		return null; //Player with given name not found.
	}

	/**
	 * Returns the bundle of the given player name.
	 * @param playerName of the player we are getting the bundle for.
	 * @return bundle of the playerName given.
	 */
	public Bundle getBundle(String playerName, boolean transmitting) {
		Bundle bundle = this.playerBundles.get(playerName);

		//If the bundle is about to get transmitted, then add all of the game objects in the players current area to the bundle.
		if (transmitting && bundle != null) {
			Area area = this.getPlayer(playerName).getCurrentArea();
			for (int y = 0; y < area.height; y++) {
				for (int x = 0; x < area.width; x++) {
					Tile tile = area.getTile(new Position(x, y));
					if (tile instanceof FloorTile && tile.getOccupant() instanceof Player) {
						bundle.addMapObject(tile.getOccupant());
					}
				}
			}
		}

		return bundle;
	}

	/**
	 *  * Returns the bundle of the given player name.
	 * @param playerName of the player we are getting the bundle for.
	 * @param transmitting if you are getting the bundle to transmit to client.
	 * @return  bundle of the playerName given.
	 */
	public synchronized void addChatLogItemToAllBundles(String addition) {
		for (Bundle b : this.playerBundles.values()) {
			b.addToChatLog(addition);
		}
	}

	/**
	 * FIXME: for saving game to xml
	 */
	public synchronized void saveGame(String playerName) {
		System.out.println("Saving game...");
		this.parser.save(this, playerName);
		this.getBundle(playerName, false).setMessage("Game saved successfully.");
	}

	/**
	 * This method is called periodically by the Clock Thread and is used to move NPC objects.
	 */
	public synchronized void moveNPC() {

		//Move all NPCs in the game towards their next direction (if possible).
		for (NonHumanPlayer npc : this.nonHumanPlayers) {
			if (this.movePlayer(npc, npc.getPotentialDirection())) {
				npc.directionMoved();
			}
		}
	}

	/**
	 * Goes through each NPC in game, and checks if there is a player in front of them. If there is, it kicks them to their room.
	 */
	public synchronized void checkNPCPath() {

		int tilesToCheck = 3; //Number of tiles the npc needs to check in front of them for a player.

		outer: for (NonHumanPlayer npc : this.nonHumanPlayers) {
			//Check one tile at a tile, "tilesToCheck" number of times.
			//If a player is found, teleport them back to their spawn room!
			for (int i = 1; i <= tilesToCheck; i++) {
				Tile tile = this.getPotentialTile(npc.getCurrentArea(), npc, npc.getCurrentDirection(), i);

				//If player caught, teleport them back to their spawn room.
				if (tile.getOccupant() instanceof Player) {
					Player player = (Player) tile.getOccupant();
					player.getCurrentArea().getTile(player.getCurrentPosition()).removeOccupant(); //Remove player from this tile.
					player.setCurrentArea(this.areas.get(player.getSpawnName())); //Set player's area back to the spawn room.
					this.moveGOToTile(player, player.getCurrentArea().getTile(this.defaultSpawnPosition)); //Move player back to original spawn position.

					//Add message to the bundle about what just happened to the player
					this.getBundle(player.getId(), false)
							.setMessage("You were caught by a teacher and sent back to your spawn room!");

					continue outer; //Exit to the outer loop.
				}
			}
		}
	}

	/** GETTERS AND SETTERS FOR XML **/

	public Map<String, Area> getAreas() {
		return this.areas;
	}

	public List<Player> getPlayers() {
		return this.players;
	}

	public List<MovableGO> getMovableObjects() {
		return movableObjects;
	}

	public List<DoorGO> getDoorObjects() {
		return doorObjects;
	}

	public Map<String, InventoryGO> getInventoryObjects() {
		return inventoryObjects;
	}

	public List<NonHumanPlayer> getNonHumanPlayers() {
		return this.nonHumanPlayers;
	}

	public Map<String, FixedContainerGO> getFixedContainerObjects() {
		return this.fixedContainerObjects;
	}
}