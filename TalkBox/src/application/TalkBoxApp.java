package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;

import config.AudioButton;
import config.Builder;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class TalkBoxApp{	//class which builds GUI for talkbox application
							//similar to config gui
	
	Image[] images;
	String[] names;
	Media[] audioFiles;
	private GridPane gridpane;
	
	public TalkBoxApp(Builder a) throws IOException{
		names = new String[50];
		images = new Image[50];
		audioFiles = new Media[50];
		setGridpane(new GridPane());
		getGridpane().setPrefSize(500, 500);
		getGridpane().setVgap(10);
		getGridpane().setHgap(10);
		int increment = 0;
		for(int i = 0; i < a.getSetButtons(); i++){
			if(i > 0 && i % 6 == 0){
				increment++;
			}
			AudioButton b = a.buttons[i];
			names[i] = b.getName();
			TextField textField = new TextField();
			textField.setText(b.getName());
			images[i] = new Image(new FileInputStream(b.ImagePath));
			ImageView iv1 = new ImageView();
			iv1.setImage(new Image(new FileInputStream(b.ImagePath)));
			iv1.setFitWidth(100);
			iv1.setPreserveRatio(true);
			iv1.setSmooth(true);
			iv1.setCache(true);
			iv1.setOnMouseClicked(new EventHandler<MouseEvent>() {
			    public void handle(MouseEvent me) {
					File f = new File(b.getAudioPath());
					URI u = f.toURI();
					Media sound = new Media(u.toString());
					MediaPlayer mediaPlayer = new MediaPlayer(sound);
			        mediaPlayer.play();
			    }
			});
			GridPane.setConstraints(textField, i % 6, 2 + 2 * increment);
			GridPane.setConstraints(iv1, i % 6, 1 + 2 * increment);
			getGridpane().getChildren().addAll(iv1,textField);
		}
	}

	public int getNumButtons(){
		return 0;
		
	}
	
	public Image[] getImages(){
		return images;
		
	}
	
	public String[] getNames(){
		return names;
	}
	
	public Media[] getAudioFiles(){
		return audioFiles;
	}
	
	public GridPane buildApp(){
		int increment = 0;
		GridPane gridpane = new GridPane();
		gridpane.setPrefSize(500, 500);
		gridpane.setVgap(10);
		gridpane.setHgap(10);
		for(int i = 0; i < getNumButtons(); i++){
			if(i > 0 && i % 6 == 0){
				increment++;
			}
			Image img = images[i];
			System.out.println(names[i]);
			TextField textField = new TextField();
			textField.setText(names[i]);
			ImageView iv1 = new ImageView();
			iv1.setImage(img);
			iv1.setFitWidth(100);
			iv1.setPreserveRatio(true);
			iv1.setSmooth(true);
			iv1.setCache(true);
			GridPane.setConstraints(textField, i % 6, 2 + 2 * increment);
			GridPane.setConstraints(iv1, i % 6, 1 + 2 * increment);
			gridpane.getChildren().addAll(iv1,textField);
		}
		
		return gridpane;
		
	}

	public GridPane getGridpane() {
		return gridpane;
	}

	public void setGridpane(GridPane gridpane) {
		this.gridpane = gridpane;
	}

}
