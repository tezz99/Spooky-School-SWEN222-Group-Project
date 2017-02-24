package game;

/**
 * Clock Thread is a thread that is run in conjunction with the game. It is used to periodically call the tick method in the game
 * object which can make necessary changes such as movements of NPCs.
 * @author Pritesh R. Patel
 *
 */
public class ClockThread extends Thread {

	private final int delay = 300; //How often (in miliseconds) to move an npc.
	private final SpookySchool game;

	public ClockThread(SpookySchool game) {
		this.game = game;
	}

	@Override
	public void run() {

		long then = System.currentTimeMillis() + delay;

		//Loop for ever.
		while (true) {

			//Delay each move by npc by 3delay ms
			if (System.currentTimeMillis() > then) {
				game.moveNPC();
				then = System.currentTimeMillis() + delay;
			}

			game.checkNPCPath();

			//Extra 10 delay.
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				//Should never happen.
			}

		}
	}

}
