package edu.unlv.searchengine.util;

import java.util.ArrayList;
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
	
	public Map<Long, Map<String, Double>> getWeightOfTermsInDocuments() {
		Map<Long, Map<String, Double>> weightOfTermsInDocuments = new HashMap<Long, Map<String,Double>>();
		Map<Long, Double> documentWeights = new HashMap<Long, Double>();
		
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
	
	public Map<String, Double> getOverallTermWeight() {
		Map<String, Double> overallTermWeight = new HashMap<String, Double>();
		
		SEFileReader invertedIndexFileReader = new SEFileReader(InvertedIndexGenerator.INVERTED_INDEX_FILE_PATH);
		
		String line;
		invertedIndexFileReader.getNextLine(); // ignore first line INVERTED_FILE_HASH={
		while ( (line = invertedIndexFileReader.getNextLine()) != null ) {
			String[] components = line.trim().split("=>");
			
			String term = components[0];
			if (components.length == 1) {
				break;
			}
			Long numberofDocsTermOccursIn = (Long) ((JSONArray) JSONValue.parse(components[1])).get(0);
			
			overallTermWeight.put(term, Math.log(TOTAL_NUMBER_OF_DOCUMENTS/numberofDocsTermOccursIn));
		}
		
		return overallTermWeight;
	}
	
	public void readQueryFile() {
		SEFileReader queryFileReader = new SEFileReader(VectorSpaceUtils.QUERY_FILE);
		List<List<String>> queries = new ArrayList<List<String>>();
		
		String line;
		boolean content = false;
		List<String> query = null;
		while( (line = queryFileReader.getNextLine()) != null) {
			if (line.startsWith(".I")) {
				if (query.size() > 0) {
					queries.add(query);
					//TODO processing for each query can be done here
					//computeCosine(query);
				}
				query = null;
				content = false;
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
			//computeCosine(query);
		}
	}
	
	public void computeCosine(List<String> terms) {
		Map<Long, Map<String, Double>> weightOfTermsInDocuments = getWeightOfTermsInDocuments();
		Map<String, Double> overallTermWeight = getOverallTermWeight();

		List<DocumentCosineWeightForTerm> docs = new ArrayList<DocumentCosineWeightForTerm>();
		for (Long documentID : weightOfTermsInDocuments.keySet()) {
			double cosineWt = 0;
			for (String term : terms) {
				cosineWt += weightOfTermsInDocuments.get(documentID).get(term)
						* overallTermWeight.get(term);
			}
			docs.add(new DocumentCosineWeightForTerm(documentID, cosineWt));
		}

	}
		
	

}
