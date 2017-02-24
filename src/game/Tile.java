package game;

import java.io.Serializable;

/**
 * Represents a basic tile and has the methods that all tiles should have.
 * @author Pritesh R. Patel
 *
 */
public abstract class Tile implements Serializable {

	private static final long serialVersionUID = 2738627054424521284L;
	private final Position position;
	private GameObject occupant;
	private String token;

	public Tile(Position pos, String token) {
		this.position = pos;
		this.token = token;
	}

	/**
	 * Sets the occupant that is 
	 * @param occupant to set onto this tile.
	 */
	public void setOccupant(GameObject occupant) {
		if (this.isOccupied()) {
			throw new Error("Tile is already occupied!");
		}
		this.occupant = occupant;
	}

	/**
	 * Removes the current occupant of this tile by setting it to null.
	 */
	public void removeOccupant() {
		this.occupant = null;
	}

	/**
	 * 
	 * @return the occupant that is currently on the tile or null if there the tile is not occupied.
	 */
	public GameObject getOccupant() {
		return this.occupant;
	}


	/**
	 * 
	 * @return the Position of this tile.
	 */
	public Position getPosition() {
		return this.position;
	}


	/**
	 * 
	 * @return true if the tile is occupied and false otherwise.
	 */
	public boolean isOccupied() {
		return this.occupant != null;
	}

	/**
	 * 
	 * @return the tile "type". Useful for displaying a particular tile.
	 */
	public String getToken() {
		return this.token;
	}
}
