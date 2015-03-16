package edu.unlv.searchengine.util.io;

public class IntermediateInvertedIndexFileReader {

	private SEFileReader fileReader;
	
	public IntermediateInvertedIndexFileReader() {
		this.fileReader = new SEFileReader(IntermediateInvertedIndexFileWriter.INTERMEDIATE_INVERTED_INDEX_FILE_PATH);
	}
	
	public IntermediateInvertedIndexFileReader(String path) {
		this.fileReader = new SEFileReader(path);
	}

	public String getNextLine() {
		return this.fileReader.getNextLine();
	}
	
	public void close() {
		this.fileReader.close();
	}
}
