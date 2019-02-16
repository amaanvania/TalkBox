package config;

import java.io.Serializable;

public class AudioButton implements Serializable{		//class that defines AudioButton object
								//which contains a name, audiopath, and imagepath
	
	public String name;
	public String AudioPath;
	public String ImagePath;
	
	public AudioButton() {
		this.name = "";
		this.AudioPath = "";
		this.ImagePath = "";
	}
	
	public AudioButton(String name, String AudioPath, String ImagePath) {
		this.name = name;
		this.AudioPath = AudioPath;
		this.ImagePath = ImagePath;
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
		this.AudioPath = Utilities.fileChoose(null); //prompt for filechooser
	}
	
	public void chooseImagePath() {
		this.ImagePath = Utilities.fileChoose(null); //prompt for filechooser
	}
	
	
	
	
}
