package com.zycus.dotproject.bo;

public enum TaskDynamic {
	
	dependencyCheckingOn(31),
	dynamicTask(1);
	
	private int value = -1;
	
	public int getValue() {
		return value;
	}
	TaskDynamic(int value)
	{
		this.value = value;
	}
	
	public static TaskDynamic valueOf(int id) {
		switch ((int) id) {
		case 31:
			return dependencyCheckingOn;
		case 1:
			return dynamicTask;
		default:
			return dependencyCheckingOn;
		}
	}

}