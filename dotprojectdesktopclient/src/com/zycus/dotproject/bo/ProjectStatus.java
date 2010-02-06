package com.zycus.dotproject.bo;

public enum ProjectStatus 
{
	ProjectNotDefined(0),
	ProjectProposed(1),
	ProjectInPlaning(2),
	ProjectInProgress(3),
	ProjectOnHold(4),
	ProjectCompleted(5);
	
	private int value = -1;
	
	public int getValue() {
		return value;
	}
	
	ProjectStatus(int value)
	{
		this.value = value;
	}
	
	public static ProjectStatus valueOf(int id) {
		switch ((int) id) {
		case 0:
			return ProjectNotDefined;
		case 1:
			return ProjectProposed;
		case 2:
			return ProjectInPlaning;
		case 3:
			return ProjectInProgress;
		case 4:
			return ProjectOnHold;
		case 5:
			return ProjectCompleted;
		default:
			return ProjectNotDefined;
		}
	}
}