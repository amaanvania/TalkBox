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
		//TalkBoxApp a = new TalkBoxApp(new File("C:\\Users\\EliteBook 8540w\\Desktop\\configFile.txt"));
		//System.out.println(a.getNames()[0]);
		

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		TalkBoxApp a = new TalkBoxApp(new File("C:\\Users\\EliteBook 8540w\\Desktop\\configFile.txt"));
		GridPane b = a.gridpane;
		primaryStage.setScene(new Scene(b));
		primaryStage.show();
		
		
	}

}
