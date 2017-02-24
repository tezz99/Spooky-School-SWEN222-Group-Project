package game;

/**
 * Used for game objects that take up more than one tile.
 * Can only be a marker for container and fixed objects.
 * @author Pritesh R. Patel
 *
 */
public class MarkerGO implements GameObject {

	private static final long serialVersionUID = -5518752175040649624L;
	private final GameObject baseGO;
	private final Position position;
	private String description;

	public MarkerGO(GameObject baseGO, Position pos) {
		//Throw error if the base object that this tile is a marker for is not a fixed or container game object.
		if (!(baseGO instanceof FixedGO || baseGO instanceof FixedContainerGO)) {
			throw new Error("Error: Invalid marker base object.");
		}
		this.baseGO = baseGO;
		this.position = pos;

		//this.description = baseGO.getDescription();
	}

	/**
	 *
	 * @return the "base" game object that this marker represents.
	 */
	public GameObject getBaseGO() {
		return this.baseGO;
	}

	@Override
	public String getToken() {
		return null;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public Position getPosition() {
		return this.position;
	}

	@Override
	public void setCurrentPosition(Position position) {
		throw new Error("Cannot change positions of marker game objects!");
	}

	@Override
	public String getDescription() {
		return this.baseGO.getDescription();
	}

	@Override
	public void setDescription(String desc) {
		this.description = desc;
	}

}
