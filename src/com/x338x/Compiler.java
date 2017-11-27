package com.x338x;
import java.awt.*;
import java.util.*;
import java.util.List;
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

        System.out.println(iNum + ") lab: " + label + " ins: " + instruction);
        
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
            byteCodes.getCodes().add(0); // placeholder
        }
        else {
            if (byteCodes.getCodes().size() == iNum)
                byteCodes.getCodes().add(op.getByteCode());
            else
                byteCodes.getCodes().set(iNum, op.getByteCode());
        }

        iNum += 1;
    }


}
