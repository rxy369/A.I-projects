package breakthrough;

import breakthrough.BreakthroughMiniMax.BoardSpace;
import breakthrough.BreakthroughMiniMax.ScoredBreakthroughMove;
import game.*;

public class AlphaBetaBreakthrough extends BreakthroughMiniMax {

	public AlphaBetaBreakthrough(String nname, int d) {
		super(nname,d);
	}

	/**
	 * Performs the a depth limited minimax algorithm. It leaves it's move
	 * recommendation at mvStack[currDepth].
	 * 
	 * @param brd
	 *            current board state
	 * @param currDepth
	 *            current depth in the search
	 */
	private void alphaBetaSearch(BreakthroughState brd, int currDepth, double alpha, double beta) {
		boolean toMaximize = (brd.getWho() == GameState.Who.HOME);
		boolean toMinimize = !toMaximize;
		boolean isTerminal = terminalValue(brd, mvStack[currDepth]);

		if (isTerminal) {
			; 
		} else if (currDepth == depthLimit) {
			// eval board is the heuristic
			mvStack[currDepth].set(0, 0, 0, 0, evalBoard(brd));
		} else {
			ScoredBreakthroughMove tempMv = new ScoredBreakthroughMove(0, 0, 0, 0, 0);

			double bestScore = (brd.getWho() == GameState.Who.HOME ? Double.NEGATIVE_INFINITY
					: Double.POSITIVE_INFINITY);
			ScoredBreakthroughMove bestMove = mvStack[currDepth];
			ScoredBreakthroughMove nextMove = mvStack[currDepth + 1];

			bestMove.set(0, 0, 0, 0, bestScore);


			// Iterate through board
			// Identify positions for currTurn
			// Identify possible moves from those pos
			// Create those moves
			// Add them to stack
			BoardSpace[][] positions = new BoardSpace[ROWS][COLS];
			
			char comparison = brd.getWho().toString().equals("AWAY") ? 'B' : 'W'; // sets whether or not the current player is home or away
			
			for (int j = 0; j < ROWS; j++) {
				for (int k = 0; k < COLS; k++) {
					positions[j][k] = new BoardSpace(j,k,false);
					positions[j][k].hasPiece = (brd.board[j][k] == comparison) ? true : false;
				}
			}
			
			int direction = brd.getWho() == GameState.Who.HOME ? 1 : -1; // which direction the piece is going
			
			shuffle(positions);
			for (int j = 0; j < ROWS; j++) {
				for (int k = 0; k < COLS; k++) {
					// evaluate true if currTurn has piece on this spot on the
					// board
					if (positions[j][k].hasPiece) {
						BoardSpace bs = positions[j][k];
						for (int offset = -1; offset < 2; offset++) { // checks diagonally left, diagonally right, and forward
							// Make move on board
							//create a temporary board from the one we passed in
							BreakthroughState tempBrd = (BreakthroughState) brd.clone();
							
							tempMv.startRow = bs.row;
							tempMv.startCol = bs.col;
							tempMv.endingRow = bs.row + direction;
							tempMv.endingCol = bs.col + offset;
							
							if(!tempBrd.moveOK(tempMv)){
								continue;
							}
							
							tempBrd.makeMove(tempMv);
							tempMv.score = evalBoard(tempBrd); // finds the score of the move from the tempBrd
							
							
							// Check out worth of this move
							alphaBetaSearch(tempBrd, currDepth + 1,alpha, beta);
							

							// Check out the results, relative to what we've seen before
							if (toMaximize && nextMove.score > bestMove.score) {
								bestMove.set(tempMv.startRow, tempMv.startCol, tempMv.endingRow, tempMv.endingCol, nextMove.score);
							} else if (!toMaximize && nextMove.score < bestMove.score) {
								bestMove.set(tempMv.startRow, tempMv.startCol, tempMv.endingRow, tempMv.endingCol, nextMove.score);
							}
							
							if(toMinimize) {
								beta = Math.min(bestMove.score, beta);
								if (bestMove.score <= alpha || bestMove.score == -MAX_SCORE) {
									return;
								}
							} else {
								alpha = Math.max(bestMove.score, alpha);
								if (bestMove.score >= beta || bestMove.score == MAX_SCORE) {
									return;
								}
							}
						}
					}
				}
			}
		}
	}

	public GameMove getMove(GameState brd, String lastMove) {
		alphaBetaSearch((BreakthroughState) brd, 0,Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		return mvStack[0];
	}

	public static void main(String[] args) {
		int depth = 4;
		GamePlayer p = new AlphaBetaBreakthrough("Maginot Line " + depth, depth);
		p.compete(args);
	}
}
