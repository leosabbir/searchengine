package edu.unlv.searchengine.util;

import edu.unlv.searchengine.context.WordsIndexDictionary;
import edu.unlv.searchengine.util.io.IntermediateInvertedIndexFileWriter;
import edu.unlv.searchengine.util.io.WordsCountFileReader;

public class WordsCountFileProcessor {
	
	private WordsCountFileReader fileReader;
	private IntermediateInvertedIndexFileWriter fileWriter;
	
	public WordsCountFileProcessor() {
		this.fileReader = new WordsCountFileReader();
		this.fileWriter = new IntermediateInvertedIndexFileWriter();
	}
	
	public void process() {
		System.out.println("==> Processing word count file to generate intermediate inverted index file...\n");
		String line;
		Integer documentID = 0;
		while ( (line = this.fileReader.getNextLine()) != null ) {
			if ( line.startsWith("Document ID")) {
				this.fileReader.getNextLine(); 
				this.fileReader.getNextLine(); //ignore two lines afte Document ID:xx is read
				documentID = Integer.parseInt(line.split(":")[1]);
			} else {
				//one entry of a document from word count file
				String[] wordFrequencyPair = line.split(":");
				if (wordFrequencyPair.length == 1) {
					continue;
				}
				Integer wordIndex = WordsIndexDictionary.addWord(wordFrequencyPair[0]);
				this.fileWriter.writeLine(new StringBuilder().append(wordIndex).append(",").append(documentID).append(",").append(wordFrequencyPair[1]).toString());
			}
		}
		this.fileWriter.close();
	}

}
