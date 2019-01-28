package config;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import javafx.stage.Stage;

public class Builder extends Application{
	
	public int numButtons;
	
	public static void main(String[] args) {
		launch(args);
		
		
	}
	
	public void newButtonPrompt(Stage primaryStage) {
		Button submit = new Button("Submit");
		Label label1 = new Label("Enter Number of Buttons:");
		TextField textField = new TextField ();
		HBox hb = new HBox();
		hb.getChildren().addAll(label1, textField,submit);
		hb.setSpacing(10);
        primaryStage.setScene(new Scene(hb));
        primaryStage.show();
        submit.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	    public void handle(ActionEvent e) {
        	        if ((textField.getText() != null && !textField.getText().isEmpty())) {
        	            numButtons = Integer.parseInt(textField.getText());
        	            primaryStage.close();
        	            buildInitialGui(primaryStage);
        	        }
        	     }
        	 });
	}
	
	public int numButtonReturn() {
		return numButtons;
	}
	
	public void buildInitialGui(Stage primaryStage) {
		HBox hb = new HBox();
		for(int i = 0; i < numButtons; i++){
			AudioButton y = new AudioButton();
			Button x = new Button("Button #:" + i);
			x.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent event) {
					y.chooseImagePath();
					
				}
			});
			hb.getChildren().addAll(x);
			hb.setSpacing(10);
		}
		primaryStage.setScene(new Scene(hb));
		primaryStage.show();
	}
	
	public void updateGui(Stage primaryStage) {
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		newButtonPrompt(primaryStage);
		//System.out.println(numButtonReturn());
		//AudioButton x = new AudioButton();
		//x.chooseAudioPath();
		//System.out.println(x.getAudioPath());
		
	}


}
