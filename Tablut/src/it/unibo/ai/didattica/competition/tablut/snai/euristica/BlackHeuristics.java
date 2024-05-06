package it.unibo.ai.didattica.competition.tablut.snai.euristica;

import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.util.Arrays;

public class BlackHeuristics extends Heuristics {

    private final int WHITE_EATEN = 0; // White pawns already eaten
    private final int BLACK_ALIVE = 1; // Black pawns still alive
    private final int BLACK_SUR_K = 2; // Black pawns surrounding the king
    private final int RHOMBUS_POS = 3; // Formation to prevent the king from escaping
    private final int BLOCKED_ESC = 3; // Pawns that block king escapes

    private final double PAWNS_AGGRESSION_WEIGHT = 2.0;


    // Number of tiles in rhombus
    private final int TILES_IN_RHOMBUS = 8;

    private final Double[] earlyGameWeights;
    private final Double[] midGameWeights;
    private final Double[] lateGameWeights;
    private final int NUMWHITE = 8;
    private final int NUMBLACK = 16;
    

    // Matrix of favourite black positions in the initial stages to block the escape ways
    private final int[][] rhombus = {
                  {1,2},       {1,6},
            {2,1},                   {2,7},

            {6,1},                   {6,7},
                  {7,2},       {7,6}
    };

    public BlackHeuristics(State state) {
        super(state);

        earlyGameWeights = new Double[4];
        earlyGameWeights[WHITE_EATEN] = 45.0;
        earlyGameWeights[BLACK_ALIVE] = 35.0;
        earlyGameWeights[BLACK_SUR_K] = 15.0;
        earlyGameWeights[RHOMBUS_POS] = 5.0;
        
        midGameWeights = new Double[4];
        midGameWeights[WHITE_EATEN] = 42.5;
        midGameWeights[BLACK_ALIVE] = 32.5;
        midGameWeights[BLACK_SUR_K] = 20.0;
        midGameWeights[RHOMBUS_POS] = 2.5;
        midGameWeights[BLOCKED_ESC] = 2.5;

        lateGameWeights = new Double[4];
        lateGameWeights[WHITE_EATEN] = 40.0;
        lateGameWeights[BLACK_ALIVE] = 30.0;
        lateGameWeights[BLACK_SUR_K] = 25.0;
        lateGameWeights[BLOCKED_ESC] = 5.0;
    }

    /**
     * @return The valuation of the current state using a weighted sum.
     */
    @Override
    public double evaluateState() {
        int[] kingPos = kingPosition(state);

        double stateValue = 0.0;
        int gamePhase = 0;

        int numbOfWhite = state.getNumberOf(State.Pawn.WHITE);
        if (numbOfWhite > 4 && numbOfWhite <= 6)
            gamePhase = 1;
        else if (numbOfWhite <= 4)
            gamePhase = 2;

        // Values for the weighted sum
        double percBlackAlive = (double) state.getNumberOf(State.Pawn.BLACK) / NUMBLACK;
        double percWhiteEaten = (double) (NUMWHITE - numbOfWhite) / NUMWHITE;
        double percSurroundKing = (double) checkAdjacentPawns(state, kingPos, State.Turn.BLACK.toString()) / getNumbToEatKing(state);

        double possibleCatches = getPossibleCatches();
        if (getPossibleCatches() > 0)
        // Percentage of whites that blacks can eat
            stateValue += (possibleCatches / numbOfWhite) * PAWNS_AGGRESSION_WEIGHT;

        double pawnsInRhombus = (double) getRhombusValue() / TILES_IN_RHOMBUS;
        double blockingPawns = (double) blockingPawns();

        if (gamePhase == 0) { // Early Game

            stateValue += percWhiteEaten * earlyGameWeights[WHITE_EATEN];
            stateValue += percBlackAlive * earlyGameWeights[BLACK_ALIVE];
            stateValue += percSurroundKing * earlyGameWeights[BLACK_SUR_K];
            stateValue += pawnsInRhombus * earlyGameWeights[RHOMBUS_POS];

        } 
        else if (gamePhase == 1) { // Mid Game
            
            stateValue += percWhiteEaten * midGameWeights[WHITE_EATEN];
            stateValue += percBlackAlive * midGameWeights[BLACK_ALIVE];
            stateValue += percSurroundKing * midGameWeights[BLACK_SUR_K];
            stateValue += pawnsInRhombus * midGameWeights[RHOMBUS_POS];
            stateValue += blockingPawns * midGameWeights[BLOCKED_ESC];

        }
        else { // Late Game

            stateValue += percWhiteEaten * lateGameWeights[WHITE_EATEN];
            stateValue += percBlackAlive * lateGameWeights[BLACK_ALIVE];
            stateValue += percSurroundKing * lateGameWeights[BLACK_SUR_K];
            stateValue += blockingPawns * lateGameWeights[BLOCKED_ESC];

        }
        
        stateValue += canCaptureKing();

        return stateValue;
    }

    /**
     * @return The number of white pawns that can be captured.
     */
    private int getPossibleCatches() {
        int capturable = 0;

        State.Pawn[][] board = state.getBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].equalsPawn(State.Pawn.WHITE.toString())) {
                    capturable += canBeCaptured(state, new int[]{i, j}, State.Pawn.WHITE) ? 1 : 0;
                }
            }
        }

        return capturable;
    }

    /**
     * 
     * @return The number of black pawns on rhombus if there are at least a total of 10 black pawns, 0 otherwhise.
     */
    public int getRhombusValue() {
        if (state.getNumberOf(State.Pawn.BLACK) >= 10) {
            return blacksInRhombus();
        } else {
            return 0;
        }
    }

    /**
     * @return The number of black pawns on rhombus configuration.
     */
    public int blacksInRhombus() {
        int count = 0;

        for (int[] position : rhombus) {
            if (state.getPawn(position[0], position[1]).equalsPawn(State.Pawn.BLACK.toString())) {
                count++;
            }
        }
        return count;
    }

    /**
     * @return The number of pawns blocking king escape.
     */
    public int blockingPawns() {
        return 4 - Arrays.stream(getKingEscapes(state, kingPosition(state))).sum();
    }

    /**
     * Used to add value to the position if we create win threats.
     *
     * @return 10.0 if the king can be captured, 0.0 otherwise.
     */
    private double canCaptureKing() {
        if (canBeCaptured(state, kingPosition(state), State.Pawn.KING))
            return +10.0;

        return 0.0;
    }
}
