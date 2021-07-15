/**
 * Filename:   Main.java
 * Project:    Milestone 3
 * Authors:    A-team 31
 *
 * Semester:   Fall 2018
 * Course:     CS400
 * 
 * Due Date:   12/12/18 10pm
 * Version:    1.0
 * 
 * Credits:    none
 * 
 * Bugs:       no known bugs
 */

package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


/**
 * Main class that is run when program begins. 
 * 
 * @author A-team 31
 */
public class Main extends Application {
	int count = 1;
	/**
	 * Starts the program and calls the PrimaryGUI to display all things created in 
	 * that class.
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			// Create the root, scene and primary GUI and display that with a title
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			PrimaryGUI pGUI = new PrimaryGUI(root, scene, primaryStage);
			
	        primaryStage.setScene(scene);
	        primaryStage.show();
			primaryStage.setTitle("Food Query and Meal Analysis");
			primaryStage.setMaximized(true);
			// Display created objects within PrimaryGUI
			pGUI.display();			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 
	 * @param args, arguments passed into the program
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
