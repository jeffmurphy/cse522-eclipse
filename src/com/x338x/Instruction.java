package com.x338x;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.java.contract.Requires;
import com.google.java.contract.Ensures;

public class Instruction {

    // could do a CMP REG, [REG | LIT] ..

    
	private int value;

    /*
     bytecode is 16 bits:

     0000 0000 0000 0000
     CCII R1R2 OPER AND.

     CC = 00 : assignment (LD/ST)
          01 : ALU  (ADD/SUB/MUL/DIV)
          10 : branch (BEQ/BGT/BLT/BNZ)
          11 : HALT

     II = 00 : instruction (depends on CC)

     R1 = 00 : A
          01 : B
          10 : LITERAL
          11 : MEM REF
     R2 = 00 : A
          01 : B
          10 : LITERAL
          11 : MEMREF

     OPERAND = value depends on CC and II

     */

   

    

    public Instruction(int c)  {
        this.setValue(c);
    }

    
    public static int execute(Registers registers, Memory memory, int instruction) throws Exception {
        int nextIns = -1;

        switch (instruction & Opcodes.INS_MASK) {
            case Opcodes.II_LD:
                nextIns= Operations.execute("LD",registers, memory.getBlocks(), instruction);
                break;
            case Opcodes.II_ST:
                nextIns= Operations.execute("ST",registers, memory.getBlocks(), instruction);
                break;
            case Opcodes.II_ADD:
                nextIns= Operations.execute("ADD",registers, memory.getBlocks(), instruction);
                break;
            case Opcodes.II_SUB:
                nextIns= Operations.execute("SUB",registers, memory.getBlocks(), instruction);
                break;
            case Opcodes.II_MUL:
                nextIns= Operations.execute("MUL",registers, memory.getBlocks(), instruction);
                break;
            case Opcodes.II_DIV:
                nextIns= Operations.execute("DIV",registers, memory.getBlocks(), instruction);
                break;
            case Opcodes.II_BEQ:
                nextIns= Operations.execute("BEQ",registers, memory.getBlocks(), instruction);
                break;
            case Opcodes.II_BGT:
                nextIns= Operations.execute("BGT",registers, memory.getBlocks(), instruction);
                break;
            case Opcodes.II_BLT:
                nextIns= Operations.execute("BLT",registers, memory.getBlocks(), instruction);
                break;
            case Opcodes.II_BNZ:
                nextIns= Operations.execute("BNZ",registers, memory.getBlocks(), instruction);
                break;
            case Opcodes.II_HALT:
                nextIns= Operations.execute("HALT",registers, memory.getBlocks(), instruction);
                break;
            default:
                throw new Exception("Unknown instruction in CC|II fields: " + (instruction & Opcodes.INS_MASK));
        }
        
        return nextIns;

        
    }

    
    /*
     * @Requires({"reg == Opcodes.REGA || reg == Opcodes.REGB"})
     */
    public static int getregval(Registers r, int reg) throws Exception {
        switch (reg) {
            case Opcodes.REGA:
                return r.getA();
            case Opcodes.REGB:
                return r.getB();
            default:
                throw new Exception("Invalid register: " + reg);
        }
    }

    /*
     * @Requires({"reg == Opcodes.REGA || reg == Opcodes.REGB"})
     * @Ensures({"r.getA() == val || r.getB() == val"})
     */
    public static void setregval(Registers r, int reg, int val) throws Exception {
        switch (reg) {
            case Opcodes.REGA:
                r.setA(val);
                break;
            case Opcodes.REGB:
                r.setB(val);
                break;
            default:
                throw new Exception("Invalid register: " + reg);
        }
    }


	public int getValue() {
		return value;
	}


	public void setValue(int value) {
		this.value = value;
	}

}
