package config;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Utilities { //class for some helpful utilities with static methods

	public static String text;
	public static String AudioPath;
	public static String ImagePath;
	public static String defaultPath;

	public static String fileChoose(Stage mainStage) { //method to prompt filechooser
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("src/resources")); //initial dir is src/resources
		File selectedFile = fileChooser.showOpenDialog(mainStage);
		if (selectedFile != null) {
			return selectedFile.getAbsolutePath();
		}
		return null;
	}
	
	public static File configFileChoose(Stage mainStage) { //method to prompt filechooser
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = (new ExtensionFilter("TalkBox File","*.talk")); //sets .talk extension
		fileChooser.getExtensionFilters().add(extFilter);
		if(defaultPath == null)
			fileChooser.setInitialDirectory(new File("src/configFiles/")); //initial dir is src/configFiles
		else fileChooser.setInitialDirectory(new File(defaultPath));
		File selectedFile = fileChooser.showOpenDialog(mainStage);
		if (selectedFile != null) {
			defaultPath = selectedFile.getParent();
			return selectedFile;
		}
		return null;
	}
	
	public static File configFileSave(Stage mainStage) { //method to prompt filechooser
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = (new ExtensionFilter("TalkBox File","*.talk"));
		fileChooser.getExtensionFilters().add(extFilter);
		File selectedFile = fileChooser.showSaveDialog(mainStage);
		if (selectedFile != null) {
			return selectedFile;
		}
		return null;
	}

	public static GridPane setEditPrompt(Stage primaryStage) { //method which builds edit prompt
		GridPane gridpane = new GridPane();
		gridpane.setVgap(10);
		gridpane.setHgap(10);
		primaryStage.setTitle("Configure Button");
		//Button submit = new Button("Submit");
		Button audio = new Button("Choose AudioFile");
		Button image = new Button("Choose Image");
		Label label1 = new Label("Set Title:"); //prompt for name of button

		TextField audioField = new TextField(AudioPath);
		TextField imageField = new TextField(ImagePath);
		
		// Set GridPaneConstraints
		setGridPaneConstraints(audio, image, label1, audioField, imageField);
		gridpane.getChildren().addAll(label1, audio, image,imageField, audioField);  //add all elements to gridpane and set their positions
		image.setOnAction(new EventHandler<ActionEvent>() { //event handler which opens up filechooser
			@Override
			public void handle(ActionEvent e) {
				AudioButton b = new AudioButton();
				b.chooseImagePath();
				imageField.setText(b.getImagePath());
				ImagePath = b.getImagePath();
			}
		});
		audio.setOnAction(new EventHandler<ActionEvent>() { //event handler which opens up filechooser
			@Override
			public void handle(ActionEvent e) {
				AudioButton b = new AudioButton();
				b.chooseAudioPath();
				audioField.setText(b.getAudioPath());
				AudioPath = b.getAudioPath();
			}
		});
		return gridpane;

	}

	private static void setGridPaneConstraints(Button audio, Button image, Label label1, TextField audioField,
			TextField imageField) {
		GridPane.setConstraints(label1, 0, 0);
		GridPane.setConstraints(audio, 0, 1);
		GridPane.setConstraints(audioField, 1, 1);
		GridPane.setConstraints(image, 0, 2);
		GridPane.setConstraints(imageField, 1, 2);
	}

	public static String getText() {
		return text;
	}
}
