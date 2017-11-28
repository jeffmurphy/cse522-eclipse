package com.x338x;

public class ADD extends Operations {

    /*
     bytecode is 16 bits:

     0000 0000 0000 0000
     CCII R1R2 OPER AND.

     CC = 01 : ALU (ADD/SUB/MUL/DIV)

     II = 00 : ADD
          01 : SUB
          10 : MUL
          11 : DIV

     R1 = 00 : A
          01 : B
     R2 = 00 : A
          01 : B

     OPERAND = ignored

     */

	/*
	 * @Requires({(arg1.equals("A") || arg1.equals("B")) && (arg2.equals("A") || arg2.equals("B"))})
	 * @Ensures({(bc & Opcodes.II_ADD == Opcodes.II_ADD)})
	 */
    public static int convert(String arg1, String arg2) {
        int bc = Opcodes.II_ADD;

        // ADD A, A is ok

        if (arg1.equals("A"))
            bc |= Opcodes.REGA << Opcodes.R1_SHIFT;
        else if (arg1.equals("B"))
            bc |= Opcodes.REGB << Opcodes.R1_SHIFT;

        if (arg2.equals("A"))
            bc |= Opcodes.REGA << Opcodes.R2_SHIFT;
        else if (arg2.equals("B"))
            bc |= Opcodes.REGB << Opcodes.R2_SHIFT;

        return bc;
    }

	/*
	 * @Requires({r != null && (dstreg == Opcodes.REGA || dstreg == Opcodes.REGB) && (srcreg == Opcodes.REGA || srcreg == Opcodes.REGB)})
	 * @Ensures({Instruction.getregval(r, dstreg) == ((srcval + dstval) && 0xFF)})
	 */
    public static void execute(Registers r, int dstreg, int srcreg) throws Exception {
        int srcval = Instruction.getregval(r, srcreg), dstval = Instruction.getregval(r, dstreg);

        // our system is 8 bit
        if (srcval + dstval > 255)
            r.setST(r.getST() | Registers.OVERFLOW);

        Instruction.setregval(r, dstreg, srcval + dstval); // Register() class will enforce 8 bit
    }

}
