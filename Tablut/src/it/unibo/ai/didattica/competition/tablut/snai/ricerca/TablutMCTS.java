package it.unibo.ai.didattica.competition.tablut.snai.ricerca;

import aima.core.search.adversarial.Game;
import aima.core.search.adversarial.MonteCarloTreeSearch;
import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.State;

public class TablutMCTS extends MonteCarloTreeSearch<State, Action, State.Turn> {

    public TablutMCTS(Game<State, Action, State.Turn> game, int iterations) {
        super(game, iterations);
    }
}
