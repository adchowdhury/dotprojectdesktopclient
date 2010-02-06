package com.zycus.dotproject.ui.event;

public interface ButtonBarListener {
	static enum ActionType{
		Add, Save, Delete, Cancel
	}
	void actionPerformed(ActionType actionType);
}