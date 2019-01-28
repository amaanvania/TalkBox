package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class Builder extends Application{
	
	public int numButtons;
	
	public static void main(String[] args) {
		launch(args);
		
		
	}
	
	public void newButtonPrompt(Stage primaryStage) {
		
		StackPane stackpane = new StackPane();
		Label title = new Label("Welcome to TalkBox Configuration App");
		title.setScaleX(2);
		title.setScaleY(2);
		Button submit = new Button("Submit");
		Label label1 = new Label("Enter Number of Buttons:");
		TextField textField = new TextField ();
		HBox hb = new HBox();
		hb.getChildren().addAll(label1,textField,submit);
		hb.setAlignment(Pos.BOTTOM_CENTER);
		hb.setSpacing(10);
		stackpane.getChildren().addAll(title,hb);
		StackPane.setAlignment(title, Pos.CENTER);
		primaryStage.setTitle("TalkBox Config App");
        primaryStage.setScene(new Scene(stackpane,500,500));
        primaryStage.show();
        submit.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	    public void handle(ActionEvent e) {
        	        if ((textField.getText() != null && !textField.getText().isEmpty())) {
        	            numButtons = Integer.parseInt(textField.getText());
        	            primaryStage.close();
        	            try {
							buildInitialGui(primaryStage);
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
        	        }
        	     }
        	 });
	}
	
	public int numButtonReturn() {
		return numButtons;
	}
	
	public ToolBar buildToolbar(){
		 ToolBar toolBar = new ToolBar(
			     new Button("Open")
			 );
		 toolBar.setPrefSize(200, 20);
		 return toolBar;
	}
	
	public void buildInitialGui(Stage primaryStage) throws FileNotFoundException {
		int increment = 0;
		GridPane gridpane = new GridPane();
		gridpane.setPrefSize(500, 500);
		gridpane.setVgap(10);
		gridpane.setHgap(10);
		Image img = new Image(new FileInputStream("src/resources/plusSign.JPG"));
		for(int i = 0; i < numButtons; i++){
			if(i > 0 && i % 6 == 0){
				increment++;
			}
			//AudioButton y = new AudioButton();
			ImageView iv1 = new ImageView();
			iv1.setImage(img);
			iv1.setFitWidth(100);
			iv1.setPreserveRatio(true);
			iv1.setSmooth(true);
			iv1.setCache(true);
			Button edit = new Button("Edit Button:");
			edit.setPrefSize(100, 20);
			edit.setOnAction(new EventHandler<ActionEvent>(){
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
					submit.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent e) {;
								try {
									edit.setText(textField.getText());
									Image n = new Image(new FileInputStream(Utilities.ImagePath));
									iv1.setImage(n);
									iv1.setFitWidth(100);
									iv1.setPreserveRatio(true);
									iv1.setSmooth(true);
									iv1.setCache(true);
									File f = new File(Utilities.AudioPath);
									URI u = f.toURI();
									Media sound = new Media(u.toString());
									MediaPlayer mediaPlayer = new MediaPlayer(sound);
									iv1.setOnMouseClicked(new EventHandler<MouseEvent>() {
									    public void handle(MouseEvent me) {
									        mediaPlayer.play();
									    }
									});
								} catch (FileNotFoundException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								x.close();
							}
						
					});
					
					/**
					try {
						Image n = new Image(new FileInputStream(Utilities.ImagePath));
						iv1.setImage(n);
						iv1.setFitWidth(100);
						iv1.setPreserveRatio(true);
						iv1.setSmooth(true);
						iv1.setCache(true);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					*/
					
				}
			});
			GridPane.setConstraints(edit, i % 6, 2 + 2 * increment);
			GridPane.setConstraints(iv1, i % 6, 1 + 2 * increment);
			gridpane.getChildren().addAll(iv1,edit);
		}
		//ToolBar bar = buildToolbar();
		//gridpane.getChildren().add(bar);
		//GridPane.setConstraints(bar, 0, 0);
		primaryStage.setScene(new Scene(gridpane));
		primaryStage.show();
	}
	
	public void updateGui(Stage primaryStage) {
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		newButtonPrompt(primaryStage);
		//System.out.println(numButtonReturn());
		//loadImage(primaryStage);
	}


}
