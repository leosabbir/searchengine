package edu.unlv.searchengine.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import edu.unlv.searchengine.context.StopWordsLoader;
import edu.unlv.searchengine.model.DocumentCosineWeightForTerm;
import edu.unlv.searchengine.util.io.SEFileReader;
import edu.unlv.searchengine.util.io.WordCounterFileWriter;

public class VectorSpaceUtils {
	
	public static final Double TOTAL_NUMBER_OF_DOCUMENTS = 1400d;
	public static final String QUERY_FILE = "cran.qry";
	public static final String QUERY_RELEVANT_FILE = "cranqrel";
	
	private Map<Long, Map<String, Double>> weightOfTermsInDocuments;
	private Map<Long, Double> documentWeights;
	private Map<String, Double> overallTermWeight;
	private Map<Long, List<Long>> queryRelevantDocuments;
	
	public VectorSpaceUtils() {
		this.getWeightOfTermsInDocuments();
		this.getOverallTermWeight();
		this.loadQueryRelevantDocuments();
	}
	
	public Map<Long, Map<String, Double>> getWeightOfTermsInDocuments() {
		weightOfTermsInDocuments = new HashMap<Long, Map<String,Double>>();
		documentWeights = new HashMap<Long, Double>();
		
		SEFileReader wordCountFileReader = new SEFileReader(WordCounterFileWriter.OUTPUT_FILE_PATH);
		String line;
		Long documentID = 0L;
		double squaredDocumentWeightSummation = 0.0;
		Map<String, Double> weightOfTermsInDocument = null;
		while ( (line = wordCountFileReader.getNextLine()) != null ) {
			if ( line.startsWith("Document ID")) {
				if (weightOfTermsInDocument != null) {
					weightOfTermsInDocuments.put(documentID, weightOfTermsInDocument);
					documentWeights.put(documentID, Math.sqrt(squaredDocumentWeightSummation));
				}
				wordCountFileReader.getNextLine(); 
				wordCountFileReader.getNextLine(); //ignore two lines after Document ID:xx is read
				documentID = Long.parseLong(line.split(":")[1]);
				weightOfTermsInDocument = new HashMap<String, Double>();
			} else {
				//one entry of a document from word count file
				String[] wordFrequencyPair = line.split(":");
				if (wordFrequencyPair.length == 1) {
					continue;
				}
				double termWeightInDocument = 1 + Math.log(Long.parseLong(wordFrequencyPair[1]));
				weightOfTermsInDocument.put(wordFrequencyPair[0], termWeightInDocument);
				squaredDocumentWeightSummation += Math.pow(termWeightInDocument, 2);
			}
		}
		weightOfTermsInDocuments.put(documentID, weightOfTermsInDocument);
		documentWeights.put(documentID, Math.sqrt(squaredDocumentWeightSummation));
		
		return weightOfTermsInDocuments;
	}
	
	public void loadQueryRelevantDocuments() {
		this.queryRelevantDocuments = new HashMap<Long, List<Long>>();
		
		SEFileReader wordCountFileReader = new SEFileReader(VectorSpaceUtils.QUERY_RELEVANT_FILE);
		String line;
		Long queryID = -1L;
		while ( (line = wordCountFileReader.getNextLine()) != null ) {
			String[] components = line.split(" ");
			long newQueryID = Long.parseLong(components[0]);
			long documentID = Long.parseLong(components[1]);
			
			if ( queryID != newQueryID) {
				queryID = newQueryID;
				List<Long> documents = new ArrayList<Long>();
				documents.add(documentID);
				this.queryRelevantDocuments.put(queryID, documents);
			} else {
				this.queryRelevantDocuments.get(queryID).add(documentID);
			}
		}
	}
	
	public Map<String, Double> getOverallTermWeight() {
		this.overallTermWeight = new HashMap<String, Double>();
		
		SEFileReader invertedIndexFileReader = new SEFileReader(InvertedIndexGenerator.INVERTED_INDEX_FILE_PATH);
		
		String line;
		invertedIndexFileReader.getNextLine(); // ignore first line INVERTED_FILE_HASH={
		while ( (line = invertedIndexFileReader.getNextLine()) != null ) {
			String[] components = line.trim().split("=>");
			
			if (components.length == 1) {
				break;
			}

			String term = components[0];
			term = term.substring(1, term.length()-1);
			
			Long numberofDocsTermOccursIn = (Long) ((JSONArray) JSONValue.parse(components[1])).get(0);
			
			this.overallTermWeight.put(term, Math.log(TOTAL_NUMBER_OF_DOCUMENTS/numberofDocsTermOccursIn));
		}
		
		return this.overallTermWeight;
	}
	
	public void readQueryFile() {
		SEFileReader queryFileReader = new SEFileReader(VectorSpaceUtils.QUERY_FILE);
		List<List<String>> queries = new ArrayList<List<String>>();
		
		String line;
		boolean content = false;
		List<String> query = null;
		int queryNumber = 0;
		while( (line = queryFileReader.getNextLine()) != null) {
			if (line.startsWith(".I")) {
				if (query != null) {
					computeCosine(query, queryNumber);
					queries.add(query);
				}
				query = null;
				content = false;
				queryNumber++;
			} else if (line.startsWith(".W")) {
				query = new ArrayList<String>();
				content = true;
				continue;
			} else if ( content ) {
				StringTokenizer t = new StringTokenizer(line, "[ \n\t\r.,;:!?(){}]'/");
				while (t.hasMoreTokens()) { // Process Words
					String word = t.nextToken();
					/*String[] subWords = word.split("-"); //break words like angel-the-demon
					if(subWords.length > 1) {
						for (String subWord : subWords) {
							processWord(wordsWithCount, subWord);
						}
					}*/
					SEStemmer stemmer = new SEStemmer();
					if (!word.isEmpty() && !StopWordsLoader.getStopWords().contains(word)) {
						word = stemmer.stem(word);
						query.add(word);
					}
				}
			}
		}
		if (query.size() > 0) {
			queries.add(query);
			computeCosine(query, queryNumber);
		}
	}
	
	public void computeCosine(List<String> terms, long queryNumber) {
		List<DocumentCosineWeightForTerm> docs = new ArrayList<DocumentCosineWeightForTerm>();
		int numberOfRelevantDocs = 0;
		for (Long documentID : weightOfTermsInDocuments.keySet()) {
			double cosineWt = 0;
			for (String term : terms) {
				if (this.weightOfTermsInDocuments.get(documentID).containsKey(term)) {
					double ft = weightOfTermsInDocuments.get(documentID).get(term);
					double ifd = overallTermWeight.get(term);
					double wd = this.documentWeights.get(documentID);
					cosineWt += ft * ifd / wd;
				}
			}
			if (cosineWt > 0) {
				docs.add(new DocumentCosineWeightForTerm(documentID, cosineWt));
				if (this.queryRelevantDocuments.get(queryNumber).contains(documentID)) {
					numberOfRelevantDocs++;
				}
			}
		}
		Collections.sort(docs);
		System.out.println("*******************");
		System.out.println("Query No. " + queryNumber);
		System.out.println("No. of doucments retrieved: " + docs.size());
		System.out.println("Precision : " + (1.0 * numberOfRelevantDocs) / docs.size());
		System.out.println("Recall : " + (1.0 * numberOfRelevantDocs) / this.queryRelevantDocuments.get(queryNumber).size() );
		System.out.println(docs);
		System.out.println("\n");

	}
}
