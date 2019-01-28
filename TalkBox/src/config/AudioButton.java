package config;

import javafx.scene.control.Button;

public class AudioButton {
	
	public String name;
	public String AudioPath;
	public String ImagePath;
	public Button button;
	
	public AudioButton() {
		this.name = "";
		this.AudioPath = "";
		this.ImagePath = "";
		this.button = null;
	}
	
	public AudioButton(String name, String AudioPath, String ImagePath, Button x) {
		this.name = name;
		this.AudioPath = AudioPath;
		this.ImagePath = ImagePath;
		this.button = x;
	}
	
	public String getName() {
		return this.name;
	}

	public String getAudioPath() {
		return this.AudioPath;
	}
	
	public String getImagePath() {
		return this.ImagePath;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setAudioPath(String AudioPath) {
		this.AudioPath = AudioPath;
	}
	
	public void setImagePath(String ImagePath) {
		this.ImagePath = ImagePath;
	}
	
	public void chooseAudioPath() {
		this.AudioPath = Utilities.fileChoose(null);
	}
	
	public void chooseImagePath() {
		this.ImagePath = Utilities.fileChoose(null);
	}
	
	
}
