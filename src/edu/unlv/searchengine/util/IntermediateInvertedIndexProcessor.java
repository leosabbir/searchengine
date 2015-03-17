package edu.unlv.searchengine.util;

import java.util.ArrayList;
import java.util.Iterator;
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
		System.out.println("==> Sorting individual runs.....");
		sort(subList);
		this.fileWriter.writeLine(subList);
		this.fileWriter.flush();
		this.fileWriter.close();
		
		System.out.println("==> Merging the individual Sorted runs.....");
		this.merge(intermediateCounter, IntermediateInvertedIndexProcessor.NUMBER_OF_ENTRIES_TO_SORT, 3);
	}
	
	private void merge(int counter, int numberInEachBlock, int blocks) {
		if (blocks < 2) {
			//TODO rename last temp file
			return;
		} else {
			this.fileReader = new IntermediateInvertedIndexFileReader((counter++) + ".tmp");
			this.fileWriter = new IntermediateInvertedIndexFileWriter(counter + ".tmp");
			blocks = 0;
			
			String line;
			List<IntermediateInvertedIndex> firstSubList = new ArrayList<IntermediateInvertedIndex>();
			List<IntermediateInvertedIndex> secondSubList = new ArrayList<IntermediateInvertedIndex>();
			
			boolean odd = true;
			while ( (line = this.fileReader.getNextLine()) != null) {
				if (odd) {
					firstSubList.add(new IntermediateInvertedIndex(line));
					if (firstSubList.size() == numberInEachBlock) {
						odd = false;
						blocks++;
					}
				} else {
					secondSubList.add(new IntermediateInvertedIndex(line));
					if (secondSubList.size() == numberInEachBlock) {
						//merge and write to temp file recursively
						this.mergeAndWrite(firstSubList, secondSubList);
						
						firstSubList = new ArrayList<IntermediateInvertedIndex>();
						secondSubList = new ArrayList<IntermediateInvertedIndex>();
						odd = true;
						blocks++;
					}
				}
			}
			if (blocks == 1) {
				this.fileWriter.close();
				this.fileWriter.deleteFile();
				this.fileWriter = new IntermediateInvertedIndexFileWriter(IntermediateInvertedIndexFileWriter.SORTED_INTERMEDIATE_INVERTED_INDEX_FILE_PATH);
			}
			this.mergeAndWrite(firstSubList, secondSubList);
			this.fileWriter.flush();
			this.fileWriter.close();
			this.fileReader.close();
			this.fileReader.delete();
			
			this.merge(counter, numberInEachBlock * 2, blocks);
		}
	}
	
	private void mergeAndWrite(List<IntermediateInvertedIndex> firstSubList, List<IntermediateInvertedIndex> secondSubList) {
		Iterator<IntermediateInvertedIndex> firstIterator = firstSubList.iterator();
		Iterator<IntermediateInvertedIndex> secondIterator = secondSubList.iterator();
		
		IntermediateInvertedIndex fromFirst = null;
		IntermediateInvertedIndex fromSecond = null;
		while ( firstIterator.hasNext() || secondIterator.hasNext() ) {
			 fromFirst = fromFirst == null && firstIterator.hasNext() ? firstIterator.next() : fromFirst;
			 fromSecond = fromSecond == null && secondIterator.hasNext() ? secondIterator.next() : fromSecond;
			
			 if (fromFirst != null && (fromSecond == null || !fromFirst.isGreaterThan(fromSecond))) {
				 this.fileWriter.writeLine(fromFirst.toString());
				 fromFirst = null;
			 } else {
				 this.fileWriter.writeLine(fromSecond.toString());
				 fromSecond = null;
			 }
		}
		
	}

	private void sort(List<IntermediateInvertedIndex> subList) {
		for (int i = 1; i < subList.size(); i++) {
			int j = i - 1;
			int k = i;
			while ( j >= 0 && !subList.get(k).isGreaterThan(subList.get(j)) ) {
				IntermediateInvertedIndex temp = subList.get(k);
				subList.set(k, subList.get(j));
				subList.set(j, temp);
				
				j = j - 1;
				k = k - 1;
			}
			
		}
	}

}
