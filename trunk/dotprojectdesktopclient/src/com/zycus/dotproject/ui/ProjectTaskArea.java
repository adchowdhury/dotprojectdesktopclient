package com.zycus.dotproject.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.TreeModel;

import com.zycus.dotproject.bo.BOProject;
import com.zycus.dotproject.bo.BOTask;
import com.zycus.dotproject.bo.BOUser;
import com.zycus.dotproject.bo.Priority;
import com.zycus.dotproject.bo.TaskCompletionStatus;
import com.zycus.dotproject.factory.UserManagerFactory;
import com.zycus.dotproject.ui.TreeViewProjectModel.Columns;
import com.zycus.dotproject.ui.TreeViewProjectModel.PureTreeModel;
import com.zycus.dotproject.ui.component.IndentBorder;
import com.zycus.dotproject.ui.component.JTreeTable;
import com.zycus.dotproject.ui.component.TreeTableModel;
import com.zycus.dotproject.ui.component.datepicker.CalendarADC;
import com.zycus.dotproject.ui.event.ProjectSelectionListener;
import com.zycus.dotproject.ui.event.ViewChangeListener.ViewType;
import com.zycus.dotproject.util.ApplicationContext;
import com.zycus.dotproject.util.DialogUtility;
import com.zycus.dotproject.util.ProjectCalculator;

/**
 * http://www.jroller.com/santhosh/entry/tree_inside_jcombobox
 * 
 */
public class ProjectTaskArea extends JTreeTable {
	private TreeViewProjectModel		customModel	= null;
	private DotProjectPopupMenu			popup		= null;
	private DotProjectHeaderPopupMenu	headerPopup	= null;
	
	static class AddChildAction extends AbstractAction {
		private ProjectTaskArea	pta	= null;

		public AddChildAction(ProjectTaskArea pta) {
			this.pta = pta;
		}

		public void actionPerformed(ActionEvent e) {
			pta.onAddTask();
		}
	}

	static class AddTaskLogAction extends AbstractAction {
		private ProjectTaskArea	pta	= null;

		public AddTaskLogAction(ProjectTaskArea pta) {
			this.pta = pta;
		}

		public void actionPerformed(ActionEvent e) {
			int selectedRow = pta.getSelectedRow();
			if (selectedRow < 0) {
				return;
			}
			Object selectedObject = null;
			if ((selectedObject = pta.getValueAt(selectedRow, -1)) == null) {
				return;
			}
			if (selectedObject instanceof BOTask && ((BOTask) selectedObject).canAddTaskLog(ApplicationContext.getCurrentUser())) {
				DialogUtility.showDialog(new TaskLogPanel(null, (BOTask) selectedObject), "Add New Log", new Dimension(400, 300));
				pta.dataChanged();
			}
		}
	}

	static class TaskDetailsAction extends AbstractAction {
		private ProjectTaskArea	pta	= null;

		public TaskDetailsAction(ProjectTaskArea pta) {
			this.pta = pta;
		}

		public void actionPerformed(ActionEvent e) {
			int selectedRow = pta.getSelectedRow();
			if (selectedRow < 0) {
				return;
			}
			Object selectedObject = null;
			if ((selectedObject = pta.getValueAt(selectedRow, -1)) == null) {
				return;
			}
			if (selectedObject instanceof BOTask) {
				DialogUtility.showDialog(new TaskManagementPanel((BOTask) selectedObject), "Task Details : " + ((BOTask) selectedObject).getTaskName(), new Dimension(750, 350));
			}
		}
	}
	
	static class TaskDeleteAction extends AbstractAction {
		private ProjectTaskArea	pta	= null;

		public TaskDeleteAction(ProjectTaskArea pta) {
			this.pta = pta;
		}

		public void actionPerformed(ActionEvent e) {
			int selectedRow = pta.getSelectedRow();
			if (selectedRow < 0) {
				return;
			}
			Object selectedObject = null;
			if ((selectedObject = pta.getValueAt(selectedRow, -1)) == null) {
				return;
			}
			if (selectedObject instanceof BOTask) {
				pta.onDeleteTask();
			}
		}
	}

	static class ExpandAllAction extends AbstractAction {
		private JTree	tree	= null;

		public ExpandAllAction(JTree tree) {
			this.tree = tree;
		}

		public void actionPerformed(ActionEvent actionEvent) {
			for (int i = tree.getRowCount(); i >= 0; i--) {
				tree.expandRow(i);
			}
		}
	}

	static class CollapseAllAction extends AbstractAction {
		private JTree	tree	= null;

		public CollapseAllAction(JTree tree) {
			this.tree = tree;
		}

		public void actionPerformed(ActionEvent actionEvent) {
			int rowCount = tree.getRowCount();
			for (int i = rowCount; i >= 0; i--) {
				tree.collapseRow(i);
				if (rowCount != tree.getRowCount()) {
					i = rowCount = tree.getRowCount();
				}
			}
		}
	}

	class PopupListener extends MouseAdapter {
		DotProjectColumnSelectorPopupMenu	popupMenu	= new DotProjectColumnSelectorPopupMenu();

		public void mousePressed(MouseEvent e) {
			showPopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			showPopup(e);
		}

		private void showPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				popupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	private class DateEditor extends AbstractCellEditor implements TableCellEditor {
		private JTextField		dateText		= null;
		private ProjectTaskArea	pta				= null;
		private int				currentRow		= -1;
		private int				currentColumn	= -1;
		
		@Deprecated
		private DateComboBox	dateCombo		= null;
		private CalendarADC		dateComboADC	= null;

		private DateEditor(ProjectTaskArea pta) {
			this.pta = pta;
			dateText = new JTextField();
			dateCombo = new DateComboBox();
			dateCombo.setEditable(true);
			dateComboADC = new CalendarADC();
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			if (value != null && value instanceof Date) {
				//dateCombo.setSelectedItem(formatter.format((Date) value));
				dateComboADC.setDate((Date) value);
			}
			if (value == null) {
				//dateCombo.setSelectedItem(formatter.format(new Date()));
				dateComboADC.setDate(new Date());
			}
			currentRow = row;
			currentColumn = column;
			return dateComboADC/*dateCombo dateText */;
		}

		public Object getCellEditorValue() {
			
			dateText.setText(formatter.format(dateComboADC.getDate()));
			try {
				return formatter.parse(dateText.getText());
			} catch (ParseException e) {
				JOptionPane.showMessageDialog(ApplicationContext.getCurrentFrame(), "Please provide proper Date", "Invalid Date", JOptionPane.ERROR_MESSAGE);
				throw new RuntimeException(e);
			}
		}
	}

	static class ChildrenEnumeration implements Enumeration {
		TreeModel	treeModel;
		Object		node;
		int			index	= -1;

		public ChildrenEnumeration(TreeModel treeModel, Object node) {
			this.treeModel = treeModel;
			this.node = node;
		}

		public boolean hasMoreElements() {
			return index < treeModel.getChildCount(node) - 1;
		}

		public Object nextElement() {
			return treeModel.getChild(node, ++index);
		}
	}

	static class PreorderEnumeration implements Enumeration {
		private TreeModel						treeModel;
		protected Stack<Enumeration<Object>>	stack;

		public PreorderEnumeration(TreeModel treeModel) {
			this.treeModel = treeModel;
			Vector<Object> v = new Vector<Object>(1);
			v.addElement(treeModel.getRoot());
			stack = new Stack<Enumeration<Object>>();
			stack.push(v.elements());
		}

		public boolean hasMoreElements() {
			return (!stack.empty() && stack.peek().hasMoreElements());
		}

		public Object nextElement() {
			Enumeration enumer = stack.peek();
			Object node = enumer.nextElement();
			if (!enumer.hasMoreElements())
				stack.pop();
			Enumeration<Object> children = new ChildrenEnumeration(treeModel, node);
			if (children.hasMoreElements())
				stack.push(children);
			return node;
		}
	}

	private class AssineeListEditor extends AbstractCellEditor implements TableCellEditor {
		private JComboBox		userCombo		= null;
		private ProjectTaskArea	pta				= null;
		private int				currentRow		= -1;
		private List<BOUser>	allUsers		= null;
		private Set<BOUser>		currentUsers	= null;

		private AssineeListEditor(ProjectTaskArea pta) {
			this.pta = pta;
			allUsers = UserManagerFactory.getUserManager().getAllUsers();
			Collections.sort(allUsers);
			userCombo = new JComboBox(allUsers.toArray()) {
				@Override
				public void hidePopup() {
					// super.hidePopup();
				}

				@Override
				public void setPopupVisible(boolean v) {
					if (!v) {
						return;
					}
					super.setPopupVisible(v);
				}
			};
			userCombo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent actionEvent) {
					BOUser user = (BOUser) userCombo.getSelectedItem();
					addEditUser(user);
				}
			});
			userCombo.setRenderer(new CheckComboRenderer());
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			if (value instanceof Set) {
				currentUsers = (Set<BOUser>) value;
			}
			currentRow = row;
			return userCombo;
		}

		public Object getCellEditorValue() {
			return userCombo.getSelectedItem();
		}

		private Set<BOUser> removeUser(BOUser user, Set<BOUser> users) {
			Set<BOUser> usesReturn = new LinkedHashSet<BOUser>();
			for (BOUser cUser : users) {
				if (cUser.getUserID() == user.getUserID()) {
					continue;
				}
				usesReturn.add(cUser);
			}
			return usesReturn;
		}

		private void addEditUser(BOUser user) {
			if (user == null || currentUsers == null) {
				return;
			}
			if (isUserPresent(user)) {
				currentUsers = removeUser(user, currentUsers);
				// userCombo.showPopup();
				// userCombo.updateUI();
				// userCombo.showPopup();
			} else {
				currentUsers.add(user);
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

		private class CheckComboRenderer implements ListCellRenderer {
			JCheckBox	checkBox;

			public CheckComboRenderer() {
				checkBox = new JCheckBox();
			}

			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				BOUser user = (BOUser) value;
				checkBox.setText(user.toString());
				// System.out.println(UIManager.getColor(
				// "Menu.selectionBackground"));
				// System.out.println(UIManager.getColor("Menu.background"));
				checkBox.setBackground(isSelected ? UIManager.getColor("Menu.selectionBackground") : UIManager.getColor("Menu.background"));
				if (currentUsers != null && isUserPresent(user)) {
					checkBox.setSelected(true);
				} else {
					checkBox.setSelected(false);
				}
				return checkBox;
			}
		}

		@Override
		protected void fireEditingStopped() {
			int rows[] = pta.getSelectedRows();
			if (rows != null && rows.length > 0) {
				List<BOTask> tasks = new ArrayList<BOTask>();
				for (int row : rows) {
					if (pta.getModel().getValueAt(row, -1) != null) {
						if (pta.getModel().getValueAt(row, -1) instanceof BOTask) {
							BOTask currentTask = (BOTask) pta.getModel().getValueAt(row, -1);
							currentTask.setAssineeUsers(new LinkedHashSet<BOUser>(currentUsers));
							tasks.add(currentTask);
						}
					}
				}
				if (tasks.size() > 0) {
					ProjectCalculator.assineeChanged(currentUsers, tasks.toArray(new BOTask[] {}));
				}
			} else {
				if (currentRow >= 0 && (pta.getModel().getValueAt(currentRow, -1) != null)) {
					if (pta.getModel().getValueAt(currentRow, -1) instanceof BOTask) {
						((BOTask) pta.getModel().getValueAt(currentRow, -1)).setAssineeUsers(new LinkedHashSet<BOUser>(currentUsers));
						ProjectCalculator.assineeChanged(currentUsers, (BOTask) pta.getModel().getValueAt(currentRow, -1));
					}
				}
			}
			super.fireEditingStopped();
		}
	}

	private class PredecessorsListEditor extends AbstractCellEditor implements TableCellEditor {
		private JComboBox		taskCombo		= null;
		private ProjectTaskArea	pta				= null;
		private int				currentRow		= -1;
		private Set<BOTask>		currentTasks	= null;
		private Set<BOTask>		oldTasks		= new LinkedHashSet<BOTask>();
		// private PureTreeModel pureModel = new
		// TreeViewProjectModel.PureTreeModel();
		private TaskListModel	taskListModel	= new TaskListModel();

		/*
		 * private void updateAllTasks() { allTasks = new ArrayList<BOTask>();
		 * Enumeration enumer = new PreorderEnumeration(pureModel); while
		 * (enumer.hasMoreElements()) { Object obj = enumer.nextElement(); if
		 * (obj instanceof BOTask) { allTasks.add((BOTask) obj); } } }
		 */

		private PredecessorsListEditor(ProjectTaskArea pta) {
			this.pta = pta;
			taskCombo = new JComboBox(taskListModel) {
				@Override
				public void hidePopup() {
					// super.hidePopup();
				}

				@Override
				public void setPopupVisible(boolean v) {
					if (!v) {
						return;
					}
					super.setPopupVisible(v);
				}
			};
			taskCombo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent actionEvent) {
					if (taskCombo.getSelectedItem() == null) {
						return;
					}
					if (taskCombo.getSelectedItem() instanceof BOProject) {
						return;
					}
					BOTask task = (BOTask) taskCombo.getSelectedItem();
					addEditTask(task);
				}
			});

			taskCombo.setRenderer(new CheckComboRenderer());
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			if (value instanceof Set) {
				currentTasks = (Set<BOTask>) value;
				oldTasks.clear();
			}
			taskCombo.updateUI();
			/*
			 * updateAllTasks(); taskCombo.removeAllItems();
			 * 
			 * for (BOTask task : allTasks) { taskCombo.addItem(allTasks); }
			 * taskCombo.updateUI();
			 */
			currentRow = row;
			return taskCombo;
		}

		public Object getCellEditorValue() {
			return taskCombo.getSelectedItem();
		}

		private void addEditTask(BOTask task) {
			if (task == null || currentTasks == null) {
				return;
			}
			if (isTaskPresent(task)) {
				currentTasks.remove(task);
				oldTasks.add(task);
			} else {
				currentTasks.add(task);
				oldTasks.remove(task);
			}
		}

		private boolean isTaskPresent(BOTask task) {
			if (currentTasks == null) {
				return false;
			}
			for (BOTask cTask : currentTasks) {
				if (cTask.getEnhancedTaskID() == task.getEnhancedTaskID()) {
					return true;
				}
			}
			return false;
		}

		private class CheckComboRenderer implements ListCellRenderer {
			private JCheckBox	checkBox;
			private JLabel		lblBlank	= null;

			public CheckComboRenderer() {
				checkBox = new JCheckBox();
				lblBlank = new JLabel();
			}

			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				if (value == null) {
					return lblBlank;
				}
				checkBox.setText(value.toString());
				checkBox.setBackground(isSelected ? UIManager.getColor("Menu.selectionBackground") : UIManager.getColor("Menu.background"));
				if (value instanceof BOTask) {
					if (currentTasks != null && isTaskPresent((BOTask) value)) {
						checkBox.setSelected(true);
					} else {
						checkBox.setSelected(false);
					}
				} else {
					checkBox.setSelected(false);
				}
				int depth = (value instanceof BOProject) ? 0 : getDepth((BOTask) value);
				IndentBorder indentBorder = new IndentBorder();
				indentBorder.setDepth(depth);
				checkBox.setBorder(indentBorder);
				return checkBox;
			}
		}

		@Override
		protected void fireEditingStopped() {
			int rows[] = pta.getSelectedRows();
			if (rows != null && rows.length > 0) {
				for (int row : rows) {
					Object obj = pta.getModel().getValueAt(row, -1);
					if (obj != null) {
						if (obj instanceof BOTask) {
							for (BOTask t : oldTasks) {
								t.getDependentTasks().remove((BOTask) obj);
							}
							
							((BOTask) obj).setPreviousTasks(new LinkedHashSet<BOTask>(currentTasks));
							for (BOTask t : currentTasks) {
								t.getDependentTasks().add((BOTask) obj);
							}
							ProjectCalculator.predecessorsChanged(currentTasks, (BOTask) obj, ApplicationContext.getCurrentUser());
						}
					}
				}
			} else {
				if (currentRow >= 0 && (pta.getModel().getValueAt(currentRow, -1) != null)) {
					Object obj = pta.getModel().getValueAt(currentRow, -1);
					if (obj instanceof BOTask) {
						for (BOTask t : oldTasks) {
							t.getDependentTasks().remove((BOTask) obj);
						}
						
						((BOTask) obj).setPreviousTasks(new LinkedHashSet<BOTask>(currentTasks));
						for (BOTask t : currentTasks) {
							t.getDependentTasks().add((BOTask) obj);
						}
						ProjectCalculator.predecessorsChanged(currentTasks, (BOTask) obj, ApplicationContext.getCurrentUser());
					}
				}
			}
			super.fireEditingStopped();
			// pta.dataChanged();
		}
	}

	private class PriorityEditor extends AbstractCellEditor implements TableCellEditor {
		private ProjectTaskArea	pta				= null;
		private JComboBox		priorityCombo	= null;

		private PriorityEditor(ProjectTaskArea pta) {
			priorityCombo = new JComboBox(Priority.values());
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			if (value instanceof Priority) {
				priorityCombo.setSelectedItem(value);
			}
			return priorityCombo;
		}

		public Object getCellEditorValue() {
			return priorityCombo.getSelectedItem();
		}
	}

	private class UserListEditor extends AbstractCellEditor implements TableCellEditor {
		private JComboBox		userCombo	= null;
		private ProjectTaskArea	pta			= null;
		private int				currentRow	= -1;
		private List<BOUser>	users		= null;

		private UserListEditor(ProjectTaskArea pta) {
			this.pta = pta;
			users = UserManagerFactory.getUserManager().getAllUsers();
			userCombo = new JComboBox();
			Collections.sort(users);
			for (BOUser user : users) {
				userCombo.addItem(user);
			}
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			if (value instanceof BOUser) {
				for (BOUser user : users) {
					if (user.getUserID() == ((BOUser) value).getUserID()) {
						userCombo.setSelectedIndex(users.indexOf(user));
						break;
					}
				}
			}
			currentRow = row;
			return userCombo;
		}

		public Object getCellEditorValue() {
			return userCombo.getSelectedItem();
		}

		@Override
		protected void fireEditingStopped() {
			int rows[] = pta.getSelectedRows();
			if (rows != null && rows.length > 0) {
				for (int row : rows) {
					if (pta.getModel().getValueAt(row, -1) != null) {
						if (pta.getModel().getValueAt(row, -1) instanceof BOTask)
							((BOTask) pta.getModel().getValueAt(row, -1)).setOwner((BOUser) userCombo.getSelectedItem());
					}
				}
			} else {
				if (currentRow >= 0 && (pta.getModel().getValueAt(currentRow, -1) != null)) {
					if (pta.getModel().getValueAt(currentRow, -1) instanceof BOTask)
						((BOTask) pta.getModel().getValueAt(currentRow, -1)).setOwner((BOUser) userCombo.getSelectedItem());
				}
			}
			super.fireEditingStopped();
		}
	}

	private void removeObject(Set set, Object objToRemove) {
		int size = set.size();
		List lst = new ArrayList(set);
		set.clear();
		for (int iCounter = 0; iCounter < size; iCounter++) {
			if (lst.get(iCounter).hashCode() != objToRemove.hashCode()) {
				set.add(lst.get(iCounter));
			}
		}
	}

	private class TaskParenttEditor extends AbstractCellEditor implements TableCellEditor {
		private JComboBox		taskCombo		= null;
		private ProjectTaskArea	pta				= null;
		private int				currentRow		= -1;
		private TaskListModel	taskListModel	= new TaskListModel();

		private TaskParenttEditor(ProjectTaskArea pta) {
			this.pta = pta;
			taskCombo = new JComboBox(taskListModel);
			taskCombo.setRenderer(new TreeListCellRenderer());
			ApplicationContext.addProjectSelectionListener(new ProjectSelectionListener() {
				public void projectSelected(BOProject project) {
					if (taskCombo != null) {
						taskCombo.updateUI();
					}
				}
			});
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			taskCombo.updateUI();
			if (value instanceof BOTask) {
				taskListModel.setSelectedItem(value);
			}
			currentRow = row;
			return taskCombo;
		}

		public Object getCellEditorValue() {
			return taskCombo.getSelectedItem();
		}

		@Override
		protected void fireEditingStopped() {
			if (taskListModel.getSelectedItem() == null || !(taskListModel.getSelectedItem() instanceof BOTask)) {
				super.fireEditingStopped();
				return;
			}
			BOTask newParent = (BOTask) taskListModel.getSelectedItem();

			int rows[] = pta.getSelectedRows();
			if (rows != null && rows.length > 0) {
				List<BOTask> tasks = new ArrayList<BOTask>();
				for (int row : rows) {
					if (pta.getModel().getValueAt(row, -1) != null) {
						if (pta.getModel().getValueAt(row, -1) instanceof BOTask) {
							BOTask currentTask = (BOTask) pta.getModel().getValueAt(row, -1);
							BOTask oldParentTask = currentTask.getParentTask();

							if (oldParentTask == null || oldParentTask.equals(currentTask)) {
								ApplicationContext.getCurrentProjet().getTasks().remove(currentTask);
								removeObject(ApplicationContext.getCurrentProjet().getTasks(), currentTask);
							} else {
								oldParentTask.getChildTasks().remove(currentTask);
								removeObject(oldParentTask.getChildTasks(), currentTask);
							}

							currentTask.setParentTask(newParent);
							tasks.add(currentTask);

							if (newParent.equals(currentTask)) {
								ApplicationContext.getCurrentProjet().getTasks().add(currentTask);
							} else {
								newParent.getChildTasks().add(currentTask);
								newParent.getPreviousTasks().clear();
							}
							ProjectCalculator.parentChanged(newParent, oldParentTask, currentTask, ApplicationContext.getCurrentUser());
						}
					}
				}
			} else {
				if (currentRow >= 0 && (pta.getModel().getValueAt(currentRow, -1) != null)) {
					if (pta.getModel().getValueAt(currentRow, -1) instanceof BOTask) {
						BOTask currentTask = (BOTask) pta.getModel().getValueAt(currentRow, -1);
						BOTask oldParentTask = currentTask.getParentTask();

						if (oldParentTask == null || oldParentTask.equals(currentTask)) {
							ApplicationContext.getCurrentProjet().getTasks().remove(currentTask);
							removeObject(ApplicationContext.getCurrentProjet().getTasks(), currentTask);
						} else {
							oldParentTask.getChildTasks().remove(currentTask);
							removeObject(oldParentTask.getChildTasks(), currentTask);
						}
						currentTask.setParentTask(newParent);
						ProjectCalculator.parentChanged(newParent, oldParentTask, currentTask, ApplicationContext.getCurrentUser());

						if (newParent.equals(currentTask)) {
							ApplicationContext.getCurrentProjet().getTasks().add(currentTask);
						} else {
							newParent.getChildTasks().add(currentTask);
							newParent.getPreviousTasks().clear();
						}
					}
				}
			}
			super.fireEditingStopped();
		}
	}

	private void onExpandCollapse() {
		int selectedRow = getSelectedRow();
		if (getTree().isExpanded(selectedRow) == false) {
			getTree().expandRow(selectedRow);
		} else {
			getTree().collapseRow(selectedRow);
		}
		getTree().setSelectionRow(selectedRow);
	}

	public void dataChanged() {
		formatter = new SimpleDateFormat(ApplicationContext.getUserPreferences().getDateDisplayFormat());
		customModel.updateBaseData();
		updateUI();
	}

	private void onAddTask() {
		if (ApplicationContext.getViewTYpe() == ViewType.ResourceView) {
			StatusBar.showWarningStatusMessage("Task addition is not allowed in resource view");
			return;
		}
		int selectedRow = getSelectedRow();
		if (selectedRow < 0) {
			return;
		}
		Object selectedObject = null;
		if ((selectedObject = getValueAt(selectedRow, -1)) == null) {
			return;
		}
		BOTask childTask = new BOTask();
		if (selectedObject instanceof BOTask) {
			BOTask parentTask = (BOTask) selectedObject;
			if (parentTask.canAddTasks(ApplicationContext.getCurrentUser()) == false) {
				StatusBar.showWarningStatusMessage("You do not have sufficient privilege to add task");
				return;
			}
			childTask.setParentTask(parentTask);
			parentTask.getChildTasks().add(childTask);
			parentTask.getPreviousTasks().clear();
		} else if (selectedObject instanceof BOProject) {
			BOProject project = (BOProject) selectedObject;
			if (project.canAddTasks(ApplicationContext.getCurrentUser()) == false) {
				StatusBar.showWarningStatusMessage("You do not have sufficient privilege to add task");
				return;
			}
			childTask.setParentTask(childTask);
			project.getTasks().add(childTask);
		}
		childTask.setOwner(ApplicationContext.getCurrentUser());
		childTask.setTaskName("New task");
		// TODO temporary bug fix
		childTask.setEnhancedTaskID(-System.currentTimeMillis());
		childTask.setProject(ApplicationContext.getCurrentProjet());
		if (getTree().isExpanded(selectedRow) == false) {
			getTree().expandRow(selectedRow);
			getTree().setSelectionRow(selectedRow);
			// super.
		}
		ProjectCalculator.taskAdded(childTask, ApplicationContext.getCurrentUser());
		dataChanged();
	}

	private class TreeListCellRenderer extends DefaultListCellRenderer {
		private PureTreeModel	pureModel	= new TreeViewProjectModel.PureTreeModel();

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			TreeListCellRenderer.this.updateUI();
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			boolean leaf = pureModel.isLeaf(value);
			setIcon(leaf ? UIManager.getIcon("Tree.leafIcon") : UIManager.getIcon("Tree.openIcon"));
			int depth = (value instanceof BOProject) ? 0 : getDepth((BOTask) value);// somehow
			IndentBorder indentBorder = new IndentBorder();
			indentBorder.setDepth(depth);
			setBorder(indentBorder);
			return this;
		}

	}

	private int getDepth(BOTask task) {
		if (task.getParentTask().getEnhancedTaskID() == task.getEnhancedTaskID()) {
			return 1;
		} else {
			return getDepth(task.getParentTask()) + 1;
		}
	}

	private void onDeleteTask() {
		
		BOUser currentUser = ApplicationContext.getCurrentUser();
		System.out.println("#### MANINI ProjectTaskArea.onDeleteTask()");
		if (ApplicationContext.getViewTYpe() == ViewType.ResourceView) {
			StatusBar.showWarningStatusMessage("Task deletion is not allowed in resource view");
			return;
		}
		int[] rows = getSelectedRows();
		for (int row : rows) {
			if (row > 0) {// to ensure not delete project
				BOTask task = (BOTask) getValueAt(row, -1);
				if (task.canBeDeleted(currentUser) == false) {
					StatusBar.showErrorStatusMessage("You do not have sufficient privilege to delete task");
					continue;
				}
				
				ProjectCalculator.taskDeleted(task, currentUser);
				
				// this is for removing previous tasks
				for (BOTask dependendTask : task.getDependentTasks()) 
				{
					dependendTask.getPreviousTasks().remove(task);
				}
				
				for (BOUser user : task.getAssineeUsers()) 
				{
					user.getAssignedTasks().remove(task);
				}

				if (task.getParentTask() != null && task.getParentTask().getTaskID()!= task.getTaskID()) 
				{
					removeObject(task.getParentTask().getChildTasks(), task);
					// task.getParentTask().getChildTasks().remove(task);
				}
				if (task.getParentTask() != null && !task.equals(task.getParentTask())) 
				{
					removeObject(task.getParentTask().getChildTasks(), task);
					// task.getParentTask().getChildTasks().remove(task);
				} 
				else 
				{
					removeObject(task.getProject().getTasks(), task);
					// task.getProject().getTasks().remove(task);
				}
				
				task.getDependentTasks().clear();
				task.setDependentTasks(null);

				task.setParentTask(null);
				task.setProject(null);
				task.setOwner(null);

				task.getAssineeUsers().clear();
				task.setAssineeUsers(null);
			}
		}
		
		System.out.println("#### MANINI " + ApplicationContext.getCurrentProjet().getTasks());
		dataChanged();
	}

	private class TaskListModel extends AbstractListModel implements ComboBoxModel {
		private Object			selectedObject	= null;
		private PureTreeModel	pureModel		= new TreeViewProjectModel.PureTreeModel();

		public Object getSelectedItem() {
			return selectedObject;
		}

		public void setSelectedItem(Object anItem) {
			selectedObject = anItem;
		}

		public Object getElementAt(int index) {
			Enumeration enumer = new PreorderEnumeration(pureModel);
			for (int i = 0; i < index; i++)
				enumer.nextElement();
			return enumer.nextElement();
		}

		public int getSize() {
			int count = 0;
			Enumeration enumer = new PreorderEnumeration(pureModel);
			while (enumer.hasMoreElements()) {
				enumer.nextElement();
				count++;
			}
			return count;
		}
	}

	static class ColumnShowHideAction extends AbstractAction {
		private ProjectTaskArea	pta	= null;

		public ColumnShowHideAction(ProjectTaskArea pta) {
			this.pta = pta;
			for (Columns col : Columns.values()) {
				col.setTableColumn(pta.getColumnModel().getColumn(col.ordinal()));
			}
		}

		public void actionPerformed(ActionEvent e) {
			JCheckBoxMenuItem m = (JCheckBoxMenuItem) e.getSource();
			if (m == null) {
				return;
			}
			pta.showHideColumn(Columns.valueOfCaption(m.getText()), m.getState());
		}
	}

	public void showHideColumn(Columns col, boolean isSelected) {
		col.setVisible(isSelected);
		ArrayList<Boolean> visibleList = new ArrayList<Boolean>();
		if (isSelected == false) {
			getColumnModel().removeColumn(col.getTableColumn());
			ApplicationContext.getUserPreferences().getColumnsSettings().set(col.ordinal(), isSelected);
		} else {
			for (Columns innerCol : Columns.values()) {
				visibleList.add(innerCol.isVisible());
				if (innerCol.ordinal() >= col.ordinal() && innerCol.isVisible()) {
					getColumnModel().removeColumn(innerCol.getTableColumn());
				}
			}
			for (Columns innerCol : Columns.values()) {
				if (innerCol.ordinal() >= col.ordinal() && innerCol.isVisible()) {
					getColumnModel().addColumn(innerCol.getTableColumn());
				}
			}
			ApplicationContext.getUserPreferences().setColumnsSettings(visibleList);
		}
		dataChanged();
		ApplicationContext.saveSettings();
	}

	private void initColumnsVisibility() {
		ArrayList<Boolean> visibleList = ApplicationContext.getUserPreferences().getColumnsSettings();
		if (visibleList.size() <= 0) {
			for (Columns col : Columns.values()) {
				visibleList.add(col.isVisible());
			}
			return;
		}
		for (Columns col : Columns.values()) {
			col.setVisible(visibleList.get(col.ordinal()));
		}

		for (Columns innerCol : Columns.values()) {
			if (innerCol.isVisible() == false) {
				getColumnModel().removeColumn(innerCol.getTableColumn());
			}
		}
		headerPopup.updateSelectedState();
	}

	private void onDeleteCellValue() {
		int[] rows = getSelectedRows();
		int col = getSelectedColumn();
		// this is for fail-safe
		if (col < 0) {
			return;
		}
		for (int row : rows) {
			if (row > 0) {// to ensure not edit project
				BOTask task = (BOTask) getValueAt(row, -1);
				if (task.canBeDeleted(ApplicationContext.getCurrentUser()) == false) {
					StatusBar.showWarningStatusMessage("You do not have sufficient privilege to edit task");
					continue;
				}
				if (col == 3) {
					System.out.println("ProjectTaskArea.onDeleteCellValue(Assinees)");
				}

			}
		}
		dataChanged();
	}

	public ProjectTaskArea(TreeTableModel treeTableModel) {
		super(treeTableModel);
		// setAutoCreateRowSorter(true);
		setAutoResizeMode(AUTO_RESIZE_OFF);
		customModel = (TreeViewProjectModel) treeTableModel;
		customModel.pta = this;
		popup = new DotProjectPopupMenu(this);
		headerPopup = new DotProjectHeaderPopupMenu(this);
		getTableHeader().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.isPopupTrigger()) {
					headerPopup.show(e.getComponent(), e.getX(), e.getY());
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					headerPopup.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent keyEvent) {
				if (keyEvent.getKeyCode() == KeyEvent.VK_DELETE && keyEvent.isControlDown()) {
					System.err.println(".keyPressed()");
					keyEvent.consume();
					onDeleteCellValue();
				}
			}

			@Override
			public void keyReleased(KeyEvent keyEvent) {
				if (keyEvent.getKeyCode() == KeyEvent.VK_INSERT && keyEvent.isControlDown()) {
					onAddTask();
				} else if (keyEvent.getKeyCode() == KeyEvent.VK_DELETE && keyEvent.isControlDown() == false) {
					onDeleteTask();
				} else if (keyEvent.getKeyCode() == KeyEvent.VK_MULTIPLY && keyEvent.isControlDown()) {
					onExpandCollapse();
				}
			}
		});

		setPreferedWidths();
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			}

			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (getTree().isExpanded(getSelectedRow())) {
						getTree().collapseRow(getSelectedRow());
					} else {
						getTree().expandRow(getSelectedRow());
					}
					return;
				}
				if (e.isPopupTrigger()) {
					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
		ApplicationContext.addProjectSelectionListener((ProjectSelectionListener) customModel);
		ApplicationContext.addProjectSelectionListener(new ProjectSelectionListener() {
			public void projectSelected(BOProject project) {
				ProjectTaskArea.this.dataChanged();
			}
		});

		// setIntercellSpacing(new Dimension(6, 0));
		// tree.putClientProperty("JTree.lineStyle", "Horizontal");
		// setShowHorizontalLines(true);
		setDefaultEditor(Priority.class, new PriorityEditor(this));
		setDefaultEditor(BOUser.class, new UserListEditor(this));
		setDefaultEditor(Date.class, new DateEditor(this));
		setDefaultEditor(BOTask.class, new TaskParenttEditor(this));
		getColumnModel().getColumn(TreeViewProjectModel.Columns.Assinees.ordinal()).setCellEditor(new AssineeListEditor(this));
		getColumnModel().getColumn(TreeViewProjectModel.Columns.Predecessors.ordinal()).setCellEditor(new PredecessorsListEditor(this));
		DefaultRenderer defaultRenderer = new DefaultRenderer(this);

		setDefaultRenderer(Priority.class, defaultRenderer);
		setDefaultRenderer(String.class, defaultRenderer);
		setDefaultRenderer(Date.class, defaultRenderer);
		setDefaultRenderer(Integer.class, defaultRenderer);
		setDefaultRenderer(Float.class, defaultRenderer);
		setDefaultRenderer(BOUser.class, defaultRenderer);
		setDefaultRenderer(BOTask.class, defaultRenderer);

		initColumnsVisibility();
	}

	static class DefaultRenderer extends DefaultTableCellRenderer {
		private ProjectTaskArea	pta				= null;
		protected Border		highlightBorder	= null;

		public DefaultRenderer(ProjectTaskArea pta) {
			this.pta = pta;
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			Color background = null;
			Color foreground = null;

			if (value != null && value instanceof Date) {
				setText(pta.formatter.format(value));
			}
			Object currentVal = pta.getValueAt(row, -1);
			if (currentVal != null && currentVal instanceof BOUser) {
				// just incase of user yellow is required
				background = new Color(255, 255, 204);
				foreground = Color.BLACK;
			} else {
				if (isSelected) {
					background = table.getSelectionBackground();
					foreground = table.getSelectionForeground();
				} else {
					background = table.getBackground();
					foreground = table.getForeground();
				}
				highlightBorder = null;
				if (hasFocus) {
					highlightBorder = UIManager.getBorder("Table.focusCellHighlightBorder");
				}

				if (isSelected || hasFocus) {
					setBackground(UIManager.getColor("Table.selectionBackground"));
				} else {
					setBackground(Color.white);
				}
			}
			setForeground(foreground);
			setBackground(background);

			if (value != null && value instanceof Number) {
				setHorizontalAlignment(RIGHT);
			} else {
				setHorizontalAlignment(LEFT);
			}
			if (pta.getColumnClass(column) == Integer.class || pta.getColumnClass(column) == Float.class) {
				setHorizontalAlignment(RIGHT);
			}
			if (table.getColumnModel().getColumn(column).getHeaderValue().toString().equalsIgnoreCase(Columns.Percentage.toString()) && value != null) {
				setText(value.toString() + " %");
				setHorizontalAlignment(RIGHT);
				if(currentVal instanceof BOTask) {
					setBackground(getTaskColor((BOTask)currentVal, background));
				}
			} else if (value != null && value instanceof Collection) {
				if (((Collection) value).size() <= 0) {
					setText("");
				}
			}
			return this;
		}
		
		private Color getTaskColor(BOTask task, Color currentColor) {
			TaskCompletionStatus taskCompletionStatus = task.getTaskCompletionStatus();
			if(taskCompletionStatus== TaskCompletionStatus.Normal) {
				return currentColor;
			}else {
				return taskCompletionStatus.getDisplayColor();
			}
		}
	}

	private SimpleDateFormat	formatter	= new SimpleDateFormat(ApplicationContext.getUserPreferences().getDateDisplayFormat());

	private void setPreferedWidths() {
		getColumnModel().getColumn(Columns.ID.ordinal()).setWidth(50);
		getColumnModel().getColumn(Columns.ID.ordinal()).setMaxWidth(50);

		getColumnModel().getColumn(Columns.TaskName.ordinal()).setPreferredWidth(300);
		getColumnModel().getColumn(Columns.Parent.ordinal()).setPreferredWidth(150);
		getColumnModel().getColumn(Columns.Predecessors.ordinal()).setPreferredWidth(150);
		getColumnModel().getColumn(Columns.StartDate.ordinal()).setPreferredWidth(100);
		getColumnModel().getColumn(Columns.EndDate.ordinal()).setPreferredWidth(100);
		getColumnModel().getColumn(Columns.Duration.ordinal()).setPreferredWidth(50);
		getColumnModel().getColumn(Columns.Percentage.ordinal()).setPreferredWidth(50);
	}

	public int getRowAtPoint(Point p) {
		Point location = getLocationOnScreen();
		int row = -1;
		int mouseY = p.y;
		int tableY = location.y;
		int rowHeight = getRowHeight();
		row = (int) Math.floor((mouseY - tableY) / rowHeight);
		return row;
	}

	@Override
	public String getToolTipText(MouseEvent event) {
		Point p = event.getLocationOnScreen();
		int rowIndex = getRowAtPoint(p);
		int colIndex = columnAtPoint(p);
		if (rowIndex < 0) {
			return super.getToolTipText(event);
		}
		Object value = getValueAt(rowIndex, colIndex);
		if (value == null) {
			return super.getToolTipText(event);
		}
		if (value instanceof Set && ((Set) value).size() <= 0) {
			return null;
		}
		/*
		 * if(colIndex == Columns.TaskName.ordinal() && getValueAt(rowIndex, -1)
		 * instanceof BOTask) { BOTask t = (BOTask)getValueAt(rowIndex, -1);
		 * if(t.getTaskDescription() == null) { return t.getTaskName(); }else {
		 * return t.getTaskName() + " [ " + t.getTaskDescription() + " ] "; }
		 * 
		 * }
		 */
		return value.toString();
	}
}