package main.java.config;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class AudioRecord{

	public boolean stopped;
	volatile boolean recording = false;
	public String filepath;

	/*
	 * Method which builds and returns
	 * the build button
	 */
	public Button buildButton(Stage primaryStage) {
		Button record = new Button("Record");
		record.setStyle("-fx-background-radius: 150em; " + "-fx-min-width: 120px; " + "-fx-min-height: 120px; "
		+ "-fx-max-width: 120px; " + "-fx-max-height: 120px;");
		record.setOnAction(e -> { 
		record.setText("Recording Audio");
		filepath = Utilities.recordFileSave(new Stage()).getAbsolutePath();
		this.recording = true;
		Runnable runnable = () -> {
			try {
				recordAudio(filepath);
			} catch (LineUnavailableException | InterruptedException e1) {
				e1.printStackTrace();
			}
		};
		Thread t = new Thread(runnable);
		t.start();
		record.setOnAction(e2 -> {
			record.setText("Done");
			this.recording = false;
			primaryStage.close();
		});
		});
		return record;
	}

	public void handleButton() throws LineUnavailableException {

	}

	/*
	 * Method which records Audio and sends it to
	 * filePath.
	 * Runs on a new thread
	 */
	public void recordAudio(String filePath) throws LineUnavailableException, InterruptedException {
		AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
		if (!AudioSystem.isLineSupported(info)) {
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
		Thread stopper = new Thread(runnable);
		stopper.start();
		while(recording)Thread.sleep(100);
		targetDataLine.stop();
		targetDataLine.close();
	}

	public FlowPane buildFlowPane(Stage primaryStage) {
		FlowPane f = new FlowPane(200, 200);
		Button b = buildButton(primaryStage);
		f.setVgap(50);
		f.setHgap(500);
		f.setMaxWidth(300);
		f.getChildren().addAll(b);
		f.setAlignment(Pos.CENTER);
		return f;
	}

}
