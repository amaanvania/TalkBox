package log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import config.Utilities;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TBCLog extends Application{
	
	Stage primaryStage; // The main stage that runs the TBCLogger application
	File logFile;
	ArrayList<String> listOfLines;
	ArrayList<Label> listOfLabels;
	VBox logPane;
	
	@FXML Label label;
	@FXML Button openlog;
	@FXML Button showstats;
	@FXML ScrollPane scrollpane;
	

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
	
	// Initialize the logFile field with the logger file the user selected when the user clicks on the 'Open Log button'
	@FXML
	public void selectLoggerFile() {
		logFile = Utilities.chooseLoggerFile(primaryStage);
		if (logFile != null) {
			removeLabel();
			listOfLines = new ArrayList<String>();
			try {
				readFile(logFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			createListView();
		}
	}

	// Remove the center placeholder text
	private void removeLabel() {
		scrollpane.getChildrenUnmodifiable().remove(label);
	}
	
	// Read the log file and store it all in an ArrayList
	private void readFile(File file) throws IOException {
		try {
			FileReader fr = new FileReader(file);
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(fr);
			String currentLine;
			listOfLines.add("Date               Time          Action");
			while ((currentLine = br.readLine()) != null) {
				String temp = currentLine.replaceFirst("T", "    ");
				temp = temp.replaceFirst("\\.\\d\\d\\d", "   ");
				listOfLines.add(temp);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	// Create and then populate the list view with the log lines
	private void createListView() {
		ObservableList<String> ll = FXCollections.<String>observableArrayList(listOfLines);
		ListView<String> lv = new ListView<String>(ll);
		lv.setOrientation(Orientation.VERTICAL);
		lv.setPrefSize(874, 540);
		scrollpane.setContent(lv);
	}
	
	// Show the statistic window
	@FXML
	public void showStats() {
		Stage st = new Stage();
		Scene sc = new Scene(Stat.getStatWindow());
		st.setScene(sc);
		st.show();
	}


}
