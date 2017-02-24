package game;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a container inventory object.
 * @author Pritesh R. Patel
 *
 */
public class ContainerGO extends InventoryGO {

	private static final long serialVersionUID = 5250695679285364004L;

	private List<InventoryGO> contents = new ArrayList<InventoryGO>();
	private final int size;
	private int sizeRemaining;

	public ContainerGO(String name, String id, String token, int size, String areaName, Position pos,
			String description) {

		super(name, id, token, size, areaName, pos, description);

		//Initially sizes are the same.
		this.size = size;
		this.sizeRemaining = size;

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

	@Override
	public int getSize() {
		return size;
	}

}
