package application;

import java.io.IOException;

import config.Builder;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TalkBoxBuilder extends Application{ //simple class that "runs" talk box app

	public static void main(String[] args) throws IOException {
		launch(args);
		

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		TalkBoxApp a = new TalkBoxApp(new Builder());
		BorderPane b = a.getPane();
		primaryStage.setTitle("TalkBox Application");
		primaryStage.setScene(new Scene(b, 900, 650));
		primaryStage.show();
		
		
	}

}
