package it.unibo.ai.didattica.mulino.actions;

import it.unibo.ai.didattica.mulino.domain.State;

public class Phase1 {
	
	public static State applyMove(State currentState, Action genericAction, State.Checker checker)
		throws 	WrongPhaseException
				, PositionNotEmptyException
				, NullCheckerException
				, NoMoreCheckersAvailableException
				, WrongPositionException
				, TryingToRemoveOwnCheckerException
				, TryingToRemoveEmptyCheckerException
				, NullStateException
				, TryingToRemoveCheckerInTripleException {
		
		Phase1Action currentAction = null;
		if (genericAction instanceof Phase1Action)
			currentAction = (Phase1Action) genericAction;
		else
			System.exit(-2);
		
		// initial checks
		initialChecks(currentState, currentAction.getPutPosition(), checker);
		
		// generate the new State
		State result = new State();
		
		// replicate the current board
		result.getBoard().putAll(currentState.getBoard());

		// put the checker on the board
		result.getBoard().put(currentAction.getPutPosition(), checker);
		
		// update the checkers available to the players
		switch (checker) {
		case WHITE :
			result.setWhiteCheckers(result.getWhiteCheckers()-1);
			break;
		case BLACK :
			result.setBlackCheckers(result.getBlackCheckers()-1);
			break;
		default:
			throw new NullCheckerException();
		}
		
		// check if this move allows to remove an opponent checker
		if (hasCompletedTriple(result, currentAction.getPutPosition(), checker))
			removeOpponentChecker(result, checker, currentAction.getRemoveOpponentChecker());
		
		// set the phase
		if (result.getWhiteCheckers() == 0 && result.getBlackCheckers() == 0)
			result.setCurrentPhase(State.Phase.SECOND);
		else
			result.setCurrentPhase(State.Phase.FIRST);
		return result;
	}
	
	
	
	
	private static void initialChecks(State currentState, String position, State.Checker checker)
			throws 	NullStateException
			, WrongPhaseException
			, WrongPositionException
			, PositionNotEmptyException
			, NullCheckerException
			, NoMoreCheckersAvailableException {
		// initial checks
		if (currentState == null)
			throw new NullStateException();
		if (currentState.getCurrentPhase()!= State.Phase.FIRST)
			throw new WrongPhaseException(currentState.getCurrentPhase(), State.Phase.FIRST);
		if (currentState.getBoard().get(position) == null)
			throw new WrongPositionException(position);
		if (currentState.getBoard().get(position) != State.Checker.EMPTY)
			throw new PositionNotEmptyException(position);
		if (checker == null || checker == State.Checker.EMPTY)
			throw new NullCheckerException();
		switch (checker) {
			case WHITE :
				if (currentState.getWhiteCheckers()<=0)
					throw new NoMoreCheckersAvailableException(checker);
				break;
			case BLACK :
				if (currentState.getWhiteCheckers()<=0)
					throw new NoMoreCheckersAvailableException(checker);
				break;
			default:
				throw new NullCheckerException();
		}
	}
	
	
	private static boolean hasCompletedTriple(State newState, String position, State.Checker checker) {	
		if (isInVTriple(newState, position))
			return true;
		if (isInHTriple(newState, position))
			return true;		
		return false;
	}
	
	
	private static void removeOpponentChecker( State newState, State.Checker checker, String willRemovePosition)
			throws 	WrongPositionException
					, TryingToRemoveOwnCheckerException
					, TryingToRemoveEmptyCheckerException
					, TryingToRemoveCheckerInTripleException {
		if (newState.getBoard().get(willRemovePosition) == null)
			throw new WrongPositionException(willRemovePosition);
		if (newState.getBoard().get(willRemovePosition) == checker)
			throw new TryingToRemoveOwnCheckerException();
		if (newState.getBoard().get(willRemovePosition) == State.Checker.EMPTY)
			throw new TryingToRemoveEmptyCheckerException();
		
//		I pezzi allineati non possono essere eliminati finché ne esistono altri non allineati.
		if (isInVTriple(newState, willRemovePosition) || isInHTriple(newState, willRemovePosition)) {
			State.Checker opponent = (checker== State.Checker.WHITE) ? State.Checker.BLACK :State.Checker.WHITE;
			for (String s: newState.getBoard().keySet())
				if (newState.getBoard().get(s) == opponent &&
						!isInVTriple(newState,s) &&
						!isInHTriple(newState,s))
					throw new TryingToRemoveCheckerInTripleException(willRemovePosition, s);
		}
		newState.getBoard().put(willRemovePosition, State.Checker.EMPTY);
	}
	
	
	
	private static boolean isInVTriple(State aState, String position) {
		int alignedV = 0;
		String [] vSet = getVSet(position);
		for (String s: vSet)
			if (aState.getBoard().get(s) == aState.getBoard().get(position))
				alignedV++;
		if (alignedV == 3)
			return true;
		else
			return false;
	}
	
	private static boolean isInHTriple(State aState, String position) {
		int alignedH = 0;
		String [] hSet = getHSet(position);
		for (String s: hSet)
			if (aState.getBoard().get(s) == aState.getBoard().get(position))
				alignedH++;
		if (alignedH == 3)
			return true;
		else
			return false;
	}
	
	
	
	
	private static String[] getVSet(String pos) {
		String[] set = new String[3];
		char column = pos.charAt(0);
		char row = pos.charAt(1);
		if (column == 'a' || column == 'g') {
			set[0] = column + "1";
			set[1] = column + "4";
			set[2] = column + "7";
		}
		if (column == 'b' || column == 'f') {
			set[0] = column + "2";
			set[1] = column + "4";
			set[2] = column + "6";
		}
		if (column == 'c' || column == 'e') {
			set[0] = column + "3";
			set[1] = column + "4";
			set[2] = column + "5";
		}
		if (column == 'd' && row < '4') {
			set[0] = column + "1";
			set[1] = column + "2";
			set[2] = column + "3";
		}
		if (column == 'd' && row > '4') {
			set[0] = column + "5";
			set[1] = column + "6";
			set[2] = column + "7";
		}
		return set;
	}
	
	private static String[] getHSet(String pos) {
		String[] set = new String[3];
		char column = pos.charAt(0);
		char row = pos.charAt(1);
		// count over the rows:
		if (row == '1' || row == '7') {
			set[0] = "a" + row;
			set[1] = "d" + row;
			set[2] = "g" + row;
		}
		if (row == '2' || row == '6') {
			set[0] = "b" + row;
			set[1] = "d" + row;
			set[2] = "f" + row;
		}
		if (row == '3' || row == '5') {
			set[0] = "c" + row;
			set[1] = "d" + row;
			set[2] = "e" + row;
		}
		if (row == '4' && column < 'd') {
			set[0] = "a" + row;
			set[1] = "b" + row;
			set[2] = "c" + row;
		}
		if (row == '4' && column > 'd') {
			set[0] = "e" + row;
			set[1] = "f" + row;
			set[2] = "g" + row;
		}
		return set;
	}
	

}
