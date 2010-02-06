package com.zycus.dotproject.bo;

public enum TaskLogReference {
	
	NotDefined(0),
	Email(1),
	Helpdesk(2),
	PhoneCall(3),
	Fax(4);
	
	private int value = 0;
	
	public int getValue() {
		return value;
	}
	
	TaskLogReference(int value)
	{
		this.value = value;
	}
	
	public static TaskLogReference valueOf(int id) {
		switch ((int) id) {
		case 0:
			return NotDefined;
		case 1:
			return Email;
		case 2:
			return Helpdesk;
		case 3:
			return PhoneCall;
		case 4:
			return Fax;
		default:
			return NotDefined;
		}
	}
}