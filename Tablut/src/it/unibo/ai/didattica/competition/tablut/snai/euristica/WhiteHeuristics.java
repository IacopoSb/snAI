package it.unibo.ai.didattica.competition.tablut.snai.euristica;

import java.util.Arrays;

import it.unibo.ai.didattica.competition.tablut.domain.State;


public class WhiteHeuristics extends Heuristics{

    // Weights for the evaluation function
    private final int KING_DISTANCE = 6; // Distance of the king from the border
    private final int KING_CAPTURE = 80; // Number of black pawns around the king
    private final int WHITE_ALIVE = 100;  // Number of white pawns still alive
    private final int BLACK_EATEN = 120;  // Number of black pawns already eaten
    private final int WIN_PATH = 400;     // Number of winning paths for the king
   

    public WhiteHeuristics(State state) {
        super(state);

    }

    /**
     * @return the evaluation of the current state using a weighted sum
     */
    @Override
    public double evaluateState() {
        double stateValue = 
                    KING_DISTANCE      * (6 - kingDistanceFromWin()) / 6                                                       +
                    KING_CAPTURE       * (4 - checkAdjacentPawns(state, kingPosition(state), State.Turn.BLACK.toString()))/4   +
                    WHITE_ALIVE        * state.getNumberOf(State.Pawn.WHITE) / NUMWHITE                                        +
                    BLACK_EATEN        * (NUMBLACK - state.getNumberOf(State.Pawn.BLACK)) / NUMBLACK +
                    WIN_PATH           * Arrays.stream(getKingEscapes(state, kingPosition(state))).sum(); 
        
        if (state.getTurn().equals(State.Turn.WHITEWIN)) {
			stateValue = Double.POSITIVE_INFINITY;
		} else if (state.getTurn().equals(State.Turn.BLACKWIN)) {
			stateValue = Double.NEGATIVE_INFINITY;
		}
		if (Arrays.stream(getKingEscapes(state, kingPosition(state))).sum() == 2) {
			stateValue = Double.POSITIVE_INFINITY;
		}
        return stateValue;
    }

   
}
