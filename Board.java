import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

/**
 * Write a description of class Board here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Board
{
    private JPanel board;
    private int a;   // board size
    private JButton [][] tiles;
    private String currentPlayer; //B or W
    private Reversi reversi;
    private int scoreB; //score for black  
    private int scoreW; // score for white

    private boolean canMove;
    /**
     * sets up the game board.
     * takes param of a Reversi class, r, and an int
     * initially creates a default 8x8 board 
     */
    public Board(Reversi r, int size) 
    {
        canMove = true;
        a = size;
        currentPlayer = "B";
        reversi = r;
        board = new JPanel(new GridLayout(a,a));
        tiles = new JButton [a][a];
        for( int x = 0; x < a; x++){
            for( int y = 0; y < a; y++){
                tiles [x][y] = new JButton ("");
                tiles[x][y].addActionListener(new ActionListener()
                    {
                        public void actionPerformed(ActionEvent e)
                        {
                            JButton tile = (JButton) e.getSource();
                            for( int x = 0; x < a; x++){
                                for( int y = 0; y < a; y++){
                                    if(tiles[x][y] == tile){
                                        if(tile.getText() == ""){                                            
                                            capture(x,y);     
                                        }

                                        else{
                                            reversi.changeStatus("Player " + getCurrentPlayer() + " Please pick an empty tile");
                                        }
                                    }
                                }
                            }
                            score();
                            reversi.updateScore();
                            checkEndGame(checkGame());                            
                        }
                    });

                tiles[x][y].setEnabled(false);
                board.add(tiles[x][y]);
            }
        }

        tiles[a/2][a/2].setText("W");
        tiles[a/2][a/2].setBackground(Color.WHITE);
        tiles[a/2][a/2].setForeground(Color.BLACK);
        tiles[(a/2)-1][a/2].setText("B");
        tiles[(a/2)-1][a/2].setBackground(Color.BLACK);
        tiles[(a/2)-1][a/2].setForeground(Color.WHITE);
        tiles[(a/2)-1][(a/2)-1].setText("W");
        tiles[(a/2)-1][(a/2)-1].setBackground(Color.WHITE);
        tiles[(a/2)-1][(a/2)-1].setForeground(Color.BLACK);
        tiles[a/2][(a/2)-1].setText("B");
        tiles[a/2][(a/2)-1].setBackground(Color.BLACK);
        tiles[a/2][(a/2)-1].setForeground(Color.WHITE);

        board.setPreferredSize(new Dimension(1000, 800));
        score();
    } 

    /**
     * returns the board panel for the reversi class
     */
    public JPanel getPanel()
    {
        return board;
    }

    /**
     *method to enable every tile on the board when the play button is clicked 
     */
    public void enableBoard()
    {
        for( int x = 0; x < a; x++){
            for( int y = 0; y < a; y++){
                tiles[x][y].setEnabled(true);
            }
        }
    }

    /**
     * method to update the scores for each player after every turn
     */
    public void score()
    {
        scoreB = 0;
        scoreW = 0;
        for( int x = 0; x < a; x++){
            for( int y = 0; y < a; y++){
                if (tiles [x][y].getText() == "B"){
                    scoreB++;                    
                }
                else if (tiles [x][y].getText() == "W"){
                    scoreW++;  
                }
                else {
                }
            }
        }
    }

    /**
     * returns the current player B or W.
     */
    public String getCurrentPlayer()
    {
        return currentPlayer;
    }

    /**
     * sets the current player B or W from the load data.
     */
    public void setCurrentPlayer(String text)
    {
        currentPlayer = text;
    }

    /**
     * returns the score for Black
     */
    public int Scoreb()
    {
        return scoreB;
    }

    /**
     * returns the score for White
     */
    public int Scorew()
    {
        return scoreW;
    }

    /**
     * method for platers to capture tiles
     * if no tiles are captured, then the move is illegal and an appropriate message will pop up 
     */
    private void capture(int xpos, int ypos)
    {       
        boolean successful = false;
        for( int px = xpos-1; px <= xpos+1; px++){
            for( int py = ypos-1; py <= ypos+1; py++){
                if( px >= 0 && px < a && py >= 0 && py < a ){
                    //only takes tiles in one direction, not all direction    
                    if((tiles[px][py].getText() == "") )
                    {
                        //does nothing
                    }
                    else if(tiles[px][py].getText() != getCurrentPlayer()){
                        int xOffSet = px - xpos;
                        int yOffSet = py - ypos;
                        for( int i = 2; i < a; i++){
                            if(xOffSet*i + xpos >= 0 && xOffSet*i + xpos < a &&   yOffSet*i + ypos >= 0 &&yOffSet*i + ypos < a){

                                if(tiles[xOffSet*i + xpos][yOffSet*i + ypos].getText() == "") {
                                    //stops 
                                    break;
                                }
                                if(tiles[xOffSet*i + xpos][yOffSet*i + ypos].getText() == getCurrentPlayer())
                                {
                                    //successful move
                                    for( int j = i; j >=0 ; j--){
                                        tiles[xOffSet*j + xpos][yOffSet*j + ypos].setText(getCurrentPlayer());
                                        changeTile(xOffSet*j + xpos, yOffSet*j + ypos);
                                    }                                    
                                    tiles[xpos][ypos].setText(getCurrentPlayer()); 
                                    changeTile(xpos, ypos);
                                    successful = true;
                                    break;
                                }
                                else if(tiles[xOffSet*i + xpos][yOffSet*i + ypos].getText() != getCurrentPlayer())
                                {
                                    //carries on in same direction
                                }

                            }
                        }
                    }
                    if( px == xpos && py == ypos){
                        //does nothing
                    }
                }
            }
        }

        if (successful){
            changePlayer();
        }
        else{ // error message to show said player has made an invalid move
            reversi.changeStatus("Invalid move, Player " + getCurrentPlayer()+" turn"); 
        }

    }

    /**
     * changes the colour of the tiles after a capture is successful 
     */
    private void changeTile(int i, int j)
    {
        if(tiles[i][j].getText()=="W")
        {
            tiles[i][j].setBackground(Color.WHITE);
            tiles[i][j].setForeground(Color.BLACK);
        }
        else if(tiles[i][j].getText()=="B")
        {
            tiles[i][j].setBackground(Color.BLACK);
            tiles[i][j].setForeground(Color.WHITE);
        }
        else
        {
            tiles[i][j].setBackground(null);
            tiles[i][j].setForeground(null);
        }

    }

    /**
     * method to change the players turn
     */
    private void changePlayer()
    {
        if(getCurrentPlayer()=="B")
        {
            currentPlayer = "W";
        }
        else{
            currentPlayer = "B";
        }
        reversi.changeStatus("Player " + getCurrentPlayer()+" turn");      
    }

    /**
     * checks if there are any possible moves left on the board after every turn
     * 
     */
    private boolean canMove()
    {
        boolean successful = false;
        for( int x = 0; x < a; x++){
            for( int y = 0; y < a; y++){
                if(tiles[x][y].getText()!= getCurrentPlayer()&&tiles[x][y].getText()=="")
                { 
                    //if it finds a tile of the opposite colour or  a null, it does nothing
                }
                else if (tiles[x][y].getText()== getCurrentPlayer()) {//if it finds a tile of the current player
                    for( int px = x-1; px <= x+1; px++){
                        for( int py = y-1; py <= y+1; py++){ //checks all direction of that one tile
                            if( px >= 0 && px < a && py >= 0 && py < a ){ //checks if its within the limits of the board
                                if((tiles[px][py].getText() == "") )
                                {
                                    //if it finds a null, does nothing
                                }
                                else if(tiles[px][py].getText() != getCurrentPlayer()){ //when it finds a tile of a different colour 
                                    int xOffSet = px - x;
                                    int yOffSet = py - y;
                                    for( int i = 2; i < a; i++){ //finds tiles of a different colour the direction of the offset to the origin 
                                        if(xOffSet*i + x >= 0 && xOffSet*i + x < a &&   yOffSet*i + y >= 0 &&yOffSet*i + y < a){
                                            if(tiles[xOffSet*i + x][yOffSet*i + y].getText() == "") {
                                                //stops when it finds a null, i.e. finds an empty tile for the current player to place.
                                                successful = true;
                                                canMove = true;
                                                break;
                                            }
                                            else if(tiles[xOffSet*i + x][yOffSet*i + y].getText() == getCurrentPlayer())
                                            {
                                                //stops if it finds the next tile to be of the same colour
                                                break;
                                            }

                                            else if(tiles[xOffSet*i + x][yOffSet*i + y].getText() != getCurrentPlayer())
                                            {
                                                //carries on in same direction
                                            }      
                                        }
                                    }
                                }
                                if( px == x && py == y){
                                    //does nothing
                                }
                            }
                        }
                    }
                }
            }
        }
        if (!successful)
        {        
            JOptionPane.showMessageDialog(reversi.getFrame(),"No more moves for Player " + getCurrentPlayer(),"ERROR", JOptionPane.ERROR_MESSAGE);             
            canMove = false;
        }
        return canMove;
    }

    /**
     * checks if any moves are available
     * if non it skips to the next player
     * if skipped twice, end the game
     */
    private void anyMoves()
    {
        canMove();
        if(!canMove)
        {   canMove = true;
            changePlayer();
            canMove();
            if(!canMove)
            {
                JOptionPane.showMessageDialog(reversi.getFrame(),reversi.getWinner(),"GAME OVER", JOptionPane.INFORMATION_MESSAGE);   // shows who the winner is
            }
        }
    }

    /**
     * check if the game has ended, if it has then show end game dialogue
     */

    private void checkEndGame(boolean checkGame)
    {
        if(checkGame){
            JOptionPane.showMessageDialog(reversi.getFrame(), reversi.getWinner(),"GAME OVER", JOptionPane.INFORMATION_MESSAGE); 
        }
    }

    /**
     * checks every tile on the board if there is "" or not
     * if all tiles are filled returns true and ends the game
     */
    private boolean checkGame()
    {        
        for( int x = 0; x < a; x++){
            for( int y = 0; y < a; y++){
                if(tiles[x][y].getText() == "")
                {
                    anyMoves();
                    return false;                    
                }
            }
        }
        return true;
    }

    /**
     *method to save the current sate of the board
     *saves every tile on the board into a single String array 
     */
    public String[] saveBoard()
    {
        String[] boardData = new String[a*a] ;
        for( int x = 0; x < a; x++){
            for( int y = 0; y < a; y++){
                if(tiles[x][y].getText() == "")
                {
                    boardData[x*a+y]= " ";
                }
                else
                {
                    boardData[x*a+y]= tiles[x][y].getText();
                }                
            }
        }
        return boardData;
    }

    public void loadBoard(String text)
    {        
        char[] loadData = text.toCharArray();
        for( int x = 0; x < a; x++){
            for( int y = 0; y < a; y++){
                if(loadData[x*a+y] == ' ')
                {
                    tiles[x][y].setText("");
                }
                else if (loadData[x*a+y] == 'B')
                {
                    tiles[x][y].setText("B");
                    changeTile(x,y);
                }
                else
                {
                    tiles[x][y].setText("W");
                    changeTile(x,y);
                }
            }
        }

    }

}