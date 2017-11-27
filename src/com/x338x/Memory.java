package com.x338x;

public class Memory {
	private int[] blocks;
	
	public Memory(int n)
	{
		this.setBlocks(new int[n]); 
	}

	public int[] getBlocks() {
		return blocks;
	}

	public void setBlocks(int[] blocks) {
		this.blocks = blocks;
	}

}
