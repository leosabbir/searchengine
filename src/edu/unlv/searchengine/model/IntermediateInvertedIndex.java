package edu.unlv.searchengine.model;

public class IntermediateInvertedIndex implements Comparable<IntermediateInvertedIndex> {

	private int wordId;
	private int docId;
	private int frequency;
	
	public IntermediateInvertedIndex(String entry) {
		String[] components = entry.split(",");
		wordId = Integer.parseInt(components[0]);
		docId = Integer.parseInt(components[1]);
		frequency = Integer.parseInt(components[2]);
	}
	
	public int getWordId() {
		return this.wordId;
	}
	
	public int getDocId() {
		return this.docId;
	}
	
	public String toString() {
		return wordId + "," + docId + "," + frequency;
	}
	
	public boolean isGreaterThan(IntermediateInvertedIndex other) {
		return this.compareTo(other) > 0;
	}
	
	@Override
	public int compareTo(IntermediateInvertedIndex o) {
		if ( this.wordId == o.getWordId()) {
			if ( this.docId > o.getDocId()) {
				return 1;
			} else {
				return -1;
			}
		} else if ( this.wordId > o.getWordId()) {
			return 1;
		} else {
			return -1;
		}
	}

	
}
