package com.x338x;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//http://jakubdziworski.github.io/java/2016/04/01/antlr_visitor_vs_listener.html

public class Compiler {
    private int iNum = 0;
    ByteCodes byteCodes = new ByteCodes();

    /* map 'label' to instruction number / instruction */
    private Map<String, Statement> labelMap = new HashMap<String, Statement>();
    /* map labels pending resolution to inst num / instruction */
    private Map<String, Statement> pendingLabelMap = new HashMap<String, Statement>();
    
    public Compiler(){
   	
    	defineValidOps();
    }
    
    public void defineValidOps()
	 {
    	String[] pat = {
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
    	
    	
		 Operations.setiPats(pat);
	 }
    /*
     * @Requires({"!program.isEmpty() && program !=null"})
     * @Ensures({"byteCodes != null && byteCodes.getCodes().size() >0"})
     */
    public void compile(String program) throws Exception {
        try {
            labelMap.clear();
            pendingLabelMap.clear();
            iNum = 0;
            byteCodes.getCodes().clear();

            for (String line : program.split("\n")) {
                checkSyntax(line);
            }
            labelMap.clear();
            pendingLabelMap.clear();
            iNum = 0;
            byteCodes.getCodes().clear();
            
            convertToByteCode(program);

        } catch(Exception e) {
            System.out.println("parse failed: " + e);
            e.printStackTrace();
            throw e;
        }
    }
    
    /*
     * @Requires ({"isprogramclean == true"})
     * @Ensures ({"for(String line : program.split("\n"))
     * 					{
     * 						convert(line) == byteCodes(extractLineNum(line))
     * 					}"})
     */
    public void convertToByteCode(String program){
    	
    	try
    	{
    		for (String line : program.split("\n")) {
                parseLine(line);
            }
    	
    		Iterator<Entry<String, Statement>> it = pendingLabelMap.entrySet().iterator();
    		while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Statement ln = pendingLabelMap.get(pair.getKey());

            System.out.println("plab: " + pair.getKey());
            if (labelMap.containsKey(pair.getKey())) {
                iNum = ln.iNum;
                parseLine(ln.instruction);
            }
            else {
                System.out.println("Tried to recompile '" + ln.instruction +
                        "' but label is still not known: " + pair.getKey());
                throw new Exception("Failed to resolve label " + pair.getKey());
            }
        }
    	}
    	catch(Exception e)
    	{
    		System.out.println(e.getMessage().toString());
    	}
    }
    
    
    /* 
     * @Requires ({"! line.isEmpty() && line !=null"})
     * @Ensures ({"match(line,label_pattern) == true
     * 				&&
     * 				ExtractOperation(line).isValid == true"})
     */
    public void checkSyntax(String line) throws Exception {
        String label_pattern = "(\\S+):\\s*(.*)";
        Pattern lp = Pattern.compile(label_pattern);
        Matcher m = lp.matcher(line);
        String instruction = line;
        String label = null;
        
        line = line.trim().replaceAll(" +", " ");
        if (line.equals("") || line.startsWith(";")) return;

        if (m.find()) {
            label = m.group(1);
            instruction = m.group(2);

            if (labelMap.get(label) == null)
                labelMap.put(label, new Statement(iNum, instruction));
            else {
                Statement ln = labelMap.get(label);
                if (ln.iNum != iNum)
                    throw new Exception("Duplicate label " + ln.iNum + " and " + iNum);
            }
        }

        System.out.println(iNum + ") lab: " + label + " stmnt: " + instruction);
        
        if(!Operations.isValidOperation(instruction))
        	throw new Exception("Can't parse line: "+instruction);
        
        

        iNum += 1;
    }

    public void parseLine(String line) throws Exception {
        String label_pattern = "(\\S+):\\s*(.*)";
        Pattern lp = Pattern.compile(label_pattern);
        Matcher m = lp.matcher(line);
        String instruction = line;
        String label = null;

        line = line.trim().replaceAll(" +", " ");
        if (line.equals("") || line.startsWith(";")) return;

        if (m.find()) {
            label = m.group(1);
            instruction = m.group(2);

            if (labelMap.get(label) == null)
                labelMap.put(label, new Statement(iNum, instruction));
            else {
                Statement ln = labelMap.get(label);
                if (ln.iNum != iNum)
                    throw new Exception("Duplicate label " + ln.iNum + " and " + iNum);
            }
        }

        System.out.println(iNum + ") lab: " + label + " ins: " + instruction);
        Operations op = new Operations(instruction, labelMap);

        if (op.getPending()) {
            pendingLabelMap.put(op.getPendingLabel(), new Statement(iNum, instruction));
            byteCodes.getCodes().add(new Instruction(0)); // placeholder
        }
        else {
            if (byteCodes.getCodes().size() == iNum)
                byteCodes.getCodes().add(new Instruction(op.getByteCode()));
            else
                byteCodes.getCodes().set(iNum,new Instruction(op.getByteCode()));
        }

        iNum += 1;
    }


}
