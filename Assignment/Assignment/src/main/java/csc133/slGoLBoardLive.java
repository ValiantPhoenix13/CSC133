package csc133;

public class slGoLBoardLive extends slGoLBoard{
    private int my_row, next_row, previous_row, my_column, next_column, previous_column, myCount;

    public slGoLBoardLive(int numRows, int numCols) {
        super(numRows, numCols);
    }

    @Override
    public int countLiveTwoDegreeNeighbors(int row, int col){
        myCount = 0;
        my_row = row;
        my_column = col;
        next_row = (my_row + 1)%NUM_ROWS;
        next_column = (my_column + 1)%NUM_COLS;
        previous_row = (my_row - 1 + NUM_ROWS) % NUM_ROWS;
        previous_column = (my_column - 1 + NUM_COLS) % NUM_COLS;
            if (liveCellArray[previous_row][next_column]) {
                myCount++;
            }
            if (liveCellArray[previous_row][my_column]) {
                myCount++;
            }
            if (liveCellArray[previous_row][previous_column]) {
                myCount++;
            }
            if (liveCellArray[my_row][previous_column]) {
                myCount++;
            }
            if (liveCellArray[next_row][previous_column]) {
                myCount++;
            }
            if (liveCellArray[next_row][my_column]) {
                myCount++;
            }
            if (liveCellArray[next_row][next_column]) {
                myCount++;
            }
            if (liveCellArray[my_row][next_column]) {
                myCount++;
            }
            return myCount;

    }
    @Override
    public int updateNextCellArray(){
        int retVal = 0;

        int nln = 0;  // Number Live Neighbors
        boolean ccs = true; // Current Cell Status
        for (int row = 0; row < NUM_ROWS - 1; ++row){
            for (int col = 0; col < NUM_COLS - 1; ++col) {
                ccs = liveCellArray[row][col];
                nln = countLiveTwoDegreeNeighbors(row, col);
                if (!ccs && nln == 3) {
                    nextCellArray[row][col] = true;
                    ++retVal;
                } else {
                    // Current Cell Status is true
                    if (nln < 2 || nln > 3) {
                        nextCellArray[row][col] = false;
                    } else {
                        // nln == 2 || nln == 3
                        nextCellArray[row][col] = true;
                        ++retVal;
                    }
                }
            }  // for (int row = 0; ...)
        }  //  for (int col = 0; ...)

        boolean[][] tmp = liveCellArray;
        liveCellArray = nextCellArray;
        nextCellArray = tmp;

        return retVal;
    }  //  int updateNextCellArray()
    }

