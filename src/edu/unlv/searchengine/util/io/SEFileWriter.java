package edu.unlv.searchengine.util.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SEFileWriter {

	private BufferedWriter bufferedWriter;

	private File file;
	private FileWriter fileWriter;

	public SEFileWriter(String filePath) {
		try {
			file = new File(filePath);
			fileWriter = new FileWriter(file);
			this.bufferedWriter = new BufferedWriter(fileWriter);
		} catch (IOException e) {
			System.err.println(filePath + " not found");
		}
	}

	public void writeLine(String line) {
		try {
			this.bufferedWriter.write(line);
			this.bufferedWriter.newLine();
		} catch (IOException e) {

		}
	}
	
	public void flush() {
		try {
			this.fileWriter.flush();
			this.bufferedWriter.flush();
		} catch (IOException e) { 
		
	}
	}

	public void close() {
		try {
			this.flush();
			this.fileWriter.close();
			this.bufferedWriter.close();
		} catch (IOException e) {

		}
	}

}
