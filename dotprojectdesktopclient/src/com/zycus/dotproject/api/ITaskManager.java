package com.zycus.dotproject.api;

import java.util.List;

import com.zycus.dotproject.bo.BOProject;
import com.zycus.dotproject.bo.BOTask;
import com.zycus.dotproject.bo.BOUser;
import com.zycus.dotproject.bo.DateRange;
import com.zycus.dotproject.bo.TaskStatus;

public interface ITaskManager {
	
	void saveTask(BOUser user, BOTask task);
	void deleteTask(BOUser user, BOTask task);

	List<BOTask> getAllTasks(BOUser user);
	List<BOTask> getAllTasks(BOUser user, BOProject project);
	
	List<BOTask> getAllTasks(BOUser user, TaskStatus taskStatus);	
	List<BOTask> getAllTasks(BOUser user, TaskStatus taskStatus, BOProject project);
	
	List<BOTask> getAllTasks(BOUser user, TaskStatus taskStatus, DateRange dateRange);
	List<BOTask> getAllTasks(BOUser user, TaskStatus taskStatus, DateRange dateRange, BOProject project);
}