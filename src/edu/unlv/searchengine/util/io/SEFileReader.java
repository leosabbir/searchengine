package edu.unlv.searchengine.util.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SEFileReader {
	private String filePath;
	private File file;
	private FileReader fileReader;
	BufferedReader bufferedReader;
	
	public SEFileReader(String filePath) {
		this.filePath = filePath;
	}

	private BufferedReader initializeFileBufferedReader() {
		try {
			file = new File(filePath);
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			return bufferedReader;
		} catch (FileNotFoundException e) {
			System.err.println("File " + filePath + " is not found");
			return null;
		}
	}
	
	public String getNextLine() {
		if (bufferedReader == null) {
			this.initializeFileBufferedReader();
		}
		
		try {
			return this.bufferedReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void close() {
		try {
			this.fileReader.close();
			this.bufferedReader.close();
		} catch (IOException e) {

		}
	}

}
