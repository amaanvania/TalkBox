package config;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Utilities {

	public static String text;
	public static String AudioPath;
	public static String ImagePath;

	public static String fileChoose(Stage mainStage) {
		FileChooser fileChooser = new FileChooser();
		File selectedFile = fileChooser.showOpenDialog(mainStage);
		if (selectedFile != null) {
			return selectedFile.getAbsolutePath();
		}
		return null;
	}

	public static GridPane setEditPrompt(Stage primaryStage) {
		GridPane gridpane = new GridPane();
		gridpane.setVgap(10);
		gridpane.setHgap(10);
		primaryStage.setTitle("Configure Button");
		//Button submit = new Button("Submit");
		Button audio = new Button("Choose AudioFile");
		Button image = new Button("Choose Image");
		Label label1 = new Label("Set Title:");
		
		TextField audioField = new TextField();
		TextField imageField = new TextField();
		GridPane.setConstraints(label1, 0, 0);
		GridPane.setConstraints(audio, 0, 1);
		GridPane.setConstraints(audioField, 1, 1);
		GridPane.setConstraints(image, 0, 2);
		GridPane.setConstraints(imageField, 1, 2);
		//gridpane.setConstraints(submit, 4, 4);
		gridpane.getChildren().addAll(label1, audio, image,imageField, audioField);
		/*submit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if ((textField.getText() != null && !textField.getText().isEmpty())) {
					text = textField.getText();
					primaryStage.close();
				}
			}
		});*/
		image.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				AudioButton b = new AudioButton();
				b.chooseImagePath();
				imageField.setText(b.getImagePath());
				ImagePath = b.getImagePath();
			}
		});
		audio.setOnAction(new EventHandler<ActionEvent>() {
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

	public static String getText() {
		return text;
	}
}
