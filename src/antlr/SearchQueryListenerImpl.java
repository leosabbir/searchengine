package antlr;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import antlr.SearchQueryParser.ExprContext;
import antlr.SearchQueryParser.StartContext;

public class SearchQueryListenerImpl implements SearchQueryListener{

	@Override
	public void enterEveryRule(ParserRuleContext arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitEveryRule(ParserRuleContext arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitErrorNode(ErrorNode arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitTerminal(TerminalNode arg0) {
		System.out.println(arg0.toString());
		
	}

	@Override
	public void enterStart(StartContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitStart(StartContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterExpr(ExprContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitExpr(ExprContext ctx) {
		// TODO Auto-generated method stub
		
	}

}
