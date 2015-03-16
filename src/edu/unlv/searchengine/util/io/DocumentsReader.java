package edu.unlv.searchengine.util.io;


public class DocumentsReader {
	
	private SEFileReader fileReader;
	private final String DOCUMENT_FILE_PATH = "cran.all.1400";
	
	public DocumentsReader() {
		this.fileReader = new SEFileReader(DOCUMENT_FILE_PATH);
	}
	
	public String getNextLine() {
		return this.fileReader.getNextLine();
	}
	
	public void close() {
		this.fileReader.close();
	}

}
