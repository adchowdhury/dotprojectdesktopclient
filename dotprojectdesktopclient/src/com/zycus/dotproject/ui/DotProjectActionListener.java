package com.zycus.dotproject.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.zycus.dotproject.api.IProjectManager;
import com.zycus.dotproject.bo.BOProject;
import com.zycus.dotproject.exception.InsufficientPrivilagesException;
import com.zycus.dotproject.factory.ProjectManagerFactory;
import com.zycus.dotproject.util.ApplicationContext;
import com.zycus.dotproject.util.DialogUtility;

public class DotProjectActionListener implements ActionListener{

	public void actionPerformed(ActionEvent actionEvent) {
		if(actionEvent == null) {
			return;
		}
		Object Source = actionEvent.getSource();
		if(Source == null) {
			return;
		}
		//JOptionPane.showMessageDialog(null, "Feature not implemented yet :P");
	}
	
	static class SaveProjectAction extends AbstractAction{
		public void actionPerformed(ActionEvent actionEvent) {
			BOProject project = ApplicationContext.getCurrentProjet();
			try {
			
					IProjectManager projectManager = ProjectManagerFactory.getProjectManager();
					projectManager.saveProjectTasks(ApplicationContext.getCurrentUser(), project);
					//ApplicationContext.projectChanged(project);
					ApplicationContext.projectSaved();
					StatusBar.showInfoStatusMessage("Project saved");	
			}
			catch(InsufficientPrivilagesException exception)
			{
				JOptionPane.showMessageDialog(ApplicationContext.getCurrentFrame(), exception.getMessage(), "Error:", JOptionPane.ERROR_MESSAGE);
			}
			catch(Throwable a_th) {
				a_th.printStackTrace(System.err);
				JOptionPane.showMessageDialog(ApplicationContext.getCurrentFrame(), "Could not save proejct, please contact your vendor", "Error:", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	static class SettingAction extends AbstractAction{
		public void actionPerformed(ActionEvent actionEvent) {
			DialogUtility.showDialog(new Settings(), "Setting", new Dimension(675, 300));			
		}
	}
	
	static class ReportAction extends AbstractAction{
		public void actionPerformed(ActionEvent actionEvent) {
			DialogUtility.showDialog(new ReporManagementPanel(), "Reports", new Dimension(775, 450));			
		}
	}

	static class ExitAction extends AbstractAction{
		public void actionPerformed(ActionEvent actionEvent) {
			System.exit(0);
		}
	}
	
	static class AboutUSAction extends AbstractAction{
		public void actionPerformed(ActionEvent actionEvent) {
			DialogUtility.showDialog(new AboutUSPanel(), "About US", new Dimension(750, 400));
		}
	}
	
	static class ShortcutKeyAction extends AbstractAction{
		public void actionPerformed(ActionEvent actionEvent) {
			DialogUtility.showDialog(new ShortcutKeyHelper(), "Shortcut Key", new Dimension(550, 200));
		}
	}
	
	static class ProjectManagementAction extends AbstractAction{
		public void actionPerformed(ActionEvent actionEvent) {
			Thread th = new Thread(new Runnable() {
				public void run() {
					DialogUtility.showDialog(new ProjectManagementPanel(), "Project Management", new Dimension(750, 650));
				}
			});
			th.setName("Projects loading thread");
			th.start();
			DialogUtility.showWaitDialog("Loading all projects, please wait....");	
		}
	}
	
	static class UserManagementAction extends AbstractAction{
		public void actionPerformed(ActionEvent actionEvent) {
			Thread th = new Thread(new Runnable() {
				public void run() {
					DialogUtility.showDialog(new UserManagementPanel(), "User Management", new Dimension(750, 300));
				}
			});
			th.setName("Users loading thread");
			th.start();
			DialogUtility.showWaitDialog("Loading all users, please wait....");	
		}
	}
}