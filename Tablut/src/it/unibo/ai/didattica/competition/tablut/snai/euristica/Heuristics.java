package it.unibo.ai.didattica.competition.tablut.snai.euristica;

import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public abstract class Heuristics {

    protected State state;

    public Heuristics(State state) {
        this.state = state;
    }

    public double evaluateState() {
        return 0;
    }

}

