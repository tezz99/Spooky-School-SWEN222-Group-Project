package game;

import java.io.Serializable;

/**
 * Represents a Game Object. Marker Interface.
 * @author Pritesh R. Patel
 *
 */
public interface GameObject extends Serializable {

	public String getToken();

	public String getId();

	public Position getPosition();

	public void setCurrentPosition(Position position);

	public String getDescription();

	public void setDescription(String desc);

}
