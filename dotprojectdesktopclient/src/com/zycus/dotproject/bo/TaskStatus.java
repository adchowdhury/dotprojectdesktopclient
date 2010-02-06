package com.zycus.dotproject.bo;

public enum TaskStatus {
	Active(0),
	InActive(-1);
	
	private int value = -1;
	
	public int getValue() {
		return value;
	}
	
	TaskStatus(int value)
	{
		this.value = value;
	}
	
	public static TaskStatus valueOf(int id) {
		switch ((int) id) {
		case 0:
			return Active;
		case -1:
			return InActive;
		default:
			return Active;
		}
	}
}