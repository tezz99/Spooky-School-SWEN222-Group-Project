package game;

import java.util.List;

/**
 * This class represents an NPC player.
 * @author Pritesh R. Patel
 *
 */
public class NonHumanPlayer extends Player {

	private List<String> directions; //Holds the list of directions the player will move.
	private int current = 0; //Used to iterate the directions list.

	public NonHumanPlayer(String playerName, String token, Area currentArea, Position currentPosition,
			List<String> directions) {

		super(playerName, null, currentArea, currentPosition);

		this.setToken(token);
		this.setDirection(directions.get(0)); //Set the default direction as the first direction in the list.
		this.directions = directions;
	}

	public String getPotentialDirection() {

		int next = current + 1;

		//Wrap around to the start of the directions list if we hit the end.
		if (next >= directions.size()) {
			next = 0;
		}
		return this.directions.get(next);
	}

	/**
	 * Used to increment current to the next item in the directions list. Current field will reset to zero once it hits the end of the list.
	 */
	public void directionMoved() {
		this.current++; //Increment current.
		//Reset current to zero once it hits the end of the directions list.
		if (this.current >= directions.size()) {
			this.current = 0;
		}
	}

	public String getCurrentDirection() {
		return this.directions.get(current);
	}

}
