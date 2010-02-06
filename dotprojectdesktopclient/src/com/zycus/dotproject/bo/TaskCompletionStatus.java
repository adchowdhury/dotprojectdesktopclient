package com.zycus.dotproject.bo;

import java.awt.Color;

public enum TaskCompletionStatus {
	Completed(Color.GREEN),
	Late(Color.RED.brighter().brighter()),
	ShouldHaveStarted(Color.ORANGE),
	OnTimeGoing(new Color(195,236,195)),
	Normal(null);
	
	private Color displayColor = null;
	
	private TaskCompletionStatus(Color a_displayColor) {
		displayColor = a_displayColor;
	}
	
	public Color getDisplayColor() {
		return displayColor;
	}
}