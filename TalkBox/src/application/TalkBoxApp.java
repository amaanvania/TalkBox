package application;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import config.AudioButton;
import config.Builder;
import config.Utilities;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class TalkBoxApp extends Application {
	// class which builds GUI for talkbox application
	// similar to config gui

	Image[] images;
	String[] names;
	Media[] audioFiles;
	private BorderPane appPane;
	private VBox audioCard;
	private FlowPane cardFlow;
	private Button play;
	private ImageView imgv;
	private FlowPane mainFlow;
	public static void main(String[] args) {
		launch(args);
	}

	public TalkBoxApp() {

	}

	public TalkBoxApp(Builder builder) throws IOException {
		names = new String[50];
		images = new Image[50];
		audioFiles = new Media[50];

		appPane = new BorderPane();
		mainFlow = new FlowPane();
		MenuBar topMenu = builder.buildTopMenu();
		appPane.setCenter(mainFlow);
		appPane.setTop(topMenu);
		audioCard = new VBox();
		audioCard.setPrefWidth(150);
		audioCard.setPrefHeight(200);
		cardFlow = new FlowPane();
		cardFlow.setOrientation(Orientation.VERTICAL);
//		setPane(new GridPane());
//		getPane().setPrefSize(500, 500);
//		getPane().setVgap(10);
//		getPane().setHgap(10);
		for (int i = 0; i < builder.getSetButtons(); i++) {
//			if (i > 0 && i % 6 == 0) {
//				increment++;
//			}
			AudioButton button = builder.buttons[i];
			names[i] = button.getName();
//			TextField textField = new TextField();
//			textField.setText(button.getName());
			play = new Button(button.getName());
			play.setPrefWidth(149);
			play.setPrefHeight(74);
			play.setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent me) {
					File f = new File(button.getAudioPath());
					URI u = f.toURI();
					Media sound = new Media(u.toString());
					MediaPlayer mediaPlayer = new MediaPlayer(sound);
					mediaPlayer.setVolume(builder.volume);
					mediaPlayer.play();
				}
			});
			images[i] = new Image(new FileInputStream(button.ImagePath));
			imgv = new ImageView();
			imgv.setImage(new Image(new FileInputStream(button.ImagePath)));
			imgv.setFitWidth(149);
			imgv.setFitHeight(124);
			imgv.setPreserveRatio(true);
			imgv.setSmooth(true);
			imgv.setCache(true);
//			GridPane.setConstraints(textField, i % 6, 2 + 2 * increment);
//			GridPane.setConstraints(iv1, i % 6, 1 + 2 * increment);
//			getPane().getChildren().addAll(imgv, textField);
			cardFlow.getChildren().addAll(imgv, play);
		}
		audioCard.getChildren().add(cardFlow);
		mainFlow.getChildren().add(audioCard);
	}

	public int getNumButtons() {
		return 0;

	}

	public Image[] getImages() {
		return images;

	}

	public String[] getNames() {
		return names;
	}

	public Media[] getAudioFiles() {
		return audioFiles;
	}

//	public GridPane buildApp() {
//		int increment = 0;
//		GridPane gridpane = new GridPane();
//		gridpane.setPrefSize(500, 500);
//		gridpane.setVgap(10);
//		gridpane.setHgap(10);
//		for (int i = 0; i < getNumButtons(); i++) {
//			if (i > 0 && i % 6 == 0) {
//				increment++;
//			}
//			Image img = images[i];
//			System.out.println(names[i]);
//			TextField textField = new TextField();
//			textField.setText(names[i]);
//			ImageView iv1 = new ImageView();
//			iv1.setImage(img);
//			iv1.setFitWidth(100);
//			iv1.setPreserveRatio(true);
//			iv1.setSmooth(true);
//			iv1.setCache(true);
////			GridPane.setConstraints(textField, i % 6, 2 + 2 * increment);
////			GridPane.setConstraints(iv1, i % 6, 1 + 2 * increment);
//			gridpane.getChildren().addAll(iv1, textField);
//		}
//
//		return gridpane;
//
//	}

	public BorderPane getPane() {
		return appPane;
	}

	public void setPane(BorderPane mainPane) {
		this.appPane = mainPane;
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/resources/WelcomeScreen.fxml"));
			Scene scene = new Scene(root, 900, 650);
			scene.getStylesheets().add(getClass().getResource("/resources/WelcomeScreen.css").toExternalForm());
			primaryStage.setTitle("TalkBox App");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void createNewTalkFileButton() {
		try {
			Builder config = new Builder();
			Stage stage = new Stage();
			FlowPane fp = config.buildFlowPane(stage);
			Scene scene = new Scene(fp, 310, 440);
			stage.setScene(scene);
			stage.show();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void openExistingTalkFileButton() {
			try {
				Builder b = Builder.openSerializedFile(Utilities.configFileChoose(new Stage()));
				TalkBoxApp a = new TalkBoxApp(b);
				BorderPane c = a.getPane();
				Stage primaryStage = new Stage();
				primaryStage.setTitle("TalkBox Application");
				Scene scene = new Scene (c, 900, 650);
				String css = this.getClass().getResource("/resources/buttonstyle.css").toExternalForm();
				scene.getStylesheets().add(css);
				primaryStage.setScene(scene);
				primaryStage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	@FXML
	public void helpButton() {
		try {
//			Desktop.getDesktop().browse(new URI(
//					"https://github.com/amaanvania/TalkBox/blob/master/Documentation/Talk%20Box%20User%20Manual.pdf"));
			Desktop.getDesktop().browse(new URI(
					"https://github.com/amaanvania/TalkBox/blob/master/Documentation"));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void githubLink() {
		try {
//			Desktop.getDesktop().browse(new URI(
//					"https://github.com/amaanvania/TalkBox/blob/master/Documentation/Talk%20Box%20User%20Manual.pdf"));
			Desktop.getDesktop().browse(new URI(
					"https://github.com/amaanvania/TalkBox"));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

}
