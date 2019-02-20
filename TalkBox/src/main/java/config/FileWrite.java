package main.java.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileWrite {	//simple class to help with file writing
	
	public File file;
	
	public FileWrite(File file){
		this.file = file;
	}
	
	public void fileAppend(String s) throws IOException{ 	//appends string s to end of file
		FileWriter fw = new FileWriter(file.getAbsolutePath(), true);
		fw.write(s + "\n");
		fw.close();
	}
	
	public static void fileCreate(String filename) throws IOException{	//creates new file in directory
		FileWriter fw = new FileWriter(new File(filename),false);
		fw.close();
	}

}