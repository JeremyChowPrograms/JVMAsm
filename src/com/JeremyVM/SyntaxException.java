package com.JeremyVM;

public class SyntaxException extends Exception{
	private static final long serialVersionUID = 1L;
	public SyntaxException(String word, String reason){
		super("Syntax Error: "+reason+": "+ word);
	}

}
