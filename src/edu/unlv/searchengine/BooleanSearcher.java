package edu.unlv.searchengine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import antlr.SearchQueryLexer;
import antlr.SearchQueryListenerImpl;
import antlr.SearchQueryParser;
import edu.unlv.searchengine.model.Formula;
import edu.unlv.searchengine.util.InvertedIndexReader;

public class BooleanSearcher {
	
	private static Map<String, List<Long>> invertedIndex;
	
	public static void main(String[] args) {
		invertedIndex = new InvertedIndexReader().constructInvertedIndex();
		
		Scanner sc = new Scanner(System.in);
		String line;
		
		while ( !(line = sc.nextLine()).equals("-1")) {
			Formula formula = constructFormula(line);
			System.out.println(search(formula));
		}
		sc.close();
	}
	
	public static List<Long> search(Formula formula) {
		if (formula.isSingleFormula()) {
			List<Long> docs = invertedIndex.get(formula.getWord()); 
			return docs == null ? new ArrayList<Long>() : docs;
		} else {
			return combine(formula.getOp(), search(formula.getLeft()), search(formula.getRight()));
		}
	}
	
	private static List<Long> and(List<Long> left, List<Long> right) {
		List<Long> result = new ArrayList<Long>(left);
		result.retainAll(right);
		
		return result;
	}
	
	private static List<Long> or(List<Long> left, List<Long> right) {
		Set<Long> union = new HashSet<Long>();
		union.addAll(left);
		union.addAll(right);
		
		return new ArrayList<Long>(union);
	}
	
	public static List<Long> combine (int op, List<Long> left, List<Long> right) {
		if ( op == 1) {
			return and(left, right);
		} else if ( op == 2) {
			return or(left, right);
		}
		return null;
	}
	
	public static Formula constructFormula(String query) {
		ANTLRInputStream input = new ANTLRInputStream(query);
		SearchQueryLexer lexer = new SearchQueryLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		SearchQueryParser parser = new SearchQueryParser(tokens);
		
		ParseTree tree = parser.start();
		
		ParseTreeWalker walker = new ParseTreeWalker();
		SearchQueryListenerImpl searchQueryListener = new SearchQueryListenerImpl();
		walker.walk(searchQueryListener, tree);

		return Formula.create2(searchQueryListener.getTokens());
	}

}
