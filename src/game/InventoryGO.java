package game;

/**
 * This class represents a game object that is an inventory object. This means players can hold this type of object in their inventory.
 * @author Pritesh R. Patel
 *
 */
public class InventoryGO implements GameObject {

	private static final long serialVersionUID = -1272721150110182719L;

	private final String name;
	private final String id;
	private final String token;
	private final int size;

	private String areaName;
	private Position position;

	private String description = "Seems to be some kind of item you can pick up. Try pressing 'z'.";

	public InventoryGO(String name, String id, String token, int size, String areaName, Position pos,
			String description) {

		this.name = name;
		this.id = id;
		this.token = token;
		this.areaName = areaName;
		this.size = size;
		this.position = pos;
		this.description = description;
	}

	/** GETTERS AND SETTERS **/

	public String getName() {
		return name;
	}

	@Override
	public String getToken() {
		return this.token;
	}

	@Override
	public String getId() {
		return this.id;
	}

	public int getSize() {
		return size;
	}

	@Override
	public Position getPosition() {
		return this.position;
	}

	@Override
	public void setCurrentPosition(Position position) {
		this.position = position;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public void setDescription(String desc) {
		this.description = desc;

	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InventoryGO other = (InventoryGO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		return true;
	}

}
