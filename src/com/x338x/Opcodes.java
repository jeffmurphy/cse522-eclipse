package com.x338x;

public class Opcodes {
	 public final static int CC_SHIFT      = 14;
	    public final static int II_SHIFT      = 12;
	    public final static int R1_SHIFT      = 10;
	    public final static int R2_SHIFT      =  8;
	    public final static int CC_MASK       = 0xC000;
	    public final static int II_MASK       = 0x3000;
	    public final static int R1_MASK       = 0x0C00;
	    public final static int R2_MASK       = 0x0300;
	    public final static int OPERAND_MASK  = 0x00FF;

	    public final static int INS_MASK      = CC_MASK | II_MASK;

	    public final static int CC_ASSIGN     = 0x0;
	    public final static int II_LD         = (CC_ASSIGN << CC_SHIFT) | (0x0 << II_SHIFT);
	    public final static int II_ST         = (CC_ASSIGN << CC_SHIFT) | (0x1 << II_SHIFT);

	    public final static int CC_ALU        = 0x1;
	    public final static int II_ADD        = (CC_ALU << CC_SHIFT) | (0x0 << II_SHIFT);
	    public final static int II_SUB        = (CC_ALU << CC_SHIFT) | (0x1 << II_SHIFT);
	    public final static int II_MUL        = (CC_ALU << CC_SHIFT) | (0x2 << II_SHIFT);
	    public final static int II_DIV        = (CC_ALU << CC_SHIFT) | (0x3 << II_SHIFT);

	    public final static int CC_BRANCH     = 0x2;
	    public final static int II_BEQ        = (CC_BRANCH << CC_SHIFT) | (0x0 << II_SHIFT);
	    public final static int II_BGT        = (CC_BRANCH << CC_SHIFT) | (0x1 << II_SHIFT);
	    public final static int II_BLT        = (CC_BRANCH << CC_SHIFT) | (0x2 << II_SHIFT);
	    public final static int II_BNZ        = (CC_BRANCH << CC_SHIFT) | (0x3 << II_SHIFT);

	    public final static int CC_HALT       = 0x3;
	    public final static int II_HALT       = (CC_HALT << CC_SHIFT);

	    public final static int REGA          = 0b00;
	    public final static int REGB          = 0b01;
	    public final static int LITERAL       = 0b10;
	    public final static int ADDRESS       = 0b11;
}
