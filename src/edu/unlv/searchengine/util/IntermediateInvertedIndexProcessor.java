package edu.unlv.searchengine.util;

import java.util.ArrayList;
import java.util.List;

import edu.unlv.searchengine.model.IntermediateInvertedIndex;
import edu.unlv.searchengine.util.io.IntermediateInvertedIndexFileReader;
import edu.unlv.searchengine.util.io.IntermediateInvertedIndexFileWriter;

public class IntermediateInvertedIndexProcessor {
	
	public static final int NUMBER_OF_ENTRIES_TO_SORT = 2000;
	
	IntermediateInvertedIndexFileReader fileReader;
	private IntermediateInvertedIndexFileWriter fileWriter;
	
	private int intermediateCounter;
	
	public IntermediateInvertedIndexProcessor() {
		this.intermediateCounter = 0;

		this.fileReader = new IntermediateInvertedIndexFileReader();
		this.fileWriter = new IntermediateInvertedIndexFileWriter(intermediateCounter + ".tmp");
		
	}
	
	public void process() {
		String line;
		int blockcount = 0;
		List<IntermediateInvertedIndex> subList = new ArrayList<IntermediateInvertedIndex>();
		while ( (line = this.fileReader.getNextLine()) != null ) {
			subList.add(new IntermediateInvertedIndex(line));
			if (subList.size() == NUMBER_OF_ENTRIES_TO_SORT) {
				blockcount++;
				sort(subList);
				this.fileWriter.writeLine(subList);
				subList = new ArrayList<IntermediateInvertedIndex>();
			}
		}
		sort(subList);
		this.fileWriter.writeLine(subList);
		this.fileWriter.flush();getClass();
		this.fileWriter.close();
	}
	
	private void merge(int counter, int blocks, List<IntermediateInvertedIndex> list) {
		if (blocks == 1) {
			return;
		} else {
			this.fileReader = new IntermediateInvertedIndexFileReader((counter++) + ".tmp");
			this.fileWriter = new IntermediateInvertedIndexFileWriter(counter + ".tmp");
			
			String line;
			List<IntermediateInvertedIndex> firstSubList = new ArrayList<IntermediateInvertedIndex>();
			List<IntermediateInvertedIndex> secondSubList = new ArrayList<IntermediateInvertedIndex>();
			
			while ( (line = this.fileReader.getNextLine()) != null) {
				
			}
			
		}
	}
	
	private void sort(List<IntermediateInvertedIndex> subList) {
		for (int i = 1; i < subList.size(); i++) {
			int j = i - 1;
			int k = i;
			while ( j >= 0 && subList.get(k).isGreaterThan(subList.get(j)) ) {
				IntermediateInvertedIndex temp = subList.get(k);
				subList.set(k, subList.get(j));
				subList.set(j, temp);
				
				j = j - 1;
				k = k - 1;
			}
			
		}
	}

}
