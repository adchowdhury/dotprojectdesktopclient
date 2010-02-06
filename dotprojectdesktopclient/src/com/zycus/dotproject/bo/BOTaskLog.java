package com.zycus.dotproject.bo;

import java.io.Serializable;
import java.util.Date;

public class BOTaskLog implements Comparable<BOTaskLog>, Serializable {

	private static final long	serialVersionUID	= 1L;

	private long				taskLogID			= 0L;
	private long				enhancedTaskLogID	= 0L;
	private BOTask				taskLogTask			= null;
	private String				taskLogName			= null;
	private String				taskLogDescription	= null;
	private BOUser				taskLogCreator		= null;
	private float				taskLogHours		= 0L;
	private Date				taskLogDate			= null;
	private String				taskCostCode		= null;
	private TaskLogProblem		taskLogProblem		= TaskLogProblem.NotAProblem;
	private TaskLogReference	taskLogReference	= TaskLogReference.NotDefined;
	
	public long getEnhancedTaskLogID() {
		if(taskLogID==0L)
		{
			return enhancedTaskLogID;
		}
		return taskLogID;
	}
	
	public void setEnhancedTaskLogID(long enhancedTaskLogID) {
		this.enhancedTaskLogID = enhancedTaskLogID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(BOTaskLog o) {
		return taskLogName.compareTo(o.taskLogName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getTaskLogName();
	}

	/**
	 * @return
	 */
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	/**
	 * @return the taskLogId
	 */
	public long getTaskLogID() {
		return taskLogID;
	}

	/**
	 * @param taskLogId
	 *            the taskLogId to set
	 */
	public void setTaskLogID(long taskLogID) {
		this.taskLogID = taskLogID;
	}

	/**
	 * @return the taskLogTask
	 */
	public BOTask getTaskLogTask() {
		return taskLogTask;
	}

	/**
	 * @param taskLogTask
	 *            the taskLogTask to set
	 */
	public void setTaskLogTask(BOTask taskLogTask) {
		this.taskLogTask = taskLogTask;
	}

	/**
	 * @return the taskLogName
	 */
	public String getTaskLogName() {
		return taskLogName;
	}

	/**
	 * @param taskLogName
	 *            the taskLogName to set
	 */
	public void setTaskLogName(String taskLogName) {
		this.taskLogName = taskLogName;
	}

	/**
	 * @return the taskLogDescription
	 */
	public String getTaskLogDescription() {
		return taskLogDescription;
	}

	/**
	 * @param taskLogDescription
	 *            the taskLogDescription to set
	 */
	public void setTaskLogDescription(String taskLogDescription) {
		this.taskLogDescription = taskLogDescription;
	}

	/**
	 * @return the taskLogCreator
	 */
	public BOUser getTaskLogCreator() {
		return taskLogCreator;
	}

	/**
	 * @param taskLogCreator
	 *            the taskLogCreator to set
	 */
	public void setTaskLogCreator(BOUser taskLogCreator) {
		this.taskLogCreator = taskLogCreator;
	}

	/**
	 * @return the taskLogHours
	 */
	public float getTaskLogHours() {
		return taskLogHours;
	}

	/**
	 * @param taskLogHours
	 *            the taskLogHours to set
	 */
	public void setTaskLogHours(float taskLogHours) {
		this.taskLogHours = taskLogHours;
	}

	/**
	 * @return the taskLogDate
	 */
	public Date getTaskLogDate() {
		return taskLogDate;
	}

	/**
	 * @param taskLogDate
	 *            the taskLogDate to set
	 */
	public void setTaskLogDate(Date taskLogDate) {
		this.taskLogDate = taskLogDate;
	}

	/**
	 * @return the taskCostCode
	 */
	public String getTaskCostCode() {
		return taskCostCode;
	}

	/**
	 * @param taskCostCode
	 *            the taskCostCode to set
	 */
	public void setTaskCostCode(String taskCostCode) {
		this.taskCostCode = taskCostCode;
	}

	/**
	 * @return the taskLogProblem
	 */
	public TaskLogProblem getTaskLogProblem() {
		return taskLogProblem;
	}

	/**
	 * @param taskLogProblem
	 *            the taskLogProblem to set
	 */
	public void setTaskLogProblem(TaskLogProblem taskLogProblem) {
		this.taskLogProblem = taskLogProblem;
	}

	/**
	 * @return the taskLogReference
	 */
	public TaskLogReference getTaskLogReference() {
		return taskLogReference;
	}

	/**
	 * @param taskLogReference
	 *            the taskLogReference to set
	 */
	public void setTaskLogReference(TaskLogReference taskLogReference) {
		this.taskLogReference = taskLogReference;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof BOTaskLog)) {
			return false;
		}
		return getEnhancedTaskLogID() == ((BOTaskLog) obj).getEnhancedTaskLogID();
	}

	@Override
	public int hashCode() {
		return (int) getEnhancedTaskLogID();
	}
}