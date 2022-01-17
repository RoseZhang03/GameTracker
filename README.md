# GameTracker
The code is compiled in Dr.Java

This program imports text data from a .txt file, sorts the data by date, runs calculations to determine player win statistics, and display data on a GUI. Changes such as adding a new game, editing info of an existing game, or deleting a game can be made through the GUI. When closed, changes are exported to the .txt file

The Game class constructs the Game object, assigning it private instant variables String date, String gameType, and String winner. These will later store the data imported from the .txt file. This class also contains getter and setter method which are used to retrieve or change the paramaters of a game object.

The Player class constructs the Player object, assigning it private instant variables String playerName, int currentStreak, and int playerHighestStreak. playerName stores either "player1" or "player2" for the sake of privacy, but in practical real names can be used. This class also contains getter and setter method. Each Player object is created with only a playName parameter, with 0's filling the other two parameters. The setter method is used to change the currentStreak and playerHighestStreak after calculations are run in the main method.

The GameTracker class contains the main method. Here, various methods are called upon to import and sort data from the .txt file, run calculations, and display a GUI. 
Future advancements will be made to allow the program to import from a Google Sheets spreadsheet, and also to refine the GUI (switch JPanels instead of JFrames) for a cleaner transition between screens.
