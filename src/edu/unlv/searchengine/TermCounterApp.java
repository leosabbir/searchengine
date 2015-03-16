package edu.unlv.searchengine;

import edu.unlv.searchengine.util.DocumentsProcessor;
import edu.unlv.searchengine.util.IntermediateInvertedIndexProcessor;
import edu.unlv.searchengine.util.WordsCountFileProcessor;

public class TermCounterApp {
	
	public static void main(String[] args) {
		//DocumentsProcessor processor = new DocumentsProcessor();
		//processor.process();
		
		//new WordsCountFileProcessor().process();
		new IntermediateInvertedIndexProcessor().process();
	}

}
