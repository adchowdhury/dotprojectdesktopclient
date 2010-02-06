package com.zycus.dotproject.bo;

public enum Priority {
	Low(-1),
	Normal(0),
	High(1);
	
	private int value = 0;
	
	public int getValue() {
		return value;
	}
	
	Priority(int value)
	{
		this.value = value;
	}
	
	public static Priority valueOf(int id) {
		switch ((int) id) {
		case -1:
			return Low;
		case 0:
			return Normal;
		case 1:
			return High;
		default:
			return Normal;
		}
	}
}