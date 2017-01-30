package breakthrough;

import game.*;

public class BreakthroughMiniMax extends BaseBreakthroughPlayer {
	public final int MAX_DEPTH = 50;
	public int depthLimit;

	// mvStack is where the search procedure places it's move recommendation.
	// If the search is at depth, d, the move is stored on mvStack[d].
	// This was done to help efficiency (i.e., reduce number constructor calls)
	// (Not sure how much it improves things.)
	protected ScoredBreakthroughMove[] mvStack;
	
	protected class BoardSpace{
		public int row;
		public int col;
		public boolean hasPiece;
		
		public BoardSpace(int row, int col, boolean piece){
			this.row = row;
			this.col = col;
			this.hasPiece = piece;
		}
	}

	// A Connect4Move with a scored (how well it evaluates)
	protected class ScoredBreakthroughMove extends BreakthroughMove {
		
		public ScoredBreakthroughMove(int r1, int c1, int r2, int c2, double s) {
			super(r1, c1, r2, c2);
			score = s;
		}

		public void set(int r1, int c1, int r2, int c2, double s) {
			startRow = r1;
			startCol = c1;
			endingRow = r2;
			endingCol = c2;
			score = s;
		}

		public double score;
	}

	public BreakthroughMiniMax(String nname, int d) {
		super(nname);
		depthLimit = d;
	}

	protected static void shuffle(BoardSpace[][] ary) {
		for (int i = 0; i < ROWS; i++) {
			for(int j =0; j < COLS; j++){
				int spot = Util.randInt(i, ROWS - 1);
				int spot2 = Util.randInt(i, COLS - 1);
				BoardSpace tmp = ary[i][j];
				ary[i][j] = ary[spot][spot2];
				ary[spot][spot2] = tmp;
			}
		}
	}

	/**
	 * Initializes the stack of Moves.
	 */
	public void init() {
		mvStack = new ScoredBreakthroughMove[MAX_DEPTH];
		for (int i = 0; i < MAX_DEPTH; i++) {
			mvStack[i] = new ScoredBreakthroughMove(0, 0, 0, 0, 0);
		}
	}

	/**
	 * Determines if a board represents a completed game. If it is, the
	 * evaluation values for these boards is recorded (i.e., 0 for a draw +X,
	 * for a HOME win and -X for an AWAY win.]
	 * 
	 * @param brd
	 *            Connect4 board to be examined
	 * @param mv
	 *            where to place the score information; column is irrelevant
	 * @return true if the brd is a terminal state
	 */
	protected boolean terminalValue(GameState brd, ScoredBreakthroughMove mv) {
		GameState.Status status = brd.getStatus();
		boolean isTerminal = true;

		if (status == GameState.Status.HOME_WIN) {
			mv.set(0, 0, 0, 0, MAX_SCORE);
		} else if (status == GameState.Status.AWAY_WIN) {
			mv.set(0, 0, 0, 0, -MAX_SCORE);
		} else if (status == GameState.Status.DRAW) {
			mv.set(0, 0, 0, 0, 0);
		} else {
			isTerminal = false;
		}
		return isTerminal;
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
	private void minimax(BreakthroughState brd, int currDepth) {
		boolean toMaximize = (brd.getWho() == GameState.Who.HOME);
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
			
			//shuffle(positions);
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
							minimax(tempBrd, currDepth + 1);
							
							// Undo the move, no need to undo since the original brd isn't adjusted
							// cloning the board hurts performance quite a bit though. Might have to work around it.
							/*
							// 
							if(currTurn.toString().equals("AWAY")){
								brd.board[bs.row][bs.col] = 'B';
							}else if(currTurn.toString().equals("HOME")){
								brd.board[bs.row][bs.col] = 'W';
							}
							
							brd.board[tempMv.endingRow][tempMv.endingCol] = BreakthroughState.emptySym;
							brd.numMoves--;
							brd.status = GameState.Status.GAME_ON;
							brd.who = currTurn;
							*/

							// Check out the results, relative to what we've seen before
							if (toMaximize && nextMove.score > bestMove.score) {
								bestMove.set(tempMv.startRow, tempMv.startCol, tempMv.endingRow, tempMv.endingCol, nextMove.score);
							} else if (!toMaximize && nextMove.score < bestMove.score) {
								bestMove.set(tempMv.startRow, tempMv.startCol, tempMv.endingRow, tempMv.endingCol, nextMove.score);
							}
						}
					}
				}
			}
		}
	}

	public GameMove getMove(GameState brd, String lastMove) {
		minimax((BreakthroughState) brd, 0);
		return mvStack[0];
	}

	public static void main(String[] args) {
		int depth = 4;
		GamePlayer p = new BreakthroughMiniMax("BT Lonely Turtle " + depth, depth);
		p.compete(args);
	}
}
