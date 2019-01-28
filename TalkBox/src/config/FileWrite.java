package config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileWrite {
	
	public File file;
	
	public FileWrite(File file){
		this.file = file;
	}
	
	public void fileAppend(String s) throws IOException{
		FileWriter fw = new FileWriter(file.getAbsolutePath(), true);
		fw.write(s + "\n");
		fw.close();
	}
	
	public static void fileCreate(String filename) throws IOException{
		FileWriter fw = new FileWriter(new File(filename + ".txt"),false);
		fw.close();
	}

}