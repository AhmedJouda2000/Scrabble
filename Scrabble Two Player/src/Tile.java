/**
 * Team Name: El Cucharachas
 * 
 * Students:
 * - Ahmed Jouda 	18329393
 * - Sean Mcdonnell 18391961
 * - Lleno Anya 	18357493
 *
 */
public class Tile{

    private static final int BLANK_VALUE = 0;
    private static final int[] TILE_VALUE = {1,3,3,2,1,4,2,4,1,8,5,1,3,1,1,3,10,1,1,1,1,4,4,8,4,10};

    private boolean blank;
    private char letter;
    private int value;

    // Tile precondition: must be uppercase letter
    Tile(char letter) {
        if (letter == '_') {
            this.blank = true;
            this.value = BLANK_VALUE;

        } else {
            this.blank = false;
            this.letter = letter;
            this.value = TILE_VALUE[(int) letter - (int) 'A'];
        }
    }

    public boolean isBlank() {
        return blank;
    }

    // getLetter precondition isBlank() = false;
    public char getLetter() {
        return letter;
    }

    // getValue precondition isBlank() = false;
    public int getValue() {
        return value;
    }

    // equals is used by the contains method to find matching objects in an ArrayList
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Tile)) {
            return false;
        } else {
            return this.letter == ((Tile) object).letter;
        }
    }

    @Override
    public String toString() {
        if (blank) {
            return "_";
        } else {
            return Character.toString(letter);
        }
    }

    public void setLetter(char letter)      //used for putting symbolic letters on blank tiles
    {
        this.letter = letter;
    }

}
