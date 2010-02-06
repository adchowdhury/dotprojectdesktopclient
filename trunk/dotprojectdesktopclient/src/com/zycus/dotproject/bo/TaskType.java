package com.zycus.dotproject.bo;

public enum TaskType {
	Unknown(0),
	Administrative(1),
	Operative(2);
	
	private int value = 0;
	
	public int getValue() {
		return value;
	}
	
	TaskType(int value)
	{
		this.value = value;
	}
	
	public static TaskType valueOf(int id) {
		switch ((int) id) {
		case 0:
			return Unknown;
		case 1:
			return Administrative;
		case 2:
			return Operative;
		default:
			return Unknown;
		}
	}
}