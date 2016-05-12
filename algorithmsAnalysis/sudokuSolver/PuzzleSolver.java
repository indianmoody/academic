// Author: Gaurav Bishnoi
// CSCI 5454: Final Project
// Currently this code does 400 iterations for n=3 and k=52. I have enterd all the configurations manually.
// Please see lines 135 to 160 if you want to change inputs
// The graph will be generated in ' C Drive ' as "chart.jpg"


import java.util.*;

// jfreechart library to plot grpahs
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;
import java.awt.Shape;
import org.jfree.chart.ChartUtilities;
import java.io.File;
import java.io.IOException;


public class PuzzleSolver {
 
 // 'Brick' represents basic unit of sudoku puzzle: a single cell. It takes row and column number of a cell to describe its address. 
 static class Brick 
 {
	 int rowNumber, columnNumber;
	 public Brick(int row, int col) 
	 {
		 super();
		 this.rowNumber = row;
		 this.columnNumber = col;
	 }
 } 
 // Function to check validity of given number in row, column and box of given brick/cell
 static boolean numberValidity(int[][] grid, int gridUnit, Brick tempBrick, int trialValue) 
 {
	
	// Checking validity of given number in row of given brick
	for (int i = 0; i < gridUnit*gridUnit; i++) 
	{
		if (grid[tempBrick.rowNumber][i] == trialValue)
		return false;
	}

	// Checking validity of given number in column of given brick
	for (int i = 0; i < gridUnit*gridUnit; i++) 
	{
		if (grid[i][tempBrick.columnNumber] == trialValue)
		return false;
	}

	// Checking validity of given number in box of given brick
	// The box for current brick is bounded by leftLimit, rightLimit, upLimit and downLimit
	int leftLimit = (int) (gridUnit * (tempBrick.rowNumber / gridUnit));
	int upLimit = (int) (gridUnit * (tempBrick.columnNumber / gridUnit));
	int rightLimit = (int) (leftLimit + gridUnit-1) ;
	int downLimit = (int) (upLimit + gridUnit-1);

	for (int hor = leftLimit; hor <= rightLimit; hor++)
	for (int ver = upLimit; ver <= downLimit; ver++)
    if (grid[hor][ver] == trialValue)
    	return false;

	// If the given value is not found in row, column and box of given brick we return True 
	return true;
}

 
 // Function to move to next brick
 static Brick nextBrick(Brick tempBrick, int gridUnit) 
 {
	 int row = tempBrick.rowNumber;
	 int col = tempBrick.columnNumber;
	 // Move horizontally to next brick, i.e. increment column number
	 col++;
	 // But if column number becomes greater than grid length, next brick is first brick of next row i.e. col = 0 and row += 1
	 if (col > gridUnit*gridUnit-1) {
		 col = 0;
		 row++;
	 }
	 // If it is last brick of puzzle, mission is complete. So, it returns 'null'
	 if (row > gridUnit*gridUnit-1)
		 return null;
	 // Create an object of 'Brick' with new values of row and col and return it.
	 Brick next = new Brick(row, col);
	 	return next;
 }

 
 // Function to solve given 'grid' matrix. It uses above defined methods of class to solve puzzzle for given grid and returns 'True' in case of 
 // success, otherwise 'False'.
 static boolean sudokuHandler(int[][] grid, int gridUnit, Brick tempBrick) 
 {
	 // If the given brick is 'null', that means whole grid has been traversed or no longer needs to be solved. We returned 'null' after
	 // detecting last brick of grid in 'nextBrick' function. 
	 if (tempBrick == null)
		 return true;
	 // No need to solve if the brick was filled initially. Move to next brick, solve for it and return that.
	 if (grid[tempBrick.rowNumber][tempBrick.columnNumber] != 0) 
	 {
		 return sudokuHandler(grid, gridUnit, nextBrick(tempBrick, gridUnit));
	 }
	 // In this loop, try possible values at empty brick, check its validity and upon approval assign it to brick. Then solve for next brick in
	 // recursion. If there is no solution for next brick i.e. False is returned, try different value.
	 for (int i = 1; i <= gridUnit*gridUnit; i++) 
	 {
		 boolean correct = numberValidity(grid, gridUnit, tempBrick, i); // Checking validity of trial number
		 if (!correct) // i not valid for this cell, try other values
			 continue;
		 
		 grid[tempBrick.rowNumber][tempBrick.columnNumber] = i; // Assigning the approved value to given brick
		 boolean correct2 = sudokuHandler(grid, gridUnit, nextBrick(tempBrick, gridUnit)); //Try to solve next brick after assigning value 'i' to current brick
		 if (correct2)	
			 return true;
		 else
			 grid[tempBrick.rowNumber][tempBrick.columnNumber] = 0; // Otherwise empty the current brick and try next value
	 }

  return false;	// There is no solution, return False. Because in case of solution, the function should have already returned True.
 }


 // "main" function
 public static void main(String[] args) 
 {
	 // plot
	 XYSeries series = new XYSeries("XYGraph");
	 // --plot
	 
	 
	 double startTime, stopTime;
	 double[] worstTime = new double[1];
	 double[][] avgTime = new double[1][400];
	 double[] tame = new double[1];
	 double medianValue = 0;
	 for (int nZero = 0; nZero < 1; nZero++)
	 {
		 for (int nIter = 0; nIter<400; nIter++)
		 {
			 PuzzleCreator puzzleObject = new PuzzleCreator(3);
			 puzzleObject.newPuzzle(52 + 4*nZero);
			 
			 startTime = System.currentTimeMillis();
			 boolean correct3 = sudokuHandler(puzzleObject.bigBox, puzzleObject.n, new Brick(0, 0));
			 if (!correct3) 
			 {
				 System.out.println("Solution is not possible!");
				 return;
			 }
			 stopTime = System.currentTimeMillis();
			 avgTime[nZero][nIter] = stopTime-startTime;
			 tame[nZero] += (avgTime[nZero][nIter]);
			 if (avgTime[nZero][nIter] > worstTime[nZero])
			 {
				 worstTime[nZero] = avgTime[nZero][nIter];
			 }
			 puzzleObject = null;
			 //System.out.println(stopTime-startTime);
			 
			 
			 
			 // plot
			 series.add(nIter, avgTime[nZero][nIter]);
			 
			 // --plot
			 
		 }
		 tame[nZero] = tame[nZero]/400;
		 
		 Arrays.sort(avgTime[nZero]);
		 
		     medianValue = ((double)avgTime[nZero][avgTime[nZero].length/2] + (double)avgTime[nZero][avgTime[nZero].length/2 - 1])/2;
		 
		System.out.println("n = 3, k = 52 " + "Mean Time = "+ tame[nZero] + " Median Time = " + medianValue + " Worst Time = " + worstTime[nZero]);
		 //System.out.println(avgTime[nZero][7]);
		 
	 }
	 
	 // plot
	 XYSeriesCollection dataset = new XYSeriesCollection();
	 dataset.addSeries(series);
	 
	 //JFreeChart chart = ChartFactory.createXYLineChart(
	 JFreeChart chart = ChartFactory.createScatterPlot(
			 "Running Time Analysis: n=3, k =64", // Graph Heading
			 "Iteration Number", // Label for horizontal/X axis
			 "Running Time", // Label for vertical/Y axis
			 dataset, // Set of data to plot on graph
			 PlotOrientation.VERTICAL, // Plot Orientation
			 true, // Show Legend
			 true, // To use the tooltips or not, True for use
			 false // want to generate URLs or not?
			 );
	 
	 
	 final XYPlot plot = chart.getXYPlot();
	 final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
     renderer.setSeriesLinesVisible(0, false);
     Shape cross = ShapeUtilities.createDiagonalCross(3, 1);
     renderer.setSeriesShape(0, cross);
     plot.setRenderer(renderer);
	 
	 try {
		 ChartUtilities.saveChartAsJPEG(new File("C:\\chart.jpg"), chart, 1000, 600);
		 } catch (IOException e) {
		 System.err.println("Problem occurred creating chart.");
		 }
	 // --plot
	 
	 
	 PuzzleCreator sampleSudoku = new PuzzleCreator(3);
	 sampleSudoku.newPuzzle(41);
	 System.out.println("Generated Puzzle:");
	 sampleSudoku.printPuzzle();
	 boolean correct3 = sudokuHandler(sampleSudoku.bigBox, sampleSudoku.n, new Brick(0, 0));
	 if (!correct3) 
	 {
		 System.out.println("Solution is not possible!");
		 return;
	 }
	 System.out.println("Solved Puzzle:");
	 sampleSudoku.printPuzzle();
	 
	
 }

}

class PuzzleCreator
{
	
	public int n;	// n = sudoku unit for n*2 . n*2 sudoku
	int[][] bigBox; // sudoku Matrix: 2D array
	//Constructor function for class 'PuzzleCreator'. It will take value of sudoku unit from whoever calls the instance of this class.
   public PuzzleCreator(int m) {
	   n = m;
	   bigBox = new int[n*n][n*n]; // generating empty matrix of n^2 . n^2
   }
	 
	//Function to generate new Sudoku Puzzle by taking number of desired empty cells as argument. It first generates a completed sudoku puzzle
    // using function 'brickFill' and then create empty spaces using function 'initialSpace'.
	public int[][] newPuzzle(int spaces)
	{
		brickFill(0,0);				// fill all bricks of the empty matrix starting from (0,0) i.e. from top left row by row
		initialSpace(spaces);		// create desired number of spaces in filled matrix, i.e. generate desired number of empty bricks.
		return bigBox;				
	}
	
	
	// This is same function as 'numberValidity' in sudoku solving class except argument selection. It takes row and column of a brick separately
		// instead of a brick object altogether.
		private boolean numberValidity(int row, int col, int trialValue) {
			// Checking in row
			for(int i=0;i<n*n;i++) 
			{
				if(trialValue == bigBox[row][i])
					return false;
			}
			// checking in column
			for(int i=0;i<n*n;i++) {
				if(trialValue == bigBox[i][col])
					return false;
			}
			// checking in small box containing current combination of row and column numbers
			int leftLimit = (int) (n * (row / n));
			int upLimit = (int) (n * (col / n));
			int rightLimit = (int) (leftLimit +n-1) ;
			int downLimit = (int) (upLimit + n-1);
			for (int hor = leftLimit; hor <= rightLimit; hor++)
			for (int ver = upLimit; ver <= downLimit; ver++)
		    if (bigBox[hor][ver] == trialValue)
		    	return false;
		
			
			return true;
		}
	
	
	
	// Function to completely fill empty puzzle. It uses Random function to place numbers randomly in the puzzle/matrix. Function 
	//'numberValidity' is used to verify if a proposed number can be placed in current brick or not.
	public boolean brickFill(int row, int col)
	{	
		int trialIndex = 0;
		int[] valueSet = new int[n*n];		// Set of numbers eligible to be filled in sudoku.
		int gridLength = valueSet.length;	// total numbers present in valueSet. They are equal to n^2
		for(int i=0; i<gridLength; i++)		// Filling valueSet with eligible numbers
		{
			valueSet[i] = i+1;
		}				
		Random rand = new Random();			
		int trialNumber = 0;				
		
		//randomizing input array
   		for(int i=gridLength-1;i>0;i--)
		{
		    trialIndex = rand.nextInt(i);
		    trialNumber = valueSet[trialIndex];
		    valueSet[trialIndex] = valueSet[i];
		    valueSet[i] = trialNumber;
    	}
		// variable to move forward
   		int newRow = row;
		int newCol = col;
   		
   		// 
		for(int i=0;i<gridLength;i++)
		{
			if(numberValidity(row, col, valueSet[i]))
			{
				bigBox[row][col] = valueSet[i];
				if(row == gridLength-1)
				{
					if(col == gridLength-1)
					{
						return true;
					}
						
					else
					{
						newRow = 0;
						newCol = col + 1;
					}
				}
				else
				{
					newRow = row + 1;
				}
				if(brickFill(newRow, newCol))
					return true;
			}
		}
		bigBox[row][col] = 0;
		return false;
	}
	
	
	// Create empty bricks in completely filled matrix
	public void initialSpace(int spaces)
	{
		
		double remainingBricks = Math.pow(n, 4); 	// bricks yet to be traversed
		double emptyBricks = (double)spaces;		// empty bricks to be accomplished
		double brickRatio;							// ratio of remaining and empty bricks to crate randomness
		for(int row=0; row<n*n; row++)
			for(int col=0; col<n*n; col++)
			{
				brickRatio = emptyBricks/remainingBricks;
				if(Math.random() <= brickRatio)
				{
					bigBox[row][col] = 0;	// brick is assigned zero or emptied because random number created for it was less than brickRatio
					emptyBricks--;			// update empty bricks to be accomplished
				}
				remainingBricks--;
			}
	}
	
	
	public void printPuzzle()
	{
		for (int i = 0; i < n*n; i++)
		{
			System.out.print("--");
		}
		System.out.println();
		for(int row = 0; row < n*n; row++)
		{
			for(int col = 0; col < n*n; col++)
			{
				System.out.print(bigBox[row][col]);
				if ((col+1)%n == 0)
				{
					System.out.print("|");
				}
				System.out.print(" ");
			}
			System.out.println();
			if ((row+1)%n == 0)
			{
				for (int i = 0; i < n*n; i++)
				{
					System.out.print("--");
				}
				System.out.println();
			}
		}
		
		System.out.println();
	}
	
}