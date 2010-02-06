package com.zycus.dotproject.bo;

public enum HistoryAction {

	Add("add"),
	Update("update"),
	Delete("delete");
	
	private String value = "update";
	
	public String getValue() {
		return value;
	}
	
	HistoryAction(String value)
	{
		this.value = value;
	}
}