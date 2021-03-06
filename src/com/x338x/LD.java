package com.x338x;

public class LD extends Operations {

    /*
     bytecode is 16 bits:

     0000 0000 0000 0000
     CCII R1R2 OPER AND.

     CC = 00 : assignment (LD/ST)

     II = 00 : LD
          01 : ST

     R1 = 00 : A
          01 : B
     R2 = 00 : ignored
          02 : ignored

     OPERAND = memory address

     */

    public static int convert(String arg1, String arg2, Boolean address) {
        int bc = Opcodes.II_LD;

        if (arg1.equals("A"))
            bc |= Opcodes.REGA << Opcodes.R1_SHIFT;
        else if (arg1.equals("B"))
            bc |= Opcodes.REGB << Opcodes.R1_SHIFT;

        // R2 indicates of the operand is a memory address or literal

        if (address)
            bc |= Opcodes.ADDRESS << Opcodes.R2_SHIFT;
        else
            bc |= Opcodes.LITERAL << Opcodes.R2_SHIFT;

        int memloc = Integer.parseInt(arg2);
        bc |= (Opcodes.OPERAND_MASK & memloc);

        return bc;
    }

    public static void execute(Registers r, Memory memory, int reg, int reg2, int operand) throws Exception {
        int val = operand;

        if (reg2 == Opcodes.ADDRESS) {
            if (operand >= memory.getBlocks().length) {
                r.setST(r.getST() | Registers.BUSERROR | Registers.HALT);
                return;
            }
            val = memory.getBlocks()[operand];
        }

        switch (reg) {
            case Opcodes.REGA:
                r.setA(val);
                break;
            case Opcodes.REGB:
                r.setB(val);
                break;
            default:
                throw new Exception("Invalid register specified: " + reg);
        }

    }
}
