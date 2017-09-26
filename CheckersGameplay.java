import java.util.ArrayList;

/*
    Back-End Class
    Stores Information about the gameplay itself
    Holds 2d board array
    Board setup functions
    Get possible Move functions (one for just jumps)
    Take turn functions and helpers
*/

class CheckersGameplay
{
    /*
        INT Representations in 2d array:
        Empty Tile = 0
        Garnet Occupied Tile = 1
        Garnet King Occupied Tile = 2
        Gold Occupied Tile = 3
        Gold King Occupied Tile = 4
    */
    int[][] board;  // board[r][c] is the contents of row r, column c.

    CheckersGameplay()
    {
        board = new int[8][8];

        for (int x = 0; x < 8; x++)
        {
            for (int y = 0; y < 3; y++)
            {
                board[x][y] = 0; // fill with 0's
            }
        }

        // place Gold Pieces
        board[0][1] = 3;
        board[0][3] = 3;
        board[0][5] = 3;
        board[0][7] = 3;
        board[1][0] = 3;
        board[1][2] = 3;
        board[1][4] = 3;
        board[1][6] = 3;
        board[2][1] = 3;
        board[2][3] = 3;
        board[2][5] = 3;
        board[2][7] = 3;

        // place Garnet pieces
        board[7][0] = 1;
        board[7][2] = 1;
        board[7][4] = 1;
        board[7][6] = 1;
        board[6][1] = 1;
        board[6][3] = 1;
        board[6][5] = 1;
        board[6][7] = 1;
        board[5][0] = 1;
        board[5][2] = 1;
        board[5][4] = 1;
        board[5][6] = 1;
    }

    void makeMove(int startRow, int startColumn, int endRow, int endCol)
    {
        // this function assumes a valid move is being passed in
        // aka no error checking on the parameters

        board[endRow][endCol] = board[startRow][startColumn];
        board[startRow][startColumn] = 0;  // move piece from start location to target location

        int jumped = startRow - endRow;

        if (jumped == 2 || jumped == -2) // check if the move is a jump
        {                                                  // if it is, remove the jumped piece
            int jumpRow = (startRow + endRow) / 2;
            int jumpCol = (startColumn + endCol) / 2;

            board[jumpRow][jumpCol] = 0; // set position of jumped piece to empty
        }

        if (endRow == 0 && board[endRow][endCol] == 1) // check for Garnet "king me"
        {
            board[endRow][endCol] = 2;
        }

        if (endRow == 7 && board[endRow][endCol] == 3) // check for Gold "king me"
        {
            board[endRow][endCol] = 4;
        }

    }

    Move [] getMove(int color)
    {
        int king;

        if (color == 1)
            king = 2;
        else
            king = 4;

        ArrayList<Move> Move = new ArrayList<Move>();  // possible Move will be temporarily stored in this list.

        Move temp = new Move(0,0,0,0);

        boolean foundJump = false;

        for (int row = 0; row < 8; row++)  // jumps are mandatory so check for those first
        {
            for (int col = 0; col < 8; col++)
            {
                // for each piece, check for a jump in every direction, if one is possible add it to the list
                if (board[row][col] == color || board[row][col] == king)
                {
                    if (isJumpPosssible(row, col, row+1, col+1, row+2, col+2))
                    {
                        temp.startRow = row;
                        temp.startCol = col;
                        temp.endRow = (row + 2);
                        temp.endCol = (col + 2);
                        Move.add(temp);
                        foundJump = true;
                        temp = new Move(0,0,0,0);
                    }
                    if (isJumpPosssible(row, col, row-1, col+1, row-2, col+2))
                    {
                        temp.startRow = row;
                        temp.startCol = col;
                        temp.endRow = (row - 2);
                        temp.endCol = (col + 2);
                        Move.add(temp);
                        foundJump = true;
                        temp = new Move(0,0,0,0);
                    }
                    if (isJumpPosssible(row, col, row+1, col-1, row+2, col-2))
                    {
                        temp.startRow = row;
                        temp.startCol = col;
                        temp.endRow = (row + 2);
                        temp.endCol = (col - 2);
                        Move.add(temp);
                        foundJump = true;
                        temp = new Move(0,0,0,0);
                    }
                    if (isJumpPosssible(row, col, row-1, col-1, row-2, col-2))
                    {
                        temp.startRow = row;
                        temp.startCol = col;
                        temp.endRow = (row - 2);
                        temp.endCol = (col - 2);
                        Move.add(temp);
                        foundJump = true;
                        temp = new Move(0,0,0,0);
                    }
                }
            }
        }

        if (foundJump == false) // if there were not any possible jumps, then make a list of regular Move
        {
            for (int row = 0; row < 8; row++)
            {
                for (int col = 0; col < 8; col++)
                {
                    if (board[row][col] == color || board[row][col] == king)
                    {
                        if (isMovePosssible(row, col,row+1,col+1))
                        {
                            temp.startRow = row;
                            temp.startCol = col;
                            temp.endRow = (row + 1);
                            temp.endCol = (col + 1);
                            Move.add(temp);
                            temp = new Move(0,0,0,0);
                        }
                        if (isMovePosssible(row, col,row-1,col+1))
                        {
                            temp.startRow = row;
                            temp.startCol = col;
                            temp.endRow = (row - 1);
                            temp.endCol = (col + 1);
                            Move.add(temp);
                            temp = new Move(0,0,0,0);
                        }
                        if (isMovePosssible(row, col,row+1,col-1))
                        {
                            temp.startRow = row;
                            temp.startCol = col;
                            temp.endRow = (row + 1);
                            temp.endCol = (col - 1);
                            Move.add(temp);
                            temp = new Move(0,0,0,0);
                        }
                        if (isMovePosssible(row, col,row-1,col-1))
                        {
                            temp.startRow = row;
                            temp.startCol = col;
                            temp.endRow = (row - 1);
                            temp.endCol = (col - 1);
                            Move.add(temp);
                            temp = new Move(0,0,0,0);
                        }
                    }
                }
            }
        }

        int size = Move.size();
        Move [] tempArray;

        if (size == 0)
        {
            return null; // this should only happen when the game is over/ending
        }
        else
        {
            tempArray = new Move[size]; // move contents of the ArrayList into a regular array to be returned

            for (int i = 0; i < size; i++)
            {
                tempArray[i] = Move.get(i);
            }
        }

        return tempArray;
    }

    Move[] getJumps(int color, int row, int col)  // similar to getMove, used for determining double jumps
    {                                              // only returns possible jumps from given location (row,col)

        int king;

        if (color == 1)
            king = 2;
        else
            king = 4;

        ArrayList<Move> Move = new ArrayList<Move>();

        Move temp = new Move(0,0,0,0);

        if (board[row][col] == color || board[row][col] == king)
        {
            if (isJumpPosssible(row, col, row+1, col+1, row+2, col+2))
            {
                temp.startRow = row;
                temp.startCol = col;
                temp.endRow = (row + 2);
                temp.endCol = (col + 2);
                Move.add(temp);
                temp = new Move(0,0,0,0);
            }
            if (isJumpPosssible(row, col, row-1, col+1, row-2, col+2))
            {
                temp.startRow = row;
                temp.startCol = col;
                temp.endRow = (row - 2);
                temp.endCol = (col + 2);
                Move.add(temp);
                temp = new Move(0,0,0,0);
            }
            if (isJumpPosssible(row, col, row+1, col-1, row+2, col-2))
            {
                temp.startRow = row;
                temp.startCol = col;
                temp.endRow = (row + 2);
                temp.endCol = (col - 2);
                Move.add(temp);
                temp = new Move(0,0,0,0);
            }
            if (isJumpPosssible(row, col, row-1, col-1, row-2, col-2))
            {
                temp.startRow = row;
                temp.startCol = col;
                temp.endRow = (row - 2);
                temp.endCol = (col - 2);
                Move.add(temp);
                temp = new Move(0,0,0,0);
            }
        }

        int size = Move.size();
        Move [] tempArray;

        if (size == 0)
        {
            return null;
        }
        else
        {
            tempArray = new Move[size];
            for (int i = 0; i < size; i++)
            {
                tempArray[i] = Move.get(i);
            }
        }

        return tempArray;
    }

    boolean isJumpPosssible(int startR, int startC, int enemyR, int enemyC, int endR, int endC)
    {       // helper to getMove and getJumps

        if (endR < 0 || endR > 7 || endC < 0 || endC > 7)
        {
            return false;  // jump not possible, end location is off the board
        }

        if (board[endR][endC] != 0)
        {
            return false;  // jump not possible, end location already occupied
        }

        int color = board[startR][startC];

        if (color == 1 || color == 2) // Garnet
        {
            if (board[startR][startC] == 1 && endR > startR)
            {
                return false;  // non-king Garnet pieces can only move up the board
            }

            if (board[enemyR][enemyC] != 3 && board[enemyR][enemyC] != 4)
            {
                return false;  // there is no Gold piece to jump in between the start and end tiles
            }

            return true;  // found valid jump
        }
        else if (color == 3 || color == 4) // Gold
        {
            if (board[startR][startC] == 3 && endR < startR)
            {
                return false;  // non-king Gold only move down
            }
            if (board[enemyR][enemyC] != 1 && board[enemyR][enemyC] != 2)
            {
                return false;  // there isn't a Garnet piece to jump
            }

            return true;  // valid jump
        }

        return false; // required return statement
    }

    boolean isMovePosssible(int startR, int startC, int endR, int endC) // helper to getMove only
    {
        if (endR < 0 || endR > 7 || endC < 0 || endC > 7)
        {
            return false;  // end location id off the board
        }

        if (board[endR][endC] != 0)
        {
            return false;  // end location is already occupied
        }

        int color = board[startR][startC];

        if (color == 1 || color == 2)
        {
            if (board[startR][startC] == 1 && endR > startR)
            {
                return false;  // Garnet pieces cannot move down the board
            }

            return true;  // legal move found
        }
        else if (color == 3 || color == 4)
        {
            if (board[startR][startC] == 3 && endR < startR)
            {
                return false;  // Gold pieces cannot move up the board
            }

            return true;  // legal move found
        }

        return false;
    }
}

class Move
{
    /*
        Simple class to hold info about a "move"
        Stores starting and ending locations of a piece that is being moved
     */

    int startRow;
    int startCol;
    int endRow;
    int endCol;

    Move(int sr, int sc, int er, int ec) {
        startRow = sr;
        startCol = sc;
        endRow = er;
        endCol = ec;
    }

    boolean jumpCheck()// move is a jump if start and end locations are two tiles away from each other
    {
        int jump = startRow - endRow;

        return (jump == 2 || jump == -2);
    }
}