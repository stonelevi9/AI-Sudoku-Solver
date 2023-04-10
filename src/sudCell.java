// This is my sudCell class that basically just comprises of an overloaded constructor for creating objects of the type sudCell
// and updating all corresponding variables for them
// row and column are integer values of the corresponding row and column values in our 2D array board
// currentValue = the currentValue of the cell on the board
// numPossible = the number of possible values we can place in this cell ie: if we can place 1 and 7 in the cell then it = 2.
// possible = an array that stores all possible values that can be placed in this specific sudCell.
public class sudCell {
	int row, column, currentValue, numPossible;
	int[] possible;
	sudCell(int row, int column, int currentValue, int numPossible, int[] possible){
		this.row = row;
		this.column = column;
		this.currentValue = currentValue;
		this.possible = possible;
		this.numPossible = numPossible;
	}
	public sudCell() {
		// TODO Auto-generated constructor stub
	}
}
