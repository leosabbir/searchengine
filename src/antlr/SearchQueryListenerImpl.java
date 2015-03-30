package antlr;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import antlr.SearchQueryParser.ExprContext;
import antlr.SearchQueryParser.StartContext;

public class SearchQueryListenerImpl implements SearchQueryListener{
	
	List<String> tokens = new ArrayList<String>();

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
		this.tokens.add(arg0.toString());
		//System.out.println(arg0.toString());
		
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
		System.out.println(ctx.toString());
		
	}

	@Override
	public void exitExpr(ExprContext ctx) {
		// TODO Auto-generated method stub
		
	}
	
	public List<String> getTokens() {
		return this.tokens;
	}

}
