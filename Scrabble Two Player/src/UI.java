import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Screen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Team Name: El Cucharachas
 * 
 * Students: - Ahmed Jouda 18329393 - Sean Mcdonnell 18391961 - Lleno Anya
 * 18357493
 *
 */

public class UI {
	static Rectangle2D screenBounds = Screen.getPrimary().getBounds(); // used to make the program proportional to the
																		// users screen

	Square[] frameHolder = new Square[7];
	Label score1;
	Label score2;
	TextField textBox;
	TilePane framePane;
	Frame currentFrame;
	Label instructionLabel;
	String[] inputText;
	TilePane boardPane;
	Board board;
	Pool pool;
	Label turnLabel;
	TilePane scores;
	Player[] players = new Player[2];
	int turn = 0;
	int pass = 0;
	int lastScore = 0; // score of last word placed
	Tree dictionary;

	UI() {
		pool = new Pool();
		board = new Board();
		textBox = new TextField();


		try {
			readDictionary();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setInstructionLabel();
		setTurnLabel();
		setFramePane();
		setScores();
		setPlayers();
		setBoardPane();

		textBox.setOnAction(e -> {
			turn += parseInput(textBox.getText());
			turnLabel.setText("Enter command " + players[(turn) % 2].getName());
			setCurrentFrame(players[turn % 2].getFrame());

			if (checkWin()) {
				instructionLabel.setText("Game Over");
				int scoreFromPlayerFrame1 = 0;
				int scoreFromPlayerFrame2 = 0;

				ArrayList<Tile> temp = players[1].getFrame().getTiles();
				for (int i = 0; i < players[1].getFrame().size(); i++) {
					scoreFromPlayerFrame1 += temp.get(i).getValue();
				}

				temp = players[0].getFrame().getTiles();
				for (int i = 0; i < players[0].getFrame().size(); i++) {
					scoreFromPlayerFrame2 += temp.get(i).getValue();
				}

				if (players[0].getFrame().isEmpty()) {
					scoreFromPlayerFrame1 *= 2;
					players[0].addScore(scoreFromPlayerFrame1);
					score1.setText(players[0].getName() + "\n" + players[0].getScore());

				} else if (players[1].getFrame().isEmpty()) {
					scoreFromPlayerFrame2 *= 2;
					players[1].addScore(scoreFromPlayerFrame2);
					score2.setText(players[1].getName() + "\n" + players[1].getScore());
				} else {
					scoreFromPlayerFrame1 *= -1;
					scoreFromPlayerFrame2 *= -1;
					players[0].addScore(scoreFromPlayerFrame2);
					players[1].addScore(scoreFromPlayerFrame1);
					score1.setText(players[0].getName() + "\n" + players[0].getScore());
					score2.setText(players[1].getName() + "\n" + players[1].getScore());
				}

				if (players[0].getScore() > players[1].getScore()) {
					instructionLabel.setText(players[0].getName() + " WINS!!!");
				} else if (players[1].getScore() > players[0].getScore()) {
					instructionLabel.setText(players[1].getName() + " WINS!!!");
				} else {
					instructionLabel.setText(" ITS A TIE!!!");
				}
				textBox.setOnAction(null);
			}
			textBox.clear();

		});

	}

	public void setPlayers() {
		players[0] = new Player();
		players[1] = new Player();
		players[0].setName("Player 1");
		players[1].setName("Player 2");
		players[0].getFrame().refill(pool);
		score1.setText(players[0].getName() + "\n" + players[0].getScore());
		players[1].getFrame().refill(pool);
		setCurrentFrame(players[turn].getFrame());
		score2.setText(players[1].getName() + "\n" + players[1].getScore());
	}
	
	public void setInstructionLabel() {
		instructionLabel = new Label("'HELP' to get instructions");
		instructionLabel.setWrapText(true);
		instructionLabel.setMaxWidth(UI.screenBounds.getWidth() / 10);
		instructionLabel.setMinHeight(UI.screenBounds.getHeight() * 2 / 4);
		instructionLabel.setAlignment(Pos.CENTER);
		instructionLabel.setStyle("-fx-background-color: rgba(5, 37, 4, .4)");
	}

	public void setTurnLabel() {
		
		turnLabel = new Label("Enter command Player 1");
		turnLabel.setAlignment(Pos.CENTER);
		turnLabel.setPrefSize(UI.screenBounds.getHeight() / 5.2, UI.screenBounds.getHeight() / 22.5);
		turnLabel.setStyle("-fx-background-color: rgba(5, 37, 4, .6)");
	}

	public void setFramePane() {
		framePane = new TilePane();
		for (int j = 0; j < 7; j++) {
			frameHolder[j] = new Square();
		}
		for (int j = 0; j < 7; j++) {
			framePane.getChildren().add(frameHolder[j]);
			framePane.setTileAlignment(Pos.TOP_LEFT);
		}
	}

	public TilePane getScores() {
		return scores;
	}

	public void setScores() {
		scores = new TilePane();
		scores.setMaxWidth(225);
		scores.setHgap(25);
		scores.setMinHeight(50);

		scores.setPrefWidth(300);
		score1 = new Label();
		score2 = new Label();

		score1.setAlignment(Pos.CENTER);
		score2.setAlignment(Pos.CENTER);
		String scoreFont = "-fx-font-size: " + UI.screenBounds.getHeight() / 72 + "px;";
		score1.setStyle(scoreFont);
		score2.setStyle(scoreFont);

		score1.setPrefSize(100, 50);
		score2.setPrefSize(100, 50);

		scores.getChildren().add(score1);
		scores.getChildren().add(score2);
	}

	boolean checkWin() {
		if (pass >= 6) {
			return true;
		} else
			return (pool.isEmpty() && (players[0].getFrame().isEmpty() || players[1].getFrame().isEmpty()));
	}

	public TilePane getFramePane() {
		return framePane;
	}

	public void setCurrentFrame(Frame currentFrame) {

		this.currentFrame = currentFrame;
		for (int i = 0; i < 7; i++) {
			frameHolder[i].setText(" ");
			frameHolder[i].setStyle(
					"-fx-background-color: rgb(216, 226, 238); -fx-text-fill: black; -fx-border-color: black;");
		}
		for (int j = 0; j < currentFrame.size(); j++) {
			frameHolder[j].setTile(currentFrame.getTiles().get(j));
		}

	}

	public TilePane getBoardPane() {
		return boardPane;
	}

	void setBoardPane() {
		boardPane = new TilePane();
		boardPane.setPrefSize(UI.screenBounds.getWidth() / 2.4, 0);
		boardPane.setPrefColumns(16);
		boardPane.setPrefRows(16);
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				boardPane.getChildren().add(board.squares[i][j]);
				boardPane.setTileAlignment(Pos.TOP_LEFT);
			}
			boardPane.getChildren().add(new Label(Integer.toString(i + 1))); // Adding the Y co-ordinates
		}
		for (int i = 0; i < 15; i++) {
			boardPane.getChildren().add(new Label(" " + (char) (i + 65))); // Adding the x co-ordinates
		}

	}

	GridPane printGame() {
		GridPane gridPane = new GridPane();
		gridPane.setVgap(15);
		gridPane.add(getScores(), 0, 0);
		gridPane.add(getBoardPane(), 0, 1);
		gridPane.add(getFramePane(), 0, 2);

		gridPane.add(instructionLabel, 1, 1);
		gridPane.add(turnLabel, 1, 2);
		gridPane.add(textBox, 1, 3);

		gridPane.setStyle("-fx-background-color: rgb(5, 37, 4, 0.658);");
		return gridPane;
	}
	void readDictionary() throws IOException {
		InputStream is = getClass().getResourceAsStream("dictionary.txt");
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader reader = new BufferedReader(isr);
		String line;
		dictionary = new Tree();
		while((line = reader.readLine()) != null){
			dictionary.set(line);
		}
	}


	// takes in a string "command" and returns an int, if 1 then give turn to next
	// player, if 0 give turn to same player
	int parseInput(String command) {
		score1.setText(players[0].getName() + "\n" + players[0].getScore());
		score2.setText(players[1].getName() + "\n" + players[1].getScore());
		// change it to upper case in case user input it in lower case
		command = command.toUpperCase();
		// If the command is Help it overrides other commands
		if (command.equals("HELP")) {
			instructionLabel.setText("- To place words enter: <x-coordinate><y-coordinate> <across/down> <word>\n Example: 'A1 DOWN HELLO'\n"
					+ "to use a blank tile simply write the word out and the blank tile will automatically replace the tile not in your frame"
					+ "- To Exchange letters, enter: EXCHANGE <letters to exchange>\n"
					+ "Use underscore to represent the blank tile in exchange\n" + "- To pass turn, enter: PASS\n"
					+ "- To challenge previous word, enter CHALLENGE\n" + "- To quit game, enter QUIT");
			return 0;
		} else {
			// if the player challenged for now manually subtract score at the end of the
			// game
			// each player has a negative score marker

				inputText = command.split(" ", 0);
				int i = 0;

				switch (inputText[i]) {
				// if the player input QUIT exit the system
					case "":
						return 0;
					case "QUIT":
						System.exit(0);
						// if the player input PASS then increment the pass counter and return 1 to
						// pass the play to the other player
					case "PASS":
						pass++;
						return 1;
					case "NAME":
						String newName = inputText[++i];
						players[turn%2].setName(newName);
						score1.setText(players[0].getName() + "\n" + players[0].getScore());
						score2.setText(players[1].getName() + "\n" + players[1].getScore());
						return 0;
					// if the player inputs Exchange and a list of tiles
					case "EXCHANGE":
						// check if pool has 7 or more tiles left
						if (pool.size() < 7) {
							instructionLabel.setText("Cannot exchange\n" + "There are less than 7 tiles left in the pool");
							return 0;
						} else {
							try {
								String letters = inputText[++i];
								// check if the tiles inputed are actually in the player's frame
								if (!players[turn % 2].getFrame().isAvailable(letters).contains("t")) {
									instructionLabel
											.setText("Cannot exchange\n" + "Your frame does not contain the tiles you entered\n"
													+ "(Use _ to represent a blank tile for exchange");
									return 0;
								} else {
									// remove tiles and refill new ones
									players[turn % 2].getFrame().remove(letters);
									players[turn % 2].getFrame().refill(pool);
									pass = 0;
									instructionLabel.setText("'HELP' to get instructions");
									return 1;
								}
							}catch(Exception e)
							{
								instructionLabel.setText("Cannot exchange\n No Letters Input");
								return 0;
							}
						}
						// if the user inputs challenge
					case "CHALLENGE":
						//Checks if the challenged words are valid
						int returnInt = 0;
						if(pass == 0) {
							if (!board.squares[7][7].isEmpty()) {

								for (int j = 0; j < (Board.wordCount + 1); j++) {
									if (!dictionary.find(Board.challengeWords[j])) {
										instructionLabel.setText("SUCCESSFUL CHALLENGE\nWORD NOT IN DICTIONARY");
										players[(turn + 1) % 2].addScore(lastScore * -1);
										score1.setText(players[0].getName() + "\n" + players[0].getScore());
										score2.setText(players[1].getName() + "\n" + players[1].getScore());
										board.removeLast();
										players[(turn + 1) % 2].getFrame().revert(pool);
										returnInt = 0;
										break;
									} else {
										returnInt = 1;        //skips challengers turn if challenge fails
									}
								}
								if(returnInt == 1)
								{
									instructionLabel.setText("UNSUCCESSFUL CHALLENGE\nWORD IN DICTIONARY");
									pass++;
								}
							} else {
								instructionLabel.setText("CANNOT CHALLENGE\nNO WORD ON BOARD");
							}
						}else if(pass % 2 == 1)
						{
							instructionLabel.setText("CANNOT CHALLENGE YOUR OWN WORD");
						}else if(pass % 2 == 0)
						{
							instructionLabel.setText("CANNOT CHALLENGE WORD PLACED BEFORE THE LAST TURN");
						}

							return returnInt;
					// otherwise the player has inputed a grid reference, direction and word
					// if the word is placeable, place it and add its score to the player's score
					default:
						try {
							String gridRef = inputText[i++];
							char xGridRef = gridRef.charAt(0);
							String yCoord = gridRef.substring(1);
							int y = Integer.parseInt(yCoord) - 1;
							String direction = inputText[i++];
							String word = inputText[i];

							if (word.length()==1) {
								throw new IllegalArgumentException("Cannot place a one letter word");
							}
							players[turn % 2]
									.addScore(lastScore = board.placeWord(players[turn % 2], word, xGridRef, y, direction));

							score1.setText(players[0].getName() + "\n" + players[0].getScore());
							score2.setText(players[1].getName() + "\n" + players[1].getScore());
							players[turn % 2].getFrame().refill(pool);
							pass = 0;
							instructionLabel.setText("'HELP' to get instructions");
							return 1;
						} catch (Exception e) {
							instructionLabel.setText("Invalid input\n" + e.getMessage() + "\nType HELP for help");
						}
						return 0;

			}
		}
	}
}
