package edu.unlv.searchengine.context;

import java.util.ArrayList;
import java.util.List;

import edu.unlv.searchengine.util.io.SEFileReader;

public class StopWordsLoader {
	
	private static List<String> STOP_WORDS;
	private static String STOP_WORDS_FILE = "stopwords";
	
	
	private static void loadStopWords() {
		StopWordsLoader.STOP_WORDS = new ArrayList<String>();
		
		SEFileReader fileReader = new SEFileReader(STOP_WORDS_FILE);
		String line;
		while( (line = fileReader.getNextLine()) != null) {
			StopWordsLoader.STOP_WORDS.add(line);
		}
		fileReader.close();
	}
	
	public static List<String> getStopWords() {
		if ( StopWordsLoader.STOP_WORDS == null) {
			loadStopWords();
		}
		return StopWordsLoader.STOP_WORDS;
	}
	
	public static void clear() {
		StopWordsLoader.STOP_WORDS.clear();
	}

}
