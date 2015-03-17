package edu.unlv.searchengine.util.io;

import java.util.List;

import edu.unlv.searchengine.model.IntermediateInvertedIndex;

public class IntermediateInvertedIndexFileWriter {
	
	public static final String INTERMEDIATE_INVERTED_INDEX_FILE_PATH = "intermediate_inverted_index.txt";
	public static final String SORTED_INTERMEDIATE_INVERTED_INDEX_FILE_PATH = "sorted_intermediate_inverted_index.txt";
	private SEFileWriter fileWriter;
	
	public IntermediateInvertedIndexFileWriter() {
		this.fileWriter = new SEFileWriter(INTERMEDIATE_INVERTED_INDEX_FILE_PATH);
	}
	
	public IntermediateInvertedIndexFileWriter(String path) {
		this.fileWriter = new SEFileWriter(path);
	}
	
	public void writeLine(String line) {
		this.fileWriter.writeLine(line);
	}
	
	public void flush() {
		this.fileWriter.flush();
	}
	
	public void close() {
		this.fileWriter.close();
	}
	
	public void deleteFile() {
		this.fileWriter.deleteFile();
	}

	public void writeLine(List<IntermediateInvertedIndex> subList) {
		for (IntermediateInvertedIndex intermediateInvertedIndex : subList) {
			this.writeLine(intermediateInvertedIndex.toString());
		}
	}

}
