package parser;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import game.Area;
import game.ContainerGO;
import game.DoorGO;
import game.FixedContainerGO;
import game.FixedGO;
import game.FloorTile;
import game.GameObject;
import game.InventoryGO;
import game.MarkerGO;
import game.MovableGO;
import game.NonHumanPlayer;
import game.Player;
import game.Position;
import game.SpookySchool;
import game.Tile;
import game.WallTile;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;
import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Parser {
	
	//Load related Fields
	private Document load;
	private List <MovableGO> movablesToLoad;	//List of MovableGameObjects that need to be loaded
	private List <DoorGO> doorsToLoad;		//List of DoorGameObjects that need to be loaded
	private List <InventoryGO> inventsToLoad;		//List of InventoryGameObjects that need to be loaded
	private List <FixedContainerGO> fixedContsToLoad;	//List of FixedContainerGameObjects to be loaded
	
	//Save related Fields
	private Document save;		//DOM structure for saving
	private Element root;	//root Node for the save file
	private List<DoorGO> doors; 	//list of Door objects that are currently in the Game
	private Player saver;		//The Player that requested the Save operation
	private List<MovableGO> movables; //list of MovableGameObjects that are currently in the Game
	private List<NonHumanPlayer> nonHumans;		//List of NonHumanPlayer Objects currently in the Game
	private Map<String, InventoryGO> inventObjects;		//Map of all InventoryGameObjects currently in the Game. Map is name of the item to the Item itself
	private List<InventoryGO> saversInvent;		//List of InventoryItems that the Player that requested the save holds
	private List<InventoryGO> itemsInContainers;	//List of all the items held in Containers in the game
	private Map<String, FixedContainerGO> fixedContainers;	//Map of all fixedContainers in the Game. Map is the name of the Container to the Container.
	
	/**
	 * COnstructor for the XML Parser which will handle the saving and loading of save states for the Game.
	 * 
	 * @author Chethana Wijesekera
	 * 
	 */
	public Parser(){
		
	}
	/**
	 * Creates a DOM document structure in memory for the program. 
	 * This is for saving the game state to an XML file.
	 * 
	 * @return Document -- the DOM Document structure to be Saved
	 */
	private Document createDocument(){
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();	//create an instance of the DocumentBuilderFactory
			DocumentBuilder builder = factory.newDocumentBuilder();		//use the factory to make a builder to create a Document
			Document doc = builder.newDocument();	//create a DOM Document
			
			return doc;		//return the newly-made DOM Structure
			
		}catch(ParserConfigurationException e){		//Catch the exception thrown by the DocumentBuilderFactory
			e.printStackTrace();			//Print stack trace
		}
		return null;	//should never get here, just to the class compile
	}
	
	/**
	 * Main method for saving the game state of the SpookySchool game. From this method, relevant details about the Game are stored and then 
	 * recorded onto an XML file through extra modular methods.
	 * 
	 * Holds a private nature as this is the method called from the SpookySchool class to initiate the saving command.
	 * 
	 * @param game -- The current instance of the running SpookySchool game
	 * @param playerName -- Name of the player that requested the save command. This players details are what will be saved, along with 
	 * 						the rest of the Game Objects.
	 */
	public void save(SpookySchool game, String playerName){
		
		save = createDocument();	//creates a workable DOM Document where the XML Tree is saved
		root = save.createElement("game");		//create the root node for the XML file
		
		Map<String, Area> areas = game.getAreas();	//Get all the different rooms in the Game. Map is from areaName to Area
		List<Player> players = game.getPlayers();	//Get all the Players currently in the Game
		this.inventObjects = game.getInventoryObjects();	//Get all the InventoryGO currently in the game
		this.doors = game.getDoorObjects();		//Get all the DoorGOs currently in the Game
		this.saver = determinePlayer(playerName, players);	//Find the Player object for the player that saved the Game
		this.movables = game.getMovableObjects();	//Get all the MovableGOs currently in the Game
		this.nonHumans = game.getNonHumanPlayers();	//Get all the NonHumanPlayers currently in the Game
		this.fixedContainers = game.getFixedContainerObjects();		//Get all the FixedContainerObjects currently in the Game
		
		saveMap(areas);		//Save the current Game Map, i.e: all the different rooms in the Game
		
		createXML();	//Output the DOM structure to be transformed into an XML file
	}
	
	/**
	 * Main method for loading an instance of a game state into the program to be played with by the user. For this method, a new XML is
	 * loaded into memory as a workable DOM structure, and parsed with the help of modular methods to create a SpookySchool instance
	 * that can be played.
	 * 
	 */
	public void load(){
		load = loadXML();		//Load the Save file as an XML, and extract the workable DOM strcuture from it
		
		
	}
	
	/**
	 * Extracts the Player object that corresponds to the playerName string passed into it.
	 * 
	 * @param playerName -- String, name of the player who saved the game, and whose Player object is to be returned
	 * @param players -- List of all the Player objects held in the current Game.
	 * @return p -- the Player object whose name matches the given playerName
	 */
	private Player determinePlayer(String playerName, List<Player> players){
		for (Player p : players){		//iterate through each Player in the list of players
			if (p.getPlayerName().equals(playerName)){	//if the Player's name matches the given playerName
				return p;		//return this player
			}
		}
		return null; 	//should not get here, just to make it compile
	}
	
	/**
	 * Saves the entire map of the current Game by iterating through each Area and saving the fields of that Area.
	 * 
	 * @param areas -- Map of all the rooms in the Game. Map is from areaName to the Area itself
	 */
	private void saveMap(Map<String, Area> areas){
		for (String key : areas.keySet()){		//iterate through each areaName in the Map
			Area currentArea = areas.get(key);		//the area to work with
			root.appendChild(saveArea(currentArea));	//get the grid of the Area as a Node with children, and append it to the root.
		}
		root.appendChild(savePlayer());	//after saving the whole game map, save the player details to the root
		save.appendChild(root);		//append the root to the DOM document structure to be transformed into an XML
	}
	
	/**
	 * Save each room of the Game, including the height and width of the grid "area", the name of the Area, and all GameObjects that are
	 * in the room.
	 * 
	 * @param currentArea -- the current Area that is to be saved 
	 * @return -- the Element roomNode with all relevant data for the room saved as its children
	 */
	private Element saveArea(Area currentArea){
		Element roomNode = save.createElement("room");		//create the node "room". Holds all subsequent data in this method as a child
		Element heightNode = save.createElement("height");		//create node "height"
		Element widthNode = save.createElement("width");		//create the node "width"
		
		heightNode.appendChild(save.createTextNode("" + currentArea.getArea()[0].length));	//record the height of the area
		widthNode.appendChild(save.createTextNode("" + currentArea.getArea().length)); //record the width of the area
		
		Element areaNameNode = save.createElement("areaName");	//create a node to represent the name of the room
		areaNameNode.appendChild(save.createTextNode(currentArea.getAreaName())); //append the name of the room to the areaName node
		
		roomNode.appendChild(heightNode);	//append the height to the room node
		roomNode.appendChild(widthNode);	//append the width to the room node
		roomNode.appendChild(areaNameNode);		//append the areaName to the room node
		
		Element areaNode  = save.createElement("area");	//create a node to represent the grid 
		roomNode.appendChild(areaNode);		//append the grids node to the roomNode so its a part of that room
		
		saveTiles(currentArea, areaNode);	//save all the tiles in the area grid and append it to the areaNode
		saveDoors(currentArea, roomNode);	//save all the doors in the room and append it to the roomNode
		saveMovables(currentArea, roomNode);	//save all the movableGOs in the room and append it to the roomNode
		saveNonHumans(currentArea, roomNode);	//save all the NonHumanPlayers in the room and append it to the roomNode
		saveInventoryGameObjects(currentArea, roomNode);	//save all the inventoryGOs in the room and append it to the roomNode
		saveFixedContainers(currentArea, roomNode);		//save all the fixedContainerGOs in the room and append it o the roomNode
		
				
		return roomNode;	//return the roomNode with all its children to be appended to the root
	}
	
	/**
	 * Saves each Tile in the 2D-array of Tiles in an Area, including WallTiles and FloorTiles as well as null tiles (Tiles that do
	 * are not a Floor or Wall Tile). Also saves the fixed position occupant of the Tile, i.e: saves the FixedGO and MarkerGO that is 
	 * on the FloorTile.
	 * 
	 * @param currentArea -- The current Area object that is being saved.
	 * @param currentParent -- The parent Node to be attached.
	 */
	private void saveTiles(Area currentArea, Element currentParent){
		
		for(int i = 0; i < currentArea.getArea().length; i++){		//iterate through the 2D-array, so essentially iterate through every Tile
			for (int j = 0; j < currentArea.getArea()[i].length; j++){
				Element tileNode = save.createElement("tile");		//Create a Tile Node
				Tile currentTile = currentArea.getArea()[i][j];		//Take a record of the current Tile
				
				if (currentTile == null){		//if the currentTile is a null Tiles
					tileNode.setAttribute("tileType", "black");		//record that it is a "black tile"
					tileNode.appendChild(save.createTextNode("null"));	//say it contains nothing
					currentParent.appendChild(tileNode);	//append the Tile to the room
				}else{
					if(currentTile instanceof WallTile){		//if the Tile is a WallTile
						tileNode.setAttribute("tileType", "WallTile");	//record that it is a WallTile
						
					}else if(currentTile instanceof FloorTile){		//if the Tile is a FloorTile
						tileNode.setAttribute("tileType", "FloorTile");		//record that it is a a FloorTile
						GameObject occupant = currentTile.getOccupant();	//get the GameObjec that is on the Tile
						Element occupantNode = save.createElement("occupant");	//create a node for this Occupant
						if(occupant != null){		//as long as there IS an occupant
							occupantNode.setAttribute("objectType", occupant.getClass().toString().substring(11)); //record the objectType of the
																				//occupant and format the string to remove unwanted tokens
						}
						
						if(occupant instanceof FixedGO){		//if the occupant is a FixedGameObject, save its fields as nodes
							occupantNode.appendChild(saveID(occupant));		//save the ID and append to the occupantNode
							occupantNode.appendChild(saveToken(occupant));		//save the token and append to the occupantNode
							occupantNode.appendChild(savePosition(occupant));		//save the position and append to the occupantNode
							occupantNode.appendChild(saveDescription(occupant));		//save the description and append to the occupantNode
							
							tileNode.appendChild(occupantNode);		//append the occupantNode and its children to the current tileNode
							
						}else if(occupant instanceof MarkerGO){
							occupantNode.appendChild(savePosition(occupant));		//save the position and append to the occupantNode
							occupantNode.appendChild(saveBaseGameObject(occupant));		//save the BaseGameObject and append to the occupantNode
							
							tileNode.appendChild(occupantNode);			//append the occupantNode and its children to the current tileNode			
						}
					}
					tileNode.appendChild(savePosition(currentTile));		//as long as the occupant isn't null, append the currentTile to the tileNode
																			//NOTE: Tiles without an occupant have already been appended
					currentParent.appendChild(tileNode);		//append the tileNode to the roomNode (the parent)
				}
			}
		}	
	}
	
	/**
	 * Saves the fields held by the Player who saved the Game. These fields are saved as child nodes to a parent "player" node, which is then
	 * appended to the main game node in the XML
	 * 
	 * @return	player -- Element that represents the player, with child nodes representing the fields.
	 */
	private Element savePlayer(){
		Element player = save.createElement("player");		//create an element to represent the player who saved the Game
		player.appendChild(saveName(saver));		//save the name and append it to the playerNode
		player.appendChild(saveAreaName(saver));		//save the areaName and append it to the playerNode
		player.appendChild(savePosition(saver));		//save the Position and append it to the playerNode
		player.appendChild(saveToken(saver));		//save the token and append it to the playerNode
		player.appendChild(saveDescription(saver));		//save the description and append it to the playerNode
	
		return player;		//return the player to be appended to the gameNode
	}
	
	/**
	 * Save the fields for each fixedContainerGO in the Game. These fields are saved as child nodes to a parent "fixedContainer" node, which is then
	 * appended to the room node in the XML.
	 * 
	 * @param currentArea -- the current Area that is being saved
	 * @param roomNode -- the parent Element that represents the room being saved.
	 */
	private void saveFixedContainers(Area currentArea, Element roomNode){
		
		for(String key : fixedContainers.keySet()){		//iterate over the keys for the Map of fixedContainers
			FixedContainerGO currentFixedContainer = fixedContainers.get(key);		//establish a currentContainter
			
			if(currentFixedContainer.getArea().equals(currentArea.getAreaName())){		//if the container is in the currentArea
				Element fixedContainerNode = save.createElement("fixedContainer");		//create an element to represent it
				fixedContainerNode.setAttribute("id", currentFixedContainer.getId());	//mark it with an id attribute based on the containerID
				
				fixedContainerNode.appendChild(saveName(currentFixedContainer));	//create nodes for each field of the container and append them
				fixedContainerNode.appendChild(saveAreaName(currentFixedContainer));		//to the fixedContainerNode
				fixedContainerNode.appendChild(saveID(currentFixedContainer));
				fixedContainerNode.appendChild(saveToken(currentFixedContainer));
				fixedContainerNode.appendChild(saveOpen(currentFixedContainer));
				fixedContainerNode.appendChild(saveLocked(currentFixedContainer));
				fixedContainerNode.appendChild(saveKeyID(currentFixedContainer));
				fixedContainerNode.appendChild(savePosition(currentFixedContainer));
				fixedContainerNode.appendChild(saveSize(currentFixedContainer));
				fixedContainerNode.appendChild(saveContents(currentFixedContainer));
				fixedContainerNode.appendChild(saveSizeRemaining(currentFixedContainer));
				
				roomNode.appendChild(fixedContainerNode);		//append the containerNode and its children to the roomNode
			}
		}
	}
	
	/**
	 * Saves all the InventoryGO in the current Area and all their field values. These values are saved as child nodes appended to a node to represent the
	 * current item.
	 * 
	 * @param currentArea -- the current Area where the items are checked to exist.
	 * @param roomNode -- the Node to which each item will be appended to.
	 */
	private void saveInventoryGameObjects(Area currentArea, Element roomNode){
		
		saversInvent = saver.getInventory();		//get the Player who saved the game's inventory
		itemsInContainers = new ArrayList<>();		//initialize the list to remember which items remain in containers
		
		for (String key : inventObjects.keySet()){		//iterate through the keys in the Map of InventoryGOs
			InventoryGO currentObject = inventObjects.get(key);	//establish a currentObject to work with
			
			if(currentObject.getPosition() != null){	//inventory item is on the floor, not being held
				
				Element inventoryObject = save.createElement("inventoryObject");	//create a node to represent the current item
				inventoryObject.setAttribute("id", currentObject.getId());	//mark the item with an id attribute 
				
				inventoryObject.appendChild(saveName(currentObject));		//save each of the fields for the currentItem, appending each of them
				inventoryObject.appendChild(saveID(currentObject));				//to the inventoryObject node
				inventoryObject.appendChild(saveToken(currentObject));
				inventoryObject.appendChild(saveAreaName(currentObject));
				inventoryObject.appendChild(saveSize(currentObject));
				inventoryObject.appendChild(savePosition(currentObject));
				inventoryObject.appendChild(saveDescription(currentObject));
				
				roomNode.appendChild(inventoryObject);		//append the object Node to the parentRoom
			}else{			//otherwise, if item is not on the floor,
				if(!saversInvent.contains(currentObject)){	//and not in the inventory, it must be in a container
					itemsInContainers.add(currentObject);	//so add it to the list of items in containers
				}
			}
		}
	}
	
	/**
	 * Saves all the NonHumanPlayers in the game and their field values to their respective Areas in the XML.
	 * 
	 * @param currentArea -- the current Area where the NonHumanPlayer is checked to exist in
	 * @param roomNode -- the Node to which each NonHumnPlayer will be appended to
	 */
	private void saveNonHumans(Area currentArea, Element roomNode){
		for (NonHumanPlayer nhp : nonHumans){		//iterate through the list of NonHumans
			if (nhp.getCurrentArea().getAreaName().equals(currentArea.getAreaName())){	//if their name matches the name of the Area,
																						//then it should be saved here
				Element nonHumanNode = save.createElement("nonHumanPlayer");	//create a node to represent it
				nonHumanNode.setAttribute("name", nhp.getPlayerName());		//mark it with the attribute name to identify it
				nonHumanNode.appendChild(saveName(nhp));					//save the nhp's respective fields and append them to the nonHumanNode
				nonHumanNode.appendChild(saveNonHumanSpawnName(nhp));
				nonHumanNode.appendChild(saveAreaName(nhp));
				nonHumanNode.appendChild(savePosition(nhp));
				
				roomNode.appendChild(nonHumanNode);		//append the nonHuman node and its children to the parent roomNode
				
			}
		}
	}
	
	/**
	 * Saves all the Movable Game Objects in the game and their field values to their respective Areas in the XML
	 * 
	 * @param currentArea -- the current Area where the NonHumanPlayer is checked to exist in
	 * @param roomNode -- the Node to which each NonHumnPlayer will be appended to
	 */
	private void saveMovables(Area currentArea, Element roomNode){
		for (MovableGO object : movables){		//iterate through the list of movableObjects
			if(object.getAreaName().equals(currentArea.getAreaName())){		//if the areaName of the object matches the name of the current Area
				Element movableNode = save.createElement("movableGO");	//create a node to represent it
				
				movableNode.appendChild(saveID(object));		//save the movable's respective fields and append them to the movableNode 
				movableNode.appendChild(saveToken(object));
				movableNode.appendChild(saveAreaName(object));
				movableNode.appendChild(savePosition(object));
				movableNode.appendChild(saveDescription(object));
				
				roomNode.appendChild(movableNode);	//append the movable node and its children to the parent roomNode
			}
		}
	}
	
	/**
	 * Saves all the Door Game Objects in the game and their field values to their respective Areas in the XML.
	 * 
	 * @param currentArea -- the current Area where the NonHumanPlayer is checked to exist in
	 * @param roomNode -- the Node to which each NonHumnPlayer will be appended to
	 */
	private void saveDoors(Area currentArea, Element roomNode){
		
		for(DoorGO door : doors){			//iterate through the list of Doors
			if (door.getSideA().equals(currentArea.getAreaName())){ 	//if the door is in the currentArea
				Element doorNode = save.createElement("door");		//create a node to represent it
				doorNode.setAttribute("id", door.getId());		//mark this doorNode with an id attribute
				
				doorNode.appendChild(saveID(door));				//save the door's respective fields and append them to the doorNode  
				doorNode.appendChild(saveOpen(door));
				doorNode.appendChild(saveLocked(door));
				doorNode.appendChild(saveKeyID(door));
				doorNode.appendChild(saveDescription(door));
				
				doorNode.appendChild(saveSideA(door));
				doorNode.appendChild(saveTokenA(door));
				doorNode.appendChild(saveSideAPos(door));
				doorNode.appendChild(saveSideAEntryPos(door));
				
				doorNode.appendChild(saveSideB(door));
				doorNode.appendChild(saveTokenB(door));
				doorNode.appendChild(saveSideBPos(door));
				doorNode.appendChild(saveSideBEntryPos(door));
				
				roomNode.appendChild(doorNode);			//append the door node and its children to the parent roomNode
			}
		}
	}
	
	/**
	 * Saves the spawn name of a NonHumanPlayer into a node
	 * 
	 * @param nhp -- the NonHumanPlayer whose name is required
	 * @return spawnName -- the Node with the NonHumanPlayers spawnName
	 */
	private Element saveNonHumanSpawnName(NonHumanPlayer nhp){
		Element spawnName = save.createElement("spawnName");	//create a node called spawnName
		Text value = save.createTextNode("null");	//NonHumans do not have a spawn name, so append the String "null" to avoid NullPointers
		spawnName.appendChild(value);	//append the two Nodes
		return spawnName; 	//return the final Node
	}
	
	/**
	 * Save the "A side" of a given door. DoorGOs have two sides, so the sideA refers to one of these sides.
	 * 
	 * @param door -- the DoorGO whose side is required
	 * @return sideA -- the Node containing the DoorGOs sideA
	 */
	private Element saveSideA(DoorGO door){
		Element sideA = save.createElement("sideA");	//create a node called sideA
		Text value = save.createTextNode(door.getSideA());		//create a node with the doors sideA value
		sideA.appendChild(value);	//append the two Nodes
		return sideA;		//return the final Node
	}
	
	/**
	 * Save the "B side" of a given door. DoorGOs have two sides, so the sideB refers to one of these sides.
	 * 
	 * @param door -- the DoorGO whose side is required
	 * @return sideB -- the Node containing the DoorGOs sideB
	 */
	private Element saveSideB(DoorGO door){
		Element sideB = save.createElement("sideB");	//create a node called sideB
		Text value = save.createTextNode(door.getSideB());		//create a node with the doors sideB value
		sideB.appendChild(value);	//append the two Nodes
		return sideB;		//return the final Node
	}
	
	/**
	 * Save the "A side" token of the door.
	 * 
     * @param door -- the DoorGO whose token is required
	 * @return tokenA -- the Node containing the DoorGOs sideAToken
	 */
	private Element saveTokenA(DoorGO door){
		Element tokenA = save.createElement("tokenA");		//create a node
		Text value = save.createTextNode(door.getTokenA());		//create a node to hold the value
		tokenA.appendChild(value);		//append the two Nodes
		return tokenA;		//return the final Node
	}
	
	/**
	 * Save the "B side" token of the door.
	 * 
     * @param door -- the DoorGO whose token is required
	 * @return tokenB -- the Node containing the DoorGOs sideBToken
	 */
	private Element saveTokenB(DoorGO door){
		Element tokenB = save.createElement("tokenB");	//create a node
		Text value = save.createTextNode(door.getTokenB());		//create a node to hold the value
		tokenB.appendChild(value);		//append the final Node
		return tokenB;		//return the final Node
	}
	
	/**
	 * Save the "A side" position of the door.
	 * 
     * @param door -- the DoorGO whose position is required
	 * @return sideAPos -- the Node containing the DoorGOs sideAPosition
	 */
	private Element saveSideAPos(DoorGO door){
		Element sideAPos = save.createElement("sideAPos");	//create a node for the Position
		Element x = save.createElement("x");		//create a node for x
		Element y = save.createElement("y");		//create a node for y
		x.appendChild(save.createTextNode("" + door.getSideAPos().getPosX())); //find and append the value for x
		y.appendChild(save.createTextNode("" + door.getSideAPos().getPosY())); //find and append the value for y
		sideAPos.appendChild(x);	//append x to Position
		sideAPos.appendChild(y);	//append y to Position
		return sideAPos;		//return the final Node
	}
	
	/**
	 * Save the "B side" position of the door.
	 * 
     * @param door -- the DoorGO whose position is required
	 * @return sideBPos -- the Node containing the DoorGOs sideBPosition
	 */
	private Element saveSideBPos(DoorGO door){
		Element sideBPos = save.createElement("sideBPos");	//create a node for the Position
		Element x = save.createElement("x");		//create a node for x
		Element y = save.createElement("y");		//create a node for y
		x.appendChild(save.createTextNode("" + door.getSideBPos().getPosX())); //find and append the value for x
		y.appendChild(save.createTextNode("" + door.getSideBPos().getPosY())); //find and append the value for y
		sideBPos.appendChild(x);	//append x to Position
		sideBPos.appendChild(y);	//append y to Position
		return sideBPos;	//return the final Node
	}
	
	/**
	 * Save the "A side" entry position of the door.
	 * 
     * @param door -- the DoorGO whose position is required
	 * @return sideAEntryPos -- the Node containing the DoorGOs sideAEntryPosition
	 */
	private Element saveSideAEntryPos(DoorGO door){
		Element sideAEntryPos = save.createElement("sideAEntryPos");	//create a node for the Position
		Element x = save.createElement("x");	//create a node for x
		Element y = save.createElement("y");	//create a node for y
		x.appendChild(save.createTextNode("" + door.getSideAEntryPos().getPosX()));	//find and append value for x
		y.appendChild(save.createTextNode("" + door.getSideAEntryPos().getPosY()));	//find and append value for y
		sideAEntryPos.appendChild(x);	//append x to position
		sideAEntryPos.appendChild(y);	//append y to position
		return sideAEntryPos;	//return final Node
	}
	
	/**
	 * Save the "B side" entry position of the door.
	 * 
     * @param door -- the DoorGO whose position is required
	 * @return sideBEntryPos -- the Node containing the DoorGOs sideBEntryPosition
	 */
	private Element saveSideBEntryPos(DoorGO door){
		Element sideBEntryPos = save.createElement("sideBEntryPos");	//create a node for the Position
		Element x = save.createElement("x");	//create a node for x
		Element y = save.createElement("y");	//create a node for y
		x.appendChild(save.createTextNode("" + door.getSideBEntryPos().getPosX()));	//find and append value for x
		y.appendChild(save.createTextNode("" + door.getSideBEntryPos().getPosY()));	//find and append value for y
		sideBEntryPos.appendChild(x);	//append x to position
		sideBEntryPos.appendChild(y);	//append y to position
		return sideBEntryPos;	//return final Node
	}
	
	/**
	 * Saves the Base GameObject that the current MarkerGO is surrounding.
	 * 
	 * @param occupant -- the MarkerGO whose base is being saved
	 * @return base -- the Node containing the Base GameObject
	 */
	private Element saveBaseGameObject(GameObject occupant){
		
		Element base = save.createElement("base");	//create a node
		GameObject baseObject = ((MarkerGO)occupant).getBaseGO();	//create a node for the value
		base.setAttribute("objectType", baseObject.getClass().toString().substring(11)); //set an attribute for the type of the BaseGO
																					//format the string
		
		if(baseObject instanceof FixedGO){			//base should always be FixedGO
			base.appendChild(saveID(baseObject));	//save the fields for the Base and append them to the base Node
			base.appendChild(saveToken(baseObject));
			base.appendChild(savePosition(baseObject));
			base.appendChild(saveDescription(baseObject));
		}
		
		return base; //return the base GameObject
		
	}
	
	/**
	 * Saves the contents of the FixedContainerGO
	 * 
	 * @param occupant -- GameObject that is on the current Tile
	 * @return contents -- Element that has all inventory items and their fields as its children
	 */
	private Element saveContents(GameObject occupant){
		Element contents = save.createElement("contents");		//create a node for the contents
		
		List<InventoryGO> items = ((FixedContainerGO) occupant).getAllItems(); //make a list of all the items in the container
		for (InventoryGO i : items){	//iterate through
			Element item = save.createElement("item");	//make a node to represent a item
			item.setAttribute("objectType", i.getClass().toString()); //mark it wit its class as an attribute
			if(i instanceof ContainerGO){		//if item is another container
				contents.appendChild(saveSizeRemaining(i));	//save its extra field
			}
			item.appendChild(saveName(i));	//save fields for the item and append them to the item node
			item.appendChild(saveID(i));
			item.appendChild(saveToken(i));
			item.appendChild(saveSize(i));
			item.appendChild(saveAreaName(i));
			item.appendChild(savePosition(i));
			item.appendChild(saveDescription(i));
			
			contents.appendChild(item);			//append the current item to the contents Node
		}
		
		return contents; //after all the items are saved, return the contents
		
	}
	
	/**
	 * Saves the field "size remaining" for containerGOs and FixedContainerGOs
	 * 
	 * @param container -- GameObject whose sizeRemaining is to be found
	 * @return sizeRemaing -- Node holding the value of sizeRemaining
	 */
	private Element saveSizeRemaining(GameObject container){
		Text value = save.createTextNode("");	//initialize value node
		Element sizeRemaining = save.createElement("sizeRemaining"); //create a node to identify the value
		
		if(container instanceof ContainerGO){
			value = save.createTextNode("" + ((ContainerGO)container).getSizeRemaining()); //cast to the appropriate Object, get value
			sizeRemaining.appendChild(value); //append value
		}else if(container instanceof FixedContainerGO){ 
			value = save.createTextNode("" + ((FixedContainerGO)container).getSizeRemaining()); //cast to the appropriate Object, get value
			sizeRemaining.appendChild(value); //appendValue
		}
		
		return sizeRemaining;	//return final Node
	}
	
	/**
	 * Saves the field "name" to a Node
	 * @param occupant -- the GameObject whose name is required
	 * @return name -- the Node containing the name
	 */
	private Element saveName(GameObject occupant){
		Text value = save.createTextNode("");	//initialize
		Element name = save.createElement("name");	//create a node to identify the Node
		if(occupant instanceof InventoryGO){
			value = save.createTextNode(((InventoryGO) occupant).getName());	//cast to the appropriate Object, get value
		} else if(occupant instanceof NonHumanPlayer){
			value = save.createTextNode(((NonHumanPlayer) occupant).getPlayerName());//cast to the appropriate Object, get value
		} else if(occupant instanceof FixedContainerGO){
			value = save.createTextNode(((FixedContainerGO) occupant).getName());//cast to the appropriate Object, get value
		} else if(occupant instanceof Player){
			value = save.createTextNode(((Player) occupant).getPlayerName());//cast to the appropriate Object, get value
		}
		
		name.appendChild(value);	//append value
		return name;	//return final Node
	}
	
	/**
	 * Saves the field "id" to a Node
	 * 
	 * @param occupant -- the GameObject whose id is required
	 * @return id -- the Node containing the id
	 */
	private Element saveID(GameObject occupant){
		Text value = save.createTextNode("");	//initialize
		Element id = save.createElement("id");	//create a node to identify the value
		if(occupant instanceof InventoryGO){
			value = save.createTextNode(((InventoryGO) occupant).getId()); //cast to the appropriate Object, get value
		}else if(occupant instanceof FixedGO){
			value = save.createTextNode(((FixedGO) occupant).getId());//cast to the appropriate Object, get value
		}else if(occupant instanceof FixedContainerGO){
			value = save.createTextNode(((FixedContainerGO) occupant).getId());//cast to the appropriate Object, get value
		}else if(occupant instanceof MovableGO){
			value = save.createTextNode(((MovableGO) occupant).getId());//cast to the appropriate Object, get value
		}
		id.appendChild(value); //append value
		return id;	//return final Node
	}
	
	/**
	 * Saves the field "keyID" to a Node
	 * 
	 * @param occupant -- the GameObject whose keyId is required
	 * @return keyID -- the Node containing the keyID
	 */
	private Element saveKeyID(GameObject occupant){
		Text value = save.createTextNode("");	//initialize
		Element keyID = save.createElement("id");	//create a node to identify the value
		if(occupant instanceof FixedContainerGO){
			if(((FixedContainerGO) occupant).getKeyID() == null){
				value = save.createTextNode("null"); //if a fixedContainer has null key ID, it is unlocked
			}else{
				value = save.createTextNode(((FixedContainerGO) occupant).getKeyID()); //cast to the appropriate Object, get value
			}
			
		}
		keyID.appendChild(value); //append value
		return keyID; //return final Node
	}
	
	/**
	 * Saves the field "position" to a Node
	 * 
	 * @param currentTile -- the Tile whose position is required
	 * @return pos -- the Node containing the Position
	 */
	private Element savePosition(Tile currentTile){
		Element pos = save.createElement("pos");	//create a node to identify the position
		Element x = save.createElement("x");	//create a node for x
		x.appendChild(save.createTextNode("" + currentTile.getPosition().getPosX())); //find and append x
		Element y = save.createElement("y");	//create a node for y
		y.appendChild(save.createTextNode("" + currentTile.getPosition().getPosY())); //find and append y
		
		pos.appendChild(x);	//append x
		pos.appendChild(y);	//append y
		
		return pos;	//return final Node
	}
	
	/**
	 * Saves the field "position" to a Node
	 * 
	 * @param occupant -- the GameObject whose position is required
	 * @return pos -- the Node containing the Position
	 */
	private Element savePosition(GameObject occupant){
		Element pos = save.createElement("pos"); //create a node to identify the position
		Element x = save.createElement("x");	//create a node for x
		Element y = save.createElement("y");	//create a node for y
		Text xVal = save.createTextNode("" + occupant.getPosition().getPosX());	//find x
		Text yVal = save.createTextNode("" + occupant.getPosition().getPosY());	//find y
		x.appendChild(xVal); //append xVal
		y.appendChild(yVal); //append yVal
		pos.appendChild(x);  //append x
		pos.appendChild(y);	 //append y
		return pos;		//return final Node
	}
	
	/**
	 * Saves the field "token" to a Node
	 * @param occupant -- The GameObject whose token is required
	 * @return token -- the Node containing the token
	 */
	private Element saveToken(GameObject occupant){
		Element token = save.createElement("token");	//create a node for token
		token.appendChild(save.createTextNode(occupant.getToken())); //find and append token
		return token; //return final Node
	}
	
	/**
	 * Saves the field "token" to a Node
	 * 
	 * @param currentTile -- The Tile whose token is required
	 * @return token -- the Node containing the token
	 */
	private Element saveToken(Tile currentTile){
		Element token = save.createElement("token"); //create a node for token
		token.appendChild(save.createTextNode(currentTile.getToken())); //find and append token
		return token; //return final Node
	}
	
	/**
	 * Saves the field "open" to a Node
	 * 
	 * @param occupant -- the GameObject that is required to know whether it is open or not
	 * @return open -- Node containing whether the Object is open or not
	 */
	private Element saveOpen(GameObject occupant){
		Text value = save.createTextNode("");	//initialize
		if(occupant instanceof DoorGO){
			 value = save.createTextNode("" + ((DoorGO) occupant).isOpen()); //cast to appropriate class and get value
		}else if(occupant instanceof FixedContainerGO){
			 value = save.createTextNode("" + ((FixedContainerGO) occupant).isOpen());	//cast to appropriate class and get value
		}
		Element open = save.createElement("open"); //create node 
		open.appendChild(value); //append value to node
		return open; //return final node
	
	}
	
	/**
	 * Saves the field "locked" to a Node
	 * 
	 * @param occupant -- the GameObject that is required to know whether it is locked or not
	 * @return locked -- Node containing whether the Object is locked or not
	 */
	private Element saveLocked(GameObject occupant){
		Text value = save.createTextNode("");	//initialize
		if(occupant instanceof DoorGO){
			value = save.createTextNode("" + ((DoorGO) occupant).isLocked()); //cast to appropriate class and get value
		}else if (occupant instanceof FixedContainerGO){
			value = save.createTextNode("" + ((FixedContainerGO) occupant).isLocked()); //cast to appropriate class and get value
		}
		Element locked = save.createElement("locked"); //create node
		locked.appendChild(value); //append value to node
		return locked; //return final Node
		
	}
	
	/**
	 * Save the field "description" to a Node
	 * 
	 * @param occupant -- the GameObject whose description is required
	 * @return description -- Node containing the description
	 */
	private Element saveDescription(GameObject occupant){
		Text value = save.createTextNode("");	//initialize
		if(occupant instanceof InventoryGO){
			value = save.createTextNode(((InventoryGO) occupant).getDescription());	//cast to appropriate class and get value
		}else if (occupant instanceof DoorGO){
			value = save.createTextNode(((DoorGO) occupant).getDescription()); //cast to appropriate class and get value
		}else if (occupant instanceof FixedGO){
			value = save.createTextNode(((FixedGO) occupant).getDescription());	 //cast to appropriate class and get value	
		}else if (occupant instanceof MarkerGO){
			value = save.createTextNode(((MarkerGO) occupant).getDescription());	 //cast to appropriate class and get value		
		}else if (occupant instanceof Player){
			value = save.createTextNode(((Player) occupant).getDescription());		//cast to appropriate class and get value	
		}else if (occupant instanceof MovableGO){
			value = save.createTextNode(((MovableGO) occupant).getDescription());	//cast to appropriate class and get value		
		}
		
		Element description = save.createElement("description"); //create node to identify description
		description.appendChild(value); 	//append value
		return description;		//return final Node
	}
	
	/**
	 * Save the field "size" to a Node
	 * 
	 * @param occupant -- the GameObject whose size is required
	 * @return size -- Node containing the field size
	 */
	private Element saveSize(GameObject occupant){
		Text value = save.createTextNode("");	//initialize
		if(occupant instanceof InventoryGO){
			value = save.createTextNode("" + ((InventoryGO) occupant).getSize()); //cast to appropriate class and get value
		}else if (occupant instanceof FixedContainerGO){
			value = save.createTextNode("" + ((FixedContainerGO) occupant).getSize()); //cast to appropriate class and get value
		}
			Element size = save.createElement("size");	//create node to identify size
			size.appendChild(value);	//append value to node
			return size;		//return final Node
	}
	
	/**
	 * Save the field "areaName" to a Node
	 * @param occupant -- the GameObject whose areaName is required
	 * @return areaName -- Node containing the field areaName
	 */
	private Element saveAreaName(GameObject occupant){
		Text value = save.createTextNode("");	//initialize
		if(occupant instanceof InventoryGO){
			value = save.createTextNode(((InventoryGO) occupant).getAreaName()); //cast to appropriate class and get value
		}else if (occupant instanceof MovableGO){
			value = save.createTextNode(((MovableGO) occupant).getAreaName());	//cast to appropriate class and get value
		}else if (occupant instanceof NonHumanPlayer){
			value = save.createTextNode((((NonHumanPlayer) occupant).getCurrentArea().getAreaName()));	//cast to appropriate class and get value
		}else if (occupant instanceof FixedContainerGO){
			value = save.createTextNode((((FixedContainerGO) occupant).getArea()));	//cast to appropriate class and get value
		}else if (occupant instanceof Player){
			value = save.createTextNode((((Player) occupant).getCurrentArea().getAreaName()));	//cast to appropriate class and get value
		}
		Element areaName = save.createElement("areaName"); //create node to identify areaName value
		areaName.appendChild(value);		//append value
		return areaName;	//return final Node

	}
	
	/**
	 * Loads a given XML file as a DOM Structure into memory so that it can be parse and have information read
	 * from it. 
	 * 
	 * @return document -- the DOM Structure of the saved XML file
	 */
	private Document loadXML(){
		try{
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();	//Create a builder factory instance
		    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();	//create a document builder to construct the Document
		    Document document = docBuilder.parse(new File("saveNew.xml")); 		//create the document by parsing the XML File
		    return document;
		}catch(SAXException ex){	//catch exception
			ex.printStackTrace();
		}catch(IOException ex){		//catch exception
			ex.printStackTrace();
		}catch(ParserConfigurationException ex){	//catch exception
			ex.printStackTrace();
		}
		return null;	//should never reach here, just to keep the compiler happy
	}
	
	/**
	 * Creates a new XML File by transforming the DOM Structure "save" into a new File
	 */
	private void createXML(){
		try{
			TransformerFactory transformerFactory = TransformerFactory.newInstance();	//creates a transformer factory instance
			Transformer transformer = transformerFactory.newTransformer();	//create a new transformer
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");		//set Output Properties
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			//transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "save.dtd");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			DOMSource source = new DOMSource(save);		//select the sourse document
			StreamResult result = new StreamResult(new File("saveNew.xml"));	//select the output File
			transformer.transform(source, result);	//transform
			// Output to console for testing
			StreamResult consoleResult =new StreamResult(System.out);
			transformer.transform(source, consoleResult);
			
			load();
		}catch(TransformerException e){	//catch Exception
			e.printStackTrace();
		}
	}
	
}


	


