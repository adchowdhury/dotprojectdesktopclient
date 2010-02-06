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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
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
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.zycus.dotproject.api.IFileManager;
import com.zycus.dotproject.bo.BOFile;
import com.zycus.dotproject.bo.BOTask;
import com.zycus.dotproject.bo.BOTaskLog;
import com.zycus.dotproject.bo.BOUser;
import com.zycus.dotproject.bo.Priority;
import com.zycus.dotproject.bo.TaskAccess;
import com.zycus.dotproject.bo.TaskMilestone;
import com.zycus.dotproject.bo.TaskStatus;
import com.zycus.dotproject.bo.TaskType;
import com.zycus.dotproject.bo.UserType;
import com.zycus.dotproject.factory.FileManagerFactory;
import com.zycus.dotproject.factory.UserManagerFactory;
import com.zycus.dotproject.ui.FileEditorPanel.FileEditorMode;
import com.zycus.dotproject.ui.event.ButtonBarListener;
import com.zycus.dotproject.util.ApplicationContext;
import com.zycus.dotproject.util.DialogUtility;
import com.zycus.dotproject.util.MenuUtility;

public class TaskManagementPanel extends CustomJPanel implements ButtonBarListener, ListSelectionListener, ChangeListener {
	private BOTask					task					= null;
	private JTextField				taskName				= null;
	private JTextArea				taskDescription			= null;
	private JTable					taskLogs				= null;
	private String[]				taskLogsCols			= { "Name", "Description", "Duration", "Date", "Creator" };
	private ButtonBar				btnBar					= null;
	private JCheckBox				chkMilestone			= null;
	private JComboBox				taskStatus				= null;
	private JComboBox				taskPriority			= null;
	private JComboBox				taskType				= null;
	private JComboBox				taskAccess				= null;
	private JList					contactList				= null;
	private List<BOUser>			currentUsers			= null;
	private JTable					taskFiles				= null;
	private String[]				filesCols				= { "", "Name", "Description", "Version", "Category", "Size", "Owner", "Checkout", "Checkedout By", "Date" };
	private static SimpleDateFormat	dateFormatter			= new SimpleDateFormat(ApplicationContext.getUserPreferences().getDateDisplayFormat());
	private JPopupMenu				taskFilesPopupMenu		= null;
	private JTabbedPane				tab						= null;

	private JMenuItem				checkOutMenu			= null;
	private JMenuItem				viewFileMenu			= null;
	private JMenuItem				checkInMenu				= null;
	private JMenuItem				addFileMenu				= null;
	private JMenuItem				deleteFileMenu			= null;
	private JMenuItem				editFileMenu			= null;
	private JMenuItem				versionsFileMenu		= null;

	private FileTableMouseAdapter	fileTableMouseAdapter	= null;
	private FileActions				fileActions				= null;
	private IFileManager			fileManager				= null;
	private FileTableModel			fileTableModel			= null;

	public TaskManagementPanel(BOTask task) {
		if (task == null) {
			throw new IllegalArgumentException("null parameter not allowed");
		}
		this.task = task;
		init();
		initLayout();
	}

	private void init() {
		fileManager = FileManagerFactory.getFileManager();

		tab = new JTabbedPane();
		tab.addChangeListener(this);

		fileActions = new FileActions();

		fileTableMouseAdapter = new FileTableMouseAdapter();

		taskFilesPopupMenu = new JPopupMenu();
		taskFilesPopupMenu.add(addFileMenu = MenuUtility.getMenuItem("Add new file", fileActions));
		addFileMenu.setIcon(IconHelper.getNewIcon());
		taskFilesPopupMenu.add(editFileMenu = MenuUtility.getMenuItem("Edit", fileActions));
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
		//editFileMenu.setIcon(IconHelper.getEditIcon());
		versionsFileMenu.setIcon(IconHelper.getViewAllIcon());
		
		fileTableModel = new FileTableModel(fileManager.getFiles(task.getTaskID(), ApplicationContext.getCurrentUser()));
		taskFiles = new JTable(fileTableModel);

		FileTableRenderer renderer = new FileTableRenderer();
		taskFiles.setDefaultRenderer(String.class, renderer);
		taskFiles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		taskFiles.getColumnModel().getColumn(0).setMaxWidth(10);
		taskFiles.getColumnModel().getColumn(0).setPreferredWidth(10);
		taskFiles.getColumnModel().getColumn(0).setResizable(false);
		
		currentUsers = new ArrayList<BOUser>(task.getContactUsers());

		BOUser[] users = UserManagerFactory.getUserManager().getAllUsers().toArray(new BOUser[] {});
		Arrays.sort(users);

		contactList = new JList(users);
		contactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		contactList.setCellRenderer(new CheckComboRenderer());
		contactList.addListSelectionListener(this);

		taskName = new JTextField(task.getTaskName());
		taskDescription = new JTextArea(task.getTaskDescription());

		taskName.setCaretPosition(0);
		taskDescription.setCaretPosition(0);

		chkMilestone = new JCheckBox();
		chkMilestone.setSelected(task.getTaskMilestone().equals(TaskMilestone.MileStone));

		taskStatus = new JComboBox(TaskStatus.values());
		taskPriority = new JComboBox(Priority.values());
		taskType = new JComboBox(TaskType.values());
		taskAccess = new JComboBox(TaskAccess.values());

		taskStatus.setSelectedItem(task.getTaskStatus());
		taskPriority.setSelectedItem(task.getTaskPriority());
		taskType.setSelectedItem(task.getTaskType());
		taskAccess.setSelectedItem(task.getTaskAccess());

		taskLogs = new JTable(new TaskLogsModel());
		taskLogs.setToolTipText("Double click to view tasklog");
		taskLogs.addMouseListener(fileTableMouseAdapter);

		if (task.canBeEdited(ApplicationContext.getCurrentUser()) == false) {
			taskName.setEditable(false);
			taskDescription.setEditable(false);
			chkMilestone.setEnabled(false);
			taskStatus.setEnabled(false);
			taskPriority.setEnabled(false);
			taskType.setEnabled(false);
			taskAccess.setEnabled(false);
			btnBar = new ButtonBar(ButtonBar.ADD | ButtonBar.CANCEL);
		} else {
			btnBar = new ButtonBar(ButtonBar.ADD | ButtonBar.SAVE | ButtonBar.CANCEL);
		}
		taskFiles.addMouseListener(fileTableMouseAdapter);
		if (task.canAddTaskLog(ApplicationContext.getCurrentUser()) == false) {
			btnBar.showButton(ButtonBar.ADD, false);
		}
		btnBar.addButtonBarListener(this);
		btnBar.enableButton(ButtonBar.ADD, false);
	}

	private void initLayout() {

		JPanel pnlGeneral = new JPanel(new BorderLayout(5, 5));
		JPanel pnlGeneralNorth = new JPanel(new GridLayout(6, 2, 5, 5));

		pnlGeneralNorth.add(new JLabel("Task Name"));
		pnlGeneralNorth.add(taskName);
		JLabel milestoneLabel = new JLabel("Milestone");
		milestoneLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent mouseEvent) {
				chkMilestone.setSelected(!chkMilestone.isSelected());
			}
		});
		pnlGeneralNorth.add(milestoneLabel);
		pnlGeneralNorth.add(chkMilestone);

		pnlGeneralNorth.add(new JLabel("Task Status"));
		pnlGeneralNorth.add(taskStatus);

		pnlGeneralNorth.add(new JLabel("Task Priority"));
		pnlGeneralNorth.add(taskPriority);

		pnlGeneralNorth.add(new JLabel("Task Type"));
		pnlGeneralNorth.add(taskType);

		pnlGeneralNorth.add(new JLabel("Task Access"));
		pnlGeneralNorth.add(taskAccess);

		JPanel pnlGeneralCenter = new JPanel(new GridLayout(1, 2));

		pnlGeneralCenter.add(new JLabel("Task Description"));
		pnlGeneralCenter.add(new JScrollPane(taskDescription));

		pnlGeneral.add(pnlGeneralNorth, BorderLayout.NORTH);
		pnlGeneral.add(pnlGeneralCenter, BorderLayout.CENTER);

		JScrollPane scrollPaneFiles = new JScrollPane(taskFiles);
		scrollPaneFiles.addMouseListener(fileTableMouseAdapter);
		tab.addTab("General", pnlGeneral);
		tab.addTab("Contacts", new JScrollPane(contactList));
		tab.addTab("Files", scrollPaneFiles);
		tab.addTab("Task Logs", new JScrollPane(taskLogs));

		int tabLogIndex = tab.getTabCount() - 1;
		int tabFileIndex = tab.getTabCount() - 2;
		tab.setEnabledAt(tabLogIndex, task.getChildTasks().size() <= 0);
		tab.setEnabledAt(tabFileIndex, task.getEnhancedTaskID() > 0);

		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, tab);
		add(BorderLayout.SOUTH, btnBar);
		
		btnBar.enableButton(ButtonBar.ADD, false);
	}

	public void stateChanged(ChangeEvent e) {
		int tabLogIndex = tab.getTabCount() - 1;
		int tabFileIndex = tab.getTabCount() - 2;
		btnBar.enableButton(ButtonBar.ADD, false);

		if (tab.getSelectedIndex() == tabLogIndex) {
			btnBar.enableButton(ButtonBar.ADD, task.canAddTaskLog(ApplicationContext.getCurrentUser()));
		} else if (tab.getSelectedIndex() == tabFileIndex) {
			btnBar.enableButton(ButtonBar.ADD, task.canBeEdited(ApplicationContext.getCurrentUser()));
		}

	}

	private class TaskLogsModel extends DefaultTableModel {

		@Override
		public int getColumnCount() {
			return taskLogsCols.length;
		}

		@Override
		public String getColumnName(int column) {
			return taskLogsCols[column];
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}

		@Override
		public int getRowCount() {
			return task.getTaskLogs().size();
		}

		@Override
		public Object getValueAt(int row, int column) {
			BOTaskLog taskLog = task.getTaskLogs().toArray(new BOTaskLog[] {})[row];
			if (column == -1) {
				return taskLog;
			}
			if (column == 0) {// name
				return taskLog.getTaskLogName();
			} else if (column == 1) {// description
				return taskLog.getTaskLogDescription();
			} else if (column == 2) {// duration
				return taskLog.getTaskLogHours();
			} else if (column == 3) {// date
				if(taskLog != null && taskLog.getTaskLogDate() != null) {
					if(dateFormatter == null) {
						return taskLog.getTaskLogDate() + "";
					}else {
						return dateFormatter.format(taskLog.getTaskLogDate());	
					}						
				}				
			} else if (column == 4) {// user
				return task.getTaskLogs().toArray(new BOTaskLog[] {})[row].getTaskLogCreator();
			}
			return super.getValueAt(row, column);
		}
	}

	private void onAddTaskLog() {
		DialogUtility.showDialog(new TaskLogPanel(null, task), "Add New Log", new Dimension(400, 300));
		((TaskLogsModel) taskLogs.getModel()).fireTableDataChanged();
	}

	private void onCancel() {
		Set<BOTaskLog> taskLogs = new LinkedHashSet<BOTaskLog>();
		for (BOTaskLog log : task.getTaskLogs()) {
			if (log.getEnhancedTaskLogID() > 0) {
				taskLogs.add(log);
			}
		}
		task.setTaskLogs(taskLogs);
		DialogUtility.closeParent(this);
	}

	private void onSave() {
		if (taskName.getText().trim().length() <= 0) {
			JOptionPane.showMessageDialog(this, "Task name is blank", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		// if (taskDescription.getText().trim().length() <= 0) {
		// JOptionPane.showMessageDialog(this, "Task description is blank",
		// "Validation Error", JOptionPane.ERROR_MESSAGE);
		// return;
		// }
		task.setTaskName(taskName.getText());
		task.setTaskDescription(taskDescription.getText());
		task.setTaskMilestone(chkMilestone.isSelected() ? TaskMilestone.MileStone : TaskMilestone.NotAMilestone);
		task.setTaskPriority((Priority) taskPriority.getSelectedItem());
		task.setTaskStatus((TaskStatus) taskStatus.getSelectedItem());
		task.setTaskType((TaskType) taskType.getSelectedItem());
		task.setTaskAccess((TaskAccess) taskAccess.getSelectedItem());
		task.setContactUsers(currentUsers);
		DialogUtility.closeParent(this);
	}

	public void actionPerformed(ActionType actionType) {
		if (actionType == ActionType.Cancel) {
			onCancel();
		} else if (actionType == ActionType.Add) {
			if (tab.getSelectedIndex() == tab.getTabCount() - 1) {
				onAddTaskLog();
			} else if (tab.getSelectedIndex() == tab.getTabCount() - 2) {
				onAddFiles();
			}
		} else if (actionType == ActionType.Save) {
			onSave();
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
		file.setTaskID(task.getTaskID());
		file.setProject(task.getProject());
		file.setFileName(file.generateFileName());
		try {
			fileManager.addFile(file, f);
			fileTableModel.updateFileList(fileManager.getFiles(task.getTaskID(), ApplicationContext.getCurrentUser()));
		} catch (Throwable a_th) {
			a_th.printStackTrace();
			JOptionPane.showMessageDialog(this, "could not save file", "File save error", JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void onExit() {
		taskFiles.removeMouseListener(fileTableMouseAdapter);
		taskLogs.removeMouseListener(fileTableMouseAdapter);

		tab.removeChangeListener(this);
		tab.removeAll();
		tab = null;

		btnBar.removeButtonBarListener(this);
		btnBar.onExit();
		btnBar = null;

		dateFormatter = null;
		taskName = null;
		taskDescription = null;
		chkMilestone = null;

		taskPriority.removeAllItems();
		taskPriority = null;

		taskStatus.removeAllItems();
		taskStatus = null;

		taskType.removeAllItems();
		taskType = null;

		taskAccess.removeAllItems();
		taskAccess = null;

		taskLogs = null;

		contactList.removeListSelectionListener(this);
		contactList.setCellRenderer(null);
		contactList.removeAll();
		contactList = null;
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

	public void valueChanged(ListSelectionEvent listEvent) {
		if (listEvent.getValueIsAdjusting()) {
			return;
		}
		BOUser user = (BOUser) contactList.getSelectedValue();
		addEditUser(user, currentUsers);
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
			/*
			 * BOUser checkoutUser = (BOUser)table.getValueAt(row, 7);
			 * if(checkoutUser != null && isSelected == false) {
			 * if(checkoutUser.equals(ApplicationContext.getCurrentUser())) {
			 * setBackground(checkOwnColor); }else {
			 * setBackground(checkElseColor); } }
			 */
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
				return files.get(row).getFileOwner();
			} else if (column == 7) {
				return files.get(row).getCheckedOutUser();
			} else if (column == 8) {
				return files.get(row).getFileCheckoutReason();
			} else if (column == 9) {
				if (dateFormatter != null) {
					return dateFormatter.format(files.get(row).getFileDate());
				} else {
					return files.get(row).getFileDate() + "";
				}
			}
			return "";
		}
	}

	private class FileTableMouseAdapter extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent event) {
			if (event.getSource() == taskLogs) {
				if (event.getClickCount() == 2) {
					DialogUtility.showDialog(new TaskLogPanel((BOTaskLog) taskLogs.getValueAt(taskLogs.getSelectedRow(), -1), task), "Task Log", new Dimension(400, 250));
				}
			} else {
				if (event.getButton() == MouseEvent.BUTTON3) {
					int selectedRow = taskFiles.getSelectedRow();

					checkOutMenu.setVisible(false);
					checkInMenu.setVisible(false);
					deleteFileMenu.setVisible(false);
					editFileMenu.setVisible(false);
					versionsFileMenu.setVisible(false);
					viewFileMenu.setVisible(false);
					
					if (selectedRow >= 0) {

						// means this is checked-out
						if (taskFiles.getValueAt(selectedRow, 0).toString().equalsIgnoreCase("true")) {
							// if i have checked-out then only i can check-in
							if (taskFiles.getValueAt(selectedRow, 7).equals(ApplicationContext.getCurrentUser())) {
								checkInMenu.setVisible(true);
							}
						} else {
							checkOutMenu.setVisible(true);
						}
						deleteFileMenu.setVisible(true);
						viewFileMenu.setVisible(true);
						//editFileMenu.setVisible(true);
						if(((BOFile)taskFiles.getValueAt(selectedRow, -1)).getVersionCount() > 1) {
							versionsFileMenu.setVisible(true);
						}
						
					}
					if(task.canBeEdited(ApplicationContext.getCurrentUser()) == false) {
						checkOutMenu.setVisible(false);
						checkInMenu.setVisible(false);
						addFileMenu.setVisible(false);
						if(ApplicationContext.getCurrentUser().getUserType() == UserType.Administrator) {
							deleteFileMenu.setVisible(true);
						}else {
							deleteFileMenu.setVisible(false);
						}
					}
					taskFilesPopupMenu.show((Component) event.getSource(), event.getX(), event.getY());
					
				}
			}
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
				onEdit();
			}
		}
	}
	
	private void onEdit() {
		int selectedIndex = taskFiles.getSelectedRow();
		if (selectedIndex < 0) {
			return;
		}

		FileEditorPanel fe = new FileEditorPanel(FileEditorMode.FileEdit);
		BOFile taskFile = (BOFile) taskFiles.getValueAt(selectedIndex, -1);
		fe.setFile(taskFile);
		DialogUtility.showDialog(fe, "Edit file", new Dimension(350, 250));
		
		if (fe.getRealFile() == null || fe.getFile() == null) {
			return;
		}
		BOFile file = fe.getFile();
		File f = fe.getRealFile();
		file.setTaskID(task.getTaskID());
		file.setProject(task.getProject());
		file.setFileName(file.generateFileName());
		try {
			fileManager.addFile(file, f);
			fileTableModel.updateFileList(fileManager.getFiles(task.getTaskID(), ApplicationContext.getCurrentUser()));
		} catch (Throwable a_th) {
			a_th.printStackTrace();
			JOptionPane.showMessageDialog(this, "could not save file, Please contact your vendor", "File save error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void onVersions() {
		int selectedIndex = taskFiles.getSelectedRow();
		if (selectedIndex < 0) {
			return;
		}
		BOFile taskFile = (BOFile) taskFiles.getValueAt(selectedIndex, -1);
		DialogUtility.showDialog(new FileVersionsPanel(fileManager.getAllVersions(taskFile.getRootFileID(), ApplicationContext.getCurrentUser())), "All versions", new Dimension(550, 200));
	}

	private void onViewFile() {
		int selectedIndex = taskFiles.getSelectedRow();
		if (selectedIndex < 0) {
			return;
		}
		BOFile taskFile = (BOFile) taskFiles.getValueAt(selectedIndex, -1);
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
		if (JFileChooser.CANCEL_OPTION == returnOption
				|| JFileChooser.ERROR_OPTION == returnOption) {
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
		int selectedIndex = taskFiles.getSelectedRow();
		if (selectedIndex < 0) {
			return;
		}
		
		if(JOptionPane.showConfirmDialog(ApplicationContext.getCurrentFrame(), "Are you sure that you want to delete the selected version?", "Delete Confirmation", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
			return;
		}

		BOFile taskFile = (BOFile) taskFiles.getValueAt(selectedIndex, -1);
		try {
			fileManager.deleteFile(taskFile, ApplicationContext.getCurrentUser());
			fileTableModel.updateFileList(fileManager.getFiles(task.getTaskID(), ApplicationContext.getCurrentUser()));
		} catch (Throwable a_th) {
			a_th.printStackTrace();
			JOptionPane.showMessageDialog(this, "could not delete file, Please contact your vendor", "File save error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void onCheckInFile() {
		int selectedIndex = taskFiles.getSelectedRow();
		if (selectedIndex < 0) {
			return;
		}

		BOFile taskFile = (BOFile) taskFiles.getValueAt(selectedIndex, -1);

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

		newTaskFile.setTaskID(task.getTaskID());
		newTaskFile.setProject(task.getProject());
		newTaskFile.setFileName(newTaskFile.generateFileName());
		newTaskFile.setFileVersion(taskFile.getFileVersion() + 1);
		newTaskFile.setFileDate(new Date());
		try {
			fileManager.checkInFile(taskFile, newTaskFile, realFile, ApplicationContext.getCurrentUser());
			fileTableModel.updateFileList(fileManager.getFiles(task.getTaskID(), ApplicationContext.getCurrentUser()));
		} catch (Throwable a_th) {
			a_th.printStackTrace();
			JOptionPane.showMessageDialog(this, "could not save file, Please contact your vendor", "File save error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void onCheckOutFile() {
		int selectedIndex = taskFiles.getSelectedRow();
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
		BOFile taskFile = (BOFile) taskFiles.getValueAt(selectedIndex, -1);
		taskFile.setCheckedOutUser(ApplicationContext.getCurrentUser());
		taskFile.setFileCheckoutReason(strReason);
		taskFile.setFileDate(new Date());
		taskFile.setFileCheckout(ApplicationContext.getCurrentUser().getUserID() + "");
		try {
			fileManager.checkOutFile(taskFile, ApplicationContext.getCurrentUser());
			fileTableModel.updateFileList(fileManager.getFiles(task.getTaskID(), ApplicationContext.getCurrentUser()));
		} catch (Throwable a_th) {
			a_th.printStackTrace();
			JOptionPane.showMessageDialog(this, "could not checkout, Please contact your vendor", "File save error", JOptionPane.ERROR_MESSAGE);
		}
	}
}