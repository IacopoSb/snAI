package it.unibo.ai.didattica.competition.tablut.snai.ricerca;

import aima.core.search.adversarial.Game;
import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;
import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.GameAshtonTablut;
import it.unibo.ai.didattica.competition.tablut.domain.State;

/**
 * "Custom" implementation of AIMA Iterative Deepening MinMax search with Alpha-Beta Pruning.
 * Maximal computation time is specified in seconds.
 * This configuration redefines the method eval() using getUtility() method in {@link GameAshtonTablut}.
 *
 * @see aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch
 */
public class TablutSearch extends IterativeDeepeningAlphaBetaSearch<State, Action, State.Turn> {


    public TablutSearch(Game<State, Action, State.Turn> game, double utilMin, double utilMax, int time) {
        super(game, utilMin, utilMax, time);
    }

    /**
     * Method that estimates the value for states. This implementation returns the utility value for
     * terminal states and heuristic value for non-terminal states.
     *
     * @param state the current state
     * @param player the player who has to make the next move (turn)
     *
     * @return the score of this state (double)
     */
    @Override
    protected double eval(State state, State.Turn player) {
        // Needed to make heuristicEvaluationUsed = true, if the state evaluated isn't terminal
        super.eval(state, player);
        // Return heuristic value for the given state
        return game.getUtility(state, player);
    }

    /**
     * Overrided to print metrics.
     *
     * @param state the current state
     *
     * @return the chosen action
     */
    @Override
    public Action makeDecision(State state) {
        Action a = super.makeDecision(state);
        System.out.println("Info: searched " + getMetrics().get(METRICS_NODES_EXPANDED) + " nodes, reaching deph " + getMetrics().get(METRICS_MAX_DEPTH));
        return a;
    }

}
