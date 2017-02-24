package ui;

import game.ContainerGO;
import game.InventoryGO;

/**
 * ItemDisplay will contain an Item from the inventory as well as information regarding how it will be displayed on the inventory panel
 * 
 * @author Andy
 *
 */

public class ItemDisplay {
	//item
	private InventoryGO item;
	private ContainerGO containItem;
	//X and Y coordinates
	private int x;
	private int y;
	//X and Y coordinates when the item is being dragged
	private int tempX;
	private int tempY;
	//signals item is being dragged
	private boolean dragging;
	//signals item is being displayed currently
	private boolean display;
	//determines if item is a container object
	private boolean container;
	
	public ItemDisplay(InventoryGO item){
		//sets item
		this.item = item;
		//checks if item is a ContainerGO
		if (item instanceof ContainerGO){
			container = true; //sets container to true
			containItem = (ContainerGO) item; //casts item as container
		}
		//default coordinates
		x = 0;
		y = 0;
		//default drag and display values
		dragging = false;
		display = false;
	}
	
	/**
	 * This method returns the item size if it is an item, or a total size and items contained, if it is a container
	 * 
	 * @return Size value to be displayed
	 */
	public String getSize(){
		return ""+(!container ? item.getSize() : containItem.getSize()-containItem.getSizeRemaining()+"/"+containItem.getSize());
	}
	
	/**
	 * Returns if item is a container object
	 * 
	 * @return if item is container
	 */
	public boolean isContainer(){
		return this.container;
	}
	
	/**
	 * Sets the item's temporary X,Y coordinates when it is being dragged
	 * 
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public void setTemp(int x, int y){
		this.tempX = x;
		this.tempY = y;
	}
	
	//GETTER and SETTER METHODS
	
	public int getTempX(){
		return tempX;
	}
	
	public int getTempY(){
		return tempY;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}

	//GETTER METHODS FOR ITEM GETTERS 
	
	public String getName(){
		return item.getName();
	}
	
	public String getID(){
		return item.getId();
	}
	
	public String getToken(){
		return item.getToken();
	}
	
	public String getDescription(){
		return item.getDescription();
	}
	
	/**
	 * Called when item is first dragged, or when no longer being dragged
	 */
	public void changeDragging(){
		this.dragging = !this.dragging;
	}
	
	/**
	 * Tells item it is no longer being displayed
	 */
	public void removeDisplay(){
		this.display=false;
		setX(-100);
		setY(-100);
	}
	
	/**
	 * Tells item it is being displayed, and gives it coordinates to be displayed at 
	 * 
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	public void setDisplay(int x, int y){
		setX(x);
		setY(y);
		this.display=true;
	}
}
