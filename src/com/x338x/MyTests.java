package com.x338x;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class MyTests {

	/* test compilation of instruction to bytecode */
	
	@Test
	public void test_compile_ADD_A_B() {
	    Map<String, Statement> labelMap = new HashMap<String, Statement>();
	    try {
	    	Operations ins = new Operations("ADD A, B", labelMap);
	    	assert(ins.getByteCode() == 0b0100000100000000);
	    }
	    catch (Exception e) {
	    	fail(e.getMessage());
	    }
	}
	
	/* test invalid syntax of ADD instruction */
	
	@Test
	public void test_compile_ADD_A_Z() {
	    Map<String, Statement> labelMap = new HashMap<String, Statement>();
	    try {
	    	@SuppressWarnings("unused")
			Operations ins = new Operations("ADD A, Z", labelMap);
	    	fail("Compilation of ADD A, Z succeeded but should not have.");
	    }
	    catch (Exception e) {
	    	if (e.getMessage().equals("Can't parse line: ADD A, Z") == false)
	    		fail(e.getMessage()); // wrong error recvd
	    }
	}
	
	/* test actual instruction conversion for ADD: does not validate syntax */
	
	@Test
	public void test_ADD_A_B() {
		int bc = ADD.convert("A", "B");
		assert(bc == 0b0100000100000000);
	}
	
	@Test
	public void test_ADD_B_B() {
		int bc = ADD.convert("B", "B");
		assert(bc == 0b0100010100000000);
	}
	
	@Test
	public void test_ADD_A_A() {
		int bc = ADD.convert("A", "A");
		assert(bc == 0b0100000000000000);
	}
	
	/* test actual instruction execution for ADD */
	
	@Test
	public void test_ADD_A_B_exec() {
		Registers r = new Registers();
		r.setA(2);
		r.setB(3);
		try {
			ADD.execute(r, Opcodes.REGA, Opcodes.REGB);
			assert(r.getA() == 5);
		}
		catch(Exception e) {
			fail("Failed to ADD A(2), B(3) got=" + r.getA());
		}
	}
	
	/* test actual instruction execution for ADD results in overflow bit set but correct result */
	
	@Test
	public void test_ADD_A_B_exec_overflow() {
		Registers r = new Registers();
		r.setA(255);
		r.setB(2);
		try {
			ADD.execute(r, Opcodes.REGA, Opcodes.REGB);
			assert(r.getA() == 1);
			assert((r.getST() & Registers.OVERFLOW) == Registers.OVERFLOW);
		}
		catch(Exception e) {
			fail("Failed to ADD A(2), B(3) got=" + r.getA());
		}
	}
	
	

}
