package antlr;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;



public class Demo {
	public static void main(String[] args) throws Exception {
		
/*		//ANTLRStringStream in = new ANTLRStringStream("12*(5-6)");
		CharStream input = new ANTLRInputStream("sabbir AND sajina");
		SearchQueryLexer lexer = new SearchQueryLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		SearchQueryParser parser = new SearchQueryParser(tokens);
		
		ParseTreeWalker walker = new ParseTreeWalker(); // create standard walker
		SearchQueryListener extractor = new SearchQueryBaseListener();
		//walker.walk(extractor, parser.);
		 * 
		 * 
*/	
		ANTLRInputStream input = new ANTLRInputStream(System.in);
		SearchQueryLexer lexer = new SearchQueryLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		SearchQueryParser parser = new SearchQueryParser(tokens);
		
		ParseTree tree = parser.start();
		System.out.println(tree.toStringTree(parser));
		
		System.out.println("walking");
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(new SearchQueryListenerImpl(), tree);
		System.out.println();
		//post fix to ... could work here using stacks
	}
}
