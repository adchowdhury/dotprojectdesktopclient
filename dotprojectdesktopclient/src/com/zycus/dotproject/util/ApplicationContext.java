package com.zycus.dotproject.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.zycus.dotproject.api.IProjectManager;
import com.zycus.dotproject.bo.BOProject;
import com.zycus.dotproject.bo.BOUser;
import com.zycus.dotproject.factory.ProjectManagerFactory;
import com.zycus.dotproject.ui.event.ProjectSelectionListener;
import com.zycus.dotproject.ui.event.ViewChangeListener.ViewType;
import com.zycus.dotproject.util.UserPreferences.LookAndFeel;

public final class ApplicationContext {

	private ApplicationContext() {
		throw new IllegalAccessError("this class should not be initialised");
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("This class should not be cloned");
	}

	private static BOUser							currentUser					= null;
	private static JFrame							currentFrame				= null;
	private static List<ProjectSelectionListener>	projectSelectionListenres	= new ArrayList<ProjectSelectionListener>();
	private static BOProject						currentProjet				= null;
	private static UserPreferences					userPreferences				= null;
	private static String							projectFileHome				= System.getProperty("user.home") + File.separatorChar + "dp";
	private static String							tempFileLocation			= projectFileHome + File.separatorChar + "usersPrefs.ser";
	private static ViewType							viewTYpe					= ViewType.TreeView;
	
	public static LookAndFeel getCurrentLookAndFeel(){
		return userPreferences.getCurrentLookAndFeel();
	}
	
	public static void setCurrentLookAndFeel(LookAndFeel a_currentLnF){
		try {
			userPreferences.setCurrentLookAndFeel(a_currentLnF);
			UIManager.setLookAndFeel(a_currentLnF.getClassName());
			SwingUtilities.updateComponentTreeUI(currentFrame);
		}catch(Throwable a_th) {
			a_th.printStackTrace(System.err);
		}
	}

	public static void projectSaved() {
		projectChanged(currentProjet);
	}

	public static String getProjectFileHome() {
		return projectFileHome;
	}

	public static ViewType getViewTYpe() {
		return viewTYpe;
	}

	public static void setViewTYpe(ViewType viewTYpe) {
		ApplicationContext.viewTYpe = viewTYpe;
	}

	public static void saveSettings() {
		try {
			File f = new File(tempFileLocation);
			if (f.getParentFile().exists() == false) {
				f.getParentFile().mkdirs();
			}
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
			oos.writeObject(userPreferences);
			oos.flush();
			oos.close();
		} catch (Throwable a_th) {
			// just ignore
		}
	}

	public static void loadSettings() {
		try {
			File f = new File(tempFileLocation);
			if (f.exists()) {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
				userPreferences = (UserPreferences) ois.readObject();
				ois.close();
			} else {
				userPreferences = new UserPreferences();
			}
		} catch (Throwable a_th) {
			userPreferences = new UserPreferences();
		}
	}

	public static UserPreferences getUserPreferences() {
		if (userPreferences == null) {
			loadSettings();
		}
		return userPreferences;
	}

	public static void setUserPreferences(UserPreferences userPreferences) {
		ApplicationContext.userPreferences = userPreferences;
	}

	public static BOProject getCurrentProjet() {
		return currentProjet;
	}

	public static void setCurrentProjet(BOProject a_project) {
		if (a_project == null) {
			ApplicationContext.currentProjet = null;
			projectChanged(null);
			return;
		}
		IProjectManager projectManager = ProjectManagerFactory.getProjectManager();
		BOProject project = projectManager.getCompleteProject(a_project.getProjectID());
		ApplicationContext.currentProjet = project;
		projectChanged(currentProjet);
	}

	public static void addProjectSelectionListener(ProjectSelectionListener projectSelectionListenre) {
		projectSelectionListenres.add(projectSelectionListenre);
	}

	public static void removeProjectSelectionListener(ProjectSelectionListener projectSelectionListenre) {
		projectSelectionListenres.remove(projectSelectionListenre);
	}

	public static void projectChanged(BOProject project) {
		ApplicationContext.currentProjet = project;
		for (ProjectSelectionListener listener : projectSelectionListenres) {
			listener.projectSelected(project);
		}
	}

	public static JFrame getCurrentFrame() {
		return currentFrame;
	}

	public static void setCurrentFrame(JFrame currentFrame) {
		ApplicationContext.currentFrame = currentFrame;
	}

	public static BOUser getCurrentUser() {
		return currentUser;
	}

	public static void setCurrentUser(BOUser currentUser) {
		ApplicationContext.currentUser = currentUser;
	}
}
