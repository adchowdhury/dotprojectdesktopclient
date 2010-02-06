package com.zycus.dotproject.bo;

public enum HistoryTable {
	
	Tasks("tasks"),
	Project("projects"),
	TaskLog("task_log");
	
	private String value = "projects";
	
	public String getValue() {
		return value;
	}
	
	HistoryTable(String value)
	{
		this.value = value;
	}
}