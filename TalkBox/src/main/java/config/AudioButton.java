package main.java.config;

import java.io.Serializable;

import javafx.stage.Stage;

/**
 * This class consists of non-static method and fields that creates the AudioButton object used
 * in making the TalkBox.
 * @author Nasif, Amaan, Tony
 *
 */
public class AudioButton implements Serializable{		//class that defines AudioButton object
								/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	//which contains a name, audiopath, and imagepath
	/**
	 * name for the AudioButton
	 */
	public String name;
	/**
	 * path for the audio file location
	 */
	public String AudioPath;
	/**
	 * path for the image file location
	 */
	public String ImagePath;
	
	/**
	 * Constructs an empty AudioButton
	 */
	public AudioButton() {
		this.name = "";
		this.AudioPath = "";
		this.ImagePath = "";
	}
	
	/**
	 * Constructs an AudioButton with defined name, AudioPath, and ImagePath 
	 * 
	 * @param name name of AudioButton
	 * @param AudioPath path of the audio file
	 * @param ImagePath path of the image
	 */
	public AudioButton(String name, String AudioPath, String ImagePath) {
		this.name = name;
		this.AudioPath = AudioPath;
		this.ImagePath = ImagePath;
	}
	
	/**
	 * Returns the name of the AudioButton
	 * 
	 * @return the name of the AudioButton
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Returns the path of the audio file
	 * 
	 * @return the path of the audio file
	 */
	public String getAudioPath() {
		return this.AudioPath;
	}
	
	/**
	 * Returns current path of the image
	 * 
	 * @return path of the image
	 */
	public String getImagePath() {
		return this.ImagePath;
	}
	
	/**
	 * Replaces the name of this AudioButton
	 * 
	 * @param name new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Replaces the audio file path of this AudioButton
	 * 
	 * @param AudioPath new path of audio file
	 */
	public void setAudioPath(String AudioPath) {
		this.AudioPath = AudioPath;
	}
	
	/**
	 * Replaces the image path of this AudioButton
	 * 
	 * @param ImagePath new path of image file
	 */
	public void setImagePath(String ImagePath) {
		this.ImagePath = ImagePath;
	}
	
	/**
	 * Prompt for {@link Utilities#fileChoose(Stage mainStage) fileChoose} method found in utilities
	 * 
	 * @see Utilities Utilities.java
	 */
	public void chooseAudioPath() {
		this.AudioPath = Utilities.fileChoose(null, true);
	}
	
	/**
	 * Prompt for {@link Utilities#fileChoose(Stage mainStage) fileChoose} method found in utilities
	 * 
	 * @see Utilities Utilities.java
	 */
	public void chooseImagePath() {
		this.ImagePath = Utilities.fileChoose(null, false); 
	}
	
	
	
	
}
