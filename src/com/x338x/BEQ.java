package com.x338x;

public class BEQ extends Operations {

    /*
     bytecode is 16 bits:

     0000 0000 0000 0000
     CCII R1R2 OPER AND.

     CC = 10 : BRANCH

     II = 00 : BNE
          01 : BGT
          10 : BLT
          11 : BNZ

     R1 = 00 : A
          01 : B
     R2 = 00 : A
          01 : B

     OPERAND = instruction number to branch to

     */

    public static int convert(String arg1, String arg2, int labelNum) {
        int bc = Opcodes.II_BEQ;

        // ADD A, A is ok

        if (arg1.equals("A"))
            bc |= Opcodes.REGA << Opcodes.R1_SHIFT;
        else if (arg1.equals("B"))
            bc |= Opcodes.REGB << Opcodes.R1_SHIFT;

        if (arg2.equals("A"))
            bc |= Opcodes.REGA << Opcodes.R2_SHIFT;
        else if (arg2.equals("B"))
            bc |= Opcodes.REGB << Opcodes.R2_SHIFT;

        bc |= (Opcodes.OPERAND_MASK & labelNum);

        return bc;
    }

    public static int execute(Registers r, int dstreg, int srcreg, int operand) throws Exception {
        int srcval = Instruction.getregval(r, srcreg), dstval = Instruction.getregval(r, dstreg);

        // BEQ A, B, iNum   --->  if A == B then goto iNum else -1

        if (srcval == dstval)
            return operand;
        return -1;
    }

}
