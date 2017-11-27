package com.x338x;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Instruction {

    // could do a CMP REG, [REG | LIT] ..

    private String[] iPats = {
            "^(LD)\\s([AB])\\s*,\\s*([@]{0,1})(\\d+)$",
            "^(ST)\\s([AB])\\s*,\\s*([@]{1})(\\d+)$",
            "^(ADD)\\s([AB])\\s*,\\s*([AB])$",
            "^(SUB)\\s([AB])\\s*,\\s*([AB])$",
            "^(MUL)\\s([AB])\\s*,\\s*([AB])$",
            "^(DIV)\\s([AB])\\s*,\\s*([AB])$",
            "^(BEQ)\\s([AB])\\s*,\\s*([AB])\\s*,\\s*(\\S+)$",
            "^(BGT)\\s([AB])\\s*,\\s*([AB])\\s*,\\s*(\\S+)$",
            "^(BLT)\\s([AB])\\s*,\\s*([AB])\\s*,\\s*(\\S+)$",
            "^(BNZ)\\s([AB])\\s*,\\s*(\\S+)$",
            "^(HALT)$"
    };
    
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

    
    public static void execute(Registers registers, int[] memory, int instruction) throws Exception {
        int nextIns = -1;

        switch (instruction & Opcodes.INS_MASK) {
            case Opcodes.II_LD:
                LD.execute(registers, memory, (instruction & Opcodes.R1_MASK) >> Opcodes.R1_SHIFT, (instruction & Opcodes.R2_MASK) >> Opcodes.R2_SHIFT, instruction & Opcodes.OPERAND_MASK);
                break;
            case Opcodes.II_ST:
                ST.execute(registers, memory, (instruction & Opcodes.R1_MASK) >> Opcodes.R1_SHIFT, instruction & Opcodes.OPERAND_MASK);
                break;
            case Opcodes.II_ADD:
                ADD.execute(registers, (instruction & Opcodes.R1_MASK) >> Opcodes.R1_SHIFT, (instruction & Opcodes.R2_MASK) >> Opcodes.R2_SHIFT);
                break;
            case Opcodes.II_SUB:
                SUB.execute(registers, (instruction & Opcodes.R1_MASK) >> Opcodes.R1_SHIFT, (instruction & Opcodes.R2_MASK) >> Opcodes.R2_SHIFT);
                break;
            case Opcodes.II_MUL:
                MUL.execute(registers, (instruction & Opcodes.R1_MASK) >> Opcodes.R1_SHIFT, (instruction & Opcodes.R2_MASK) >> Opcodes.R2_SHIFT);
                break;
            case Opcodes.II_DIV:
                DIV.execute(registers, (instruction & Opcodes.R1_MASK) >> Opcodes.R1_SHIFT, (instruction & Opcodes.R2_MASK) >> Opcodes.R2_SHIFT);
                break;
            case Opcodes.II_BEQ:
                nextIns = BEQ.execute(registers, (instruction & Opcodes.R1_MASK) >> Opcodes.R1_SHIFT, (instruction & Opcodes.R2_MASK) >> Opcodes.R2_SHIFT, instruction & Opcodes.OPERAND_MASK);
                break;
            case Opcodes.II_BGT:
                nextIns = BGT.execute(registers, (instruction & Opcodes.R1_MASK) >> Opcodes.R1_SHIFT, (instruction & Opcodes.R2_MASK) >> Opcodes.R2_SHIFT, instruction & Opcodes.OPERAND_MASK);
                break;
            case Opcodes.II_BLT:
                nextIns = BLT.execute(registers, (instruction & Opcodes.R1_MASK) >> Opcodes.R1_SHIFT, (instruction & Opcodes.R2_MASK) >> Opcodes.R2_SHIFT, instruction & Opcodes.OPERAND_MASK);
                break;
            case Opcodes.II_BNZ:
                nextIns = BNZ.execute(registers, (instruction & Opcodes.R1_MASK) >> Opcodes.R1_SHIFT, instruction & Opcodes.OPERAND_MASK);
                break;
            case Opcodes.II_HALT:
                HALT.execute(registers);
                break;
            default:
                throw new Exception("Unknown instruction in CC|II fields: " + (instruction & Opcodes.INS_MASK));
        }

        if (nextIns == -1)
            registers.setPC(registers.getPC() + 1);
        else
            registers.setPC(nextIns);
    }

    

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
