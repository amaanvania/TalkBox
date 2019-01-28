package application;

import java.io.File;
import java.io.IOException;

import config.FileWrite;
import config.Utilities;
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
		TalkBoxApp a = new TalkBoxApp(new File(Utilities.fileChoose(primaryStage)));
		GridPane b = a.gridpane;
		primaryStage.setTitle("TalkBox Application");
		primaryStage.setScene(new Scene(b));
		primaryStage.show();
		
		
	}

}
