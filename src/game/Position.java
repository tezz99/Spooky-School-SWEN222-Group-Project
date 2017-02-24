package game;

import java.io.Serializable;

/**
 * Holds X and Y coordinates as integers.
 * @author Pritesh R. Patel
 *
 */
public class Position implements Serializable {

	private static final long serialVersionUID = -2558803055346062860L;
	private final int posX;
	private final int posY;

	public Position(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
	}

	/* GETTERS AND SETTERS */
	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + posX;
		result = prime * result + posY;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (posX != other.posX)
			return false;
		if (posY != other.posY)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "X: " + this.posX + " Y: " + this.posY;
	}


}
