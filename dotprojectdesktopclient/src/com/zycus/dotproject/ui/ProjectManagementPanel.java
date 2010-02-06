package com.zycus.dotproject.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.zycus.dotproject.api.IDepartmentManager;
import com.zycus.dotproject.api.IFileManager;
import com.zycus.dotproject.api.IProjectManager;
import com.zycus.dotproject.bo.BOCompany;
import com.zycus.dotproject.bo.BODepartment;
import com.zycus.dotproject.bo.BOFile;
import com.zycus.dotproject.bo.BOProject;
import com.zycus.dotproject.bo.BOUser;
import com.zycus.dotproject.bo.Priority;
import com.zycus.dotproject.bo.ProjectStatus;
import com.zycus.dotproject.bo.ProjectType;
import com.zycus.dotproject.bo.UserType;
import com.zycus.dotproject.factory.CompanyManagerFactory;
import com.zycus.dotproject.factory.DepartmentManagerFactory;
import com.zycus.dotproject.factory.FileManagerFactory;
import com.zycus.dotproject.factory.ProjectManagerFactory;
import com.zycus.dotproject.factory.UserManagerFactory;
import com.zycus.dotproject.ui.FileEditorPanel.FileEditorMode;
import com.zycus.dotproject.ui.component.IndentBorder;
import com.zycus.dotproject.ui.component.datepicker.CalendarADC;
import com.zycus.dotproject.ui.event.ButtonBarListener;
import com.zycus.dotproject.util.ApplicationContext;
import com.zycus.dotproject.util.DialogUtility;
import com.zycus.dotproject.util.MenuUtility;

public class ProjectManagementPanel extends CustomJPanel implements ButtonBarListener, ListSelectionListener {
	private ButtonBar				buttonBar				= null;
	private JList					lstProjects				= null;
	private IProjectManager			projectManager			= null;
	private JTextField				projectName				= null;
	private JTextArea				projectDescription		= null;
	private JComboBox				projectOwner			= null;
	private CalendarADC				projectEndDate			= null;
	private JComboBox				projectType				= null;
	private JComboBox				projectStatus			= null;
	private JComboBox				projectPriority			= null;
	private JComboBox				projectCompany			= null;
	private JList					contactList				= null;
	private List<BOUser>			currentUsers			= null;
	private JComboBox				departmentCombo			= null;

	private JTable					projectFiles			= null;
	private String[]				filesCols				= { "", "Name", "Description", "Version", "Category", "Size", "Task", "Owner", "Checkout", "Checkedout By", "Date" };
	private static SimpleDateFormat	dateFormatter			= new SimpleDateFormat(ApplicationContext.getUserPreferences().getDateDisplayFormat());
	private IFileManager			fileManager				= null;
	private FileTableModel			fileTableModel			= null;

	private JPopupMenu				taskFilesPopupMenu		= null;
	private JMenuItem				checkOutMenu			= null;
	private JMenuItem				viewFileMenu			= null;
	private JMenuItem				checkInMenu				= null;
	private JMenuItem				deleteFileMenu			= null;
	private JMenuItem				versionsFileMenu		= null;
	private JMenuItem				addFileMenu				= null;
	private JMenuItem				editFileMenu			= null;
	private FileActions				fileActions				= null;
	private FileTableMouseAdapter	fileTableMouseAdapter	= null;
	private CalendarADC				projectStartDate		= null;

	public ProjectManagementPanel() {
		init();
		initLayout();
	}

	private void init() {
		projectStartDate = new CalendarADC();
		projectStartDate.setFormat(ApplicationContext.getUserPreferences().getDateDisplayFormat());

		fileTableMouseAdapter = new FileTableMouseAdapter();

		fileActions = new FileActions();

		taskFilesPopupMenu = new JPopupMenu();
		taskFilesPopupMenu.add(addFileMenu = MenuUtility.getMenuItem("Add new file", fileActions));
		addFileMenu.setIcon(IconHelper.getNewIcon());
		// taskFilesPopupMenu.add(editFileMenu = MenuUtility.getMenuItem("Edit",
		// fileActions));
		taskFilesPopupMenu.add(viewFileMenu = MenuUtility.getMenuItem("Download", fileActions));
		taskFilesPopupMenu.add(checkOutMenu = MenuUtility.getMenuItem("CheckOut", fileActions));
		taskFilesPopupMenu.add(checkInMenu = MenuUtility.getMenuItem("CheckIn", fileActions));
		taskFilesPopupMenu.add(versionsFileMenu = MenuUtility.getMenuItem("Show all versions", fileActions));
		taskFilesPopupMenu.add(deleteFileMenu = MenuUtility.getMenuItem("Delete", fileActions));
		deleteFileMenu.setIcon(IconHelper.getDeleteIcon());
		viewFileMenu.setIcon(IconHelper.getDownloadIcon());
		checkOutMenu.setIcon(IconHelper.getCheckOutIcon());
		checkInMenu.setIcon(IconHelper.getCheckInIcon());
		addFileMenu.setIcon(IconHelper.getAddNewFileIcon());
		// editFileMenu.setIcon(IconHelper.getEditIcon());
		versionsFileMenu.setIcon(IconHelper.getViewAllIcon());

		fileManager = FileManagerFactory.getFileManager();
		fileTableModel = new FileTableModel(new ArrayList<BOFile>());
		projectFiles = new JTable(fileTableModel);
		projectFiles.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		FileTableRenderer renderer = new FileTableRenderer();
		projectFiles.setDefaultRenderer(String.class, renderer);
		projectFiles.addMouseListener(fileTableMouseAdapter);

		projectFiles.getColumnModel().getColumn(0).setMaxWidth(10);
		projectFiles.getColumnModel().getColumn(0).setPreferredWidth(10);
		projectFiles.getColumnModel().getColumn(0).setResizable(false);

		buttonBar = new ButtonBar(ButtonBar.ADD | ButtonBar.SAVE | ButtonBar.DELETE | ButtonBar.CANCEL);

		buttonBar.enableButton(ButtonBar.SAVE, false);
		buttonBar.enableButton(ButtonBar.DELETE, false);

		buttonBar.addButtonBarListener(this);
		projectManager = ProjectManagerFactory.getProjectManager();
		Object[] projectsForSorting = projectManager.getAllProjects(null, ApplicationContext.getCurrentUser()).toArray();
		Arrays.sort(projectsForSorting);

		lstProjects = new JList(projectsForSorting);
		lstProjects.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		projectName = new JTextField();
		projectDescription = new JTextArea(2, 5);
		BOUser[] users = UserManagerFactory.getUserManager().getAllUsers().toArray(new BOUser[] {});
		Arrays.sort(users);

		projectOwner = new JComboBox(users);

		projectEndDate = new CalendarADC();
		projectEndDate.setFormat(ApplicationContext.getUserPreferences().getDateDisplayFormat());
		//projectEndDate.setSelectable(true);
		
		projectType = new JComboBox(ProjectType.values());

		projectStatus = new JComboBox(ProjectStatus.values());
		projectPriority = new JComboBox(Priority.values());
		currentUsers = new ArrayList<BOUser>();
		contactList = new JList(users);
		contactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		contactList.setCellRenderer(new CheckComboRenderer());
		contactList.addListSelectionListener(this);
		projectCompany = new JComboBox(CompanyManagerFactory.getCompanyManager().getAllCompanies(ApplicationContext.getCurrentUser()).toArray());
		lstProjects.addListSelectionListener(this);

		departmentCombo = new JComboBox(getDepartments());
		departmentCombo.setRenderer(new TreeListCellRenderer());
	}

	private Map<Long, Integer>	departmentDepth	= new LinkedHashMap<Long, Integer>();

	private BODepartment[] getDepartments() {
		IDepartmentManager departmentManager = DepartmentManagerFactory.getDepartmentManager();
		List<BODepartment> deps = new ArrayList<BODepartment>(departmentManager.getAllDepartments(ApplicationContext.getCurrentUser()));
		Set<BODepartment> depsReturn = new LinkedHashSet<BODepartment>();
		for (BODepartment department : deps) {
			// ignore the childTasks
			if (department.getParentDepartmentID() == 0L) {
				depsReturn.add(department);
				departmentDepth.put(department.getDepartmentID(), 0);
				addChild(department, depsReturn);
			}

		}

		return depsReturn.toArray(new BODepartment[] {});
	}

	private void addChild(BODepartment department, Set<BODepartment> deptSet) {
		if (department == null || department.getChildDepartments() == null) {
			return;
		}
		for (BODepartment childDepartment : department.getChildDepartments()) {
			deptSet.add(childDepartment);
			departmentDepth.put(childDepartment.getDepartmentID(), departmentDepth.get(childDepartment.getParentDepartmentID()) + 1);
			addChild(childDepartment, deptSet);
		}
	}

	public void valueChanged(ListSelectionEvent listEvent) {
		if (listEvent.getSource() == lstProjects) {
			if (lstProjects.getSelectedIndex() >= 0) {
				Thread th = new Thread(new Runnable() {
					public void run() {
						try {
							projectSelected((BOProject) lstProjects.getSelectedValue());
						}catch(Throwable a_th) {
							JOptionPane.showMessageDialog(ApplicationContext.getCurrentFrame(), "Problem loading project, \n please contact your vendor", "System error:", JOptionPane.ERROR_MESSAGE);
						}
						DialogUtility.hideWaitDialog();
					}
				});
				th.setName("Project loading thread");
				th.start();
				DialogUtility.showWaitDialog("Loading project, please wait....");
			}
		} else if (listEvent.getSource() == contactList) {
			if (listEvent.getValueIsAdjusting()) {
				return;
			}
			BOUser user = (BOUser) contactList.getSelectedValue();
			addEditUser(user, currentUsers);
		}
	}

	private void addEditUser(BOUser user, List<BOUser> users) {
		if (user == null || users == null) {
			return;
		}
		if (isUserPresent(user)) {
			currentUsers = removeUser(user, users);
		} else {
			users.add(user);
		}
	}

	private List<BOUser> removeUser(BOUser user, List<BOUser> users) {
		List<BOUser> usesReturn = new ArrayList<BOUser>();
		for (BOUser cUser : users) {
			if (cUser.getUserID() == user.getUserID()) {
				continue;
			}
			usesReturn.add(cUser);
		}
		return usesReturn;
	}

	private boolean isUserPresent(BOUser user) {
		if (currentUsers == null) {
			return false;
		}
		for (BOUser cUser : currentUsers) {
			if (cUser.getUserID() == user.getUserID()) {
				return true;
			}
		}
		return false;
	}

	private void initLayout() {
		setLayout(new BorderLayout());
		JPanel pnlEditorArea = new JPanel(new BorderLayout());
		pnlEditorArea.add(buttonBar, BorderLayout.SOUTH);
		JPanel pnlFields = new JPanel(new GridLayout(9, 2, 5, 5));

		pnlFields.add(new JLabel("Project Name"));
		pnlFields.add(projectName);

		pnlFields.add(new JLabel("Project Owner"));
		pnlFields.add(projectOwner);

		pnlFields.add(new JLabel("Project Company"));
		pnlFields.add(projectCompany);

		pnlFields.add(new JLabel("Project Department"));
		pnlFields.add(departmentCombo);

		pnlFields.add(new JLabel("Project Start Date"));
		pnlFields.add(projectStartDate);

		pnlFields.add(new JLabel("Project End Date"));
		pnlFields.add(projectEndDate);

		pnlFields.add(new JLabel("Project Type"));
		pnlFields.add(projectType);

		pnlFields.add(new JLabel("Project Status"));
		pnlFields.add(projectStatus);

		pnlFields.add(new JLabel("Project Priority"));
		pnlFields.add(projectPriority);

		pnlEditorArea.add(pnlFields, BorderLayout.NORTH);
		JScrollPane scrollPaneFiles = new JScrollPane(projectFiles);
		scrollPaneFiles.addMouseListener(fileTableMouseAdapter);

		JTabbedPane tab = new JTabbedPane();
		tab.addTab("Description", new JScrollPane(projectDescription));
		tab.addTab("Contacts", new JScrollPane(contactList));
		tab.addTab("Files", scrollPaneFiles);

		pnlEditorArea.add(tab, BorderLayout.CENTER);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(lstProjects), pnlEditorArea);
		splitPane.setDividerLocation(200);
		add(splitPane, BorderLayout.CENTER);
	}

	private void initReload(BOProject project) {
		final Object[] projectsForSorting = projectManager.getAllProjects(null, ApplicationContext.getCurrentUser()).toArray();
		Arrays.sort(projectsForSorting);
		lstProjects.setModel(new AbstractListModel() {
			public int getSize() {
				return projectsForSorting.length;
			}

			public Object getElementAt(int index) {
				return projectsForSorting[index];
			}
		});
		if (project != null) {
			lstProjects.setSelectedValue(project, true);
		} else {
			onAdd();
		}
	}

	private void projectSelected(BOProject project) {
		project.loadContactUsers();
		projectName.setText(project.getProjectName());
		projectDescription.setText(project.getProjectDescription());
		projectOwner.setSelectedItem(project.getProjectOwner());
		projectStartDate.setDate(project.getProjectStartDate());
		projectEndDate.setDate(project.getProjectEndDate());
		projectStatus.setSelectedItem(project.getProjectStatus());
		projectType.setSelectedItem(project.getProjectType());
		projectPriority.setSelectedItem(project.getProjectPriority());
		currentUsers = new ArrayList<BOUser>(project.getContactUsers());
		projectCompany.setSelectedItem(project.getProjectCompany());

		Set<BODepartment> departments = project.getProjectDepartments();
		if (departments != null && departments.size() > 0) {
			departmentCombo.setSelectedItem(departments.toArray()[0]);
		} else {
			departmentCombo.setSelectedItem(null);
		}

		projectDescription.setCaretPosition(0);
		projectName.setCaretPosition(0);
		contactList.updateUI();

		buttonBar.enableButton(ButtonBar.SAVE, project.canSaveProjectInfo(ApplicationContext.getCurrentUser(), project.getProjectOwner()));
		buttonBar.enableButton(ButtonBar.DELETE, project.canSaveProjectInfo(ApplicationContext.getCurrentUser(), project.getProjectOwner()));
		try {
			fileTableModel.updateFileList(fileManager.getAllFile(project, ApplicationContext.getCurrentUser()));
		}catch(Throwable a_th) {
			//just not required to do something major, lets log it.
			a_th.printStackTrace();
		}
	}

	private void onAdd() {
		lstProjects.setSelectedIndex(-1);
		lstProjects.clearSelection();
		currentUsers = new ArrayList<BOUser>();
		projectName.setText("");
		projectDescription.setText("");
		projectStartDate.setDate(new Date());
		projectEndDate.setDate(new Date());
		projectStatus.setSelectedIndex(-1);
		projectOwner.setSelectedIndex(-1);
		projectType.setSelectedIndex(-1);
		projectPriority.setSelectedIndex(-1);
		contactList.clearSelection();
		contactList.updateUI();
		buttonBar.enableButton(ButtonBar.SAVE, true);
	}

	private void onCancel() {
		DialogUtility.closeParent(this);
	}

	private class CheckComboRenderer implements ListCellRenderer {
		JCheckBox	checkBox;

		public CheckComboRenderer() {
			checkBox = new JCheckBox();
		}

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			BOUser user = (BOUser) value;
			checkBox.setText(user.toString());
			checkBox.setBackground(isSelected ? UIManager.getColor("Menu.selectionBackground") : UIManager.getColor("Menu.background"));
			if (currentUsers != null && isUserPresent(user)) {
				checkBox.setSelected(true);
			} else {
				checkBox.setSelected(false);
			}
			return checkBox;
		}
	}

	private boolean validateEntry() {
		if (projectName.getText().trim().length() <= 0) {
			JOptionPane.showMessageDialog(ApplicationContext.getCurrentFrame(), "Please provide project name", "Validation Error:", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (projectOwner.getSelectedIndex() < 0) {
			JOptionPane.showMessageDialog(ApplicationContext.getCurrentFrame(), "Please select project owner", "Validation Error:", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (projectCompany.getSelectedIndex() < 0) {
			JOptionPane.showMessageDialog(ApplicationContext.getCurrentFrame(), "Please select project company", "Validation Error:", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (projectType.getSelectedIndex() < 0) {
			JOptionPane.showMessageDialog(ApplicationContext.getCurrentFrame(), "Please select project type", "Validation Error:", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (projectStatus.getSelectedIndex() < 0) {
			JOptionPane.showMessageDialog(ApplicationContext.getCurrentFrame(), "Please select project status", "Validation Error:", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	private void onSave() {
		if (validateEntry() == false) {
			return;
		}

		BOProject project = new BOProject();
		if (lstProjects.getSelectedValue() != null) {
			project = (BOProject) lstProjects.getSelectedValue();
		}

		project.setProjectName(projectName.getText());

		BOUser oldOwner = project.getProjectOwner();
		project.setProjectOwner((BOUser) projectOwner.getSelectedItem());
		project.setProjectCompany((BOCompany) projectCompany.getSelectedItem());
		project.setProjectStartDate(projectStartDate.getDate());
		project.setProjectEndDate(projectEndDate.getDate());
		project.setProjectType((ProjectType) projectType.getSelectedItem());
		project.setProjectStatus((ProjectStatus) projectStatus.getSelectedItem());
		project.setProjectPriority((Priority) projectPriority.getSelectedItem());
		project.setProjectDescription(projectDescription.getText());
		project.setContactUsers(currentUsers);

		Set<BODepartment> projectDepartments = new LinkedHashSet<BODepartment>();
		projectDepartments.add((BODepartment) departmentCombo.getSelectedItem());
		project.setProjectDepartments(projectDepartments);

		projectManager.saveProjectInfo(ApplicationContext.getCurrentUser(), oldOwner, project);

		initReload(project);
	}

	public void actionPerformed(ActionType actionType) {
		if (actionType == ActionType.Cancel) {
			onCancel();
		} else if (actionType == ActionType.Add) {
			onAdd();
		} else if (actionType == ActionType.Save) {
			try {
				onSave();
			} catch (Throwable a_th) {
				a_th.printStackTrace(System.err);
				JOptionPane.showMessageDialog(ApplicationContext.getCurrentFrame(), "Could not save proejct", "Error:", JOptionPane.ERROR_MESSAGE);
			}
		} else if (actionType == ActionType.Delete) {
			try {
				onDelete();
			} catch (Throwable a_th) {
				a_th.printStackTrace(System.err);
				JOptionPane.showMessageDialog(ApplicationContext.getCurrentFrame(), "Could not delete proejct", "Error:", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void onDelete() {
		if (lstProjects.getSelectedValue() == null) {
			JOptionPane.showMessageDialog(ApplicationContext.getCurrentFrame(), "Please select project to delete", "Validation Error:", JOptionPane.ERROR_MESSAGE);
			return;
		}
		int returnCode = JOptionPane.showConfirmDialog(ApplicationContext.getCurrentFrame(), "Do you want to delete the selected project", "Confirm: ", JOptionPane.YES_NO_OPTION);
		if (returnCode == JOptionPane.NO_OPTION) {
			return;
		}
		projectManager.deleteProject(ApplicationContext.getCurrentUser(), (BOProject) lstProjects.getSelectedValue());
		initReload(null);
	}

	@Override
	public void onExit() {
		buttonBar.removeButtonBarListener(this);
		buttonBar.onExit();
		buttonBar = null;

		lstProjects.removeListSelectionListener(this);
		lstProjects.removeAll();
		lstProjects = null;

		projectManager = null;
		projectName = null;
		projectDescription = null;

		projectOwner.removeAllItems();
		projectOwner = null;

		projectStartDate = null;
		projectEndDate = null;

		projectType.removeAllItems();
		projectType = null;

		projectStatus.removeAllItems();
		projectStatus = null;

		projectPriority.removeAllItems();
		projectPriority = null;

		projectCompany.removeAllItems();
		projectCompany = null;

		contactList.removeListSelectionListener(this);
		contactList.setCellRenderer(null);
		contactList.removeAll();
		contactList = null;

		currentUsers.clear();
		currentUsers = null;

		departmentCombo.setRenderer(null);
		departmentCombo = null;
	}

	private class TreeListCellRenderer extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			TreeListCellRenderer.this.updateUI();
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			int depth = (value instanceof BOProject) ? 0 : getDepth((BODepartment) value);// somehow
			IndentBorder indentBorder = new IndentBorder();
			indentBorder.setDepth(depth);
			setBorder(indentBorder);
			return this;
		}
	}

	private int getDepth(BODepartment department) {
		if (department == null) {
			return 0;
		} else {
			return departmentDepth.get(department.getDepartmentID());
		}
	}

	private class FileTableModel extends DefaultTableModel {
		private List<BOFile>	files	= new ArrayList<BOFile>();

		public FileTableModel(List<BOFile> files) {
			this.files = files;
		}

		public void updateFileList(List<BOFile> files) {
			this.files = files;
			fireTableDataChanged();
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return String.class;
		}

		@Override
		public int getColumnCount() {
			return filesCols.length;
		}

		@Override
		public String getColumnName(int column) {
			return filesCols[column];
		}

		@Override
		public int getRowCount() {
			if (files == null) {
				return 0;
			}
			return files.size();
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}

		@Override
		public Object getValueAt(int row, int column) {
			if (column == -1) {
				return files.get(row);
			} else if (column == 0) {
				return files.get(row).getCheckedOutUser() != null;
			} else if (column == 1) {
				return files.get(row).getRealFilename();
			} else if (column == 2) {
				return files.get(row).getFileDescription();
			} else if (column == 3) {
				BOFile file = files.get(row);
				if (file.getVersionCount() > 1) {
					return files.get(row).getFileVersion() + " ( " + file.getVersionCount() + " )";
				} else {
					return files.get(row).getFileVersion();
				}
			} else if (column == 4) {
				return files.get(row).getFileCategory();
			} else if (column == 5) {
				return files.get(row).getFileSize();
			} else if (column == 6) {
				return files.get(row).getTaskName();
			} else if (column == 7) {
				return files.get(row).getFileOwner();
			} else if (column == 8) {
				return files.get(row).getCheckedOutUser();
			} else if (column == 9) {
				return files.get(row).getFileCheckoutReason();
			} else if (column == 10) {
				if (dateFormatter != null) {
					return dateFormatter.format(files.get(row).getFileDate());
				} else {
					return files.get(row).getFileDate();
				}
			}
			return "";
		}
	}

	private class FileActions extends AbstractAction {
		public void actionPerformed(ActionEvent actionPerformed) {
			if (actionPerformed.getSource() == addFileMenu) {
				onAddFiles();
			} else if (actionPerformed.getSource() == checkOutMenu) {
				onCheckOutFile();
			} else if (actionPerformed.getSource() == checkInMenu) {
				onCheckInFile();
			} else if (actionPerformed.getSource() == deleteFileMenu) {
				onDeleteFile();
			} else if (actionPerformed.getSource() == viewFileMenu) {
				onViewFile();
			} else if (actionPerformed.getSource() == versionsFileMenu) {
				onVersions();
			} else if (actionPerformed.getSource() == editFileMenu) {
				// onEdit();
			}
		}
	}

	private void onVersions() {
		int selectedIndex = projectFiles.getSelectedRow();
		if (selectedIndex < 0) {
			return;
		}
		BOFile taskFile = (BOFile) projectFiles.getValueAt(selectedIndex, -1);
		DialogUtility.showDialog(new FileVersionsPanel(fileManager.getAllVersions(taskFile.getRootFileID(), ApplicationContext.getCurrentUser())), "All versions", new Dimension(550, 200));
	}

	private void onViewFile() {
		int selectedIndex = projectFiles.getSelectedRow();
		if (selectedIndex < 0) {
			return;
		}
		BOFile taskFile = (BOFile) projectFiles.getValueAt(selectedIndex, -1);
		JFileChooser fileChooser = new JFileChooser() {
			@Override
			public void cancelSelection() {
				Map<Thread, StackTraceElement[]> stackTrace = Thread.getAllStackTraces();
				if (stackTrace.get(Thread.currentThread())[4].getFileName().contains("AbstractButton")) {
					super.cancelSelection();
				}
			}
		};
		File f = null;
		if (ApplicationContext.getUserPreferences().getUserSelectedDir() != null) {
			f = new File(ApplicationContext.getUserPreferences().getUserSelectedDir() + File.separator + taskFile.getRealFilename());
		} else {
			f = new File(System.getProperty("java.io.tmpdir") + taskFile.getRealFilename());

		}
		fileChooser.setSelectedFile(f);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setAcceptAllFileFilterUsed(false);
		int returnOption = fileChooser.showSaveDialog(ApplicationContext.getCurrentFrame());
		if (JFileChooser.CANCEL_OPTION == returnOption || JFileChooser.ERROR_OPTION == returnOption) {
			return;
		}
		if (fileChooser.getSelectedFile() == null) {
			return;
		}
		f = fileChooser.getSelectedFile();
		fileManager.saveFile(taskFile, f);
		try {
			Desktop.getDesktop().open(f);
		} catch (IOException a_excp) {
			a_excp.printStackTrace();
		}
		ApplicationContext.getUserPreferences().setUserSelectedDir(fileChooser.getCurrentDirectory().getAbsolutePath());
		ApplicationContext.saveSettings();
	}

	private void onDeleteFile() {
		int selectedIndex = projectFiles.getSelectedRow();
		if (selectedIndex < 0) {
			return;
		}

		if (JOptionPane.showConfirmDialog(ApplicationContext.getCurrentFrame(), "Are you sure that you want to delete the selected version?", "Delete Confirmation", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
			return;
		}

		BOFile taskFile = (BOFile) projectFiles.getValueAt(selectedIndex, -1);
		try {
			fileManager.deleteFile(taskFile, ApplicationContext.getCurrentUser());
			fileTableModel.updateFileList(fileManager.getAllFile((BOProject) lstProjects.getSelectedValue(), ApplicationContext.getCurrentUser()));
		} catch (Throwable a_th) {
			a_th.printStackTrace();
			JOptionPane.showMessageDialog(this, "could not delete file, Please contact your vendor", "File save error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void onCheckInFile() {
		int selectedIndex = projectFiles.getSelectedRow();
		if (selectedIndex < 0) {
			return;
		}

		BOFile taskFile = (BOFile) projectFiles.getValueAt(selectedIndex, -1);

		FileEditorPanel fe = new FileEditorPanel(FileEditorMode.FileCheckIn);
		fe.setFile(taskFile);
		DialogUtility.showDialog(fe, "CheckIn file", new Dimension(350, 250));
		if (fe.getRealFile() == null || fe.getFile() == null) {
			return;
		}

		taskFile.setFileCheckout("");
		taskFile.setCheckedOutUser(null);
		taskFile.setFileDate(new Date());

		BOFile newTaskFile = fe.getFile();
		File realFile = fe.getRealFile();
		newTaskFile.setTaskID(taskFile.getTaskID());

		newTaskFile.setProject((BOProject) lstProjects.getSelectedValue());
		newTaskFile.setFileName(newTaskFile.generateFileName());
		newTaskFile.setFileVersion(taskFile.getFileVersion() + 1);
		newTaskFile.setFileDate(new Date());
		try {
			fileManager.checkInFile(taskFile, newTaskFile, realFile, ApplicationContext.getCurrentUser());
			fileTableModel.updateFileList(fileManager.getAllFile((BOProject) lstProjects.getSelectedValue(), ApplicationContext.getCurrentUser()));
		} catch (Throwable a_th) {
			a_th.printStackTrace();
			JOptionPane.showMessageDialog(this, "could not save file, Please contact your vendor", "File save error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void onCheckOutFile() {
		int selectedIndex = projectFiles.getSelectedRow();
		if (selectedIndex < 0) {
			return;
		}
		JTextArea area = new JTextArea(5, 10);
		int result = JOptionPane.showOptionDialog(ApplicationContext.getCurrentFrame(), new Object[] { "Please provide reason for checkout", new JScrollPane(area) }, "File Checkout",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

		if (result == JOptionPane.CANCEL_OPTION) {
			return;
		}

		String strReason = area.getText();
		if (strReason == null) {
			return;
		}
		BOFile taskFile = (BOFile) projectFiles.getValueAt(selectedIndex, -1);
		taskFile.setCheckedOutUser(ApplicationContext.getCurrentUser());
		taskFile.setFileCheckoutReason(strReason);
		taskFile.setFileDate(new Date());
		taskFile.setFileCheckout(ApplicationContext.getCurrentUser().getUserID() + "");
		try {
			fileManager.checkOutFile(taskFile, ApplicationContext.getCurrentUser());
			fileTableModel.updateFileList(fileManager.getAllFile((BOProject) lstProjects.getSelectedValue(), ApplicationContext.getCurrentUser()));
		} catch (Throwable a_th) {
			a_th.printStackTrace();
			JOptionPane.showMessageDialog(this, "could not checkout, Please contact your vendor", "File save error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void onAddFiles() {
		FileEditorPanel fe = new FileEditorPanel(FileEditorMode.FileAdd);
		DialogUtility.showDialog(fe, "Add new file", new Dimension(350, 250));
		if (fe.getRealFile() == null || fe.getFile() == null) {
			return;
		}
		BOFile file = fe.getFile();
		File f = fe.getRealFile();

		file.setProject((BOProject) lstProjects.getSelectedValue());
		file.setFileName(file.generateFileName());
		try {
			fileManager.addFile(file, f);
			fileTableModel.updateFileList(fileManager.getAllFile((BOProject) lstProjects.getSelectedValue(), ApplicationContext.getCurrentUser()));
		} catch (Throwable a_th) {
			a_th.printStackTrace();
			JOptionPane.showMessageDialog(this, "could not save file, Please contact your vendor", "File save error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private class FileTableMouseAdapter extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent event) {
			if (lstProjects.getSelectedValue() != null) {
				if (event.getButton() == MouseEvent.BUTTON3) {
					int selectedRow = projectFiles.getSelectedRow();

					checkOutMenu.setVisible(false);
					checkInMenu.setVisible(false);
					deleteFileMenu.setVisible(false);
					viewFileMenu.setVisible(false);
					// editFileMenu.setVisible(false);
					versionsFileMenu.setVisible(false);
					addFileMenu.setVisible(true);

					if (selectedRow >= 0) {

						// means this is checked-out
						if (projectFiles.getValueAt(selectedRow, 0).toString().equalsIgnoreCase("true")) {
							// if i have checked-out then only i can check-in
							if (projectFiles.getValueAt(selectedRow, 7).equals(ApplicationContext.getCurrentUser())) {
								checkInMenu.setVisible(true);
							}
						} else {
							checkOutMenu.setVisible(true);
						}
						viewFileMenu.setVisible(true);
						deleteFileMenu.setVisible(true);
						// editFileMenu.setVisible(true);
						if (((BOFile) projectFiles.getValueAt(selectedRow, -1)).getVersionCount() > 1) {
							versionsFileMenu.setVisible(true);
						}
					}

					// this is extra measure
					if (((BOProject) lstProjects.getSelectedValue()).canEditProjectDataOtherThanTasks(ApplicationContext.getCurrentUser()) == false) {
						checkOutMenu.setVisible(false);
						checkInMenu.setVisible(false);
						addFileMenu.setVisible(false);
						if (ApplicationContext.getCurrentUser().getUserType() == UserType.Administrator) {
							deleteFileMenu.setVisible(true);
						} else {
							deleteFileMenu.setVisible(false);
						}
					}

					taskFilesPopupMenu.show((Component) event.getSource(), event.getX(), event.getY());
				}
			}
		}
	}

	private class FileTableRenderer extends DefaultTableCellRenderer {
		private JCheckBox	chk				= null;
		private Color		checkOwnColor	= new Color(219, 235, 204);
		private Color		checkElseColor	= new Color(238, 187, 58);

		public FileTableRenderer() {
			chk = new JCheckBox();
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			if (value != null && (value.toString().equalsIgnoreCase("true") || value.toString().equalsIgnoreCase("false"))) {
				chk.setSelected(new Boolean(value.toString()));
				chk.setBackground(getBackground());
				chk.setBorder(getBorder());
				return chk;
			} else {
				return this;
			}
		}
	}
}