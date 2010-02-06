package com.zycus.dotproject.util;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.zycus.dotproject.ui.DotProjectActionListener;

public class MenuUtility {
	private static DotProjectActionListener	commonActionListener	= new DotProjectActionListener();

	public static JMenu getMenu(String strCaption) {
		JMenu menuReturn = new JMenu();
		menuReturn.setText(strCaption);
		return menuReturn;
	}

	public static JMenuItem getMenuItem(String strCaption, AbstractAction action) {
		JMenuItem menuReturn = new JMenuItem();
		if (action == null) {
			menuReturn.addActionListener(commonActionListener);
		} else {
			menuReturn.setAction(action);
		}
		menuReturn.setText(strCaption);
		return menuReturn;
	}
}
