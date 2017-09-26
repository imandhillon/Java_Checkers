import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.imageio.ImageIO;


public class CheckersGUI extends JPanel
{
    JLabel label;  // Label for displaying messages to the user.
    JLabel colorPicker = new JLabel("Select Tile Colors Below");

    JButton Reset = new JButton("Reset Game");
    JButton GreenYellow = new JButton("Purple and Black");
    JButton Classic = new JButton("Classic Colors");
    JButton RedBlack = new JButton("Red and Black");
    JButton BlackYellow = new JButton("Black and Yellow");

    static BufferedImage GoldKing = null; // crown pictures for kings
    static BufferedImage GarnetKing = null;

    public static void main(String[] args)
    {
        try
        {
            GarnetKing = ImageIO.read(new File("C:\\Users\\austi\\Desktop\\src\\crown.png"));
            GoldKing = ImageIO.read(new File("C:\\Users\\austi\\Desktop\\src\\KHcrown.png"));
        }
        catch (IOException E)
        {
            E.printStackTrace();
        }

        JFrame frame = new JFrame("Checkers Game");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CheckersGUI Game = new CheckersGUI();

        frame.add(Game);

        frame.pack();

        frame.setVisible(true);
    }

    public CheckersGUI()
    {

        JPanel board = new CheckerBoard();

        setLayout(null);

        setPreferredSize(new Dimension(1100, 765));

        add(board);
        add(label);
        add(colorPicker);
        add(Reset);
        add(Classic);
        add(RedBlack);
        add(GreenYellow);
        add(BlackYellow);

        board.setBounds(20, 20, 722, 722);
        label.setBounds(750, 200, 350, 60);
        colorPicker.setBounds(855, 300, 200, 60);
        Reset.setBounds(750, 100, 340, 60);
        Classic.setBounds(850, 350, 150, 30);
        RedBlack.setBounds(850, 400, 150, 30);
        BlackYellow.setBounds(850, 450, 150, 30);
        GreenYellow.setBounds(850, 500, 150, 30);

    }

    class CheckerBoard extends JPanel implements ActionListener, MouseListener  // nested board class
    { // paints board and pieces, handles events

        CheckersGameplay board; // CheckersGameplay class stores the gameplay itself

        int currentPlayer = 1;  // Garnet always starts
        int clickRow = -1;
        int clickColumn = -1;

        Move [] possibleMove; // array that stores possible Move for a given piece

        Color GOLD = new Color(204, 184, 136); // Custom FSU Gold Color
        Color GARNET = new Color(120, 47, 64); // Custom FSU Garnet Color
        Color primary = Color.BLACK;
        Color secondary = Color.WHITE;

        CheckerBoard()
        {
            addMouseListener(this);

            label = new JLabel("", JLabel.CENTER);

            board = new CheckersGameplay();

            addMouseListener(this);
            Reset.addActionListener(this);
            Classic.addActionListener(this);
            RedBlack.addActionListener(this);
            BlackYellow.addActionListener(this);
            GreenYellow.addActionListener(this);

            possibleMove = board.getMove(1);  // get Garnet's first Move

            label.setText("Garnet's Turn. Please make a move.");

            repaint();
        }

        void gameOver(String str)
        {
            label.setText(str);
        }

        void takeTurn(Move move)
        {
            int sr = move.startRow;
            int sc = move.startCol;
            int er = move.endRow;
            int ec = move.endCol;
            int tempjump = sr - er;

            board.makeMove(sr, sc, er, ec);

            if (tempjump == 2 || tempjump == -2) // if the move just made was a jump, you must check for a possible second jump
            {
                // start by getting new list of possible jumps from the piece's new location
                possibleMove = board.getJumps(currentPlayer, move.endRow, move.endCol);

                if (possibleMove != null) // if the new Move array is not empty, then there is a double jump possible
                {
                    if (currentPlayer == 1)
                    {
                        label.setText("Garnet's Turn. A second jump is possible. You must take it.");
                    }
                    else
                    {
                        label.setText("Gold's Turn. A second jump is possible. You must take it.");
                    }

                    clickRow = move.endRow;  // set the piece to be the "clicked piece" since it has to jump again
                    clickColumn = move.endCol;

                    repaint();

                    return; // if this happens end the function here
                }
            }

            if (currentPlayer == 1) // switch who's turn it is, check for game over
            {
                currentPlayer = 3;
                possibleMove = board.getMove(currentPlayer); // check to see if Gold still has any Move left

                if (possibleMove == null) // if Gold has no Move left, Garnet wins
                {
                    gameOver("Gold has no possible Move left. Garnet wins!");
                }
                else if (possibleMove[0].jumpCheck()) // Gold has a jump to make
                {
                    label.setText("Gold's Turn. Please make your move. You must jump.");
                }
                else // Gold has non-jump Move to make
                {
                    label.setText("Gold's Turn. Please make your move.");
                }
            }
            else if (currentPlayer == 3)
            {
                currentPlayer = 1;
                possibleMove = board.getMove(currentPlayer);

                if (possibleMove == null) // Garnet has no possible Move, Gold wins
                {
                    gameOver("Garnet has no possible Move left. Gold wins!");
                }
                else if (possibleMove[0].jumpCheck()) // Garnet can make a jump
                {
                    label.setText("Garnet's Turn. Please make your move. You must jump.");
                }
                else // Garnet has non-jump Move still possible
                {
                    label.setText("Garnet's Turn. Please make your move.");
                }
            }

            clickRow = -1; // reset clicked row

            repaint();
        }

        public void paint(Graphics G)
        {
            Graphics2D g = (Graphics2D) G;

            int piece;

            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 725, 725);

            for (int i = 0; i < 8; i++)  // draw the checkerboard pattern first
            {
                for (int j = 0; j < 8; j++)
                {
                    if (((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1))) //if i and j are both even or both odd
                    {
                        g.setColor(secondary);
                        g.fillRect(2 + j * 90, 2 + i * 90, 180, 180);
                    }
                    else
                    {
                        g.setColor(primary);
                        g.fillRect(2 + j * 90, 2 + i * 90, 180, 180);
                    }
                }
            }// checker pattern drawn

            for (int i = 0; i < 8; i++)
            {
                for(int j = 0; j < 8; j++)
                {
                    piece = board.board[i][j];

                    if (piece == 1 || piece == 2) // Draw Garnet Pieces
                    {
                        g.setColor(GARNET);
                        g.fillOval(4 + j * 90, 4 + i * 90, 85, 85);

                        if (piece == 2) // is it a King? - if yes, draw 'K' on piece
                        {
                            g.setColor(GARNET);
                            g.fillOval(4 + j * 90, 4 + i * 90, 85, 85);
                            g.drawImage(GarnetKing, (j * 90) + 10, (i * 90) + 8, 75, 75, null);
                        }

                    }
                    else if (piece == 3 || piece == 4) // Draw Gold Pieces
                    {
                        g.setColor(GOLD);
                        g.fillOval(4 + j * 90, 4 + i * 90, 85, 85);

                        if (piece == 4) // Gold King
                        {
                            g.setColor(GOLD);
                            g.fillOval(4 + j * 90, 4 + i * 90, 85, 85);
                            g.drawImage(GoldKing, (j * 90) + 10, (i * 90) + 8, 75, 75, null);
                        }
                    }
                }
            }

            for (int i = 0; i < 8; i++)
            {
                if (clickRow > -1) // when a player picks a piece, draw a red border around it to show it is selected
                {                   // also highlight tiles that selected piece can move
                    g.setColor(Color.BLUE);
                    g.drawRect(1 + clickColumn * 90, 1 + clickRow * 90, 90, 90);

                    for (int z = 0; z < possibleMove.length; z++)
                    {
                        if (possibleMove[z].startCol == clickColumn)
                        {
                            if (possibleMove[z].startRow == clickRow)
                            {
                                g.setColor(Color.BLUE);
                                g.fillRect(2 + possibleMove[z].endCol * 90, 2 + possibleMove[z].endRow * 90, 90, 90);
                            }
                        }
                    }
                }
            }
        }

        public void actionPerformed(ActionEvent evt)
        {
            if(evt.getSource() == Reset)
            {
                board.board[3][0] = 0;
                board.board[3][1] = 0;
                board.board[3][2] = 0;
                board.board[3][3] = 0;
                board.board[3][4] = 0;
                board.board[3][5] = 0;
                board.board[3][6] = 0;
                board.board[3][7] = 0;
                board.board[4][0] = 0;
                board.board[4][1] = 0;
                board.board[4][2] = 0;
                board.board[4][3] = 0;
                board.board[4][4] = 0;
                board.board[4][5] = 0;
                board.board[4][6] = 0;
                board.board[4][7] = 0;

                // place Gold Pieces
                board.board[0][1] = 3;
                board.board[0][3] = 3;
                board.board[0][5] = 3;
                board.board[0][7] = 3;
                board.board[1][0] = 3;
                board.board[1][2] = 3;
                board.board[1][4] = 3;
                board.board[1][6] = 3;
                board.board[2][1] = 3;
                board.board[2][3] = 3;
                board.board[2][5] = 3;
                board.board[2][7] = 3;

                // place Garnet pieces
                board.board[7][0] = 1;
                board.board[7][2] = 1;
                board.board[7][4] = 1;
                board.board[7][6] = 1;
                board.board[6][1] = 1;
                board.board[6][3] = 1;
                board.board[6][5] = 1;
                board.board[6][7] = 1;
                board.board[5][0] = 1;
                board.board[5][2] = 1;
                board.board[5][4] = 1;
                board.board[5][6] = 1;

                label.setText("Garnet's Turn. Please make your move.");
                currentPlayer = 1;
                clickRow = -1;
                clickColumn = -1;
                possibleMove = board.getMove(currentPlayer);
                repaint();
            }
            else if (evt.getSource() == Classic)
            {
                primary = Color.BLACK;
                secondary = Color.WHITE;
                repaint();
            }
            else if (evt.getSource() == RedBlack)
            {
                primary = Color.BLACK;
                secondary = Color.RED;
                repaint();
            }
            else if (evt.getSource() == BlackYellow)
            {
                primary = Color.BLACK;
                secondary = Color.YELLOW;
                repaint();
            }
            else if (evt.getSource() == GreenYellow)
            {
                primary = Color.BLACK;
                secondary = new Color(139, 0, 139);
                repaint();
            }
        }

        public void mousePressed(MouseEvent evt)
        {
            int row = evt.getY() / 90;
            int col = evt.getX() / 90;

            if (row >= 0 && row < 8 && col >= 0 && col < 8)
            {
                for (int i = 0; i < possibleMove.length; i++)
                {
                    if (possibleMove[i].startRow == row && possibleMove[i].startCol == col)
                    {
                        clickRow = row;
                        clickColumn = col;

                        if (currentPlayer == 1)
                        {
                            label.setText("Garnet's Turn. Please make a move.");
                        }
                        else if (currentPlayer == 3)
                        {
                            label.setText("Gold's Turn. Please make a move.");
                        }

                        repaint();

                        return;
                    }
                }
                for (int i = 0; i < possibleMove.length; i++)
                {
                    if (possibleMove[i].startRow == clickRow && possibleMove[i].startCol == clickColumn)
                    {
                        if (possibleMove[i].endRow == row && possibleMove[i].endCol == col)
                        {
                            takeTurn(possibleMove[i]);
                            break;
                        }
                    }
                }
            }
        }

        //Required Event Handlers Here
        public void mouseReleased(MouseEvent evt) {}
        public void mouseClicked(MouseEvent evt) {}
        public void mouseEntered(MouseEvent evt) {}
        public void mouseExited(MouseEvent evt) {}
        //End Required Event Handlers
    }
}



