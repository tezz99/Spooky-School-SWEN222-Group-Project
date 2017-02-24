package network;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import game.SpookySchool;
import ui.CreateServerPanel;

/**
 * Server actively listens for new connections until server is full. Then stops listening until a player leaves the game.
 * @author Rongji Wang
 * @author Pritesh R. Patel
 *
 */
public class Server extends Thread {

	private SpookySchool game;
	private int nclients = 4; //Number of connected that can still connect.
	private int port;
	private CreateServerPanel serverPanel;

	private boolean printFull = true; //To stop printing server full on the server print area multiple times

	public Server(Integer port, CreateServerPanel serverPanel) {
		this.game = new SpookySchool();
		this.port = port.intValue();
		this.serverPanel = serverPanel;
	}

	/**
	 * Run the server. Listen on the socket for new clients.
	 */
	@Override
	public void run() {
		this.serverPanel.printToTextPrintArea("Server Started: Listening for new clients");

		try {

			PlayerThread[] connections = new PlayerThread[nclients];

			ServerSocket serverSocket = new ServerSocket(port);
			this.serverPanel
					.updateServerStatusField("Server running on: " + InetAddress.getLocalHost().getHostAddress());

			this.displayAddresses();

			while (true) {

				Socket socket = serverSocket.accept(); // Wait for a socket/incoming connection.

				//Listen for new clients if new players can join.
				if (this.nclients > 0) {
					this.serverPanel.printToTextPrintArea("ACCEPTED CONNECTION FROM: " + socket.getInetAddress());
					this.serverPanel.printToTextPrintArea("Spots Avaliable on server: " + this.nclients);
					PlayerThread pT = new PlayerThread(socket, game, this.serverPanel); //Create the player thread
					this.addPlayerThread(pT, connections); //Add the player thread to the array of player threads that exists on this server.
					pT.start(); //Start the player thread.

				} else {
					if (this.printFull) {
						this.serverPanel.printToTextPrintArea("SERVER FULL"); //Print full if you haven't already.
						this.printFull = false;
					}
					socket.close();
				}

				this.removeDisconnectedPlayers(connections); //Remove all players that have been disconnected from this server.
			}

		} catch (IOException e) {
			this.serverPanel.updateServerStatusField("FAILED TO CREATE SERVER");
		}
	}

	/**
	 * Remove all players that have been disconnected. This allows more players to join.
	 * @param connections
	 */
	private void removeDisconnectedPlayers(PlayerThread... connections) {
		for (int i = 0; i < connections.length; i++) {
			PlayerThread pT = connections[i];
			if (pT != null && !pT.isAlive()) {
				connections[i] = null; //Clear this connection.
				nclients++; //More space available.
				this.serverPanel.printToTextPrintArea("Removed a client from server list.");
				this.printFull = true;
			}
		}
	}

	/**
	 * Add the player thread to the array of player threads running on the server.
	 * @param pT player thread to add to the connections array.
	 * @param connections Array of player threads running on the server.
	 */
	private void addPlayerThread(PlayerThread pT, PlayerThread[] connections) {

		for (int i = 0; i < connections.length; i++) {
			if (connections[i] == null) {
				connections[i] = pT; //Add this thread to player
				this.nclients--;
				return;
			}
		}
	}

	public void displayAddresses() throws SocketException {
		Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
		while (ifaces.hasMoreElements()) {
			NetworkInterface iface = ifaces.nextElement();
			Enumeration<InetAddress> addresses = iface.getInetAddresses();

			while (addresses.hasMoreElements()) {
				InetAddress addr = addresses.nextElement();
				if (addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
					this.serverPanel.printToTextPrintArea(addr.getHostAddress());
				}
			}
		}
	}

}
