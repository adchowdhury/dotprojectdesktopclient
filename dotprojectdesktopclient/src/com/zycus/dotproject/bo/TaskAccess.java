package com.zycus.dotproject.bo;

public enum TaskAccess {
	Public(0),
	Protected(1),
	Participant(2),
	Private(3),
	Privileged(4);
	
	private int value = 0;
	
	public int getValue() {
		return value;
	}
	
	TaskAccess(int value)
	{
		this.value = value;
	}
	
	public static TaskAccess valueOf(int id) {
		switch ((int) id) {
		case 0:
			return Public;
		case 1:
			return Protected;
		case 2:
			return Participant;
		case 3:
			return Private;
		case 4:
			return Privileged;
		default:
			return Public;
		}
	}
}