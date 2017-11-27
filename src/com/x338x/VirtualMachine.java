package com.x338x;

import javax.swing.*;
import java.util.List;

public class VirtualMachine {
    private int[] memory;
    private ByteCodes byteCodes;

    Registers registers;
    MemoryTableModel mtm;
    JTable bct;

    JLabel pcLabel, stLabel, aregLabel, bregLabel;

    public VirtualMachine(MemoryTableModel _mtm,
                          JTable _bct,
                          JLabel _pcLabel,
                          JLabel _stLabel,
                          JLabel _aregLabel,
                          JLabel _bregLabel) {
        memory = new int[10];
        registers = new Registers();

        bct = _bct;
        mtm = _mtm;
        mtm.updateMemory(memory);
        pcLabel = _pcLabel;
        stLabel = _stLabel;
        aregLabel = _aregLabel;
        bregLabel = _bregLabel;

        visualizeRegisters();
    }
    
    public VirtualMachine(){
    	memory= new int[10];
    	registers=new Registers();
    }

    private void visualizeRegisters() {
        pcLabel.setText("PC: " + registers.getPC());
        stLabel.setText("ST: " + registers.getST_asString());
        aregLabel.setText(" A: " + registers.getA());
        bregLabel.setText(" B: " + registers.getB());
    }

    public ByteCodes getByteCodes() {
        return byteCodes;
    }

    public void setByteCodes(ByteCodes byteCodes) {
        this.byteCodes = byteCodes;
    }

    public void reset() {
        registers.reset();
        memory = new int[10];
        mtm.updateMemory(memory);
        visualizeRegisters();
    }

    public void step() throws Exception {
        if (byteCodes != null && byteCodes.getCodes().size() > 0) {
            System.out.printf("stepping over instruction %d%n", registers.getPC());

            if ((registers.getST() & Registers.HALT) != Registers.HALT) {
                int iNum = registers.getPC();
                registers.setST(0);
               // bct.setRowSelectionInterval(iNum, iNum);

                if (iNum > byteCodes.getCodes().size())
                    registers.setST(registers.getST() | Registers.BUSERROR | Registers.HALT);
                else
                    Instruction.execute(registers, memory, byteCodes.getCodes().get(iNum));

               // visualizeRegisters();
               // mtm.fireTableDataChanged();
            } else
                throw new Exception("Execution finished.");

        }
        else {
            registers.setST(registers.getST() | Registers.BUSERROR | Registers.HALT);
            throw new Exception("Bytecode empty. Compile first?");
        }
    }

    public void run() throws Exception {
        if (byteCodes != null && byteCodes.getCodes().size() > 0) {
            reset();
            System.out.println("running " + byteCodes.getCodes().size() + " instructions.");

            while ((registers.getST() & Registers.HALT) != Registers.HALT) {
                int iNum = registers.getPC();
                registers.setST(0);
                bct.setRowSelectionInterval(iNum, iNum);

                if (iNum > byteCodes.getCodes().size())
                    registers.setST(registers.getST() | Registers.BUSERROR | Registers.HALT);
                else
                    Instruction.execute(registers, memory, byteCodes.getCodes().get(iNum));

                visualizeRegisters();
                mtm.fireTableDataChanged();
            }
            throw new Exception("Execution finished.");
        }
        else {
            registers.setST(registers.getST() | Registers.BUSERROR | Registers.HALT);
            throw new Exception("Bytecode empty. Compile first?");
        }
    }

}
