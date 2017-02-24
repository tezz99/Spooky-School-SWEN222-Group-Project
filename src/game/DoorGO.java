package game;

/**
 * 
 * Represents a door and WINDOW game object in the game.
 * @author Pritesh R. Patel
 *
 */

public class DoorGO implements GameObject {

	private static final long serialVersionUID = 2625782518677987399L;
	private final String id;
	private boolean open;
	private boolean locked;
	private final String keyID;
	private String description = "This is a Door.";

	private final String sideA;
	private final String tokenA;
	private final Position sideAPos;
	private final Position sideAEntryPos;

	private final String sideB;
	private final String tokenB;
	private final Position sideBPos;
	private final Position sideBEntryPos;


	public DoorGO(String id, boolean open, boolean locked, String keyID, String sideA, String tokenA, Position sideAPos,
			Position sideAEntryPos, String sideB, String tokenB, Position sideBPos, Position sideBEntryPos) {
		this.id = id;
		this.open = open;
		this.locked = locked;
		this.keyID = keyID;

		this.sideA = sideA;
		this.tokenA = tokenA;
		this.sideAPos = sideAPos;
		this.sideAEntryPos = sideAEntryPos;
		this.sideB = sideB;
		this.tokenB = tokenB;
		this.sideBPos = sideBPos;
		this.sideBEntryPos = sideBEntryPos;
	}


	/** GETTERS AND SETTERS **/

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getToken() {
		throw new Error("This method should not be used for door objects. use getToken(String currentSide) instead.");
	}

	/**
	 * Get the token for the door depending on what side of the door the player is on. Used for rendering
	 * @param currentSide name of the room the player is currently in.
	 * @return token that is needed for rendering the door.
	 */
	public String getToken(String currentSide) {
		if (this.sideA.equals(currentSide)) {
			return this.tokenA;
		}

		return tokenB;
	}

	public boolean isOpen() {
		return this.open;
	}

	public void setOpen(boolean change) {
		this.open = change;
	}

	public boolean isLocked() {
		return this.locked;
	}

	public void setLocked(boolean change) {
		this.locked = change;
	}

	/**
	 * Return the name of the area on the other side of this door.
	 * @param currentSide the name of the area the player is currently in.
	 * @return the name of the are that is on the other side of this door.
	 */
	public String getOtherSide(String currentSide) {
		if (currentSide.equals(sideA)) {
			return this.sideB;
		}
		return this.sideA;
	}


	/**
	 * Returns the position of tile that the player lands on (on the other side) when they pass through this door.
	 * @param currentSide the name of the area the player is currently in.
	 * @return the position of the tile that the player lands on when they apss through the door.
	 */
	public Position getOtherSideEntryPos(String currentSide) {
		if (currentSide.equals(sideA)) {
			return this.sideBEntryPos;
		}
		return this.sideAEntryPos;
	}


	/**
	 * Return the position of the door in a given area.
	 * @param areaName name of the area in which the door is.
	 * @return Position of the door in the given area.
	 */
	public Position getPosition(String areaName) {
		if (areaName.equals(sideA)) {
			return this.sideAPos;
		}
		return sideBPos;
	}


	@Override
	public Position getPosition() {
		throw new Error("This method is not avaliavle for door objects! Use getPosition(String areaName) instead!");
	}

	@Override
	public void setCurrentPosition(Position position) {
		throw new Error("Cannot change positions of door game objects!");
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public void setDescription(String desc) {
		this.description = desc;
	}


	/** GETTER AND SETTER METHODS FOR PARSER **/

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public String getSideA() {
		return sideA;
	}


	public String getTokenA() {
		return tokenA;
	}


	public Position getSideAPos() {
		return sideAPos;
	}


	public Position getSideAEntryPos() {
		return sideAEntryPos;
	}


	public String getSideB() {
		return sideB;
	}


	public String getTokenB() {
		return tokenB;
	}


	public String getKeyID() {
		return this.keyID;
	}

	public Position getSideBPos() {
		return sideBPos;
	}


	public Position getSideBEntryPos() {
		return sideBEntryPos;
	}

}
