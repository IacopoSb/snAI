package it.unibo.ai.didattica.competition.tablut.snai.euristica;

import it.unibo.ai.didattica.competition.tablut.domain.State;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public abstract class Heuristics {

    protected State state;

    // position of camps
    private final int[][] camps = {
                        {0,3}, {0,4}, {0,5},
                               {1,4},

            {3,0},                                 {3,8},
            {4,0}, {4,1},      {4,4},       {4,7}, {4,8},
            {5,0},                                 {5,8},

                               {7,4},
                        {8,3}, {8,4}, {8,5},
    };
    
    
    protected final int TILES_IN_RHOMBUS = 8;
    private final int[][] rhombus = {
            {1,2},       {1,6},
    {2,1},                   {2,7},

    {6,1},                   {6,7},
            {7,2},       {7,6}
    };

    private final int[][] winPosition = {
            {0,1}, {0,2},        {0,6}, {0,7},
    {1,0},                                    {1,8},
    {2,0},                                    {2,8},

    {6,0},                                    {6,8},
    {7,0},                                    {7,8},
            {8,1}, {8,2},        {8,6}, {8,7}
    };
    
    
    protected final int NUMWHITE = 8;
    protected final int NUMBLACK = 16;


    public Heuristics(State state) {
        this.state = state;
    }

    public double evaluateState() {
        return 0;
    }

    /**
     * @return the position of the King
     */
    public int[] kingPosition(State state) {
        int[] pos = new int[2];

        State.Pawn[][] board = state.getBoard();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (state.getPawn(i, j).equalsPawn("K")) {
                    pos[0] = i;
                    pos[1] = j;

                    return pos;
                }
            }
        }
        return pos;
    }

    /**
     * @return true if king is on throne, false otherwise
     */
    public boolean isKingOnThrone(State state){
        return state.getPawn(4, 4).equalsPawn("K");
    }

    /**
     * @return the number of adjacent pawns that are target(BLACK, WHITE or King)
     */
    public int checkAdjacentPawns(State state, int[] pos, String target) {
        int count = 0;

        State.Pawn[][] board = state.getBoard();
        if (board[pos[0]-1][pos[1]].equalsPawn(target))
            count++;
        if (board[pos[0]+1][pos[1]].equalsPawn(target))
            count++;
        if (board[pos[0]][pos[1]-1].equalsPawn(target))
            count++;
        if (board[pos[0]][pos[1]+1].equalsPawn(target))
            count++;
        return count;
    }

    /**
     * @return the number of adjacent squares the king can move in
     */
    public int getKingMovement(State state, int[] pos) {
        int count = 4;

        int[] space = new int[]{pos[0]-1,pos[1]};
        // vertical checks
        if (isPositionOccupied(state, space) || Arrays.stream(camps).anyMatch(camp -> Arrays.equals(camp, space)))
            count--;
        space[0] = pos[0] + 1;
        if (isPositionOccupied(state, space) || Arrays.stream(camps).anyMatch(camp -> Arrays.equals(camp, space)))
            count--;

        // horizontal checks
        space[0] = pos[0];
        space[1] = pos[1] - 1;
        if (isPositionOccupied(state, space) || Arrays.stream(camps).anyMatch(camp -> Arrays.equals(camp, space)))
            count--;
        space[1] = pos[1] + 1;
        if (isPositionOccupied(state, space) || Arrays.stream(camps).anyMatch(camp -> Arrays.equals(camp, space)))
            count--;

        return count;
    }


    /**
     * @return true if a camp is adjacent to pos
     */
    public boolean checkAdjacentCamp(int[] pos){
        if (Arrays.stream(camps).anyMatch(camp -> Arrays.equals(camp, new int[]{pos[0]-1,pos[1]})))
            return true;
        if (Arrays.stream(camps).anyMatch(camp -> Arrays.equals(camp, new int[]{pos[0]+1,pos[1]})))
            return true;
        if (Arrays.stream(camps).anyMatch(camp -> Arrays.equals(camp, new int[]{pos[0],pos[1]-1})))
            return true;
        if (Arrays.stream(camps).anyMatch(camp -> Arrays.equals(camp, new int[]{pos[0],pos[1]+1})))
            return true;
        return false;
    }

    /**
     * @return the adjacent positions occupied by target
     */
    protected List<int[]> adjacentPositionsOccupied(State state,int[] position, String target){
        List<int[]> occupiedPositions = new ArrayList<int[]>();
        int[] pos = new int[2];

        State.Pawn[][] board = state.getBoard();
        if (board[position[0]-1][position[1]].equalsPawn(target)) {
            pos[0] = position[0] - 1;
            pos[1] = position[1];
            occupiedPositions.add(pos);
        }
        if (board[position[0]+1][position[1]].equalsPawn(target)) {
            pos[0] = position[0] + 1;
            pos[1] = position[1];
            occupiedPositions.add(pos);
        }
        if (board[position[0]][position[1]-1].equalsPawn(target)) {
            pos[0] = position[0];
            pos[1] = position[1] - 1;
            occupiedPositions.add(pos);
        }
        if (board[position[0]][position[1]+1].equalsPawn(target)) {
            pos[0] = position[0];
            pos[1] = position[1] + 1;
            occupiedPositions.add(pos);
        }

        return occupiedPositions;
    }

    /**
     * @return true if king is adjacent
     */
    protected boolean checkAdjacentKing(State state, int[] position) {
        return checkAdjacentPawns(state, position, "K") > 0;
    }

    /**
     * @return true if king is on a center tile
     */
    public boolean isKingNearThrone(State state,int[] kingPosition) {
        return (kingPosition[0] > 2 && kingPosition[0] < 6 && kingPosition[1] > 2 && kingPosition[1] < 6);
    }

    /**
     * @return the escapes which king can reach in the following order [ up, down, left, right ]: 1 if can escape, 0 if not
     */
    public int[] getKingEscapes(State state, int[] kingPosition) {
        int[] escapes = new int[4];
        
        if (!isKingNearThrone(state, kingPosition)) {
            if ((!(kingPosition[1] > 2 && kingPosition[1] < 6)) && (!(kingPosition[0] > 2 && kingPosition[0] < 6))) {
                int[] tempV = countFreeColumn(state, kingPosition);
                int[] tempH = countFreeRow(state, kingPosition);
                escapes[0] = tempV[0];
                escapes[1] = tempV[1];
                escapes[2] = tempH[0];
                escapes[3] = tempH[1];
            }
            if ((kingPosition[1] > 2 && kingPosition[1] < 6)) {
                int[] tempH = countFreeRow(state, kingPosition);
                escapes[2] = tempH[0];
                escapes[3] = tempH[1];
            }
            if ((kingPosition[0] > 2 && kingPosition[0] < 6)) {
                int[] tempV = countFreeColumn(state, kingPosition);
                escapes[0] = tempV[0];
                escapes[1] = tempV[1];
            }
            return escapes;
        }

        return escapes;
    }

    /**
     * @return free rows from given position [ left, right ]: 1 if free, 0 if not
     */
    public int[] countFreeRow(State state, int[] position) {
        int row = position[0];
        int column = position[1];
        int[] currentPosition = new int[2];
        int[] freeWays = new int[2];

        freeWays[0] = 1;
        freeWays[1] = 1;

        currentPosition[0] = row;
        // left side
        for (int i = column-1; i >= 0; i--) {
            currentPosition[1] = i;
            if (isPositionOccupied(state, currentPosition) || Arrays.stream(camps).anyMatch(camp -> Arrays.equals(camp, currentPosition))){
                freeWays[0] = 0;
                break;
            }
        }

        // right side
        for (int i = column+1; i <= 8; i++) {
            currentPosition[1] = i;
            if (isPositionOccupied(state, currentPosition) || Arrays.stream(camps).anyMatch(camp -> Arrays.equals(camp, currentPosition))) {
                freeWays[1] = 0;
                break;
            }
        }

        return freeWays;
    }

    /**
     * @return number of free columns from given position [ up, down ]: 1 if free, 0 if not
     */
    public int[] countFreeColumn(State state,int[] position){
        int row = position[0];
        int column = position[1];
        int[] currentPosition = new int[2];
        int[] freeWays = new int[2];

        freeWays[0] = 1;
        freeWays[1] = 1;

        currentPosition[1]=column;
        // upside
        for(int i=row-1; i>=0; i--) {
            currentPosition[0]=i;
            if (isPositionOccupied(state, currentPosition) || Arrays.stream(camps).anyMatch(camp -> Arrays.equals(camp, currentPosition))){
                freeWays[0] = 0;
                break;
            }
        }

        // downside
        for(int i=row+1; i<=8; i++) {
            currentPosition[0]=i;
            if (isPositionOccupied(state, currentPosition) || Arrays.stream(camps).anyMatch(camp -> Arrays.equals(camp, currentPosition))) {
                freeWays[1] = 0;
                break;
            }
        }

        return freeWays;
    }

    /**
     * @return true if a position is occupied, false otherwise
     */
    public boolean isPositionOccupied(State state,int[] position){
        return !state.getPawn(position[0], position[1]).equals(State.Pawn.EMPTY);
    }


    /**
     * @return number of black bad people needed to eat king in the current state
     */
    public int getNumbToEatKing(State state){
        int[] kingPosition = kingPosition(state);

        if (kingPosition[0] == 4 && kingPosition[1] == 4){
            return 4;
        } else if (isKingNearThrone(state, kingPosition)) {
            return 3;
        } else if (checkAdjacentCamp(kingPosition)) {
            return 1;
        } else {
            return 2;
        }
    }

    /**
     * @return number of Target pawns in given quadrant
     */
    public int getQuadrantCount(State state, State.Pawn target, int quadrant) {
        int[] rowRange = new int[2];
        int[] columnRange = new int[2];
        int count = 0;

        if (quadrant == 1 || quadrant == 2) {
            rowRange[0] = 0;
            rowRange[1] = 3;
        } else {
            rowRange[0] = 5;
            rowRange[1] = 9;
        }

        if(quadrant == 1 || quadrant == 3) {
            columnRange[0] = 0;
            columnRange[1] = 3;
        } else {
            columnRange[0] = 5;
            columnRange[1] = 9;
        }

        for(int i = rowRange[0]; i <= rowRange[1]; i++) {
            for(int j = columnRange[0] ; j <= columnRange[1]; j++) {
                if(state.getBoard()[i][j].equalsPawn(target.toString()))
                    count++;
            }
        }

        return count;
    }

    /**
     * @param state the current state of the board
     * @param enemy color of the opposite pawn
     * @param position position
     *
     * @return return true or false whether an enemy pawn can go to that position from mentioned side
    */
    public boolean checkUpside(State state, State.Pawn enemy, int[] position) {
        for(int i = position[0]; i >= 0; i--) {
            if (isPositionOccupied(state, position)) {
                if (state.getBoard()[i][position[1]].equalsPawn(enemy.toString()))
                    return true;
                else return false;
            }
            // camp check
            if ((position[1] == 0 || position[1] == 8) && i == 3)
                return false;
            if ((position[1] == 1 || position[1] == 7) && i == 4)
                return false;
        }
        return false;
    }

    /**
     * @param state the current state of the board
     * @param enemy color of the opposite pawn
     * @param position position
     *
     * @return return true or false whether an enemy pawn can go to that position from mentioned side
     */
    public boolean checkDownside(State state, State.Pawn enemy, int[] position) {
        for(int i = position[0]; i <= 8; i++) {
            if (isPositionOccupied(state, position)) {
                if (state.getBoard()[i][position[1]].equalsPawn(enemy.toString()))
                    return true;
                else return false;
            }
            // camp check
            if ((position[1] == 0 || position[1] == 8) && i == 5)
                return false;
            if ((position[1] == 1 || position[1] == 7) && i == 4)
                return false;
        }
        return false;
    }

    /**
     * @param state the current state of the board
     * @param enemy color of the opposite pawn
     * @param position position
     *
     * @return return true or false whether an enemy pawn can go to that position from mentioned side
     */
    public boolean checkRightSide(State state, State.Pawn enemy, int[] position) {
        for(int i = position[1]; i <= 8; i++) {
            if (isPositionOccupied(state, position)) {
                if (state.getBoard()[position[0]][i].equalsPawn(enemy.toString()))
                    return true;
                else return false;
            }
            // camp check
            if ((position[0] == 0 || position[0] == 8) && i == 5)
                return false;
            if ((position[0] == 1 || position[0] == 7) && i == 4)
                return false;
        }
        return false;
    }

    /**
     * @param state the current state of the board
     * @param enemy color of the opposite pawn
     * @param position position
     *
     * @return return true or false whether an enemy pawn can go to that position from mentioned side
     */
    public boolean checkLeftSide(State state, State.Pawn enemy, int[] position) {
        for(int i = position[1]; i >= 0; i--) {
            if (isPositionOccupied(state, position)) {
                if (state.getBoard()[position[0]][i].equalsPawn(enemy.toString()))
                    return true;
                else return false;
            }
            // camp check
            if ((position[0] == 0 || position[0] == 8) && i == 3)
                return false;
            if ((position[0] == 1 || position[0] == 7) && i == 4)
                return false;
        }
        return false;
    }

    /**
     * @param state the current state of the board
     * @param position the position of the pawn
     *
     * @return return a true or false whether a pawn can be captured
    */

    public boolean canBeCaptured(State state, int[] position, State.Pawn pawn) {
        if ((position[0] == 0 || position[0] == 8) && (position[1] == 0 || position[1] == 8))
            return false;

        State.Pawn enemy = (pawn.equalsPawn(State.Pawn.WHITE.toString()) || pawn.equalsPawn(State.Pawn.KING.toString())) ? State.Pawn.BLACK : State.Pawn.WHITE;

        // if is king and on center we have special cases
        if (pawn.equalsPawn(State.Pawn.KING.toString()) && isKingNearThrone(state, position)) {
            int needed = getNumbToEatKing(state);

            // if king is in danger (1 more enemy pawn to kill)
            if (checkAdjacentPawns(state, position, enemy.toString()) == (needed - 1)) {
                // search for empty space
            	
            	// can he move up?
                int[] space = new int[]{position[0]-1,position[1]};
                if (isPositionOccupied(state, space) && space[0] != 4 && space[1] != 4)
                    return (checkLeftSide(state, enemy, space) || checkRightSide(state, enemy, space));
                // can he move down?
                space[0] = position[0] + 1;
                if (isPositionOccupied(state, space) && space[0] != 4 && space[1] != 4)
                    return (checkLeftSide(state, enemy, space) || checkRightSide(state, enemy, space));
                // can he move left?
                space[0] = position[0];
                space[1] = position[1] - 1;
                if (isPositionOccupied(state, space) && space[0] != 4 && space[1] != 4)
                    return (checkUpside(state, enemy, space) || checkDownside(state, enemy, space));
                // can he move right?
                space[1] = position[1] + 1;
                return (checkUpside(state, enemy, space) || checkDownside(state, enemy, space));
            }
            else return false;
        }

        if (verticalCapturePossible(position, enemy))
            return true;
        if (horizontalCapturePossible(position, enemy))
            return true;

        return false;
    }

    /**
     * @return true if an Enemy pawn can capture a pawn in given Position vertically
     */
    private boolean verticalCapturePossible (int[] position, State.Pawn enemy) {
        int[] targetPos = new int[2];
        int[] checkPos = new int[2];

        if (position[0] == 0 || position[0] == 8)
            return false;

        // upside
        targetPos[0] = position[0] - 1;
        targetPos[1] = position[1];

        // if the target position is a camp or an enemy pawn
        if (Arrays.stream(camps).anyMatch(camp -> Arrays.equals(camp, targetPos)) ||
                (isPositionOccupied(state, targetPos) && state.getPawn(targetPos[0], targetPos[1]).equalsPawn(enemy.toString()))) {
            checkPos[0] = position[0] + 1;
            checkPos[1] = position[1];
            // if the check position is empty and the enemy can capture from the left or right
            if (!isPositionOccupied(state, checkPos) && (checkLeftSide(state, enemy, checkPos) || checkRightSide(state, enemy, checkPos))) {
                return true;
            }
        }

        // downside
        targetPos[0] = position[0] + 1;
        targetPos[1] = position[1];

        if (Arrays.stream(camps).anyMatch(camp -> Arrays.equals(camp, targetPos)) ||
                (isPositionOccupied(state, targetPos) && state.getPawn(targetPos[0], targetPos[1]).equalsPawn(enemy.toString()))) {
            checkPos[0] = position[0] - 1;
            checkPos[1] = position[1];
            if (!isPositionOccupied(state, checkPos) && (checkLeftSide(state, enemy, checkPos) || checkRightSide(state, enemy, checkPos))) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return true if an Enemy pawn can capture a pawn in given Position horizontally
     */
    private boolean horizontalCapturePossible(int[] position, State.Pawn enemy) {
        int[] targetPos = new int[2];
        int[] checkPos = new int[2];

        if (position[1] == 0 || position[1] == 8)
            return false;
        // left side present
        targetPos[0] = position[0];
        targetPos[1] = position[1] - 1;

        if (Arrays.stream(camps).anyMatch(camp -> Arrays.equals(camp, targetPos)) ||
                (isPositionOccupied(state, targetPos) && state.getPawn(targetPos[0], targetPos[1]).equalsPawn(enemy.toString()))) {
            checkPos[0] = position[0];
            checkPos[1] = position[1] + 1;
            if (!isPositionOccupied(state, checkPos) && (checkUpside(state, enemy, checkPos) || checkDownside(state, enemy, checkPos))) {
                return true;
            }
        }

        // right side present
        targetPos[0] = position[0];
        targetPos[1] = position[1] + 1;

        if (Arrays.stream(camps).anyMatch(camp -> Arrays.equals(camp, targetPos)) ||
                (isPositionOccupied(state, targetPos) && state.getPawn(targetPos[0], targetPos[1]).equalsPawn(enemy.toString()))) {
            checkPos[0] = position[0];
            checkPos[1] = position[1] - 1;
            if (!isPositionOccupied(state, checkPos) && (checkUpside(state, enemy, checkPos) || checkDownside(state, enemy, checkPos))) {
                return true;
            }
        }

        return false;
    }

    protected double kingDistanceFromWin(){
        int[] king = kingPosition(state);
        double min = Double.MAX_VALUE;
        for (int[] win : winPosition) {
            if (min > (Math.abs(king[0] - win[0]) + Math.abs(king[1] - win[1])))
                min = Math.abs(king[0] - win[0]) + Math.abs(king[1] - win[1]);
        }
        return min;
    }

        /**
     * @return The number of white pawns that can be captured.
     */
    protected int getPossibleCatches() {
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
    protected double canCaptureKing() {
        if (canBeCaptured(state, kingPosition(state), State.Pawn.KING))
            return +10.0;

        return 0.0;
    }
}

