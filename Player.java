public class Player {
  //private instance variables
  private String playerName;
  private int currentStreak;
  private int playerHighestStreak;
  
  //constructors
  public Player() {
    playerName = "default name";
    currentStreak = 0;
    playerHighestStreak = 0;
  }
  
  public Player(String playerName, int currentStreak, int playerHighestStreak) {
    this.playerName = playerName;
    this.currentStreak = currentStreak;
    this.playerHighestStreak = playerHighestStreak;
  }
  
  //getter and setter methods
  public String getPlayerName() {
    return playerName;
  }
  
  public void setPlayerName(String playerName) {
    this.playerName = playerName;
  }
  
  public int getCurrentStreak() {
    return currentStreak;
  }
  
  public void setCurrentStreak(int currentStreak) {
    this.currentStreak = currentStreak;
  }
  
  public int getPlayerHighestStreak() {
    return playerHighestStreak;
  }
  
  //toString method
  public String toString() {
    return "Player Name: " + playerName + "\nCurrent Streak: " + currentStreak + "\nPlayer Highest Streak: " + playerHighestStreak + "\n";
  }
  
  public void setPlayerHighestStreak(int playerHighestStreak) {
    this.playerHighestStreak = playerHighestStreak;
  }
}