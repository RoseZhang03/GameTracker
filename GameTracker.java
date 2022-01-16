//libraries
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.math.*;

public class GameTracker {
  
  //global variables
  private static Player player1;
  private static Player player2;
  private static ArrayList<Game> gameList;
  
  private static int highestStreak;
  private static boolean isChecked = false;
  private static boolean isChecked2 = false;
  private static int index;
  
  private static ArrayList<JLabel> aGame;
  private static ArrayList<JRadioButton> circleSelectionButton;
  private static ArrayList<JRadioButton> circleSelectionButton2;
  
  private static JTextField monthField;
  private static String month = "";
  private static JTextField dayField;
  private static String day = "";
  private static JTextField yearField;
  private static String year = "";
  private static JTextField gameNameField;
  private static String gameName = "";
  private static JRadioButton player1Button;
  private static JRadioButton player2Button;
  private static String winnerName = "";
  private static Dimension size;
  
  //JFrames need to be global because other JFrames will need to open them (such as the next button or back button)
  private static JFrame streakDisplayScreen;
  private static JFrame gameStatisticsScreen;
  private static JFrame gameDisplayScreen;
  private static JFrame addGameScreen;
  private static JFrame editSelectionScreen;
  private static JFrame editGameScreen;
  private static JFrame deleteSelectionScreen;
  private static JFrame deleteConfirmationScreen;
  
  //main method
  public static void main(String[] args) {
    
    //creates the Player objects and an arraylist of Game objects
    player1 = new Player("Player1", 0, 0);
    player2 = new Player("Player2", 0, 0);
    gameList = new ArrayList<Game>();
    
    //read in data from text file and place into ArrayList, then sort it by date (latest to oldest)
    try {
      File gameData = new File("C:\\Users\\Rose\\Documents\\Dr. Java\\Computer Science IA\\IA .txt Files\\GameData.txt");
      Scanner scan = new Scanner(gameData);
      while (scan.hasNextLine()) {
        String data = scan.nextLine();
        String[] commaSplit = data.split(",");
        gameList.add(new Game(commaSplit[0], commaSplit[1], commaSplit[2]));
      }
      scan.close();
    } catch (FileNotFoundException e) {
      JFrame error = new JFrame();
      JOptionPane.showMessageDialog(error,"An error has occurred.");  
      e.printStackTrace();
    }
    sort(gameList);
    Collections.reverse(gameList);
    
    //calculates streak data and the gets the current winner
    calculateCurrentStreak();
    calculatePlayerHighestStreak();
    getCurrentWinner();
   
    //GUI
    streakDisplayScreen();
    
  }
  //end of main method
  
  //sorts gameList from oldest to newest date, I reverse this in the main method
  public static void sort(ArrayList<Game> gameList) {
    gameList.sort((o1, o2)
                    -> o1.getDate().compareTo(o2.getDate()));
  }
  
  //setting current winner and current streak, depending on who won the most recent game
  public static void calculateCurrentStreak() {  
    if (gameList.get(0).getWinner().equals("Player1")) {
      player1.setCurrentStreak(1);
      player2.setCurrentStreak(0);
      for (int i = 1; i < gameList.size(); i++) {
        if (gameList.get(i).getWinner().equals("Player1")) {
          player1.setCurrentStreak(player1.getCurrentStreak() + 1);
        }
        else {
              i = gameList.size();
        }
      }
    }
    else {
      player2.setCurrentStreak(1);
      player1.setCurrentStreak(0);
      for (int i = 1; i < gameList.size(); i++) {
        if (gameList.get(i).getWinner().equals("Player2")) {
        player2.setCurrentStreak(player2.getCurrentStreak() + 1);
        }
        else {
          i = gameList.size();
        }
      }
    }
  }
  
  //finds the highest streak for both players
  public static void calculatePlayerHighestStreak() {  
    int player1Max = 0;
    int player1Current = 0;
    int player2Max = 0;
    int player2Current = 0;
    for (int i = 0; i < gameList.size(); i++) {
      if (gameList.get(i).getWinner().equals("Player1")) {
        player1Current += 1;
      }
      else {
        player1Current = 0;
      }
      if (player1Current > player1Max) {
        player1Max = player1Current;
      }
      if (gameList.get(i).getWinner().equals("Player2")) {
        player2Current += 1;
      }
      else {
        player2Current = 0;
      }
      if (player2Current > player2Max) {
        player2Max = player2Current;
      }
    }
    player1.setPlayerHighestStreak(player1Max);
    player2.setPlayerHighestStreak(player2Max);
  }
  
  //returns the winner (player with the highest streak)
  public static String getCurrentWinner() {
     if(player1.getPlayerHighestStreak() > player2.getPlayerHighestStreak()) {
      highestStreak = player1.getPlayerHighestStreak();
      return "Player1";
    }
     else if (player1.getPlayerHighestStreak() < player2.getPlayerHighestStreak()) {
      highestStreak = player2.getPlayerHighestStreak();
      return "Player2";
    }
     else
     {
       highestStreak = player2.getPlayerHighestStreak();
       return "It's a tie";
     }
  }
  
  //gets the total number of games played
  public static int getTotalGamesPlayed() {
    int sum = 0;;
    for (int i = 0; i < gameList.size(); i++) {
      sum ++;
    }
    return sum;
  }
  
  //gets the total number of wins for a player
  public static int getPlayerTotalWins(Player p) {
    int sum = 0;
    for(int i = 0; i < gameList.size(); i++) {
      if(gameList.get(i).getWinner().equals(p.getPlayerName())) {
        sum ++;
      }
    }
    return sum;
  }
  
  //gets the percentage of wins (wins over total games) for a player
  public static double getWinPercentage(Player p) {
    return round(1.0* getPlayerTotalWins(p) / getTotalGamesPlayed() * 100, 2);
  }
  
  //this will round the percentage up to avoid infinitely repeating decimals
  private static double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    BigDecimal bd = new BigDecimal(Double.toString(value));
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }
  
  //GUI for the streak display screen 
  public static void streakDisplayScreen() {
    streakDisplayScreen = new JFrame("Streak Display Screen");
    JPanel streakDisplayPanel = new JPanel();
    streakDisplayScreen.getContentPane();
    
    JLabel highestStreakLabel = new JLabel("Highest Streak: " + highestStreak);
    size = highestStreakLabel.getPreferredSize();
    highestStreakLabel.setBounds(300, 50, size.width, size.height);
    
    JLabel currentWinnerLabel = new JLabel("Current Winner: " + getCurrentWinner());
    size = currentWinnerLabel.getPreferredSize();
    currentWinnerLabel.setBounds(285, 100, size.width, size.height);
    
    JLabel player1CurrentStreakLabel = new JLabel("Player1's Current Streak: " + player1.getCurrentStreak());
    size = player1CurrentStreakLabel.getPreferredSize();
    player1CurrentStreakLabel.setBounds(280, 150, size.width, size.height);
    
    JLabel player2CurrentStreakLabel = new JLabel("Player2's Current Streak: " + player2.getCurrentStreak());
    size = player2CurrentStreakLabel.getPreferredSize();
    player2CurrentStreakLabel.setBounds(280, 200, size.width, size.height);
    
    JButton nextScreenButton1 = new JButton();
    nextScreenButton1.setBounds(570, 290, 60, 30);
    try{
    Image rightArrow = ImageIO.read(new File("C:\\Users\\Rose\\Documents\\Dr. Java\\Computer Science IA\\IA Buttons\\orangebuttonRIGHT.png")).getScaledInstance(60, 30, Image.SCALE_DEFAULT);
    nextScreenButton1.setIcon(new ImageIcon(rightArrow));
    } 
    catch (Exception e) {
    }
    nextScreenButton1.addActionListener(new NextScreenButton1Listener());
    
    streakDisplayPanel.add(highestStreakLabel);
    streakDisplayPanel.add(currentWinnerLabel);
    streakDisplayPanel.add(player1CurrentStreakLabel);
    streakDisplayPanel.add(player2CurrentStreakLabel);
    streakDisplayPanel.add(nextScreenButton1);
    streakDisplayPanel.setLayout(null);
    streakDisplayPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    streakDisplayScreen.addWindowListener(new WindowAdapter());
    streakDisplayScreen.add(streakDisplayPanel);
    streakDisplayScreen.setSize(700, 400);
    streakDisplayScreen.setLocation(600, 300);
    streakDisplayScreen.setVisible(true);
  }
  
  //code for the next button, will close the streak display screen and opens the game statistics screen
   private static class NextScreenButton1Listener implements ActionListener {
    public void actionPerformed (ActionEvent e) {
      streakDisplayScreen.setVisible(false);
      gameStatisticsScreen();
    }
  }
  
   //GUI for the game statistics screen
  public static void gameStatisticsScreen() {
    gameStatisticsScreen = new JFrame("Game Statistics Screen");
    gameStatisticsScreen.getContentPane();
    JPanel gameStatisticsPanel = new JPanel();
    
    JLabel totalGamesPlayedLabel = new JLabel("Total Games Played: " + getTotalGamesPlayed());
    size = totalGamesPlayedLabel.getPreferredSize();
    totalGamesPlayedLabel.setBounds(285, 30, size.width, size.height);
    
    JLabel player1TotalWinsLabel = new JLabel("Player1's Total Wins: " + getPlayerTotalWins(player1));
    size = player1TotalWinsLabel.getPreferredSize();
    player1TotalWinsLabel.setBounds(285, 90, size.width, size.height);
    
    JLabel player2TotalWinsLabel = new JLabel("Player2's Total Wins: " + getPlayerTotalWins(player2));
    size = player2TotalWinsLabel.getPreferredSize();
    player2TotalWinsLabel.setBounds(285, 120, size.width, size.height);
    
    JLabel player1WinPercentageLabel = new JLabel("Player1's Win Percentage: " + getWinPercentage(player1) + "%");
    size = player1WinPercentageLabel.getPreferredSize();
    player1WinPercentageLabel.setBounds(260, 180, size.width, size.height);
    
    JLabel player2WinPercentageLabel = new JLabel("Player2" + "'s Win Percentage: " + getWinPercentage(player2) + "%");
    size = player2WinPercentageLabel.getPreferredSize();
    player2WinPercentageLabel.setBounds(260, 210, size.width, size.height);
    
    JButton backScreenButton2 = new JButton();
    backScreenButton2.setBounds(50, 290, 60, 30);
    try{
    Image leftArrow = ImageIO.read(new File("C:\\Users\\Rose\\Documents\\Dr. Java\\Computer Science IA\\IA Buttons\\orangebuttonLEFT.png")).getScaledInstance(60, 30, Image.SCALE_DEFAULT);
    backScreenButton2.setIcon(new ImageIcon(leftArrow));
    } 
    catch (Exception e) {
    }
    backScreenButton2.addActionListener(new BackScreenButton2Listener());
    
    JButton nextScreenButton2 = new JButton();
    nextScreenButton2.setBounds(570, 290, 60, 30);
    try{
    Image rightArrow = ImageIO.read(new File("C:\\Users\\Rose\\Documents\\Dr. Java\\Computer Science IA\\IA Buttons\\orangebuttonRIGHT.png")).getScaledInstance(60, 30, Image.SCALE_DEFAULT);
    nextScreenButton2.setIcon(new ImageIcon(rightArrow));
    } 
    catch (Exception e) {
    }
    nextScreenButton2.addActionListener(new NextScreenButton2Listener());
    
    gameStatisticsPanel.add(totalGamesPlayedLabel);
    gameStatisticsPanel.add(player1TotalWinsLabel);
    gameStatisticsPanel.add(player2TotalWinsLabel);
    gameStatisticsPanel.add(player1WinPercentageLabel);
    gameStatisticsPanel.add(player2WinPercentageLabel);
    gameStatisticsPanel.add(backScreenButton2);
    gameStatisticsPanel.add(nextScreenButton2);
    gameStatisticsPanel.setLayout(null);
    gameStatisticsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    gameStatisticsScreen.addWindowListener(new WindowAdapter());
    gameStatisticsScreen.add(gameStatisticsPanel);
    gameStatisticsScreen.setSize(700, 400);
    gameStatisticsScreen.setLocation(600, 300);
    gameStatisticsScreen.setVisible(true);
  }
  
  //code for the back button, goes back to the streak display screen
  private static class BackScreenButton2Listener implements ActionListener {
    public void actionPerformed (ActionEvent e) {
      gameStatisticsScreen.setVisible(false);
      streakDisplayScreen();
    }
  }
  
  //code for the next button, goes to the game display screen
  private static class NextScreenButton2Listener implements ActionListener {
    public void actionPerformed (ActionEvent e) {
      gameStatisticsScreen.setVisible(false);
      gameDisplayScreen();
      }
    }
  
  //GUI for the game display screen
  private static void gameDisplayScreen() {
    gameDisplayScreen = new JFrame("Game Display Screen");
    gameDisplayScreen.getContentPane();
    JPanel gameDisplayPanel = new JPanel();
    aGame = new ArrayList<JLabel>();
    JPanel labelPanel = new JPanel();
    gameDisplayPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    gameDisplayPanel.setLayout(new BoxLayout(gameDisplayPanel, BoxLayout.PAGE_AXIS));
    labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.PAGE_AXIS));
    labelPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    gameDisplayPanel.add(labelPanel);
    
    JButton addButton = new JButton();
    addButton.setBounds(150, 20, 80, 40);
    try{
      Image greenAdd = ImageIO.read(new File("C:\\Users\\Rose\\Documents\\Dr. Java\\Computer Science IA\\IA Buttons\\addbutton.png")).getScaledInstance(80,40, Image.SCALE_DEFAULT);
      addButton.setIcon(new ImageIcon(greenAdd));
    }
    catch (Exception e) {
    }
    addButton.addActionListener(new AddButtonListener());
    gameDisplayScreen.add(addButton);
    
    JButton editButton = new JButton();
    editButton.setBounds(300, 20, 90, 40);
    try{
      Image yellowEdit = ImageIO.read(new File("C:\\Users\\Rose\\Documents\\Dr. Java\\Computer Science IA\\IA Buttons\\editbutton.png")).getScaledInstance(90, 40, Image.SCALE_DEFAULT);
      editButton.setIcon(new ImageIcon(yellowEdit));
    }
    catch (Exception e) {
    }
    editButton.addActionListener(new EditButtonListener());
    gameDisplayScreen.add(editButton);
    
    JButton deleteButton = new JButton();
    deleteButton.setBounds(450, 20, 100, 40);
    try{
      Image redDelete = ImageIO.read(new File("C:\\Users\\Rose\\Documents\\Dr. Java\\Computer Science IA\\IA Buttons\\deletebutton.png")).getScaledInstance(100, 40, Image.SCALE_DEFAULT);
      deleteButton.setIcon(new ImageIcon(redDelete));
    }
    catch (Exception e) {
    }
    deleteButton.addActionListener(new DeleteButtonListener());
    gameDisplayScreen.add(deleteButton);
    
    for (int i = 0; i < gameList.size(); i++) {
      aGame.add(new JLabel(gameList.get(i).toString()));
      labelPanel.add(aGame.get(i)); 
      labelPanel.add(new JLabel("         "));
    }
    
    JButton backScreenButton3 = new JButton();
    backScreenButton3.setBounds(50, 290, 60, 30);
    try{
    Image leftArrow = ImageIO.read(new File("C:\\Users\\Rose\\Documents\\Dr. Java\\Computer Science IA\\IA Buttons\\orangebuttonLEFT.png")).getScaledInstance(60, 30, Image.SCALE_DEFAULT);
    backScreenButton3.setIcon(new ImageIcon(leftArrow));
    } 
    catch (Exception e) {
    }
    backScreenButton3.addActionListener(new BackScreenButton3Listener());
    gameDisplayScreen.add(backScreenButton3);
    
    JScrollPane scroll = new JScrollPane(gameDisplayPanel);
    scroll.setBorder(BorderFactory.createEmptyBorder(100, 200, 70, 0));
    gameDisplayScreen.add(scroll);
    gameDisplayScreen.addWindowListener(new WindowAdapter());
    gameDisplayScreen.setSize(700, 400);
    gameDisplayScreen.setLocation(600, 300);
    gameDisplayScreen.setVisible(true);
  }
  
  //code for the add button, it will open up the add game screen which will let the user enter info and add a game
  private static class AddButtonListener implements ActionListener {
    public void actionPerformed (ActionEvent e) {
      gameDisplayScreen.setVisible(false);
      addGameScreen();
    }
  }

  //code for the edit button, it will open up the edit selection screen and let the user select a game to be edited
  private static class EditButtonListener implements ActionListener {
    public void actionPerformed (ActionEvent e) {
      gameDisplayScreen.setVisible(false);
      editSelectionScreen();
    }
  }
  
  //code for the delete button, it will open up the delete selection screen and let the user select a game to be deleted
  private static class DeleteButtonListener implements ActionListener {
    public void actionPerformed (ActionEvent e) {
      gameDisplayScreen.setVisible(false);
      deleteSelectionScreen();
    }
  }
  
  //code for the back button, it will go back to the game statistics screen
  private static class BackScreenButton3Listener implements ActionListener {
    public void actionPerformed (ActionEvent e) {
      gameDisplayScreen.setVisible(false);
      gameStatisticsScreen();
    }
  }
  
  //GUI for the add game screen
  //there are text fields for the user to enter in information about a game they want to add
  //the FocusListener code is under the edit game screen section
  //FocusListener code can be found starting at line 810
  //the ActionListener code for the winner input is also found under the edit game screen
  //ActionListener code for the winner input is found at line 854
  private static void addGameScreen() {
    addGameScreen = new JFrame("Add Game Screen");
    addGameScreen.getContentPane();
    JPanel addGamePanel = new JPanel();
    addGamePanel.setLayout(null);
    addGamePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    JLabel newDateLabel = new JLabel("Date: ");
    size = newDateLabel.getPreferredSize();
    newDateLabel.setBounds(220, 50, size.width, size.height);
    addGamePanel.add(newDateLabel);
    
    monthField = new JTextField("");
    monthField.setBounds(260, 50, 25, 20);
    monthField.addFocusListener(new MonthInputListener());
    addGamePanel.add(monthField);
    
    JLabel slashLabel1 = new JLabel("/");
    slashLabel1.setFont(new Font("MS Sans Serif", Font.BOLD, 18));
    size = slashLabel1.getPreferredSize();
    slashLabel1.setBounds(290, 48, size.width, size.height);
    addGamePanel.add(slashLabel1);
    
    dayField = new JTextField("");
    dayField.setBounds(300, 50, 25, 20);
    dayField.addFocusListener(new DayInputListener());
    addGamePanel.add(dayField);
    
    JLabel slashLabel2 = new JLabel("/");
    slashLabel2.setFont(new Font("MS Sans Serif", Font.BOLD, 18));
    size = slashLabel2.getPreferredSize();
    slashLabel2.setBounds(330, 48, size.width, size.height);
    addGamePanel.add(slashLabel2);
    
    yearField = new JTextField("");
    yearField.setBounds(340, 50, 40, 20);
    yearField.addFocusListener(new YearInputListener());
    addGamePanel.add(yearField);
    
    JLabel newGameNameLabel = new JLabel("Game: ");
    size = newGameNameLabel.getPreferredSize();
    newGameNameLabel.setBounds(220, 130, size.width, size.height);
    addGamePanel.add(newGameNameLabel);
    
    gameNameField = new JTextField("");
    gameNameField.setBounds(260, 130, 150, 20);
    gameNameField.addFocusListener(new GameNameInputListener());
    addGamePanel.add(gameNameField);
    
    JLabel newWinnerLabel = new JLabel("Winner: ");
    size = newWinnerLabel.getPreferredSize();
    newWinnerLabel.setBounds(220, 210, size.width, size.height);
    addGamePanel.add(newWinnerLabel);
    
    //two JRadioButtons for the user to select between Player1 and Player 2
    ButtonGroup group = new ButtonGroup();
    
    player1Button = new JRadioButton("Player1");
    size = player1Button.getPreferredSize();
    player1Button.setBounds(270, 205, size.width, size.height);
    group.add(player1Button);
    addGamePanel.add(player1Button); 
    player1Button.addActionListener(new WinnerInputListener());
    
    player2Button = new JRadioButton("Player2");
    size = player2Button.getPreferredSize();
    player2Button.setBounds(350, 205, size.width, size.height);
    group.add(player2Button);
    addGamePanel.add(player2Button);
    player2Button.addActionListener(new WinnerInputListener());
    
    JButton saveButton = new JButton();
    saveButton.setBounds(400, 290, 100, 40);
    try{
      Image greenSave = ImageIO.read(new File("C:\\Users\\Rose\\Documents\\Dr. Java\\Computer Science IA\\IA Buttons\\savebutton.png")).getScaledInstance(100, 40, Image.SCALE_DEFAULT);
      saveButton.setIcon(new ImageIcon(greenSave));
    }
    catch (Exception e) {
    }
    saveButton.addActionListener(new AddGameSaveButtonListener());
    addGamePanel.add(saveButton);
    
    JButton cancelButton = new JButton();
    cancelButton.setBounds(520, 290, 100, 40);
    try{
      Image redCancel = ImageIO.read(new File("C:\\Users\\Rose\\Documents\\Dr. Java\\Computer Science IA\\IA Buttons\\cancelbutton.png")).getScaledInstance(100, 40, Image.SCALE_DEFAULT);
      cancelButton.setIcon(new ImageIcon(redCancel));
    }
    catch (Exception e) {
    }
    cancelButton.addActionListener(new AddGameCancelButtonListener());
    addGamePanel.add(cancelButton);
    
    addGameScreen.addWindowListener(new WindowAdapter());
    addGameScreen.add(addGamePanel);
    addGameScreen.setSize(700, 400);
    addGameScreen.setLocation(600, 300);
    addGameScreen.setVisible(true);
  }
  
  //code for the save button
  //it first checks to make sure everything is filled out, if not then it will send an error message
  //since the formatting of the date is important, I have a check to make sure the date is formatted correctly
  //if the format of the date is incorrect, then there will be an error message with instructions
  //if everything is correct the game will be added to the ArrayList of games, and the screen will return to the game display screen
  //after saving the program will also re-sort the list and recalculate streak values and whatnot
  //it will also set all the data values to null so it won't interfere with future input 
  public static class AddGameSaveButtonListener implements ActionListener {
    public void actionPerformed (ActionEvent e) {
      if ((month.length() == 0) || (day.length() == 0) || (year.length() == 0) || (gameName.length() == 0) || (winnerName.length() == 0)) {
        JFrame alert = new JFrame();
        JOptionPane.showMessageDialog(alert, "Please fill out all fields.");
      }
      else if ((month.length() != 2) || (day.length() != 2) || (year.length() != 4)) {
        JFrame alert2 = new JFrame();
        JOptionPane.showMessageDialog(alert2, "Please fill in the date according to MM-DD-YYYY format.");
      }
      else {
        String dataDate = year + "-" + month + "-" + day;
        gameList.add(new Game(dataDate, gameName, winnerName));
        month = "";
        day = "";
        year = "";
        gameName = "";
        winnerName = "";
        sort(gameList);
        Collections.reverse(gameList);
        calculateCurrentStreak();
        calculatePlayerHighestStreak();
        getCurrentWinner();
        addGameScreen.setVisible(false);
        gameDisplayScreen();
      }
    }
  }
  
  //code for the cancel button, the game will not be added to the list, data values are set to null, and the screen will return to the game display screen
  public static class AddGameCancelButtonListener implements ActionListener {
    public void actionPerformed (ActionEvent e) {
      month = "";
      day = "";
      year = "";
      gameName = "";
      winnerName = "";
      addGameScreen.setVisible(false);
      gameDisplayScreen();
    }
  }
  
  //GUI for the edit selection screen
  private static void editSelectionScreen() {
    editSelectionScreen = new JFrame("Edit Selection Screen");
    editSelectionScreen.getContentPane();
    JPanel editSelectionPanel = new JPanel();
    editSelectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    editSelectionPanel.setLayout(new BoxLayout(editSelectionPanel, BoxLayout.PAGE_AXIS));
    JPanel labelPanel = new JPanel();
    labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.PAGE_AXIS));
    editSelectionPanel.add(labelPanel);
    circleSelectionButton = new ArrayList<JRadioButton>();
    ButtonGroup group = new ButtonGroup();
    int yValue = 50;
    
    //this for loop will give each game in the ArrayList of games a JRadioButton, add it to a button group, and put it on a JPanel
    for(int i = 0; i < gameList.size(); i++) {
    circleSelectionButton.add(new JRadioButton(gameList.get(i).toString()));
    size = circleSelectionButton.get(i).getPreferredSize();
    circleSelectionButton.get(i).setBounds(170, yValue, size.width, size.height);
    group.add(circleSelectionButton.get(i));
    labelPanel.add(circleSelectionButton.get(i));
    labelPanel.add(new JLabel("                     "));
    circleSelectionButton.get(i).addActionListener(new CircleSelectionButtonListener());
    yValue += 50;
    }
    
    JButton selectButton = new JButton();
    selectButton.setBounds(400, 290, 100, 40);
    try{
      Image yellowSelect = ImageIO.read(new File("C:\\Users\\Rose\\Documents\\Dr. Java\\Computer Science IA\\IA Buttons\\selectbutton.png")).getScaledInstance(100, 40, Image.SCALE_DEFAULT);
      selectButton.setIcon(new ImageIcon(yellowSelect));
    }
    catch (Exception e) {
    }
    selectButton.addActionListener(new EditSelectionSelectButtonListener());
    editSelectionScreen.add(selectButton);
    
    JButton cancelButton = new JButton();
    cancelButton.setBounds(520, 290, 100, 40);
    try{
      Image redCancel = ImageIO.read(new File("C:\\Users\\Rose\\Documents\\Dr. Java\\Computer Science IA\\IA Buttons\\cancelbutton.png")).getScaledInstance(100, 40, Image.SCALE_DEFAULT);
      cancelButton.setIcon(new ImageIcon(redCancel));
    }
    catch (Exception e) {
    }
    cancelButton.addActionListener(new EditSelectionCancelButtonListener());
    editSelectionScreen.add(cancelButton);

    //I have a scroll pane in case there are too many games to fit on screen
    JScrollPane scroll = new JScrollPane(editSelectionPanel);
    scroll.setBorder(BorderFactory.createEmptyBorder(10, 125, 80, 0));
    editSelectionScreen.add(scroll);
    editSelectionScreen.addWindowListener(new WindowAdapter());
    editSelectionScreen.setSize(700, 400);
    editSelectionScreen.setLocation(600, 300);
    editSelectionScreen.setVisible(true);
  }
  
  //code for the JRadioButton, I have a flag to check whether or not a button has been selected
  //it also stores the index which will be needed later when setting new (edited) values for the game
  private static class CircleSelectionButtonListener implements ActionListener {
    public void actionPerformed (ActionEvent e) {
      for (int i = 0; i < circleSelectionButton.size(); i++) {
        if (circleSelectionButton.get(i).isSelected()) {
          index = i;
          i = circleSelectionButton.size();
          isChecked = true;
        }
      }
    }
  }
  
  //code for the select button
  //checks a flag to see if a JRadioButton has been selected
  //if true, it'll go to the edit game screen and reset the flag
  //if flase, it will show an error message and inform the user to select a game
  private static class EditSelectionSelectButtonListener implements ActionListener {
    public void actionPerformed (ActionEvent e) {
      if (isChecked) {
      editSelectionScreen.setVisible(false);
      isChecked = false;
      editGameScreen();
      }
      else {
        JFrame alert = new JFrame();
        JOptionPane.showMessageDialog(alert, "Please select a game before clicking the select button.");
      }
    }
  }
  
  //code for the cancel button, which will set the flag to false and go back to the game display screen
  private static class EditSelectionCancelButtonListener implements ActionListener {
    public void actionPerformed (ActionEvent e) {
      isChecked = false;
      editSelectionScreen.setVisible(false);
      gameDisplayScreen();
    }
  }
  
  //GUI for the edit game screen
  //there are text fields to allow the user to enter in new (edited) values
  private static void editGameScreen() {
    editGameScreen = new JFrame("Edit Game Screen");
    editGameScreen.getContentPane();
    JPanel editGamePanel = new JPanel();
    editGamePanel.setLayout(null);
    editGamePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    JLabel currentDateLabel = new JLabel("Current Date: " + gameList.get(index).toStringDateFormatted());
    size = currentDateLabel.getPreferredSize();
    currentDateLabel.setBounds(50, 50, size.width, size.height);
    editGamePanel.add(currentDateLabel);
    
    JLabel newDateLabel = new JLabel("New Date: ");
    size = newDateLabel.getPreferredSize();
    newDateLabel.setBounds(350, 50, size.width, size.height);
    editGamePanel.add(newDateLabel);
    
    monthField = new JTextField("");
    monthField.setBounds(420, 50, 25, 20);
    monthField.addFocusListener(new MonthInputListener());
    editGamePanel.add(monthField);
    
    JLabel slashLabel1 = new JLabel("/");
    slashLabel1.setFont(new Font("MS Sans Serif", Font.BOLD, 18));
    size = slashLabel1.getPreferredSize();
    slashLabel1.setBounds(450, 48, size.width, size.height);
    editGamePanel.add(slashLabel1);
    
    dayField = new JTextField("");
    dayField.setBounds(460, 50, 25, 20);
    dayField.addFocusListener(new DayInputListener());
    editGamePanel.add(dayField);
    
    JLabel slashLabel2 = new JLabel("/");
    slashLabel2.setFont(new Font("MS Sans Serif", Font.BOLD, 18));
    size = slashLabel2.getPreferredSize();
    slashLabel2.setBounds(490, 48, size.width, size.height);
    editGamePanel.add(slashLabel2);
    
    yearField = new JTextField("");
    yearField.setBounds(500, 50, 40, 20);
    yearField.addFocusListener(new YearInputListener());
    editGamePanel.add(yearField);
    
    JLabel currentGameNameLabel = new JLabel("Current Game: " + gameList.get(index).getGameType());
    size = currentGameNameLabel.getPreferredSize();
    currentGameNameLabel.setBounds(50, 100, size.width, size.height);
    editGamePanel.add(currentGameNameLabel);
    
    JLabel newGameNameLabel = new JLabel("New Game: ");
    size = newGameNameLabel.getPreferredSize();
    newGameNameLabel.setBounds(350, 100, size.width, size.height);
    editGamePanel.add(newGameNameLabel);
    
    gameNameField = new JTextField("");
    gameNameField.setBounds(420, 100, 150, 20);
    gameNameField.addFocusListener(new GameNameInputListener());
    editGamePanel.add(gameNameField);
    
    JLabel currentWinnerLabel = new JLabel("Current Winner: " + gameList.get(index).getWinner());
    size = currentWinnerLabel.getPreferredSize();
    currentWinnerLabel.setBounds(50, 150, size.width, size.height);
    editGamePanel.add(currentWinnerLabel);
    
    JLabel newWinnerLabel = new JLabel("New Winner: ");
    size = newWinnerLabel.getPreferredSize();
    newWinnerLabel.setBounds(350, 150, size.width, size.height);
    editGamePanel.add(newWinnerLabel);
    
    //two JRadioButtons and a ButtonGroup to make the user choose between 1 of the 2 players
    
    ButtonGroup group = new ButtonGroup();
    
    player1Button = new JRadioButton("Player1");
    size = player1Button.getPreferredSize();
    player1Button.setBounds(430, 147, size.width, size.height);
    group.add(player1Button);
    editGamePanel.add(player1Button); 
    player1Button.addActionListener(new WinnerInputListener());
    
    player2Button = new JRadioButton("Player2");
    size = player2Button.getPreferredSize();
    player2Button.setBounds(500, 147, size.width, size.height);
    group.add(player2Button);
    editGamePanel.add(player2Button);
    player2Button.addActionListener(new WinnerInputListener());
    
    JButton saveButton = new JButton();
    saveButton.setBounds(400, 290, 100, 40);
    try{
      Image greenSave = ImageIO.read(new File("C:\\Users\\Rose\\Documents\\Dr. Java\\Computer Science IA\\IA Buttons\\savebutton.png")).getScaledInstance(100, 40, Image.SCALE_DEFAULT);
      saveButton.setIcon(new ImageIcon(greenSave));
    }
    catch (Exception e) {
    }
    saveButton.addActionListener(new EditGameSaveButtonListener());
    editGamePanel.add(saveButton);
    
    JButton cancelButton = new JButton();
    cancelButton.setBounds(520, 290, 100, 40);
    try{
      Image redCancel = ImageIO.read(new File("C:\\Users\\Rose\\Documents\\Dr. Java\\Computer Science IA\\IA Buttons\\cancelbutton.png")).getScaledInstance(100, 40, Image.SCALE_DEFAULT);
      cancelButton.setIcon(new ImageIcon(redCancel));
    }
    catch (Exception e) {
    }
    cancelButton.addActionListener(new EditGameCancelButtonListener());
    editGamePanel.add(cancelButton);
    
    editGameScreen.addWindowListener(new WindowAdapter());
    editGameScreen.add(editGamePanel);
    editGameScreen.setSize(700, 400);
    editGameScreen.setLocation(600, 300);
    editGameScreen.setVisible(true);
  }
  
  //all FocusListeners apply to both the add game screen as well as the edit game screen
  //will save the input in the month TextField when the user moves their cursor away
  public static class MonthInputListener implements FocusListener {
    public void focusGained(FocusEvent e) {
        //don't need this, but it has to be here for the code to compile
    }
    public void focusLost(FocusEvent e) {
      month = monthField.getText();
    }
  }
  
  //will save the input in the day TextField when the user moves their cursor away
  public static class DayInputListener implements FocusListener {
    public void focusGained(FocusEvent e) {
        //don't need this, but it has to be here for the code to compile
    }
    public void focusLost(FocusEvent e) {
      day = dayField.getText();
    }
  }
  
  //will save the input in the year TextField when the user moves their cursor away
  public static class YearInputListener implements FocusListener {
    public void focusGained(FocusEvent e) {
        //don't need this, but it has to be here for the code to compile
    }
    public void focusLost(FocusEvent e) {
      year = yearField.getText();
    }
  }
  
  //will save the input in the game TextField when the user moves their cursor away
  public static class GameNameInputListener implements FocusListener {
    public void focusGained(FocusEvent e) {
        //don't need this, but it has to be here for the code to compile
    }
    public void focusLost(FocusEvent e) {
      gameName = gameNameField.getText();
    }
  }
  
  //will determine which player has been selected
  public static class WinnerInputListener implements ActionListener {
    public void actionPerformed (ActionEvent e) {
      if(player1Button.isSelected()) {
        winnerName = "Player1";
      }
      if(player2Button.isSelected()) {
        winnerName = "Player2";
      }
     }
    }
  
   //code for the save button
  //it first checks to make sure everything is filled out, if not then it will send an error message
  //since the formatting of the date is important, I have a check to make sure the date is formatted correctly
  //if the format of the date is incorrect, then there will be an error message with instructions
  //if everything is correct the game at the index will be updated to match the user input values, and the screen will return to the game display screen
  //after saving the program will also re-sort the list and recalculate streak values and whatnot
  //it will also set all the data values to null so it won't interfere with future input 
  public static class EditGameSaveButtonListener implements ActionListener {
    public void actionPerformed (ActionEvent e) {
      if ((month.length() == 0) || (day.length() == 0) || (year.length() == 0) || (gameName.length() == 0) || (winnerName.length() == 0)) {
        JFrame alert = new JFrame();
        JOptionPane.showMessageDialog(alert, "Please fill out all fields.");
      }
      else if ((month.length() != 2) || (day.length() != 2) || (year.length() != 4)) {
        JFrame alert2 = new JFrame();
        JOptionPane.showMessageDialog(alert2, "Please fill in the date according to MM-DD-YYYY format.");
      }
      else {
        String dataDate = year + "-" + month + "-" + day;
        gameList.get(index).setDate(dataDate);
        gameList.get(index).setGameType(gameName);
        gameList.get(index).setWinner(winnerName);
        month = "";
        day = "";
        year = "";
        gameName = "";
        winnerName = "";
        sort(gameList);
        Collections.reverse(gameList);
        calculateCurrentStreak();
        calculatePlayerHighestStreak();
        getCurrentWinner();
        editGameScreen.setVisible(false);
        gameDisplayScreen();
      }
    }
  }
  
  //code for the cancel button, it sets all data values to null then goes back to the edit selection screen
  public static class EditGameCancelButtonListener implements ActionListener {
    public void actionPerformed (ActionEvent e) {
      month = "";
      day = "";
      year = "";
      gameName = "";
      winnerName = "";
      editGameScreen.setVisible(false);
      editSelectionScreen();
    }
  }
  
  //GUI for the delete selection screen
  private static void deleteSelectionScreen() {
    deleteSelectionScreen = new JFrame("Delete Selection Screen");
    deleteSelectionScreen.getContentPane();
    JPanel deleteSelectionPanel = new JPanel();
    deleteSelectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    deleteSelectionPanel.setLayout(new BoxLayout(deleteSelectionPanel, BoxLayout.PAGE_AXIS));
    JPanel labelPanel = new JPanel();
    labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.PAGE_AXIS));
    deleteSelectionPanel.add(labelPanel);
    circleSelectionButton2 = new ArrayList<JRadioButton>();
    ButtonGroup group = new ButtonGroup();
    int yValue = 50;
    
    //adds all the games in the game ArrayList to a JRadioButton and in a ButtonGroup and puts it onto the JPanel
    for(int i = 0; i < gameList.size(); i++) {
    circleSelectionButton2.add(new JRadioButton(gameList.get(i).toString()));
    size = circleSelectionButton2.get(i).getPreferredSize();
    circleSelectionButton2.get(i).setBounds(200, yValue, size.width, size.height);
    group.add(circleSelectionButton2.get(i));
    labelPanel.add(circleSelectionButton2.get(i));
    labelPanel.add(new JLabel("                     "));
    circleSelectionButton2.get(i).addActionListener(new CircleSelectionButton2Listener());
    yValue += 50;
    }
    
    JButton selectButton = new JButton();
    selectButton.setBounds(400, 290, 100, 40);
    try{
      Image yellowSelect = ImageIO.read(new File("C:\\Users\\Rose\\Documents\\Dr. Java\\Computer Science IA\\IA Buttons\\selectbutton.png")).getScaledInstance(100, 40, Image.SCALE_DEFAULT);
      selectButton.setIcon(new ImageIcon(yellowSelect));
    }
    catch (Exception e) {
    }
    selectButton.addActionListener(new DeleteSelectButtonListener());
    deleteSelectionScreen.add(selectButton);
    
    JButton cancelButton = new JButton();
    cancelButton.setBounds(520, 290, 100, 40);
    try{
      Image redCancel = ImageIO.read(new File("C:\\Users\\Rose\\Documents\\Dr. Java\\Computer Science IA\\IA Buttons\\cancelbutton.png")).getScaledInstance(100, 40, Image.SCALE_DEFAULT);
      cancelButton.setIcon(new ImageIcon(redCancel));
    }
    catch (Exception e) {
    }
    cancelButton.addActionListener(new DeleteSelectionCancelButtonListener());
    deleteSelectionScreen.add(cancelButton);
 
    //a Scrollpane in case there are too many games to fit onto the screen
    JScrollPane scroll = new JScrollPane(deleteSelectionPanel);
    scroll.setBorder(BorderFactory.createEmptyBorder(25, 150, 80, 0));
    deleteSelectionScreen.add(scroll);
    deleteSelectionScreen.addWindowListener(new WindowAdapter());
    deleteSelectionScreen.setSize(700, 400);
    deleteSelectionScreen.setLocation(600, 300);
    deleteSelectionScreen.setVisible(true);
  }
  
  //code to see whether a JRadioButton has been selected or not
  //if it has, then the flag is set to true, and the index is set to the index of the game that has been selected
  private static class CircleSelectionButton2Listener implements ActionListener {
    public void actionPerformed (ActionEvent e) {
      for (int i = 0; i < circleSelectionButton2.size(); i++) {
        if (circleSelectionButton2.get(i).isSelected()) {
          index = i;
          i = circleSelectionButton2.size();
          isChecked2 = true;
        }
      }
    }
  }
  
  //code for the select button
  //if the flag is true, then the screen goes to the delete confirmation screen and the flag is set to false again
  //if the flag is false (no game has been selected), then an error message pops up
  private static class DeleteSelectButtonListener implements ActionListener {
    public void actionPerformed (ActionEvent e) {
      if (isChecked2) {
      deleteSelectionScreen.setVisible(false);
      isChecked2 = false;
      deleteConfirmationScreen();
      }
      else {
        JFrame alert = new JFrame();
        JOptionPane.showMessageDialog(alert, "Please select a game before clicking the select button.");
      }
    }
  }
  
  //code for the cancel button, the flag is set to false and the screen goes back to the game display screen
  private static class DeleteSelectionCancelButtonListener implements ActionListener {
    public void actionPerformed (ActionEvent e) {
      deleteSelectionScreen.setVisible(false);
      isChecked2 = false;
      gameDisplayScreen();
    }
  }
  
  //GUI for the delete confimation screen
  //asks the user whether or not they want to delete the game they selected
  private static void deleteConfirmationScreen() {
    deleteConfirmationScreen = new JFrame("Delete Confirmation Screen");
    deleteConfirmationScreen.getContentPane();
    JPanel deleteConfirmationPanel = new JPanel();
    deleteConfirmationPanel.setLayout(null);
    deleteConfirmationPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    JLabel questionLabel = new JLabel("Do you wish to delete this game?");
    size = questionLabel.getPreferredSize();
    questionLabel.setBounds(260, 100, size.width, size.height);
    deleteConfirmationPanel.add(questionLabel);
    
    JLabel gameLabel = new JLabel(gameList.get(index).toString());
    size = gameLabel.getPreferredSize();
    gameLabel.setBounds(200, 150, size.width, size.height);
    deleteConfirmationPanel.add(gameLabel);
    
    JButton yesButton = new JButton();
    yesButton.setBounds(350, 270, 100, 40);
    try{
      Image greenYes = ImageIO.read(new File("C:\\Users\\Rose\\Documents\\Dr. Java\\Computer Science IA\\IA Buttons\\yesbutton.png")).getScaledInstance(100, 40, Image.SCALE_DEFAULT);
      yesButton.setIcon(new ImageIcon(greenYes));
    }
    catch (Exception e) {
    }
    yesButton.addActionListener(new YesButtonListener());
    deleteConfirmationPanel.add(yesButton);
    
    JButton noButton = new JButton();
    noButton.setBounds(470, 270, 100, 40);
    try{
      Image redNo = ImageIO.read(new File("C:\\Users\\Rose\\Documents\\Dr. Java\\Computer Science IA\\IA Buttons\\nobutton.png")).getScaledInstance(100, 40, Image.SCALE_DEFAULT);
      noButton.setIcon(new ImageIcon(redNo));
    }
    catch (Exception e) {
    }
    noButton.addActionListener(new NoButtonListener());
    deleteConfirmationPanel.add(noButton);
    
    deleteConfirmationScreen.addWindowListener(new WindowAdapter());
    deleteConfirmationScreen.add(deleteConfirmationPanel);
    deleteConfirmationScreen.setSize(700, 400);
    deleteConfirmationScreen.setLocation(600, 300);
    deleteConfirmationScreen.setVisible(true);
  }
  
  //code for the yes button
  //removes the game at the index value, then recalculates all the streaks and whatnot, and sorts the games again
  private static class YesButtonListener implements ActionListener {
    public void actionPerformed (ActionEvent e) {
      gameList.remove(index);
      calculateCurrentStreak();
      calculatePlayerHighestStreak();
      getCurrentWinner();
      deleteConfirmationScreen.setVisible(false);
      gameDisplayScreen();
    }
  }
  
  //code for the no button, goes back to the delete selection screen
  private static class NoButtonListener implements ActionListener {
    public void actionPerformed (ActionEvent e) {
      deleteConfirmationScreen.setVisible(false);
      deleteSelectionScreen();
    }
  }
  
  //code for when the window is closed
  //the purpose of this is to export the code into the .txt file when the window closes
  //I had to override all the WindowEvents or else it wouldn't compile
  private static class WindowAdapter implements WindowListener {
    @Override
    public void windowOpened(WindowEvent e) {
    }
    @Override
    public void windowClosing(WindowEvent e) {
      try{
      FileWriter writer = new FileWriter("C:\\Users\\Rose\\Documents\\Dr. Java\\Computer Science IA\\IA .txt Files\\GameData.txt"); 
      for(Game x: gameList) {
      writer.write(x.toStringTextFile() + System.lineSeparator());
      }
      writer.close();
      } catch (IOException e2) {
      JFrame error = new JFrame();
      JOptionPane.showMessageDialog(error,"An error has occurred.");  
      e2.printStackTrace();
      }
      System.exit(0);
    }
    @Override
    public void windowClosed(WindowEvent e) {
    }
    @Override
    public void windowIconified(WindowEvent e) {
    }
    @Override
    public void windowDeiconified(WindowEvent e) {
    }
    @Override
    public void windowActivated(WindowEvent e) {
    }
    @Override
    public void windowDeactivated(WindowEvent e) {
    }
  }
  
}