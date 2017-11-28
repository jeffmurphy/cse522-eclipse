package com.x338x;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Operations {
	
	 private static String[] iPats = {
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
	 
	 	private Boolean pending = false;
	    private String pendingLabel;

	    private String i;
	    private Map<String, Statement> labelMap;

	    private int labelNum = -1;
	    private String instruction = "";
	    private String arg1 = "";
	    private String arg2 = "";
	    private Boolean addrRef = false;

	    private int byteCode;
	    
	public Operations()
	{
		
	}
	 
	 public Operations(String i, Map<String, Statement> labelMap) throws Exception
	 {
		   this.i = i.trim().replaceAll(" +", " ");
	        this.labelMap = labelMap;
	        parse();
	 }
	 
	 public static boolean isValidOperation(String i)
	 {
		 boolean err = true;

	        for (String ip : iPats) {
	            Pattern lp = Pattern.compile(ip);
	            Matcher m = lp.matcher(i);

	            if (m.find()) {
	                err = false;
	                String tmp = m.group(1);
	                
	            }
	            
	           }
	        return !err;
	 }
	 
	 public void parse() throws Exception {
	        boolean err = true;

	        for (String ip : iPats) {
	            Pattern lp = Pattern.compile(ip);
	            Matcher m = lp.matcher(i);

	            if (m.find()) {
	                err = false;

	                instruction = m.group(1);

	                // if it's an instruction that uses a label, try to resolve the label

	                if (instruction.equals("BEQ") || instruction.equals("BGT") || instruction.equals("BLT")) {
	                    Statement ln = labelMap.get(m.group(4));
	                    arg1 = m.group(2);
	                    arg2 = m.group(3);
	                    if (ln == null) {
	                        pending = true;
	                        pendingLabel = m.group(3);
	                    }
	                    else {
	                        labelNum = ln.iNum;
	                        convert(instruction);
	                    }
	                }

	                else if (instruction.equals("BNZ")) {
	                    arg1 = m.group(2);
	                    Statement ln = labelMap.get(m.group(3));
	                    if (ln == null) {
	                        pending = true;
	                        pendingLabel = m.group(3);
	                    }
	                    else {
	                        labelNum = ln.iNum;
	                        convert(instruction);
	                    }
	                }

	                else if (instruction.equals("LD") || instruction.equals("ST")) {
	                    arg1 = m.group(2);
	                    addrRef = false;
	                    if (m.group(3).equals("@"))
	                        addrRef = true;
	                    arg2 = m.group(4);
	                    pending = false;
	                    labelNum = -1;
	                    convert(instruction);
	                }

	                else if (instruction.equals("ADD") || instruction.equals("SUB") ||
	                        instruction.equals("MUL") || instruction.equals("DIV")
	                        ) {
	                    arg1 = m.group(2);
	                    arg2 = m.group(3);
	                    pending = false;
	                    labelNum = -1;
	                    convert(instruction);
	                }

	                else if (instruction.equals("HALT")) {
	                    convert(instruction);
	                }
	            }
	        }

	        if (err)
	            throw new Exception("Can't parse line: " + i);
	    }

	    public void convert(String i) {
	        if (i.equals("LD"))
	            byteCode = LD.convert(arg1, arg2, addrRef);
	        else if (i.equals("ST"))
	            byteCode = ST.convert(arg1, arg2);
	        else if (i.equals("ADD"))
	            byteCode = ADD.convert(arg1, arg2);
	        else if (i.equals("SUB"))
	            byteCode = SUB.convert(arg1, arg2);
	        else if (i.equals("MUL"))
	            byteCode = MUL.convert(arg1, arg2);
	        else if (i.equals("DIV"))
	            byteCode = DIV.convert(arg1, arg2);
	        else if (i.equals("BNE"))
	            byteCode = BEQ.convert(arg1, arg2, labelNum);
	        else if (i.equals("BGT"))
	            byteCode = BGT.convert(arg1, arg2, labelNum);
	        else if (i.equals("BLT"))
	            byteCode = BLT.convert(arg1, arg2, labelNum);
	        else if (i.equals("BNZ"))
	            byteCode = BNZ.convert(arg1, labelNum);
	        else if (i.equals("HALT"))
	            byteCode = HALT.convert();
	    }
	    
	    public static int execute(String type,Registers registers, int[] memory, int instruction) throws Exception {
	    	int next =-1;
	    	switch(type){
	    	 case "LD":
	                LD.execute(registers, memory, (instruction & Opcodes.R1_MASK) >> Opcodes.R1_SHIFT, (instruction & Opcodes.R2_MASK) >> Opcodes.R2_SHIFT, instruction & Opcodes.OPERAND_MASK);
	                break;
	            case "ST":
	                ST.execute(registers, memory, (instruction & Opcodes.R1_MASK) >> Opcodes.R1_SHIFT, instruction & Opcodes.OPERAND_MASK);
	                break;
	            case "ADD":
	                ADD.execute(registers, (instruction & Opcodes.R1_MASK) >> Opcodes.R1_SHIFT, (instruction & Opcodes.R2_MASK) >> Opcodes.R2_SHIFT);
	                break;
	            case "SUB":
	                SUB.execute(registers, (instruction & Opcodes.R1_MASK) >> Opcodes.R1_SHIFT, (instruction & Opcodes.R2_MASK) >> Opcodes.R2_SHIFT);
	                break;
	            case "MUL":
	                MUL.execute(registers, (instruction & Opcodes.R1_MASK) >> Opcodes.R1_SHIFT, (instruction & Opcodes.R2_MASK) >> Opcodes.R2_SHIFT);
	                break;
	            case "DIV":
	                DIV.execute(registers, (instruction & Opcodes.R1_MASK) >> Opcodes.R1_SHIFT, (instruction & Opcodes.R2_MASK) >> Opcodes.R2_SHIFT);
	                break;
	            case "BEQ":
	                next = BEQ.execute(registers, (instruction & Opcodes.R1_MASK) >> Opcodes.R1_SHIFT, (instruction & Opcodes.R2_MASK) >> Opcodes.R2_SHIFT, instruction & Opcodes.OPERAND_MASK);
	                break;
	            case "BGT":
	                next = BGT.execute(registers, (instruction & Opcodes.R1_MASK) >> Opcodes.R1_SHIFT, (instruction & Opcodes.R2_MASK) >> Opcodes.R2_SHIFT, instruction & Opcodes.OPERAND_MASK);
	                break;
	            case "BLT":
	                next = BLT.execute(registers, (instruction & Opcodes.R1_MASK) >> Opcodes.R1_SHIFT, (instruction & Opcodes.R2_MASK) >> Opcodes.R2_SHIFT, instruction & Opcodes.OPERAND_MASK);
	                break;
	            case "BNZ":
	                next = BNZ.execute(registers, (instruction & Opcodes.R1_MASK) >> Opcodes.R1_SHIFT, instruction & Opcodes.OPERAND_MASK);
	                break;
	            case "HALT":
	                HALT.execute(registers);
	                break;
	            default:
	                throw new Exception("Unknown instruction in CC|II fields: " + (instruction & Opcodes.INS_MASK));

	    	}
	    		return next;
	    	}
	    
	    
	    
	    
	    public Boolean getPending() {
	        return pending;
	    }

	    public int getByteCode() {
	        return byteCode;
	    }

	    public void setByteCode(int byteCode) {
	        this.byteCode = byteCode;
	    }

	    public int getLabelNum() {
	        return labelNum;
	    }

	    public String getInstruction() {
	        return instruction;
	    }

	    public String getArg1() {
	        return arg1;
	    }

	    public String getArg2() {
	        return arg2;
	    }

	    public String getPendingLabel() {
	        return pendingLabel;
	    }

	 

}
