/**
 * Suphawich Sungkhavorn
 * 5810451110
 * 200
 */

import controllers.MainController;
import javafx.application.*;
import javafx.stage.*;

public class StartApp extends Application {
	
	/**
	 * 
	 * START PROGRAM HERE.
	 * 2017-10-08 03:32 PM
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Create MainController for make root frame.
	 * 
	 * 2017-10-08 03:32 PM
	 */
	@Override
	public void start(Stage primaryStage) {
		MainController mc = new MainController();
		mc.start(primaryStage);
	}
}
