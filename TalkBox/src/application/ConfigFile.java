package application;

import java.io.File;

public class ConfigFile { //class which parses configFiles to check if they are
						//legitimate configFiles
	public File file;
	
	public ConfigFile(File file){
		this.file = file;
	}
	//todo
	public boolean isConfigFile(){
		return true;
	}
	
	

}
