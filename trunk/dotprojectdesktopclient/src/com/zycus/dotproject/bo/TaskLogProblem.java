package com.zycus.dotproject.bo;

public enum TaskLogProblem {
	NotAProblem(0),
	Problem(1);
	
	private int value = 0;
	
	public int getValue() {
		return value;
	}
	
	TaskLogProblem(int value)
	{
		this.value = value;
	}
	
	public static TaskLogProblem valueOf(int id) {
		switch ((int) id) {
		case 0:
			return NotAProblem;
		case 1:
			return Problem;
		default:
			return Problem;
		}
	}
}