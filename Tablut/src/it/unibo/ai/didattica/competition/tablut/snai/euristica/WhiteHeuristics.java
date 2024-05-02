package it.unibo.ai.didattica.competition.tablut.snai.euristica;

import it.unibo.ai.didattica.competition.tablut.domain.GameAshtonTablut;
import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.util.Arrays;

public class WhiteHeuristics extends Heuristics{


    public WhiteHeuristics(State state) {
        super(state);
    }

    /**
     * @return the evaluation of the current state using a weighted sum
     */
    @Override
    public double evaluateState() {
        double stateValue = 0.0;
        // TODO heuristic evaluation
        return stateValue;
    }

   
}
