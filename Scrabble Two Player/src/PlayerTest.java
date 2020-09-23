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
public class PlayerTest {

	// For full marks, you should include automated comparison between actual and expected results

	public static void main (String[] args) {

		// Test draw random tiles
		Pool pool = new Pool();
		System.out.println(pool.size());
		System.out.println("Expected: 100\n");

		ArrayList<Tile> draw = pool.drawTiles(1);
		System.out.printf("%s\n",draw);
		System.out.println(pool.size());
		System.out.println("Expected: 99\n");

		draw = pool.drawTiles(7);
		System.out.printf("%s\n",draw);
		System.out.println(pool.size());
		System.out.println("Expected: 92\n");


		// Test pool empty
		pool = new Pool();
		boolean empty = pool.isEmpty();
		System.out.printf("Pool empty? %b\n",empty);
		System.out.printf("Expected: false\n");
		for (int i=0; i<99; i++) {
			draw = pool.drawTiles(1);
			System.out.print(draw);
		}
		System.out.println();
		System.out.println(pool.size());
		empty = pool.isEmpty();
		System.out.printf("Pool empty? %b\n",empty);
		System.out.printf("Expected: false\n");
		draw = pool.drawTiles(1);
		System.out.println(draw);
		System.out.printf("Expected: 1 letter\n");
		System.out.println(pool.size());
		System.out.printf("Expected: 0\n");
		empty = pool.isEmpty();
		System.out.printf("Pool empty? %b\n",empty);
		System.out.printf("Expected: true\n");
		draw = pool.drawTiles(7);
		System.out.printf("%s\n",draw);
		System.out.printf("Expected: empty\n");
				
		// Test player names
		Player player1 = new Player(), player2 = new Player();
		player1.setName("Chris");
		player2.setName("Jack");
		System.out.println(player1.getName());
		System.out.printf("Expected: Chris\n");
		System.out.println(player2.getName());
		System.out.printf("Expected: Jack\n");
		
		// Test scoring
		System.out.println(player1.getScore());
		System.out.printf("Expected: 0\n");
		player1.addScore(10);
		System.out.println(player1.getScore());
		System.out.printf("Expected: 10\n");
		
		// Test frames
		pool = new Pool();
		Frame frame = player1.getFrame();
		empty = frame.isEmpty();
		System.out.printf("Is empty? %b \n",empty);
		System.out.printf("Expected: true\n");
		System.out.println(frame);
		System.out.printf("Expected: empty\n");
		boolean available = !frame.isAvailable("A").contains("f");
		System.out.println(available);
		System.out.printf("Expected: false\n");
		frame.refill(pool);          
		System.out.println(frame);
		System.out.printf("Expected: 7 letters\n");
		empty = frame.isEmpty();                          
		System.out.printf("Is empty? %b\n",empty);
		System.out.printf("Expected: false\n");
		System.out.println(pool.size());
		System.out.printf("Expected: 93\n");
		draw = frame.getTiles();
		String drawString = "";
		for (int i=0; i<draw.size(); i++) {
			drawString = drawString + draw.get(i).toString();
		}
		System.out.println(draw);
		available = !frame.isAvailable(drawString.substring(0,6)).contains("f");
		System.out.println(available);
		System.out.printf("Expected: true\n");
		available = !frame.isAvailable("X").contains("f");
		System.out.println(available);
		System.out.printf("Expected: false\n");
		frame.remove(drawString.substring(0,3));
		System.out.println(frame);
		System.out.printf("Expected: 4 left\n");
		frame = player2.getFrame();
		frame.refill(pool);
		System.out.println(pool.size());
		System.out.printf("Expected: 86\n");
		System.out.println(frame);
		System.out.printf("Expected: 7 left\n");
		frame = player1.getFrame();
		System.out.println(frame);
		System.out.printf("Expected: 4 left\n");
	}
}
