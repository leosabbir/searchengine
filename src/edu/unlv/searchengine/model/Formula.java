package edu.unlv.searchengine.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;



public class Formula {
	String word;
	
	Formula left;
	Formula right;
	int op;
	
	boolean not;
	
	List<Formula> formulae;

	public  Formula(String word) {
		op = -1;
		this.not = false;
		this.word = word;
	}
	
	public Formula(int op, Formula left, Formula right) {
		this.left = left;
		this.right = right;
		this.op = op;
		this.not = false;
	}
	
	public boolean isNot() {
		return this.not;
	}
	
	public void setIsNot() {
		this.not = true;
	}
	
	public boolean isSingleFormula() {
		return op == -1;
	}
	
	public int getOp() {
		return this.op;
	}
	
	public Formula getLeft() {
		return this.left;
	}
	
	public Formula getRight() {
		return this.right;
	}
	
	public String getWord() {
		return this.word;
	}
	
	
	public String toString() {
		String not = this.not ? "!" : "";
		return op == -1 ? not + word : not + "[" + left + " " + op + " " + right + "]";
	}
	
	public static Formula create3 (List<String> tokens) {
		Stack<String> stack = new Stack<String>();
		boolean expectExpression = true;
		
		List<String> postFixQuery = new ArrayList<String>();
		for (int i = 0; i < tokens.size()+1; i++) {
			String token = i < tokens.size() ? tokens.get(i) : "$";
			if (expectExpression) {
				if (isVar(token)) {
					//System.out.print(token + " ");
					postFixQuery.add(token);
					expectExpression = false;
				} else if ( token.equals("(")) {
					stack.push(token);
				} else if ( token.equals("NOT")) {
					stack.push("NOT");
				} else {
					System.err.println("Error");
				}
			} else {
				if ( token.equals("AND") || token.equals("OR")) {
					while( !stack.isEmpty() && !stack.peek().equals("(") ) {
						String op = stack.pop();
						postFixQuery.add(op);
						//System.out.print(op + " ");
					}
					stack.push(token);
					expectExpression = true;
				} else if ( token.equals(")") || token.equals("$")) {
					while( !stack.isEmpty() && !stack.peek().equals("(") ) {
						String op = stack.pop();
						postFixQuery.add(op);
						//System.out.print(op + " ");
					}
					if (!stack.isEmpty()) {
						stack.pop();
					}
				}
			}
		}
		
		Stack<Formula> formulae = new Stack<Formula>();
		for (String token : postFixQuery) {
			if (isVar(token)) {
				formulae.push(new Formula(token));
			} else if (token.equals("AND") || token.equals("OR")) {
				int operator = token.equals("AND") ? 1 : 2;
				Formula left = formulae.pop();
				formulae.push(new Formula(operator, formulae.pop(), left));
			} else {
				formulae.peek().setIsNot();
			}
			
		}
		
		Formula formula = formulae.pop();
		return formula;
	}
	
	public static boolean isVar(String token) {
		return !(token.equals("AND") || token.equals("OR") || token.equals("NOT") || token.equals("(") || token.equals(")") );
	}
	
	
	public static void main(String[] args) {
		/*
		 * ( sajina AND ( sabbir OR sajina ) OR NOT saru ) AND sanil
sajina sabbir sajina OR AND saru NOT OR sanil AND


String input = "NOT ( NOT sajina AND NOT ( sabbir OR sajina ) OR NOT saru ) AND sanil";
sajina NOT sabbir sajina OR NOT AND saru NOT OR NOT sanil AND

( NOT sajina AND ( NOT sabbir OR NOT sajina ) OR NOT saru ) AND NOT sanil
sajina NOT sabbir NOT sajina NOT OR AND saru NOT OR sanil NOT AND
		 */
		String input = "( NOT sajina AND ( NOT sabbir OR NOT sajina ) OR NOT saru ) AND NOT sanil";
		//String input = " ( sabbir AND ( saru ) )";
		List<String> tokens = Arrays.asList(input.split(" "));
		System.out.println(Formula.create3(tokens));
		
	}

}