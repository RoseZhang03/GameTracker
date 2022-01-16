public class Game {
  //private instance variables
  private String date;
  private String gameType;
  private String winner;
  
  //constructors
  public Game() {
    date = "0000-00-00";
    gameType = "default";
    winner = "default name";
  }
  
  public Game(String date, String gameType, String winner) {
    this.date = date;
    this.gameType = gameType;
    this.winner = winner;
  }
  
  //getter and setter methods
  public String getDate() {
    return date;
  }
  
  public void setDate(String date) {
    this.date = date;
  }
  
  public String getGameType() {
    return gameType;
  }
  
  public void setGameType(String gameType) {
    this.gameType = gameType;
  }
  
  public String getWinner() {
    return winner;
  }
  
  public void setWinner(String winner) {
    this.winner = winner;
  }
  
  
  //toString methods
  public String toString() {
    return "Date: " + date.substring(5, 7) + "/" + date.substring(8) + "/" + date.substring(0, 4) + 
           "     Game: " + gameType + "     Winner: " + winner;
  }
  
  public String toStringDateFormatted() {
    return date.substring(5, 7) + "/" + date.substring(8) + "/" + date.substring(0, 4);
  }
  
  public String toStringTextFile() {
    return date + "," + gameType + "," + winner;
  }
}