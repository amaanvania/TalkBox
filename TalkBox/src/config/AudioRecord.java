package config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class AudioRecord extends Application {

	public boolean stopped;
	volatile boolean recording = false;
	public String filepath;

	public static void main(String[] args) throws LineUnavailableException, InterruptedException {
		launch(args);

	}
	public ImageView buildRecordImage() throws FileNotFoundException {
		String s = getClass().getResource("/resources/recordbutton.png").toExternalForm();
		Image img = new Image(s);
		ImageView iv1 = new ImageView();
		iv1.setImage(img);
		iv1.setFitWidth(120);
		iv1.setPreserveRatio(true);
		iv1.setSmooth(true);
		iv1.setCache(true);
		return iv1;
	}
	public ImageView buildStopImage() throws FileNotFoundException {
		String s = getClass().getResource("/resources/stopbutton.png").toExternalForm();
		Image img = new Image(s);
		ImageView iv1 = new ImageView();
		iv1.setImage(img);
		iv1.setFitWidth(120);
		iv1.setPreserveRatio(true);
		iv1.setSmooth(true);
		iv1.setCache(true);
		return iv1;
	}
	public ImageView buildRunButton(){
		String s = getClass().getResource("/resources/runbutton.png").toExternalForm();
		Image img = new Image(s);
		ImageView iv1 = new ImageView();
		iv1.setImage(img);
		iv1.setFitWidth(120);
		iv1.setPreserveRatio(true);
		iv1.setSmooth(true);
		iv1.setCache(true);
		return iv1;
	}
	public void runHandle(){
			File f = new File(filepath);
			Media sound = new Media(f.toURI().toString());
			MediaPlayer mediaPlayer = new MediaPlayer(sound);
			mediaPlayer.play();
	}
	public HBox buildButtons() throws FileNotFoundException {
		HBox box = new HBox();
		ImageView record = buildRecordImage();
		ImageView stop = buildStopImage();
		ImageView run = buildRunButton();
		run.setOnMouseClicked(e -> runHandle());
		record.setDisable(false);
		stop.setDisable(true);
		run.setDisable(true);
		run.setOpacity(0);
		stop.setOpacity(0);
		stop.setOnMouseClicked(e -> {
			this.recording = false;
			stop.setOpacity(0);
			record.setOpacity(100);
			run.setOpacity(100);
			stop.setDisable(true);
			run.setDisable(false);
			record.setDisable(false);
		});
		stop.setStyle("-fx-background-radius: 150em; " + "-fx-min-width: 120px; " + "-fx-min-height: 120px; "
				+ "-fx-max-width: 120px; " + "-fx-max-height: 120px;");
		record.setStyle("-fx-background-radius: 150em; " + "-fx-min-width: 120px; " + "-fx-min-height: 120px; "
				+ "-fx-max-width: 120px; " + "-fx-max-height: 120px;");
		record.setOnMouseClicked(e -> {
			filepath = Utilities.recordFileSave(new Stage()).getAbsolutePath();
			this.recording = true;
			recordAudio();
			record.setOpacity(0);
			stop.setOpacity(100);
			run.setOpacity(0);
			stop.setDisable(false);
			record.setDisable(true);
			run.setDisable(true);
		});
		box.getChildren().addAll(record,stop,run);
		return box;
	}
	public void recordAudio(){
		Runnable runnable = () -> {
			try {
				recordAudio(filepath);
			} catch (LineUnavailableException | InterruptedException e1) {
				e1.printStackTrace();
			}
		};
		Thread t = new Thread(runnable);
		t.start();
	}
	public void handleButton() throws LineUnavailableException {

	}
	public void alertHandle(){
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning!");
		alert.setHeaderText("Warning! No microphone");
		alert.setContentText("Connect microphone to record");
		alert.showAndWait();
	}

	public void recordAudio(String filePath) throws LineUnavailableException, InterruptedException {
		AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
		if (!AudioSystem.isLineSupported(info)) {
			alertHandle();
			return;
		}
		final TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
		targetDataLine.open();
		targetDataLine.start();
		Runnable runnable = () -> {
			AudioInputStream audioStream = new AudioInputStream(targetDataLine);
			File wavFile = new File(filePath);
			try {
				AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, wavFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		};
		Thread stopper = new Thread(runnable);
		stopper.start();
		while (recording)
			Thread.sleep(100);
		targetDataLine.stop();
		targetDataLine.close();
	}

	public FlowPane buildFlowPane(Stage primaryStage) throws FileNotFoundException {
		FlowPane f = new FlowPane(200, 200);
		HBox b = buildButtons();
		f.setVgap(50);
		f.setHgap(500);
		f.setMaxWidth(300);
		f.getChildren().addAll(b);
		f.setAlignment(Pos.CENTER);
		return f;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new Scene(buildFlowPane(primaryStage)));
		primaryStage.show();
	}

}
