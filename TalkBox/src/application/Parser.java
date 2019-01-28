package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import config.AudioButton;

public class Parser {

	public AudioButton[] buttons;
	public int numButtons;

	public Parser(File file) throws IOException {
		buttons = new AudioButton[100];
		BufferedReader bufRead = new BufferedReader(new FileReader(file));
		String myLine = null;
		List<String[]> arrayList = new ArrayList<>();
		while ((myLine = bufRead.readLine()) != null) {
			String[] vals = myLine.split("\n");
			arrayList.add(vals);
		}
		bufRead.close();
		numButtons = Integer.parseInt(arrayList.get(0)[0]);
		int lineNum = 0;
		String combinedString = "";
		String[] lines = new String[numButtons * 3 + 10];
		for (String[] currLine : arrayList) {
			for (String currString : currLine) {
				combinedString += currString;
			}
			lines[lineNum] = combinedString;
		   combinedString = "";
		   lineNum++;
		}
		for(int i = 1; i < numButtons * 3; i+=3){
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
