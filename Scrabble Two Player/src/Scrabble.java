import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Team Name: El Cucharachas
 * 
 * Students: - Ahmed Jouda 18329393 - Sean Mcdonnell 18391961 - Lleno Anya
 * 18357493
 *
 */
public class Scrabble extends Application {
	Stage window;
	Scene game;
	UI ui;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		ui = new UI();
		window = primaryStage;
		game = new Scene(ui.printGame(), UI.screenBounds.getWidth() / 1.9, UI.screenBounds.getHeight() / 1.2);
		game.getStylesheets().clear();
		game.getStylesheets().add("style.css");
		window.setScene(game);
		window.setTitle("Scrabble");
		window.setResizable(false);
		window.show();
	}

}