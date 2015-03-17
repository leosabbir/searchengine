package edu.unlv.searchengine.util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TreeMap;

import edu.unlv.searchengine.context.StopWordsLoader;
import edu.unlv.searchengine.util.io.DocumentsReader;
import edu.unlv.searchengine.util.io.WordCounterFileWriter;

public class DocumentsProcessor {
	
	
	public void process() {
		System.out.println("==> Reading documents source file to generate word count file.....\n");
		DocumentsReader documentReader = new DocumentsReader();
		
		Map<String, Integer> wordsWithCount = null;
		
		String line;
		int documentID = 0;
		boolean content = false;
		WordCounterFileWriter fileWriter = new WordCounterFileWriter();
		while( (line = documentReader.getNextLine()) != null) {
			if (line.startsWith(".I")) {
				if (wordsWithCount != null) {
					this.writeDocumentToFile(wordsWithCount, documentID, fileWriter);
				}
				documentID = Integer.parseInt(line.substring(3));
				wordsWithCount = null;
				content = false;
			} else if (line.startsWith(".W")) {
				wordsWithCount = new TreeMap<String, Integer>();
				content = true;
				continue;
			} else if ( content ) {
				StringTokenizer t = new StringTokenizer(line, "[ \n\t\r.,;:!?(){}]'/");
				while (t.hasMoreTokens()) { // Process Words
					String word = t.nextToken();
					String[] subWords = word.split("-"); //break words like angel-the-demon
					if(subWords.length > 1) {
						for (String subWord : subWords) {
							processWord(wordsWithCount, subWord);
						}
					}
					processWord(wordsWithCount, word);
				}
			}
		}
		this.writeDocumentToFile(wordsWithCount, documentID, fileWriter);
		fileWriter.printFinalMessage();
		fileWriter.close();
	}

	private void writeDocumentToFile(Map<String, Integer> wordsWithCount, int documentID, WordCounterFileWriter fileWriter) {
		fileWriter.writeLine("======================================");
		System.out.println("*********Document ID: " + documentID + "...done");
		fileWriter.writeLine("Document ID:" + documentID);
		fileWriter.writeLine("Number of Unique Terms: " + wordsWithCount.size());
		fileWriter.writeLine("======================================");
		for (Entry<String, Integer> entry : wordsWithCount.entrySet()) {
			//System.out.println(entry.getKey() + " : " + entry.getValue());
			fileWriter.writeLine(entry.getKey() + ":" + entry.getValue());
		}
	}

	private void processWord(Map<String, Integer> wordsWithCount, String word) {
		SEStemmer stemmer = new SEStemmer();
		if (!word.isEmpty() && !StopWordsLoader.getStopWords().contains(word)) {
			word = stemmer.stem(word);
			if (wordsWithCount.containsKey(word)) {
				wordsWithCount.put(word, wordsWithCount.get(word) + 1);
			} else {
				wordsWithCount.put(word, 1);
			}
		}
	}

}
