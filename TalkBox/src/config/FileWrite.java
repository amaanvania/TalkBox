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
		FileWriter fw = new FileWriter(file.getAbsolutePath(), false);
		fw.write(s);
		fw.close();
	}
	
	public static void fileCreate(String filename,String msg) throws IOException{
		FileWriter fw = new FileWriter(new File("src/resources/" + filename + ".txt"),true);
		fw.write(msg);
		fw.close();
	}

}