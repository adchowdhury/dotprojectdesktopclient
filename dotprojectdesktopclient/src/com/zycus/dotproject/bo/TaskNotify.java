package com.zycus.dotproject.bo;

public enum TaskNotify {
	DoNotNotify(0),
	Notify(1);
	
	private int value = 0;
	
	public int getValue() {
		return value;
	}
	
	TaskNotify(int value)
	{
		this.value = value;
	}
	
	public static TaskNotify valueOf(int id) {
		switch ((int) id) {
		case 0:
			return DoNotNotify;
		case 1:
			return Notify;
		default:
			return DoNotNotify;
		}
	}
}