import de.ovgu.dke.teaching.ml.tictactoe.api.IBoard;
import de.ovgu.dke.teaching.ml.tictactoe.api.IPlayer;
import de.ovgu.dke.teaching.ml.tictactoe.api.IllegalMoveException;
import de.ovgu.dke.teaching.ml.tictactoe.game.Move;

import java.util.ArrayList;
import java.util.Iterator;

class BoardMoves
{
	private IBoard board;
	private int[] move;
	
	public BoardMoves(IBoard b, int[] m)
	{
		this.board = b;
		this.move = m;		
	}
	
	public IBoard getBoard()
	{
		return board;
	}
	
	public int[] getMove()
	{
		return move;
	}	
}

/**
 * @author Christoph Dollase, Kilian Pößel
 */
public class NewPlayer implements IPlayer {
	
	private float learningRate = 0.001f;
	private ArrayList<Double> w_i;
	private ArrayList<Integer> x_i;
	private IBoard savedBoard = null;	
	private boolean isfirstRound = true;
	private boolean isDebugMode = false;
	
	private int X1;
	private int X2;
	private int X3;
	private int X4;
	private int X5;
	
	private int NumberOfFeatures = 7;


	public String getName() 
	{
		return "Nic Aragua";
	}
	
	public int[] makeMove(IBoard board) {

		// create a clone of the board that can be modified
		IBoard copy = board.clone();
		int size = board.getSize();
		int[] winningStart = new int[]{size/2,size/2,size/2};

		if (board.getFieldValue(winningStart) == null)
			return winningStart;			
		
		// foreach field in board-copy:
		// board.getFieldValue((new int[] { i, k, m }))
		
		/*				
		Überprüfe, was der gegner gemacht hat (oldBoard vs. newBoard)
		Überprüfe die Achsen seines letzten Zuges		
		
		Gewichte anpassen	
		*/

		/* ================== ENDE INTRO ======================= */
		
		if (this.isfirstRound) 
		{
			this.w_i = this.initializeWeights(NumberOfFeatures);
			this.x_i = this.initializeFeatures(NumberOfFeatures);
			this.isfirstRound = false;
		}

		// If there was a round before, update weights
		if (this.savedBoard != null) 
		{
			this.w_i = this.UpdateWeights(this.w_i, this.savedBoard, board);
		}
			
		IBoard bestBoard = null;
		int bestBoardIndex = 0;
		double bestValue = -Double.MAX_VALUE;
		
		// simuliere alle mögliche Züge
		ArrayList<BoardMoves> BoardsWithMoves = this.getAllPossibleBoardMoves(copy);
		ArrayList<IBoard> possibleBoardMoves = new ArrayList<IBoard>();
		for (int q= 0; q < BoardsWithMoves.size(); q++)
		{
			possibleBoardMoves.add(BoardsWithMoves.get(q).getBoard());			
		}		

		// berechne Zielfunktion für jeden Zug und speicher besten Zug
		for (int i = 0; i < possibleBoardMoves.size(); ++i) 
		{			
			if (this.getTargetFunction(this.w_i, (IBoard) possibleBoardMoves.get(i)) > bestValue) 
			{
				bestValue = this.getTargetFunction(this.w_i, (IBoard) possibleBoardMoves.get(i));
				bestBoard = possibleBoardMoves.get(i);
				bestBoardIndex = i;
			}
		}

		this.savedBoard = board.clone();
		
		// do a move using the cloned board
		int[] result = this.getMove(BoardsWithMoves, bestBoardIndex, bestBoard);		
		try {
			copy.makeMove(new Move(this, result));
		} catch (IllegalMoveException e) {
			System.out.println("ERROR: ILLEGAL MOVE!!!!!!1111einelf!!");
		}		
		return result;
	}
	
	private boolean isBoardEmpty(IBoard board)
	{
		int size = board.getSize();
		boolean isEmpty = false;
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				for (int k = 0; k < size; k++) 
				{
					if (board.getFieldValue(new int[]{i,j,k}) == null)
						isEmpty = true;
					else
						isEmpty = false;
				}
			}
		}
		return isEmpty;
	}
	
	private ArrayList<Integer> initializeFeatures(int size) 
	{
		ArrayList<Integer> temp = new ArrayList<>(); 
		for (int i = 0; i < size; i++)
		{
			temp.add(0);
		}
		return temp;
	}

	private ArrayList<Double> initializeWeights(int size) 
	{
		ArrayList<Double> weights = new ArrayList<Double>();
		//target function contains concat(Features(me),Features(Enemy)); therefore it's twice the boardsize
		for (int i=0; i< size;i++)
		{
			weights.add(0.1D);
		}		
		return weights;
	}
		
	private int[] getMove(ArrayList<BoardMoves> bm, int index, IBoard best) 
	{
		int[] temp = bm.get(index).getMove();
		
		if (bm.get(index).getBoard() != best)
			System.out.println("Not the same Board");
		
		return temp;
	}

	private ArrayList<BoardMoves> getAllPossibleBoardMoves(IBoard board) 
	{
		int size = board.getSize();
		ArrayList<BoardMoves> BoardAndMove = new ArrayList<BoardMoves>();
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				for (int k = 0; k < size; k++) 
				{
					if (board.getFieldValue(new int[]{i, k, j}) == null) {
						IBoard temp = board.clone();

						try {
							temp.makeMove(new Move(this, new int[]{i, k, j}));
						} catch (IllegalMoveException e) {
							System.out.println("Wrong move!!!!");
						}

						BoardAndMove.add(new BoardMoves(temp, new int[]{i, k, j}));
					}
				}
			}
		}	
		
		return BoardAndMove;
	}

	private double getTargetFunction(ArrayList<Double> w_i, IBoard iBoard)
	{	
		getBoardFeatures(iBoard);
		ArrayList<Double> features = new ArrayList<>();
		Iterator<Integer> intlist = this.x_i.iterator();
		while(intlist.hasNext())
		{
			Integer i = intlist.next();
			features.add(i.doubleValue());
		}
		
		if (X5 > 0)
			return 100000;
		
		return weightedSum(w_i, features);
	}

	private double weightedSum(ArrayList<Double> w, ArrayList<Double> x) 
	{
		if (w.size() != x.size()) 
		{
			System.out.println("Weighted sum of unequal arrays!");
			return 0.0D;
		}
		else
		{
			double sum = 0.0D;
			for (int i = 0; i < w.size(); ++i) {
				sum += (w.get(i)) * (x.get(i));
			}
			return sum;
		}
	}

	//TODO: may change and refactor
	private ArrayList<Double> UpdateWeights(ArrayList<Double> w, IBoard savedBoard, IBoard board) 
	{
		Double error = Double.valueOf(this.getTargetFunction(w, board) - this.getTargetFunction(w, savedBoard));
		ArrayList<Integer> f = this.getBoardFeatures(board);
		ArrayList<Double> weigths = new ArrayList<Double>();

		int i;
		for (i = 0; i < w.size(); ++i) {
			weigths.add(new Double(((Double) w.get(i)).doubleValue()
					+ (double) this.learningRate * (double) ((Integer) f.get(i)).intValue() * error.doubleValue()));
		}

		if (this.isDebugMode) {
			System.out.print("Old Features: ");

			for (i = 0; i < f.size(); ++i) {
				System.out.print(f.get(i) + " ");
			}

			System.out.println();
			System.out.print("New Weights: ");

			for (i = 0; i < weigths.size(); ++i) {
				System.out.print(weigths.get(i) + " ");
			}
			System.out.println();
		}
		return weigths;
	}

	private ArrayList<Integer> getBoardFeatures(IBoard board) 
	{
		int size = board.getSize();
		X1=0;
		X2=0;
		X3=0;
		X4=0;
		X5=0;
		/*
		 X0 = hasFirstMove ? 1 : 0;
		 X1 = selbst 3 in einer Reihe und sonst nichts in der Reihe
	     X2 = Gegner hat 3 in einer Reihe und sonst nichts in der Reihe
	     X3 = selbst 4 in einer Reihe und sonst nichts in der Reihe
	     X4 = Gegner hat 4 in einer Reihe und sonst nichts in der Reihe
	     X5 = selbst 5 in einer Reihe
		 */
		
		// Evaluate the Board
		for(int i = 0; i<size; i++)
		{
			for (int j = 0; j<size; j++)
			{
				for (int k = 0; k<size; k++)
				{
					searchField(board, i,j,k); 
				}
			}
		}
		return x_i;
	}

	public void onMatchEnds(IBoard board) 
	{	
		this.w_i = this.UpdateWeights(this.w_i, this.savedBoard, board.clone());
		this.savedBoard = null;
		
		return;
	}

	private void searchField(IBoard board, int i, int j, int k)
	{ 
		if(board.getFieldValue(new int[] {i,j,k}) != null)
		{
			int size = board.getSize();
			int[] vec = new int[3];
			int value = 1;
			 boolean IsPlayer;
			if (board.getFieldValue(new int[] {i,j,k}) == this) IsPlayer = true;
			else IsPlayer = false;
			
			if(i+1 < size) // Check if the move is on a upper border of the board
			{
				vec[0]=1;
				vec[1]=0;
				vec[2]=0;
				_searchField(board, i+1, j, k, vec, value, IsPlayer);
			}
			if(j+1 < size) // Check if the move is on a upper border of the board
			{
				vec[0]=0;
				vec[1]=1;
				vec[2]=0;
				_searchField(board, i, j+1, k, vec, value, IsPlayer);
			}
			if(i+1 < size && j+1 <size) // Check if the move is on a upper border of the board
			{
				vec[0]=1;
				vec[1]=1;
				vec[2]=0;
				_searchField(board, i+1, j+1, k, vec, value, IsPlayer);
			}
			
			
			if(i+1 < size && k+1 <size) // Check if the move is on a upper border of the board
			{
				vec[0]=1;
				vec[1]=0;
				vec[2]=1;
				_searchField(board, i+1, j, k+1, vec, value, IsPlayer);
			}
			if(j +1 < size && k+1 <size) // Check if the move is on a upper border of the board
			{ 
				vec[0]=0;
				vec[1]=1;
				vec[2]=1;
				_searchField(board, i, j+1, k+1, vec, value, IsPlayer);
			}
			if(j+1 < size && k+1 <size) // Check if the move is on a upper border of the board
			{
				vec[0]=0;
				vec[1]=1;
				vec[2]=1;
				_searchField(board, i, j+1, k+1, vec, value, IsPlayer);
			}
			if(i+1 < size && j+1 < size && k+1 <size) // Check if the move is on a upper border of the board
			{
				vec[0]=1;
				vec[1]=1;
				vec[2]=1;
				_searchField(board, i+1, j+1, k+1, vec, value, IsPlayer);
			}
			
			saveXtoFeatureList();
		}
	
	return;	
	}
	
	private void saveXtoFeatureList() 
	{
		x_i.set(1, X1);
		x_i.set(2, X2);
		x_i.set(3, X3);
		x_i.set(4, X4);
		x_i.set(5, X5);		
	}

	private void _searchField(IBoard board, int i, int j, int k,int[] vec, int value, Boolean IsPlayer )
	{
		if(board.getFieldValue(new int[] {i,j,k}) == this 
				||
				board.getFieldValue(new int[] {i,j,k}) != null && !IsPlayer)
		{
			int size = board.getSize();
			int _value = value + 1;
			
			if(i+vec[0] < size && j+vec[1] < size && k+vec[2]<size)
			{				
				_searchField(board, i+vec[0], j+vec[1], k+vec[2], vec, _value, IsPlayer);
			}
			else
			{
				setValue(value, IsPlayer, i, j, k, vec, board);
			}
			
		}
		else
		{
			setValue(value, IsPlayer, i, j, k, vec, board);
		}
	return;			
	}
	
	private void setValue(int value, Boolean IsPlayer, int i, int j, int k, int[] vec, IBoard board)
	{
		int size = board.getSize();
		if(value == 5) X5++;
		
		if(value == 4)
		{
		 if(i+vec[0] == size || j+vec[1] == size || k+vec[2] == size)
		 {
			 if (board.getFieldValue(new int[] {i-4*vec[0],j-4*vec[1],k-4*vec[2]}) == null)
			 {
				 if(IsPlayer) X3++;
				 else X4++;
			 }			 
		 }
		 else if(board.getFieldValue(new int[] {i+vec[0],j+vec[1],k+vec[2]}) == null)
		 {
			 if(IsPlayer) X3++;
			 else X4++;
		 }
		}
		
		if(value == 3)
		{
		 if(i+vec[0] == size || j+vec[1] == size || k+vec[2] == size)
		 {
			 if (board.getFieldValue(new int[] {i-3*vec[0],j-3*vec[1],k-3*vec[2]}) == null
					 &&
				 board.getFieldValue(new int[] {i-4*vec[0],j-4*vec[1],k-4*vec[2]}) == null)
			 {
				 if(IsPlayer) X1++;
				 else X2++;
			 }
		 }
		 else
		 {
			 if(!(i+2*vec[0] == size || j+2*vec[1] == size || k+2*vec[2] == size)
					 &&
				board.getFieldValue(new int[] {i+vec[0],j+vec[1],k+vec[2]}) == null					 
					 &&
				board.getFieldValue(new int[] {i+2*vec[0],j+2*vec[1],k+2*vec[2]}) == null)
			 {
				 if(IsPlayer) X1++;
				 else X2++;
			 }
			 if (board.getFieldValue(new int[] {i-3*vec[0],j-3*vec[1],k-3*vec[2]}) == null //TODO: wirft Fehler
					 &&
				 board.getFieldValue(new int[] {i+vec[0],j+vec[1],k+vec[2]}) == null)
			 {
				 if(IsPlayer) X1++;
				 else X2++;
			 }
		 }
		}
	}

}


