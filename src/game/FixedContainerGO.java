package game;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a container object that is fixed on the map.
 * @author Pritesh R. Patel
 *
 */
public class FixedContainerGO implements GameObject {

	private static final long serialVersionUID = 118153527696427133L;

	private final String name;
	private final String area;
	private final String id;
	private final String token;
	private boolean open;
	private boolean locked;
	private final String keyID;
	private final Position position;

	private List<InventoryGO> contents = new ArrayList<InventoryGO>();
	private final int size; // For container size.
	private int sizeRemaining;

	private String description;

	public FixedContainerGO(String name, String area, String id, String token, boolean open, boolean locked,
			String keyID, int size, Position position) {

		this.name = name;
		this.area = area;
		this.id = id;
		this.token = token;
		this.setOpen(open);
		this.setLocked(locked);
		this.keyID = keyID;
		this.size = size;
		this.sizeRemaining = size;
		this.position = position;

		//Ensure a key exists the container is locked.
		if (keyID == null && locked) {
			throw new Error("KeyID cannot be null if the container is locked!");
		}

	}

	//TODO Need to add functionality here.

	/** GETTERS AND SETTERS **/

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getToken() {
		return this.token;
	}

	@Override
	public Position getPosition() {
		return position;
	}

	@Override
	public void setCurrentPosition(Position position) {
		throw new Error("Cannot change positions of fixed container game objects!");
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public void setDescription(String desc) {
		this.description = desc;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public int getSize() {
		return size;
	}

	/**
	 * Attempt to add the given item to the container.
	 * @param item the item to be added into the container.
	 * @return true if item is successfully added to the container. If there is not enough space in the container, return false.
	 */
	public boolean addToContainer(InventoryGO item) {
		if (item.getSize() > this.sizeRemaining) {
			return false;
		}

		this.contents.add(item);
		this.sizeRemaining = this.sizeRemaining - item.getSize();
		return true;
	}

	/**
	 * Get all of the items in the inventory. ClearContainer() method should be called after using/calling this method.
	 * @return
	 */
	public List<InventoryGO> getAllItems() {
		return this.contents;
	}

	/**
	 * Remove the given item from the container.
	 * @param item the item to remove.
	 * @return true if item is successfully removed, and false otherwise.
	 */
	public boolean removeItem(InventoryGO item) {
		this.sizeRemaining += item.getSize();
		return this.contents.remove(item);
	}

	/**
	 * Clear the contents of this container.
	 * Note, calling this method will mean you lose all inventory items that may be in the container.
	 */
	public void clearContainer() {
		this.sizeRemaining = this.size;
		this.contents.clear();
	}

	/**
	 * @return true if the container is empty and false otherwise.
	 */
	public boolean isEmpty() {
		return this.size == this.sizeRemaining;
	}

	/**
	 * The space that is left in the container..
	 * @return the size/space left in the container.
	 */
	public int getSizeRemaining() {
		return sizeRemaining;
	}

	public String getArea() {
		return area;
	}

	public String getKeyID() {
		return keyID;
	}

	public String getName() {
		return name;
	}
}
