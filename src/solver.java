
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
public class solver {
	
// This is my main method.It starts by creating a 2D array for our board and a boolean variable called runner responsible for
// The sentinel loop the rest of the method is enclosed in. Next it asks the user what puzzle it would like it to solve and
// reads the user input and then passes it into our importSuduko(), which initializes the board. We then create a HashMap with
// a pointer integer and our object sudCell which gets initialized by calling our initialScan method. Next, we call our sudSolver method
// which updates the board to be solved and then print out the board with our toString(). Lastly we ask the user if they would like 
// to see another puzzle solved and close or rerun the loop according to their input.
	public static void main(String[] args) throws FileNotFoundException {
		boolean runner= true;
		int[][] board = new int[9][9];
		int userInput=0;
		do {
			Scanner scan = new Scanner(System.in);
			System.out.println("What Puzzle would you like me to solve today?");
			System.out.println("Press 1 for Puzzle 1");
			System.out.println("Press 2 for Puzzle 2");
			System.out.println("Press 3 for Puzzle 3");
			System.out.println("Press 4 for Puzzle 4");
			System.out.println("Press 5 for Puzzle 5");
			System.out.println("Or press any other number for a suprise!");
			userInput = scan.nextInt();
		board = importSuduko(board, userInput);
		toString(board);
		HashMap<Integer, sudCell> h1 = initialScan(board);
		sudSolver(board, h1, 0);
		toString(board);
		System.out.println("Would you like me to go again?");
		System.out.println("Press 1 for Yes or 2 for No");
		int input = scan.nextInt();
		if(input == 1) {
			runner = true;
		}
		else if(input == 2) {
			scan.close();
			runner = false;
			
		}
		else {
			System.out.println("You aren't very good at following instructions huh? Oh well. Bye for now");
			scan.close();
			runner = false;
			
			
		}
		}
		while(runner == true);
	}
		
		
	
	// This is my method responsible for taking in the board as a text file
	// and returning it as a 2D array. It takes in user input to determine which puzzle to initialize the board as.
	public static int[][] importSuduko(int[][] board, int num) throws FileNotFoundException {
		String fileName;
		if (num == 1) {
			fileName = "Suduko1.txt";
		}
		else if (num == 2) {
			fileName = "Suduko2.txt";
		}
		else if (num == 3) {
			fileName = "Suduko3.txt";
		}
		else if (num == 4) {
			fileName = "Suduko4.txt";
		}
		else if (num == 5) {
			fileName = "Suduko5.txt";
		}
		else {
			System.out.println("Since you seemed to enter a wrong input, we decided to give you a random puzzle :)");
			fileName = "SurpriseSuduko.txt";
		}
		File File = new File(fileName);
		Scanner scan = new Scanner(File);
		for (int i =0; i<9; i++) {
			for (int j=0; j<9; j++) {
				board[i][j]= scan.nextInt();
			}
			
		}
		scan.close();
		return board;
	}
	// This is my positionCheck method. It works by first initializing a variable called count and current.
	// Next, we go into our nested for loop where the outer loop is responsible for iterating the count up each time when
	// the nested one completes and the middle one is responsible for scanning all the indexes in our HashMap of empty cells.
	// Each index of our HashMap is tested against the if statement that checks to see if:
	// 1) The current number of possible values we can insert in the current cell is equal to our count variable
	// and 2) That the current value of the current cell is equal to 0 and not already initialized.
	// If the current cell meets these two if statements, we assign the value to current and break out of the for loop by
	// changing our i and j values. Lastly we return the index (in the hashMap) of the position that is the first one found
	// that is of the lowest cost. 
	// We are evaluating the cost of each cell by prioritizing the cells that have the least amount of possibilities in them
	// and initializing them first. This is what the count variable is used for. It first looks for cells that have only one
	// possible number in them (as these will never change and can be used to limit the possibilities of other cells that may have more)
	// Thus on each completion of the inner for loop, we iterate count up by one because we could not find a cell that had x number
	// of possibilities so we will next check them all for x+1 and so on until we cannot find a cell that is empty
	// If we cannot find a cell that is empty, current the variable that returns the index of the next cell to be initialized in the HashMap
	// is returned as its initial value of 100 and this will come into play in a later method, our sudSolver().
	public static int positionCheck(int [][]board, HashMap<Integer, sudCell> h1){
		int count = 1;
		int current= 100;
		for(int j = 0; j<9; j++) {
			for(int i=0; i< h1.size(); i++) {
				if(numPossible(h1.get(i).row, h1.get(i).column, board)== count && board[h1.get(i).row][h1.get(i).column]==0) {
					current = i;
					i =h1.size() + 10;
					j = 11;
				}
			}
			count++;
			}
		return current;
	}
	// This is our initalScan method. It's purpose is to initialize our HashMap with all the cells (and their information)
	// we need to update to complete the puzzle. It first creates a HashMap which stores pairs of pointer indexes as integers
	// and their corresponding sudCell objects. We next initialize a variable called count as 0.
	// We then go into a nested for loop that is responsible for iterating through every cell of the board.
	// We test each cell against an if statement that checks to see if the current value of the cell is equal to zero (empty)
	// If so, we create a new Variable called cellName that intakes our current value of count.
	// This will be our pointer index to this cell in our HashMap.
	// We then add a new pair to our HashMap and initialize all of its corresponding data
	// cellName = the index of the sudCell in the HashMap
	// i = row, j = column, 0 = initialValue
	// To get the number of possibilities for that cell we call numPossible and initialize the returning integer.
	// To get the possibilities of that cell we call possibleArr and initialize the returning array.
	// We then iterate count up so it is ready for the next index of the next pair
	// Lastly we return our HashMap
	public static HashMap<Integer, sudCell> initialScan (int [][]board) {
		HashMap<Integer, sudCell> cellTrack = new HashMap<Integer, sudCell>();
		int count = 0;
		for (int i =0; i < 9; i++) {
			for (int j=0; j<9; j++) {
				if(board[i][j]==0) {
					int cellName = count;
					cellTrack.put(cellName, new sudCell(i, j, 0, numPossible(i, j, board), possibleArr(i, j, board)));
					count++;
				}
			}
			}
		return cellTrack;
	}
	// This is my numPossible method. It is responsible for returning the number of possible values that can go in each cell
	// First we initialize a count variable as zero. Then we enter a for loop that tests each value 1 through 9 to see if it can 
	// fit in the cell by calling the combineChecks method.
	// If it can, we iterate count up by one. If it can't, we move on and check the next value.
	// Lastly, after all possible values have been tested, we return the count which is the number of possible values for that cell.
	public static int numPossible(int row, int column, int[][] board) {
		int count = 0;
		for(int i = 1; i<10; i++) {
			if (combineChecks(row, column, i, board)== true) {
				count++;
			}
		}
		return count;
		
	}
	// This is my possibleArr method. It is responsible for returning an Array containing all possible numbers that can go in a specific cell.
	// First we create an Array called arr and use the numPossible method to determine the appropriate size for it.
	// Next we create and initialize a integer variable called count to 0. Count will be responsible for keeping track of our
	// current index in the Array. 
	// Next we enter a for loop that iterates through all possible values that can go in a cell (1->9).
	// We check to see if each value is able to be placed in the cell by calling our combineChecks method.
	// If it is able to be placed in the cell, we add it to our array in the index of count and increment count up by one.
	// Lastly we return this array with all the possible values in it.
	public static int[] possibleArr(int row, int column, int[][] board) {
		int[] arr = new int[numPossible(row, column, board)];
		int count = 0;
		for (int i =1; i<10; i++) {
			if(combineChecks(row, column, i, board)==true) {
				arr[count] = i;
				count++;
			}
		}
		return arr;
	}
	// This is my rowCheck method which just checks to see if a given value
	// is present in a certain row and returns a boolean.
	public static boolean rowCheck(int row, int value, int [][]board) {
		for(int i=0; i<9; i++) {
			if (board[row][i] == value) {
				return true;
			}
		}
		return false;
	}
	// This is my columnCheck method which just checks to see if a given value
		// is present in a certain column and returns a boolean.
	public static boolean columnCheck(int column, int value, int[][] board) {
		for (int i =0; i<9; i++) {
			if (board[i][column]== value) {
				return true;
			}
		}
		return false;
	}
	// This is my sectionCheck method which just checks to see if a given value
		// is present in a certain section (or 3x3 square) and returns a boolean.
	public static boolean sectionCheck(int row, int column, int value, int[][] board) {
		int rowSection= row- row %3;
		int colSection= column - column %3;
		for(int i=0; i<3; i++) {
			for(int j=0; j<3; j++ ) {
				if(board[rowSection +i][colSection+j]== value) {
					return true;
				}
			}
		}
		return false;
	}
	// This is my combineChecks method which just checks to see all the previous
	// checks are false and if so returns true meaning a number can be placed
	// in said position.
	public static boolean combineChecks(int row, int column, int value, int[][] board) {
		if (rowCheck(row, value, board) == false && columnCheck(column, value, board) == false && sectionCheck(row, column, value, board) == false) {
			return true;
		}
		else {
			return false;
		}
		
		
	}
	// This is my sudSolver method which is responsible for actually solving the puzzle through recursion.
	// First we in take the first position we find with the lowest number of possibilities into an integer variable called count
	// which corresponds to the index of the sudCell in our HashMap (this is done by calling positionCheck). 
	// Next we create a boolean nextCall variable which will be initialized later.
	// Next since this is a recursive method, we have our ending condition in the form of an if statement
	// This if statement checks to see if current == 100, which means that when we called positionCheck and no empty cells could be found.
	// When this is true, we print out a statement that says the amount of recursions that took place and return true to cease the recursion.
	// Next we have a for loop that iterates through all possible values in the current sudCell's array of possible numbers
	// We check each of these numbers to see if they are possible to be placed in the cell currently by calling our combineChecks method
	// (Note: some possible values may be possible during initialScan but not once other cells are updated)
	// If they are possible to be placed (ie: combineChecks returns true), then we assign this value to the board
	// as well as update the current sudCell's currentValue to this new value
	// We next iterate up RecursionCheck (which keeps track of how many recursive calls we have made)
	// Then we initialize nextCall by recursively calling sudSolver again
	// Below this we have another end point for a specific recursive call that returns true if the previous call of sudSolver returned true
	// (this is so we do not reach the code below it in this case and do not update values of the board back to 0)
	// In the case that the previous call does not return true, we keep iterating the for loop starting where we left off but before
	// the next for loop iteration we change the values on the board we had previously updated back to 0 as well as the sudCell's
	// currentValue variable.
	// Lastly, in the event we complete the entire for loop without returning true, we return false as we 
	// could not find a variable in the the array of possible numbers for this current cell that we could actually place on the board
	// and we return to the previous call to update the previous cell again.
	public static boolean sudSolver(int[][] board, HashMap<Integer, sudCell> h1, int RecursionCheck) {
			int current = positionCheck(board, h1);
			boolean nextCall;
			if (current == 100) {
				System.out.println("Total number of recursions: " + RecursionCheck);
				return true;
			}

			for(int i = 0; i<h1.get(current).possible.length; i++) {
				if(combineChecks(h1.get(current).row, h1.get(current).column, h1.get(current).possible[i], board)== true) {
					board[h1.get(current).row][h1.get(current).column] = h1.get(current).possible[i];
					h1.get(current).currentValue = h1.get(current).possible[i];
					RecursionCheck++;
					nextCall = sudSolver(board, h1, RecursionCheck);
					if(nextCall == true) {
						return true;
					}
					board[h1.get(current).row][h1.get(current).column] = 0;
					h1.get(current).currentValue = 0;
				}
			}
			return false;
			
		}
		
		
	
	//This is my toString method to print out the board.
	public static void toString(int[][] board) {
		System.out.println(" ");
		System.out.println(" ");
		System.out.println("_________________________");
		System.out.println("");
		for (int i =0; i < 9; i++) {
			System.out.print("| ");
			for (int j=0; j<9; j++) {
				System.out.print(board[i][j]+ " ");
				if ((j+1)%3 == 0 ) {
					System.out.print("| ");
				}
			}
			if((i+1)%3 ==0) {
				System.out.println("");
				System.out.println("_________________________");
			}
			System.out.println("");
		}
	}

}

