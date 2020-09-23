import java.util.ArrayList;
import java.util.Arrays;

/**
 * Team Name: El Cucharachas
 * 
 * Students: - Ahmed Jouda 18329393 - Sean Mcdonnell 18391961 - Lleno Anya
 * 18357493
 *
 */
public class Board {
	static int[] taken = { 0, 0, 0, 0, 0, 0, 0 ,0, 0, 0, 0, 0, 0, 0, 0 }; // needed to check whether word is placed around already paced letter
	static int[] prevTaken = { 0, 0, 0, 0, 0, 0, 0 ,0, 0, 0, 0, 0, 0, 0, 0 };
	boolean connects = false; // check if placed word connects with other words
	static String lettersToRemove = ""; // contains letters to remove from player's frame after placement
	Square[][] squares;
	static int wordMultiplier = 1;
	static int letterMultiplier = 1;
	static String symbolicBlankLetters = "";
	static String[] challengeWords = new String[8];
	static int wordCount = 0;
	static int prevFirstLetterX = 0;
	static int prevFirstLetterY = 0;
	static String prevDirection = "";
	static int prevWordLength = 0;
	String error = "";

	// Constructor
	Board() {
		squares = new Square[15][15];
		// Sets Value of Each Square
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				squares[i][j] = new Square();
				if ((i + j) % 7 == 0 && (i == 0 || i == 7 || i == 14)) {
					squares[i][j].setValue("3W");
				} else if (((i + j - 6) % 4 == 0 && (i == 5 || i == 9 || i == 13 || i == 1) && Math.abs(i - j) > 3
						&& Math.abs(i - j) < 9) || (i == j && j == 9) || (i == j && j == 5)) {
					squares[i][j].setValue("3L");
				} else if (((i == 6 || i == 8) && (j == 6 || j == 8))
						|| (i != j && (i == 3 || i == 11 || i == 14 || i == 0)
								&& (j == 3 || j == 11 || j == 14 || j == 0))
						|| (i != j && (i == 8 || i == 6 || i == 12 || i == 2)
								&& (j == 2 || j == 6 || j == 8 || j == 12))
						|| i == 7 && (j == 3 || j == 11) || j == 7 && (i == 3 || i == 11)) {
					squares[i][j].setValue("2L");
				} else if (i == j || i + j == 14) {
					squares[i][j].setValue("2W");
				}

			}
		}
		squares[7][7].setValue("*");
	}

	// Allows the board to be reset
	public void resetBoard() {
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				squares[i][j].setTile(null);
				squares[i][j].setEmpty(true);
			}
		}
	}

	public void printBoard() {
		System.out.println("\n----------------------------------------------------------------\n");
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				System.out.print(squares[i][j].toString());
			}
			System.out.print(" " + i + "\n");
		}

		// Prints Square Co-ordinates
		for (int i = 0; i < 10; i++) {
			System.out.print("  " + i + " ");
		}
		for (int i = 10; i < 15; i++) {
			System.out.print(" " + i + " ");
		}
		System.out.println("\n----------------------------------------------------------------\n");

	}

	// Places the word on the board if it is valid and removes the used tiles from
	// the Player's frame
	public int placeWord(Player p, String word, char alphFirstLetterX, int firstLetterY, String direction)
			throws IllegalArgumentException {
		int firstLetterX = 0;
		int score = 0;
		firstLetterX = letterGridToIndex(Character.toUpperCase(alphFirstLetterX));
		direction = direction.toLowerCase();
		word = word.toUpperCase(); // in case the user input lower case
		error = "";
		lettersToRemove = ""; // reset letters to remove
		// if the word is invalid throw an illegal argument exception
		if (!isValidWord(p, word, firstLetterX, firstLetterY, direction)) {
			lettersToRemove = "";
			throw new IllegalArgumentException(error);
		}

		System.arraycopy(taken,0,prevTaken,0,15);
		prevFirstLetterX = firstLetterX;
		prevFirstLetterY = firstLetterY;
		prevDirection = "";
		prevDirection += direction;
		prevWordLength = word.length();
		resetChallenge();

		// Put string (given word) into a tile array list, each letter = 1 tile
		int blankIndex = 0;
		int lettersToRemoveIndex = 0;
		ArrayList<Tile> stringTiles = new ArrayList<>(); // added whole blank bit
		for (int i = 0; i < word.length(); i++) {
			if (taken[i] == 1) {
				if (direction.equals("across")) {
					if (squares[firstLetterY][firstLetterX + i].getTile().isBlank()) {
						Tile tempTile = new Tile('_');
						tempTile.setLetter(squares[firstLetterY][firstLetterX + i].getTile().getLetter());
						stringTiles.add(tempTile);
					} else {
						Tile tempTile = new Tile(word.charAt(i));
						stringTiles.add(tempTile);
					}
				} else if (direction.equals("down")) {
					if (squares[firstLetterY + i][firstLetterX].getTile().isBlank()) {
						Tile tempTile = new Tile('_');
						tempTile.setLetter(squares[firstLetterY + i][firstLetterX].getTile().getLetter());
						stringTiles.add(tempTile);
					} else {
						Tile tempTile = new Tile(word.charAt(i));
						stringTiles.add(tempTile);
					}
				}
			} else {
				Tile tempTile = new Tile(lettersToRemove.charAt(lettersToRemoveIndex));
				if (lettersToRemove.charAt(lettersToRemoveIndex) == '_') {
					tempTile.setLetter(symbolicBlankLetters.charAt(blankIndex));
					blankIndex += 1;
				}
				stringTiles.add(tempTile);
				lettersToRemoveIndex += 1;
			}
		}
		symbolicBlankLetters = "";

		score += scoring(firstLetterX, firstLetterY, direction, stringTiles);
		//lastScore = score;
		//printChallenge();

		// place on board based on direction specified
		if (direction.equals("across")) {
			for (Tile stringTile : stringTiles) {
				squares[firstLetterY][firstLetterX].setTile(stringTile);
				firstLetterX++;
			}
		} else if (direction.equals("down")) {
			for (Tile stringTile : stringTiles) {
				squares[firstLetterY][firstLetterX].setTile(stringTile);
				firstLetterY++;
			}

		} else {
			throw new IllegalArgumentException("-Invalid Direction\n");
		}
		p.getFrame().remove(lettersToRemove);
		return score;
	}

	// checks if word is being placed around letters and if they conflict

	// Checks if word is valid and returns an indicative result
	public boolean isValidWord(Player p, String word, int firstLetterX, int firstLetterY, String direction) {
		boolean valid = true; // boolean to return
		connects = false;
		if (checkBounds(word, firstLetterX, firstLetterY, direction)) {
			if (squares[7][7].isEmpty()) {

				// test if its the first word being placed is in the center, if not its not
				// valid
				if (!checkFirstWord(word, firstLetterX, firstLetterY, direction)) {
					valid = false;
				}
			}

			if (!checkConflict(word, firstLetterX, firstLetterY, direction)) {
				valid = false;

			}

			// test if players frame contains necessary letters
			if (p.getFrame().isAvailable(lettersToRemove).contains("f")) {
				error += "-The players frame does not contain the letters needed for this word\n";
				valid = false;
			} else if (!p.getFrame().isAvailable(lettersToRemove).contains("t")) // blank placement helper
			{
				int num = 0;
				String val = p.getFrame().isAvailable(lettersToRemove);
				String temp = "";
				for (int i = 0; i < val.length(); i++) {
					temp = "";
					num = Character.getNumericValue(val.charAt(i));
					for (int j = 0; j < lettersToRemove.length(); j++) {
						if (j == num) {
							symbolicBlankLetters += "" + lettersToRemove.charAt(j);
							temp += "_";
						} else {
							temp += "" + lettersToRemove.charAt(j);
						}
					}
					lettersToRemove = "";
					lettersToRemove += temp;
				}

			}

			if (!connects && !squares[7][7].isEmpty()) // tests if word connects to another word (only if not first
														// word), if not its not valid
			{
				if (!checkConnects(word, firstLetterX, firstLetterY, direction)) {
					valid = false;
				}
			}

		}
		return valid;
	}

	// Checks if the first word is being placed in the center
	boolean checkFirstWord(String word, int x, int y, String direction) {
		if (direction.equals("across")) {
			for (int i = 0; i < word.length(); i++) {
				if (x == 7 && y == 7) {
					return true;
				}
				x++;
			}
		} else if (direction.equals("down")) {
			for (int i = 0; i < word.length(); i++) {
				if (x == 7 && y == 7) {
					return true;
				}
				y++;
			}
		}

		error ="-The first word is not over the centre square\n";
		return false;

	}

	// Checks if the the word being placed connects with previously placed words
	boolean checkConnects(String word, int x, int y, String direction) {
		if (direction.equals("across")) {
			for (int i = 0; i < word.length() && !connects; i++) {
				if (i == 0 && x != 0) {
					if (!squares[y][x - 1].isEmpty()) {
						return true;
					}
				}

				if (y != 0) {
					if (!squares[y - 1][x].isEmpty()) {
						return true;
					}
				}

				if (y != 14) {
					if (!squares[y + 1][x].isEmpty()) {
						return true;
					}
				}

				if (i == word.length() - 1 && x != 14) {
					if (!squares[y][x + 1].isEmpty()) {
						return true;
					}
				}

				x++;
			}
		} else if (direction.equals("down")) {
			for (int i = 0; i < word.length() && !connects; i++) {
				{
					if (i == 0 && y != 0) {
						if (!squares[y - 1][x].isEmpty()) {
							return true;
						}
					}

					if (x != 0) {
						if (!squares[y][x - 1].isEmpty()) {
							return true;
						}
					}

					if (x != 14) {
						if (!squares[y][x + 1].isEmpty()) {
							return true;
						}
					}

					if (i == word.length() - 1 && y != 14) {
						if (!squares[y + 1][x].isEmpty()) {
							return true;
						}
					}

					y++;
				}
			}
		}

		error = "-The word does not connect to an existing word\n";
		return false;
	}

	boolean checkConflict(String word, int x, int y, String direction) {
		Arrays.fill(taken, 0);
		boolean valid = true;
		if (direction.equals("across")) {
			for (int i = 0; i < word.length(); i++) {
				if (!squares[y][x].isEmpty()) {
					if (word.charAt(i) != squares[y][x].getTile().getLetter()) {
						error = "Word cannot be placed because it is being placed on top of a letter\n";
						valid = false;
					} else {
						taken[i] = 1;
					}
				}
				x++;
			}
		} else if (direction.equals("down")) {
			for (int i = 0; i < word.length(); i++) {
				if (!squares[y][x].isEmpty()) {
					if (word.charAt(i) != squares[y][x].getTile().getLetter()) {
						error = "Word cannot be placed because it conflicts with existing letters\n";
						valid = false;
					} else {
						taken[i] = 1;
					}
				}
				y++;
			}
		}

		if (!checkLetterUsedFromFrame(taken, word)) {
			valid = false;
		}

		return valid;
	}

	boolean checkLetterUsedFromFrame(int[] taken, String word) {
		boolean atLeastOne = false; // placement uses at least one letter from the rack
		for (int i = 0; i < word.length(); i++) {
			if (taken[i] == 0) {
				lettersToRemove += "" + word.charAt(i);
				atLeastOne = true; // placement uses at least one letter from the rack
			} else {
				connects = true;
			}
		}
		// test if frame has at least one letter
		if (!atLeastOne) {
			error = "Placement does not include at least one letter from the frame\n";
			return false;
		}
		return true;
	}

	// tests if the word is within the bounds of the board
	boolean checkBounds(String word, int x, int y, String direction) {
		if (x > 14 || x < 0 || y < 0 || y > 14) {
			error = "The word is not within the bounds of the board\n";
			return false;
		} else if (direction.equals("across") && x + word.length() - 1 > 14) {
			error = "The word is not within the bounds of the board\n";
			return false;
		} else if (direction.equals("down") && y + word.length() - 1 > 14) {
			error = "The word is not within the bounds of the board\n";
			return false;
		} else
			return true;
	}

	int scoring(int x, int y, String direction, ArrayList<Tile> stringTiles) {
		wordMultiplier = 1;
		letterMultiplier = 1;
		int score = 0;
		int currentWordScore = 0;
		int gridx = x;
		int gridy = y;
		boolean word;
		boolean end = false;
		boolean last = false;
		wordCount = 0;

		for (int index = 0; index < stringTiles.size(); index++) // only for these ones side wordds
		{
			word = false;
			if (direction.equals("across")) {
				if (!last) {
					if (squares[y][x].isEmpty()) // need to figure out
					{
						if (y != 0) // these two groups of if statements check if their is a word on the line
						{
							if (!squares[y - 1][x].isEmpty()) {
								word = true;
							}
						}

						if (y != 14) {
							if (!squares[y + 1][x].isEmpty()) {
								word = true;
							}
						}

						if (word) {
							multiplier(squares[y][x].getValue());
							y = rewind('d', x, y);
							while (y < 15 && !end) {
								if (squares[y][x].isEmpty() && y != gridy) {
									end = true;
								} else {

									if (y == gridy) {
										challengeWords[wordCount] += "" + stringTiles.get(index).getLetter();
										currentWordScore += (stringTiles.get(index).getValue() * letterMultiplier);
									} else {
										challengeWords[wordCount] += "" + squares[y][x].getTile().getLetter();
										currentWordScore += squares[y][x].getTile().getValue();
									}
									y++;
								}
							}
							score += (currentWordScore * wordMultiplier);
							currentWordScore = 0;
							wordMultiplier = 1;
							letterMultiplier = 1;
							y = gridy;
							x++;
							wordCount++;
							end = false;
						} else {
							x++;
						}
					} else {
						x++; // moves across the taken letter

					}
					if (index == stringTiles.size() - 1) {
						index = -1; // resets loop
						last = true;
						x = gridx;
						x = rewind('r', x, y);
					}
				} else {
					while (x < gridx) // end = false before it gets to last so this only runs once aka until the
										// placed letters
					{
						challengeWords[wordCount] += "" + squares[y][x].getTile().getLetter();
						currentWordScore += squares[y][x].getTile().getValue();
						x++;
					}

					if (squares[y][x].isEmpty()) {
						multiplier(squares[y][x].getValue());
					}

					challengeWords[wordCount] += "" + stringTiles.get(index).getLetter();
					currentWordScore += (stringTiles.get(index).getValue() * letterMultiplier);
					letterMultiplier = 1;
					x++;

					if (index == stringTiles.size() - 1) {
						end = false;
						while (x < 15 && !end) {
							if (squares[y][x].isEmpty()) {
								end = true;
							} else {
								challengeWords[wordCount] += "" + squares[y][x].getTile().getLetter();
								currentWordScore += squares[y][x].getTile().getValue();
								x++;
							}
						}

						currentWordScore *= wordMultiplier;
						score += currentWordScore;
					}
				}
			} else if (direction.equals("down")) {
				if (!last) {
					if (squares[y][x].isEmpty()) // need to figure out
					{
						if (x != 0) // these two groups of if statements check if their is a word on the line
						{
							if (!squares[y][x - 1].isEmpty()) {
								word = true;
							}
						}

						if (x != 14) {
							if (!squares[y][x + 1].isEmpty()) {
								word = true;
							}
						}

						if (word) {
							multiplier(squares[y][x].getValue());
							x = rewind('r', x, y);

							while (x < 15 && !end) {
								if (squares[y][x].isEmpty() && x != gridx) {
									end = true;
								} else {

									if (x == gridx) {
										challengeWords[wordCount] += "" + stringTiles.get(index).getLetter();
										currentWordScore += (stringTiles.get(index).getValue() * letterMultiplier);
									} else {
										challengeWords[wordCount] += "" + squares[y][x].getTile().getLetter();
										currentWordScore += squares[y][x].getTile().getValue();
									}
									x++;
								}
							}
							score += (currentWordScore * wordMultiplier);
							currentWordScore = 0;
							wordMultiplier = 1;
							letterMultiplier = 1;
							x = gridx;
							y++;
							wordCount++;
							end = false;
						} else {
							y++;
						}

						if (index == stringTiles.size() - 1) {
							index = -1; // resets loop
							last = true;
							y = gridy;
							y = rewind('d', x, y);
						}
					} else {
						y++; // moves across the taken letter
					}
					if (index == stringTiles.size() - 1) {
						index = -1; // resets loop
						last = true;
						y = gridy;
						y = rewind('d', x, y);
					}
				} else {
					while (y < gridy) // end = false before it gets to last so this only runs once aka until the
										// placed letters
					{
						challengeWords[wordCount] += "" + squares[y][x].getTile().getLetter();
						currentWordScore += squares[y][x].getTile().getValue();
						y++;
					}

					if (squares[y][x].isEmpty()) {
						multiplier(squares[y][x].getValue());
					}

					challengeWords[wordCount] += "" + stringTiles.get(index).getLetter();
					currentWordScore += (stringTiles.get(index).getValue() * letterMultiplier);
					letterMultiplier = 1;
					y++;

					if (index == stringTiles.size() - 1) {
						end = false;
						while (y < 15 && !end) {
							if (squares[y][x].isEmpty()) {
								end = true;
							} else {
								challengeWords[wordCount] += "" + squares[y][x].getTile().getLetter();
								currentWordScore += squares[y][x].getTile().getValue();
								y++;
							}
						}
						currentWordScore *= wordMultiplier;
						score += currentWordScore;
					}
				}
			}
		}

		if (lettersToRemove.length() == 7) {
			score += 50;
		}

		//////////////////////////////////////////////////////
		return score;
	}

	void multiplier(String m) {
		switch (m) {
		case "3W":
			wordMultiplier *= 3;
			break;
		case "3L":
			letterMultiplier = 3;
			break;
		case "*" :
		case "2W":
			wordMultiplier *= 2;
			break;
		case "2L":
			letterMultiplier = 2; // need to change for main word
			break;
		default:
			break;

		}
	}

	int rewind(char direction, int x, int y) {
		boolean done = false;
		if (direction == 'd') {
			while (y > 0 && !done) {
				if (!squares[y - 1][x].isEmpty()) {
					y--;
				} else {
					done = true;
				}
			}

			return y;
		} else {
			while (x > 0 && !done) {
				if (!squares[y][x - 1].isEmpty()) {
					x--;
				} else {
					done = true;
				}
			}

			return x;
		}
	}

	int letterGridToIndex(char letterGridRef) {
		int xGridVal = 0;
		String alphabet = "ABCDEFGHIJKLMNO";
		for (int i = 0; i < 15; i++) {
			if (alphabet.charAt(i) == letterGridRef) {
				xGridVal = i;
				break;
			}
		}
		return xGridVal;
	}

	void resetChallenge()
	{
		for(int i = 0;i < 8; i++)
		{
			challengeWords[i] = "";
		}
	}


	void removeLast()
	{
		int i = 0;
		if(prevDirection.equals("across"))
		{
			while((prevFirstLetterX + i) < 15 && i < prevWordLength)
			{
				if(prevTaken[i] == 0) {
					squares[prevFirstLetterY][prevFirstLetterX + i].setTile(null);
				}
				i++;
			}
		}else
		{
			while((prevFirstLetterY + i) < 15 && i < prevWordLength)
			{
				if(prevTaken[i] == 0) {
					squares[prevFirstLetterY+i][prevFirstLetterX].setTile(null);
				}
				i++;
			}
		}
	}

}