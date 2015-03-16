package edu.unlv.searchengine.context;

import java.util.HashMap;

public class WordsIndexDictionary extends HashMap<String, Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Integer index = 1;
	
	public Integer  addWord(String word) {
		if (!this.containsKey(word)) {
			this.put(word, index);
			return this.index++;
		}
		return this.get(word);
	}
	
	

}
