package com.zycus.dotproject.bo;

public enum FileCategory {
	Unknown(0), Document(1), Application(2);

	private int	value	= 0;

	public int getValue() {
		return value;
	}

	FileCategory(int value) {
		this.value = value;
	}

	public static FileCategory valueOf(int id) {
		switch ((int) id) {
			case 1:
				return Document;
			case 2:
				return Application;
			default:
				return Unknown;
		}
	}
}