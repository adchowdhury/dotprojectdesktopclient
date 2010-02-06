package com.zycus.dotproject.ui;

import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.zycus.dotproject.api.IProjectManager;
import com.zycus.dotproject.bo.BOProject;
import com.zycus.dotproject.bo.ProjectStatus;
import com.zycus.dotproject.factory.ProjectManagerFactory;
import com.zycus.dotproject.util.ApplicationContext;
import com.zycus.dotproject.util.DialogUtility;

public class ProjectSelectorBar extends JPanel {
	private JComboBox		projectListsCombo			= null;
	private JComboBox		projectStatus				= null;
	private JButton			btnRefresh					= null;
	private IProjectManager	projectManager				= null;
	private boolean			isProjectSelectionAllowed	= false;

	public ProjectSelectorBar() {
		init();
		initLayout();
	}

	private void init() {
		projectStatus = new JComboBox(ProjectStatus.values());
		projectStatus.setSelectedIndex(-1);
		projectStatus.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent itemEvent) {
				if (itemEvent.getStateChange() != ItemEvent.SELECTED) {
					return;
				}
				Thread th = new Thread(new Runnable() {
					public void run() {
						loadProjects();
						DialogUtility.hideWaitDialog();
					}
				});
				th.setName("Projects loading thread");
				th.start();
				DialogUtility.showWaitDialog("Loading all projects, please wait....");
			}
		});
		projectManager = ProjectManagerFactory.getProjectManager();
		projectListsCombo = new JComboBox();
		// loadProjects();
		projectListsCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent itemEvent) {
				if (itemEvent.getStateChange() != ItemEvent.SELECTED) {
					return;
				}
				if(isProjectSelectionAllowed == false) {
					return;
				}
				if (projectListsCombo.getSelectedItem() != null) {
					Thread th = new Thread(new Runnable() {
						public void run() {
							ApplicationContext.setCurrentProjet((BOProject) projectListsCombo.getSelectedItem());
							DialogUtility.hideWaitDialog();
						}
					});
					projectListsCombo.hidePopup();
					th.setName("Project loading thread");
					th.start();
					DialogUtility.showWaitDialog("Loading project, please wait....");
				} else {
					ApplicationContext.setCurrentProjet(null);
				}

			}
		});
		projectListsCombo.setEditable(false);
		btnRefresh = new JButton();
		//btnRefresh.setMargin(new Insets(2, 2, 2, 2));
		btnRefresh.setAction(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				Thread th = new Thread(new Runnable() {
					public void run() {
						loadProjects();
						DialogUtility.hideWaitDialog();
					}
				});
				th.setName("Projects loading thread");
				th.start();
				DialogUtility.showWaitDialog("Loading all projects, please wait....");

			}
		});
		btnRefresh.setToolTipText("Reloads all projects from database and discard current changes");
		btnRefresh.setIcon(IconHelper.getRefreshIcon());
	}

	private void loadProjects() {
		if (projectStatus.getSelectedIndex() < 0) {
			return;
		}
		isProjectSelectionAllowed = false;
		ApplicationContext.setCurrentProjet(null);
		List<BOProject> projects = projectManager.getAllProjects(null, ApplicationContext.getCurrentUser(), (ProjectStatus) projectStatus.getSelectedItem());
		Collections.sort(projects);
		projectListsCombo.removeAllItems();
		for (BOProject project : projects) {
			projectListsCombo.addItem(project);
		}
		projectListsCombo.setSelectedItem(null);
		projectListsCombo.setSelectedIndex(-1);
		isProjectSelectionAllowed = true;
	}

	private void initLayout() {
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(new JLabel("Load projects with status : "));
		add(projectStatus);
		add(projectListsCombo);
		add(btnRefresh);
	}
}