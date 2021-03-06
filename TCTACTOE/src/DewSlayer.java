import de.ovgu.dke.teaching.ml.tictactoe.api.IBoard;
import de.ovgu.dke.teaching.ml.tictactoe.api.IPlayer;
import de.ovgu.dke.teaching.ml.tictactoe.api.IllegalMoveException;
import de.ovgu.dke.teaching.ml.tictactoe.game.Move;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * For further documentation and answers to the taks a) and b)
 * see the Docu.txt file
 * @author Christoph Dollase, Kilian P��el
 */
public class DewSlayer implements IPlayer {
	
	private static int counter = 1;
	private int id = 0;
	private float learningRate = 0.0001f;
	private ArrayList<Double> w_i; //List of weigths
	private ArrayList<Integer> x_i; //List of features
	private IBoard savedBoard = null;	
	private boolean isfirstRound = true;
	private boolean isDebugMode = false;
	
	// for statistics
	private int round = 0;
	private int wins = 0;
	private int looses = 0;
	private int turns = 0;	
	private List<int[]> Stats = new ArrayList<int[]>();
	
	// Features Xi
	private int X1, X2, X3, X4, X5, X6, X7, X8, X9, X10;	
	private int NumberOfFeatures = 10;
	
	public DewSlayer() {
		this.id = counter++;
	}

	public String getName() {
		return "DewSlayer " + this.id;
	}
	
	public int[] makeMove(IBoard board) {

		turns++;
		// create a clone of the board that can be modified
		IBoard copy = board.clone();
		int size = board.getSize();
		int[] winningStart = new int[]{size/2,size/2,size/2};

		
		if (board.getFieldValue(winningStart) == null)
			return winningStart;			
		
		
		if (this.isfirstRound) 
		{
			this.w_i = this.initializeWeights(NumberOfFeatures);
			this.x_i = this.initializeFeatures(NumberOfFeatures);
			this.isfirstRound = false;
		}

			
		IBoard bestBoard = null;
		int bestBoardIndex = 0;
		double bestValue = -Double.MAX_VALUE;
		
		this.savedBoard = board.clone();
		
		// simuliere alle m�gliche Z�ge
		ArrayList<BoardMoves> BoardsWithMoves = this.getAllPossibleBoardMoves(copy);
		ArrayList<IBoard> possibleBoardMoves = new ArrayList<IBoard>();
		for (int q= 0; q < BoardsWithMoves.size(); q++)
		{
			possibleBoardMoves.add(BoardsWithMoves.get(q).getBoard());			
		}		

		// berechne Zielfunktion f�r jeden Zug und speicher besten Zug
		for (int i = 0; i < possibleBoardMoves.size(); ++i) 
		{			
			if (this.getTargetFunction(this.w_i, (IBoard) possibleBoardMoves.get(i)) >= bestValue) 
			{
				bestValue = this.getTargetFunction(this.w_i, (IBoard) possibleBoardMoves.get(i));
				bestBoard = possibleBoardMoves.get(i);
				bestBoardIndex = i;
			}
		}
		
		// do a move using the cloned board
		int[] result = this.getMove(BoardsWithMoves, bestBoardIndex, bestBoard);		
		try {
			copy.makeMove(new Move(this, result));
		} catch (IllegalMoveException e) {
			System.out.println("ERROR: ILLEGAL MOVE!!!!!!1111einelf!!");
		}		
		return result;
	}
	
	private ArrayList<Integer> initializeFeatures(int size) 
	{
		ArrayList<Integer> temp = new ArrayList<>(); 
		for (int i = 0; i <= size; i++)
		{
			temp.add(0);
		}
		return temp;
	}

	private ArrayList<Double> initializeWeights(int size) 
	{
		ArrayList<Double> weights = new ArrayList<Double>();
		//target function contains concat(Features(me),Features(Enemy)); therefore it's twice the boardsize
		for (int i=0; i<= size;i++)
		{
			weights.add(1D);
		}	

		return weights;
	}
		
	private int[] getMove(ArrayList<BoardMoves> bm, int index, IBoard best) 
	{
		int[] temp = bm.get(index).getMove();
		
		//if (bm.get(index).getBoard() != best)
		//	System.out.println("Not the same Board");
		
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

	private ArrayList<Double> UpdateWeights(ArrayList<Double> w, IBoard savedBoard, IBoard board) 
	{
		Double error = Math.max(5D, (double) this.getTargetFunction(w, board) - this.getTargetFunction(w, savedBoard));
		ArrayList<Integer> f = this.getBoardFeatures(board);
		ArrayList<Double> weigths = new ArrayList<Double>();

		int i;
		for (i = 0; i < w.size(); ++i) {
			weigths.add((double) w.get(i) + (double) this.learningRate * (double) ((int) f.get(i)) * error);
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
		int breakpoint = 1;
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
		X6=0;
		X7=0;
		X8=0;
		X9=0;
		/*
		 X0 = hasFirstMove ? 1 : 0;
		 X1 = selbst 3 in einer Reihe und sonst nichts in der Reihe
	   - X2 = Gegner hat 3 in einer Reihe und sonst nichts in der Reihe
	     X3 = selbst 4 in einer Reihe und sonst nichts in der Reihe
	   - X4 = Gegner hat 4 in einer Reihe und sonst nichts in der Reihe
	     X5 = selbst 5 in einer Reihe
	     X6 = selbst 1 in der Reihe + einen Frei
	   - X7 = Gegner 1 in der reihe + 2 frei
	     X8 = selbst 2 in der Reihe + einen Frei
	     X9 = selbst 2 in der Reihe + 2 frei
	   - X10= Gegner 2 in einer Reihe + 2 frei
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
		saveXtoFeatureList();
		return x_i;
	}

	public void onMatchEnds(IBoard board) 
	{	
		
		round++;
		
		if (board.getWinner() == this)
			wins++;
		else if (board.getWinner() != null)
			looses++;
		
		this.w_i = this.UpdateWeights(this.w_i, this.savedBoard, board.clone());
		this.savedBoard = null;
		
		//System.out.println("R:"+ round + " W:"+wins+" L:"+looses+" T:"+turns);
		System.out.println(w_i.toString());
		Stats.add(new int[] {round,wins,looses,turns});
		turns = 0;
		try 
		{
			SaveStats();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
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
		x_i.set(6, X6);
		x_i.set(7, X7);
		x_i.set(8, X8);
		x_i.set(9, X9);
		x_i.set(10, X10);

		
		if (isDebugMode)
		{
			//System.out.println("Feautres:" + X1 +","+  X2 +","+ X3 +","+ X4 +","+ X5 );
		}
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
		int breakpoint;
		if (value >=3)
			breakpoint = 0;
		int size = board.getSize();
		if(value == 5) X5++;
		
		if(value == 4)
		{
		 if(i+vec[0] >= size || j+vec[1] >= size || k+vec[2] >= size)
		 {
			 if (!(i-4*vec[0] < 0 || j-4*vec[1] <0 || k-4*vec[2] <0) && board.getFieldValue(new int[] {i-4*vec[0],j-4*vec[1],k-4*vec[2]}) == null)
			 {
				 if(IsPlayer) X3++;
				 else X4-=10;
			 }			 
		 }
		 else if(board.getFieldValue(new int[] {i+vec[0],j+vec[1],k+vec[2]}) == null)
		 {
			 if(IsPlayer) X3++;
			 else X4-=10;
		 }
		}
		
		if(value == 3)
		{
		 if(i+vec[0] >= size || j+vec[1] >= size || k+vec[2] >= size)
		 {
			 if (!(i-3*vec[0] < 0 || j-3*vec[1] <0 || k-3*vec[2] <0) 
					 &&
				 board.getFieldValue(new int[] {i-3*vec[0],j-3*vec[1],k-3*vec[2]}) == null)
			 {
				 if(IsPlayer) X1++;
				 else X2--;
			 }
		 }
		
		 else
		 {
			 if(board.getFieldValue(new int[] {i+vec[0],j+vec[1],k+vec[2]}) == null)
			 {
				 if(IsPlayer) X1++;
				 else X2--;
			 }
			 if (!(i-3*vec[0] < 0 || j-3*vec[1] <0 || k-3*vec[2] <0) 
					 &&
				 board.getFieldValue(new int[] {i-3*vec[0],j-3*vec[1],k-3*vec[2]}) == null)
			 {
				 if(IsPlayer) X1++;
				 else X2--;
			 }
		 }
		}
		if(value == 2) 
		{
			if(i+vec[0] >= size || j+vec[1] >= size || k+vec[2] >= size)
			 {
				 if (!(i-2*vec[0] < 0 || j-2*vec[1] <0 || k-2*vec[2] <0)
						 &&
					 board.getFieldValue(new int[] {i-2*vec[0],j-2*vec[1],k-2*vec[2]}) == null)
				 {
					 if(IsPlayer) X8++;
					 else X10--;
				 }
			 }
			else
			 {
				 if(!(i+2*vec[0] >= size || j+2*vec[1] >= size || k+2*vec[2] >= size))
				 {
					 if((!(i-2*vec[0] < 0 || j-2*vec[1] <0 || k-2*vec[2] <0) 
							&&
						board.getFieldValue(new int[] {i-2*vec[0],j-2*vec[1],k-2*vec[2]}) == null)
							||
						board.getFieldValue(new int[] {i+vec[0],j+vec[1],k+vec[2]}) == null)
					 {
						 if(IsPlayer) X8++;
						 else ;				
				 	}
					 else if (board.getFieldValue(new int[] {i+vec[0],j+vec[1],k+vec[2]}) == null)
						 {
						 if(IsPlayer) X8++;
						 else ;
						 }
						 
				 }
			 }
		}
		
		if(value == 1)
		{ 
			int temp = 0;
			int count = 1;
			
			while(!(i+count*vec[0] >= size || j+count*vec[1] >= size || k+count*vec[2] >= size))
			{
				if(board.getFieldValue(new int[] {i+count*vec[0],j+count*vec[1],k+count*vec[2]}) == null)
				{
					temp++;
					count++;
				}
				else
				{
					break;
				}
			}
			count = 1;
			while(!(i-count*vec[0] < 0 || j-count*vec[1] < 0 || k-count*vec[2] < 0))
			{
				if(board.getFieldValue(new int[] {i-count*vec[0],j-count*vec[1],k-count*vec[2]}) == null)
				{
					temp++;
					count++;
				}
				else
				{
					break;
				}
			}
			
			if(temp >= 3) // Wenn 4, sind genau 4 Felder frei, wenn mehr als 4 Felder frei w�ren, w�rde irgendwas schief laufen..
			{
				if(IsPlayer) X6++;
				 else X7--;
			}
		}
		
	saveXtoFeatureList();	
	}
	
	protected void finalize() throws Throwable
	{
		SaveStats();
		super.finalize();
	}

	private void SaveStats() throws IOException 
	{
		FileWriter writer = new FileWriter("ML_Stats.csv");

		for (int j = 0; j < Stats.size(); j++) 
		{
			for (int i = 0; i< Stats.get(j).length; i++)
			{
				writer.append(String.valueOf(Stats.get(j)[i]));
			    writer.append(";");
			}
			writer.append("\n");		    
		}
		writer.close();
		FileWriter weights = new FileWriter("finalWeigths.txt");
		weights.append(w_i.toString());
		weights.close();
	}

}

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


