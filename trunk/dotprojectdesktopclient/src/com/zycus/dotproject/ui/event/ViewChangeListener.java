package com.zycus.dotproject.ui.event;

public interface ViewChangeListener {
	public static enum ViewType{
		ResourceView,
		TreeView
	}
	void viewChanged(ViewType viewType);
}