import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.nio.*;
/**
 * Write a description of class Board here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Reversi
{
    private Container contentPane;
    private Board b;
    private JFrame frame;
    private JPanel board;
    private JLabel statusLabel;
    private JPanel playerName;
    private JPanel flow; 

    private JLabel player1;
    private JLabel player1Name;
    private JTextField name1;
    private JLabel score1;
    private JLabel games1;
    private JLabel colour1;
    private JButton enter1;
    private JButton enter2;

    private JButton play;

    private JLabel player2;
    private JLabel player2Name;
    private JTextField name2;
    private JLabel score2;
    private JLabel games2;
    private JLabel colour2;
    private int gamesWon1;
    private int gamesWon2;
    private int boardSize;
    /**
     * Constructor for objects of class Reversi
     */
    public Reversi()
    {
        boardSize = 8;
        makeFrame();       
    }

    public JFrame getFrame()
    {
        return frame;
    }

    private void makeFrame()
    {
        frame = new JFrame("Reversi");

        makeMenuBar(frame);
        contentPane = frame.getContentPane();

        //creates the board panel
        makeBoard();

        //name and score
        makePlayerPanel();

        //status bar 
        statusBar();
        contentPane.add(statusLabel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
    }

    // -------- sets up status --------

    private void statusBar()
    {
        statusLabel = new JLabel("Ready");
    }

    /**
     *changes the status bar at the bottom of the panel
     *takes param of String text
     */
    public void changeStatus(String text)
    {
        statusLabel.setText(text);
    }

    // -------- sets up board --------
    public void makeBoard()
    {
        b = new Board(this, boardSize);
        board = b.getPanel();
        contentPane.add(board, BorderLayout.CENTER);
    }

    // -------- sets up player and score panel --------

    private void makePlayerPanel()
    {
        //set up buttons for player 1
        playerName = new JPanel(new GridLayout(0,1));
        
        player1= new JLabel("Player 1:");
        player1Name= new JLabel("");
        score1= new JLabel("Player 1 Score: " + b.Scoreb());
        name1 = new JTextField(20);
        enter1 = new JButton("ENTER");      
        colour1= new JLabel("BLACK");
        games1 = new JLabel("Player 1 Games: " + gamesWon1);    

        JButton newSession = new JButton("START A NEW SESSION");
        play = new JButton("PLAY");
        play.setEnabled(false);

        //set up buttons for player 2
        name2 = new JTextField(20);
        enter2 = new JButton("ENTER");
        score2= new JLabel("Player 2 Score: " +b.Scorew());
        player2= new JLabel("Player 2: ");
        player2Name= new JLabel("");
        colour2= new JLabel("WHITE");
        games2 = new JLabel("Player 2 Games: " + gamesWon2);

        //Sets up layout for player 1(top)
        playerName.add(name1);
        playerName.add(enter1);
        playerName.add(colour1);
        playerName.add(player1);
        playerName.add(player1Name);
        playerName.add(score1);
        playerName.add(games1);

        playerName.add(play);
        playerName.add(newSession);
        
        //Sets up layout for player 2(bottom)
        playerName.add(colour2);       
        playerName.add(player2);
        playerName.add(player2Name);
        playerName.add(score2);   
        playerName.add(games2);
        playerName.add(name2);
        playerName.add(enter2);

        //Action listeners for play and enter
        enter1.addActionListener(new ActionListener()

            {
                public void actionPerformed(ActionEvent e)
                {
                    if(name1.getText().trim().equals("")){  //checks if the JTextfield is empty or not 
                        JOptionPane.showMessageDialog(frame, "Please insert a name", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                    else{
                        player1Name.setText(name1.getText());
                    }
                    if(!player1Name.getText().equals("") && !player2Name.getText().equals("")) //checks if both player names label have a name in them before the game can begin 
                    {
                        play.setEnabled(true);
                    }
                }

            });

        play.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    if(play.getText() == "PLAY")
                    {
                        {
                            play();
                        }
                    }
                    else if(play.getText() == "RESTART")
                    {
                        contentPane.remove(board);
                        makeBoard();
                        updateScore();
                        play.setText("PLAY");
                        frame.pack();
                    }
                }
            });  

        newSession.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    gamesWon1 = 0;
                    gamesWon2 = 0;
                    boardSize = 8;
                    contentPane.remove(flow);
                    makePlayerPanel();
                    contentPane.remove(board);
                    makeBoard();
                    updateScore();
                    statusLabel.setText("Ready");
                    frame.pack();
                }
            });  

        enter2.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    if(name2.getText().trim().equals("")){//checks if the JTextfield is empty or not 
                        JOptionPane.showMessageDialog(frame, "Please insert a name", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                    else{
                        player2Name.setText(name2.getText());
                    }
                    if(!player1Name.getText().equals("") && !player2Name.getText().equals("")) //checks if both player names label have a name in them before the game can begin 
                    {
                        play.setEnabled(true);
                    }
                }
            });
        flow = new JPanel();
        flow.add(playerName);
        contentPane.add(flow, BorderLayout.EAST);
    }

    public void play()
    {
        name1.setEnabled(false);
        name2.setEnabled(false);                           
        enter1.setEnabled(false);
        enter2.setEnabled(false);
        b.enableBoard();
        play.setText("RESTART");
    }

    // -------- sets up menu bar  --------
    private void makeMenuBar(JFrame frame)
    {
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenuItem saveItem = new JMenuItem("Save");
        fileMenu.add(saveItem);
        saveItem.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    save();
                }
            });

        JMenuItem openItem = new JMenuItem("Open");
        fileMenu.add(openItem);
        openItem.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    open();
                }
            });

        JMenuItem quitItem = new JMenuItem("Quit");
        fileMenu.add(quitItem);
           quitItem.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                     System.exit(0);
                }
            });

        JMenu board = new JMenu("Board Size");
        menuBar.add(board);

        JMenuItem changeSize = new JMenuItem("Change Size");
        board.add(changeSize);
        changeSize.addActionListener(e -> changeBoardSize());
    }  

    /**
     * method to save the current game (i.e. the board, tiles on the board, the scores and the games won for both players)
     */
    private void save()
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("X:\\home\\CO520"));
        if (fileChooser.showSaveDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {            
            try(FileWriter save = new FileWriter(fileChooser.getSelectedFile()))
            {
                //simple code to make the file a reversi type file
                save.write("Reversi Game"+"\n");

                for(String data: b.saveBoard()){
                    save.write(data);
                }
                save.write("\n" + statusLabel.getText()+"\n");
                save.write(b.getCurrentPlayer()+"\n");
                save.write(player1Name.getText()+"\n");
                save.write(player2Name.getText()+"\n");
                save.write(gamesWon1+"\n");
                save.write(gamesWon2+"\n");
                save.write(colour1.getText()+"\n");
                save.write(colour2.getText()+"\n");

            }
            catch(IOException e){
                e.printStackTrace();
            }
        }

    }

    private void open()
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("X:\\home\\CO520"));
        if (fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION){
            try(FileReader read = new FileReader(fileChooser.getSelectedFile())){
                BufferedReader br = new BufferedReader(read);

                String code = br.readLine();
                if(code.equals("Reversi Game")) //simple check method to see if the file is a reversi type
                {

                    //reads the saved data line by line
                    String loadData = br.readLine();
                    String status = br.readLine();
                    String current = br.readLine();
                    String playerOneName = br.readLine();
                    String playerTwoName = br.readLine();                
                    gamesWon1 = Integer.parseInt(br.readLine());
                    gamesWon2 = Integer.parseInt(br.readLine());                
                    String colourOne = br.readLine();
                    String colourTwo = br.readLine();

                    //reconstructs the board
                    boardSize = (int) Math.sqrt(loadData.length());
                    contentPane.remove(board);
                    makeBoard();
                    b.loadBoard(loadData);  

                    //sets current player
                    b.setCurrentPlayer(current);

                    //updates the score
                    b.score();
                    updateScore();

                    //update game
                    games1.setText("Player 1 Games " + gamesWon1);
                    games2.setText("Player 2 Games " + gamesWon2);

                    statusLabel.setText(status);
                    player1Name.setText(playerOneName);
                    player2Name.setText(playerTwoName);

                    colour1.setText(colourOne);
                    colour2.setText(colourTwo);

                    play();
                }
                else
                {
                     JOptionPane.showMessageDialog(frame,"This is not a reversi game file, please select a reversi game file", "INCORRECT FILE", JOptionPane.ERROR_MESSAGE);
                    
                }
            }
            catch(FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }
    // -------- methods to be able to change the boardSize --------
    /**
     * method to change the size of the board, 
     * an input dialog will pop up for users to input a number
     * it first checks if the input an int type, if not it will show an error message
     * 2nd checks if the input is an even number, if not it will show an error message
     * 3rd checks if the input is greater than 2, if not it will show an error message 
     */
    private void changeBoardSize()
    {
        String size = JOptionPane.showInputDialog("Input a size");
        if(isNumeric(size)){
            if(boardSize % 2 ==0){
                if(boardSize > 2){
                    contentPane.remove(board);
                    makeBoard();
                    frame.pack();
                }
                else{
                    JOptionPane.showMessageDialog(frame,"Please input a size greather than 2", "INCORRECT INPUT", JOptionPane.ERROR_MESSAGE);  
                }
            }

            else {
                JOptionPane.showMessageDialog(frame,"Please input an even number","INCORRECT INPUT", JOptionPane.ERROR_MESSAGE);  
            }
        }
        else{
            JOptionPane.showMessageDialog(frame,"Please input a numberical value","INCORRECT INPUT", JOptionPane.ERROR_MESSAGE);  
        }
    }

    /**
     * checks if the input is a String or an int type 
     * takes param of String type from changeBoardSize method
     */
    public boolean isNumeric(String i)
    {
        try 
        {
            boardSize = Integer.parseInt(i);
        }

        catch(NumberFormatException nfe)  
        {
            return false;
        }
        return true;
    }  

    // -------- gets the winner --------

    /**
     * returns the Winner
     * returns the Player's name and changes the colour 
     * if the player that wins is black, changes his colour to white so he becomes player 2
     */
    public String getWinner()
    {
        String text = "Game is a Tie, No Winners";
        if(b.Scoreb() > b.Scorew())
        { 
            if(colour1.getText() == "BLACK")
            {         
                text = player1Name.getText()+" wins!";
                win1();  
                colour1.setText("WHITE");
                colour2.setText("BLACK");            
            }
            else
            {
                text =player2Name.getText()+" wins!";
                win2();
                colour1.setText("BLACK");
                colour2.setText("WHITE");  
            }
        }
        else if (b.Scoreb() < b.Scorew())
        {
            text = player2Name.getText()+" wins!";
            win2();
        }        
        return text;
    }

    /**
     * method to increment the games won by player 1 and updates the playerPanel
     */
    private void win1()
    {        
        gamesWon1++;
        games1.setText("Player 1 Games " + gamesWon1);
    }

    /**
     *  method to increment the games won by player 2 and updates the playerPanel
     */
    private void win2()
    {
        gamesWon2++;
        games2.setText("Player 2 Games " + gamesWon2);
    }

    /**
     * updates the tile scores of both players ont he playerPanel
     */
    public void updateScore()
    {
        score1.setText("Player 1 Score: " + b.Scoreb());
        score2.setText("Player 2 Score: " + b.Scorew());
    }
}
