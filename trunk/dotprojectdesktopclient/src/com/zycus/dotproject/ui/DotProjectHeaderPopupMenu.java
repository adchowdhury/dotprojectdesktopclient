package com.zycus.dotproject.ui;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;

import com.zycus.dotproject.ui.TreeViewProjectModel.Columns;

public class DotProjectHeaderPopupMenu extends JPopupMenu {
	private JCheckBoxMenuItem[] menuItems = new JCheckBoxMenuItem[Columns.values().length];
	
	public DotProjectHeaderPopupMenu(ProjectTaskArea projectTaskArea) {
		initComponents(projectTaskArea);
	}
	
	private void initComponents(ProjectTaskArea projectTaskArea) {
		for(Columns col : Columns.values()) {
			menuItems[col.ordinal()] = new JCheckBoxMenuItem();
			menuItems[col.ordinal()].addActionListener(new ProjectTaskArea.ColumnShowHideAction(projectTaskArea));
			menuItems[col.ordinal()].setText(col.toString());
			menuItems[col.ordinal()].setSelected(col.isVisible());
			add(menuItems[col.ordinal()]);
		}
	}
	
	public void updateSelectedState() {
		for(Columns col : Columns.values()) {
			menuItems[col.ordinal()].setSelected(col.isVisible());
		}
	}
}