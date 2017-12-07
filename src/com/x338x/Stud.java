package com.x338x;

import javax.swing.JFrame;

public class Stud {

	public static void main(String[] args) {
		Compiler compiler =  new Compiler();
        VirtualMachine vm = new VirtualMachine();
        //String program = "LD A, 10\nLD B, 1\nL1: SUB A, B\nST A, @0\nST B, @1\nBNZ A, L1\nLD B, 5\nST B, @2\nHALT";
        //String program = "LD A, 10";
        String program = "L1: LD A, 10\nBNZ A, L1";

        try{
        	 compiler.compile(program);
        	 vm.setByteCodes(compiler.byteCodes);
        	 moveNext(vm);
        	 moveNext(vm);
        }
        catch(Exception e)
        {
        	System.out.println(e.getMessage().toString());
        }
        
		
        
	}

	private static void moveNext(VirtualMachine vm) throws Exception {
		vm.step();
	}

}
