package com.zycus.dotproject.ui.component;

import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class IndentBorder extends EmptyBorder {
	int	indent	= UIManager.getInt("Tree.leftChildIndent");

	public IndentBorder() {
		super(0, 0, 0, 0);
	}

	public void setDepth(int depth) {
		left = indent * depth;
	}

}