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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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
				buildInitialGui(primaryStage);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
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

	public ToolBar buildTopToolbar() { // method which builds and returns a
										// Toolbar
		Button help = new Button("Help"); // help button
		help.setId("help-config");
		help.setTooltip(new Tooltip("Opens the User Manual"));
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
		Button recordAudio = new Button("Record Audio");
		recordAudio.setId("recordAudio-config");
		recordAudio.setTooltip(new Tooltip("Click to Record Audio"));
		recordAudio.setOnAction(e -> recordHandle());		
		Button contact = new Button("Contact"); // contact button
		contact.setId("contact-wiki");
		contact.setTooltip(new Tooltip("Opens contact wiki"));
		contact.setOnAction( e-> {
					try {
						Desktop.getDesktop().browse(new URI("https://github.com/amaanvania/TalkBox/wiki"));
					} catch (IOException | URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		});
		
		ToolBar toolBar = new ToolBar(recordAudio,help, contact // add help button to toolbar
		);

		toolBar.setPrefSize(200, 20);
		toolBar.setId("top-toolbar-config");
		return toolBar;
	}
	public void recordHandle(){
		AudioRecord a = new AudioRecord();
		Stage x = new Stage();
		Button record = a.buildButton(x);
		record.setId("record-config");
		Scene scene = new Scene(record,150,150);
		String css = this.getClass().getResource("/resources/buttonstyle.css").toExternalForm();
		scene.getStylesheets().add(css);
		x.setScene(scene);
		x.show();
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
		ToolBar toolBar = new ToolBar(play, save // add save button to toolbar
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
		Button play = new Button("Play"); // play button
		play.setId("play-config");
		play.setTooltip(new Tooltip("Click to Open this configuration in the TalkBox App"));
		play.setOnMouseClicked(e -> {
			try {
				if (getSetButtons() > 0) {
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
				// TODO Auto-generated catch block
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
	public void imageHandle(String audioPath){
		File f = new File(audioPath);
		URI u = f.toURI();
		Media sound = new Media(u.toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
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
			if (buttons[i] == null)iv1.setImage(img);
			else iv1.setImage(new Image(new FileInputStream(buttons[i].getImagePath())));
			TextField textField;
			textField = (buttons[i] == null) ? new TextField("") : new TextField(currentButton.getName());
			Button edit = buildButton(i, textField);
			Button submit = new Button("Submit");
			Stage x = new Stage();
			edit.setOnAction(e -> editHandle(x,submit,textField));
			submit.setOnAction(event -> submitHandle(k, currentButton, iv1, textField, edit, x));
			iv1.setOnMouseClicked(e -> imageHandle(buttons[k].getAudioPath()));
			GridPane.setConstraints(edit, i % 6, 5 + 2 * increment);
			GridPane.setConstraints(iv1, i % 6, 4 + 2 * increment);
			gridpane.getChildren().addAll(iv1, edit);
		}
		ToolBar topToolBar = buildTopToolbar(); 
		ToolBar botToolBar = buildBotToolbar();
		StackPane a = new StackPane();
		a.getChildren().addAll(gridpane, botToolBar, topToolBar);
		StackPane.setAlignment(topToolBar, Pos.TOP_LEFT);
		StackPane.setAlignment(botToolBar, Pos.BOTTOM_CENTER);
		StackPane.setAlignment(gridpane, Pos.BOTTOM_CENTER);
		Scene scene = new Scene(a);
		String css = this.getClass().getResource("/resources/buttonstyle.css").toExternalForm();
		scene.getStylesheets().add(css);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private Button buildButton(int i, TextField textField) {
		Button edit = (buttons[i] == null) ? new Button("Edit Button:") : new Button(textField.getText());
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
			return;
		} catch (ClassNotFoundException c) {
			return;
		}
		this.file = f;
		this.buttons = e.buttons;
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
	public Builder openSerializedFile(File file) throws IOException {
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
		b.buttons = this.buttons;
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
		// TODO Auto-generated method stub
		return numSetButtons;
	}

	@Override
	public int getNumberOfAudioSets() {
		// TODO Auto-generated method stub
		return numSetButtons;
	}

	@Override
	public int getTotalNumberOfButtons() {
		// TODO Auto-generated method stub
		return numTotalButtons;
	}

	@Override
	public Path getRelativePathToAudioFiles() {
		// TODO Auto-generated method stub
		Path path = FileSystems.getDefault().getPath("/resources");
		return path;
	}

	@Override
	public String[][] getAudioFileNames() {
		// TODO Auto-generated method stub
		String[][] result = new String[getNumberOfAudioButtons()][getNumberOfAudioSets()];
		for (int i = 0; i < buttons.length; i++) {
			result[i][0] = buttons[i].getAudioPath();

		}
		return result;
	}

}
