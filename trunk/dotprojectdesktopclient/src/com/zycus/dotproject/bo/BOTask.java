package com.zycus.dotproject.bo;

import static com.zycus.dotproject.bo.BOCustomField.CustomTaskModule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zycus.dotproject.factory.UserManagerFactory;

public class BOTask implements Serializable, Comparable<BOTask> {
	private static final long			serialVersionUID	= 1L;

	// TODO task_custom field has not been created or mapped since it is
	// longtext

	private long						taskID				= 0L;
	private long						enhancedTaskID		= 0L;
	private String						taskName			= null;
	private BOTask						parentTask			= null;
	private TaskMilestone				taskMilestone		= TaskMilestone.NotAMilestone;
	private BOProject					project				= null;
	private BOUser						owner				= null;
	private DateRange					taskRange			= new DateRange();
	private float						duration			= 0L;
	private TaskDurationType			taskDurationType	= TaskDurationType.Days;
	private float						taskHoursWorked		= 0;
	private TaskStatus					taskStatus			= TaskStatus.Active;
	private Priority					taskPriority		= Priority.Normal;
	private float						percentageCompleted	= 0L;
	private String						taskDescription		= null;
	private float						taskBudget			= 0L;
	private String						taskRelatedUrl		= null;
	private BOUser						taskCreator			= null;
	private int							taskOrder			= 0;
	private TaskClientPublish			taskClientPublish	= TaskClientPublish.DoNotPublish;
	private TaskDynamic					taskDynamic			= TaskDynamic.dependencyCheckingOn;
	private TaskAccess					taskAccess			= TaskAccess.Public;
	private TaskNotify					taskNotify			= TaskNotify.DoNotNotify;
	private String						taskDepartments		= null;
	private TaskType					taskType			= TaskType.Unknown;

	private Set<BOTask>					childTasks			= null;
	private Set<BOUser>					assineeUsers		= null;
	private Set<BOTask>					previousTasks		= null;
	private Set<BOTaskLog>				taskLogs			= null;
	private Set<BOTask>					dependentTasks		= new LinkedHashSet<BOTask>();
	private String						taskModule			= null;
	private List<BOUser>				contactUsers		= null;
	private Set<BOFile>					files				= null;

	private Map<BOCustomField, String>	customFieldValues	= null;
	
	
	public Set<BOFile> getFiles() {
		if(files == null) {
			files = new LinkedHashSet<BOFile>();
		}
		return files;
	}
	
	public void setFiles(Set<BOFile> files) {
		this.files = files;
	}
	
	public void addFile(BOFile file) {
		getFiles().add(file);
	}
	
	public void removeFile(BOFile file) {
		getFiles().remove(file);
	}

	public Map<BOCustomField, String> getCustomFieldValues() {
		return customFieldValues;
	}

	public void setCustomFieldValues(Map<BOCustomField, String> customFieldValues) {
		this.customFieldValues = customFieldValues;
	}

	public String getTaskModule() {
		if (customFieldValues != null && customFieldValues.size() > 0) {
			// return customFieldValues.get(CustomTaskModule);
			return customFieldValues.get(customFieldValues.keySet().toArray()[0]);
		}
		return taskModule;
	}

	public void setTaskModule(String taskModule) {
		this.taskModule = taskModule;
		if (customFieldValues == null) {
			customFieldValues = new LinkedHashMap<BOCustomField, String>();
		}
		// this is HotFix. needs to be made proper
		if (customFieldValues.size() > 0) {
			customFieldValues.put(customFieldValues.keySet().toArray(new BOCustomField[] {})[0], taskModule);
		} else {
			customFieldValues.put(CustomTaskModule, taskModule);
		}
		/*
		 * if(CustomTaskModule != null) {
		 * customFieldValues.put(CustomTaskModule, taskModule); }
		 */
	}

	public Set<BOTask> getDependentTasks() {
		return dependentTasks;
	}

	public void setDependentTasks(Set<BOTask> dependentTasks) {
		this.dependentTasks = dependentTasks;
	}

	public int compareTo(BOTask o) {
		return taskName.compareTo(o.taskName);
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Task " + taskID + " starts ");
		buffer.append("\n\ttaskName->" + taskName);
		buffer.append("\n\tstart date->" + getStartDate());
		buffer.append("\n\tduration->" + duration);
		buffer.append("\n\tend date->" + getEndDate());
		buffer.append("Task " + taskID + " ends ");
		return taskName;
	}

	public String toLongString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Task " + taskID + " starts ");
		buffer.append("\n\ttaskName->" + taskName);
		buffer.append("\n\tstart date->" + getStartDate());
		buffer.append("\n\tduration->" + duration);
		buffer.append("\n\tend date->" + getEndDate());
		buffer.append("Task " + taskID + " ends ");

		return buffer.toString();
	}

	/**
	 * @return the taskID
	 */
	public long getTaskID() {
		return taskID;
	}

	/**
	 * @param taskID
	 *            the taskID to set
	 */
	public void setTaskID(long taskID) {
		this.taskID = taskID;
		//System.out.println("taskID : " + taskID);
	}

	/**
	 * @return the enhancedTaskID
	 */
	public long getEnhancedTaskID() {
		if (taskID == 0L) {
			return enhancedTaskID;
		}
		return taskID;
	}

	/**
	 * @param enhancedTaskID
	 *            the enhancedTaskID to set
	 */
	public void setEnhancedTaskID(long enhancedTaskID) {
		this.enhancedTaskID = enhancedTaskID;
	}

	/**
	 * @return the taskName
	 */
	public String getTaskName() {
		return taskName;
	}

	/**
	 * @param taskName
	 *            the taskName to set
	 */
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	/**
	 * @return the parentTask
	 */
	public BOTask getParentTask() {
		return parentTask;
	}

	/**
	 * @param parentTask
	 *            the parentTask to set
	 */
	public void setParentTask(BOTask parentTask) {
		this.parentTask = parentTask;
	}

	/**
	 * @return the taskMilestone
	 */
	public TaskMilestone getTaskMilestone() {
		return taskMilestone;
	}

	/**
	 * @param taskMilestone
	 *            the taskMilestone to set
	 */
	public void setTaskMilestone(TaskMilestone taskMilestone) {
		this.taskMilestone = taskMilestone;
	}

	/**
	 * @return the project
	 */
	public BOProject getProject() {
		return project;
	}

	/**
	 * @param project
	 *            the project to set
	 */
	public void setProject(BOProject project) {
		this.project = project;
	}

	/**
	 * @return the owner
	 */
	public BOUser getOwner() {
		return owner;
	}

	/**
	 * @param owner
	 *            the owner to set
	 */
	public void setOwner(BOUser owner) {
		this.owner = owner;
	}

	/**
	 * @return the taskRange
	 */
	public DateRange getTaskRange() {
		return taskRange;
	}

	/**
	 * @param taskRange
	 *            the taskRange to set
	 */
	public void setTaskRange(DateRange taskRange) {
		this.taskRange = taskRange;
	}

	/**
	 * @return the duration
	 */
	public float getDuration() {
		return duration;
	}

	/**
	 * @param duration
	 *            the duration to set
	 */
	public void setDuration(float duration) {
		this.duration = duration;
	}

	public float getDurationInDays() {

		float durationInDays = duration;
		if (taskDurationType == TaskDurationType.Hours) {
			durationInDays = DateRange.getDurationInDays(duration);
		}
		return durationInDays;
	}

	public void setDurationInDays(float durationInDays) {

		float durationInDaysOrHours = durationInDays;
		if (taskDurationType == TaskDurationType.Hours) {
			durationInDaysOrHours = DateRange.getDurationInHoursCeiled(durationInDays);
		}
		setDuration(durationInDaysOrHours);
	}

	/**
	 * @return the taskDurationType
	 */
	public TaskDurationType getTaskDurationType() {
		return taskDurationType;
	}

	/**
	 * @param taskDurationType
	 *            the taskDurationType to set
	 */
	public void setTaskDurationType(TaskDurationType taskDurationType) {
		this.taskDurationType = taskDurationType;
	}

	/**
	 * @return the taskHoursWorked
	 */
	public float getTaskHoursWorked() {
		return taskHoursWorked;
	}

	/**
	 * @param taskHoursWorked
	 *            the taskHoursWorked to set
	 */
	public void setTaskHoursWorked(float taskHoursWorked) {
		this.taskHoursWorked = taskHoursWorked;
	}

	/**
	 * @return the taskStatus
	 */
	public TaskStatus getTaskStatus() {
		return taskStatus;
	}

	/**
	 * @param taskStatus
	 *            the taskStatus to set
	 */
	public void setTaskStatus(TaskStatus taskStatus) {
		this.taskStatus = taskStatus;
	}

	/**
	 * @return the taskPriority
	 */
	public Priority getTaskPriority() {
		return taskPriority;
	}

	/**
	 * @param taskPriority
	 *            the taskPriority to set
	 */
	public void setTaskPriority(Priority taskPriority) {
		this.taskPriority = taskPriority;
	}

	/**
	 * @return the percentageCompleted
	 */
	public float getPercentageCompleted() {
		return percentageCompleted;
	}

	/**
	 * @param percentageCompleted
	 *            the percentageCompleted to set
	 */
	public void setPercentageCompleted(float percentageCompleted) {
		this.percentageCompleted = percentageCompleted;
	}

	/**
	 * @return the taskDescription
	 */
	public String getTaskDescription() {
		return taskDescription;
	}

	/**
	 * @param taskDescription
	 *            the taskDescription to set
	 */
	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}

	/**
	 * @return the taskBudget
	 */
	public float getTaskBudget() {
		return taskBudget;
	}

	/**
	 * @param taskBudget
	 *            the taskBudget to set
	 */
	public void setTaskBudget(float taskBudget) {
		this.taskBudget = taskBudget;
	}

	/**
	 * @return the taskRelatedUrl
	 */
	public String getTaskRelatedUrl() {
		return taskRelatedUrl;
	}

	/**
	 * @param taskRelatedUrl
	 *            the taskRelatedUrl to set
	 */
	public void setTaskRelatedUrl(String taskRelatedUrl) {
		this.taskRelatedUrl = taskRelatedUrl;
	}

	/**
	 * @return the taskCreator
	 */
	public BOUser getTaskCreator() {
		return taskCreator;
	}

	/**
	 * @param taskCreator
	 *            the taskCreator to set
	 */
	public void setTaskCreator(BOUser taskCreator) {
		this.taskCreator = taskCreator;
	}

	/**
	 * @return the taskOrder
	 */
	public int getTaskOrder() {
		return taskOrder;
	}

	/**
	 * @param taskOrder
	 *            the taskOrder to set
	 */
	public void setTaskOrder(int taskOrder) {
		this.taskOrder = taskOrder;
	}

	/**
	 * @return the taskClientPublish
	 */
	public TaskClientPublish getTaskClientPublish() {
		return taskClientPublish;
	}

	/**
	 * @param taskClientPublish
	 *            the taskClientPublish to set
	 */
	public void setTaskClientPublish(TaskClientPublish taskClientPublish) {
		this.taskClientPublish = taskClientPublish;
	}

	/**
	 * @return the taskDynamic
	 */
	public TaskDynamic getTaskDynamic() {
		return taskDynamic;
	}

	/**
	 * @param taskDynamic
	 *            the taskDynamic to set
	 */
	public void setTaskDynamic(TaskDynamic taskDynamic) {
		this.taskDynamic = taskDynamic;
	}

	/**
	 * @return the taskAccess
	 */
	public TaskAccess getTaskAccess() {
		return taskAccess;
	}

	/**
	 * @param taskAccess
	 *            the taskAccess to set
	 */
	public void setTaskAccess(TaskAccess taskAccess) {
		this.taskAccess = taskAccess;
	}

	/**
	 * @return the taskNotify
	 */
	public TaskNotify getTaskNotify() {
		return taskNotify;
	}

	/**
	 * @param taskNotify
	 *            the taskNotify to set
	 */
	public void setTaskNotify(TaskNotify taskNotify) {
		this.taskNotify = taskNotify;
	}

	/**
	 * @return the taskDepartments
	 */
	public String getTaskDepartments() {
		return taskDepartments;
	}

	/**
	 * @param taskDepartments
	 *            the taskDepartments to set
	 */
	public void setTaskDepartments(String taskDepartments) {
		this.taskDepartments = taskDepartments;
	}

	/**
	 * @return the taskType
	 */
	public TaskType getTaskType() {
		return taskType;
	}

	/**
	 * @param taskType
	 *            the taskType to set
	 */
	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}

	/**
	 * @return the childTasks
	 */
	public Set<BOTask> getChildTasks() {
		if (childTasks == null) {
			childTasks = new LinkedHashSet<BOTask>();
		}
		return childTasks;
	}

	/**
	 * @param childTasks
	 *            the childTasks to set
	 */
	public void setChildTasks(Set<BOTask> childTasks) {
		this.childTasks = childTasks;
	}

	/**
	 * @return the assineeUsers
	 */
	public Set<BOUser> getAssineeUsers() {
		if (assineeUsers == null) {
			assineeUsers = new LinkedHashSet<BOUser>();
		}
		return assineeUsers;
	}

	/**
	 * @param assineeUsers
	 *            the assineeUsers to set
	 */
	public void setAssineeUsers(Set<BOUser> assineeUsers) {
		this.assineeUsers = assineeUsers;
	}

	/**
	 * @return the previousTasks
	 */
	public Set<BOTask> getPreviousTasks() {
		if (previousTasks == null) {
			previousTasks = new LinkedHashSet<BOTask>();
		}
		return previousTasks;
	}

	/**
	 * @param previousTasks
	 *            the previousTasks to set
	 */
	public void setPreviousTasks(Set<BOTask> previousTasks) {

		this.previousTasks = previousTasks;
	}

	/**
	 * @return the taskLogs
	 */
	public Set<BOTaskLog> getTaskLogs() {
		if (taskLogs == null) {
			taskLogs = new LinkedHashSet<BOTaskLog>();
		}
		return taskLogs;
	}

	/**
	 * @param taskLogs
	 *            the taskLogs to set
	 */
	public void setTaskLogs(Set<BOTaskLog> taskLogs) {
		this.taskLogs = taskLogs;
	}

	/**
	 * @return
	 */
	public Date getStartDate() {
		return this.taskRange.getStartDate();
	}

	public void setStartDate(Date startDate) {
		this.taskRange.setStartDate(startDate);
	}

	/**
	 * @return
	 */
	public Date getEndDate() {
		return this.taskRange.getEndDate();
	}

	public void setEndDate(Date endDate) {
		this.taskRange.setEndDate(endDate);
	}

	/**
	 * @return the serialVersionUID
	 */
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof BOTask)) {
			return false;
		}
		return getEnhancedTaskID() == ((BOTask) obj).getEnhancedTaskID();
	}

	@Override
	public int hashCode() {
		return (int) getEnhancedTaskID();
	}

	public boolean canBeEdited(BOUser user) {
		if (user.getUserID() == project.getProjectOwner().getUserID()) {
			return true;
		}else {
			if (user.getUserID() == owner.getUserID()) {
				return true;
			}

			BOTask currenTask = this;
			BOTask currParentTask = parentTask;
			while (currParentTask != null && currParentTask.getEnhancedTaskID() != currenTask.getEnhancedTaskID()) {
				if (user.getUserID() == currParentTask.getOwner().getUserID()) {
					return true;
				}
				currenTask = currParentTask;
				currParentTask = currParentTask.getParentTask();
			}
		}
		return canUpsertChild(this, user);
	}
	
	private boolean canUpsertChild(BOTask a_currenTask, BOUser user) {
		boolean canUpsertTask = false;
		for(BOTask task : a_currenTask.getChildTasks()) {
			if(task.getOwner().getUserID() == user.getUserID()) {
				return true;
			}
			if(user.isContainedIn(task.getAssineeUsers())) {
				return true;
			}
			if(task.getChildTasks().isEmpty() == false) {
				canUpsertTask =  canUpsertChild(task, user);
			}
			if(canUpsertTask) {
				break;
			}
		}
		return canUpsertTask;
	}

	public boolean canAddTaskLog(BOUser user) {
		if (user.isContainedIn(assineeUsers) || canBeEdited(user)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean canBeDeleted(BOUser user) {
		if (user.getUserID() == project.getProjectOwner().getUserID()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean canAddTasks(BOUser user) {
		return canBeEdited(user);
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
		if (strContactUsers == null || strContactUsers.trim().length() <= 0) {
			return;
		}
		contactUsers = UserManagerFactory.getUserManager().getUsers(strContactUsers);
	}

	public String getContactUsersString() {
		if (contactUsers == null) {
			return "";
		}
		StringBuilder strReturn = new StringBuilder();
		for (BOUser contact : contactUsers) {
			if (strReturn.length() > 0) {
				strReturn.append(",");
			}
			strReturn.append(contact.getUserID());
		}
		return strReturn.toString();
	}

	public boolean isContainedIn(Collection c) {
		ArrayList list = new ArrayList(c);
		return list.contains(this);
	}
	
	public TaskCompletionStatus getTaskCompletionStatus() {
		Date currentDate = new Date();
		if(getPercentageCompleted() == 100) {
			return TaskCompletionStatus.Completed;
		}
		
		if(getPercentageCompleted() <= 0) {
			if(getEndDate().before(currentDate)) {
				return TaskCompletionStatus.Late;
			}
			
			if(getStartDate().before(currentDate) && getEndDate().after(currentDate)) {
				return TaskCompletionStatus.ShouldHaveStarted;
			}
		}
		
		if(getPercentageCompleted() < 100 && getPercentageCompleted() > 0) {
			if(getEndDate().after(currentDate)) {
				return TaskCompletionStatus.OnTimeGoing;
			}
			if(getEndDate().before(currentDate)) {
				return TaskCompletionStatus.Late;
			}
		}
		
		
		return TaskCompletionStatus.Normal;
	}
}