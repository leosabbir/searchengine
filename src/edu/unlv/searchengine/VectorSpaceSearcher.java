package edu.unlv.searchengine;

import edu.unlv.searchengine.util.VectorSpaceUtils;

public class VectorSpaceSearcher {
	
	public static void main(String[] args) {
		VectorSpaceUtils core = new VectorSpaceUtils();
		core.readQueryFile();
	}

}
