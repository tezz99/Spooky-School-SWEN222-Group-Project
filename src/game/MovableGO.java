package game;

/**
 * THis class represents a game object that can be pushed by a player.
 * @author Pritesh R. Patel
 *
 */
public class MovableGO implements GameObject {

	private static final long serialVersionUID = 4547800855384957101L;

	private final String id;
	private final String token;
	private String description = "This is a movable object.";

	private final String areaName; //Movable objects cannot be taken out of their room/area.
	private Position position;

	public MovableGO(String id, String token, String area, Position position) {
		this.id = id;
		this.token = token;
		this.areaName = area;
		this.position = position;
	}

	/** GETTERS AND SETTERS **/

	@Override
	public String getToken() {
		return this.token;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public Position getPosition() {
		return this.position;
	}

	@Override
	public void setCurrentPosition(Position currentPosition) {
		this.position = currentPosition;
	}

	public String getAreaName() {
		return areaName;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public void setDescription(String desc) {
		this.description = desc;
	}

}
