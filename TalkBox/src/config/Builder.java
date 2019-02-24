package config;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

import javax.imageio.ImageIO;

import application.TalkBoxApp;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Builder extends Application implements TalkBoxConfiguration {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	// class which builds the gui for config application

	/**
	 * 
	 */
	public int numTotalButtons;
	public int numSetButtons;
	public AudioButton[] buttons;
	public String filename;
	public transient int inc;
	public double volume;
	File file;

	public Builder() {
		this.numTotalButtons = 0;
		this.numSetButtons = 0;
		this.buttons = null;
		this.filename = "";
		this.inc = 0;
	}

	public static void main(String[] args) {
		launch(args);
	}

	/*
	 * Method which builds the TalkBox image
	 */
	public ImageView buildImage() throws FileNotFoundException {
		String s = getClass().getResource("/resources/talkbox.jpg").toExternalForm();
		Image img = new Image(s);
		ImageView iv1 = new ImageView();
		iv1.setImage(img);
		iv1.setFitWidth(300);
		iv1.setPreserveRatio(true);
		iv1.setSmooth(true);
		iv1.setCache(true);
		return iv1;
	}

	/*
	 * Method which builds the "choose Existing" button on welcome screen
	 */
	public Button buildChooseExisting(Stage primaryStage) {
		Button button = new Button("Choose Existing");
		button.setTooltip(new Tooltip("Click to choose existing Project"));
		button.setOnAction(e -> {
			try {
				openSerializedFile(primaryStage);
			} catch (IOException e1) {
			}
		});

		return button;

	}

	/*
	 * Method which builds the "new Project" button on welcome screen
	 */
	public Button buildNewProject(Stage primaryStage) {
		Button button = new Button("New Project");
		button.setTooltip(new Tooltip("Click to Start New Project"));
		button.setOnAction(e -> {
			try {
				newButtonPrompt(primaryStage);
			} catch (FileNotFoundException e1) {
			}
		});
		return button;
	}

	/*
	 * Method which builds the initial "Welcome Screen" Gui for talkBox config
	 * app
	 */
	public FlowPane buildWelcomeScreen(Stage primaryStage) throws FileNotFoundException {
		FlowPane f = new FlowPane(Orientation.VERTICAL);
		f.setColumnHalignment(HPos.CENTER);
		f.setPrefWrapLength(200);
		f.setHgap(30);
		f.setVgap(30);
		f.getChildren().addAll(buildImage(), buildNewProject(primaryStage), buildChooseExisting(primaryStage));
		Scene scene = new Scene(f, 300, 400);
		scene.getStylesheets().add(this.getClass().getResource("/resources/buttonstyle.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setTitle("Welcome to TalkBox Config");
		primaryStage.show();
		return f;

	}

	/*
	 * Method which builds the frame when "new Project" button is clicked
	 */
	public FlowPane buildFlowPane(Stage primaryStage) throws FileNotFoundException {
		HBox hb = new HBox();
		FlowPane s = buildSlider();
		Button submit = new Button("Submit");
		submit.setTooltip(new Tooltip("Click Submit to Start Configuration"));
		submit.setOnAction(e -> {
			try {
				if(numTotalButtons < 1){
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning Dialog");
					alert.setHeaderText("Warning! Zero Buttons");
					alert.setContentText("Set more than 0 buttons");
					alert.showAndWait();
				}else buildInitialGui(primaryStage);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		HBox hb2 = new HBox();
		hb2.getChildren().addAll(s);
		hb2.setAlignment(Pos.CENTER);
		hb.getChildren().addAll(submit);
		hb.setAlignment(Pos.BOTTOM_CENTER);
		hb.setSpacing(10);
		FlowPane g = new FlowPane(Orientation.VERTICAL);
		g.setColumnHalignment(HPos.CENTER);
		g.setPrefWrapLength(200);
		g.setVgap(20);
		g.setHgap(20);
		g.setAlignment(Pos.BOTTOM_CENTER);
		g.getChildren().addAll(buildImage(), hb2, hb);
		return g;
	}

	/*
	 * Method which builds GUI when "new Project" button is chosen
	 */
	public void newButtonPrompt(Stage primaryStage) throws FileNotFoundException { // GUI
																					// for
																					// initial
																					// prompt
		FlowPane hb = buildFlowPane(primaryStage);
		hb.setVgap(20);
		primaryStage.setTitle("TalkBox Config App");
		Scene scene = new Scene(hb, 320, 450);
		String css = this.getClass().getResource("/resources/buttonstyle.css").toExternalForm();
		scene.getStylesheets().add(css);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/*
	 * Method which builds slider to define number of Buttons
	 */
	public FlowPane buildSlider() {
		numTotalButtons = 1;
		final Label instances = new Label("Number of Buttons: 1");
		final Slider slider = new Slider(0, 18, 1);
		slider.setPrefWidth(250);
		slider.setShowTickMarks(true);
		slider.setShowTickLabels(true);
		slider.setMajorTickUnit(6);
		slider.setBlockIncrement(1);
		slider.setTooltip(new Tooltip("Drag Slider to set Number of Buttons"));
		slider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				numTotalButtons = newValue.intValue();
				instances.textProperty().setValue("Number of Buttons: " + newValue.intValue());
			}
		});

		final VBox box = new VBox(20, instances, slider);
		box.setAlignment(Pos.CENTER);
		final FlowPane layout = new FlowPane(20, 20, box);
		layout.setPadding(new Insets(20));
		layout.setAlignment(Pos.CENTER);

		return layout;

	}
	
	public MenuBar buildTopMenu() {
		Menu helps = new Menu("Help"); //help dropdown
		MenuItem help = new MenuItem("User manual");
		help.setId("help-config");
		help.setOnAction(e -> {
		try {
		Desktop.getDesktop().browse(new URI(
				"https://github.com/amaanvania/TalkBox/blob/master/Documentation/Talk%20Box%20User%20Manual.pdf"));
		} catch (IOException e1) {
		e1.printStackTrace();
		} catch (URISyntaxException e1) {
		e1.printStackTrace();
		}
		});
		MenuItem contact = new MenuItem("Contact us");
		contact.setId("contact-wiki");
		contact.setOnAction( e-> {
			try {
				Desktop.getDesktop().browse(new URI("https://github.com/amaanvania/TalkBox/wiki"));
			} catch (IOException | URISyntaxException e1) {
				e1.printStackTrace();
			}
		});
		helps.getItems().addAll(help, contact);
		
		Menu volumes = new Menu("Volume"); //volume drop down
		CustomMenuItem volumesOne = new CustomMenuItem();
		final Slider vSlider = new Slider(0, 100, 100); // volume slider
		volume = 100; //initial value for volume
		vSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				volume = newValue.doubleValue() / 100.00;
			}
		});
		volumesOne.setContent(vSlider);
		volumes.getItems().addAll(volumesOne);
		
		MenuBar menuBar = new MenuBar();
		menuBar.setId("Top-menu");
		menuBar.getMenus().addAll(volumes, helps);
		return menuBar;
	}

	public void recordHandle() throws FileNotFoundException{
		AudioRecord a = new AudioRecord();
		Stage x = new Stage();
		HBox record = a.buildButtons();
		VBox v = buildRecordFrame();
		v.getChildren().add(record);
		record.setAlignment(Pos.BOTTOM_LEFT);
		Scene scene = new Scene(v);
		String css = this.getClass().getResource("/resources/buttonstyle.css").toExternalForm();
		scene.getStylesheets().add(css);
		x.setScene(scene);
		x.show();
	}
	
	public VBox buildRecordFrame(){
		VBox v = new VBox();
		Text line1 = new Text("\n Instructions on Recording Audio \n");
		Text line2 = new Text("\n 1. Click Record Button \n");
		Text line3 = new Text("\n 2. Set path of audio file \n");
		Text line4 = new Text("\n 3. Now microphone is recording, input sound \n");
		Text line5 = new Text("\n 4. Click stop Button to stop recording \n");
		Text line6 = new Text("\n 5. Click play Button to play recording \n");
		v.getChildren().addAll(line1,line2,line3,line4,line5,line6);
		return v;
	}
	public void alertHandle(){
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning!");
		alert.setHeaderText("Warning! Not Yet Implemented");
		alert.setContentText("Functionality will come in future");
		alert.showAndWait();
	}
	public ToolBar buildBotToolbar() throws IOException { // method
																			// which
																			// builds
																			// and
																			// returns
																			// the
																			// bot
																			// Toolbar
		Button play = playBtn();
		Button save = saveBtn();
		Button addNewButton = new Button("Add Button");
		addNewButton.setOnAction(e -> alertHandle());
		Button deleteButton = new Button("Delete Button");
		deleteButton.setOnAction(e -> alertHandle());
		Button recordAudio = new Button("Record Audio");
		recordAudio.setId("recordAudio-config");
		recordAudio.setTooltip(new Tooltip("Click to Record Audio"));
		recordAudio.setOnAction(e -> {
			try {
				//
				recordHandle();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		});
		ToolBar toolBar = new ToolBar(play, save, recordAudio,addNewButton,deleteButton // add save button to toolbar
		);
		toolBar.setPrefSize(200, 20);
		toolBar.setId("bot-toolbar-config");
		return toolBar;
	}

	/**
	 * @param primaryStage
	 * @return
	 */
	private Button playBtn() {
		Button play = new Button("Test"); // play button
		play.setId("play-config");
		play.setTooltip(new Tooltip("Click to Open this configuration in the TalkBox App"));
		play.setOnMouseClicked(e -> {
			try {
				if (getSetButtons() > 0 && this.file != null) {
					TalkBoxApp a = new TalkBoxApp(openSerializedFile(file));
					BorderPane b = a.getPane();
					Stage primaryStage = new Stage();
					primaryStage.setTitle("TalkBox Application");
					Scene scene = new Scene (b, 900, 650);
					String css = this.getClass().getResource("/resources/buttonstyle.css").toExternalForm();
					scene.getStylesheets().add(css);
					primaryStage.setScene(scene);
					primaryStage.show();
				} else {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning Dialog");
					alert.setHeaderText("Warning! Null File");
					alert.setContentText("Save file before attempting to Play");
					alert.showAndWait();
				}
			} catch (IOException z) {
				z.printStackTrace();
			}
		});
		return play;
	}

	/**
	 * @return returns a save button that has the functionality to save the current state of the application
	 */
	private Button saveBtn() {
		Button save = new Button("Save"); // save button
		save.setId("save-config");
		save.setTooltip(new Tooltip("Click to Save File"));
		save.setOnMouseClicked(e -> {
			if (file == null)
				file = Utilities.configFileSave(new Stage());
			saveSerializedFile(); // append each button to file
		});
		return save;
	}
	public ImageView buildImageView(){
		ImageView iv1 = new ImageView();
		iv1.setId("image-config");
		iv1.setFitWidth(100);
		iv1.setPreserveRatio(true);
		iv1.setSmooth(true);
		iv1.setCache(true);
		return iv1;
	}
	
	public void editHandle(Stage x,Button submit,TextField textField){
		GridPane g = Utilities.setEditPrompt(x);	
		GridPane.setConstraints(submit, 4, 4);
		GridPane.setConstraints(textField, 1, 0);
		g.getChildren().add(submit);
		g.getChildren().add(textField);
		x.setScene(new Scene(g));
		x.show();
	}
	public boolean notValidInput(TextField textField){
		return textField.getText().length() < 1 || Utilities.AudioPath == null || 
				Utilities.AudioPath.length() < 1 || Utilities.ImagePath == null ||
				Utilities.ImagePath.length() < 1;
	}
	public void emptyFieldHandle(){
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning!");
		alert.setHeaderText("Warning! Invalid input");
		alert.setContentText("Make sure input is valid.");
		alert.showAndWait();
	}
	public void imageHandle(String audioPath){
		File f = new File(audioPath);
		URI u = f.toURI();
		Media sound = new Media(u.toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.setVolume(volume);
		mediaPlayer.play();
	}
	public void dragOverHandle(DragEvent e){
		if(e.getDragboard().hasFiles()){
			e.acceptTransferModes(TransferMode.ANY);
		}
	}
	public void dropHandle(DragEvent e,AudioButton b) throws IOException{
		List<File> files = e.getDragboard().getFiles();
		if(isImage(files.get(0).getAbsolutePath())) b.setImagePath(files.get(0).getAbsolutePath());
		if(isAudio(files.get(0).getAbsolutePath())) b.setAudioPath(files.get(0).getAbsolutePath());
	}
	public static boolean isImage(String filepath) throws IOException {
	    File f = new File(filepath);
	    return (ImageIO.read(f) != null);
	}
	public static boolean isAudio(String filepath){
		File f = new File(filepath);
		return f.getAbsolutePath().endsWith(".wav") || f.getAbsolutePath().endsWith(".mp3");
	}
	/*
	 * Method which builds the configuration GUI Allowing user to view and edit
	 * each button
	 * 
	 */
	public void buildInitialGui(Stage primaryStage) throws IOException {
		buttons = (buttons == null) ? new AudioButton[numTotalButtons] : buttons;
		inc = 0;
		int increment = 0;
		GridPane gridpane = new GridPane();
		gridpane.setPrefSize(500, 500);
		gridpane.setVgap(10);
		gridpane.setHgap(10);
		String s = getClass().getResource("/resources/PlusSign.png").toExternalForm();
		Image img = new Image(s);
		for (int i = 0; i < numTotalButtons; i++) {
			int k = i;
			if (i > 0 && i % 6 == 0) increment++; // incrementer to define number of rows
			AudioButton currentButton = (buttons[i] == null) ? new AudioButton() : buttons[i];
			ImageView iv1 = buildImageView();
			iv1.setOnDragOver(e -> dragOverHandle(e));
			if (buttons[i] == null)iv1.setImage(img);
			else iv1.setImage(new Image(new FileInputStream(buttons[i].getImagePath())));
			TextField textField;
			textField = (buttons[i] == null) ? new TextField("") : new TextField(currentButton.getName());
			Button edit = buildButton(i, textField);
			Button submit = new Button("Submit");
			Stage x = new Stage();
			edit.setOnAction(e -> editHandle(x,submit,textField));
			submit.setOnAction(event -> {
				if(notValidInput(textField)) emptyFieldHandle();
				else submitHandle(k, currentButton, iv1, textField, edit, x);
			});
			iv1.setOnDragDropped(e -> {
				try {
					dropHandle(e,currentButton);
					if(isImage(e.getDragboard().getFiles().get(0).getAbsolutePath())){
						iv1.setImage(new Image(new FileInputStream(currentButton.getImagePath())));
						Utilities.ImagePath = currentButton.getImagePath();
					}
					if(isAudio(e.getDragboard().getFiles().get(0).getAbsolutePath())){
						Utilities.AudioPath = currentButton.getAudioPath();
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			iv1.setOnMouseClicked(e -> imageHandle(buttons[k].getAudioPath()));
			GridPane.setConstraints(edit, i % 6, 5 + 2 * increment);
			GridPane.setConstraints(iv1, i % 6, 4 + 2 * increment);
			gridpane.getChildren().addAll(iv1, edit);
		}
		ToolBar botToolBar = buildBotToolbar();
		MenuBar topMenu = buildTopMenu();
		StackPane a = new StackPane();
		a.getChildren().addAll(gridpane, botToolBar, topMenu);
		StackPane.setAlignment(topMenu, Pos.TOP_CENTER);
		StackPane.setAlignment(botToolBar, Pos.BOTTOM_CENTER);
		StackPane.setAlignment(gridpane, Pos.BOTTOM_CENTER);
		Scene scene = new Scene(a, 651, 510);
		String css = this.getClass().getResource("/resources/buttonstyle.css").toExternalForm();
		scene.getStylesheets().add(css);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private Button buildButton(int i, TextField textField) {
		Button edit = (buttons[i] == null) ? new Button("Edit:") : new Button(textField.getText());
		edit.setPrefSize(100, 20);
		edit.setId("edit-config");
		edit.setTooltip(new Tooltip("Click to Edit Button"));
		return edit;
	}

	private void submitHandle(int k, AudioButton currentButton, ImageView iv1, TextField textField, Button edit,
			Stage x) {
		try {
			edit.setText(textField.getText());
			Image n = new Image(new FileInputStream(Utilities.ImagePath)); // sets
			iv1.setImage(n);
			Tooltip.install(iv1, new Tooltip("Click to Play Sound"));
			currentButton.setName(textField.getText());
			currentButton.setImagePath(Utilities.ImagePath);
			currentButton.setAudioPath(Utilities.AudioPath);
			buttons[k] = currentButton;
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		x.close();
	}

	/*
	 * Method which returns number of non-null buttons
	 */
	public int getSetButtons() {
		int i = 0;
		for (AudioButton b : buttons) {
			if (b != null) {
				if (b.getName().length() > 0 && b.getAudioPath().length() > 0 && b.getImagePath().length() > 0) {
					i++;
				}
			}
		}
		numSetButtons = i;
		return numSetButtons;
	}

	/*
	 * Method to open "Existing" serialized File Launches a fileChooser and
	 * stores the file sets fields from "Saved" file to "This" builder
	 */
	public void openSerializedFile(Stage primaryStage) throws IOException {
		Builder e = null;
		File f;
		try {
			f = Utilities.configFileChoose(new Stage());
			FileInputStream fileIn = new FileInputStream(f);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			e = (Builder) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException i) {
			i.printStackTrace();
			return;
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
			return;
		}
		AudioButton[] newButtons = new AudioButton[e.numTotalButtons];
		int k = 0;
		for(AudioButton a : e.buttons){
			if(a != null && ((a.name.length() > 0) || (a.AudioPath.length() > 0) || a.ImagePath.length() > 0)){
				newButtons[k] = a;
				k++;
			}
		}
		this.file = f;
		this.buttons = newButtons;
		this.numSetButtons = e.numSetButtons;
		this.filename = e.filename;
		this.numTotalButtons = e.numTotalButtons;
		this.inc = e.inc;
		buildInitialGui(primaryStage);
	}

	/*
	 * Method to open "Saved" serialized File Within the configuration App
	 * Returns the "Saved" builder object
	 */
	public static Builder openSerializedFile(File file) throws IOException {
		Builder e = null;
		try {
			FileInputStream fileIn = new FileInputStream(file.getAbsolutePath());
			ObjectInputStream in = new ObjectInputStream(fileIn);
			e = (Builder) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException i) {
			i.printStackTrace();
		} catch (ClassNotFoundException c) {
			System.out.println("class not found");
			c.printStackTrace();
		}
		return e;
	}

	/*
	 * Method to "Save" Builder Object Within the configuration App Saves it to
	 * configFiles folder with .talk extension
	 */
	public void saveSerializedFile() {
		Builder b = new Builder();
		AudioButton[] newButtons = new AudioButton[this.numTotalButtons];
		int k = 0;
		for(AudioButton a : buttons){
			if(a != null && ((a.name.length() > 0) || (a.AudioPath.length() > 0) || a.ImagePath.length() > 0)){
				newButtons[k] = a;
				k++;
			}
		}
		b.buttons = newButtons;
		b.filename = this.filename;
		b.numSetButtons = this.numSetButtons;
		b.numTotalButtons = this.numTotalButtons;
		b.file = this.file;
		try {
			FileOutputStream fileOut = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(b);
			out.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		buildWelcomeScreen(primaryStage);
	}

	@Override
	public int getNumberOfAudioButtons() {
		return numSetButtons;
	}

	@Override
	public int getNumberOfAudioSets() {
		return numSetButtons;
	}

	@Override
	public int getTotalNumberOfButtons() {
		return numTotalButtons;
	}

	@Override
	public Path getRelativePathToAudioFiles() {
		Path path = FileSystems.getDefault().getPath("/resources");
		return path;
	}

	@Override
	public String[][] getAudioFileNames() {
		String[][] result = new String[getNumberOfAudioButtons()][getNumberOfAudioSets()];
		for (int i = 0; i < buttons.length; i++) {
			result[i][0] = buttons[i].getAudioPath();

		}
		return result;
	}

}
