package com.x338x;

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
	 
	 public void Operations()
	 {
		 
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
	 

}
