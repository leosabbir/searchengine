package edu.unlv.searchengine.util.io;

public class WordsCountFileReader {
	
	private SEFileReader fileReader;
	
	public WordsCountFileReader() {
		this.fileReader = new SEFileReader(WordCounterFileWriter.OUTPUT_FILE_PATH);
	}
	
	public String getNextLine() {
		return this.fileReader.getNextLine();
	}
	
	public void close() {
		this.fileReader.close();
	}

}
