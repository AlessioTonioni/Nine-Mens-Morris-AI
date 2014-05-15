package gioco;

import java.util.List;

import aima.core.search.adversarial.Game;

public class MillsGame implements Game<MillsState, MillsAction, String> {

	@Override
	public List<MillsAction> getActions(MillsState currentState) {
		return currentState.getNextMoves();
	}

	@Override
	public MillsState getInitialState() {
		 //TODO da rivedere
		return new MillsState(1,8);
	}

	@Override
	public String getPlayer(MillsState arg0) {
		return ""+arg0.getTurno();
	}

	@Override
	public String[] getPlayers() {
		return new String[]{"1","-1"};
	}

	@Override
	public MillsState getResult(MillsState currentState, MillsAction action) {
		return currentState.performAction(action);
	}

	@Override
	public double getUtility(MillsState stato, String player) {
		return stato.getUtility();
	}

	@Override
	public boolean isTerminal(MillsState state) {
		return state.isTerminal();
	}

}
