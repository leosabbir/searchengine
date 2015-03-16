package edu.unlv.searchengine.util;

public class SEStemmer extends Stemmer {
	
	
	public String stem(String wordToStem) {
		wordToStem = wordToStem.toLowerCase();
		for (int i = 0; i < wordToStem.length(); i++) {
			this.add(wordToStem.charAt(i));
		}
		this.stem();
		return this.toString();
	}
	
	public static void main(String[] args) {
		SEStemmer stemmer = new SEStemmer();
		System.out.println(stemmer.stem("engineer"));
		System.out.println(stemmer.stem("engineering"));
		System.out.println(stemmer.stem("engineered"));
		System.out.println(stemmer.stem("swimming"));
		System.out.println(stemmer.stem("I am the king of King king"));
	}

}
