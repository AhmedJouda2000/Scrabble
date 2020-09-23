import java.util.ArrayList;

/**
 * Team Name: El Cucharachas
 * 
 * Students:
 * - Ahmed Jouda 	18329393
 * - Sean Mcdonnell 18391961
 * - Lleno Anya 	18357493
 *
 */
public class Frame {

	private static final int MAX_TILES = 7;
	private ArrayList<Tile> tiles;
	private ArrayList<Tile> previouslyDrawn;
	private ArrayList<Tile> placed;

	Frame() {
		tiles = new ArrayList<>();
		placed = new ArrayList<>();
		previouslyDrawn = new ArrayList<>();
	}

	public int size() {
		return(tiles.size());
	}
	
	public boolean isEmpty() {
		return tiles.isEmpty();
	}
	
	public boolean isFull() {
		return tiles.size() == MAX_TILES;
	}
	
	public String isAvailable(String letters) {
		boolean found = true;
		boolean blank = false;
		StringBuilder val = new StringBuilder();	//1 = true pass; > 1 = blank true; 0 = false
		if (letters.length() > tiles.size()) {
			found = false;
		}
		else {
			ArrayList<Tile> copyTiles = new ArrayList<>(tiles);
			for (int i=0; (i<letters.length()) && found; i++) {
				Tile tileSought = new Tile(letters.charAt(i));
				if (copyTiles.contains(tileSought)) {
					copyTiles.remove(tileSought);
				} else {
					Tile tileSoughtBlank = new Tile('_');
					if(copyTiles.contains(tileSoughtBlank))
					{
						val.append(i);
						copyTiles.remove(tileSoughtBlank);
						blank = true;
					}
					else
					{
						found = false;
					}
				}
			}
		}
		if(found && !blank)
		{
			val.append("t");
		}else if(!found)
		{
			val.append("f");
		}

		return val.toString();
	}
	
	public ArrayList<Tile> getTiles() {
		return tiles;
	}

	// remove precondition: isAvailable(letters) is true
	public void remove(String letters) {
		placed.clear();
		for (int i=0; (i<letters.length()); i++) {
			tiles.remove(new Tile(letters.charAt(i)));
			placed.add(new Tile(letters.charAt(i)));
		}
	}

	public void refill(Pool pool) {
		int numTilesToDraw = MAX_TILES - tiles.size();
		previouslyDrawn = pool.drawTiles(numTilesToDraw);
		tiles.addAll(previouslyDrawn);
	}

	public void revert(Pool pool){
		for(Tile tile : previouslyDrawn) {
			tiles.remove(tile);
		}
		tiles.addAll(placed);
		placed.clear();
		pool.returnTiles(previouslyDrawn);
	}
	
	
	//Method used for testing to avoid randomization
	public void refillForTest() {
		ArrayList<Tile> draw = new ArrayList<>();
		Tile tempTile = new Tile('_');
		draw.add(tempTile);
		tempTile = new Tile('C');
		draw.add(tempTile);
		tempTile = new Tile('A');
		draw.add(tempTile);
		tempTile = new Tile('_');
		draw.add(tempTile);
		tempTile = new Tile('I');
		draw.add(tempTile);
		tempTile = new Tile('N');
		draw.add(tempTile);
		tempTile = new Tile('L');
		draw.add(tempTile);
		tiles.addAll(draw);
	}

	@Override
	public String toString() {
		return tiles.toString();
	}
	
}
