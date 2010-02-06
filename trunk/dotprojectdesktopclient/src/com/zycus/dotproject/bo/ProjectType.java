package com.zycus.dotproject.bo;

public enum ProjectType {
	Unknown(0),
	Administrative(1),
	Operative(2);
	
	private int value = -1;
	
	public int getValue() {
		return value;
	}
	
	ProjectType(int value)
	{
		this.value = value;
	}
	
	public static ProjectType valueOf(int id) {
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