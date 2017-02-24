package network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import game.Bundle;
import ui.GameFrame;
import ui.JoinServerPanel;

/**
 * Client handles the communication with the server. It creates a new game window once a connection with the game has been established.
 * The client sends commands to the server and receives bundles which hold key information about the game.
 * @author Rongji Wang
 * @author Pritesh R. Patel
 *
 */
public class Client extends Thread {

	private Socket socket;
	private String playerName;
	private DataOutputStream output;
	private ObjectInputStream objInput;
	private GameFrame gameFrame; //Used to call a processBundle method on the frame.
	private JoinServerPanel joinServerPanel;

	public Client(String playerName, Socket socket, JoinServerPanel joinServerPanel) {
		this.playerName = playerName;
		this.socket = socket;
		this.joinServerPanel = joinServerPanel;
	}

	@Override
	public void run() {

		try {
			this.output = new DataOutputStream(socket.getOutputStream());
			this.sendCommand("newPlayer " + this.playerName); //Send command to add player to game on server.

			boolean exit = false;
			while (!exit) {

				//Receive and read the bundle.
				objInput = new ObjectInputStream(socket.getInputStream());
				Bundle bundle = (Bundle) objInput.readObject();

				//Bundle can only be null if a player doesn't exist in the game on the server.
				//Try to send playerName to get added into the game on the server. Bundle received will be null until player is added.
				if (bundle == null) {
					this.joinServerPanel.updateServerStatusField("Player Name Already Taken");
					this.joinServerPanel.askForNewName(); //Ask the user for a new name.
					continue; //Skip this iteration. player does not exist yet so cannot continue down...
				}

				this.joinServerPanel.hideJoinWindow(); //Connection with server and game properly established so hide join window.

				//Create a game frame if haven't already.
				if (this.gameFrame == null) {
					System.out.println("Creating game frame.");
					this.gameFrame = new GameFrame("Spooky School - " + this.playerName, this, playerName); //Valid player has been added to game on server end so show game frame.
				}

				this.gameFrame.processBundle(bundle); //Send bundle to gameFrame to process and display appropriately.

			}

			socket.close();

		} catch (IOException | ClassNotFoundException e) {

			//Close the socket and close game window.
			try {
				this.joinServerPanel.updateServerStatusField("ERROR: Client socket closed.");
				this.socket.close();

				if (this.gameFrame != null) {
					this.gameFrame.disconnected();
				}

			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Send a command to the Server which will then process it.
	 * @param command that the sever needs to process.
	 *
	 */
	public void sendCommand(String command) {
		try {
			//Pass command to the player thread.
			this.output.write(command.getBytes());
			this.output.flush();

		} catch (IOException e) {
			this.joinServerPanel.updateServerStatusField("Server is full. Restart to try again.");
			//System.exit(0);
		}
	}

	/**
	 * Set the player name. Should be used when server already has player name.
	 * @param name the name to set this player to.
	 */
	public void setPlayerName(String name) {

		this.playerName = name;
	}

	/**
	 * Closes the socket connected to the server. Used to close socket when closing client ui window.
	 */
	public void closeSocket() {
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
