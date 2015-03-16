package edu.unlv.searchengine.util.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class WordCounterFileWriter {
	
	public final static String OUTPUT_FILE_PATH = "word_count_in_each_doc.txt";
	private BufferedWriter bufferedWriter;
	
	public WordCounterFileWriter() {
		try {
			FileWriter fileWriter = new FileWriter(OUTPUT_FILE_PATH);
			this.bufferedWriter = new BufferedWriter(fileWriter);
		} catch (IOException e) {
			System.err.println(OUTPUT_FILE_PATH + " not found");
		}
	}
	
	public void writeLine(String line) {
		try {
			this.bufferedWriter.write(line);
			this.bufferedWriter.newLine();
		} catch (IOException e) {
			
		}
	}
	
	public void close() {
		try {
			this.bufferedWriter.close();
		} catch ( IOException e) {
			
		}
	}
	
	public void printFinalMessage() {
		System.out.println("DONE!! Written to output file " + WordCounterFileWriter.OUTPUT_FILE_PATH);
	}
	
	public static void main(String [] args) {
		WordCounterFileWriter fw = new WordCounterFileWriter();
		fw.writeLine("tom marvolo ridle");
		
		for (int i = 0; i < 20000; i++) {
			fw.writeLine("King of kings");
			
		}
		
		fw.close();
	}

}
