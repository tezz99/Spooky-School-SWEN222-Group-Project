package game;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the Spooky School game and holds all information related to the player.
 * @author Pritesh R. Patel
 *
 */
public class Player implements GameObject {

	private static final long serialVersionUID = -347596131285383989L;
	private final String playerName;
	private Area currentArea;
	private final String spawnName;
	private Position currentPosition;
	private List<InventoryGO> inventory = new ArrayList<InventoryGO>();

	private String direction = "NORTH";
	private String token;
	private String description;

	public Player(String playerName, String spawnName, Area currentArea, Position currentPosition) {
		this.playerName = playerName;
		this.spawnName = spawnName;
		this.setCurrentArea(currentArea);
		this.setCurrentPosition(currentPosition);
		this.token = "0p20";
		this.description = this.playerName;
	}

	/** GETTERS AND SETTERS **/
	public String getPlayerName() {
		return playerName;
	}

	public Area getCurrentArea() {
		return currentArea;
	}

	public void setCurrentArea(Area currentArea) {
		this.currentArea = currentArea;
	}

	public Position getCurrentPosition() {
		return currentPosition;
	}

	@Override
	public void setCurrentPosition(Position currentPosition) {
		this.currentPosition = currentPosition;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	@Override
	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String getId() {
		return this.playerName;
	}

	@Override
	public Position getPosition() {
		return currentPosition;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public void setDescription(String desc) {
		this.description = desc;
	}

	public String getSpawnName() {
		return spawnName;
	}

	/**
	 * @return the player's inventory
	 */
	public List<InventoryGO> getInventory() {
		return inventory;
	}

	/**
	 * Removes the given item from the player's inventory.
	 * @param item that is to be removed from the player's inventory/
	 */
	public void removeFromInventory(InventoryGO item) {
		this.inventory.remove(item);
	}

	/**
	 * Add an item to the player's inventory.
	 * @param item the inventory item to add to the player's inventory.
	 */
	public void addToInventory(InventoryGO item) {
		this.inventory.add(item);
	}

	public boolean hasItemInInventory(InventoryGO item) {
		for (InventoryGO obj : this.inventory) {
			if (obj.getId().equals(item)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (playerName == null) {
			if (other.playerName != null)
				return false;
		} else if (!playerName.equals(other.playerName))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((playerName == null) ? 0 : playerName.hashCode());
		return result;
	}

}
