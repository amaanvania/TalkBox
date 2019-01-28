package application;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TalkBoxBuilder extends Application{

	public static void main(String[] args) throws IOException {
		launch(args);
		

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		TalkBoxApp a = new TalkBoxApp(new File("src/resources/configFile.txt"));
		GridPane b = a.getGridpane();
		primaryStage.setTitle("TalkBox Application");
		primaryStage.setScene(new Scene(b));
		primaryStage.show();
		
		
	}

}
