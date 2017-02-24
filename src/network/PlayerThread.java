package network;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import game.Bundle;
import game.SpookySchool;
import ui.CreateServerPanel;

/**
 * A player thread is a thread that is created on the server for each player that connects and joins the game.
 * @author Rongji Wang
 * @author Pritesh R. Patel
 */
public class PlayerThread extends Thread {

	private Socket socket;
	private String playerName;
	private final SpookySchool game;
	private final int broadcastClock = 15;
	private DataInputStream input;
	private ObjectOutputStream objOut;
	private CreateServerPanel serverPanel;

	public PlayerThread(Socket socket, SpookySchool game, CreateServerPanel serverPanel) {
		this.socket = socket;
		this.game = game;
		this.serverPanel = serverPanel;
		this.serverPanel.printToTextPrintArea("New player Thread Created");
	}

	@Override
	public void run() {

		try {
			this.input = new DataInputStream(socket.getInputStream());

			boolean exit = false;
			while (!exit) {
				try {

					if (input.available() != 0) {
						//Number of bytes from the input stream
						int count = input.available();

						//Create buffer
						byte[] commandInBytes = new byte[count];

						//Read player data into the buffer
						input.read(commandInBytes);

						String command = "";

						//convert each byte in buffer to a char and add to string
						for (byte b : commandInBytes) {
							char c = (char) b;
							command = command + c;
						}

						//Process command if one is received.
						if (command.length() > 0) {
							this.processCommand(command);
						}
					}

					//If player exists in the game then transmit the bundle.
					if (!(this.game.getPlayer(this.playerName) == null)) {
						this.transmitBundle();//Transmit this player's bundle to client.
						Thread.sleep(this.broadcastClock); //Pause for a little bit.
					}

				} catch (InterruptedException e) {

				}
			}

			socket.close(); // release socket ... v.important!

		} catch (IOException e) {
			game.removePlayer(playerName);
			this.serverPanel.printToTextPrintArea("PLAYER " + playerName + " DISCONNECTED");
			return;
		}
	}

	/**
	 * Transmits this player's bundle to the player client.
	 */
	public void transmitBundle() {
		try {
			this.objOut = new ObjectOutputStream(socket.getOutputStream());
			Bundle bundle = game.getBundle(playerName, true);
			objOut.writeObject(bundle);
			objOut.flush();

			//Clear the bundle now that it has been sent.
			if (bundle != null) {
				bundle.clearBundle();
			}

		} catch (IOException e) {
			//Close the socket
			try {
				this.serverPanel.printToTextPrintArea("Error transmitting bundle: closing socket!");
				this.socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Process the command that is received from the client by calling the respective method on the game.
	 * @param command
	 */
	public void processCommand(String command) {
		Scanner scan = new Scanner(command);
		while (scan.hasNext()) {
			String nextToken = scan.next();

			if (nextToken.equals("newPlayer")) {
				this.playerName = scan.next();
				if (this.game.addPlayer(playerName)) {
					this.serverPanel.printToTextPrintArea("New Player added to game: " + this.playerName);
				} else {
					this.serverPanel.printToTextPrintArea(
							"Player name already exists on server, waiting for new name on thread.");
					this.playerName = "-1"; // Set to negative one so that it doesn't send another player's bundle
					this.transmitBundle(); // Transmit a null bundle. Bundle will be null as long as no player with the name "-1" exists
				}

			} else if (nextToken.equals("NORTH") || nextToken.equals("SOUTH") || nextToken.equals("EAST")
					|| nextToken.equals("WEST")) {

				this.game.movePlayer(game.getPlayer(playerName), nextToken);

			} else if (nextToken.equals("ACTION")) {
				this.game.processAction(playerName); 

			} else if (nextToken.equals("DROP")) {
				this.game.processDrop(playerName, scan.next());

			} else if (nextToken.equals("PACK")) {
				this.game.addToContainer(playerName, scan.next(), scan.next());

			} else if (nextToken.equals("UNPACK")) {
				this.game.unpackContainer(playerName, scan.next());

			} else if (nextToken.equals("PASS")) {
				this.game.passItem(playerName, scan.next());

			} else if (nextToken.equals("CHAT")) {
				String message = "<" + this.playerName + "> " + scan.nextLine(); //Append the player name before the message
				this.game.addChatLogItemToAllBundles(message); //Add the message to all player's bundles so they can display in their chat window.

			} else if (nextToken.equals("SAVE")) {
				this.game.saveGame(playerName);
			}
		}
	}

}
