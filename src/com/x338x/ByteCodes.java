package com.x338x;

import java.util.ArrayList;
import java.util.List;

public class ByteCodes {
	
    private List<Integer> codes;
    
    public ByteCodes()
    {
    	setCodes(new ArrayList<>());
    }

	public List<Integer> getCodes() {
		return codes;
	}

	public void setCodes(List<Integer> codes) {
		this.codes = codes;
	}


}
