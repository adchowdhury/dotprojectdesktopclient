package com.zycus.dotproject.ui;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.event.TreeModelListener;
import javax.swing.table.TableColumn;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.zycus.dotproject.bo.BOProject;
import com.zycus.dotproject.bo.BOTask;
import com.zycus.dotproject.bo.BOUser;
import com.zycus.dotproject.bo.Priority;
import com.zycus.dotproject.ui.ProjectTaskArea.PreorderEnumeration;
import com.zycus.dotproject.ui.component.TreeTableModel;
import com.zycus.dotproject.ui.event.ProjectSelectionListener;
import com.zycus.dotproject.ui.event.ViewChangeListener.ViewType;
import com.zycus.dotproject.util.ApplicationContext;
import com.zycus.dotproject.util.ProjectCalculator;

public class TreeViewProjectModel implements TreeTableModel, ProjectSelectionListener {
	private PureTreeModel								treeModel		= new PureTreeModel();
	private BOProject									currentProject	= null;
	private Map<BOProject, Map<BOUser, List<BOTask>>>	baseData		= null;
	private static NumberFormat							formatter		= new DecimalFormat("#0.00");
	protected ProjectTaskArea							pta				= null;
	private static Comparator<BOTask>					taskComparator	= new Comparator<BOTask>() {
																			public int compare(BOTask o1, BOTask o2) {
																				if (o1.getStartDate() == null) {
																					return -1;
																				}
																				if (o2.getStartDate() == null) {
																					return 1;
																				}
																				return o1.getStartDate().compareTo(o2.getStartDate());
																			}
																		};

	public static enum Columns {
		ID(Integer.class, "ID"), TaskName(TreeTableModel.class, "Task Name"), Creator(BOUser.class, "Owner"), Assinees(String.class, "Assinees"), StartDate(Date.class, "Start Date"), EndDate(
				Date.class, "End Date"), Priority(com.zycus.dotproject.bo.Priority.class, "Priority"), Module(String.class, "Module"), Duration(Float.class, "Duration (in days)"), Percentage(
				Integer.class, "Percentage"), Parent(BOTask.class, "Parent"), Predecessors(String.class, "Predessors");
		private Class		dataClass	= null;
		private String		caption		= null;
		private boolean		isVisible	= true;
		private TableColumn	tableColumn	= null;
		
		public TableColumn getTableColumn() {
			return tableColumn;
		}
		
		public void setTableColumn(TableColumn tableColumn) {
			this.tableColumn = tableColumn;
		}

		private Columns(Class a_dataClass, String caption) {
			this.dataClass = a_dataClass;
			this.caption = caption;
		}

		public boolean isVisible() {
			return isVisible;
		}

		public void setVisible(boolean isVisible) {
			this.isVisible = isVisible;
		}

		public Class getDataClass() {
			return dataClass;
		}

		@Override
		public String toString() {
			return caption;
		}

		public static Columns valueOfCaption(String strCaption) {
			if (strCaption == null) {
				throw new IllegalArgumentException("Null value passed");
			}
			for (Columns col : values()) {
				if (col.toString().equalsIgnoreCase(strCaption)) {
					return col;
				}
			}
			throw new IllegalArgumentException("Not found");
		}
	}

	public void updateBaseData() {
		if (ApplicationContext.getCurrentProjet() == null) {
			return;
		}
		if (ApplicationContext.getViewTYpe() != ViewType.ResourceView) {
			return;
		}
		baseData = new LinkedHashMap<BOProject, Map<BOUser, List<BOTask>>>();
		Map<BOUser, List<BOTask>> projectValue = new LinkedHashMap<BOUser, List<BOTask>>();
		List<BOTask> userTasks = new ArrayList<BOTask>();
		List<BOUser> userLists = new ArrayList<BOUser>();

		BOUser unassignedUser = new BOUser();
		userTasks = new ArrayList<BOTask>();
		projectValue.put(unassignedUser, userTasks);

		unassignedUser.setLoginName("Unassigned");
		PreorderEnumeration l_Enum = new ProjectTaskArea.PreorderEnumeration(treeModel);
		while (l_Enum.hasMoreElements()) {
			Object obj = l_Enum.nextElement();
			if (obj instanceof BOProject) {
				continue;// no need to project
			}
			BOTask task = (BOTask) obj;
			if (task.getChildTasks().size() > 0) {
				continue;// no need of parent tasks
			}
			for (BOUser user : task.getAssineeUsers()) {
				if (!userLists.contains(user)) {
					userLists.add(user);
				}
				if (projectValue.get(user) == null || projectValue.containsKey(user) == false) {
					userTasks = new ArrayList<BOTask>();
					projectValue.put(user, userTasks);
				}
				if (projectValue.get(user).indexOf(task) < 0) {
					projectValue.get(user).add(task);
				}
			}
			if (task.getAssineeUsers().size() <= 0) {
				projectValue.get(unassignedUser).add(task);
			}
		}
		// if no task unassigned then pls ignore the Unassigned user
		if (projectValue.get(unassignedUser).size() <= 0) {
			projectValue.remove(unassignedUser);
		}

		baseData.put(currentProject, projectValue);
		for (List<BOTask> lstTask : projectValue.values()) {
			Collections.sort(lstTask, taskComparator);
		}
	}

	public TreeViewProjectModel() {
		currentProject = ApplicationContext.getCurrentProjet();
	}

	public Class getColumnClass(int column) {
		return Columns.values()[column].getDataClass();
	}

	public int getColumnCount() {
		return Columns.values().length;
	}

	public String getColumnName(int column) {
		return Columns.values()[column].toString();
	}

	public Object getValueAt(Object node, int column) {
		if (column == -1) {
			return node;
		}
		if (node instanceof BOTask) {
			if (column == Columns.ID.ordinal()) {
				if (((BOTask) node).getEnhancedTaskID() <= 0) {
					return "*";
				}
				return ((BOTask) node).getEnhancedTaskID();
			} else if (column == Columns.TaskName.ordinal()) {
				return ((BOTask) node).getTaskName();
			} else if (column == Columns.Creator.ordinal()) {
				return ((BOTask) node).getOwner();
			} else if (column == Columns.StartDate.ordinal()) {
				return ((BOTask) node).getStartDate();
			} else if (column == Columns.EndDate.ordinal()) {
				return ((BOTask) node).getEndDate();
			} else if (column == Columns.Percentage.ordinal()) {
				return formatter.format(((BOTask) node).getPercentageCompleted());
			} else if (column == Columns.Parent.ordinal()) {
				return ((BOTask) node).getParentTask();
			} else if (column == Columns.Duration.ordinal()) {
				return ((BOTask) node).getDurationInDays();//formatter.format(((
															// BOTask) node).
															// getDurationInDays
															// ());
			} else if (column == Columns.Assinees.ordinal()) {
				return ((BOTask) node).getAssineeUsers();
			} else if (column == Columns.Predecessors.ordinal()) {
				return ((BOTask) node).getPreviousTasks();
			} else if (column == Columns.Priority.ordinal()) {
				return ((BOTask) node).getTaskPriority();
			} else if (column == Columns.Module.ordinal()) {
				return ((BOTask) node).getTaskModule();
			}
		} else if (node instanceof BOProject) {
			if (column == Columns.ID.ordinal()) {
				return ((BOProject) node).getProjectID();
			} else if (column == Columns.TaskName.ordinal()) {
				return ((BOProject) node).getProjectName();
			} else if (column == Columns.Creator.ordinal()) {
				return ((BOProject) node).getProjectOwner();
			} else if (column == Columns.StartDate.ordinal()) {
				return ((BOProject) node).getProjectStartDate();
			} else if (column == Columns.EndDate.ordinal()) {
				return ((BOProject) node).getProjectEndDate();
			}
		} else if (node instanceof BOUser) {
			if (column == Columns.StartDate.ordinal()) {
				return baseData.get(currentProject).get(node).get(0).getStartDate();
			} else if (column == Columns.EndDate.ordinal()) {
				return baseData.get(currentProject).get(node).get(baseData.get(currentProject).get(node).size() - 1).getEndDate();
			} else if (column == Columns.Duration.ordinal()) {
				List<BOTask> tasks = baseData.get(currentProject).get(node);
				float duration = 0;
				for (BOTask task : tasks) {
					duration += task.getDurationInDays();
				}
				return duration;
			} else if (column == Columns.Percentage.ordinal()) {
				List<BOTask> tasks = baseData.get(currentProject).get(node);
				float percentage = 0;
				for (BOTask task : tasks) {
					percentage += task.getPercentageCompleted();
				}
				return formatter.format(percentage / tasks.size());
			}
		}
		return null;
	}

	public boolean isCellEditable(Object node, int column) {
		if (node instanceof BOTask) {
			if (((BOTask) node).canBeEdited(ApplicationContext.getCurrentUser()) == false) {
				return false;
			}
			if ((column == Columns.Creator.ordinal() || column == Columns.Module.ordinal() || column == Columns.TaskName.ordinal() || column == Columns.Priority.ordinal()
					|| column == Columns.Parent.ordinal() || column == Columns.Assinees.ordinal())) {
				return true;
			} else if (column == Columns.StartDate.ordinal()) {
				return ((BOTask) node).getPreviousTasks().size() <= 0 && ((BOTask) node).getChildTasks().size() <= 0;
			} else if (column == Columns.Duration.ordinal()) {
				return ((BOTask) node).getChildTasks().size() <= 0;
			} else if (column == Columns.Predecessors.ordinal()) {
				return ((BOTask) node).getChildTasks().size() <= 0;
			}
		}
		return false;
	}

	public void setValueAt(Object value, Object node, int column) {
		if (node instanceof BOTask) {
			if (column == Columns.TaskName.ordinal()) {
				((BOTask) node).setTaskName(value.toString());
			} else if (column == Columns.Duration.ordinal()) {
				int rows[] = pta.getSelectedRows();
				if (rows != null && rows.length > 0) {
					for (int row : rows) {
						Object obj = pta.getModel().getValueAt(row, -1);
						if (obj != null) {
							if (obj instanceof BOTask) {
								((BOTask) node).setDurationInDays(Float.parseFloat(value.toString()));
								ProjectCalculator.startDateOrDurationChanged((BOTask) node);
							}
						}
					}
				} else {
					((BOTask) node).setDurationInDays(Float.parseFloat(value.toString()));
					ProjectCalculator.startDateOrDurationChanged((BOTask) node);
				}
			} else if (column == Columns.Module.ordinal()) {
				int rows[] = pta.getSelectedRows();
				if (rows != null && rows.length > 0) {
					for (int row : rows) {
						Object obj = pta.getModel().getValueAt(row, -1);
						if (obj != null) {
							if (obj instanceof BOTask) {
								((BOTask) obj).setTaskModule(value.toString());
							}
						}
					}
				} else {
					((BOTask) node).setTaskModule(value.toString());
				}
			} else if (column == Columns.Priority.ordinal()) {
				int rows[] = pta.getSelectedRows();
				if (rows != null && rows.length > 0) {
					for (int row : rows) {
						Object obj = pta.getModel().getValueAt(row, -1);
						if (obj != null) {
							if (obj instanceof BOTask) {
								((BOTask) obj).setTaskPriority((Priority) value);
							}
						}
					}
				} else {
					((BOTask) node).setTaskPriority((Priority) value);
				}
			} else if (column == Columns.StartDate.ordinal()) {
				int rows[] = pta.getSelectedRows();
				if (rows != null && rows.length > 0) {
					for (int row : rows) {
						Object obj = pta.getModel().getValueAt(row, -1);
						if (obj != null) {
							if (obj instanceof BOTask) {
								((BOTask) obj).setStartDate((Date) value);
								ProjectCalculator.startDateOrDurationChanged((BOTask) obj);
							}
						}
					}
				} else {
					((BOTask) node).setStartDate((Date) value);
					ProjectCalculator.startDateOrDurationChanged((BOTask) node);
				}
			}
		}
		// updateBaseData();
		pta.dataChanged();
	}

	public void addTreeModelListener(TreeModelListener l) {

	}

	public Object getChild(Object parent, int index) {
		if (ApplicationContext.getViewTYpe() == ViewType.TreeView) {
			return treeModel.getChild(parent, index);
		} else if (ApplicationContext.getViewTYpe() == ViewType.ResourceView) {
			if (parent instanceof BOProject) {
				return baseData.get(parent).keySet().toArray()[index];
			} else if (parent instanceof BOUser) {
				return baseData.get(currentProject).get(parent).get(index);
			}
		}
		return null;
	}

	public int getChildCount(Object parent) {
		if (ApplicationContext.getViewTYpe() == ViewType.TreeView) {
			return treeModel.getChildCount(parent);
		} else if (ApplicationContext.getViewTYpe() == ViewType.ResourceView) {
			if (parent instanceof BOProject) {
				return baseData.get(parent).size();
			} else if (parent instanceof BOUser) {
				if (baseData.get(currentProject).get(parent) != null)
					return baseData.get(currentProject).get(parent).size();
			}
		}
		return 0;
	}

	public int getIndexOfChild(Object parent, Object child) {
		if (ApplicationContext.getViewTYpe() == ViewType.TreeView) {
			return treeModel.getIndexOfChild(parent, child);
		} else if (ApplicationContext.getViewTYpe() == ViewType.ResourceView) {
			if (parent instanceof BOProject) {
				return new ArrayList<BOUser>(baseData.get(parent).keySet()).indexOf(child);
			} else if (parent instanceof BOUser) {
				return baseData.get(currentProject).get(parent).indexOf(child);
			}
		}

		return -1;
	}

	public Object getRoot() {
		return treeModel.getRoot();
	}

	public boolean isLeaf(Object node) {
		return getChildCount(node) <= 0;
	}

	public void removeTreeModelListener(TreeModelListener l) {

	}

	public void valueForPathChanged(TreePath path, Object newValue) {

	}

	public void projectSelected(BOProject project) {
		currentProject = project;
		updateBaseData();
	}

	static class PureTreeModel implements TreeModel {

		public PureTreeModel() {

		}

		public void addTreeModelListener(TreeModelListener l) {

		}

		public Object getChild(Object parent, int index) {
			if (parent instanceof BOTask) {
				if (((BOTask) parent).getChildTasks() != null) {
					ArrayList<BOTask> childs = new ArrayList<BOTask>(((BOTask) parent).getChildTasks());
					Collections.sort(childs, taskComparator);
					return childs.get(index);
				}
			} else if (parent instanceof BOProject) {
				ArrayList<BOTask> childs = new ArrayList<BOTask>(((BOProject) parent).getTasks());
				Collections.sort(childs, taskComparator);
				return childs.get(index);
			}
			return null;
		}

		public int getChildCount(Object parent) {
			if (parent instanceof BOTask) {
				if (((BOTask) parent).getChildTasks() != null) {
					return ((BOTask) parent).getChildTasks().size();
				}
			} else if (parent instanceof BOProject) {
				return ((BOProject) parent).getTasks().size();
			}
			return 0;
		}

		public int getIndexOfChild(Object parent, Object child) {
			if (parent instanceof BOTask) {
				if (((BOTask) parent).getChildTasks() != null) {
					ArrayList<BOTask> childs = new ArrayList<BOTask>(((BOTask) parent).getChildTasks());
					Collections.sort(childs, taskComparator);
					return childs.indexOf(child);
				}
			} else if (parent instanceof BOProject) {
				ArrayList<BOTask> childs = new ArrayList<BOTask>(((BOProject) parent).getTasks());
				Collections.sort(childs, taskComparator);
				return childs.indexOf(child);
			}
			return -1;
		}

		public Object getRoot() {
			return ApplicationContext.getCurrentProjet();
		}

		public boolean isLeaf(Object node) {
			return getChildCount(node) <= 0;
		}

		public void removeTreeModelListener(TreeModelListener l) {

		}

		public void valueForPathChanged(TreePath path, Object newValue) {

		}
	}
}