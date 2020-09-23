import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Team Name: El Cucharachas
 *
 * Students: - Ahmed Jouda 18329393 - Sean Mcdonnell 18391961 - Lleno Anya
 * 18357493
 *
 */

public class Bot1 implements BotAPI {

	// The public API of Bot must not change
	// This is ONLY class that you can edit in the program
	// Rename Bot to the name of your team. Use camel case.
	// Bot may not alter the state of the game objects
	// It may only inspect the state of the board and the player objects

	private PlayerAPI me;
	private OpponentAPI opponent;
	private BoardAPI board;
	private UserInterfaceAPI info;
	private DictionaryAPI dictionary;
	private int turnCount;
	private boolean valid = false;
	private boolean opposite = false;
	private ArrayList<String> movePossibilities = new ArrayList<>();
	private int exchange = 0;

	Bot1(PlayerAPI me, OpponentAPI opponent, BoardAPI board, UserInterfaceAPI ui, DictionaryAPI dictionary) {
		this.me = me;
		this.opponent = opponent;
		this.board = board;
		this.info = ui;
		this.dictionary = dictionary;
		turnCount = 0;
	}

	public String getCommand() {
		// Add your code here to input your commands
		// Your code must give the command NAME <botname> at the start of the game
		if (turnCount == 0) {
			turnCount++;
			return "NAME BOB";
		}
		String command;
		if (me.getFrameAsString().contains("_")) {
			if (exchange > 0) {
				command = "PASS";
			} else {
				command = "EXCHANGE _";
				exchange++;
			}
		} else {
			sampleCommands();
			exchange = 0;
			if (movePossibilities.size() == 0) // no possible move
			{
				command = "PASS";
				System.out.println("no possible words can be placed");
			} else {
				int max = movePossibilities.get(0).length();
				int maxInd = 0;
				for (int i = 0; i < movePossibilities.size(); i++) {
					if (movePossibilities.get(i).length() > max) {
						maxInd = i;
					}
				}

				command = movePossibilities.get(maxInd);
			}
		}

		turnCount++;
		return command;
	}

	String canPlaceWord(String coordinates) {
		int x = coordinates.charAt(0) - 65;
		String yVal = "" + coordinates.charAt(1);
		if (coordinates.length() == 3) {
			yVal += "" + coordinates.charAt(2);
		}

		int y = Integer.parseInt(yVal) - 1;

		if (x == 0 && y == 0) {
			coordinates += " a" + getLengthWordAcross(y, x + 1) + " d" + getLengthWordDown(y + 1, x) + " l0 u0";
		} else if (y == 0 && !board.getSquareCopy(y, x + 1).isOccupied()) {
			coordinates += " a0 d" + getLengthWordDown(y + 1, x) + " l" + getLengthWordLeft(y, x - 1) + " u0";
		} else if (y == 0 && !board.getSquareCopy(y, x - 1).isOccupied()) {
			coordinates += " a" + getLengthWordAcross(y, x + 1) + " d" + getLengthWordDown(y + 1, x) + " l0 u0";
		} else if (x == 0 && !board.getSquareCopy(y + 1, x).isOccupied()) {
			coordinates += " a" + getLengthWordAcross(y, x + 1) + " d0 l0 u" + +getLengthWordUp(y - 1, x);
		} else if (x == 0 && !board.getSquareCopy(y - 1, x).isOccupied()) {
			coordinates += " a" + getLengthWordAcross(y, x + 1) + " d" + getLengthWordDown(y + 1, x) + " l0 u0";
		} else if (x == 14 && y == 14) {
			coordinates += " a0 d0 l" + getLengthWordLeft(y, x - 1) + " u" + getLengthWordUp(y - 1, x);
		} else if (y == 14 && !board.getSquareCopy(y, x - 1).isOccupied()) {
			coordinates += " a" + getLengthWordAcross(y, x + 1) + " d0 l0 u" + getLengthWordUp(y - 1, x);
		} else if (y == 14 && !board.getSquareCopy(y, x + 1).isOccupied()) {
			coordinates += " a0 d0 l" + getLengthWordLeft(y, x - 1) + " u" + getLengthWordUp(y - 1, x);
		} else if (x == 14 && !board.getSquareCopy(y - 1, x).isOccupied()) {
			coordinates += " a0 d" + getLengthWordDown(y + 1, x) + " l" + getLengthWordLeft(y, x - 1) + " u0";
		} else if (x == 14 && !board.getSquareCopy(y + 1, x).isOccupied()) {
			coordinates += " a0 d0 l" + getLengthWordLeft(y, x - 1) + " u" + getLengthWordUp(y - 1, x);
		} else {
			if (x > 0 && !board.getSquareCopy(y, x - 1).isOccupied()) {
				coordinates += " a" + getLengthWordAcross(y, x + 1);
			} else {
				coordinates += " a0";
			}

			if (y > 0 && !board.getSquareCopy(y - 1, x).isOccupied()) {
				coordinates += " d" + getLengthWordDown(y + 1, x);
			} else // if x and y are 14
			{
				coordinates += " d0";
			}

			if (x < 14 && !board.getSquareCopy(y, x + 1).isOccupied()) {
				coordinates += " l" + getLengthWordLeft(y, x - 1);
			} else {
				coordinates += " l0";
			}

			if (y < 14 && !board.getSquareCopy(y + 1, x).isOccupied()) {
				coordinates += " u" + getLengthWordUp(y - 1, x);
			} else // if x and y are 14
			{
				coordinates += " u0";
			}
		}
		// coordinates += " a" + getLengthWordAcross(y, x+1)+ " d" +
		// getLengthWordDown(y+1, x)+ " l"+getLengthWordLeft(y, x-1)+ " u" +
		// getLengthWordUp(y-1, x);
		// System.out.println(coordinates);
		return coordinates;
	}

	private int getLengthWordDown(int y, int x) {
		if (y == 15) {
			return 0;
		} else if (y < 14 && board.getSquareCopy(y + 1, x).isOccupied()) {
			return 0;
		} else if (board.getSquareCopy(y, x).isOccupied()) {
			return 0;
		} else if (x > 0 && board.getSquareCopy(y, x - 1).isOccupied()) {
			return 0;
		} else if (x < 14 && board.getSquareCopy(y, x + 1).isOccupied()) {
			return 0;
		} else {

			return 1 + getLengthWordDown(y + 1, x);
		}
	}

	private int getLengthWordAcross(int y, int x) {
		if (x == 15) {
			return 0;
		} else if (x < 14 && board.getSquareCopy(y, x + 1).isOccupied()) {
			return 0;
		} else if (board.getSquareCopy(y, x).isOccupied()) {
			return 0;
		} else if (y > 0 && board.getSquareCopy(y - 1, x).isOccupied()) {
			return 0;
		} else if (y < 14 && board.getSquareCopy(y + 1, x).isOccupied()) {
			return 0;
		} else {
			return 1 + getLengthWordAcross(y, x + 1);
		}
	}

	private int getLengthWordUp(int y, int x) {
		if (y == -1) {
			return 0;
		} else if (y > 0 && board.getSquareCopy(y - 1, x).isOccupied()) {
			return 0;
		} else if (board.getSquareCopy(y, x).isOccupied()) {
			return 0;
		} else if (x > 0 && board.getSquareCopy(y, x - 1).isOccupied()) {
			return 0;
		} else if (x < 14 && board.getSquareCopy(y, x + 1).isOccupied()) {
			return 0;
		} else {

			return 1 + getLengthWordUp(y - 1, x);
		}
	}

	private int getLengthWordLeft(int y, int x) {
		if (x == -1) {
			return 0;
		} else if (x > 0 && board.getSquareCopy(y, x - 1).isOccupied()) {
			return 0;
		} else if (board.getSquareCopy(y, x).isOccupied()) {
			return 0;
		} else if (y > 0 && board.getSquareCopy(y - 1, x).isOccupied()) {
			return 0;
		} else if (y < 14 && board.getSquareCopy(y + 1, x).isOccupied()) {
			return 0;
		} else {
			return 1 + getLengthWordLeft(y, x - 1);
		}
	}

	private void sampleCommands() {
		String command;
		movePossibilities.clear();
		if (board.getSquareCopy(7, 7).isOccupied()) {
			for (int i = 0; i < 15; i++) {
				for (int j = 0; j < 15; j++) {
					if (board.getSquareCopy(i, j).isOccupied() && !board.getSquareCopy(i, j).getTile().isBlank()) {
						char tile = board.getSquareCopy(i, j).getTile().getLetter();
						char x = (char) (j + 65);
						int y = (i + 1);
						String coord = "" + x + y;

						StringTokenizer com = new StringTokenizer(canPlaceWord(coord));
						coord = com.nextToken();
						for (int dir = 0; dir < 4; dir++) {
							valid = false;
							opposite = false;
							command = com.nextToken();
							String data = "";
							int numletters = 0;

							if (command.charAt(1) != '0') // across
							{
								data = "" + command.charAt(1);
								if (command.length() == 3) {
									data += "" + command.charAt(2);
								}

								char direction;
								numletters = Integer.parseInt(data);

								if (command.charAt(0) == 'l') {
									opposite = true;
									direction = 'a';
								} else if (command.charAt(0) == 'u') {
									opposite = true;
									direction = 'd';
								} else {
									direction = command.charAt(0);
								}

								String word = validWords(tile, numletters);
								if (valid) {
									if (command.charAt(0) == 'l') {
										x -= (word.length() - 1);
									} else if (command.charAt(0) == 'u') {
										y -= (word.length() - 1);
									}

									command = "" + x + "" + y + ' ' + direction + ' ' + word;
									movePossibilities.add(command);
								}
							}
						}
					}
				}
			}
		} else // if the board is empty places first word across
		{
			command = "H8 A ";
			command += validWords('#', 6);
			if (valid) {
				movePossibilities.add(command);
			}
		}
	}

	static ArrayList<String> perm = new ArrayList<String>();

	// Recursive function to generate all permutations of a String
	private static void permutations(String candidate, String remaining) {
		if (remaining.length() == 0) {
			perm.add(candidate);
		}

		for (int i = 0; i < remaining.length(); i++) {
			String newCandidate = candidate + remaining.charAt(i);

			String newRemaining = remaining.substring(0, i) + remaining.substring(i + 1);

			permutations(newCandidate, newRemaining);
		}
	}

	static ArrayList<String> perm2 = new ArrayList<String>();

	private static void morePerm(String str) {
		for (int i = 0; i < str.length(); i++) {
			for (int j = i + 1; j <= str.length(); j++) {
				perm2.add(str.substring(i, j));
			}
		}
	}

	String validWords(char tile, int size) {
		ArrayList<Word> potentialWord = new ArrayList<Word>(); // Arraylist to store the word to be dictionary checked
		String finalWord = null;
		Word tempWord;
		String opString = "";
		String playerFrame = me.getFrameAsString();
		StringBuilder playerFrameLetters = new StringBuilder();
		for (int i = 0; i < playerFrame.length(); i++) // converting frame string [letter, letter,.....,letter] to
														// letterletter...letter
		{
			if (playerFrame.charAt(i) >= 'A' && playerFrame.charAt(i) <= 'Z') {
				playerFrameLetters.append(playerFrame.charAt(i));
			}
		}
		String tempString;
		// get combinations from frame
		String inputString = playerFrameLetters.toString();
		// combine(size); //get all combinations of a max length "size"
		// permutationFinder("a b");
		perm.clear();
		perm2.clear();
		permutations("", inputString);
		for (String s : perm) {
			morePerm(s);
		}
		perm.addAll(perm2);
		int length = 0;

		// loop over combinations checking each string
		for (int i = 0; i < perm.size(); i++) {
			potentialWord.clear();
			if (tile != '#') // if tile char is # then the board is empty
			{
				if (opposite) {
					tempString = perm.get(i) + tile;
				} else {
					tempString = tile + perm.get(i);
				}
			} else {
				tempString = perm.get(i);
			}
			tempWord = new Word(7, 7, true, tempString);// coordinates dont matter
			// add that to the array list then pass it into the dictionary check
			// if the dictionary check is passed then done = true and assign that word to
			// finalWord
			potentialWord.add(tempWord);
			if (dictionary.areWords(potentialWord) && potentialWord.get(0).length() <= size + 1) {
				if (tempString.length() > length) {
					length = tempString.length();
					finalWord = tempString;
				}
				valid = true;
			}

		}

		return finalWord;

	}

}
