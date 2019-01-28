package config;

import java.io.File;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Utilities {
	
	public static String fileChoose(Stage mainStage) {
		 FileChooser fileChooser = new FileChooser();
		 File selectedFile = fileChooser.showOpenDialog(mainStage);
		 if (selectedFile != null) {
		    return selectedFile.getAbsolutePath();
		 }
		 return null;
	}
}
