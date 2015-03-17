package edu.unlv.searchengine.util;


import java.util.ArrayList;
import java.util.List;

import edu.unlv.searchengine.context.WordsIndexDictionary;
import edu.unlv.searchengine.model.IntermediateInvertedIndex;
import edu.unlv.searchengine.util.io.IntermediateInvertedIndexFileReader;
import edu.unlv.searchengine.util.io.IntermediateInvertedIndexFileWriter;

public class InvertedIndexGenerator {
	
	public static final String INVERTED_INDEX_FILE_PATH = "inverted_index_file.txt";
	
	private IntermediateInvertedIndexFileReader fileReader;
	private IntermediateInvertedIndexFileWriter fileWriter;
	
	public InvertedIndexGenerator() {
		this.fileReader = new IntermediateInvertedIndexFileReader(IntermediateInvertedIndexFileWriter.SORTED_INTERMEDIATE_INVERTED_INDEX_FILE_PATH);
		this.fileWriter = new IntermediateInvertedIndexFileWriter(InvertedIndexGenerator.INVERTED_INDEX_FILE_PATH);
	}
	
	public void process() {
		System.out.println("==> Generating Inverted Index file");
		String line;
		List<IntermediateInvertedIndex> wordLocations = new ArrayList<IntermediateInvertedIndex>();
		int previousWordId = -1;
		while ( (line = this.fileReader.getNextLine()) != null ) {
			IntermediateInvertedIndex indexEntry = new IntermediateInvertedIndex(line);
			if (previousWordId == -1) {
				previousWordId = indexEntry.getWordId();
			} else if ( previousWordId == indexEntry.getWordId() ) {
				wordLocations.add(indexEntry);
			} else {
				//Write to file the info for current word
				this.writeToFile(previousWordId, wordLocations);
				wordLocations = new ArrayList<IntermediateInvertedIndex>();
				wordLocations.add(indexEntry);
				previousWordId = indexEntry.getWordId();
			}
		}
		this.writeToFile(previousWordId, wordLocations);
		
		this.fileWriter.close();
	}
	
	private void writeToFile(int wordId, List<IntermediateInvertedIndex> wordLocations) {
		StringBuilder sb = new StringBuilder("'")
								.append(WordsIndexDictionary.getWord(wordId))
								.append("'=>[");//open all
								
		sb.append(wordLocations.size());
		sb.append(",[");//open list of locations
		
		for (IntermediateInvertedIndex intermediateInvertedIndex : wordLocations) {
			sb.append("["); // open a location
			sb.append(intermediateInvertedIndex.getDocId());
			sb.append(",");
			sb.append(intermediateInvertedIndex.getFrequency());
			sb.append("]"); // close a location
		}
		sb.append("]");// close list of locations
		sb.append("]");//close all
		
		this.fileWriter.writeLine(sb.toString());
	}

}
