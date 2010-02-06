package com.zycus.dotproject.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.zycus.dotproject.factory.UserManagerFactory;

public class BOProject implements Serializable, Comparable<BOProject> {

	private static final long		serialVersionUID			= 1l;

	private long					projectID					= 0L;
	private String					projectName					= null;
	private BOUser					projectOwner				= null;
	private BOCompany				projectCompany				= null;
	private ProjectType				projectType					= ProjectType.Unknown;
	private ProjectStatus			projectStatus				= ProjectStatus.ProjectNotDefined;
	private Priority				projectPriority				= Priority.Normal;
	private String					projectDescription			= null;

	private Set<BODepartment>		projectDepartments			= null;
	private Set<BOTask>				tasks						= null;
	private List<BOUser>			contactUsers				= null;
	private DateRange				projectRange				= new DateRange();
	private String					contactStringUsers			= null;

	private ArrayList<BOHistory>	currentHistoryForProject	= new ArrayList<BOHistory>();

	public long getProjectID() {
		return projectID;
	}

	public void setProjectID(long projectID) {
		this.projectID = projectID;
	}

	public BOUser getProjectOwner() {
		return projectOwner;
	}

	public void setProjectOwner(BOUser projectOwner) {
		this.projectOwner = projectOwner;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectDescription() {
		return projectDescription;
	}

	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	public int compareTo(BOProject o) {
		return getProjectName().compareTo(o.getProjectName());
	}

	@Override
	public String toString() {
		return getProjectName();
	}

	public ProjectStatus getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(ProjectStatus projectStatus) {
		this.projectStatus = projectStatus;
	}

	public Set<BOTask> getTasks() {
		if (tasks == null) {
			tasks = new LinkedHashSet<BOTask>();
		}
		return tasks;
	}

	public void setTasks(Set<BOTask> tasks) {
		this.tasks = tasks;
	}

	public List<BOUser> getContactUsers() {
		if (contactUsers == null) {
			contactUsers = new ArrayList<BOUser>();
		}
		return contactUsers;
	}

	public void setContactUsers(List<BOUser> contactUsers) {
		this.contactUsers = contactUsers;
	}

	public void setContactUsersString(String strContactUsers) {
		contactStringUsers = strContactUsers;
	}

	public void loadContactUsers() {
		if (contactStringUsers == null || contactStringUsers.trim().length() <= 0) {
			return;
		}
		contactUsers = UserManagerFactory.getUserManager().getUsers(contactStringUsers);
	}

	public String getContactUsersString() {
		StringBuilder strReturn = new StringBuilder();

		if (contactUsers != null) {
			for (BOUser contact : contactUsers) {
				if (strReturn.length() > 0) {
					strReturn.append(",");
				}
				strReturn.append(contact.getUserID());
			}
		}
		return strReturn.toString();
	}

	public DateRange getProjectRange() {
		return projectRange;
	}

	public void setProjectRange(DateRange projectRange) {
		this.projectRange = projectRange;
	}

	/**
	 * @return
	 */
	public Date getProjectStartDate() {
		return this.projectRange.getStartDate();
	}

	public void setProjectStartDate(Date startDate) {
		this.projectRange.setStartDate(startDate);
	}

	/**
	 * @return
	 */
	public Date getProjectEndDate() {
		return this.projectRange.getEndDate();
	}

	public void setProjectEndDate(Date endDate) {
		this.projectRange.setEndDate(endDate);
	}

	public Priority getProjectPriority() {
		return projectPriority;
	}

	public void setProjectPriority(Priority projectPriority) {
		this.projectPriority = projectPriority;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	/**
	 * @return the projectCompany
	 */
	public BOCompany getProjectCompany() {
		return projectCompany;
	}

	/**
	 * @param projectCompany
	 *            the projectCompany to set
	 */
	public void setProjectCompany(BOCompany projectCompany) {
		this.projectCompany = projectCompany;
	}

	/**
	 * @return the projectDepartment
	 */
	public Set<BODepartment> getProjectDepartments() {
		if (projectDepartments == null) {
			projectDepartments = new LinkedHashSet<BODepartment>();
		}
		return projectDepartments;
	}

	/**
	 * @param projectDepartment
	 *            the projectDepartment to set
	 */
	public void setProjectDepartments(Set<BODepartment> projectDepartments) {
		this.projectDepartments = projectDepartments;
	}

	/**
	 * @return the projectType
	 */
	public ProjectType getProjectType() {
		return projectType;
	}

	/**
	 * @param projectType
	 *            the projectType to set
	 */
	public void setProjectType(ProjectType projectType) {
		this.projectType = projectType;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof BOProject)) {
			return false;
		}
		return getProjectID() == ((BOProject) obj).getProjectID();
	}

	@Override
	public int hashCode() {
		return (int) getProjectID();
	}

	public boolean canViewProject(BOUser user) {
		Set<BOTask> taskSet = tasks;
		for (Iterator taskIterator = taskSet.iterator(); taskIterator.hasNext();) {
			BOTask currentTask = (BOTask) taskIterator.next();
			if (currentTask.canAddTaskLog(user)) {
				return true;
			}
		}

		return false;
	}

	public boolean canSaveProjectInfo(BOUser currentUser, BOUser oldUser) {
		// System.out.println("BOProject.canSaveProjectInfo() oldUser " +
		// oldUser);
		// System.out.println("BOProject.canSaveProjectInfo() oldUser " +
		// currentUser);

		if (oldUser == null) {
			return true;
		}
		if (oldUser.getUserID() == currentUser.getUserID()) {
			return true;
		} else {
			return false;
		}
	}

	// TODO send the parent task, if parent task is null then check for project
	// ownership
	public boolean canAddTasks(BOUser user) {
		if (projectOwner.getUserID() == user.getUserID()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean canEditProjectDataOtherThanTasks(BOUser user) {
		if (projectOwner.getUserID() == user.getUserID()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean canDeleteProject(BOUser user) {
		// TODO should the admin be allowed to delete a project ... I think not
		// (user.getUserType()==UserType.Administrator)
		if (projectOwner.getUserID() == user.getUserID()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean canSaveProjectTasks(BOUser user) {
		if (tasks != null) {
			Iterator<BOTask> taskIterator = tasks.iterator();
			while (taskIterator.hasNext()) {
				BOTask task = taskIterator.next();
				if (task.canBeEdited(user)) {
					return true;
				}
			}
			return false;
		}

		else if (projectOwner.getUserID() == user.getUserID()) {
			return true;
		}

		else {
			return false;
		}
	}

	/**
	 * @return the currentHistoryForProject
	 */
	public ArrayList<BOHistory> getCurrentHistoryForProject() {
		return currentHistoryForProject;
	}

	/**
	 * @param currentHistoryForProject
	 *            the currentHistoryForProject to set
	 */
	public void setCurrentHistoryForProject(ArrayList<BOHistory> currentHistoryForProject) {
		this.currentHistoryForProject = currentHistoryForProject;
	}
}