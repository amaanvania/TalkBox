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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import application.TalkBoxApp;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import log.Stat;
import log.TBCLog;

public class Builder extends Application implements TalkBoxConfiguration {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	// class which builds the gui for config application
	
	private static final Logger logr = Logger.getLogger(Builder.class.getName());
	
	/**
	 * 
	 */
	public int numTotalButtons = 6;
	public int numSetButtons;
	public List<AudioButton> buttons;
	public String filename;
	public transient int inc;
	public double volume;
	public int numPages = 1;
	volatile boolean audioPlaying = false;
	File file;

	public Builder() {
		this.numTotalButtons = 6;
		this.numSetButtons = 0;
		this.buttons = null;
		this.filename = "";
		this.inc = 0;
		this.numPages = 1;
		buttons = new ArrayList<AudioButton>();
		for (int i = 0; i < numTotalButtons; i++) {
			buttons.add(null);
		}
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
		File defaultConfig = new File(Utilities.logPath + "defaultConfig.log"); //building temp files to log in
		if(defaultConfig.exists()) //sometime this file will already exist 
			defaultConfig.delete();
		try {
		defaultConfig.createNewFile();
		}
		catch(IOException ee) {
			ee.printStackTrace();
		}
		defaultConfig.deleteOnExit();
		Button button = new Button("New Project");
		button.setTooltip(new Tooltip("Click to Start New Project"));
		button.setOnAction(e -> {
			try {
				buildInitialGui(primaryStage, pagination());
			} catch (IOException e1) {
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
	 * OUT DATED
	 * Method which builds the frame when "new Project" button is clicked
	 */
	/* public FlowPane buildFlowPane(Stage primaryStage) throws FileNotFoundException {
		HBox hb = new HBox();
		FlowPane s = buildSlider();
		Button submit = new Button("Submit");
		submit.setTooltip(new Tooltip("Click Submit to Start Configuration"));
		submit.setOnAction(e -> {
			try {
				if (numTotalButtons < 1) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning Dialog");
					alert.setHeaderText("Warning! Zero Buttons");
					alert.setContentText("Set more than 0 buttons");
					alert.showAndWait();
				} else
					buildInitialGui(primaryStage, pagination());
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
	*/

	/*
	 * OUT DATED
	 * Method which builds GUI when "new Project" button is chosen
	 */
	/*public void newButtonPrompt(Stage primaryStage) throws FileNotFoundException { // GUI
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
	*/

	public ImageView buildStopImage() throws FileNotFoundException {
		String s = getClass().getResource("/resources/stopbutton.png").toExternalForm();
		Image img = new Image(s);
		ImageView iv1 = new ImageView();
		iv1.setImage(img);
		iv1.setFitWidth(100);
		iv1.setPreserveRatio(true);
		iv1.setSmooth(true);
		iv1.setCache(true);
		return iv1;
	}

	/*
	 * OUT DATED
	 * Method which builds slider to define number of Buttons
	 */
	/*
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
	*/

	public MenuBar buildTopMenu(Logger log) {
		Menu helps = new Menu("Help"); // help dropdown
		MenuItem help = new MenuItem("User manual");
		help.setId("help-config");
		help.setOnAction(e -> {
			try {
				Desktop.getDesktop().browse(new URI(
						"https://github.com/amaanvania/TalkBox/blob/master/Documentation/TalkBoxUserManual.pdf"));
				log.fine("Help: user manual opened");
				Stat.UserManualCounter++;
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
		});
		MenuItem contact = new MenuItem("Contact us");
		contact.setId("contact-wiki");
		contact.setOnAction(e -> {
			try {
				Desktop.getDesktop().browse(new URI("https://github.com/amaanvania/TalkBox/wiki"));
				log.fine("Help: contact us opened");
				Stat.ContactUsCounter++;
			} catch (IOException | URISyntaxException e1) {
				e1.printStackTrace();
			}
		});
		MenuItem bugReport = new MenuItem("Report issue");
		bugReport.setOnAction(e -> {
			try {
				Desktop.getDesktop().browse(new URI("https://github.com/amaanvania/TalkBox/issues"));
				log.fine("Help: report issue opened");
				Stat.ReportIssueCounter++;
			} catch (IOException | URISyntaxException e1) {
				e1.printStackTrace();
			}
		});
		helps.getItems().addAll(help, contact, bugReport);

		Menu volumes = new Menu("Volume"); // volume drop down
		CustomMenuItem volumesOne = new CustomMenuItem();
		final Slider vSlider = new Slider(0, 100, 100); // volume slider
		volume = 100; // initial value for volume
		vSlider.setOnMouseClicked(e -> {
			volumeHandle(vSlider);
			log.fine("VOLUME: volume changed to " + vSlider.getValue());
			Stat.VolumeCounter++;
		});
		
		Menu logss = new Menu("Logs");
		logss.setId("Logs");
	    MenuItem simulator = new MenuItem("Open the Logger");
	    simulator.setOnAction(e -> {
	    	try {
	    		TBCLog aa = new TBCLog();
	    		aa.start(new Stage());
	    	}
	    	catch(Exception e1){
	    		e1.printStackTrace();
	    	}
	    });
	    logss.getItems().addAll(simulator);
		
		volumesOne.setContent(vSlider);
		volumes.getItems().addAll(volumesOne);

		MenuBar menuBar = new MenuBar();
		menuBar.setId("Top-menu");
		menuBar.getMenus().addAll(volumes, helps, logss);
		return menuBar;
	}
	
	public void volumeHandle(Slider v) {
		v.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				volume = newValue.doubleValue() / 100.00;
			}
		});
	}

	public void recordHandle() throws FileNotFoundException {
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

	public VBox buildRecordFrame() {
		VBox v = new VBox();
		Text line1 = new Text("\n Instructions on Recording Audio \n");
		Text line2 = new Text("\n 1. Click Record Button \n");
		Text line3 = new Text("\n 2. Set path of audio file \n");
		Text line4 = new Text("\n 3. Now microphone is recording, input sound \n");
		Text line5 = new Text("\n 4. Click stop Button to stop recording \n");
		Text line6 = new Text("\n 5. Click play Button to play recording \n");
		v.getChildren().addAll(line1, line2, line3, line4, line5, line6);
		return v;
	}

	public void alertHandle() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning!");
		alert.setHeaderText("Warning! Not Yet Implemented");
		alert.setContentText("Functionality will come in future");
		alert.showAndWait();
	}

	public ToolBar buildBotToolbar(Stage primaryStage) throws IOException { // method
																			// which
																			// builds
																			// and
																			// returns
																			// the
																			// bot
																			// Toolbar
		Button play = playBtn(primaryStage);
		Button save = saveBtn();
		Button addNewButton = new Button("Add Button");
		addNewButton.setOnAction(e -> addButtonHandle(primaryStage));
		Button deleteButton = new Button("Delete Last Button");
		deleteButton.setOnAction(e -> deleteLastButtonHandle(primaryStage));
		Button deleteButton2 = new Button("Delete Specific Button");
		deleteButton2.setOnAction(e -> deleteSpecificButtonHandle(primaryStage));
		Button recordAudio = new Button("Record Audio");
		recordAudio.setId("recordAudio-config");
		recordAudio.setTooltip(new Tooltip("Click to Record Audio"));
		recordAudio.setOnAction(e -> {
			try {
				//
				recordHandle();
				logr.fine("Record: recording window opened");
				Stat.RecordAudioCounter++;
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		});
		ToolBar toolBar = new ToolBar(play, save, recordAudio, addNewButton, deleteButton, deleteButton2 // add
																											// save
																											// button
																											// to
																											// toolbar
		);
		toolBar.setPrefSize(200, 20);
		toolBar.setId("bot-toolbar-config");
		return toolBar;
	}

	/**
	 * @param primaryStage
	 * @return
	 */
	private Button playBtn(Stage primaryStage) {
		Button play = new Button("Test"); // play button
		play.setId("play-config");
		play.setTooltip(new Tooltip("Click to Open this configuration in the TalkBox App"));
		play.setOnMouseClicked(e -> {
			try {
				if (getSetButtons() > 0 && this.file != null) {
					TalkBoxApp a = new TalkBoxApp(openSerializedFile(file));
					a.buildApplication(new Stage());
					primaryStage.close();
					logr.fine("TEST: " + filename + " simulator being opened");
					Stat.TestCounter++;
				} else {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning Dialog");
					alert.setHeaderText("Warning! Null File");
					alert.setContentText("Save file before attempting to Play");
					alert.showAndWait();
					logr.fine("Error: " +  "failed simulator opening");
					Stat.TestCounter++;
				}
			} catch (IOException z) {
				z.printStackTrace();
			}
		});
		return play;
	}

	/**
	 * @return returns a save button that has the functionality to save the
	 *         current state of the application
	 */
	private Button saveBtn() throws IOException {
		Button save = new Button("Save"); // save button
		save.setId("save-config");
		save.setTooltip(new Tooltip("Click to Save File"));
		save.setOnMouseClicked(e -> {
			if (file == null) {
				file = Utilities.configFileSave(new Stage());
				if(file != null) {
					filename = Utilities.getFileName();
					logr.fine("Save button: " + "current config saved as " + filename);
					Stat.SaveCounter++;
				}
				if(file == null) {
					logr.fine("Save button: save failed or closed");
					Stat.SaveCounter++;
				}
			}
			if(file != null) {
				File appendLog = new File(Utilities.logPath + filename + "Config.log");
				File defaultConfig = new File(Utilities.logPath + "defaultConfig.log"); //building temp files to log in	
				FileHandler fh = null;
				if(!appendLog.exists()) {
					try {
						appendLog.createNewFile();
						Files.copy(defaultConfig.toPath(), appendLog.toPath(), StandardCopyOption.REPLACE_EXISTING);
						for(Handler aaa : logr.getHandlers()) {
							aaa.close();
							logr.removeHandler(aaa);
						}
						new File(Utilities.logPath + filename + "Sim.log").createNewFile();
						fh = new FileHandler(Utilities.logPath + filename + "Config.log", 8096, 1, true);
						fh.setLevel(Level.FINEST);
						fh.setFormatter(new Formatter() {
						   public String format(LogRecord record) {
						        return 
						        		LocalDateTime.now().toString() + " " + record.getMessage() + "\n";
						      }
						    });
						logr.addHandler(fh);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				else {
					logr.fine("Save button: " + filename + " saved");
					Stat.SaveCounter++;
	
				}
			}
			saveSerializedFile(); // append each button to file
		});
		return save;
	}

	public ImageView buildImageView() {
		ImageView iv1 = new ImageView();
		iv1.setId("image-config");
		iv1.setFitWidth(100);
		iv1.setPreserveRatio(true);
		iv1.setSmooth(true);
		iv1.setCache(true);
		return iv1;
	}

	public void editHandle(Stage x, Button submit, TextField textField) {
		GridPane g = Utilities.setEditPrompt(x);
		GridPane.setConstraints(submit, 4, 4);
		GridPane.setConstraints(textField, 1, 0);
		g.getChildren().add(submit);
		g.getChildren().add(textField);
		x.setScene(new Scene(g));
		x.show();
	}

	public boolean notValidInput(TextField textField) {
		return textField.getText().length() < 1 || Utilities.AudioPath == null || Utilities.AudioPath.length() < 1
				|| Utilities.ImagePath == null || Utilities.ImagePath.length() < 1;
	}

	public void emptyFieldHandle() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning!");
		alert.setHeaderText("Warning! Invalid input");
		alert.setContentText("Make sure input is valid."); 
		alert.showAndWait();
	}

	public MediaPlayer imageHandle(String audioPath) throws InterruptedException {
		File f = new File(audioPath);
		URI u = f.toURI();
		Media sound = new Media(u.toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.setVolume(volume);
		return mediaPlayer;

	}

	public void dragOverHandle(DragEvent e) {
		if (e.getDragboard().hasFiles()) {
			e.acceptTransferModes(TransferMode.ANY);
		}
	}

	public void dropHandle(DragEvent e, AudioButton b) throws IOException {
		List<File> files = e.getDragboard().getFiles();
		if (isImage(files.get(0).getAbsolutePath())) {
			b.setImagePath(files.get(0).getAbsolutePath());
			b.setName(files.get(0).getName());
			logr.fine("Drag drop: " +  "image changed to " + Utilities.ImagePath);
			Stat.DragDropAudioButtonCounter++;
		}
		if (isAudio(files.get(0).getAbsolutePath())) {
			b.setAudioPath(files.get(0).getAbsolutePath());
			logr.fine("Drag drop: " + "audio changed to " + Utilities.ImagePath);
			Stat.DragDropAudioButtonCounter++;
		}	
		
	}

	public static boolean isImage(String filepath) throws IOException {
		File f = new File(filepath);
		return (ImageIO.read(f) != null);
	}

	public static boolean isAudio(String filepath) {
		File f = new File(filepath);
		return f.getAbsolutePath().endsWith(".wav") || f.getAbsolutePath().endsWith(".mp3");
	}

	public void buildAutomaticButton(AudioButton b) throws Exception {
		TextInputDialog d = new TextInputDialog("Enter random word");
		d.setContentText("Set Text");
		d.showAndWait();
		String input = d.getResult();
		File f = new File(WebDownloader.ImagePath + input + ".jpg");
		File p = new File(WebDownloader.audioPath + input + ".wav");
		if (f.exists() && p.exists()) {
			b.setImagePath(WebDownloader.ImagePath + input + ".jpg");
			b.setAudioPath(WebDownloader.audioPath + input + ".wav");
			b.setName(input);
		} else {
			WebDownloader.downloadAutomatic(input);
			b.setImagePath(WebDownloader.ImagePath + input + ".jpg");
			b.setAudioPath(WebDownloader.audioPath + input + ".wav");
			b.setName(input);
		}
	}

	/*
	 * Method which builds the configuration GUI Allowing user to view and edit
	 * each button
	 * 
	 */
	public void buildInitialGui(Stage primaryStage, Pagination p) throws IOException {
		logr.setLevel(Level.ALL);
		for(Handler aaa : logr.getHandlers()) {
			aaa.close();
			logr.removeHandler(aaa);
		}
		File existFile = new File(Utilities.logPath + filename + "Config.log");
		FileHandler fh = null;
		if(existFile.exists()) { //checking if the log was already created if so use the already created file to log
			try {
				fh = new FileHandler(Utilities.logPath + filename + "Config.log", 8096, 1, true);
				fh.setLevel(Level.FINEST);
				fh.setFormatter(new Formatter() {
				   public String format(LogRecord record) {
				        return 
				        		LocalDateTime.now().toString() + " " + record.getMessage() + "\n";
				      }
				    });
				logr.addHandler(fh);
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
		else { // using a default logging file that will be transfered to the a log file one saved
			try {
				fh = new FileHandler(Utilities.logPath + "defaultConfig.log", 8096, 1, true);
				fh.setLevel(Level.FINEST);
				fh.setFormatter(new Formatter() {
				   public String format(LogRecord record) {
				        return 
				        		LocalDateTime.now().toString() + " " + record.getMessage() + "\n";
				      }
				    });
				logr.addHandler(fh);
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
		ToolBar botToolBar = buildBotToolbar(primaryStage);
		MenuBar topMenu = buildTopMenu(logr);
		StackPane a = new StackPane();
		p.setCurrentPageIndex(numPages - 1);
		a.getChildren().addAll(botToolBar, topMenu, p);
		StackPane.setAlignment(topMenu, Pos.TOP_CENTER);
		StackPane.setAlignment(botToolBar, Pos.BOTTOM_CENTER);
		Scene scene = new Scene(a, 651, 510);
		String css = this.getClass().getResource("/resources/buttonstyle.css").toExternalForm();
		scene.getStylesheets().add(css);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public VBox createPage(int pageIndex) throws FileNotFoundException {
		VBox box = new VBox(20);
		int page = pageIndex;
		for (int i = page; i < page + 1; i++) {
			box.getChildren().add(buildConfigWindow(i));
		}
		return box;
	}

	public Pagination pagination() {
		Pagination pagination = new Pagination(1);
		pagination.setPageCount(this.numPages);
		pagination.setMaxHeight(400.0);
		pagination.setPageFactory(new Callback<Integer, Node>() {
			@Override
			public Node call(Integer pageIndex) {
				try {
					return createPage(pageIndex);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				return null;
			}
		});
		return pagination;
	}

	public void addButtonHandle(Stage primaryStage) {
		{
			buttons.add(null);
			if (buttons.size() > 6 && (buttons.size() - 6) % 6 == 1)
				numPages++;
			System.out.println("size of buttons: " + buttons.size());
			Platform.runLater(() -> {
				try {
					buildInitialGui(primaryStage, pagination());
					logr.fine("Add: new button added to the right");
					Stat.AddNewCounter++;
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
		System.out.println("Num Buttons: " + buttons.size() + " " + "numPages: " + numPages);
	}

	public void deleteLastButtonHandle(Stage primaryStage) {
		if (buttons.size() > 0) {
			buttons.remove(buttons.size() - 1);
			System.out.println("size of buttons: " + buttons.size());
		}
		Platform.runLater(() -> {
			try {
				if (numPages > 1 && (buttons.size() - 6) % 6 == 0)
					numPages--;
				buildInitialGui(primaryStage, pagination());
				logr.fine("Delete: most right button deleted");
				Stat.DeleteLastCounter++;
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

	}

	public void deleteSpecificButtonHandle(Stage primaryStage) {
		TextInputDialog d = new TextInputDialog("Enter Index (0 = first)");
		d.setContentText("Set index");
		d.showAndWait();
		int i = Integer.parseInt(d.getResult());
		System.out.println(i);
		if (buttons.size() > 0) {
			buttons.remove(i);
			System.out.println("size of buttons: " + buttons.size());
		}
		Platform.runLater(() -> {
			try {
				if (numPages > 1 && (buttons.size() - 6) % 6 == 0)
					numPages--;
				buildInitialGui(primaryStage, pagination());
				logr.fine("Delete: button " + i  + " deleted");
				Stat.DeleteSpecificCounter++;
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

	}

	public GridPane buildConfigWindow(int pageNumber) throws FileNotFoundException {
		GridPane gridpane = new GridPane();
		gridpane.setPrefSize(500, 500);
		gridpane.setVgap(10);
		gridpane.setHgap(10);
		String s = getClass().getResource("/resources/PlusSign.png").toExternalForm();
		Image img = new Image(s);
		int numButtons;
		if (pageNumber < numPages) {
			numButtons = 6;
		} else {
			numButtons = buttons.size() - ((numPages - 1) * 6);
		}
		for (int i = pageNumber * 6; i < (pageNumber * 6) + numButtons; i++) {
			int k = i;
			k = (k >= buttons.size() ? buttons.size() - 1 : k);
			int j = k;
			ImageView stop = buildStopImage();
			stop.setDisable(true);
			stop.setOpacity(0);
			AudioButton currentButton = (buttons.get(k) == null) ? new AudioButton() : buttons.get(k);
			ImageView iv1 = buildImageView();
			iv1.setOnDragOver(e -> dragOverHandle(e));
			if (buttons.get(k) == null)
				iv1.setImage(img);
			else
				iv1.setImage(new Image(new FileInputStream(buttons.get(k).getImagePath())));
			TextField textField;
			textField = (buttons.get(k) == null) ? new TextField("") : new TextField(currentButton.getName());
			Button edit = buildButton(k, textField);
			Button buildAuto = new Button("AutoFill");
			buildAuto.setPrefSize(100, 20);
			buildAuto.setOnAction(e -> {
				try {
					buttons.set(j, currentButton);
					buildAutomaticButton(buttons.get(j));
					iv1.setImage(new Image(new FileInputStream(buttons.get(j).getImagePath())));
					Utilities.ImagePath = buttons.get(j).getImagePath();
					Utilities.AudioPath = buttons.get(j).getAudioPath();
					textField.setText(buttons.get(j).getName());
					edit.setText(buttons.get(j).getName());
					logr.fine("Autofill: button " + j + " autofilled to " + buttons.get(j).getName());
					Stat.AutofillAudioButtonCounter++;
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			});
			Button submit = new Button("Submit");
			Stage x = new Stage();
			edit.setOnAction(e -> editHandle(x, submit, textField));
			submit.setOnAction(event -> {
				if (notValidInput(textField))
					emptyFieldHandle();
				else
					submitHandle(j, currentButton, iv1, textField, edit, x);
			});
			iv1.setOnDragDropped(e -> {
				try {
					dropHandle(e, currentButton);
					if (isImage(e.getDragboard().getFiles().get(0).getAbsolutePath())) {
						iv1.setImage(new Image(new FileInputStream(currentButton.getImagePath())));
						Utilities.ImagePath = currentButton.getImagePath();
						textField.setText(currentButton.getName());
					}
					if (isAudio(e.getDragboard().getFiles().get(0).getAbsolutePath())) {
						Utilities.AudioPath = currentButton.getAudioPath();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			});
			iv1.setOnMouseClicked(e -> {
				playSound(j, stop, iv1, logr, true);

			});
			GridPane.setConstraints(buildAuto, j % 6, 6 + 2);
			GridPane.setConstraints(edit, j % 6, 5 + 2);
			GridPane.setConstraints(iv1, j % 6, 4 + 2);
			GridPane.setConstraints(stop, j % 6, 4 + 2);
			gridpane.getChildren().addAll(stop, iv1, edit, buildAuto);
		}
		return gridpane;
	}

	public void playSound(int j, ImageView stop, ImageView iv1, Logger log, Boolean choice) {
		try {
			MediaPlayer p = imageHandle(buttons.get(j).getAudioPath());
			p.setOnPlaying(() -> {
				audioPlaying = true;
				iv1.setOpacity(0);
				stop.setOpacity(100);
				iv1.setDisable(true);
				stop.setDisable(false);
				if(choice == true) {
					log.fine("Played: " + buttons.get(j).getName() + " played");
					Stat.AudioButtonsTested++;
				} 
				else if(choice == false){
					log.fine("Played: " + buttons.get(j).getName() + " played");
					Stat.AudioButtonsPlayed++;
				}
				stop.setOnMouseClicked(z -> {
					audioPlaying = false;
					iv1.setDisable(false);
					stop.setDisable(true);
					stop.setOpacity(0);
					iv1.setOpacity(100);
					audioPlaying = false;
					p.stop();
					if(choice == true) {
						log.fine("Stop: " + buttons.get(j).getName() + " audio stopped early");
						Stat.AudioButtonsTestedStop++;
					} 
					else if(choice == false) {
						log.fine("Stop: " + buttons.get(j).getName() + " audio stopped early");
						Stat.AudioButtonsStopped++;
					}
				});
			});
			p.setOnEndOfMedia(() -> {
				iv1.setDisable(false);
				stop.setDisable(true);
				stop.setOpacity(0);
				iv1.setOpacity(100);
				audioPlaying = false;
			});
			if (!audioPlaying) {
				p.play();
				iv1.setDisable(false);
				stop.setDisable(true);
				stop.setOpacity(0);
				iv1.setOpacity(100);
			}
		} catch (InterruptedException e1) {
			audioPlaying = false;
		}
	}

	private Button buildButton(int i, TextField textField) {
		Button edit = (buttons.get(i) == null) ? new Button("Edit: " + i) : new Button(textField.getText());
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
			logr.fine("Button " + k + " edited");
			Stat.AudioButtonsEdited++;
			if(!currentButton.getName().equals(edit.getText())) {
				logr.fine("Button: " + k + " name changed to " + edit.getText());
				Stat.TitlesSet++;
			}
			currentButton.setName(textField.getText());
			if(!currentButton.getImagePath().equals(Utilities.ImagePath)) {
				logr.fine("Button: " + currentButton.getName() + " " + "image changed to " + Utilities.ImagePath);
				Stat.ImagesSelected++;
			}
			currentButton.setImagePath(Utilities.ImagePath);
			if(!currentButton.getAudioPath().equals(Utilities.AudioPath)) {
				logr.fine("Button: " + currentButton.getName() + " " + "audio changed to " + Utilities.AudioPath);
				Stat.AudioTracksSelected++;
			}
			currentButton.setAudioPath(Utilities.AudioPath);
			buttons.set(k, currentButton);
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
		List<AudioButton> newButtons = new ArrayList<AudioButton>();
		for (AudioButton a : e.buttons) {
			if (a != null && ((a.name.length() > 0) || (a.AudioPath.length() > 0) || a.ImagePath.length() > 0)) {
				newButtons.add(a);
			}
		}
		this.file = f;
		this.buttons = newButtons;
		this.numSetButtons = e.numSetButtons;
		this.filename = e.filename;
		this.numTotalButtons = e.numTotalButtons;
		this.inc = e.inc;
		double numPages = Math.ceil(newButtons.size() / 6.0);
		this.numPages = (int) numPages;
		System.out.println(this.numPages);
		buildInitialGui(primaryStage, pagination());
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
		List<AudioButton> newButtons = new ArrayList<AudioButton>();
		for (AudioButton a : buttons) {
			if (a != null && ((a.name.length() > 0) || (a.AudioPath.length() > 0) || a.ImagePath.length() > 0)) {
				newButtons.add(a);
			}
		}
		b.buttons = newButtons;
		b.filename = this.filename;
		b.numSetButtons = this.numSetButtons;
		b.numTotalButtons = this.numTotalButtons;
		b.file = this.file;
		double numPages = Math.ceil(newButtons.size() / 6.0);
		b.numPages = (int) numPages;
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
		File sourcePath = new File(Utilities.resourcePath);
		if(!sourcePath.exists()) {
			try {
				sourcePath.mkdir();
			}
			catch(SecurityException e) {
				e.printStackTrace();
			}	
			
		}
		File logPath = new File(Utilities.logPath); //building the directory for logging
		if(!logPath.exists()) {
			try {
				logPath.mkdir();
			}
			catch(SecurityException e) {
				e.printStackTrace();
			}
		}
		File defaultConfig = new File(Utilities.logPath + "defaultConfig.log"); //building temp files to log in
		if(defaultConfig.exists()) //sometime this file will already exist 
			defaultConfig.delete();
		defaultConfig.createNewFile();
		defaultConfig.deleteOnExit();
		
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
		for (int i = 0; i < buttons.size(); i++) {
			result[i][0] = buttons.get(i).getAudioPath();

		}
		return result;
	}

}
