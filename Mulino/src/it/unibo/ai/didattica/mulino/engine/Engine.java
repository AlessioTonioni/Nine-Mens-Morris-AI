package it.unibo.ai.didattica.mulino.engine;

import java.io.IOException;

import it.unibo.ai.didattica.mulino.actions.Action;
import it.unibo.ai.didattica.mulino.actions.Phase1;
import it.unibo.ai.didattica.mulino.domain.State;

public class Engine {

	private static State currentState = null;
	private static State.Checker currentPlayer;
	private static Action currentAction;
	private static int delay;
	
	private TCPMulino whiteSocket;
	private TCPMulino blackSocket;
	
	
	public Engine() {
		currentState = new State();
		currentPlayer = State.Checker.WHITE;
		delay = 60;
	}
	
	
	
	public void run() throws IOException {
		Thread t;
		System.out.println("Wating for connections...");
		
		whiteSocket = new TCPMulino(State.Checker.WHITE);
		whiteSocket.writeState(currentState);
		blackSocket = new TCPMulino(State.Checker.BLACK);
		blackSocket.writeState(currentState);		
		System.out.println(currentState.toString());
		
		TCPInput whiteRunner = new TCPInput(whiteSocket);
		TCPInput blackRunner = new TCPInput(blackSocket);
		TCPInput tin = null;
		
		while (currentState.getCurrentPhase() != State.Phase.SECOND) {
			System.out.println("Waiting for " + currentPlayer.toString() + " move...");
			switch (currentPlayer) {
				case WHITE:
					tin = whiteRunner;
					break;
				case BLACK:
					tin = blackRunner;
					break;
				default:
					System.exit(4);
			}
			t = new Thread(tin);
			t.start();
			
			try {
				int counter = 0;
				while (counter<delay && t.isAlive()) {
					Thread.sleep(1000);
					counter++;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (t.isAlive()) {
				System.out.println("Timeout!!!!");
				System.out.println("Player " + currentPlayer.toString() + " has lost!!!");
				System.exit(0);
			}
			try {
				System.out.println("Player " + currentPlayer.toString() + " move: ");
				currentState = Phase1.applyMove(currentState, currentAction, currentPlayer);
				whiteSocket.writeState(currentState);
				blackSocket.writeState(currentState);
				System.out.println(currentState.toString());
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
			currentPlayer = (currentPlayer== State.Checker.WHITE) ? State.Checker.BLACK : State.Checker.WHITE;
		}
		
		System.out.println("Game finished! State:");
		System.out.println(currentState.toString());
	}
	
	
	
	
	public static void main(String[] args) throws IOException {
		Engine eee = new Engine();
		eee.run();
	}
	
	
	
	private class TCPInput implements Runnable {
		private TCPMulino theSocket;
		public TCPInput (TCPMulino theSocket) { this.theSocket = theSocket; }
		
		public void run() {
			try {
				currentAction = theSocket.readAction();
			} catch (Exception e) { }
		}
	}
	
	
	
}
