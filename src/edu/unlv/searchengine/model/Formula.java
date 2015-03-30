package edu.unlv.searchengine.model;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;



public class Formula {
	String word;
	
	Formula left;
	Formula right;
	int op;
	
	List<Formula> formulae;
	
	public Formula(int op, Formula left, Formula right) {
		this.left = left;
		this.right = right;
		this.op = op;
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
	
	public  Formula(String word) {
		op = -1;
		this.word = word;
	}
	
	public String toString() {
		return op == -1 ? word : "[" + left + " " + op + " " + right + "]";
	}
	
	public static Formula create2 (List<String> tokens) {
		Stack<Object> stack = new Stack<Object>();
		Integer op;
		for (String token : tokens) {
			if ( token.equals("AND") || token.equals("NOT")) {
				op = 1;
				stack.push(op);
			} else if (token.equals("OR")) {
				op = 2;
				stack.push(op);
			} else if (token.equals("(")) {
				stack.push(token);
			} else if (token.equals(")")) {
				//only one Formula for whatever inside the bracket
				if (stack.size() > 1) {
				Formula right = (Formula) stack.pop();
				//stack.pop(); // extract (
				op = (Integer) stack.pop();
				Formula left = (Formula) stack.pop();
				stack.push(new Formula(op, left, right));
				}
			} else { // search words
				if ( stack.isEmpty()) {
					stack.push(new Formula(token));
				} else {
					Object content = stack.pop();
					if (content instanceof Integer) {
						op = (Integer) content;
						Formula left = (Formula) stack.pop();
						stack.push(new Formula(op, left, new Formula(token)));
					} else {
						stack.push(new Formula(token));
						
					}
				}
			}
		}
		
		Formula formula = (Formula) stack.pop();
		return formula;
	}
	
	/*public static Formula convertToCNF(Formula formula) {
		if ( formula.op == -1) {
			return formula;
		} else {
			if (formula.op == 1) { //AND
			     return the concatenation of P and Q
			         (each of which is a vector of clauses)
			} else if ( formula.op == 2 ) { //OR
				
			} else if (formula.op == 3) { //NOT
				
			}
			
		}
		return null;
	}*/
	
	public static void main(String[] args) {
		String input = "( sajina AND ( sabbir OR sajina ) OR NOT saru ) AND sanil";
		List<String> tokens = Arrays.asList(input.split(" "));
		System.out.println(Formula.create2(tokens));
		
	}

}