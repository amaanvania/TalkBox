package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import config.AudioButton;

public class Parser { //class which parses txt files for configFile format

	public AudioButton[] buttons;
	public int numButtons;

	public Parser(File file) throws IOException {
		buttons = new AudioButton[100]; //max 100 buttons
		BufferedReader bufRead = new BufferedReader(new FileReader(file)); //buffered reader on file
		String myLine = null;
		List<String[]> arrayList = new ArrayList<>(); //arrayList of string[]
		while ((myLine = bufRead.readLine()) != null) { //read every line
			String[] vals = myLine.split("\n"); //split by new line
			arrayList.add(vals);	//add values to an arraylist
		}
		bufRead.close();
		numButtons = Integer.parseInt(arrayList.get(0)[0]); //number of buttons will be first line
		int lineNum = 0;
		String combinedString = "";
		String[] lines = new String[numButtons * 3 + 10]; 
		for (String[] currLine : arrayList) { //turn array list of String[] to just String[]
			for (String currString : currLine) {
				combinedString += currString;
			}
			lines[lineNum] = combinedString;
		   combinedString = "";
		   lineNum++;
		}
		for(int i = 1; i < numButtons * 3; i+=3){ //assign Each 3 lines to a button
			AudioButton b = new AudioButton();
			b.setName(lines[i]);
			b.setImagePath(lines[i+1]);
			b.setAudioPath(lines[i+2]);
			this.buttons[i / 3] = b;
		}
		

	}
	
	public AudioButton[] getAudioButtons(){
		return buttons;
	}
}
