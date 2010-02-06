package com.zycus.dotproject.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.zycus.dotproject.ui.event.ViewChangeListener;

public class DotProjectContainer extends JPanel implements ViewChangeListener{
	private ProjectTaskArea taskArea = null;
	public DotProjectContainer() {
		init();
		initLayout();
	}

	private void init() {
		taskArea = new ProjectTaskArea(new TreeViewProjectModel());
		//taskArea = new ProjectTaskArea(new FileSystemModel());
	}

	private void initLayout() {
		setLayout(new BorderLayout());
		add(new JScrollPane(taskArea));
	}

	public void viewChanged(ViewType viewType) {
		taskArea.dataChanged();
	}
}