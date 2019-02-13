package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import application.Parser;
import application.TalkBoxApp;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class Builder extends Application implements TalkBoxConfiguration{		//class which builds the gui for config application

	/**
	 * 
	 */
	private static final long serialVersionUID = -3914103189932065787L;
	public int numTotalButtons;
	public int numSetButtons;
	public String[] result;
	public AudioButton[] buttons;
	public String filename;
	public int inc;

	public static void main(String[] args) {
		launch(args);
	}
	public Label buildTitle(){
		Label title = new Label("TalkBox Config App");
		title.setScaleX(2);
		title.setScaleY(2);
		return title;
	}
	
	public ImageView buildImage() throws FileNotFoundException{
		Image img = new Image(new FileInputStream("src/resources/talkbox.JPG"));
		ImageView iv1 = new ImageView();
		iv1.setImage(img);
		iv1.setFitWidth(300);
		iv1.setPreserveRatio(true);
		iv1.setSmooth(true);
		iv1.setCache(true);
		return iv1;
	}
	
	public Button buildChooseExisting(Stage primaryStage){
		Button button = new Button("Choose Existing");
		button.setTooltip(new Tooltip("Click to choose existing Project"));
		button.setOnAction(new EventHandler<ActionEvent>() { //actionevent upon clicking "submit" button
			@Override
			public void handle(ActionEvent e) {
				filename = Utilities.fileChoose(new Stage());
				try {
					Parser p = new Parser(new File(filename));
					buttons = p.getAudioButtons();
					numTotalButtons = buttons.length;
					numSetButtons = buttons.length;
					buildInitialGui(primaryStage);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		return button;
		
	}
	
	public Button buildNewProject(Stage primaryStage){
		Button button = new Button("New Project");	
		button.setTooltip(new Tooltip("Click to Start New Project"));
		button.setOnAction(new EventHandler<ActionEvent>() { //actionevent upon clicking "submit" button
			@Override
			public void handle(ActionEvent e) {
				try {
					newButtonPrompt(primaryStage);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		return button;
	}
	public FlowPane buildWelcomeScreen(Stage primaryStage) throws FileNotFoundException{
		FlowPane f = new FlowPane(Orientation.VERTICAL);
		f.setColumnHalignment(HPos.CENTER);
		f.setPrefWrapLength(200);
		f.setHgap(30);
		f.setVgap(30);
		f.getChildren().addAll(buildImage(),buildNewProject(primaryStage),buildChooseExisting(primaryStage));
		Scene scene = new Scene(f,300,400);
		scene.getStylesheets().add(this.getClass().getResource("/resources/buttonstyle.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setTitle("Welcome to TalkBox Config");
		primaryStage.show();
		return f;
		
	}
	public FlowPane buildFlowPane(Stage primaryStage) throws FileNotFoundException{
		HBox hb = new HBox();
		FlowPane s = buildSlider();
		TextField textField2 = new TextField();
		Button submit = new Button("Submit");
		submit.setTooltip(new Tooltip("Click Submit to Start Configuration"));
		submit.setOnAction(new EventHandler<ActionEvent>() { //actionevent upon clicking "submit" button
			@Override
			public void handle(ActionEvent e) {
				if ((textField2.getText() != null && !textField2.getText().isEmpty())) {
					filename = textField2.getText();
					primaryStage.close();
					try {
						buildInitialGui(primaryStage);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		Label label2 = new Label("Enter File Name:");
		HBox hb2 = new HBox();
		hb2.getChildren().addAll(s);
		hb2.setAlignment(Pos.CENTER);
		hb.getChildren().addAll(label2, textField2,submit);
		hb.setAlignment(Pos.BOTTOM_CENTER);
		hb.setSpacing(10);	
		FlowPane g = new FlowPane(Orientation.VERTICAL);
		g.setColumnHalignment(HPos.CENTER);
		g.setPrefWrapLength(200);
		g.setVgap(20);
		g.setHgap(20);
		g.setAlignment(Pos.BOTTOM_CENTER);
		g.getChildren().addAll(buildImage(),hb2,hb);
		return g;
	}
	public void newButtonPrompt(Stage primaryStage) throws FileNotFoundException {   //GUI for initial prompt
		FlowPane hb = buildFlowPane(primaryStage);
		hb.setVgap(20);
		primaryStage.setTitle("TalkBox Config App");
		Scene scene = new Scene(hb,320,450);
		String css = this.getClass().getResource("/resources/buttonstyle.css").toExternalForm();
		scene.getStylesheets().add(css);
		primaryStage.setScene(scene);	
		primaryStage.show();
	}
	
	public FlowPane buildSlider(){
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

	public ToolBar buildToolbar() { // method which builds and returns a Toolbar
		Button x = new Button("Save"); // save button
		x.setTooltip(new Tooltip("Click to Save File"));
		x.setOnMouseClicked(new EventHandler<MouseEvent>() { // action listener
																// for mouse
																// click
			public void handle(MouseEvent me) {
				try {
					saveFile(buttons);	//append each button to file

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		ToolBar toolBar = new ToolBar(x // add save button to toolbar
		);

		toolBar.setPrefSize(200, 20);
		return toolBar;
	}

	public void buildInitialGui(Stage primaryStage) throws IOException {		//method which builds the configuration GUI
		buttons = (buttons == null) ? new AudioButton[numTotalButtons] : buttons;
		inc = 0;
		int increment = 0;
		GridPane gridpane = new GridPane();
		gridpane.setPrefSize(500, 500);
		gridpane.setVgap(10);
		gridpane.setHgap(10);
		Image img = new Image(new FileInputStream("src/resources/plusSign.JPG"));	//simple + sign image
		for (int i = 0; i < numTotalButtons; i++) {
			int k = i;
			if (i > 0 && i % 6 == 0) {	
				increment++; //incrementer to define number of rows
			}
			AudioButton currentButton;
			currentButton = (buttons[i] == null) ? new AudioButton() : buttons[i];
			ImageView iv1 = new ImageView();
			if(buttons[i] == null)iv1.setImage(img);
			else iv1.setImage(new Image(new FileInputStream(buttons[i].getImagePath())));
			iv1.setFitWidth(100);
			iv1.setPreserveRatio(true);
			iv1.setSmooth(true);
			iv1.setCache(true);
			File f;
			f = (buttons[i] == null) ? new File("src/resources/correct.wav") : new File(buttons[i].getAudioPath());
			TextField textField;
			if(currentButton == null) textField = new TextField("");
			else textField = new TextField(currentButton.getName());
			Button edit = new Button("Edit Button:");
			edit.setPrefSize(100, 20);
			edit.setTooltip(new Tooltip("Click to Edit Button"));
			edit.setOnAction(new EventHandler<ActionEvent>() { //event handler for "edit" button allowing user to edit buttons
				@Override
				public void handle(ActionEvent event) {
					Stage x = new Stage();
					GridPane g = Utilities.setEditPrompt(x);
					Button submit = new Button("Submit");
					GridPane.setConstraints(submit, 4, 4);
					GridPane.setConstraints(textField, 1, 0);
					g.getChildren().add(submit);
					g.getChildren().add(textField);
					x.setScene(new Scene(g));
					x.show();
					submit.setOnAction(new EventHandler<ActionEvent>() {	//event handler for submit within edit gui
						@Override
						public void handle(ActionEvent e) {
							;
							try {
								//if(numSetButtons < numTotalButtons)numSetButtons++;
								edit.setText(textField.getText());	//sets text of button to user input
								Image n = new Image(new FileInputStream(Utilities.ImagePath)); //sets imagePath to user input
								iv1.setImage(n);
								iv1.setFitWidth(100);
								iv1.setPreserveRatio(true);
								iv1.setSmooth(true);
								iv1.setCache(true);
								Tooltip.install(iv1, new Tooltip("Click to Play Sound"));
								currentButton.setName(textField.getText());//assigns current input to an audiobutton
								currentButton.setImagePath(Utilities.ImagePath);
								currentButton.setAudioPath(Utilities.AudioPath);
								buttons[k] = currentButton;
								iv1.setOnMouseClicked(new EventHandler<MouseEvent>() {	//adds event handler to picture, allowing user click to generate sound
									public void handle(MouseEvent me) {
										if(me.getButton().equals(MouseButton.PRIMARY)){
											URI u = f.toURI();
											Media sound = new Media(u.toString());
											MediaPlayer mediaPlayer = new MediaPlayer(sound);
											mediaPlayer.play();//method which allows sound to be outputted
										}
									}
								});

							} catch (FileNotFoundException e1) {
								e1.printStackTrace();
							}
							x.close();
						}

					});
				}

			});
			GridPane.setConstraints(edit, i % 6, 5 + 2 * increment);	//sets edit buttons in correct positions
			GridPane.setConstraints(iv1, i % 6, 4 + 2 * increment);		//sets images in correct positions
			gridpane.getChildren().addAll(iv1, edit);
		}
		ToolBar bar = buildToolbar();		//adds toolbar to GUI
		Button play = buildPlayButton(new Stage()); //adds playButton to GUI
		StackPane a = new StackPane();
		a.getChildren().addAll(gridpane, play, bar);	//adds all elements to a stackpane
		StackPane.setAlignment(bar, Pos.TOP_LEFT);		
		StackPane.setAlignment(play, Pos.BOTTOM_RIGHT);
		StackPane.setAlignment(gridpane, Pos.BOTTOM_CENTER);
		Scene scene = new Scene(a);
		String css = this.getClass().getResource("/resources/buttonstyle.css").toExternalForm();
		scene.getStylesheets().add(css);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public Button buildPlayButton(Stage primaryStage) throws IOException {	//method which builds "play" button
		Button play = new Button("Play");
		play.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {		//mouseevent handler which launches talkbox app
				try {
					TalkBoxApp a = new TalkBoxApp(new File(filename + ".txt"));
					GridPane b = a.getGridpane();
					primaryStage.setTitle("TalkBox Application");
					primaryStage.setScene(new Scene(b));
					primaryStage.show();
				} catch (IOException e) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error Dialog");
					alert.setHeaderText("Invalid File");
					alert.setContentText("Save File Before Playing");
					alert.showAndWait();
					e.printStackTrace();
				}
			}
		});

		return play;

	}
	
	public int getSetButtons(){
		int i = 0;
		for(AudioButton b : buttons){
			if(b!=null){
				if(b.getName().length() > 0 && b.getAudioPath().length() > 0 && b.getImagePath().length() > 0){
					i++;	
				}
			}
		}
		numSetButtons = i;
		return numSetButtons;
	}
	
	public void saveFile(AudioButton[] b) throws IOException {		//simple method to save a file
		FileWrite a = new FileWrite(new File(filename + ".txt"));	//create new txt file
		FileWrite.fileCreate(filename + ".txt");
		a.fileAppend(getSetButtons() + ""); //append numbuttons
		for (AudioButton x : b) {
			if(x!= null){
			a.fileAppend(x.name);	//append name
			a.fileAppend(x.ImagePath); //append imagepath
			a.fileAppend(x.AudioPath); //append audiopath
			}
		}

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		buildWelcomeScreen(primaryStage);
		// System.out.println(numButtonReturn());
		// loadImage(primaryStage);
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
		Path path = FileSystems.getDefault().getPath("src/resouces");
		return path;
	}

	@Override
	public String[][] getAudioFileNames() {
		// TODO Auto-generated method stub
		String[][] result = new String[getNumberOfAudioButtons()][getNumberOfAudioSets()];
		for(int i = 0; i < buttons.length; i++) {
			result[i][0] = buttons[i].getAudioPath();
			
		}
		return result;
	}

}
