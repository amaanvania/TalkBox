package application;

import java.io.File;

public class ConfigFile {
	public File file;
	
	public ConfigFile(File file){
		this.file = file;
	}
	
	public boolean isConfigFile(){
		return true;
	}
	
	

}
