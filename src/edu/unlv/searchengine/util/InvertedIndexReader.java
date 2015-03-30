package edu.unlv.searchengine.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import edu.unlv.searchengine.util.io.SEFileReader;

public class InvertedIndexReader {
	
	private SEFileReader fileReader;
	
	public InvertedIndexReader() {
		this.fileReader = new SEFileReader(InvertedIndexGenerator.INVERTED_INDEX_FILE_PATH);
	}
	
	public Map<String, List<Long>> constructInvertedIndex() {
		Map<String, List<Long>> invertedIndex = new HashMap<String, List<Long>>();
		String line;
		fileReader.getNextLine(); // ignore first line INVERTED_FILE_HASH={
		while ( (line = fileReader.getNextLine()) != null ) {
			List<Long> documentIDs = new ArrayList<Long>();
			String[] components = line.trim().split("=>");
			
			String key = components[0];
			if (components.length == 1) {
				break;
			}
			JSONArray docs = (JSONArray) ((JSONArray) JSONValue.parse(components[1])).get(1);
			for (Object object : docs) {
				JSONArray doc = (JSONArray) object;
				documentIDs.add((Long)doc.get(0)); // this is required docid
			}
			invertedIndex.put(key.substring(1, key.length()-1), documentIDs);
		}
		
		return invertedIndex;
	}

}
