package main.java.application;

import java.io.IOException;

import main.java.config.Builder;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TalkBoxBuilder extends Application{ //simple class that "runs" talk box app

	public static void main(String[] args) throws IOException {
		launch(args);
		

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		TalkBoxApp a = new TalkBoxApp(new Builder());
		GridPane b = a.getGridpane();
		primaryStage.setTitle("TalkBox Application");
		primaryStage.setScene(new Scene(b));
		primaryStage.show();
		
		
	}

}
