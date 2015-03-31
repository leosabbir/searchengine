package edu.unlv.searchengine.model;

public class Formula {
	String word;
	
	Formula left;
	Formula right;
	int op;
	
	boolean not;
	
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
	
}