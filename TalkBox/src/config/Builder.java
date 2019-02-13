package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

import application.TalkBoxApp;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class Builder extends Application {		//class which builds the gui for config application

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
		Label title = new Label("Welcome to TalkBox Configuration App");
		title.setScaleX(2);
		title.setScaleY(2);
		return title;
	}
	public HBox buildHBox(Stage primaryStage){
		HBox hb = new HBox();
		Slider s = buildSlider();
		TextField textField2 = new TextField();
		Button submit = new Button("Submit");
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
		Label label1 = new Label("Enter Number of Buttons:");
		Label label2 = new Label("Enter File Name:");
		hb.getChildren().addAll(label2, textField2,label1, s, submit);
		hb.setAlignment(Pos.BOTTOM_CENTER);
		hb.setSpacing(10);	
		return hb;
	}
	public void newButtonPrompt(Stage primaryStage) {   //GUI for initial prompt
		StackPane stackpane = new StackPane();
		Label title = buildTitle();
		HBox hb = buildHBox(primaryStage);
		stackpane.getChildren().addAll(title,hb);
		StackPane.setAlignment(title, Pos.CENTER);
		primaryStage.setTitle("TalkBox Config App");
		primaryStage.setScene(new Scene(stackpane, 900, 300)); //show the stackpane, 700x300 by default
		primaryStage.show();
	}
	
	public Slider buildSlider(){
		final Slider slider = new Slider(0, 18, 1);
        slider.setPrefWidth(250);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(6);
        slider.setBlockIncrement(1);
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                numTotalButtons = newValue.intValue();
            }
        });
        return slider;
		
	}

	public ToolBar buildToolbar() { // method which builds and returns a Toolbar
		Button x = new Button("Save"); // save button
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
		buttons = new AudioButton[numTotalButtons];
		inc = 0;
		int increment = 0;
		GridPane gridpane = new GridPane();
		gridpane.setPrefSize(500, 500);
		gridpane.setVgap(10);
		gridpane.setHgap(10);
		Image img = new Image(new FileInputStream("src/resources/plusSign.JPG"));		//simple + sign image
		for (int i = 0; i < numTotalButtons; i++) {
			if (i > 0 && i % 6 == 0) {	
				increment++; //incrementer to define number of rows
			}
			ImageView iv1 = new ImageView();
			iv1.setImage(img);
			iv1.setFitWidth(100);
			iv1.setPreserveRatio(true);
			iv1.setSmooth(true);
			iv1.setCache(true);
			Button edit = new Button("Edit Button:");
			edit.setPrefSize(100, 20);
			edit.setOnAction(new EventHandler<ActionEvent>() { //event handler for "edit" button allowing user to edit buttons
				@Override
				public void handle(ActionEvent event) {
					Stage x = new Stage();
					GridPane g = Utilities.setEditPrompt(x);
					Button submit = new Button("Submit");
					TextField textField = new TextField();
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
								numSetButtons++;
								edit.setText(textField.getText());	//sets text of button to user input
								Image n = new Image(new FileInputStream(Utilities.ImagePath)); //sets imagePath to user input
								iv1.setImage(n);
								iv1.setFitWidth(100);
								iv1.setPreserveRatio(true);
								iv1.setSmooth(true);
								iv1.setCache(true);
								buttons[inc++ % numSetButtons] = new AudioButton(textField.getText(),Utilities.AudioPath,Utilities.ImagePath); //assigns current input to an audiobutton
								iv1.setOnMouseClicked(new EventHandler<MouseEvent>() {	//adds event handler to picture, allowing user click to generate sound
									public void handle(MouseEvent me) {
										File f = new File(Utilities.AudioPath);
										URI u = f.toURI();
										Media sound = new Media(u.toString());
										MediaPlayer mediaPlayer = new MediaPlayer(sound);
										mediaPlayer.play();
									}
								});

							} catch (FileNotFoundException e1) {
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
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
		Button play = playButton(new Stage()); //adds playButton to GUI
		StackPane a = new StackPane();
		a.getChildren().addAll(gridpane, play, bar);	//adds all elements to a stackpane
		StackPane.setAlignment(bar, Pos.TOP_LEFT);		
		StackPane.setAlignment(play, Pos.BOTTOM_RIGHT);
		StackPane.setAlignment(gridpane, Pos.BOTTOM_CENTER);
		primaryStage.setScene(new Scene(a));
		primaryStage.show();
	}

	public Button playButton(Stage primaryStage) throws IOException {	//method which builds "play" button
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		return play;

	}

	public void saveFile(AudioButton[] b) throws IOException {		//simple method to save a file
		FileWrite a = new FileWrite(new File(filename + ".txt"));	//create new txt file
		FileWrite.fileCreate(filename + ".txt");
		a.fileAppend(numSetButtons + ""); //append numbuttons
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
		newButtonPrompt(primaryStage);
		// System.out.println(numButtonReturn());
		// loadImage(primaryStage);
	}

}
