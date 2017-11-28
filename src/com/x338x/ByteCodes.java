package com.x338x;

import java.util.ArrayList;
import java.util.List;

public class ByteCodes {
	
    private List<Instruction> codes;
    
    public ByteCodes()
    {
    	setCodes(new ArrayList<>());
    }

	public List<Instruction> getCodes() {
		return codes;
	}

	public void setCodes(List<Instruction> codes) {
		this.codes = codes;
	}


}
