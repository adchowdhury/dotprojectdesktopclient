package com.zycus.dotproject.ui;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import com.zycus.dotproject.bo.BOProject;
import com.zycus.dotproject.ui.event.ProjectSelectionListener;
import com.zycus.dotproject.ui.event.ViewChangeListener;
import com.zycus.dotproject.util.ApplicationContext;

public class DotProjectToolbar extends JToolBar implements ProjectSelectionListener {
	private ProjectSelectorBar	projectBar	= null;
	private ViewBar				viewBar		= null;
	private JButton				btnSave		= null;

	public DotProjectToolbar() {
		init();
		initLayout();

	}

	@Override
	public boolean isFloatable() {
		return false;
	}

	private void init() {
		projectBar = new ProjectSelectorBar();
		viewBar = new ViewBar();
		btnSave = new JButton(new DotProjectActionListener.SaveProjectAction());
		btnSave.setIcon(IconHelper.getSaveIcon());
		btnSave.setEnabled(false);
		btnSave.setToolTipText("save project into database");
		btnSave.setFocusable(false);
		ApplicationContext.addProjectSelectionListener(this);
	}

	private void initLayout() {
		JPanel pnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pnl.add(btnSave);
		pnl.add(projectBar);
		//addSeparator();
		pnl.add(viewBar);
		add(pnl);
	}

	public void projectSelected(BOProject project) {
		btnSave.setEnabled(project != null);
	}
	
	public void addViewChangelistener(ViewChangeListener listener) {
		viewBar.addViewChangelistener(listener);
	}
	
	public void removeViewChangelistener(ViewChangeListener listener) {
		viewBar.removeViewChangelistener(listener);
	}
}