package com.x338x;

public class Student {

	public static void main(String[] args) {
		Compiler compiler =  new Compiler();
        String program = "LD A, 10\nLD B, 1\nL1: SUB A, B\nST A, @0\nST B, @1\nBNZ A, L1\nLD B, 5\nST B, @2\nHALT";
        try{
        	 compiler.compile(program);
        }
        catch(Exception e)
        {
        	System.out.println("Compilation error");
        }
	}

}
