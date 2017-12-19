/**
 * Suphawich Sungkhavorn
 * 5810451110
 * 200
 */

package controllers;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

public class MainController {
	
	/**
	 * Make root window.
	 * @param primaryStage from extends Application
	 * 
	 * 2017-10-08 03:32 PM
	 */
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Root.fxml"));
	        Parent root = loader.load();
	        Scene scene = new Scene(root);
			primaryStage.setTitle("Calendar");
			scene.getStylesheets().add(getClass().getResource("/views/style.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			System.err.println("เกิดข้อผิดพลาด หน้าแสดงหลัก");
//			e.printStackTrace();
		}
	}
}
