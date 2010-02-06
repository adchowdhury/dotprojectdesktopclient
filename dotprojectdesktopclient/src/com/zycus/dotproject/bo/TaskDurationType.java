package com.zycus.dotproject.bo;

public enum TaskDurationType {
	Hours(1),
	Days(24);
	
	private int value = 24;
	
	public int getValue() {
		return value;
	}
	
	TaskDurationType(int value)
	{
		this.value = value;
	}
	
	public static TaskDurationType valueOf(int id) {
		switch ((int) id) {
		case 1:
			return Hours;
		case 24:
			return Days;
		default:
			return Days;
		}
	}
}