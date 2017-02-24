package ui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import game.InventoryGO;
import network.Client;

/**
 * Inventory panel is passed any changes to the inventory, and displays them. The user can scroll thru the inventory, allowing the user
 * to see all the items they hold
 *
 * @author Andy
 *
 */
public class InventoryPanel extends JPanel implements MouseListener, MouseMotionListener{
	//references to other panels
	private UIImageMap imageMap;
	private Client client;
	private OverlayPanel overlayPanel;
	private GameFrame home;
	//list of items that are in inventory
	private List<ItemDisplay> itemList;
	//list of items that are currently being displayed
	private List<ItemDisplay> itemsShown;
	final static Dimension boardSize = new Dimension(250,150);
	//Value to determine if highlighed panel needs to be displayed
	private boolean highlighted;
	//coordinates of where highlighted panel should be displayed
	private int highlightedX;
	private int highlightedY;
	//Highlighted Panel image
	private Image highLight;
	//Background image
	private Image background;
	//custom font
	private Font pixelFont;
	//Item imagemap
	private ItemImageMap itemMap;
	//Determines which item is currently being dragged, if any
	private int dragged = -1;
	//used when up/down buttons pressed
	private int level;


	public InventoryPanel(GameFrame home, UIImageMap imageMap, Client client, OverlayPanel overlayPanel){
		super(new GridLayout(3,5));
		this.home = home;
		//Image map
		this.imageMap = imageMap;
		//Client
		this.client = client;
		//Overlay Panel
		this.overlayPanel = overlayPanel;
		//creates new Item imagemap
		this.itemMap = new ItemImageMap();

		//Creates custom font
		try {
			pixelFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("slkscr.ttf"));
		} catch (Exception e) {}

		//creates new list to store all passed items
		itemList = new ArrayList<ItemDisplay>();
		//creates new list of which items to display
		itemsShown = new ArrayList<ItemDisplay>();
		//determines where in item list to start displaying items in itemsShown
		this.level = 0;
		this.setPreferredSize(boardSize);
		//assigns icon to Highlight panel
		highLight = imageMap.getImage("hi");
		//assigns icon to background
		background = imageMap.getImage("invBack");
		//adds MouseListener to panel
		addMouseListener(this);
		//adds motionlistener to panel
		addMouseMotionListener(this);

		//visibility
		setOpaque(false);
		setVisible(true);
		validate();
	}

	/**
	 * This method reduces the level value, when the up button is pressed
	 */
	public void upOne(){
		if((level-5) >= 0){ //if level can be safely increased
			level-=5; //increases level by 5
			processItems(); //reprocesses the items to be shown
			repaint();
		}
	}

	/**
	 * This method increases the level value, when the down button is pressed
	 */
	public void downOne(){
		if(level < (itemList.size()+5)){//if level can be safely increased
			level+=5; //increases level by 5
			processItems(); //reprocesses the items to be shown
			repaint();
		}
	}

	/**
	 * This method compares a passed list of inventory items, to the currently stored list
	 *
	 * @param items List of items passed by bundle
	 * @return if there are any changes
	 */
	public boolean itemsChanged(List<InventoryGO> items){
		if(items.size() != itemList.size())  //if the passed inventory is bigger or smaller
			return true;

		return false; //no changes were made
	}

	/**
	 * Changes list of items to new list of items, if it is a different list of items
	 *
	 * @param items Items to check, and add
	 */
	public void addItems(List<InventoryGO> items){
		if(items != null){ //if list isn't null
			if(itemsChanged(items)){ //if there are actual changes
				itemList.clear(); //removes currently stored items
				for(InventoryGO item : items){ //for each item in new list
					itemList.add(new ItemDisplay(item)); //adds item to inventory to display
				}
				processItems(); //displays items on panel
				repaint();
			}
		}
	}
	/**
	 * Place items onto correct x,y coordinates, of the panel, emulating a 5x3 grid. This is determined by the level modifier,
	 * which selects where in items to start
	 */
	public void processItems(){
		//removes currently shown item coordinates
		for(ItemDisplay item : itemsShown){
			item.removeDisplay();
		}
		//remove all items from itemsShown
		itemsShown.clear();

		//gets level
		int i = level;

		ItemDisplay toAdd = null;
		for(int j = 0; j < 3; j++){ //For each row
			for(int k = 0; k < 5; k++){ //for each column
				//ensure we can never try access a non-existant item
				if(i >= itemList.size()){
					return;
				}
				//gets item from item list
				toAdd = itemList.get(i);
				//sets coordinate for item
				toAdd.setDisplay(k*50, j*50);
				//adds item to itemsshown
				itemsShown.add(toAdd);
				i++;
			}
		}

	}
	/**
	 * Highlight a particular square of the InventoryPanel, by getting the x,y coordinate of the mouse and displaying the
	 * highlight image over that square
	 *
	 * @param x x Coordinate
	 * @param y y coordinate
	 */
	public void highlight(int x, int y){
		highlighted = true;
		highlightedX = x*50;
		highlightedY = y*50;
		repaint();
	}
	/**
	 * Remove any highlighting (when mouse moves off InventoryPanel)
	 */
	public void unhighlight(){
		highlighted = false;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		//sets font
		g.setFont(pixelFont.deriveFont(Font.TRUETYPE_FONT, 10f));
		//sets colour
		g.setColor(Color.BLACK);
		//draws background of inventory panel
		g.drawImage(background, 0, 0, background.getWidth(null), background.getHeight(null), null);
		//if a square is highlighted
		if(highlighted){
			//draws the highlighted panel square
			g.drawImage(highLight, highlightedX, highlightedY, highLight.getWidth(null), highLight.getHeight(null), null);
		}

		Image image = null;
		ItemDisplay item = null;
		Image panel = null;

		//attempt to draw items shown
		for(int i = 0; i < 15; i++){
			if(i >= itemsShown.size()) //if index is every out of bounds, stop
				break;
			//gets next item shown
			item = itemsShown.get(i);
			//gets image of that item
			image = itemMap.getImage(item.getToken());

			//if item is not currently being dragged by user
			if(dragged != i){
				//draw item in correct panel
				g.drawImage(image, item.getX(), item.getY(), image.getWidth(null), image.getHeight(null), null);
				//if item is a ContainerGO item
				if(item.isContainer()){
					//draw the container size panel
					panel = imageMap.getImage("invPanel");
				} else { //otherwise
					//draw the normal item size panel
					panel = imageMap.getImage("invPanel2");
				}
				//draw the panel
				g.drawImage(panel, item.getX(), item.getY()+39, panel.getWidth(null), panel.getHeight(null), null);
				//display the items size on this panel
				g.drawString(item.getSize(), item.getX()+2, item.getY()+48);
			}
		}
		//if a item is currently being dragged by user
		if(dragged != -1){
			//get item image of that item
			Image dragImage = itemMap.getImage(itemsShown.get(dragged).getToken());
			//draw the item based on mouse x,y coordinates
			g.drawImage(dragImage, itemsShown.get(dragged).getTempX(), itemsShown.get(dragged).getTempY(), image.getWidth(null), image.getHeight(null), null);
		}
	}
	/**
	 * Creates a new popup menu object and displays it based on mouses x,y coordinates
	 *
	 * @param e The Mouse
	 * @para item the item that was clicked on
	 */
	private void doPop(MouseEvent e, ItemDisplay item){
		PopUpMenu menu = new PopUpMenu(item);
		menu.show(e.getComponent(), e.getX(), e.getY());
	}

	@Override
	public void mouseDragged(MouseEvent e) { //when user drags mouse
		if(home.getInteract()){
			highlightedX=(e.getX()/50)*50; //updates highlighted panel X value
			highlightedY=(e.getY()/50)*50; //updates highlighted panel Y value
			if(dragged != -1){ //if an item is currently being dragged
				itemsShown.get(dragged).setTemp(e.getX()-25, e.getY()-25); //updates that item's temporary x,y coordinates
			}
			repaint();
		}
	}
	@Override
	public void mouseMoved(MouseEvent e) { //when user moves mouse
		if(home.getInteract()){
			highlightedX=(e.getX()/50)*50; //updates highlighted panel X value
			highlightedY=(e.getY()/50)*50; //updates highlighted panel Y value
			repaint();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) { //when user clicks mouse
		if(home.getInteract()){
			if(SwingUtilities.isRightMouseButton(e)){ //if right mouse button
				int index = e.getX()/50 + ((e.getY()/50)*5); //gets square that user clicked on

				if(itemsShown.size() > index && index >= 0){//if item is there
					doPop(e, itemsShown.get(index)); //creates popup menu based on item and location
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) { //when mouse is pressed
		if(home.getInteract()){

			int index = e.getX()/50 + ((e.getY()/50)*5); //gets square that user pressed mouse on on

			if(itemsShown.size() > index && index >= 0 && !SwingUtilities.isRightMouseButton(e)){ //if item is there and user didn't press right mouse button
				dragged = index; //assigns that item to dragged value

				//sets that item to being dragged
				itemsShown.get(dragged).changeDragging();
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) { //when user releases mouse
		if(home.getInteract()){
			//ID's of items
			String firstID = "";
			String secondID = "";

			//if Item is being dragged
			if(dragged != -1){
				firstID += itemsShown.get(dragged).getID(); //sets firstID to id of item that was dragged
				int secondIndexs = e.getX()/50 + ((e.getY()/50)*5); //gets square that user pressed mouse on on

				//checks that secondIndex corresponds to an item or not
				if(itemsShown.size() > secondIndexs && secondIndexs >= 0)
					secondID += itemsShown.get(secondIndexs).getID(); //sets secondID to the item id that was found
				itemsShown.get(dragged).changeDragging();

				dragged = -1; //no item is being dragged now

			}
			if(!firstID.equals("") && !secondID.equals("")){ //if method found 2 ID.
				client.sendCommand("PACK "+secondID+" "+firstID); //sends Pack method to client
			}
			repaint();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) { //when mouse moves into a panel
		if(home.getInteract()){
			highlightedX=(e.getX()/50)*50; //updates highlighted panel X value
			highlightedY=(e.getY()/50)*50; //updates highlighted panel Y value
			highlighted = true; //tells class that a panel must be highlighted
			repaint();
		}
	}
	@Override
	public void mouseExited(MouseEvent e) { //when mouse leaves panel
		if(home.getInteract()){
			highlighted = false; //tells class that no panel must be highlighted now

			if(dragged != -1){ //if any item is being dragged
				itemsShown.get(dragged).changeDragging(); //tells that item that it is no longer being dragged
				dragged = -1; //tells class no item is being dragged
			}

			repaint();
		}
	}
	/**
	 * Popupmenu will be a menu when a player right clicks on an inventory item
	 *
	 * @author Andy
	 *
	 */
	class PopUpMenu extends JPopupMenu {
		private JMenuItem inspect;
		private JMenuItem drop;
		private JMenuItem unpack;
		private JMenuItem pass;

		public PopUpMenu(ItemDisplay item){

			ActionListener popUpListener = new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					if(event.getActionCommand().equals("Inspect")){
						overlayPanel.setFooterMessage(item.getDescription());
					} else if(event.getActionCommand().equals("Drop")){
						client.sendCommand("DROP "+item.getID());
					} else if (event.getActionCommand().equals("Open")){
						client.sendCommand("OPEN "+item.getID());
					} else if (event.getActionCommand().equals("Pass")){
						client.sendCommand("PASS "+item.getID());
					} else if (event.getActionCommand().equals("Unpack")){
						client.sendCommand("UNPACK "+item.getID());
					}
				}

			};

			try {
				inspect = new JMenuItem("Inspect");
				drop = new JMenuItem("Drop");
				pass = new JMenuItem("Pass");
				unpack = new JMenuItem("Unpack");

				Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("slkscr.ttf"));
				inspect.setFont(font.deriveFont(Font.TRUETYPE_FONT, 13f));
				drop.setFont(font.deriveFont(Font.TRUETYPE_FONT, 13f));
				pass.setFont(font.deriveFont(Font.TRUETYPE_FONT, 13f));
				unpack.setFont(font.deriveFont(Font.TRUETYPE_FONT, 13f));
			} catch (Exception e) {}


			inspect.addActionListener(popUpListener);
			add(inspect);

			drop.addActionListener(popUpListener);
			add(drop);

			pass.addActionListener(popUpListener);
			add(pass);

			if(item.isContainer()){
				unpack.addActionListener(popUpListener);
				add(unpack);
			}
		}
	}
}