package com.zycus.dotproject.bo;

public enum TaskMilestone {
	NotAMilestone(0),
	MileStone(1);
	
	private int value = 0;
	
	public int getValue() {
		return value;
	}
	
	TaskMilestone(int value)
	{
		this.value = value;
	}
	
	public static TaskMilestone valueOf(int id) {
		switch ((int) id) {
		case 0:
			return NotAMilestone;
		case 1:
			return MileStone;
		default:
			return MileStone;
		}
	}
}