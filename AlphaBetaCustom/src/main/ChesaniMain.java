package main;

import it.unibo.ai.didattica.mulino.actions.Action;
import it.unibo.ai.didattica.mulino.client.MulinoClient;
import it.unibo.ai.didattica.mulino.domain.State;
import it.unibo.ai.didattica.mulino.engine.TCPMulino;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import mills.MillsAction;
import node.Node;
import search.TimeoutAlphaBetaSearch;
import chesani.Chesanizer;

public class ChesaniMain {

	private static boolean white;
	private Socket playerSocket;
	private ObjectInputStream in;
	private ObjectOutputStream out;

	public ChesaniMain() throws UnknownHostException, IOException {
		int port = 0;
		if(white) {
			port = TCPMulino.whiteSocket;
		} else {
			port = TCPMulino.blackSocket;
		}
		
		playerSocket = new Socket("localhost", port);
		out = new ObjectOutputStream(playerSocket.getOutputStream());
		in = new ObjectInputStream(new BufferedInputStream(playerSocket.getInputStream()));
	}

	public void write(Action action) throws IOException, ClassNotFoundException {
		out.writeObject(action);
	}

	public State read() throws ClassNotFoundException, IOException {
		return (State) in.readObject();
	}

	public boolean getPlayer() { return white; }

	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {
		
		if (args.length==0) {
			System.out.println("You must specify which player you are (White or Black)!");
			System.exit(-1);
		}
		System.out.println("Selected client: " + args[0]);

		if ("White".equals(args[0]))
			white = true;
		else
			white = false;
		
		int time = Integer.parseInt(args[1]);
		
		Action action;
		State currentState = null;

		Chesanizer chesanizer = new Chesanizer(white);
		TimeoutAlphaBetaSearch search=new TimeoutAlphaBetaSearch();
		
		if (white) {
			MulinoClient client = new MulinoClient(State.Checker.WHITE);
			System.out.println("You are player white !");
			System.out.println("Current state:");
			currentState = client.read();
			
			while (true) {
				
				Node root=chesanizer.deChesanizer(currentState);
				MillsAction millsAction =(MillsAction) search.getNextMove(root, time);
				System.out.println(millsAction.toString());
				System.out.println("Player white do your move: ");
				
				action = chesanizer.chesanizeAction(millsAction, root.getState().getPhase());
				
				client.write(action);
				currentState = client.read();
				
				currentState = client.read();
			}
		}
		else {
			MulinoClient client = new MulinoClient(State.Checker.BLACK);
			currentState = client.read();
			System.out.println("You are player black !");
			System.out.println("Current state:");
			System.out.println(currentState.toString());
			while (true) {
				System.out.println("Waiting for your opponent move...");
				currentState = client.read();
				System.out.println("Player black do your move: ");
				
				Node root=chesanizer.deChesanizer(currentState);
				MillsAction millsAction =(MillsAction) search.getNextMove(root, time);
				System.out.println(millsAction);
				action = chesanizer.chesanizeAction(millsAction, root.getState().getPhase());
				
				client.write(action);
				currentState = client.read();
			}
		}

	}
}