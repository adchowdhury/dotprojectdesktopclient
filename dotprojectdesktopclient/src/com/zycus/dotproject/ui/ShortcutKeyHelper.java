package com.zycus.dotproject.ui;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class ShortcutKeyHelper extends CustomJPanel {
	private JTable		table	= null;
	private String[]	columns	= { "Shortcut Key", "Definition" };
	private String[][]	value = {
			{"Ctrl + Ins", "Add child task to current selected Task"},
			{"Delete", "Delete currently all selected tasks"},
			{"Ctrl + *", "Expand or collapse current selection"}
	};
	public ShortcutKeyHelper() {
		init();
		initLayout();
	}

	private void init() {
		table = new JTable(new KeyTableModel());
	}

	private void initLayout() {
		setLayout(new BorderLayout());
		add(new JScrollPane(table), BorderLayout.CENTER);
	}

	private class KeyTableModel extends AbstractTableModel {

		public int getColumnCount() {
			return columns.length;
		}
		
		@Override
		public String getColumnName(int column) {
			return columns[column];
		}

		public int getRowCount() {
			return value.length;
		}
		
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			return value[rowIndex][columnIndex];
		}
	}
	
	@Override
	public void onExit() {
		table.removeAll();
		table = null;
	}
}