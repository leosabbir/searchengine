package edu.unlv.searchengine.context;

import java.util.HashMap;
import java.util.Map;

public class WordsIndexDictionary {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Integer index = 1;
	private static Map<String, Integer> dictionary = new HashMap<String, Integer>();
	private static Map<Integer, String> backdictionary = new HashMap<Integer, String>();
	
	public static Integer addWord(String word) {
		if (!dictionary.containsKey(word)) {
			dictionary.put(word, index);
			backdictionary.put(index, word);
			return index++;
		}
		return dictionary.get(word);
	}
	
	public static String getWord(int wordId) {
		return backdictionary.get(wordId);
	}
	
	

}
