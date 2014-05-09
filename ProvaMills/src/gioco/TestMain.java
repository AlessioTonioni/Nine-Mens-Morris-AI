package gioco;

import aima.core.search.adversarial.AdversarialSearch;
import aima.core.search.adversarial.AlphaBetaSearch;
import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;
import aima.core.search.adversarial.MinimaxSearch;

public class TestMain {

	public static void main(String[] args){
		MillsGame gioco=new MillsGame();
		MillsState init=gioco.getInitialState();
		
		

		AdversarialSearch<MillsState, PosRing> search;
		PosRing action;

		//search = MinimaxSearch.createFor(gioco);

		search = AlphaBetaSearch.createFor(gioco);

		//search = IterativeDeepeningAlphaBetaSearch.createFor(game, 0.0,1.0, 1000);

		//search = IterativeDeepeningAlphaBetaSearch.createFor(gioco, 0.0,1.0, 1000);
		//((IterativeDeepeningAlphaBetaSearch<?, ?, ?>) search).setLogEnabled(true);

		/*action = search.makeDecision(init);
		System.out.println(action);
		MillsState primaMossa=gioco.getResult(init, action);
		action=search.makeDecision(primaMossa);
		System.out.println(action);
		MillsState secondaMossa=gioco.getResult(init, action);
		action=search.makeDecision(secondaMossa);
		System.out.println(action);*/
		
		action = search.makeDecision(init);
		MillsState stato=gioco.getResult(init, action);
		System.out.println(action);
		System.out.println(stato);
		
		MillsState previousState = stato;
		for(int i=0; i<20; i++){
			
			MillsState newState = previousState.reverse();
			action=search.makeDecision(newState);
			System.out.println(action);
			previousState=gioco.getResult(newState, action);
			System.out.println(newState);
		}
	}
}
