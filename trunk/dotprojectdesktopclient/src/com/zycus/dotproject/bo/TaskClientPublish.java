package com.zycus.dotproject.bo;

public enum TaskClientPublish {
	DoNotPublish(0),
	Publish(1);
	
	private int value = 0;
	
	public int getValue() {
		return value;
	}
	
	TaskClientPublish(int value)
	{
		this.value = value;
	}
	
	public static TaskClientPublish valueOf(int id) {
		switch ((int) id) {
		case 0:
			return DoNotPublish;
		case 1:
			return Publish;
		default:
			return Publish;
		}
	}
}