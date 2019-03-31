package log;

import javax.swing.text.StyledEditorKit.BoldAction;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Stat {
	// Statistics for the TalkBox Configuration Application
	public static int TestCounter = 0;
	public static int SaveCounter = 0;
	public static int RecordAudioCounter = 0;
	public static int AddNewCounter = 0;
	public static int DeleteLastCounter = 0;
	public static int DeleteSpecificCounter = 0;
	
	public static int AutofillAudioButtonCounter = 0;
	public static int DragDropAudioButtonCounter = 0;
	public static int TitlesSet = 0;
	public static int ImagesSelected = 0;
	public static int AudioTracksSelected = 0;
	public static int AudioButtonsTested = 0;
	public static int AudioButtonsTestedStop = 0;
	public static int AudioButtonsEdited = 0;

	
	// Statistics for the TalkBox Simulator Application
	public static int AudioButtonsPlayed = 0;
	public static int AudioButtonsStopped= 0; 
	
	
	// General Statistics
	public static int VolumeCounter = 0;
	public static int UserManualCounter = 0;
	public static int ReportIssueCounter = 0;
	public static int ContactUsCounter = 0;
	
	public static VBox getStatWindow() {
		VBox statBox = new VBox();
		Label title = new Label("Your Stats this session");
		title.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
		statBox.getChildren().add(title);
		addStat(statBox, "No. of times you opened the current configuration", TestCounter);
		addStat(statBox, "No. of times you tried to save the state of the config file", SaveCounter);
		addStat(statBox, "No. of times you tried to Record your own audio", RecordAudioCounter);
		addStat(statBox, "No. of new Buttons Added using the Add New button", AddNewCounter);
		addStat(statBox, "No. of times you deleted the audio button at last", DeleteLastCounter);
		addStat(statBox, "No. of times you deleted an audio at a specific index", DeleteSpecificCounter);
		addStat(statBox, "No. of times you used the autofill feature", AutofillAudioButtonCounter);
		addStat(statBox, "No. of times you used drag and drop", DragDropAudioButtonCounter);
		addStat(statBox, "No. of audio buttons title set", TitlesSet);
		addStat(statBox, "No. of images added to audio buttons", ImagesSelected);
		addStat(statBox, "No. of audio tracks added to audio buttons", AudioTracksSelected);
		addStat(statBox, "No. of times you played the audio buttons", AudioButtonsTested);
		addStat(statBox, "No. of times you stopped playing audio buttons midway", AudioButtonsStopped);
		addStat(statBox, "No. of times you edited audio buttons", AudioButtonsEdited);
		addStat(statBox, "No. of times you played audio buttons in the simulator", AudioButtonsPlayed);
		addStat(statBox, "No. of times you stopped playing audio buttons midway in the simulator", AudioButtonsStopped);
		addStat(statBox, "No. of times you used the volume counter", VolumeCounter);
		addStat(statBox, "No. of times you tried consulting the user manual for help", UserManualCounter);
		addStat(statBox, "No. of times you tried to issue a bug report", ReportIssueCounter);
		addStat(statBox, "No. of times you tried to open up the contact page", ContactUsCounter);
		
		return statBox;
	}
	
	private static void addStat(VBox vbox, String field, int stat) {
		Label l = new Label(field + ": " + stat);
		l.setFont(Font.font("Verdana", FontWeight.LIGHT, 14));
		vbox.getChildren().add(l);	
	}
	

}
