package config;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.voicerss.tts.AudioCodec;
import com.voicerss.tts.AudioFormat;
import com.voicerss.tts.Languages;
import com.voicerss.tts.VoiceParameters;
import com.voicerss.tts.VoiceProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


public class WebDownloader {
	public static String audioPath = System.getProperty("user.home") + File.separatorChar + "TalkBoxData"
			+ File.separatorChar + "DownloadedAudio" + File.separatorChar;
	public static String ImagePath = System.getProperty("user.home") + File.separatorChar + "TalkBoxData"
			+ File.separatorChar + "DownloadedImages" + File.separatorChar;

	public static void downloadAutomatic(String topic) throws Exception{
		Boolean b = new File(audioPath).mkdirs();
		Boolean x = new File(ImagePath).mkdirs();
		downloadTTS(topic);
		downloadBestImage(topic);
	}
	public static void downloadBestImage(String topic) throws IOException {
		DownloadImage(bestMatch(getImages(topic), topic), ImagePath + topic + ".jpg");
	}

	public static List<String> getImages(String topic) {
		List<String> result = new ArrayList<String>();
		try {
			Document doc = Jsoup.connect("https://pixabay.com/images/search/" + topic + "/")
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
					.get();

			// Extract images from site
			Elements elemImages = doc.select("img[src$=.jpg]");

			for (Element e : elemImages)
				result.add(e.absUrl("src"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String bestMatch(List<String> r, String topic) {
		for (String s : r) {
			if (s.contains(topic))
				return s;
		}
		return r.get(0);
	}

	public static void DownloadImage(String search, String path) throws IOException {

		// This will get input data from the server
		InputStream inputStream = null;

		// This will read the data from the server;
		OutputStream outputStream = null;

		try {
			// This will open a socket from client to server
			URL url = new URL(search);

			// This user agent is for if the server wants real humans to visit
			String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";

			// This socket type will allow to set user_agent
			URLConnection con = url.openConnection();

			// Setting the user agent
			con.setRequestProperty("User-Agent", USER_AGENT);

			// Requesting input data from server
			inputStream = con.getInputStream();

			// Open local file writer
			outputStream = new FileOutputStream(path);

			// Limiting byte written to file per loop
			byte[] buffer = new byte[2048];

			// Increments file size
			int length;

			// Looping until server finishes
			while ((length = inputStream.read(buffer)) != -1) {
				// Writing data
				outputStream.write(buffer, 0, length);
			}
		} catch (Exception ex) {

		}

		// closing used resources
		// The computer will not be able to use the image
		// This is a must

		outputStream.close();
		inputStream.close();
	}

	public static void downloadTTS(String input) throws Exception {
		VoiceProvider tts = new VoiceProvider("1b8701f7a7424b24965bbc6428165b89");

		VoiceParameters params = new VoiceParameters(input, Languages.English_UnitedStates);
		params.setCodec(AudioCodec.WAV);
		params.setFormat(AudioFormat.Format_44KHZ.AF_44khz_16bit_stereo);
		params.setBase64(false);
		params.setSSML(false);
		params.setRate(0);

		byte[] voice = tts.speech(params);

		FileOutputStream fos = new FileOutputStream(audioPath+input+".wav");
		fos.write(voice, 0, voice.length);
		fos.flush();
		fos.close();
	}

}
