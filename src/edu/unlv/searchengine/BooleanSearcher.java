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
import edu.unlv.searchengine.util.BooleanFormulaBuilder;
import edu.unlv.searchengine.util.InvertedIndexReader;

public class BooleanSearcher {
	
	private static Map<String, List<Long>> invertedIndex;
	private static List<Long> allDocs = new ArrayList<Long>();
	
	public static void main(String[] args) {
		invertedIndex = new InvertedIndexReader().constructInvertedIndex();
		
		for (long i = 1; i <= 1400; i++) {
			allDocs.add(i);
		}
		
		Scanner sc = new Scanner(System.in);
		String line;
		
		System.out.println("Enter \"EXIT\" to exit....\n\n");
		System.out.print("\n\nEnter Search Query (MUST BE IN BOOLEAN FORMAT):\n=>>");
		while ( !(line = sc.nextLine()).equalsIgnoreCase("EXIT")) {
			Formula formula = constructFormula(line);
			
			List<Long> result = search(formula);
			System.out.println("Number of documents: " + result.size());
			System.out.println(result);
			System.out.print("\n\nEnter Search Query (MUST BE IN BOOLEAN FORMAT):\n=>>");
		}
		sc.close();
	}
	
	public static List<Long> search(Formula formula) {
		if (formula.isSingleFormula()) {
			List<Long> docs = invertedIndex.get(formula.getWord());
			docs = docs == null ? new ArrayList<Long>() : docs;
			if (formula.isNot()) {
				computeComplement(docs);
			}
			
			return docs;
		} else {
			return combine(formula.getOp(), formula.isNot(), search(formula.getLeft()), search(formula.getRight()));
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
	
	public static List<Long> computeComplement(List<Long> original) {
		List<Long> temp = new ArrayList<Long>(allDocs);
		temp.removeAll(original);
		original.clear();
		original.addAll(temp);
		
		return original;
	}
	
	public static List<Long> combine (int op, boolean isNot,  List<Long> left, List<Long> right) {
		if ( op == 1) {
			return isNot ? computeComplement(and(left, right)) : and(left, right);
		} else if ( op == 2) {
			return isNot ? computeComplement(or(left, right)) : or(left, right);
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

		return BooleanFormulaBuilder.build(searchQueryListener.getTokens());
	}

}
