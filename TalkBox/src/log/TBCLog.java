package log;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TBCLog extends Application{
	
	Stage primaryStage; // The main stage that runs the TBCLogger application

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		Parent root = FXMLLoader.load(getClass().getResource("/log/GUI.fxml"));
		Scene scene = new Scene(root, 900, 650);
		scene.getStylesheets().add(getClass().getResource("GUI.css").toExternalForm());
		this.primaryStage.setTitle("TalkBox Logger");
		this.primaryStage.setScene(scene);
		this.primaryStage.show();
	}

}
