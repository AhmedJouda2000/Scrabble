/**
 * Team Name: El Cucharachas
 * 
 * Students:
 * - Ahmed Jouda 	18329393
 * - Sean Mcdonnell 18391961
 * - Lleno Anya 	18357493
 *
 */
public class Player {

	private String name;
	private int score;


	Player () {
		name = "";
		score = 0;
		frame = new Frame();
	}


	private Frame frame;

	public void setName (String text) {
		name = text;
	}
	
	public String getName () {
		return(name);
	}
	
	public void addScore (int increment) {
		score = score + increment;
	}

	public int getScore() {
		return(score);
	}
	
	public Frame getFrame() {
		return(frame);
	}
	
}
