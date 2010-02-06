package com.zycus.dotproject.ui;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;

import com.zycus.dotproject.util.MenuUtility;

public class DotProjectColumnSelectorPopupMenu extends JPopupMenu {
	private JMenuItem	mnuExpandAll	= null;
	private JMenuItem	mnuCollapseAll	= null;
	
	public DotProjectColumnSelectorPopupMenu() {
		initComponents();
	}

	private void initComponents() {
		add(mnuExpandAll = MenuUtility.getMenuItem("Expand All", null));
		add(mnuCollapseAll = MenuUtility.getMenuItem("Collapse All", null));
	}
}