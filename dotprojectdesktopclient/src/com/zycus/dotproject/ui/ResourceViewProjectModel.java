package com.zycus.dotproject.ui;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;

import com.zycus.dotproject.bo.BOProject;
import com.zycus.dotproject.bo.BOTask;
import com.zycus.dotproject.bo.BOUser;
import com.zycus.dotproject.ui.ProjectTaskArea.PreorderEnumeration;
import com.zycus.dotproject.ui.TreeViewProjectModel.Columns;
import com.zycus.dotproject.ui.component.TreeTableModel;
import com.zycus.dotproject.ui.event.ProjectSelectionListener;
import com.zycus.dotproject.util.ApplicationContext;
import com.zycus.dotproject.util.ProjectCalculator;

@Deprecated
public class ResourceViewProjectModel implements TreeTableModel, ProjectSelectionListener {
	private Map<BOProject, Map<BOUser, List<BOTask>>>	baseData		= null;
	private BOProject									currentProject	= null;

	private void updateBaseData() {
		if(ApplicationContext.getCurrentProjet() == null) {
			return;
		}
		baseData = new LinkedHashMap<BOProject, Map<BOUser, List<BOTask>>>();
		Map<BOUser, List<BOTask>> projectValue = new LinkedHashMap<BOUser, List<BOTask>>();
		List<BOTask> userTasks = new ArrayList<BOTask>();
		List<BOUser> userLists = new ArrayList<BOUser>();
		TreeViewProjectModel model = new TreeViewProjectModel();
		model.projectSelected(ApplicationContext.getCurrentProjet());
		
		PreorderEnumeration l_Enum = new ProjectTaskArea.PreorderEnumeration(model);
		while(l_Enum.hasMoreElements()) {
			Object obj = l_Enum.nextElement();
			if(obj instanceof BOProject) {
				continue;//no need to project
			}
			BOTask task = (BOTask)obj;
			if(task.getChildTasks().size() > 0) {
				continue;//no need of parent tasks
			}
			for(BOUser user : task.getAssineeUsers()) {
//				System.out.println(userLists + " -> " + user);
				if(!userLists.contains(user)) {
					userLists.add(user);
				}
				if(projectValue.get(user) == null || 
						projectValue.containsKey(user) == false) {
					userTasks = new ArrayList<BOTask>();
					projectValue.put(user, userTasks);
				}
				projectValue.get(user).add(task);
			}
		}
//		for(BOUser user : projectValue.keySet()) {
//			System.out.println(user + " : " + user.hashCode());
//		}
//		System.out.println(projectValue.keySet());
		baseData.put(currentProject, projectValue);
	}
	
	public ResourceViewProjectModel() {
		currentProject = ApplicationContext.getCurrentProjet();
		updateBaseData();
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
					return null;
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
				return ((BOTask) node).getPercentageCompleted() + " % ";
			} else if (column == Columns.Parent.ordinal()) {
				return ((BOTask) node).getParentTask();
			} else if (column == Columns.Duration.ordinal()) {
				return ((BOTask) node).getDurationInDays();
			} else if (column == Columns.Assinees.ordinal()) {
				return ((BOTask) node).getAssineeUsers();
			} else if (column == Columns.Predecessors.ordinal()) {
				return ((BOTask) node).getPreviousTasks();
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
			if (column == Columns.TaskName.ordinal()) {
				return ((BOUser) node).getLoginName();
			}
		}
		return null;
	}

	public boolean isCellEditable(Object node, int column) {
		if (node instanceof BOTask) {
			if ((column == Columns.Creator.ordinal() 
					|| column == Columns.TaskName.ordinal() 
					//|| column == Columns.Parent.ordinal() 
					|| column == Columns.Assinees.ordinal())) {
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
				((BOTask) node).setDurationInDays(Integer.parseInt(value.toString()));
				//ProjectCalculator.durationChanged(((BOTask) node), Integer.parseInt(value.toString()));
			}
			
		}
		updateBaseData();
		// System.out.println(value + " - " + node + " - " + column);
	}

	public void addTreeModelListener(TreeModelListener l) {

	}

	public Object getChild(Object parent, int index) {
		if (parent instanceof BOProject) {
			return baseData.get(parent).keySet().toArray()[index];
		} else if (parent instanceof BOUser) {
			return baseData.get(currentProject).get(parent).get(index);
		}
		return null;
	}

	public int getChildCount(Object parent) {
		if (parent instanceof BOProject) {
			return baseData.get(parent).size();
		} else if (parent instanceof BOUser) {
			return baseData.get(currentProject).get(parent).size();
		}
		return 0;
	}

	public int getIndexOfChild(Object parent, Object child) {
		if (parent instanceof BOProject) {
			return new ArrayList<BOUser>(baseData.get(parent).keySet()).indexOf(child);
		} else if (parent instanceof BOUser) {
			return baseData.get(currentProject).get(parent).indexOf(child);
		}
		return -1;
	}

	public Object getRoot() {
		return currentProject;
	}

	public boolean isLeaf(Object node) {
		return getChildCount(node) <= 0;
	}

	public void removeTreeModelListener(TreeModelListener l) {

	}

	public void valueForPathChanged(TreePath path, Object newValue) {

	}

	public void projectSelected(BOProject project) {
		if(currentProject != null &&
				project != null &&
				currentProject.equals(project)) {
			return;
		}
		currentProject = project;
		updateBaseData();
	}
}