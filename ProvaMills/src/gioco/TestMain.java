package gioco;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import aima.core.search.adversarial.AdversarialSearch;
import aima.core.search.adversarial.AlphaBetaSearch;
import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;
import aima.core.search.adversarial.MinimaxSearch;

public class TestMain {

	public static void main(String[] args){
		MillsGame gioco=new MillsGame();
		MillsState init=gioco.getInitialState();
		
		

		AdversarialSearch<MillsState, MillsAction> search;
		MillsAction action;

		//search = MinimaxSearch.createFor(gioco);

		//search = AlphaBetaSearch.createFor(gioco);

		search = IterativeDeepeningAlphaBetaSearch.createFor(gioco, -1000,1000, 1000*60);

		//search = IterativeDeepeningAlphaBetaSearch.createFor(gioco, 0.0,1.0, 1000);
		//((IterativeDeepeningAlphaBetaSearch<?, ?, ?>) search).setLogEnabled(true);
		
		action = search.makeDecision(init);
		MillsState stato=gioco.getResult(init, action);
		System.out.println(action);
		//System.out.println(stato);
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		MillsState previousState = stato;
		previousState.reset(8);
		
		do{
			/*MillsState newState = previousState.reverse();
			action=search.makeDecision(newState);
			System.out.println(action);
			previousState=gioco.getResult(newState, action);
			previousState.reset(7);*/
			//System.out.println(newState);
			
			System.out.println("Inserisci prossima mossa: ");
			try {
				String line = in.readLine();
				StringTokenizer st = new StringTokenizer(line);
				int ringFrom = Integer.parseInt(st.nextToken());
				int posFrom = Integer.parseInt(st.nextToken());
				int ringTo = Integer.parseInt(st.nextToken());
				int posTo = Integer.parseInt(st.nextToken());
				int ringDelete = Integer.parseInt(st.nextToken());
				int posDelete = Integer.parseInt(st.nextToken());
				
				MillsAction posring = new MillsAction(ringFrom, posFrom, ringTo, posTo, ringDelete, posDelete);
				MillsState newState = previousState.performAction(posring);
				
				action=search.makeDecision(newState);
				System.out.println(action);
				previousState=gioco.getResult(newState, action);
				previousState.reset(8);
				
			} catch (Exception e) {
				System.out.println("Non ho capito.");
				continue;
			}
		} while(!previousState.isTerminal());
	}
}
